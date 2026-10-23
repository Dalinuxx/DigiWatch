package com.digiwear.wear.watchface

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Typeface
import android.os.BatteryManager
import android.view.SurfaceHolder
import androidx.wear.watchface.CanvasType
import androidx.wear.watchface.ComplicationSlot
import androidx.wear.watchface.ComplicationSlotsManager
import androidx.wear.watchface.DrawMode
import androidx.wear.watchface.Renderer
import androidx.wear.watchface.TapEvent
import androidx.wear.watchface.TapType
import androidx.wear.watchface.WatchFace
import androidx.wear.watchface.WatchFaceService
import androidx.wear.watchface.WatchFaceType
import androidx.wear.watchface.WatchState
import androidx.wear.watchface.style.CurrentUserStyleRepository
import com.digiwear.core.model.BackgroundTheme
import com.digiwear.core.model.CustomThemeConfig
import com.digiwear.core.model.PetSpecies
import com.digiwear.core.model.PetState
import com.digiwear.core.model.SpriteFrame
import com.digiwear.wear.MainActivity
import com.digiwear.wear.data.PetRepository
import com.digiwear.wear.haptic.HapticManager
import com.digiwear.wear.render.PixelArtMatrices
import com.digiwear.wear.sound.BuzzerManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Standalone Wear OS Interactive Watch Face Service for DigiWear.
 *
 * Implements AndroidX WatchFace with custom CanvasRenderer2:
 * - Interactive Mode:
 *   - Authentic 7-segment retro LCD digital clock typography with inactive shadow segments (88:88).
 *   - Active partner sprite animated with idle/walk cycles (1 Hz) or joyful double-hop bounce emotes.
 *   - Physiological hunger and strength heart gauges.
 *   - Battery percentage with safe fallback to ACTION_BATTERY_CHANGED (no integer underflow).
 *   - Total adventure step count vs daily step goal.
 *   - Derived theme palette supporting both built-in themes and custom user LCD matrices.
 * - Dynamic User Interaction:
 *   - Single tap: Triggers partner dual-hop bounce animation & joy heart emote with haptic vibration and chime.
 *   - Double tap: Launches MainActivity for full pet management.
 *   - Ambient mode tap suppression to prevent accidental reactions in sleep/AOD state.
 * - Ambient Mode: Low-power monochrome dimmed retro outline rendering with pure black OLED
 *   background (OPR <= 15%) to prevent display burn-in and maximize battery life.
 * - Zero Resource Leaks: Guarantees full AudioTrack & tone release on service/renderer teardown.
 */
