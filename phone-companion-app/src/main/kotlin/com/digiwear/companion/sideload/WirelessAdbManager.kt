package com.digiwear.companion.sideload

import android.content.Context
import dadb.Dadb
import dadb.InstallResult
import dadb.UninstallResult
import io.github.muntashirakon.adb.AdbPairingRequiredException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class SideloadConnectionState(val label: String) {
    IDLE("Disconnected"),
    CONNECTING("Connecting to Watch..."),
    AUTHENTICATING("Authenticating (Check Watch Screen)..."),
    CONNECTED("Connected to Watch"),
    TRANSFERRING("Streaming APK to Watch..."),
    INSTALLING("Installing Package (-r -d -g)..."),
    GRANTING_PERMISSIONS("Granting Sensor & Notification Permissions..."),
    SUCCESS("Installation Successful!"),
    ERROR("Error")
}

data class ConnectedWatchDetails(
    val model: String = "Unknown Watch",
    val manufacturer: String = "Unknown",
    val androidVersion: String = "Unknown",
    val sdkVersion: Int = 0,
    val wearPlatform: String = "",
    val isDigiWearInstalled: Boolean = false,
    val installedVersionName: String = "",
    val batteryPercent: String = ""
)

class WirelessAdbManager(private val context: Context) {

    private val keyManager = AdbKeyManager(context)
    private var activeLibadb: DigiWearAdbConnectionManager? = null
    private var activeDadb: Dadb? = null

    private val _connectionState = MutableStateFlow(SideloadConnectionState.IDLE)
    val connectionState: StateFlow<SideloadConnectionState> = _connectionState.asStateFlow()

    private val _watchDetails = MutableStateFlow<ConnectedWatchDetails?>(null)
    val watchDetails: StateFlow<ConnectedWatchDetails?> = _watchDetails.asStateFlow()

    private val _consoleLogs = MutableStateFlow<List<String>>(emptyList())
    val consoleLogs: StateFlow<List<String>> = _consoleLogs.asStateFlow()

    private val _lastError = MutableStateFlow<String?>(null)
    val lastError: StateFlow<String?> = _lastError.asStateFlow()

    fun log(message: String) {
        val time = SimpleDateFormat("HH:mm:ss", Locale.US).format(Date())
        val entry = "[$time] $message"
        _consoleLogs.value = (_consoleLogs.value + entry).takeLast(250)
    }

    fun clearLogs() {
        _consoleLogs.value = emptyList()
    }

    fun getPublicKeyFingerprint(): String = keyManager.getPublicKeyFingerprintSha256()

    fun isConnected(): Boolean = (activeLibadb?.isConnected == true || activeDadb != null) &&
        _connectionState.value != SideloadConnectionState.IDLE &&
        _connectionState.value != SideloadConnectionState.ERROR &&
        _connectionState.value != SideloadConnectionState.CONNECTING

    /**
     * Performs SPAKE2 pairing over TLS 1.3 with a Wear OS 3/4/5 smartwatch (Android 11+).
     *
     * @param host Watch IP address.
     * @param port Dynamic pairing port displayed on the watch's "Pair new device" screen.
     * @param pairingCode 6-digit Wi-Fi pairing code displayed on the watch screen.
     */
    suspend fun pair(host: String, port: Int, pairingCode: String): Boolean = withContext(Dispatchers.IO) {
        val cleanHost = host.trim()
        val cleanCode = pairingCode.trim()
        if (cleanHost.isEmpty()) {
            _lastError.value = "Watch IP address is required for pairing"
            log("Pairing Error: Watch IP address is empty")
            return@withContext false
        }
        if (cleanCode.length != 6 || !cleanCode.all { it.isDigit() }) {
            _lastError.value = "Pairing code must be exactly 6 digits"
            log("Pairing Error: Pairing code must be a 6-digit number (e.g. 123456)")
            return@withContext false
        }
        if (port <= 0 || port > 65535) {
            _lastError.value = "Invalid pairing port"
            log("Pairing Error: Invalid port $port (must be 1-65535)")
            return@withContext false
        }

        log("--------------------------------------------------")
        log("INITIATING SPAKE2 WIRELESS PAIRING (WEAR OS 3/4/5)...")
        log("Target: $cleanHost:$port | Code: $cleanCode")
        log("Loading RSA-2048 key & self-signed X.509 certificate...")

        try {
            val manager = DigiWearAdbConnectionManager(keyManager)
            log("Transmitting pairing credentials to watch...")
            val result = manager.pairWatch(cleanHost, port, cleanCode)
            if (result.isSuccess && result.getOrDefault(false)) {
                log("SUCCESS: Pairing authenticated!")
                log("Smartwatch has trusted this companion app's credentials.")
                log("NEXT STEP: Return to 'Wireless debugging' on your watch, enter the operational port into 'Port' above, and tap CONNECT.")
                true
            } else {
                val err = result.exceptionOrNull()?.message ?: "Pairing request was rejected by the watch."
                _lastError.value = err
                log("Pairing rejected: $err")
                false
            }
        } catch (e: Exception) {
            val err = e.message ?: e.javaClass.simpleName
            _lastError.value = "Pairing failed: $err"
            log("SPAKE2 Pairing Error: $err")
            log("Troubleshooting:")
            log("1. Ensure watch screen is currently open on 'Pair new device'.")
            log("2. Verify that the 6-digit code and pairing port match what is shown on watch.")
            log("3. Ensure phone and watch are connected to the same Wi-Fi / Hotspot.")
            false
        }
    }

