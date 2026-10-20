package com.digiwear.companion.ui

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.LaunchedEffect
import com.digiwear.companion.NfcBattleBridge
import com.digiwear.companion.nfc.NfcDuelManager
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import com.digiwear.core.qrcode.QrMatrixGenerator
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.digiwear.companion.data.PhoneDatabase
import com.digiwear.companion.data.SyncedPetEntity
import com.digiwear.companion.datalayer.PhoneWearableManager
import com.digiwear.companion.render.SpriteCanvasRenderer
import com.digiwear.companion.widget.DigiWearAppWidgetProvider
import com.digiwear.core.battle.DeterministicBattleSolver
import com.digiwear.core.battle.WildScanEncounter
import com.digiwear.core.battle.WildScanEncounterGenerator
import com.digiwear.core.codec.CommCodeCodec
import com.digiwear.core.codec.CommCodePayload
import com.digiwear.core.fusion.JogressEngine
import com.digiwear.core.model.BattleFighter
import com.digiwear.core.model.BattleMode
import com.digiwear.core.model.BattleResult
import com.digiwear.core.model.ElementalAttribute
import com.digiwear.core.model.PetSpecies
import com.digiwear.core.model.PetStage
import com.digiwear.core.model.PetState
import com.digiwear.core.model.SpriteFrame
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

private val FEATURED_FIGHTERS = listOf(201, 301, 304, 401, 501, 503, 515, 227, 439)

private val ONLINE_LOBBY_TAMERS = listOf(
    Triple("Tamer_Taichi [A-Com]", 401, 0x4101),    // MetalGreymon
    Triple("Tamer_Yamato [D-Com]", 404, 0x4502),    // WereGarurumon
    Triple("Tamer_Ruki [WiFiCom]", 425, 0x7711),    // Taomon
    Triple("Tamer_Takato [0nl1ne]", 515, 0x8822),   // Dukemon
    Triple("RoyalKnight_X [WiFiCom]", 523, 0x99AA), // Alphamon
    Triple("DarkMaster_Z [A-Com]", 509, 0xDEAD)     // Piedmon
)

private val COLOSSEUM_ENVIRONMENTS = listOf(
    "Normal",
    "Thunderstorm",
    "Magma Heat",
    "Tidal Current",
    "Miasma Fog",
    "Zero Gravity",
    "Apocalymon Void",
    "X-Program Fog",
    "Cocytus Blizzard",
    "Dark Area Sin Gates",
    "GRB Wave Cataclysm",
    "Quantum Singularity"
)