class DigiWatchFaceService : WatchFaceService() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private var buzzerManager: BuzzerManager? = null
    private var lastTapTimestampMs: Long = 0L

    override fun onDestroy() {
        serviceScope.cancel()
        buzzerManager?.release()
        buzzerManager = null
        super.onDestroy()
    }

    override suspend fun createWatchFace(
        surfaceHolder: SurfaceHolder,
        watchState: WatchState,
        complicationSlotsManager: ComplicationSlotsManager,
        currentUserStyleRepository: CurrentUserStyleRepository
    ): WatchFace {
        val repository = PetRepository.getInstance(applicationContext)
        val hapticManager = HapticManager(applicationContext)
        val bm = BuzzerManager().apply {
            isEnabled = repository.soundEnabledFlow.value
            soundpack = repository.soundpackFlow.value
        }
        buzzerManager = bm

        val renderer = DigiCanvasRenderer(
            context = applicationContext,
            surfaceHolder = surfaceHolder,
            currentUserStyleRepository = currentUserStyleRepository,
            watchState = watchState,
            repository = repository,
            hapticManager = hapticManager,
            buzzerManager = bm
        )

        val watchFace = WatchFace(
            watchFaceType = WatchFaceType.DIGITAL,
            renderer = renderer
        )

        // Observe repository state updates and trigger render invalidations
        serviceScope.launch {
            repository.petStateFlow.collect {
                renderer.updatePetState(it)
                renderer.invalidate()
            }
        }

        serviceScope.launch {
            repository.themeFlow.collect {
                renderer.updateTheme(it)
                renderer.invalidate()
            }
        }

        serviceScope.launch {
            repository.customThemeConfigFlow.collect {
                renderer.updateCustomThemeConfig(it)
                renderer.invalidate()
            }
        }

        serviceScope.launch {
            repository.clockFormatFlow.collect {
                renderer.updateClockFormat(it)
                renderer.invalidate()
            }
        }

        serviceScope.launch {
            repository.dailyStepGoalFlow.collect {
                renderer.updateStepGoal(it)
                renderer.invalidate()
            }
        }

        serviceScope.launch {
            repository.adventureStateFlow.collect {
                renderer.updateAdventureSteps(it.totalAdventureSteps)
                renderer.invalidate()
            }
        }

        serviceScope.launch {
            repository.ambientStyleFlow.collect {
                renderer.updateAmbientStyle(it)
                renderer.invalidate()
            }
        }

        serviceScope.launch {
            repository.soundEnabledFlow.collect {
                bm.isEnabled = it
            }
        }

        serviceScope.launch {
            repository.soundpackFlow.collect {
                bm.soundpack = it
            }
        }

        // Tap listener: single tap = partner bounce, double tap = launch MainActivity
        watchFace.setTapListener(object : WatchFace.TapListener {
            override fun onTapEvent(tapType: Int, tapEvent: TapEvent, complicationSlot: ComplicationSlot?) {
                // Ignore taps when screen is sleeping / ambient to prevent unintentional wake reactions
                if (watchState.isAmbient.value == true) {
                    return
                }

                if (tapType == TapType.UP) {
                    val now = System.currentTimeMillis()
                    if (now - lastTapTimestampMs < DOUBLE_TAP_THRESHOLD_MS) {
                        // Double Tap: Launch MainActivity
                        lastTapTimestampMs = 0L
                        renderer.cancelBounce()
                        val launchIntent = Intent(applicationContext, MainActivity::class.java).apply {
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                        }
                        startActivity(launchIntent)
                    } else {
                        // Single Tap: Trigger partner bounce emote
                        lastTapTimestampMs = now
                        renderer.triggerBounce(serviceScope)
                    }
                }
            }
        })

        return watchFace
    }

    companion object {
        private const val DOUBLE_TAP_THRESHOLD_MS = 380L
    }
}

class DigiSharedAssets : Renderer.SharedAssets {
    override fun onDestroy() {}
}

/**
 * Custom Hardware Canvas Renderer for DigiWatchFaceService.
 */