    /**
     * Connects to a smartwatch listening on [host]:[port] over wireless ADB.
     * Tries modern TLS 1.3 (Wear OS 3/4/5) first; falls back to legacy unencrypted ADB (AOSP / port 5555).
     */
    suspend fun connect(host: String, port: Int = 5555): Boolean = withContext(Dispatchers.IO) {
        disconnect()
        val cleanHost = host.trim()
        if (cleanHost.isEmpty()) {
            _lastError.value = "Watch IP address is required"
            log("Error: Watch IP address is empty")
            return@withContext false
        }

        _connectionState.value = SideloadConnectionState.CONNECTING
        _lastError.value = null
        log("Initiating Wireless ADB connection to $cleanHost:$port...")

        // 1. Attempt modern TLS 1.3 connection (Wear OS 3/4/5 / Android 11+)
        try {
            log("Attempting TLS 1.3 ADB connection (Wear OS 3/4/5)...")
            val manager = DigiWearAdbConnectionManager(keyManager)
            _connectionState.value = SideloadConnectionState.AUTHENTICATING

            val connResult = manager.connectWatch(cleanHost, port)
            if (connResult.isSuccess && manager.isConnected) {
                activeLibadb = manager
                log("TLS 1.3 ADB Handshake OK! Querying smartwatch hardware details...")
                val details = manager.queryDetails()
                _watchDetails.value = details

                _connectionState.value = SideloadConnectionState.CONNECTED
                log("SUCCESS: Connected to ${details.model} (${details.manufacturer}) via TLS 1.3")
                log("Watch OS: Android ${details.androidVersion} (API ${details.sdkVersion}) ${details.wearPlatform}")
                if (details.isDigiWearInstalled) {
                    log("Existing DigiWear installation detected: ${details.installedVersionName}")
                } else {
                    log("DigiWear is not yet installed on this watch")
                }
                return@withContext true
            } else {
                val ex = connResult.exceptionOrNull()
                val isPairingReq = ex is AdbPairingRequiredException ||
                    ex?.cause is AdbPairingRequiredException ||
                    ex?.message?.contains("pairing required", ignoreCase = true) == true
                if (isPairingReq) {
                    throw (ex as? AdbPairingRequiredException
                        ?: (ex?.cause as? AdbPairingRequiredException)
                        ?: AdbPairingRequiredException(ex?.message ?: "Pairing required"))
                }
                log("TLS connection could not be established: ${ex?.message ?: "Not accepted"}")
            }
        } catch (e: AdbPairingRequiredException) {
            _connectionState.value = SideloadConnectionState.ERROR
            val msg = "Watch requires 6-digit pairing code before connecting! Use 'PAIR WITH 6-DIGIT CODE' below first."
            _lastError.value = msg
            log("PAIRING REQUIRED: $msg")
            disconnect()
            return@withContext false
        } catch (e: Exception) {
            log("TLS connection attempt failed (${e.message ?: e.javaClass.simpleName}).")
        }

        // 2. Attempt legacy unencrypted TCP ADB fallback (dadb)
        log("Attempting legacy ADB fallback (AOSP / Port 5555)...")
        try {
            val keyPair = keyManager.getOrCreateKeyPair()
            val fp = keyManager.getPublicKeyFingerprintSha256()
            log("Using RSA-2048 key (SHA-256: $fp)")
            log("Waiting for watch authorization. If prompted on your watch screen, tap 'Always Allow'!")

            _connectionState.value = SideloadConnectionState.AUTHENTICATING
            val dadb = Dadb.create(cleanHost, port, keyPair)
            activeDadb = dadb

            log("ADB Handshake OK! Querying smartwatch hardware details...")
            val details = queryWatchDetails(dadb)
            _watchDetails.value = details

            _connectionState.value = SideloadConnectionState.CONNECTED
            log("SUCCESS: Connected to ${details.model} (${details.manufacturer}) via legacy ADB")
            log("Watch OS: Android ${details.androidVersion} (API ${details.sdkVersion}) ${details.wearPlatform}")
            if (details.isDigiWearInstalled) {
                log("Existing DigiWear installation detected: ${details.installedVersionName}")
            } else {
                log("DigiWear is not yet installed on this watch")
            }
            return@withContext true
        } catch (e: Exception) {
            _connectionState.value = SideloadConnectionState.ERROR
            val err = e.message ?: e.javaClass.simpleName
            _lastError.value = err
            log("Connection failed: $err")
            log("Troubleshooting tips:")
            log("1. For Wear OS 3/4/5 (Pixel Watch, Galaxy Watch 4+): You MUST pair with the 6-digit code first!")
            log("2. Ensure watch and phone are on the exact same Wi-Fi / Hotspot.")
            log("3. Ensure 'ADB Debugging' and 'Wireless Debugging' (or Debug over Wi-Fi) are enabled.")
            log("4. Check that the port matches the dynamic port shown in Wireless Debugging settings.")
            disconnect()
            return@withContext false
        }
    }