@Composable
fun BattleHubScreen(
    db: PhoneDatabase,
    wearableManager: PhoneWearableManager
) {
    var ownMonsterId by remember { mutableIntStateOf(301) } // Default Greymon until DB loads
    var ownVictories by remember { mutableIntStateOf(12) }
    var ownEffort by remember { mutableIntStateOf(4) }
    var ownSeed by remember { mutableIntStateOf(0x1337) }
    var generatedCode by remember { mutableStateOf("") }
    var generatedDigiRom by remember { mutableStateOf("") }
    var inputCode by remember { mutableStateOf("") }
    var decodedOpponent by remember { mutableStateOf<CommCodePayload?>(null) }
    var validationError by remember { mutableStateOf<String?>(null) }
    var battleResult by remember { mutableStateOf<BattleResult?>(null) }
    var beamStatus by remember { mutableStateOf<String?>(null) }

    var showQrMatrix by remember { mutableStateOf(false) }
    var isCameraScannerOpen by remember { mutableStateOf(false) }
    var scannerStatusError by remember { mutableStateOf<String?>(null) }

    // 4-Mode Battle Command Center State
    var selectedBattleMode by remember { mutableStateOf(BattleMode.NFC_TAP_DUEL) }
    var barcodeScanInput by remember { mutableStateOf("4902425617220") }
    var activeWildEncounter by remember { mutableStateOf<WildScanEncounter?>(null) }
    var selectedEnvironment by remember { mutableStateOf("Normal") }

    // Live Animated Face-to-Face Battle Arena State
    var battleJob by remember { mutableStateOf<Job?>(null) }
    var isAnimatingBattle by remember { mutableStateOf(false) }
    var currentTurnIndex by remember { mutableIntStateOf(0) }
    var qteActive by remember { mutableStateOf(false) }
    var critBoostTriggered by remember { mutableStateOf(false) }
    var turnFxProgress by remember { mutableFloatStateOf(0f) }
    var activeCutInSpeciesId by remember { mutableStateOf<Int?>(null) }
    var activeCutInTitle by remember { mutableStateOf("CRITICAL HIT") }
    var encounterFlashEnemyId by remember { mutableStateOf<Int?>(null) }

    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val buzzerManager = remember { com.digiwear.companion.sound.BuzzerManager(context) }
    var retroSoundEnabled by remember { mutableStateOf(buzzerManager.isEnabled) }

    val ownSpecies = remember(ownMonsterId) { PetSpecies.getById(ownMonsterId) }

    LaunchedEffect(Unit) {
        val latest = db.phoneDao().getLatestSnapshot()
        if (latest != null) {
            ownMonsterId = latest.speciesId
            ownVictories = latest.victories
            ownEffort = latest.effort
        }
    }

    // Handle NFC Tag or Watch-to-Phone NFC Tap
    LaunchedEffect(Unit) {
        NfcBattleBridge.incomingCodeFlow.collect { scannedCode ->
            if (!scannedCode.isNullOrBlank()) {
                if (selectedBattleMode == BattleMode.WILD_SCAN) {
                    val enc = WildScanEncounterGenerator.generateFromScan(
                        rawScanData = scannedCode,
                        scanSource = "NFC_TAG",
                        playerStage = ownSpecies.stage
                    )
                    activeWildEncounter = enc
                    inputCode = enc.commCode128
                    selectedEnvironment = enc.environmentHazard
                    beamStatus = "📡 Wild ${enc.species.name} (${enc.habitatZone}) spawned from NFC Tag!"
                } else if (selectedBattleMode == BattleMode.ACOM_DCOM_WIFICOM) {
                    // NFC Tap from Watch to Phone in A-Com Relay mode: import Watch fighter or challenge!
                    try {
                        val payload = CommCodeCodec.decodeAuto(scannedCode)
                        ownMonsterId = payload.monsterId
                        ownVictories = payload.historicalVictories
                        ownEffort = payload.trainingEffort
                        ownSeed = payload.battleRngSeed
                        val sp = PetSpecies.getById(payload.monsterId)
                        beamStatus = "🌐 NFC Watch Relay: Loaded ${sp.name} (${scannedCode.take(14)}) into A-Com Transceiver!"
                    } catch (_: Exception) {
                        inputCode = scannedCode
                        beamStatus = "🌐 Scanned A-Com / DigiROM Packet via NFC!"
                    }
                } else {
                    inputCode = scannedCode
                    validationError = null
                    decodedOpponent = try { CommCodeCodec.decodeAuto(scannedCode) } catch (_: Exception) { null }
                    beamStatus = "📶 Opponent scanned via NFC Tap-to-Duel!"
                }
                NfcBattleBridge.incomingCodeFlow.value = null
            }
        }
    }

    // Handle Watch DataLayer A-Com Relay ("RELAY DIGIROM TO PHONE" button on Watch)
    LaunchedEffect(Unit) {
        NfcBattleBridge.incomingWatchRelayFlow.collect { watchDigiRom ->
            if (!watchDigiRom.isNullOrBlank()) {
                selectedBattleMode = BattleMode.ACOM_DCOM_WIFICOM
                try {
                    val payload = CommCodeCodec.decodeAuto(watchDigiRom)
                    ownMonsterId = payload.monsterId
                    ownSeed = payload.battleRngSeed
                    val sp = PetSpecies.getById(payload.monsterId)
                    beamStatus = "📲 Watch A-Com Relay Received: ${sp.name} ($watchDigiRom) ready for Online Battle!"
                } catch (_: Exception) {
                    beamStatus = "📲 Watch A-Com Relay Received: $watchDigiRom"
                }
                NfcBattleBridge.incomingWatchRelayFlow.value = null
            }
        }
    }

    // Generate own code on load / change
    LaunchedEffect(ownMonsterId, ownVictories, ownEffort, ownSeed) {
        val species = PetSpecies.getById(ownMonsterId)
        generatedCode = CommCodeCodec.encode128(
            monsterId = ownMonsterId,
            stage = species.stage,
            attribute = species.attribute,
            basePowerHp = species.basePower,
            historicalVictories = ownVictories,
            trainingEffort = ownEffort,
            battleRngSeed = ownSeed
        )
        generatedDigiRom = CommCodeCodec.encodeDigiRom(
            monsterId = ownMonsterId,
            stage = species.stage,
            attribute = species.attribute,
            basePowerHp = species.basePower,
            battleRngSeed = ownSeed
        )
    }

    val ownQrMatrix = remember(generatedCode) {
        if (generatedCode.isNotBlank()) {
            try {
                QrMatrixGenerator.generateQrMatrix(generatedCode)
            } catch (_: Exception) {
                null
            }
        } else null
    }

    val launchAnimatedBattle: (String, BattleMode, String) -> Unit = launchAnimatedBattle@{ codeToFight, mode, envHazard ->
        if (isAnimatingBattle) return@launchAnimatedBattle
        battleJob?.cancel()
        try {
            val payload = CommCodeCodec.decodeAuto(codeToFight)
            decodedOpponent = payload
            validationError = null

            val ownSp = PetSpecies.getById(ownMonsterId)
            val oppSp = PetSpecies.getById(payload.monsterId)

            val fA = BattleFighter(
                id = "player",
                speciesId = ownMonsterId,
                name = ownSp.name,
                stage = ownSp.stage,
                attribute = ownSp.attribute,
                maxHp = ownSp.baseHp,
                currentHp = ownSp.baseHp,
                basePower = ownSp.basePower,
                victories = ownVictories,
                effort = ownEffort,
                battleSeed = ownSeed
            )
            val fB = BattleFighter(
                id = "opponent",
                speciesId = payload.monsterId,
                name = oppSp.name,
                stage = payload.stage,
                attribute = payload.attribute,
                maxHp = oppSp.baseHp,
                currentHp = oppSp.baseHp,
                basePower = payload.basePowerHp.coerceAtLeast(oppSp.basePower),
                victories = payload.historicalVictories,
                effort = payload.trainingEffort,
                battleSeed = payload.battleRngSeed
            )

            val normalRes = DeterministicBattleSolver.resolveBattle(
                fighterA = fA,
                fighterB = fB,
                environmentEffect = envHazard,
                attackerCritBoost = false,
                battleMode = mode
            )
            val critRes = DeterministicBattleSolver.resolveBattle(
                fighterA = fA,
                fighterB = fB,
                environmentEffect = envHazard,
                attackerCritBoost = true,
                battleMode = mode
            )

            var activeRes = normalRes
            battleResult = activeRes
            isAnimatingBattle = true
            currentTurnIndex = 0
            critBoostTriggered = false

            battleJob = scope.launch {
                try {
                    // Dramatic Encounter Flash Sequence
                    encounterFlashEnemyId = fB.speciesId
                    buzzerManager.playAttentionBeep()
                    delay(1050L)
                    encounterFlashEnemyId = null

                    var idx = 0
                    while (idx < activeRes.turns.size) {
                        currentTurnIndex = idx
                        val turn = activeRes.turns[idx]
                        if (turn.attackerIndex == 0) {
                            qteActive = true
                            delay(420L)
                            qteActive = false
                            if (critBoostTriggered) {
                                activeRes = critRes
                                battleResult = critRes
                            }
                        }

                        // Authentic Color V-Pet Cut-In on Critical Hit / Mega Hit
                        val isCrit = turn.attackType == "MEGA_HIT" || (turn.attackerIndex == 0 && critBoostTriggered)
                        if (isCrit) {
                            val attackerId = if (turn.attackerIndex == 0) fA.speciesId else fB.speciesId
                            activeCutInTitle = if (turn.attackType == "MEGA_HIT") "MEGA HIT" else "CRITICAL HIT"
                            activeCutInSpeciesId = attackerId
                            buzzerManager.playCriticalCutInTone()
                            delay(850L)
                            activeCutInSpeciesId = null
                        } else {
                            buzzerManager.playAttackImpactTone()
                        }

                        for (step in 1..8) {
                            turnFxProgress = step / 8f
                            delay(65L)
                        }
                        idx++
                    }
                    if (activeRes.winnerIndex == 0) {
                        buzzerManager.playVictoryFanfare()
                    } else {
                        buzzerManager.playDefeatJingle()
                    }
                } finally {
                    isAnimatingBattle = false
                    encounterFlashEnemyId = null
                    activeCutInSpeciesId = null
                    qteActive = false
                }
            }
        } catch (e: Exception) {
            validationError = "Validation Failed: ${e.message}"
            isAnimatingBattle = false
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(DigiWearPalette.CyberBg)
                .padding(16.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
        DigiSectionHeader(
            tag = "D-COM // 4-MODE BATTLE COMMAND CENTER",
            title = selectedBattleMode.displayName,
            subtitle = "Canonical V-Pet battle formulas, Face-to-Face LCD Arena & Watch Relay",
            accentColor = Color(0xFFEF5350)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .background(if (retroSoundEnabled) Color(0xFF1B3B2B) else Color(0xFF263238), RoundedCornerShape(8.dp))
                    .border(1.dp, if (retroSoundEnabled) Color(0xFF00E676) else Color(0xFF546E7A), RoundedCornerShape(8.dp))
                    .clickable {
                        buzzerManager.isEnabled = !buzzerManager.isEnabled
                        retroSoundEnabled = buzzerManager.isEnabled
                        if (retroSoundEnabled) buzzerManager.playButtonBeep()
                    }
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
                Text(
                    text = if (retroSoundEnabled) "🔊 RETRO BUZZER: ON" else "🔇 RETRO BUZZER: MUTED",
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace,
                    color = if (retroSoundEnabled) Color(0xFF00E676) else Color(0xFFB0BEC5),
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 4 Distinct Battle Mode Selector Tabs
        val battleModes = listOf(
            BattleMode.NFC_TAP_DUEL to "📶 NFC DUEL",
            BattleMode.WILD_SCAN to "📡 WILD SCAN",
            BattleMode.ACOM_DCOM_WIFICOM to "🌐 A-COM / WIFI",
            BattleMode.ADVENTURE to "🏟 COLOSSEUM"
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            battleModes.forEach { (mode, label) ->
                val active = selectedBattleMode == mode
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            if (active) Color(0xFF281C24) else DigiWearPalette.CardSurface,
                            RoundedCornerShape(10.dp)
                        )
                        .border(
                            1.dp,
                            if (active) Color(0xFFEF5350) else DigiWearPalette.CardBorder,
                            RoundedCornerShape(10.dp)
                        )
                        .clickable { selectedBattleMode = mode }
                        .padding(vertical = 9.dp, horizontal = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = label,
                        fontSize = 9.5.sp,
                        fontWeight = FontWeight.Black,
                        color = if (active) Color(0xFFFF8A80) else Color(0xFFB0BEC5),
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Quick Fighter Selector Strip
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FEATURED_FIGHTERS.forEach { id ->
                val sp = PetSpecies.getById(id)
                val selected = ownMonsterId == id
                Row(
                    modifier = Modifier
                        .background(
                            if (selected) Color(0xFF281C24) else DigiWearPalette.CardSurface,
                            RoundedCornerShape(10.dp)
                        )
                        .border(
                            1.dp,
                            if (selected) Color(0xFFEF5350) else DigiWearPalette.CardBorder,
                            RoundedCornerShape(10.dp)
                        )
                        .clickable { ownMonsterId = id }
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SpriteCanvasRenderer(
                        speciesId = sp.id,
                        frame = SpriteFrame.IDLE_A,
                        size = 26.dp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text(
                            text = sp.name,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (selected) Color(0xFFFF8A80) else Color.White
                        )
                        Text(
                            text = "${sp.stage.displayName} • ${sp.basePower}P",
                            fontSize = 8.sp,
                            color = Color(0xFF90A4AE),
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Generator & Active Fighter Formula Card
        val ownFighterPreview = remember(ownMonsterId, ownVictories, ownEffort, ownSeed) {
            BattleFighter(
                id = "player",
                speciesId = ownMonsterId,
                name = ownSpecies.name,
                stage = ownSpecies.stage,
                attribute = ownSpecies.attribute,
                maxHp = ownSpecies.baseHp,
                currentHp = ownSpecies.baseHp,
                basePower = ownSpecies.basePower,
                victories = ownVictories,
                effort = ownEffort,
                battleSeed = ownSeed
            )
        }
        val ownEffPower = remember(ownFighterPreview) {
            DeterministicBattleSolver.computeEffectivePower(ownFighterPreview)
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = DigiWearPalette.CardSurface),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, DigiWearPalette.CardBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "▸ YOUR FIGHTER PACKET (${ownSpecies.name.uppercase()})",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = DigiWearPalette.DigiviceCyan,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = "BASE ${ownSpecies.basePower}P → EFF PWR ${ownEffPower}P • ${ownSpecies.baseHp} HP • ${ownEffort}★ EFFORT",
                            fontSize = 9.5.sp,
                            color = Color(0xFF80CBC4),
                            fontFamily = FontFamily.Monospace
                        )
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        StageBadge(ownSpecies.stage.displayName)
                        AttributeBadge(ownSpecies.attribute.displayName)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Toggle Bar for QR Matrix vs Text Comm Code
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (showQrMatrix) "▸ BATTLE QR MATRIX (INSTANT MATCHMAKING)" else "▸ 128-BIT BASE32 & DIGIROM",
                        fontSize = 9.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF78909C),
                        fontFamily = FontFamily.Monospace
                    )
                    OutlinedButton(
                        onClick = { showQrMatrix = !showQrMatrix },
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                        modifier = Modifier.height(26.dp)
                    ) {
                        Text(
                            text = if (showQrMatrix) "📄 SHOW TEXT CODE" else "🏁 SHOW QR MATRIX",
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            color = if (showQrMatrix) DigiWearPalette.DigiviceCyan else Color(0xFF00E676)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                if (showQrMatrix) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(DigiWearPalette.CardInner, RoundedCornerShape(10.dp))
                            .border(1.dp, Color(0xFF263238), RoundedCornerShape(10.dp))
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(180.dp)
                                    .background(Color.White, RoundedCornerShape(8.dp))
                                    .padding(8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                if (ownQrMatrix != null) {
                                    QrCodeMatrixView(
                                        matrix = ownQrMatrix,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                } else {
                                    Text("Generating QR Matrix...", fontSize = 10.sp, color = Color.DarkGray)
                                }
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "POINT COMPANION CAMERA SCANNER TO DUEL",
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.Black,
                                color = DigiWearPalette.CrestGold,
                                fontFamily = FontFamily.Monospace
                            )
                            Text(
                                text = CommCodeCodec.formatWithHyphens(generatedCode),
                                fontSize = 8.5.sp,
                                color = Color(0xFF90A4AE),
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(DigiWearPalette.CardInner, RoundedCornerShape(10.dp))
                            .border(1.dp, Color(0xFF263238), RoundedCornerShape(10.dp))
                            .padding(12.dp)
                    ) {
                        Column {
                            Text(
                                text = "128-BIT BASE32 COMM CODE:",
                                fontSize = 9.sp,
                                color = Color(0xFF78909C),
                                fontFamily = FontFamily.Monospace
                            )
                            Text(
                                text = CommCodeCodec.formatWithHyphens(generatedCode),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF00E676),
                                fontFamily = FontFamily.Monospace
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "A-COM / D-COM / WIFICOM / 0NL1NE DIGIROM:",
                                fontSize = 9.sp,
                                color = Color(0xFF78909C),
                                fontFamily = FontFamily.Monospace
                            )
                            Text(
                                text = generatedDigiRom,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = DigiWearPalette.CrestGold,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            clipboardManager.setText(AnnotatedString(generatedCode))
                            Toast.makeText(context, "128-Bit Base32 Code copied!", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.ContentCopy, contentDescription = "Copy Base32", modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Base32", fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                    }

                    OutlinedButton(
                        onClick = {
                            clipboardManager.setText(AnnotatedString(generatedDigiRom))
                            Toast.makeText(context, "0nl1ne DigiROM copied!", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.ContentCopy, contentDescription = "Copy ROM", modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("DigiROM", fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                    }

                    Button(
                        onClick = { ownSeed = Random.nextInt(0xFFFF) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF37474F))
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = "Reroll", modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Seed", fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            scope.launch {
                                val watchState = wearableManager.requestPetStateFromWatch()
                                if (watchState != null) {
                                    ownMonsterId = watchState.speciesId
                                    ownVictories = watchState.victories
                                    ownEffort = watchState.effort
                                    val sp = PetSpecies.getById(watchState.speciesId)
                                    beamStatus = "📲 Pulled live ${sp.name} from Smartwatch!"
                                } else {
                                    beamStatus = "Watch offline — tap Watch to Phone via NFC or use local fighter"
                                }
                            }
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("PULL FROM WATCH", fontSize = 10.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                    }

                    Button(
                        onClick = {
                            scope.launch {
                                val ok = wearableManager.sendCommCodeToWatch(generatedCode)
                                beamStatus = if (ok) "Dispatched Comm Code to Watch!" else "Watch not reachable (Offline Mode)"
                            }
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00897B))
                    ) {
                        Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Beam", modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("BEAM TO WATCH", fontSize = 10.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                    }
                }
            }
        }

        beamStatus?.let {
            Text("▸ $it", fontSize = 11.sp, color = Color(0xFF00E676), fontFamily = FontFamily.Monospace, modifier = Modifier.padding(top = 6.dp))
        }

        Spacer(modifier = Modifier.height(14.dp))

        // =========================================================================
        // MODE-SPECIFIC PANEL (1: NFC DUEL, 2: WILD SCAN, 3: A-COM/WIFI, 4: COLOSSEUM)
        // =========================================================================
        when (selectedBattleMode) {
            BattleMode.NFC_TAP_DUEL -> {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = DigiWearPalette.CardSurface),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, Color(0xFF00E676).copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .background(Color(0xFF00E676), CircleShape)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "MODE 1: NFC TAP-TO-DUEL (WATCH / PHONE P2P)",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF00E676),
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                            Text(
                                text = if (NfcDuelManager.isNfcEnabled(context)) "NFC READY" else "NFC OFF",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (NfcDuelManager.isNfcEnabled(context)) Color(0xFF00E676) else Color.Red,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Tap two watches together, tap your watch to this phone, or tap an NFC Battle Card to exchange 128-bit CommCodes or trigger DNA Jogress!",
                            fontSize = 10.sp,
                            color = Color.LightGray
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = {
                                    val tag = NfcBattleBridge.pendingTagToWrite
                                    if (tag != null) {
                                        val ok = NfcDuelManager.writeDuelCodeToTag(tag, generatedDigiRom)
                                        beamStatus = if (ok) "✓ Wrote ${ownSpecies.name} DigiROM to physical NFC Tag!" else "Tag write failed (Check tag write-lock)"
                                    } else {
                                        beamStatus = "Hold a physical NFC tag to back of phone, then click Write"
                                    }
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("WRITE TO NFC TAG", fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                            }
                            if (inputCode.isNotEmpty()) {
                                Button(
                                    onClick = {
                                        scope.launch {
                                            val ok = wearableManager.sendCommCodeToWatch(inputCode)
                                            beamStatus = if (ok) "Dispatched scanned opponent to Watch!" else "Watch not reachable"
                                        }
                                    },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00897B))
                                ) {
                                    Text("BEAM OPP TO WATCH", fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                                }
                            }
                        }
                    }
                }
            }

            BattleMode.WILD_SCAN -> {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = DigiWearPalette.CardSurface),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, Color(0xFF69F0AE).copy(alpha = 0.6f))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "▸ MODE 2: WILD BARCODE / QR / NFC SCANNER",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF69F0AE),
                            fontFamily = FontFamily.Monospace
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Scan any UPC/EAN-13 product barcode, QR code, or tap any physical NFC card/transit pass/amiibo to deterministically discover Wild Digimon!",
                            fontSize = 10.sp,
                            color = Color.LightGray
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = barcodeScanInput,
                                onValueChange = { barcodeScanInput = it },
                                label = { Text("UPC / EAN-13 Barcode, QR Text, or NFC UID", fontSize = 10.sp) },
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                            Button(
                                onClick = {
                                    val enc = WildScanEncounterGenerator.generateFromScan(
                                        rawScanData = barcodeScanInput,
                                        scanSource = "BARCODE",
                                        playerStage = ownSpecies.stage
                                    )
                                    activeWildEncounter = enc
                                    inputCode = enc.commCode128
                                    selectedEnvironment = enc.environmentHazard
                                    decodedOpponent = CommCodeCodec.decodeAuto(enc.commCode128)
                                    beamStatus = "📡 Decoded Barcode → Wild ${enc.species.name} in ${enc.habitatZone}!"
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
                            ) {
                                Text("SCAN", fontSize = 11.sp, fontWeight = FontWeight.Black, fontFamily = FontFamily.Monospace)
                            }
                            Button(
                                onClick = {
                                    scannerStatusError = null
                                    isCameraScannerOpen = true
                                    buzzerManager.playButtonBeep()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00897B))
                            ) {
                                Icon(Icons.Default.CameraAlt, contentDescription = "Camera Scan", modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("CAM", fontSize = 11.sp, fontWeight = FontWeight.Black, fontFamily = FontFamily.Monospace)
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("PRESET BARCODES & TAGS:", fontSize = 9.sp, color = Color.Gray, fontFamily = FontFamily.Monospace)
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            WildScanEncounterGenerator.PRESET_BARCODES.forEach { (barcode, label) ->
                                OutlinedButton(
                                    onClick = {
                                        barcodeScanInput = barcode
                                        val enc = WildScanEncounterGenerator.generateFromScan(
                                            rawScanData = barcode,
                                            scanSource = "BARCODE",
                                            playerStage = ownSpecies.stage
                                        )
                                        activeWildEncounter = enc
                                        inputCode = enc.commCode128
                                        selectedEnvironment = enc.environmentHazard
                                        decodedOpponent = CommCodeCodec.decodeAuto(enc.commCode128)
                                    }
                                ) {
                                    Text(label, fontSize = 9.5.sp, fontFamily = FontFamily.Monospace)
                                }
                            }
                        }

                        activeWildEncounter?.let { enc ->
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFF13261E), RoundedCornerShape(10.dp))
                                    .border(1.dp, if (enc.isRareAnomaly) DigiWearPalette.CrestGold else Color(0xFF00E676), RoundedCornerShape(10.dp))
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                    SpriteCanvasRenderer(speciesId = enc.species.id, frame = SpriteFrame.IDLE_A, size = 44.dp)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = "${if (enc.isRareAnomaly) "★ RARE BOSS: " else "WILD: "}${enc.species.name}",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Black,
                                            color = if (enc.isRareAnomaly) DigiWearPalette.CrestGold else Color(0xFF69F0AE)
                                        )
                                        Text(
                                            text = "${enc.habitatZone} • ENV: ${enc.environmentHazard}",
                                            fontSize = 9.5.sp,
                                            color = Color.LightGray,
                                            fontFamily = FontFamily.Monospace
                                        )
                                        Text(
                                            text = "HP ${enc.fighter.maxHp} • PWR ${enc.fighter.basePower} • ${enc.species.attribute.displayName}",
                                            fontSize = 9.sp,
                                            color = Color(0xFF80CBC4),
                                            fontFamily = FontFamily.Monospace
                                        )
                                    }
                                }
                                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Button(
                                        onClick = {
                                            launchAnimatedBattle(enc.commCode128, BattleMode.WILD_SCAN, enc.environmentHazard)
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE65100))
                                    ) {
                                        Text("FIGHT WILD", fontSize = 9.5.sp, fontWeight = FontWeight.Black, fontFamily = FontFamily.Monospace)
                                    }
                                    OutlinedButton(
                                        onClick = {
                                            scope.launch {
                                                val ok = wearableManager.sendCommCodeToWatch(enc.commCode128)
                                                beamStatus = if (ok) "Beamed Wild ${enc.species.name} to Watch!" else "Watch offline"
                                            }
                                        }
                                    ) {
                                        Text("TO WATCH", fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            BattleMode.ACOM_DCOM_WIFICOM -> {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = DigiWearPalette.CardSurface),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, DigiWearPalette.CrestGold.copy(alpha = 0.6f))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "▸ MODE 3: A-COM / D-COM / WIFICOM + WATCH NFC RELAY",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = DigiWearPalette.CrestGold,
                            fontFamily = FontFamily.Monospace
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Tap your Smartwatch to the back of this phone (or click 'RELAY DIGIROM TO PHONE' on Watch) to bridge your Watch Partner into A-Com / D-Com / WiFiCom / Digimon 0nl1ne lobbies!",
                            fontSize = 10.sp,
                            color = Color.LightGray
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "0NL1NE / WIFICOM TAMER LOBBY (CLICK TO CHALLENGE OR BEAM TO WATCH):",
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF80D8FF),
                            fontFamily = FontFamily.Monospace
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        ONLINE_LOBBY_TAMERS.forEach { (tamerName, monId, seed) ->
                            val tamerSp = PetSpecies.getById(monId)
                            val tamerRom = CommCodeCodec.encodeDigiRom(
                                monsterId = monId,
                                stage = tamerSp.stage,
                                attribute = tamerSp.attribute,
                                basePowerHp = tamerSp.basePower,
                                battleRngSeed = seed
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 3.dp)
                                    .background(DigiWearPalette.CardInner, RoundedCornerShape(8.dp))
                                    .border(1.dp, Color(0xFF263238), RoundedCornerShape(8.dp))
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                    SpriteCanvasRenderer(speciesId = monId, frame = SpriteFrame.IDLE_A, size = 32.dp)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(tamerName, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                        Text(
                                            text = "${tamerSp.name} • $tamerRom",
                                            fontSize = 9.sp,
                                            color = DigiWearPalette.CrestGold,
                                            fontFamily = FontFamily.Monospace
                                        )
                                    }
                                }
                                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    OutlinedButton(
                                        onClick = {
                                            scope.launch {
                                                val ok = wearableManager.sendCommCodeToWatch(tamerRom)
                                                beamStatus = if (ok) "Relayed $tamerName ($tamerRom) to Watch!" else "Watch offline"
                                            }
                                        }
                                    ) {
                                        Text("WATCH", fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                                    }
                                    Button(
                                        onClick = {
                                            inputCode = tamerRom
                                            launchAnimatedBattle(tamerRom, BattleMode.ACOM_DCOM_WIFICOM, "Normal")
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1565C0))
                                    ) {
                                        Text("DUEL", fontSize = 9.5.sp, fontWeight = FontWeight.Black, fontFamily = FontFamily.Monospace)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            BattleMode.ADVENTURE -> {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = DigiWearPalette.CardSurface),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, Color(0xFF26A69A).copy(alpha = 0.6f))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "▸ MODE 4: COLOSSEUM & ENVIRONMENTAL WEATHER ARENA",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF64FFDA),
                            fontFamily = FontFamily.Monospace
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Select an active Digital World sector hazard to test canonical Attribute & Weather modifiers:",
                            fontSize = 10.sp,
                            color = Color.LightGray
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            COLOSSEUM_ENVIRONMENTS.forEach { env ->
                                val active = selectedEnvironment == env
                                Box(
                                    modifier = Modifier
                                        .background(
                                            if (active) Color(0xFF00695C) else DigiWearPalette.CardInner,
                                            RoundedCornerShape(8.dp)
                                        )
                                        .border(
                                            1.dp,
                                            if (active) Color(0xFF64FFDA) else Color(0xFF263238),
                                            RoundedCornerShape(8.dp)
                                        )
                                        .clickable { selectedEnvironment = env }
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = "⚡ $env",
                                        fontSize = 9.5.sp,
                                        fontWeight = if (active) FontWeight.Bold else FontWeight.Normal,
                                        color = if (active) Color.White else Color.LightGray,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Opponent Code Input & Live Animated Face-to-Face Combat Arena
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = DigiWearPalette.CardSurface),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, DigiWearPalette.CardBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "▸ OPPONENT D-COM / DIGIROM TRANSCEIVER & LIVE ARENA",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFF8A80),
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        scannerStatusError = null
                        isCameraScannerOpen = true
                        buzzerManager.playButtonBeep()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00897B))
                ) {
                    Icon(Icons.Default.QrCodeScanner, contentDescription = "Scan QR", modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("📷 POINT-AND-SHOOT CAMERA SCANNER", fontSize = 10.5.sp, fontWeight = FontWeight.Black, fontFamily = FontFamily.Monospace)
                }
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = inputCode,
                    onValueChange = {
                        inputCode = it
                        validationError = null
                        decodedOpponent = try {
                            CommCodeCodec.decodeAuto(it)
                        } catch (_: Exception) {
                            null
                        }
                    },
                    label = { Text("Paste 26-Char Base32 or V1-XXXX-XXXX-XXXX DigiROM", fontSize = 11.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                val compatibleHubFusions = remember(ownMonsterId) {
                    JogressEngine.getCompatibleRecipesFor(ownMonsterId)
                }
                if (compatibleHubFusions.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "⚡ 1-TAP JOGRESS / DIGIXROS PARTNER CODES FOR ${ownSpecies.name.uppercase()}:",
                        fontSize = 9.5.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFFEA80FC),
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        compatibleHubFusions.forEach { recipe ->
                            val partnerId = if (recipe.primarySpeciesId == ownMonsterId) recipe.secondarySpeciesId else recipe.primarySpeciesId
                            val partnerSp = PetSpecies.getById(partnerId)
                            val targetSp = PetSpecies.getById(recipe.targetSpeciesId)
                            OutlinedButton(
                                onClick = {
                                    val code = CommCodeCodec.encode128(
                                        monsterId = partnerSp.id,
                                        stage = partnerSp.stage,
                                        attribute = partnerSp.attribute,
                                        basePowerHp = partnerSp.basePower,
                                        historicalVictories = 18,
                                        trainingEffort = 4,
                                        battleRngSeed = 0x7777
                                    )
                                    inputCode = CommCodeCodec.formatWithHyphens(code)
                                    decodedOpponent = try { CommCodeCodec.decodeAuto(code) } catch (_: Exception) { null }
                                    validationError = null
                                },
                                border = BorderStroke(1.dp, Color(recipe.fusionType.badgeColorHex))
                            ) {
                                Text(
                                    text = "⚡ +${partnerSp.name} → ${targetSp.name} (${recipe.fusionType.displayName})",
                                    fontSize = 9.5.sp,
                                    color = Color(recipe.fusionType.badgeColorHex),
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            val devimonCode = CommCodeCodec.encode128(
                                monsterId = 303, // Devimon (Virus)
                                stage = PetStage.ADULT,
                                attribute = ElementalAttribute.VIRUS,
                                basePowerHp = 210,
                                historicalVictories = 9,
                                trainingEffort = 3,
                                battleRngSeed = 0xBEEF
                            )
                            inputCode = CommCodeCodec.formatWithHyphens(devimonCode)
                            decodedOpponent = try { CommCodeCodec.decodeAuto(devimonCode) } catch (_: Exception) { null }
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Devimon", fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                    }

                    OutlinedButton(
                        onClick = {
                            val metalGaruruCode = CommCodeCodec.encode128(
                                monsterId = 503, // MetalGarurumon (Data)
                                stage = PetStage.ULTIMATE,
                                attribute = ElementalAttribute.DATA,
                                basePowerHp = 280,
                                historicalVictories = 18,
                                trainingEffort = 4,
                                battleRngSeed = 0xCAFE
                            )
                            inputCode = CommCodeCodec.formatWithHyphens(metalGaruruCode)
                            decodedOpponent = try { CommCodeCodec.decodeAuto(metalGaruruCode) } catch (_: Exception) { null }
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("MetalGaruru", fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                    }

                    Button(
                        onClick = {
                            launchAnimatedBattle(inputCode, selectedBattleMode, selectedEnvironment)
                        },
                        modifier = Modifier.weight(1.2f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = "Simulate", modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(2.dp))
                        Text("FIGHT!", fontSize = 10.sp, fontWeight = FontWeight.Black, fontFamily = FontFamily.Monospace)
                    }
                }

                // Live Animated Face-to-Face Bandai LCD VS Arena & DNA Jogress Compatibility
                decodedOpponent?.let { opp ->
                    val oppSp = PetSpecies.getById(opp.monsterId)
                    val ownSp = PetSpecies.getById(ownMonsterId)
                    val fusionRecipe = JogressEngine.findFusionRecipe(ownMonsterId, opp.monsterId)

                    val activeTurn = if (isAnimatingBattle && battleResult != null) {
                        battleResult!!.turns.getOrNull(currentTurnIndex)
                    } else {
                        battleResult?.turns?.lastOrNull()
                    }

                    val playerRemHp = when {
                        battleResult == null -> ownSp.baseHp
                        !isAnimatingBattle -> battleResult!!.fighterAFinalHp
                        activeTurn == null -> battleResult!!.fighterA.maxHp
                        activeTurn.attackerIndex == 1 -> activeTurn.defenderRemainingHp
                        else -> battleResult!!.turns.take(currentTurnIndex).lastOrNull { it.attackerIndex == 1 }?.defenderRemainingHp ?: battleResult!!.fighterA.maxHp
                    }

                    val oppRemHp = when {
                        battleResult == null -> oppSp.baseHp
                        !isAnimatingBattle -> battleResult!!.fighterBFinalHp
                        activeTurn == null -> battleResult!!.fighterB.maxHp
                        activeTurn.attackerIndex == 0 -> activeTurn.defenderRemainingHp
                        else -> battleResult!!.turns.take(currentTurnIndex).lastOrNull { it.attackerIndex == 0 }?.defenderRemainingHp ?: battleResult!!.fighterB.maxHp
                    }

                    val playerHpFrac = (playerRemHp.toFloat() / ownSp.baseHp.coerceAtLeast(1)).coerceIn(0f, 1f)
                    val oppHpFrac = (oppRemHp.toFloat() / oppSp.baseHp.coerceAtLeast(1)).coerceIn(0f, 1f)

                    Spacer(modifier = Modifier.height(12.dp))

                    // Face-to-Face Bandai LCD VS Arena Card
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF15221D), RoundedCornerShape(12.dp))
                            .border(1.dp, Color(0xFF4CAF50), RoundedCornerShape(12.dp))
                            .padding(12.dp)
                    ) {
                        EnvironmentParticleCanvas(environmentEffect = selectedEnvironment)
                        if (isAnimatingBattle && activeTurn != null) {
                            val attackerSp = if (activeTurn.attackerIndex == 0) ownSp else oppSp
                            BattleClashFxCanvas(
                                attackerIndex = activeTurn.attackerIndex,
                                attribute = attackerSp.attribute,
                                attackType = activeTurn.attackType,
                                isCritical = activeTurn.isCritical,
                                moveName = activeTurn.moveName,
                                damage = activeTurn.damage,
                                progress = turnFxProgress
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${selectedBattleMode.displayName.uppercase()} • ENV: ${selectedEnvironment.uppercase()}",
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.Black,
                                color = DigiWearPalette.CrestGold,
                                fontFamily = FontFamily.Monospace
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Left Fighter (Player - flipped horizontally to face Right!)
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    SpriteCanvasRenderer(
                                        speciesId = ownSp.id,
                                        frame = if (activeTurn?.attackerIndex == 0) SpriteFrame.ATTACK_RELEASE else SpriteFrame.ATTACK_CHARGE,
                                        size = 64.dp,
                                        flipHorizontal = true
                                    )
                                    Text(ownSp.name, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF80CBC4))
                                    Spacer(modifier = Modifier.height(3.dp))
                                    Box(
                                        modifier = Modifier
                                            .width(84.dp)
                                            .height(6.dp)
                                            .background(Color(0xFF263238), RoundedCornerShape(3.dp))
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth(playerHpFrac)
                                                .height(6.dp)
                                                .background(Color(0xFF00E676), RoundedCornerShape(3.dp))
                                        )
                                    }
                                    Text("$playerRemHp / ${ownSp.baseHp} HP • ${ownEffPower}P", fontSize = 9.sp, color = Color.LightGray, fontFamily = FontFamily.Monospace)
                                }

                                // Center Clash & Projectile Indicator
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Box(
                                        modifier = Modifier
                                            .background(Color(0xFF3E1C1C), RoundedCornerShape(6.dp))
                                            .border(1.dp, Color(0xFFFF5252), RoundedCornerShape(6.dp))
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = when {
                                                activeTurn?.attackType == "MEGA_HIT" -> "⚡MEGA HIT⚡"
                                                activeTurn?.attackerIndex == 0 -> "●━━━▶"
                                                activeTurn?.attackerIndex == 1 -> "◀━━━●"
                                                else -> "VS"
                                            },
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Black,
                                            color = Color(0xFFFFD54F),
                                            fontFamily = FontFamily.Monospace
                                        )
                                    }
                                    activeTurn?.let { turn ->
                                        Spacer(modifier = Modifier.height(3.dp))
                                        Text(
                                            text = "R${turn.round} • HIT ${turn.hitRatePercent}%",
                                            fontSize = 8.5.sp,
                                            color = Color(0xFF90CAF9),
                                            fontFamily = FontFamily.Monospace
                                        )
                                    }
                                }

                                // Right Fighter (Opponent - faces Left toward Player!)
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    SpriteCanvasRenderer(
                                        speciesId = oppSp.id,
                                        frame = if (activeTurn?.attackerIndex == 1) SpriteFrame.ATTACK_RELEASE else SpriteFrame.ATTACK_CHARGE,
                                        size = 64.dp,
                                        flipHorizontal = false
                                    )
                                    Text(oppSp.name, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF8A80))
                                    Spacer(modifier = Modifier.height(3.dp))
                                    Box(
                                        modifier = Modifier
                                            .width(84.dp)
                                            .height(6.dp)
                                            .background(Color(0xFF263238), RoundedCornerShape(3.dp))
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth(oppHpFrac)
                                                .height(6.dp)
                                                .background(Color(0xFFFF5252), RoundedCornerShape(3.dp))
                                        )
                                    }
                                    Text("$oppRemHp / ${oppSp.baseHp} HP • ${opp.basePowerHp}P", fontSize = 9.sp, color = Color.LightGray, fontFamily = FontFamily.Monospace)
                                }
                            }

                            if (isAnimatingBattle && activeTurn != null) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "${activeTurn.attackerName} [${activeTurn.attackType}] → -${activeTurn.damage} HP!",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (activeTurn.isAdvantage) Color(0xFF00E676) else Color.White,
                                    fontFamily = FontFamily.Monospace
                                )
                                if (qteActive) {
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Button(
                                        onClick = {
                                            critBoostTriggered = true
                                            qteActive = false
                                            buzzerManager.playButtonBeep()
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF1744))
                                    ) {
                                        Text("⚡ TAP TO MEGA-BOOST (+25% CRIT)! ⚡", fontSize = 10.sp, fontWeight = FontWeight.Black, fontFamily = FontFamily.Monospace)
                                    }
                                }
                            }

                            // DNA Jogress / DigiXros Trigger Card
                            if (fusionRecipe != null) {
                                val targetSp = PetSpecies.getById(fusionRecipe.targetSpeciesId)
                                Spacer(modifier = Modifier.height(10.dp))
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1B2836)),
                                    shape = RoundedCornerShape(8.dp),
                                    border = BorderStroke(1.dp, Color(fusionRecipe.fusionType.badgeColorHex))
                                ) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Column(modifier = Modifier.weight(1f)) {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Icon(
                                                        imageVector = Icons.Default.Bolt,
                                                        contentDescription = null,
                                                        tint = Color(fusionRecipe.fusionType.badgeColorHex),
                                                        modifier = Modifier.size(16.dp)
                                                    )
                                                    Spacer(modifier = Modifier.width(4.dp))
                                                    Text(
                                                        text = "${fusionRecipe.fusionType.displayName.uppercase()} COMPATIBLE!",
                                                        fontSize = 10.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = Color(fusionRecipe.fusionType.badgeColorHex),
                                                        fontFamily = FontFamily.Monospace
                                                    )
                                                }
                                                Spacer(modifier = Modifier.height(2.dp))
                                                Text(
                                                    text = "${ownSp.name} + ${oppSp.name} → ${targetSp.name}",
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.ExtraBold,
                                                    color = Color.White
                                                )
                                                Text(
                                                    text = fusionRecipe.description,
                                                    fontSize = 10.sp,
                                                    color = Color.LightGray
                                                )
                                            }
                                            SpriteCanvasRenderer(
                                                speciesId = targetSp.id,
                                                frame = SpriteFrame.IDLE_A,
                                                size = 46.dp
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Button(
                                            onClick = {
                                                scope.launch {
                                                    val fusedState = JogressEngine.executeFusion(
                                                        primary = PetState(
                                                            speciesId = ownMonsterId,
                                                            maxHp = ownSp.baseHp,
                                                            currentHp = ownSp.baseHp,
                                                            victories = ownVictories,
                                                            effort = ownEffort
                                                        ),
                                                        secondarySpeciesId = opp.monsterId,
                                                        secondaryVictories = opp.historicalVictories,
                                                        secondaryEffort = opp.trainingEffort
                                                    )
                                                    ownMonsterId = fusedState.speciesId
                                                    ownVictories = fusedState.victories
                                                    ownEffort = fusedState.effort
                                                    db.phoneDao().insertSnapshot(
                                                        SyncedPetEntity(
                                                            syncedTimestampUtc = System.currentTimeMillis() / 1000L,
                                                            speciesId = fusedState.speciesId,
                                                            speciesName = targetSp.name,
                                                            currentHp = fusedState.currentHp,
                                                            maxHp = fusedState.maxHp,
                                                            hunger = fusedState.hunger,
                                                            strength = fusedState.strength,
                                                            weight = fusedState.weight,
                                                            ageSeconds = fusedState.ageSeconds,
                                                            careMistakes = fusedState.careMistakes,
                                                            effort = fusedState.effort,
                                                            overfeedCount = fusedState.overfeedCount,
                                                            totalBattles = fusedState.totalBattles,
                                                            victories = fusedState.victories
                                                        )
                                                    )
                                                    wearableManager.sendPetStateToWatch(fusedState)
                                                    DigiWearAppWidgetProvider.updateAllWidgets(context)
                                                    beamStatus = "DNA Digivolution Complete: Awakened ${targetSp.name}!"
                                                }
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(fusionRecipe.fusionType.badgeColorHex)),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text("DNA DIGIVOLVE WITH FRIEND'S CODE!", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color.Black, fontFamily = FontFamily.Monospace)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                validationError?.let {
                    Text(
                        text = it,
                        fontSize = 11.sp,
                        color = Color(0xFFFF5252),
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }

        // Battle Simulation Log Results
        battleResult?.let { result ->
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = DigiWearPalette.CardSurface),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, if (result.winnerIndex == 0) Color(0xFF00E676) else Color(0xFFEF5350))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "▸ CANONICAL V-PET COMBAT RESOLUTION LOG (${result.battleMode.displayName.uppercase()})",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = DigiWearPalette.CrestGold,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = "WINNER: ${result.winnerName ?: "DRAW"} (${result.totalRounds} ROUNDS)",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        color = if (result.winnerIndex == 0) Color(0xFF00E676) else Color(0xFFFF5252),
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = result.formulaSummary,
                        fontSize = 9.5.sp,
                        color = Color(0xFF80CBC4),
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    for (turn in result.turns) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp)
                                .background(DigiWearPalette.CardInner, RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "R${turn.round} [${turn.attackType} • ${turn.hitRatePercent}%]: ${turn.attackerName} → ${turn.defenderName}",
                                fontSize = 9.5.sp,
                                color = Color.White,
                                fontFamily = FontFamily.Monospace
                            )
                            Text(
                                text = "-${turn.damage} HP ${if (turn.isAdvantage) "[1.2x]" else ""} (${turn.defenderRemainingHp}HP)",
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (turn.isAdvantage) Color(0xFF00E676) else Color(0xFFFF8A80),
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Codec Bit-Map Inspector
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = DigiWearPalette.CardSurface),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, DigiWearPalette.CardBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "▸ 128-BIT PAYLOAD ARCHITECTURE",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFCC80),
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.height(6.dp))
                BitFieldRow("000 - 007", "Version (8-bit)", "0x01")
                BitFieldRow("008 - 023", "Monster ID (16-bit)", "$ownMonsterId (${ownSpecies.name})")
                BitFieldRow("024 - 027", "Stage Index (4-bit)", "${ownSpecies.stage.stageIndex}")
                BitFieldRow("028 - 031", "Attribute (4-bit)", ownSpecies.attribute.displayName)
                BitFieldRow("032 - 047", "Base Power (16-bit)", "${ownSpecies.basePower} PWR")
                BitFieldRow("080 - 095", "PRNG Seed (16-bit)", "0x${Integer.toHexString(ownSeed).uppercase()}")
                BitFieldRow("112 - 127", "HMAC-16 Hash (16-bit)", "Cryptographic Tamper-Proof")
            }
        }
    }

    // High-Impact Battle Cut-In Overlay
    activeCutInSpeciesId?.let { cutInId ->
        PhoneCutInOverlay(
            speciesId = cutInId,
            title = activeCutInTitle,
            autoDismissMs = null,
            onDismiss = { activeCutInSpeciesId = null }
        )
    }

    // Authentic Battle Encounter Flash Overlay
    encounterFlashEnemyId?.let { enemyId ->
        PhoneEncounterFlashOverlay(
            enemySpeciesId = enemyId,
            isBoss = false,
            onFinished = { encounterFlashEnemyId = null }
        )
    }

    // Real-Time Optical CameraX / MLKit QR Battle Scanner Overlay
    if (isCameraScannerOpen) {
        CameraBattleScanner(
            statusMessage = scannerStatusError,
            onDismiss = {
                isCameraScannerOpen = false
                scannerStatusError = null
            },
            onCodeScanned = { scannedCode ->
                if (!isAnimatingBattle) {
                    if (selectedBattleMode == BattleMode.WILD_SCAN) {
                        try {
                            val enc = WildScanEncounterGenerator.generateFromScan(
                                rawScanData = scannedCode,
                                scanSource = "CAMERA_SCAN",
                                playerStage = ownSpecies.stage
                            )
                            buzzerManager.playAttentionBeep()
                            activeWildEncounter = enc
                            inputCode = enc.commCode128
                            selectedEnvironment = enc.environmentHazard
                            decodedOpponent = CommCodeCodec.decodeAuto(enc.commCode128)
                            beamStatus = "📡 Camera Scanned Wild ${enc.species.name} (${enc.habitatZone})!"
                            isCameraScannerOpen = false
                            scannerStatusError = null
                            launchAnimatedBattle(enc.commCode128, BattleMode.WILD_SCAN, enc.environmentHazard)
                        } catch (e: Exception) {
                            scannerStatusError = "SCAN ERROR: ${e.message}"
                        }
                    } else {
                        try {
                            val payload = CommCodeCodec.decodeAuto(scannedCode)
                            // Cryptographically validated via HMAC-16!
                            buzzerManager.playAttentionBeep()
                            inputCode = CommCodeCodec.formatWithHyphens(scannedCode)
                            decodedOpponent = payload
                            validationError = null
                            scannerStatusError = null
                            isCameraScannerOpen = false
                            val oppSpecies = PetSpecies.getById(payload.monsterId)
                            beamStatus = "📷 QR Verified: Opponent ${oppSpecies.name} locked into Arena!"
                            // Immediately launch face-to-face battle arena!
                            launchAnimatedBattle(scannedCode, selectedBattleMode, selectedEnvironment)
                        } catch (e: Exception) {
                            // HMAC-16 fails or corrupted data: update status and keep scanning!
                            scannerStatusError = "HMAC-16 VERIFY FAILED: ${e.message ?: "Invalid checksum"}"
                        }
                    }
                }
            }
        )
    }
}
}

@Composable
private fun BitFieldRow(bits: String, name: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "Bits $bits", fontSize = 10.sp, color = Color.Gray, fontFamily = FontFamily.Monospace)
        Text(text = name, fontSize = 10.sp, color = Color.LightGray)
        Text(text = value, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White, fontFamily = FontFamily.Monospace)
    }
}
