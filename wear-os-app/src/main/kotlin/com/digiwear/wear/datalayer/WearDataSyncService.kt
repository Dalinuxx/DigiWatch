package com.digiwear.wear.datalayer

import com.digiwear.core.model.BackgroundTheme
import com.digiwear.core.model.PetState
import com.digiwear.wear.complication.DigiWearComplicationService
import com.digiwear.wear.data.PetRepository
import com.digiwear.wear.tile.DigiWearTileService
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.Wearable
import com.google.android.gms.wearable.WearableListenerService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class WearDataSyncService : WearableListenerService() {

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)
    private val json = Json { ignoreUnknownKeys = true }

    override fun onMessageReceived(messageEvent: MessageEvent) {
        val repo = PetRepository.getInstance(applicationContext)

        when (messageEvent.path) {
            "/sync/request_pet_state" -> {
                scope.launch {
                    val state = repo.getPetState()
                    val payload = json.encodeToString(state).toByteArray(Charsets.UTF_8)
                    Wearable.getMessageClient(applicationContext)
                        .sendMessage(messageEvent.sourceNodeId, "/sync/response_pet_state", payload)
                }
            }
            "/sync/update_pet_state" -> {
                scope.launch {
                    try {
                        val str = String(messageEvent.data, Charsets.UTF_8)
                        val incomingState = json.decodeFromString<PetState>(str)
                        repo.savePetState(incomingState)
                    } catch (_: Exception) {}
                }
            }
            "/sync/request_settings" -> {
                scope.launch {
                    val settingsPayload = json.encodeToString(repo.getSettingsMap()).toByteArray(Charsets.UTF_8)
                    Wearable.getMessageClient(applicationContext)
                        .sendMessage(messageEvent.sourceNodeId, "/sync/response_settings", settingsPayload)
                }
            }
            "/sync/update_settings" -> {
                scope.launch {
                    try {
                        val str = String(messageEvent.data, Charsets.UTF_8)
                        val map = json.decodeFromString<Map<String, String>>(str)
                        map["theme"]?.let { themeId ->
                            repo.setTheme(BackgroundTheme.fromId(themeId))
                        }
                        map["vibration"]?.let { vibStr ->
                            repo.setVibrationEnabled(vibStr.toBoolean())
                        }
                        map["vpet_3button_mode"]?.let { btnStr ->
                            repo.setThreeButtonMode(btnStr.toBoolean())
                        }
                        map["pendulum_shake_mode"]?.let { shakeStr ->
                            repo.setPendulumShakeMode(shakeStr.toBoolean())
                        }
                        map["sound_enabled"]?.let { soundStr ->
                            repo.setSoundEnabled(soundStr.toBoolean())
                        }
                        map["nfc_bridge_mode"]?.let { nfcMode ->
                            repo.setNfcBridgeMode(nfcMode)
                        }
                        map["display_shape_override"]?.let { shapeMode ->
                            repo.setDisplayShapeOverride(shapeMode)
                        }
                        map["daily_step_goal"]?.toIntOrNull()?.let { goal ->
                            repo.setDailyStepGoal(goal)
                        }
                        map["lcd_contrast_level"]?.let { level ->
                            repo.setContrastLevel(level)
                        }
                        map["sleep_schedule"]?.let { schedule ->
                            repo.setSleepSchedule(schedule)
                        }
                        map["ambient_display_style"]?.let { style ->
                            repo.setAmbientStyle(style)
                        }
                        map["hazard_alerts_enabled"]?.let { alertStr ->
                            repo.setHazardAlertsEnabled(alertStr.toBoolean())
                        }
                        map["clock_format_24h"]?.let { clockStr ->
                            repo.setClockFormat24h(clockStr.toBoolean())
                        }
                    } catch (_: Exception) {}
                }
            }
            "/sync/remote_care_action" -> {
                scope.launch {
                    try {
                        val action = String(messageEvent.data, Charsets.UTF_8).trim()
                        val updatedState = when {
                            action == "FEED_MEAL" || action == "FEED" -> repo.feedMeal()
                            action == "FEED_PROTEIN" -> repo.feedProtein()
                            action == "CLEAN_WASTE" || action == "CLEAN" -> repo.cleanWaste()
                            action == "TOGGLE_LIGHTS" -> repo.toggleLights()
                            action == "TOGGLE_COLD_SLEEP" -> repo.toggleColdSleep()
                            action == "CURE_INJURY" -> repo.cureInjury()
                            action == "TRAIN_WIN" -> repo.train(won = true)
                            action.startsWith("HATCH_EGG:") -> {
                                val eggId = action.substringAfter("HATCH_EGG:").toIntOrNull() ?: 101
                                repo.hatchNewEgg(eggId)
                            }
                            else -> repo.getPetState()
                        }
                        if (action == "FEED_MEAL" || action == "FEED" || action == "CLEAN_WASTE" || action == "CLEAN") {
                            DigiWearComplicationService.requestUpdate(applicationContext)
                            DigiWearTileService.requestUpdate(applicationContext)
                        }
                        val payload = json.encodeToString(updatedState).toByteArray(Charsets.UTF_8)
                        Wearable.getMessageClient(applicationContext)
                            .sendMessage(messageEvent.sourceNodeId, "/sync/response_pet_state", payload)
                    } catch (_: Exception) {}
                }
            }
            "/sync/battle_code" -> {
                val code = String(messageEvent.data, Charsets.UTF_8).trim()
                if (code.isNotEmpty()) {
                    repo.setBeamedCommCode(code)
                }
            }
            "/sync/campaign_data" -> {
                val campaignJson = String(messageEvent.data, Charsets.UTF_8).trim()
                if (campaignJson.isNotEmpty()) {
                    repo.saveCustomCampaignJson(campaignJson)
                }
            }
            else -> super.onMessageReceived(messageEvent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}