    private fun queryWatchDetails(dadb: Dadb): ConnectedWatchDetails {
        val model = dadb.shell("getprop ro.product.model").output.trim().ifEmpty { "Android Watch" }
        val manufacturer = dadb.shell("getprop ro.product.manufacturer").output.trim().ifEmpty { "Generic" }
        val androidVer = dadb.shell("getprop ro.build.version.release").output.trim().ifEmpty { "Unknown" }
        val sdkVer = dadb.shell("getprop ro.build.version.sdk").output.trim().toIntOrNull() ?: 0

        val wearVer = dadb.shell("getprop ro.wearable.platform.version").output.trim()
            .ifEmpty { dadb.shell("getprop ro.build.version.wear").output.trim() }

        val pkgCheck = dadb.shell("pm list packages com.digiwear.wear").output
        val isInstalled = pkgCheck.contains("package:com.digiwear.wear")

        val installedVersion = if (isInstalled) {
            val dumpsys = dadb.shell("dumpsys package com.digiwear.wear | grep -i versionName").output.trim()
            dumpsys.substringAfter("versionName=").substringBefore("\n").trim().ifEmpty { "v1.0" }
        } else ""

        val batteryOutput = dadb.shell("dumpsys battery | grep -i level").output.trim()
        val battery = batteryOutput.substringAfter("level:").trim().takeIf { it.isNotEmpty() }?.let { "$it%" } ?: ""

        return ConnectedWatchDetails(
            model = model,
            manufacturer = manufacturer,
            androidVersion = androidVer,
            sdkVersion = sdkVer,
            wearPlatform = if (wearVer.isNotEmpty()) "Wear OS $wearVer" else "",
            isDigiWearInstalled = isInstalled,
            installedVersionName = installedVersion,
            batteryPercent = battery
        )
    }

    private fun execShell(cmd: String): String {
        activeLibadb?.let { return it.execShell(cmd) }
        activeDadb?.let { return it.shell(cmd).output.trim() }
        return ""
    }

    /**
     * Installs the target APK file onto the connected watch.
     * Alias for [sideloadApk] using the active connection engine.
     */
    suspend fun installApk(
        apkFile: File,
        grantPermissions: Boolean = true,
        reinstallKeepData: Boolean = true,
        autoLaunch: Boolean = true
    ): Boolean = sideloadApk(apkFile, grantPermissions, reinstallKeepData, autoLaunch)

