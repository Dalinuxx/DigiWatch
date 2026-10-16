package com.digiwatch.companion.datalayer

import android.content.Context
import com.digiwatch.core.model.PetState
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class PhoneWearableManager(private val context: Context) {

    private val nodeClient = Wearable.getNodeClient(context)
    private val messageClient = Wearable.getMessageClient(context)
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun getConnectedWatchNodes(): List<String> = withContext(Dispatchers.IO) {
        try {
            val nodes = Tasks.await(nodeClient.connectedNodes)
            nodes.map { it.id }
        } catch (_: Exception) {
            emptyList()
        }
    }

    /**
     * Sends `/sync/request_pet_state` to the paired smartwatch and awaits `/sync/response_pet_state`.
     */
    suspend fun requestPetStateFromWatch(): PetState? = withContext(Dispatchers.IO) {
        val nodes = getConnectedWatchNodes()
        if (nodes.isEmpty()) return@withContext null

        val deferred = CompletableDeferred<PetState?>()
        val listener = MessageClient.OnMessageReceivedListener { event ->
            if (event.path == "/sync/response_pet_state") {
                try {
                    val str = String(event.data, Charsets.UTF_8)
                    val state = json.decodeFromString<PetState>(str)
                    deferred.complete(state)
                } catch (_: Exception) {
                    deferred.complete(null)
                }
            }
        }

        try {
            Tasks.await(messageClient.addListener(listener))
            val emptyPayload = ByteArray(0)
            for (node in nodes) {
                try {
                    Tasks.await(messageClient.sendMessage(node, "/sync/request_pet_state", emptyPayload))
                } catch (_: Exception) {}
            }
            withTimeoutOrNull(2500L) {
                deferred.await()
            }
        } catch (_: Exception) {
            null
        } finally {
            try {
                Tasks.await(messageClient.removeListener(listener))
            } catch (_: Exception) {}
        }
    }

    suspend fun sendPetStateToWatch(state: PetState): Boolean = withContext(Dispatchers.IO) {
        val nodes = getConnectedWatchNodes()
        if (nodes.isEmpty()) return@withContext false

        val payload = json.encodeToString(state).toByteArray(Charsets.UTF_8)
        var success = false
        for (node in nodes) {
            try {
                Tasks.await(messageClient.sendMessage(node, "/sync/update_pet_state", payload))
                success = true
            } catch (_: Exception) {}
        }
        success
    }

    suspend fun sendSettingsToWatch(
        themeId: String,
        vibrationEnabled: Boolean,
        threeButtonMode: Boolean,
        pendulumShakeMode: Boolean = false
    ): Boolean = withContext(Dispatchers.IO) {
        val nodes = getConnectedWatchNodes()
        if (nodes.isEmpty()) return@withContext false

        val map = mapOf(
            "theme" to themeId,
            "vibration" to vibrationEnabled.toString(),
            "vpet_3button_mode" to threeButtonMode.toString(),
            "pendulum_shake_mode" to pendulumShakeMode.toString()
        )
        val payload = json.encodeToString(map).toByteArray(Charsets.UTF_8)
        var success = false
        for (node in nodes) {
            try {
                Tasks.await(messageClient.sendMessage(node, "/sync/update_settings", payload))
                success = true
            } catch (_: Exception) {}
        }
        success
    }

    suspend fun sendCommCodeToWatch(code: String): Boolean = withContext(Dispatchers.IO) {
        val nodes = getConnectedWatchNodes()
        if (nodes.isEmpty()) return@withContext false

        val payload = code.toByteArray(Charsets.UTF_8)
        var success = false
        for (node in nodes) {
            try {
                Tasks.await(messageClient.sendMessage(node, "/sync/battle_code", payload))
                success = true
            } catch (_: Exception) {}
        }
        success
    }

    suspend fun sendCampaignToWatch(campaignJson: String): Boolean = withContext(Dispatchers.IO) {
        val nodes = getConnectedWatchNodes()
        if (nodes.isEmpty()) return@withContext false

        val payload = campaignJson.toByteArray(Charsets.UTF_8)
        var success = false
        for (node in nodes) {
            try {
                Tasks.await(messageClient.sendMessage(node, "/sync/campaign_data", payload))
                success = true
            } catch (_: Exception) {}
        }
        success
    }
}
