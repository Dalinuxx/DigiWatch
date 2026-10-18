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
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.Inet4Address
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.NetworkInterface
import java.net.Socket

/**
 * Dual-Transport Watch Sync Manager supporting:
 * 1. Wear OS DataLayer (`com.google.android.gms.wearable`) for paired Wear OS smartwatches.
 * 2. AOSP / Standard Android Watch LAN & Hotspot Bridge (TCP port 45820 + UDP auto-discovery on 45821)
 *    for standalone Full Android Watches (Kospet, Zeblaze, Lokmat, Lemfo, Rogbid, etc.) and de-Googled devices.
 */
class PhoneWearableManager(private val context: Context) {

    companion object {
        const val AOSP_TCP_PORT = 45820
        const val AOSP_UDP_DISCOVERY_PORT = 45821
        private const val DISCOVERY_PROBE = "DIGIWATCH_DISCOVER"
        private const val DISCOVERY_REPLY_PREFIX = "DIGIWATCH_AOSP_NODE:"
        private const val AOSP_NODE_PREFIX = "aosp://"
    }

    private val prefs = context.getSharedPreferences("digiwatch_companion_prefs", Context.MODE_PRIVATE)
    private val json = Json { ignoreUnknownKeys = true }

    private val nodeClient by lazy {
        runCatching { Wearable.getNodeClient(context) }.getOrNull()
    }
    private val messageClient by lazy {
        runCatching { Wearable.getMessageClient(context) }.getOrNull()
    }

    fun getConfiguredAospWatchIp(): String =
        prefs.getString("aosp_watch_ip", "")?.trim() ?: ""

    fun setConfiguredAospWatchIp(ip: String) {
        val cleaned = ip.trim().substringBefore(":")
        prefs.edit().putString("aosp_watch_ip", cleaned).apply()
    }

    /**
     * Broadcasts a UDP discovery probe on local Wi-Fi / Hotspot interfaces to auto-discover
     * an AOSP / standard Android watch running [com.digiwatch.wear.datalayer.AospLanSyncServer].
     */
    suspend fun discoverAospWatchOnLan(timeoutMs: Int = 450): String? = withContext(Dispatchers.IO) {
        try {
            DatagramSocket().use { socket ->
                socket.broadcast = true
                socket.soTimeout = timeoutMs
                val probeBytes = DISCOVERY_PROBE.toByteArray(Charsets.UTF_8)

                val targets = mutableSetOf<InetAddress>()
                runCatching { targets.add(InetAddress.getByName("255.255.255.255")) }
                // Also add directed subnet broadcast addresses (e.g. 192.168.43.255 for Android Hotspot)
                runCatching {
                    val interfaces = NetworkInterface.getNetworkInterfaces()
                    if (interfaces != null) {
                        for (intf in interfaces.iterator()) {
                            if (!intf.isUp || intf.isLoopback) continue
                            for (ia in intf.interfaceAddresses) {
                                val bcast = ia.broadcast
                                if (bcast != null) targets.add(bcast)
                            }
                        }
                    }
                }

                for (target in targets) {
                    runCatching {
                        val packet = DatagramPacket(probeBytes, probeBytes.size, target, AOSP_UDP_DISCOVERY_PORT)
                        socket.send(packet)
                    }
                }

                val buf = ByteArray(256)
                val replyPacket = DatagramPacket(buf, buf.size)
                socket.receive(replyPacket)
                val replyText = String(replyPacket.data, 0, replyPacket.length, Charsets.UTF_8).trim()
                if (replyText.startsWith(DISCOVERY_REPLY_PREFIX)) {
                    val discoveredIp = replyPacket.address.hostAddress ?: return@withContext null
                    setConfiguredAospWatchIp(discoveredIp)
                    return@withContext discoveredIp
                }
            }
        } catch (_: Exception) {
        }
        null
    }

    private suspend fun sendTcpCommandToAospWatch(ip: String, path: String, payload: String): String? =
        withContext(Dispatchers.IO) {
            try {
                Socket().use { socket ->
                    socket.connect(InetSocketAddress(ip, AOSP_TCP_PORT), 1200)
                    socket.soTimeout = 2500
                    val writer = PrintWriter(socket.getOutputStream(), true, Charsets.UTF_8)
                    val reader = BufferedReader(InputStreamReader(socket.getInputStream(), Charsets.UTF_8))
                    writer.println(path)
                    writer.println(payload.replace("\n", " "))
                    writer.flush()
                    reader.readLine()
                }
            } catch (_: Exception) {
                null
            }
        }