class DigiCanvasRenderer(
    private val context: Context,
    surfaceHolder: SurfaceHolder,
    currentUserStyleRepository: CurrentUserStyleRepository,
    watchState: WatchState,
    private val repository: PetRepository,
    private val hapticManager: HapticManager,
    private val buzzerManager: BuzzerManager
) : Renderer.CanvasRenderer2<DigiSharedAssets>(
    surfaceHolder = surfaceHolder,
    currentUserStyleRepository = currentUserStyleRepository,
    watchState = watchState,
    canvasType = CanvasType.HARDWARE,
    interactiveDrawModeUpdateDelayMillis = 1000L,
    clearWithBackgroundTintBeforeRenderingHighlightLayer = false
) {

    @Volatile
    private var petState: PetState = PetState(speciesId = 101)

    @Volatile
    private var activeTheme: BackgroundTheme = BackgroundTheme.CLASSIC_VPET_LCD

    @Volatile
    private var customThemeConfig: CustomThemeConfig? = null

    @Volatile
    private var ambientStyle: String = "MINIMAL_DOT"

    @Volatile
    private var is24hFormat: Boolean = false

    @Volatile
    private var dailyStepGoal: Int = 5000

    @Volatile
    private var currentSteps: Int = 0

    @Volatile
    private var bounceUntilTimestampMs: Long = 0L

    private var bounceJob: Job? = null

    private val timeFormatter24 = DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())
    private val timeFormatter12 = DateTimeFormatter.ofPattern("hh:mm", Locale.getDefault())
    private val amPmFormatter = DateTimeFormatter.ofPattern("a", Locale.getDefault())

    private val bgPaint = Paint()
    private val dotPaint = Paint().apply {
        style = Paint.Style.FILL
        isAntiAlias = false
    }
    private val framePaint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 3f
        isAntiAlias = true
    }
    private val segmentPaint = Paint().apply {
        style = Paint.Style.FILL
        isAntiAlias = false
    }
    private val textPaint = Paint().apply {
        typeface = Typeface.MONOSPACE
        isAntiAlias = true
        textAlign = Paint.Align.CENTER
    }
    private val spritePaint = Paint().apply {
        isAntiAlias = false
        isFilterBitmap = false
        isDither = false
    }
    private val heartPaint = Paint().apply {
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    fun updatePetState(state: PetState) {
        petState = state
    }

    fun updateTheme(theme: BackgroundTheme) {
        activeTheme = theme
    }

    fun updateCustomThemeConfig(config: CustomThemeConfig?) {
        customThemeConfig = config
    }

    fun updateAmbientStyle(style: String) {
        ambientStyle = style
    }

    fun updateClockFormat(is24h: Boolean) {
        is24hFormat = is24h
    }

    fun updateStepGoal(goal: Int) {
        dailyStepGoal = goal
    }

    fun updateAdventureSteps(steps: Int) {
        currentSteps = steps
    }

    fun triggerBounce(scope: CoroutineScope) {
        val now = System.currentTimeMillis()
        bounceUntilTimestampMs = now + BOUNCE_DURATION_MS
        try {
            hapticManager.playStepMilestone()
        } catch (_: Exception) {}
        buzzerManager.playButtonBeep()
        invalidate()

        bounceJob?.cancel()
        bounceJob = scope.launch {
            val startTime = System.currentTimeMillis()
            while (isActive && (System.currentTimeMillis() - startTime) < BOUNCE_DURATION_MS) {
                delay(80L)
                invalidate()
            }
            bounceUntilTimestampMs = 0L
            invalidate()
        }
    }

    fun cancelBounce() {
        bounceJob?.cancel()
        bounceJob = null
        bounceUntilTimestampMs = 0L
        invalidate()
    }

    override fun onDestroy() {
        cancelBounce()
        buzzerManager.release()
        super.onDestroy()
    }

    override suspend fun createSharedAssets(): DigiSharedAssets = DigiSharedAssets()

    override fun renderHighlightLayer(
        canvas: Canvas,
        bounds: Rect,
        zonedDateTime: ZonedDateTime,
        sharedAssets: DigiSharedAssets
    ) {
        // Highlight layer unused for standalone digital face
    }

    override fun render(
        canvas: Canvas,
        bounds: Rect,
        zonedDateTime: ZonedDateTime,
        sharedAssets: DigiSharedAssets
    ) {
        val isAmbient = renderParameters.drawMode == DrawMode.AMBIENT
        val w = bounds.width().toFloat()
        val h = bounds.height().toFloat()
        val centerX = bounds.exactCenterX()
        val centerY = bounds.exactCenterY()

        if (isAmbient) {
            renderAmbientMode(canvas, bounds, zonedDateTime, centerX, centerY, w, h)
        } else {
            renderInteractiveMode(canvas, bounds, zonedDateTime, centerX, centerY, w, h)
        }
    }

    private fun renderAmbientMode(
        canvas: Canvas,
        bounds: Rect,
        zonedDateTime: ZonedDateTime,
        centerX: Float,
        centerY: Float,
        w: Float,
        h: Float
    ) {
        // Pure black background for OLED burn-in protection and <= 15% on-pixel ratio (OPR)
        canvas.drawColor(Color.BLACK)

        val tintColor = when (ambientStyle) {
            "DIMMED_LCD" -> Color.parseColor("#80CBC4")
            "OUTLINE" -> Color.parseColor("#E0E0E0")
            else -> Color.WHITE
        }

        // 1. Digital Clock (Authentic 7-Segment Retro Digits in Monochrome)
        val timeStr = if (is24hFormat) timeFormatter24.format(zonedDateTime) else timeFormatter12.format(zonedDateTime)
        val amPmStr = if (!is24hFormat) amPmFormatter.format(zonedDateTime) else ""
        val clockWidth = w * 0.52f
        val clockHeight = h * 0.16f
        val clockY = centerY - h * 0.22f

        draw7SegmentTime(
            canvas = canvas,
            timeStr = timeStr,
            cx = centerX,
            cy = clockY,
            clockWidth = clockWidth,
            clockHeight = clockHeight,
            activeColor = tintColor,
            shadowColor = Color.BLACK,
            drawGhost = false
        )

        if (amPmStr.isNotEmpty()) {
            textPaint.color = tintColor
            textPaint.textAlign = Paint.Align.LEFT
            textPaint.textSize = w * 0.04f
            canvas.drawText(amPmStr, centerX + (clockWidth * 0.54f), clockY - (clockHeight * 0.1f), textPaint)
            textPaint.textAlign = Paint.Align.CENTER
        }

        // 2. Monochrome Ambient Outline Sprite
        val ambientBitmap = PixelArtMatrices.getAndroidBitmap(petState.speciesId, SpriteFrame.IDLE_A, isAmbient = true)
        val spriteSize = (w * 0.34f).toInt()
        val spriteLeft = (centerX - spriteSize / 2f).toInt()
        val spriteTop = (centerY - spriteSize / 2f + h * 0.02f).toInt()
        val dstRect = Rect(spriteLeft, spriteTop, spriteLeft + spriteSize, spriteTop + spriteSize)
        canvas.drawBitmap(ambientBitmap, null, dstRect, spritePaint)

        // 3. Status Line
        textPaint.color = tintColor
        textPaint.textSize = w * 0.055f
        val statusText = when {
            petState.isDeceased -> "[RIP]"
            petState.attentionCallActive -> "[CALL]"
            petState.isSleeping -> "ZZZ"
            else -> "H:${petState.hunger}/4  S:${petState.strength}/4"
        }
        canvas.drawText(statusText, centerX, centerY + h * 0.28f, textPaint)

        // Minimal battery / steps
        val batteryPct = getBatteryPercentage(context)
        textPaint.color = Color.DKGRAY
        textPaint.textSize = w * 0.045f
        canvas.drawText("${currentSteps}st • ${batteryPct}%", centerX, centerY + h * 0.38f, textPaint)
    }

    private fun renderInteractiveMode(
        canvas: Canvas,
        bounds: Rect,
        zonedDateTime: ZonedDateTime,
        centerX: Float,
        centerY: Float,
        w: Float,
        h: Float
    ) {
        val now = System.currentTimeMillis()
        val isBouncing = now < bounceUntilTimestampMs
        val species = PetSpecies.getById(petState.speciesId)

        // Derive palette with Custom LCD Theme support
        val isCustom = activeTheme == BackgroundTheme.CUSTOM_MATRIX && customThemeConfig != null
        val baseLcdColor = if (isCustom) customThemeConfig!!.primaryColorHex.toInt() else activeTheme.primaryColorHex.toInt()
        val darkPixelColor = if (isCustom) customThemeConfig!!.secondaryColorHex.toInt() else activeTheme.secondaryColorHex.toInt()
        val gridDotColor = if (isCustom) customThemeConfig!!.gridColorHex.toInt() else activeTheme.gridColorHex.toInt()

        // 1. Fill Screen with LCD theme primary tint
        bgPaint.color = baseLcdColor
        canvas.drawRect(bounds, bgPaint)

        // 2. Subtle LCD Dot Matrix Grid Overlay
        dotPaint.color = gridDotColor
        dotPaint.alpha = 45
        val dotSpacing = 10f
        var x = 5f
        while (x < w) {
            var y = 5f
            while (y < h) {
                canvas.drawCircle(x, y, 1.2f, dotPaint)
                y += dotSpacing
            }
            x += dotSpacing
        }

        // 3. Inner LCD Screen Bezel Frame
        framePaint.color = darkPixelColor
        framePaint.alpha = 110
        val bezelMargin = w * 0.06f
        val bezelRect = RectF(bezelMargin, bezelMargin, w - bezelMargin, h - bezelMargin)
        canvas.drawRoundRect(bezelRect, 18f, 18f, framePaint)

        // 4. Top Header Status Bar: Battery • Partner Name • Step Target
        val batteryPct = getBatteryPercentage(context)
        textPaint.color = darkPixelColor
        textPaint.textSize = w * 0.042f
        textPaint.textAlign = Paint.Align.LEFT
        canvas.drawText("⚡${batteryPct}%", bezelMargin + 8f, bezelMargin + 18f, textPaint)

        textPaint.textAlign = Paint.Align.RIGHT
        canvas.drawText("${currentSteps}/${dailyStepGoal}st", w - bezelMargin - 8f, bezelMargin + 18f, textPaint)

        textPaint.textAlign = Paint.Align.CENTER
        textPaint.textSize = w * 0.046f
        canvas.drawText(species.name.uppercase(), centerX, bezelMargin + 18f, textPaint)

        // 5. Authentic 7-Segment Retro LCD Digital Clock with Ghost Segments
        val timeStr = if (is24hFormat) timeFormatter24.format(zonedDateTime) else timeFormatter12.format(zonedDateTime)
        val amPmStr = if (!is24hFormat) amPmFormatter.format(zonedDateTime) else ""
        val clockWidth = w * 0.54f
        val clockHeight = h * 0.17f
        val clockY = centerY - h * 0.17f

        draw7SegmentTime(
            canvas = canvas,
            timeStr = timeStr,
            cx = centerX,
            cy = clockY,
            clockWidth = clockWidth,
            clockHeight = clockHeight,
            activeColor = darkPixelColor,
            shadowColor = gridDotColor,
            drawGhost = true
        )

        if (amPmStr.isNotEmpty()) {
            textPaint.textAlign = Paint.Align.LEFT
            textPaint.textSize = w * 0.04f
            canvas.drawText(amPmStr, centerX + (clockWidth * 0.54f), clockY - (clockHeight * 0.1f), textPaint)
            textPaint.textAlign = Paint.Align.CENTER
        }

        // 6. Current Partner Sprite Animation (1 Hz Walking / Idle Cycle or Dynamic Dual-Hop Bounce Emote)
        val bounceProgress = if (isBouncing) {
            val elapsed = BOUNCE_DURATION_MS - (bounceUntilTimestampMs - now)
            (elapsed.toFloat() / BOUNCE_DURATION_MS).coerceIn(0f, 1f)
        } else 0f

        val hop = if (isBouncing) {
            kotlin.math.abs(kotlin.math.sin(bounceProgress * kotlin.math.PI * 2.0)).toFloat()
        } else 0f
        val bounceOffset = -16f * hop

        val activeFrame = when {
            isBouncing -> if (hop > 0.35f) SpriteFrame.JOY_EAT else SpriteFrame.IDLE_A
            petState.isInjured -> SpriteFrame.INJURED
            petState.isSleeping -> if ((zonedDateTime.second % 2) == 0) SpriteFrame.SLEEP_A else SpriteFrame.SLEEP_B
            (zonedDateTime.second % 2) == 0 -> SpriteFrame.IDLE_A
            else -> SpriteFrame.IDLE_B
        }

        val spriteBitmap = PixelArtMatrices.getAndroidBitmap(petState.speciesId, activeFrame, isAmbient = false)
        val spriteSize = (w * 0.32f).toInt()
        val spriteLeft = (centerX - spriteSize / 2f).toInt()
        val spriteTop = (centerY - spriteSize / 2f + h * 0.05f + bounceOffset).toInt()
        val dstRect = Rect(spriteLeft, spriteTop, spriteLeft + spriteSize, spriteTop + spriteSize)
        canvas.drawBitmap(spriteBitmap, null, dstRect, spritePaint)

        // If bouncing, draw a floating joy heart emote scaling above the partner
        if (isBouncing) {
            val heartOffset = bounceOffset - 10f - (bounceProgress * 6f)
            drawJoyHeart(canvas, centerX, (centerY - spriteSize / 2f + h * 0.05f) + heartOffset, darkPixelColor)
        }

        // 7. Physiological Hearts (Hunger & Strength)
        val metersY = centerY + h * 0.27f
        drawHeartRow(canvas, centerX - w * 0.22f, metersY, "HGR", petState.hunger.coerceIn(0, 4), darkPixelColor, gridDotColor)
        drawHeartRow(canvas, centerX + w * 0.04f, metersY, "STR", petState.strength.coerceIn(0, 4), darkPixelColor, gridDotColor)

        // 8. Bottom Information Line
        val bottomY = h - bezelMargin - 10f
        textPaint.textSize = w * 0.04f
        val bottomText = if (petState.isSleeping) {
            "ZZZ • LIGHTS ${if (petState.lightsOff) "OFF" else "ON"}"
        } else {
            "${species.stage.displayName} • ${petState.weight}g"
        }
        canvas.drawText(bottomText, centerX, bottomY, textPaint)
    }

    /**
     * Draws authentic 7-segment retro LCD digital clock time with ghost inactive segments.
     */
    private fun draw7SegmentTime(
        canvas: Canvas,
        timeStr: String,
        cx: Float,
        cy: Float,
        clockWidth: Float,
        clockHeight: Float,
        activeColor: Int,
        shadowColor: Int,
        drawGhost: Boolean
    ) {
        val chars = timeStr.toCharArray()
        val digitW = clockWidth * 0.19f
        val colonW = clockWidth * 0.08f
        val spacing = clockWidth * 0.035f
        val totalW = digitW * 4 + colonW + spacing * 4
        var curX = cx - totalW / 2f
        val curY = cy - clockHeight / 2f

        segmentPaint.color = shadowColor
        segmentPaint.style = Paint.Style.FILL

        // 1. Draw Ghost 88:88 background segments if requested
        if (drawGhost) {
            segmentPaint.alpha = 28
            var gX = curX
            for (i in 0 until 5) {
                if (i == 2) {
                    drawColon(canvas, gX, curY, colonW, clockHeight, segmentPaint)
                    gX += colonW + spacing
                } else {
                    draw7SegmentDigit(canvas, 127, gX, curY, digitW, clockHeight, segmentPaint)
                    gX += digitW + spacing
                }
            }
        }

        // 2. Draw Active Segments
        segmentPaint.color = activeColor
        segmentPaint.alpha = 255
        for (ch in chars) {
            when (ch) {
                ':' -> {
                    drawColon(canvas, curX, curY, colonW, clockHeight, segmentPaint)
                    curX += colonW + spacing
                }
                in '0'..'9' -> {
                    val mask = getDigitMask(ch)
                    draw7SegmentDigit(canvas, mask, curX, curY, digitW, clockHeight, segmentPaint)
                    curX += digitW + spacing
                }
                else -> {
                    curX += digitW + spacing
                }
            }
        }
    }

    private fun getDigitMask(ch: Char): Int = when (ch) {
        '0' -> 0x3F // a,b,c,d,e,f
        '1' -> 0x06 // b,c
        '2' -> 0x5B // a,b,g,e,d
        '3' -> 0x4F // a,b,g,c,d
        '4' -> 0x66 // f,g,b,c
        '5' -> 0x6D // a,f,g,c,d
        '6' -> 0x7D // a,f,g,e,c,d
        '7' -> 0x07 // a,b,c
        '8' -> 0x7F // a,b,c,d,e,f,g
        '9' -> 0x6F // a,b,c,d,f,g
        else -> 0
    }

    private fun draw7SegmentDigit(
        canvas: Canvas,
        mask: Int,
        x: Float,
        y: Float,
        w: Float,
        h: Float,
        paint: Paint
    ) {
        val t = (w * 0.18f).coerceAtLeast(2f)
        val halfH = h / 2f
        val gap = t * 0.25f

        // Segment a: top
        if ((mask and 1) != 0) {
            canvas.drawRect(x + t, y, x + w - t, y + t, paint)
        }
        // Segment b: top-right
        if ((mask and 2) != 0) {
            canvas.drawRect(x + w - t, y + t + gap, x + w, y + halfH - gap, paint)
        }
        // Segment c: bottom-right
        if ((mask and 4) != 0) {
            canvas.drawRect(x + w - t, y + halfH + gap, x + w, y + h - t - gap, paint)
        }
        // Segment d: bottom
        if ((mask and 8) != 0) {
            canvas.drawRect(x + t, y + h - t, x + w - t, y + h, paint)
        }
        // Segment e: bottom-left
        if ((mask and 16) != 0) {
            canvas.drawRect(x, y + halfH + gap, x + t, y + h - t - gap, paint)
        }
        // Segment f: top-left
        if ((mask and 32) != 0) {
            canvas.drawRect(x, y + t + gap, x + t, y + halfH - gap, paint)
        }
        // Segment g: middle
        if ((mask and 64) != 0) {
            val halfT = t / 2f
            canvas.drawRect(x + t, y + halfH - halfT, x + w - t, y + halfH + halfT, paint)
        }
    }

    private fun drawColon(canvas: Canvas, x: Float, y: Float, w: Float, h: Float, paint: Paint) {
        val dotSize = (w * 0.6f).coerceAtLeast(3f)
        val dotX = x + (w - dotSize) / 2f
        val topY = y + h * 0.32f - dotSize / 2f
        val bottomY = y + h * 0.68f - dotSize / 2f
        canvas.drawRect(dotX, topY, dotX + dotSize, topY + dotSize, paint)
        canvas.drawRect(dotX, bottomY, dotX + dotSize, bottomY + dotSize, paint)
    }

    private fun drawHeartRow(
        canvas: Canvas,
        startX: Float,
        y: Float,
        label: String,
        filledCount: Int,
        activeColor: Int,
        emptyColor: Int
    ) {
        textPaint.color = activeColor
        textPaint.textSize = 10f
        textPaint.textAlign = Paint.Align.LEFT
        canvas.drawText(label, startX, y, textPaint)

        var hX = startX + 26f
        for (i in 0 until 4) {
            val isFilled = i < filledCount
            drawHeartIcon(canvas, hX, y - 4f, 4f, if (isFilled) activeColor else emptyColor, isFilled)
            hX += 11f
        }
        textPaint.textAlign = Paint.Align.CENTER
    }

    private fun drawHeartIcon(canvas: Canvas, cx: Float, cy: Float, radius: Float, color: Int, filled: Boolean) {
        heartPaint.color = color
        heartPaint.style = if (filled) Paint.Style.FILL else Paint.Style.STROKE
        heartPaint.strokeWidth = 1.2f

        val path = Path().apply {
            moveTo(cx, cy + radius * 0.8f)
            cubicTo(cx - radius * 1.5f, cy - radius * 0.8f, cx - radius, cy - radius * 1.8f, cx, cy - radius * 0.7f)
            cubicTo(cx + radius, cy - radius * 1.8f, cx + radius * 1.5f, cy - radius * 0.8f, cx, cy + radius * 0.8f)
            close()
        }
        canvas.drawPath(path, heartPaint)
    }

    private fun drawJoyHeart(canvas: Canvas, cx: Float, cy: Float, color: Int) {
        drawHeartIcon(canvas, cx, cy, 6f, color, filled = true)
    }

    companion object {
        private const val BOUNCE_DURATION_MS = 1400L

        /**
         * Robust battery percentage retrieval protecting against Integer.MIN_VALUE or negative values.
         */
        fun getBatteryPercentage(context: Context): Int {
            val bm = context.getSystemService(Context.BATTERY_SERVICE) as? BatteryManager
            val cap = bm?.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY) ?: -1
            if (cap in 0..100) return cap
            return try {
                val filter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
                val batteryStatus = context.registerReceiver(null, filter)
                val level = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
                val scale = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
                if (level >= 0 && scale > 0) {
                    (level * 100 / scale.toFloat()).toInt().coerceIn(0, 100)
                } else {
                    100
                }
            } catch (_: Exception) {
                100
            }
        }
    }
}