    /**
     * Sideloads the target APK file to the connected watch using the active ADB connection engine.
     */
    suspend fun sideloadApk(
        apkFile: File,
        grantPermissions: Boolean = true,
        reinstallKeepData: Boolean = true,
        autoLaunch: Boolean = true
    ): Boolean = withContext(Dispatchers.IO) {
        val libadb = activeLibadb
        val dadb = activeDadb
        if (libadb == null && dadb == null) {
            _lastError.value = "Watch is not connected"
            log("Cannot sideload: No active ADB connection")
            return@withContext false
        }

        if (!apkFile.exists() || apkFile.length() <= 0) {
            _lastError.value = "APK file not found or empty: ${apkFile.name}"
            log("Error: ${apkFile.absolutePath} not found")
            return@withContext false
        }

        val sizeMb = String.format(Locale.US, "%.2f MB", apkFile.length() / (1024.0 * 1024.0))
        log("--------------------------------------------------")
        log("SIDELOADING: ${apkFile.name} ($sizeMb)")
        log("Target: ${_watchDetails.value?.model ?: "Connected Watch"}")
        log("Engine: ${if (libadb != null) "TLS 1.3 (Wear OS 3/4/5)" else "Legacy TCP ADB"}")
        log("Options: Reinstall=$reinstallKeepData, GrantPermissions=$grantPermissions, AutoLaunch=$autoLaunch")

        _connectionState.value = SideloadConnectionState.TRANSFERRING
        var installSuccess = false

        try {
            if (libadb != null) {
                // Install via TLS connection manager
                _connectionState.value = SideloadConnectionState.INSTALLING
                log("Streaming APK to PackageInstaller session...")
                val result = libadb.installApk(apkFile)
                if (result.isSuccess) {
                    installSuccess = true
                    log("Package installer session committed successfully!")
                } else {
                    val err = result.exceptionOrNull()?.message ?: "Installation failed"
                    _lastError.value = err
                    log("TLS Install Error: $err")
                }
            } else if (dadb != null) {
                // Install via Dadb
                val installOptions = mutableListOf<String>()
                if (reinstallKeepData) {
                    installOptions.add("-r")
                    installOptions.add("-d")
                }
                if (grantPermissions) {
                    installOptions.add("-g")
                }

                _connectionState.value = SideloadConnectionState.INSTALLING
                val installResult: InstallResult = try {
                    dadb.install(apkFile, *installOptions.toTypedArray())
                } catch (e: Exception) {
                    InstallResult.Failure(e.message ?: "Streaming install failed")
                }

                if (installResult is InstallResult.Success) {
                    installSuccess = true
                    log("Package installer session committed successfully!")
                } else {
                    val failureReason = (installResult as InstallResult.Failure).reason
                    log("Direct streaming failed ($failureReason). Falling back to push + pm install...")

                    val remotePath = "/data/local/tmp/digiwear_install.apk"
                    _connectionState.value = SideloadConnectionState.TRANSFERRING
                    log("Pushing APK to $remotePath...")
                    dadb.push(apkFile, remotePath)
                    log("Push complete. Executing pm install...")

                    _connectionState.value = SideloadConnectionState.INSTALLING
                    val flags = if (reinstallKeepData) "-r -d" else ""
                    val grantFlag = if (grantPermissions) "-g" else ""
                    val pmCmd = "pm install $flags $grantFlag $remotePath"
                    log("Running: $pmCmd")
                    var pmOutput = dadb.shell(pmCmd).output.trim()
                    log("pm output: $pmOutput")

                    if (!pmOutput.contains("Success", ignoreCase = true)) {
                        log("Retrying pm install without -g flag in case of platform restrictions...")
                        pmOutput = dadb.shell("pm install $flags $remotePath").output.trim()
                        log("pm fallback output: $pmOutput")
                    }

                    // Clean up remote temp file
                    dadb.shell("rm -f $remotePath")

                    if (pmOutput.contains("Success", ignoreCase = true)) {
                        installSuccess = true
                        log("pm install succeeded!")
                    } else {
                        _connectionState.value = SideloadConnectionState.ERROR
                        _lastError.value = "pm install failed: $pmOutput"
                        log("INSTALLATION FAILED: $pmOutput")
                        return@withContext false
                    }
                }
            }

            if (!installSuccess) {
                _connectionState.value = SideloadConnectionState.ERROR
                return@withContext false
            }

            // Post-install permission grants
            if (grantPermissions) {
                _connectionState.value = SideloadConnectionState.GRANTING_PERMISSIONS
                log("Ensuring required runtime permissions are granted...")
                val permissions = listOf(
                    "android.permission.ACTIVITY_RECOGNITION",
                    "android.permission.BODY_SENSORS",
                    "android.permission.POST_NOTIFICATIONS"
                )
                for (perm in permissions) {
                    val out = execShell("pm grant com.digiwear.wear $perm")
                    if (out.isNotEmpty()) {
                        log("Grant $perm: $out")
                    } else {
                        log("Granted: ${perm.substringAfterLast('.')}")
                    }
                }
            }

            // Refresh watch details
            if (libadb != null) {
                _watchDetails.value = libadb.queryDetails()
            } else if (dadb != null) {
                _watchDetails.value = queryWatchDetails(dadb)
            }

            // Auto-launch if requested
            if (autoLaunch) {
                log("Launching DigiWear on watch (am start)...")
                val startOutput = execShell("am start -n com.digiwear.wear/.MainActivity")
                log("Launch: $startOutput")
            }

            _connectionState.value = SideloadConnectionState.SUCCESS
            log("==================================================")
            log("ALL OPERATIONS COMPLETED SUCCESSFULLY!")
            log("DigiWear is now running on your smartwatch!")
            log("==================================================")
            true
        } catch (e: Exception) {
            _connectionState.value = SideloadConnectionState.ERROR
            val err = e.message ?: e.javaClass.simpleName
            _lastError.value = err
            log("Sideload error: $err")
            false
        }
    }