    suspend fun getConnectedWatchNodes(): List<String> = withContext(Dispatchers.IO) {
        val result = mutableListOf<String>()
        // 1. Check Wear OS DataLayer nodes
        try {
            val nc = nodeClient
            if (nc != null) {
                val nodes = Tasks.await(nc.connectedNodes)
                result.addAll(nodes.map { it.id })
            }
        } catch (_: Exception) {
        }

        // 2. Check configured or auto-discovered AOSP / standard Android watch over Wi-Fi / Hotspot LAN
        val configuredIp = getConfiguredAospWatchIp()
        if (configuredIp.isNotEmpty()) {
            val ping = sendTcpCommandToAospWatch(configuredIp, "/sync/request_settings", "")
            if (!ping.isNullOrBlank() && ping != "ERR") {
                result.add("$AOSP_NODE_PREFIX$configuredIp")
            } else if (result.isEmpty()) {
                val discovered = discoverAospWatchOnLan(350)
                if (!discovered.isNullOrBlank()) {
                    result.add("$AOSP_NODE_PREFIX$discovered")
                }
            }
        } else if (result.isEmpty()) {
            val discovered = discoverAospWatchOnLan(350)
            if (!discovered.isNullOrBlank()) {
                result.add("$AOSP_NODE_PREFIX$discovered")
            }
        }

        result
    }

