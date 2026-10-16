package com.digiwatch.wear.datalayer

import com.digiwatch.core.model.BackgroundTheme
import com.digiwatch.core.model.PetState
import com.digiwatch.wear.data.PetRepository
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