    /**
     * Launches the DigiWear app on the watch.
     */
    suspend fun launchOnWatch(): Boolean = withContext(Dispatchers.IO) {
        if (!isConnected()) return@withContext false
        log("Executing am start on watch...")
        val out = execShell("am start -n com.digiwear.wear/.MainActivity")
        log("Launch result: $out")
        out.contains("Starting") || out.contains("Warning: Activity not started")
    }

    /**
     * Uninstalls any existing DigiWear package from the watch (resolves signature mismatch issues).
     */
    suspend fun uninstallFromWatch(): Boolean = withContext(Dispatchers.IO) {
        val libadb = activeLibadb
        val dadb = activeDadb
        if (libadb == null && dadb == null) return@withContext false

        log("Uninstalling com.digiwear.wear from watch...")
        var success = false

        if (libadb != null) {
            val out = libadb.execShell("pm uninstall com.digiwear.wear")
            log("pm uninstall output: $out")
            success = out.contains("Success", ignoreCase = true)
            if (success) {
                _watchDetails.value = libadb.queryDetails()
            }
        } else if (dadb != null) {
            val result = try {
                dadb.uninstall("com.digiwear.wear")
            } catch (_: Exception) {
                null
            }
            success = if (result is UninstallResult.Success) {
                true
            } else {
                val out = dadb.shell("pm uninstall com.digiwear.wear").output.trim()
                log("pm uninstall output: $out")
                out.contains("Success", ignoreCase = true)
            }
            if (success) {
                _watchDetails.value = queryWatchDetails(dadb)
            }
        }

        if (success) {
            log("com.digiwear.wear uninstalled.")
        } else {
            log("Uninstall did not find package or failed.")
        }
        success
    }

    /**
     * Refreshes the watch's status and installed package info.
     */
    suspend fun refreshWatchStatus() = withContext(Dispatchers.IO) {
        val libadb = activeLibadb
        val dadb = activeDadb
        if (libadb == null && dadb == null) return@withContext
        log("Refreshing watch diagnostic status...")
        if (libadb != null) {
            _watchDetails.value = libadb.queryDetails()
        } else if (dadb != null) {
            _watchDetails.value = queryWatchDetails(dadb)
        }
        log("Status updated.")
    }

    /**
     * Disconnects the active ADB session.
     */
    fun disconnect() {
        try {
            activeLibadb?.disconnect()
        } catch (_: Exception) {}
        try {
            activeLibadb?.close()
        } catch (_: Exception) {}
        activeLibadb = null

        try {
            activeDadb?.close()
        } catch (_: Exception) {}
        activeDadb = null

        _connectionState.value = SideloadConnectionState.IDLE
        _watchDetails.value = null
    }
}