    /**
     * Sends `/sync/request_pet_state` to the paired Wear OS or AOSP smartwatch and awaits `/sync/response_pet_state`.
     */
    suspend fun requestPetStateFromWatch(): PetState? = withContext(Dispatchers.IO) {
        val nodes = getConnectedWatchNodes()
        if (nodes.isEmpty()) return@withContext null

        // Check AOSP LAN node first if present
        val aospNode = nodes.firstOrNull { it.startsWith(AOSP_NODE_PREFIX) }
        if (aospNode != null) {
            val ip = aospNode.removePrefix(AOSP_NODE_PREFIX)
            val resp = sendTcpCommandToAospWatch(ip, "/sync/request_pet_state", "")
            if (!resp.isNullOrBlank()) {
                runCatching { return@withContext json.decodeFromString<PetState>(resp) }
            }
        }

        val wearNodes = nodes.filterNot { it.startsWith(AOSP_NODE_PREFIX) }
        val mc = messageClient ?: return@withContext null
        if (wearNodes.isEmpty()) return@withContext null

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
            Tasks.await(mc.addListener(listener))
            val emptyPayload = ByteArray(0)
            for (node in wearNodes) {
                try {
                    Tasks.await(mc.sendMessage(node, "/sync/request_pet_state", emptyPayload))
                } catch (_: Exception) {}
            }
            withTimeoutOrNull(2500L) {
                deferred.await()
            }
        } catch (_: Exception) {
            null
        } finally {
            try {
                Tasks.await(mc.removeListener(listener))
            } catch (_: Exception) {}
        }
    }

    suspend fun sendPetStateToWatch(state: PetState): Boolean = withContext(Dispatchers.IO) {
        val nodes = getConnectedWatchNodes()
        if (nodes.isEmpty()) return@withContext false

        val jsonStr = json.encodeToString(state)
        val payload = jsonStr.toByteArray(Charsets.UTF_8)
        var success = false
        for (node in nodes) {
            if (node.startsWith(AOSP_NODE_PREFIX)) {
                val ip = node.removePrefix(AOSP_NODE_PREFIX)
                val resp = sendTcpCommandToAospWatch(ip, "/sync/update_pet_state", jsonStr)
                if (resp == "OK") success = true
            } else {
                try {
                    messageClient?.let { mc ->
                        Tasks.await(mc.sendMessage(node, "/sync/update_pet_state", payload))
                        success = true
                    }
                } catch (_: Exception) {}
            }
        }
        success
    }

    suspend fun requestSettingsFromWatch(): Map<String, String>? = withContext(Dispatchers.IO) {
        val nodes = getConnectedWatchNodes()
        if (nodes.isEmpty()) return@withContext null

        val aospNode = nodes.firstOrNull { it.startsWith(AOSP_NODE_PREFIX) }
        if (aospNode != null) {
            val ip = aospNode.removePrefix(AOSP_NODE_PREFIX)
            val resp = sendTcpCommandToAospWatch(ip, "/sync/request_settings", "")
            if (!resp.isNullOrBlank()) {
                runCatching { return@withContext json.decodeFromString<Map<String, String>>(resp) }
            }
        }

        val wearNodes = nodes.filterNot { it.startsWith(AOSP_NODE_PREFIX) }
        val mc = messageClient ?: return@withContext null
        if (wearNodes.isEmpty()) return@withContext null

        val deferred = CompletableDeferred<Map<String, String>?>()
        val listener = MessageClient.OnMessageReceivedListener { event ->
            if (event.path == "/sync/response_settings") {
                try {
                    val str = String(event.data, Charsets.UTF_8)
                    val map = json.decodeFromString<Map<String, String>>(str)
                    deferred.complete(map)
                } catch (_: Exception) {
                    deferred.complete(null)
                }
            }
        }

        try {
            Tasks.await(mc.addListener(listener))
            val emptyPayload = ByteArray(0)
            for (node in wearNodes) {
                try {
                    Tasks.await(mc.sendMessage(node, "/sync/request_settings", emptyPayload))
                } catch (_: Exception) {}
            }
            withTimeoutOrNull(2500L) {
                deferred.await()
            }
        } catch (_: Exception) {
            null
        } finally {
            try {
                Tasks.await(mc.removeListener(listener))
            } catch (_: Exception) {}
        }
    }

    suspend fun sendSettingsToWatch(
        themeId: String,
        vibrationEnabled: Boolean,
        threeButtonMode: Boolean,
        pendulumShakeMode: Boolean = false,
        soundEnabled: Boolean = false,
        nfcBridgeMode: String = "NFC_DUEL",
        displayShapeOverride: String? = null
    ): Boolean = withContext(Dispatchers.IO) {
        val nodes = getConnectedWatchNodes()
        if (nodes.isEmpty()) return@withContext false

        val map = buildMap {
            put("theme", themeId)
            put("vibration", vibrationEnabled.toString())
            put("vpet_3button_mode", threeButtonMode.toString())
            put("pendulum_shake_mode", pendulumShakeMode.toString())
            put("sound_enabled", soundEnabled.toString())
            put("nfc_bridge_mode", nfcBridgeMode)
            if (displayShapeOverride != null) {
                put("display_shape_override", displayShapeOverride)
            }
        }
        val jsonStr = json.encodeToString(map)
        val payload = jsonStr.toByteArray(Charsets.UTF_8)
        var success = false
        for (node in nodes) {
            if (node.startsWith(AOSP_NODE_PREFIX)) {
                val ip = node.removePrefix(AOSP_NODE_PREFIX)
                val resp = sendTcpCommandToAospWatch(ip, "/sync/update_settings", jsonStr)
                if (resp == "OK") success = true
            } else {
                try {
                    messageClient?.let { mc ->
                        Tasks.await(mc.sendMessage(node, "/sync/update_settings", payload))
                        success = true
                    }
                } catch (_: Exception) {}
            }
        }
        success
    }

    suspend fun sendRemoteCareCommand(action: String): PetState? = withContext(Dispatchers.IO) {
        val nodes = getConnectedWatchNodes()
        if (nodes.isEmpty()) return@withContext null

        val aospNode = nodes.firstOrNull { it.startsWith(AOSP_NODE_PREFIX) }
        if (aospNode != null) {
            val ip = aospNode.removePrefix(AOSP_NODE_PREFIX)
            val resp = sendTcpCommandToAospWatch(ip, "/sync/remote_care_action", action)
            if (!resp.isNullOrBlank()) {
                runCatching { return@withContext json.decodeFromString<PetState>(resp) }
            }
        }

        val wearNodes = nodes.filterNot { it.startsWith(AOSP_NODE_PREFIX) }
        val mc = messageClient ?: return@withContext null
        if (wearNodes.isEmpty()) return@withContext null

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
            Tasks.await(mc.addListener(listener))
            val payload = action.toByteArray(Charsets.UTF_8)
            for (node in wearNodes) {
                try {
                    Tasks.await(mc.sendMessage(node, "/sync/remote_care_action", payload))
                } catch (_: Exception) {}
            }
            withTimeoutOrNull(2500L) {
                deferred.await()
            }
        } catch (_: Exception) {
            null
        } finally {
            try {
                Tasks.await(mc.removeListener(listener))
            } catch (_: Exception) {}
        }
    }

    suspend fun sendCommCodeToWatch(code: String): Boolean = withContext(Dispatchers.IO) {
        val nodes = getConnectedWatchNodes()
        if (nodes.isEmpty()) return@withContext false

        val payload = code.toByteArray(Charsets.UTF_8)
        var success = false
        for (node in nodes) {
            if (node.startsWith(AOSP_NODE_PREFIX)) {
                val ip = node.removePrefix(AOSP_NODE_PREFIX)
                val resp = sendTcpCommandToAospWatch(ip, "/sync/battle_code", code)
                if (resp == "OK") success = true
            } else {
                try {
                    messageClient?.let { mc ->
                        Tasks.await(mc.sendMessage(node, "/sync/battle_code", payload))
                        success = true
                    }
                } catch (_: Exception) {}
            }
        }
        success
    }

    suspend fun sendCampaignToWatch(campaignJson: String): Boolean = withContext(Dispatchers.IO) {
        val nodes = getConnectedWatchNodes()
        if (nodes.isEmpty()) return@withContext false

        val payload = campaignJson.toByteArray(Charsets.UTF_8)
        var success = false
        for (node in nodes) {
            if (node.startsWith(AOSP_NODE_PREFIX)) {
                val ip = node.removePrefix(AOSP_NODE_PREFIX)
                val resp = sendTcpCommandToAospWatch(ip, "/sync/campaign_data", campaignJson)
                if (resp == "OK") success = true
            } else {
                try {
                    messageClient?.let { mc ->
                        Tasks.await(mc.sendMessage(node, "/sync/campaign_data", payload))
                        success = true
                    }
                } catch (_: Exception) {}
            }
        }
        success
    }
}
