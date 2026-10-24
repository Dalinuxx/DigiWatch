package com.digiwear.companion.sideload

import io.github.muntashirakon.adb.AbsAdbConnectionManager
import java.io.ByteArrayOutputStream
import java.io.File
import java.security.PrivateKey
import java.security.cert.Certificate
import java.util.concurrent.TimeUnit

/**
 * Custom ADB Connection Manager supporting Android 11+ and Wear OS 3/4/5 Wireless Debugging
 * over TLS 1.3 with SPAKE2 pairing authentication.
 *
 * Extends [AbsAdbConnectionManager] from MuntashirAkon/libadb-android, supplying
 * the on-device RSA-2048 private key and X.509 self-signed certificate.
 */
class DigiWearAdbConnectionManager(
    private val keyManager: AdbKeyManager
) : AbsAdbConnectionManager() {

    private var deviceName: String = "DigiWear-Companion"

    init {
        val api = try {
            val sdk = android.os.Build.VERSION.SDK_INT
            if (sdk > 0) sdk else 35
        } catch (_: Throwable) {
            35
        }
        setApi(api)
        setDeviceName("DigiWear-Companion")
        setTimeout(20, TimeUnit.SECONDS)
    }

    fun setDeviceName(name: String) {
        deviceName = name
    }

    public override fun getPrivateKey(): PrivateKey = keyManager.getPrivateKey()

    public override fun getCertificate(): Certificate = keyManager.getCertificate()

    public override fun getDeviceName(): String = deviceName

    /**
     * Executes SPAKE2 pairing with a smartwatch running Wear OS 3/4/5 (Android 11+).
     *
     * @param host Target watch IP address.
     * @param port Dynamic pairing port shown on the watch's "Pair new device" screen.
     * @param pairingCode 6-digit Wi-Fi pairing code displayed on the watch.
     */
    fun pairWatch(host: String, port: Int, pairingCode: String): Result<Boolean> {
        return runCatching {
            pair(host, port, pairingCode)
        }
    }

    /**
     * Establishes a TLS 1.3 or TCP connection to the smartwatch.
     *
     * @param host Target watch IP address.
     * @param port Operational debugging port displayed on the watch's Wireless Debugging screen.
     */
    fun connectWatch(host: String, port: Int): Result<Boolean> {
        return runCatching {
            connect(host, port)
        }
    }

    /**
     * Automatically discovers the smartwatch over mDNS (_adb-tls-connect._tcp and _adb._tcp)
     * and establishes an ADB TLS 1.3 or TCP connection.
     *
     * @param context Application context for NsdManager.
     * @param timeoutMs Maximum discovery wait time in milliseconds (default 4000ms).
     */
    fun autoConnectWatch(context: android.content.Context, timeoutMs: Long = 4000L): Result<Boolean> {
        return runCatching {
            autoConnect(context, timeoutMs)
        }
    }

    /**
     * Executes an arbitrary shell command on the connected smartwatch and returns its standard output.
     */
    fun execShell(command: String): String {
        val stream = openStream("shell:$command")
        return try {
            val inStream = stream.openInputStream()
            val buffer = ByteArray(4096)
            val outputStream = ByteArrayOutputStream()
            var bytesRead: Int
            while (inStream.read(buffer).also { bytesRead = it } != -1) {
                outputStream.write(buffer, 0, bytesRead)
            }
            outputStream.toString("UTF-8").trim()
        } finally {
            try { stream.close() } catch (_: Exception) {}
        }
    }

    fun pairDevice(host: String, port: Int, pairingCode: String): Result<Boolean> = pairWatch(host, port, pairingCode)

    /**
     * Installs an APK onto the connected smartwatch.
     *
     * Stages the APK to /data/local/tmp/digiwear_install.apk, executes pm install over shell,
     * and automatically removes the staged file upon completion.
     *
     * @param file Local APK file.
     * @param onProgress Optional progress callback emitting a float between 0.0 and 1.0.
     */
    fun installApk(file: File, onProgress: ((Float) -> Unit)? = null): Result<String> {
        return runCatching {
            if (!file.exists() || file.length() <= 0) {
                throw IllegalArgumentException("APK file does not exist or is empty: ${file.name}")
            }

            val totalLength = file.length()

            // Primary: Stage to /data/local/tmp/digiwear_install.apk and execute pm install.
            // This is the most reliable, rock-solid method across all Wear OS versions (Pixel, Samsung Galaxy Watch 4/5/6/7, etc.)
            val resultOutput = tryStageAndInstall(file, totalLength, onProgress)
            if (resultOutput.contains("Success", ignoreCase = true)) {
                return@runCatching resultOutput
            }

            // Fallback: Direct stream session if staging produced a non-success output
            val streamOutput = tryStreamSession(
                execCmd = "exec:pm install -r -d -g -S $totalLength",
                file = file,
                totalLength = totalLength,
                onProgress = onProgress
            )
            if (streamOutput.contains("Success", ignoreCase = true)) {
                return@runCatching streamOutput
            }

            throw IllegalStateException("Installation failed: $resultOutput (Fallback: $streamOutput)")
        }
    }

    private fun tryStreamSession(
        execCmd: String,
        file: File,
        totalLength: Long,
        onProgress: ((Float) -> Unit)?
    ): String {
        return try {
            val stream = openStream(execCmd)
            try {
                val outStream = stream.openOutputStream()
                file.inputStream().use { input ->
                    val buffer = ByteArray(64 * 1024)
                    var totalWritten = 0L
                    var read: Int
                    while (input.read(buffer).also { read = it } != -1) {
                        outStream.write(buffer, 0, read)
                        totalWritten += read
                        onProgress?.invoke(totalWritten.toFloat() / totalLength.toFloat())
                    }
                    outStream.flush()
                }

                val inStream = stream.openInputStream()
                val resultBuf = ByteArray(2048)
                val resultOut = ByteArrayOutputStream()
                var readBytes: Int
                while (inStream.read(resultBuf).also { readBytes = it } != -1) {
                    resultOut.write(resultBuf, 0, readBytes)
                }
                resultOut.toString("UTF-8").trim()
            } finally {
                try { stream.close() } catch (_: Exception) {}
            }
        } catch (e: Exception) {
            "Stream install error: ${e.message}"
        }
    }

    private fun tryStageAndInstall(
        file: File,
        totalLength: Long,
        onProgress: ((Float) -> Unit)?
    ): String {
        val tmpPath = "/data/local/tmp/digiwear_install.apk"

        return try {
            // First stage the APK file by streaming to cat > /data/local/tmp/digiwear_install.apk and closing stream to signal EOF
            val uploadStream = openStream("exec:sh -c \"cat > $tmpPath\"")
            try {
                val outStream = uploadStream.openOutputStream()
                file.inputStream().use { input ->
                    val buffer = ByteArray(64 * 1024)
                    var totalWritten = 0L
                    var read: Int
                    while (input.read(buffer).also { read = it } != -1) {
                        outStream.write(buffer, 0, read)
                        totalWritten += read
                        onProgress?.invoke(totalWritten.toFloat() / totalLength.toFloat())
                    }
                    outStream.flush()
                }
            } finally {
                try { uploadStream.close() } catch (_: Exception) {}
            }

            // Execute pm install on the staged file
            var output = execShell("pm install -r -d -g $tmpPath")
            if (!output.contains("Success", ignoreCase = true)) {
                // Fallback without -g in case of restricted permissions
                val fallbackOut = execShell("pm install -r -d $tmpPath")
                if (fallbackOut.contains("Success", ignoreCase = true)) {
                    output = fallbackOut
                }
            }
            execShell("rm -f $tmpPath")
            output
        } catch (e: Exception) {
            "Stage install error: ${e.message}"
        }
    }

    /**
     * Queries connected watch hardware, OS, and package details over ADB shell.
     */
    fun queryDetails(): ConnectedWatchDetails {
        val model = runCatching { execShell("getprop ro.product.model") }.getOrDefault("Android Watch").ifEmpty { "Android Watch" }
        val manufacturer = runCatching { execShell("getprop ro.product.manufacturer") }.getOrDefault("Generic").ifEmpty { "Generic" }
        val androidVer = runCatching { execShell("getprop ro.build.version.release") }.getOrDefault("Unknown").ifEmpty { "Unknown" }
        val sdkVer = runCatching { execShell("getprop ro.build.version.sdk").toIntOrNull() ?: 0 }.getOrDefault(0)

        val wearVer = runCatching {
            val v = execShell("getprop ro.wearable.platform.version")
            if (v.isNotEmpty()) v else execShell("getprop ro.build.version.wear")
        }.getOrDefault("")

        val pkgCheck = runCatching { execShell("pm list packages com.digiwear.wear") }.getOrDefault("")
        val isInstalled = pkgCheck.contains("package:com.digiwear.wear")

        val installedVersion = if (isInstalled) {
            val dumpsys = runCatching { execShell("dumpsys package com.digiwear.wear | grep -i versionName") }.getOrDefault("")
            dumpsys.substringAfter("versionName=").substringBefore("\n").trim().ifEmpty { "v1.0" }
        } else ""

        val batteryOutput = runCatching { execShell("dumpsys battery | grep -i level") }.getOrDefault("")
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
}
