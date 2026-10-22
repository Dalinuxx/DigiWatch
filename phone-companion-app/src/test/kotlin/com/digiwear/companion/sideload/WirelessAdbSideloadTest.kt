package com.digiwear.companion.sideload

import android.content.Context
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

/**
 * Comprehensive test suite verifying Wireless ADB Key Management,
 * APK source metadata handling, and Sideload Connection states.
 */
class WirelessAdbSideloadTest {

    @get:Rule
    val tempFolder = TemporaryFolder()

    private lateinit var mockContext: Context
    private lateinit var fakeFilesDir: File
    private lateinit var fakeCacheDir: File

    @Before
    fun setUp() {
        fakeFilesDir = tempFolder.newFolder("files")
        fakeCacheDir = tempFolder.newFolder("cache")

        // Lightweight mock Context returning our temporary folders
        mockContext = object : android.content.ContextWrapper(null) {
            override fun getFilesDir(): File = fakeFilesDir
            override fun getCacheDir(): File = fakeCacheDir
        }
    }

    @Test
    fun testAdbKeyManagerGeneratesAndPersistsKeys() {
        val keyManager = AdbKeyManager(mockContext)

        val keyPair = keyManager.getOrCreateKeyPair()
        assertNotNull("Generated key pair should not be null", keyPair)

        assertTrue("Private key file should exist", keyManager.privateKeyFile.exists())
        assertTrue("Public key file should exist", keyManager.publicKeyFile.exists())
        assertTrue("Private key file should not be empty", keyManager.privateKeyFile.length() > 0)
        assertTrue("Public key file should not be empty", keyManager.publicKeyFile.length() > 0)

        // Verify SHA-256 fingerprint formatting (32 hex bytes separated by colons: 32*2 + 31 = 95 chars)
        val sha256 = keyManager.getPublicKeyFingerprintSha256()
        assertNotNull(sha256)
        assertTrue("SHA-256 fingerprint should contain colons", sha256.contains(":"))
        assertEquals("SHA-256 fingerprint should have 32 colon-delimited hex octets", 32, sha256.split(":").size)

        // Verify MD5 fingerprint formatting (16 hex bytes separated by colons: 16*2 + 15 = 47 chars)
        val md5 = keyManager.getPublicKeyFingerprintMd5()
        assertNotNull(md5)
        assertTrue("MD5 fingerprint should contain colons", md5.contains(":"))
        assertEquals("MD5 fingerprint should have 16 colon-delimited hex octets", 16, md5.split(":").size)

        // Re-reading from disk returns the same key
        val keyPair2 = keyManager.getOrCreateKeyPair()
        assertNotNull(keyPair2)
        assertEquals("Fingerprint must remain consistent across reads", sha256, keyManager.getPublicKeyFingerprintSha256())
    }

    @Test
    fun testWatchApkInfoFormatting() {
        val dummyFile = tempFolder.newFile("dummy.apk")

        val zeroInfo = WatchApkInfo(dummyFile, "zero.apk", 0L, ApkSourceType.PHONE_STORAGE)
        assertEquals("0 B", zeroInfo.sizeFormatted)

        val oneMbInfo = WatchApkInfo(dummyFile, "app.apk", 1024L * 1024L, ApkSourceType.GITHUB_DOWNLOAD)
        assertEquals("1.00 MB", oneMbInfo.sizeFormatted)

        val watchApkSize = 60071973L // 60.07 MB actual wear-os-app-release.apk
        val releaseInfo = WatchApkInfo(dummyFile, "DigiWear-WearOS-release.apk", watchApkSize, ApkSourceType.EMBEDDED_ASSET)
        assertEquals("57.29 MB", releaseInfo.sizeFormatted)
    }

    @Test
    fun testApkSourceTypeLabels() {
        assertEquals("Phone Storage", ApkSourceType.PHONE_STORAGE.label)
        assertEquals("GitHub Release", ApkSourceType.GITHUB_DOWNLOAD.label)
        assertEquals("Embedded Asset", ApkSourceType.EMBEDDED_ASSET.label)
        assertEquals("Local Cache", ApkSourceType.LOCAL_CACHE.label)
    }

    @Test
    fun testWirelessAdbManagerInitialStateAndLogs() {
        val adbManager = WirelessAdbManager(mockContext)

        assertEquals("Initial state must be IDLE", SideloadConnectionState.IDLE, adbManager.connectionState.value)
        assertFalse("Must not be connected initially", adbManager.isConnected())
        assertTrue("Initial logs should be empty", adbManager.consoleLogs.value.isEmpty())

        adbManager.log("Test log entry 1")
        adbManager.log("Test log entry 2")

        assertEquals(2, adbManager.consoleLogs.value.size)
        assertTrue("Log entry must contain message text", adbManager.consoleLogs.value[0].contains("Test log entry 1"))
        assertTrue("Log entry must contain timestamp bracket", adbManager.consoleLogs.value[0].startsWith("["))

        adbManager.clearLogs()
        assertTrue("Logs should be empty after clear", adbManager.consoleLogs.value.isEmpty())
    }

    @Test
    fun testConnectedWatchDetailsDefaults() {
        val details = ConnectedWatchDetails(
            model = "Pixel Watch 2",
            manufacturer = "Google",
            androidVersion = "14",
            sdkVersion = 34,
            wearPlatform = "Wear OS 5.0",
            isDigiWearInstalled = true,
            installedVersionName = "v2.0.0",
            batteryPercent = "92%"
        )

        assertEquals("Pixel Watch 2", details.model)
        assertEquals("Google", details.manufacturer)
        assertEquals("14", details.androidVersion)
        assertEquals(34, details.sdkVersion)
        assertEquals("Wear OS 5.0", details.wearPlatform)
        assertTrue(details.isDigiWearInstalled)
        assertEquals("v2.0.0", details.installedVersionName)
        assertEquals("92%", details.batteryPercent)
    }

    @Test
    fun testSideloadConnectionStateEnumLabels() {
        assertEquals("Disconnected", SideloadConnectionState.IDLE.label)
        assertEquals("Connecting to Watch...", SideloadConnectionState.CONNECTING.label)
        assertEquals("Connected to Watch", SideloadConnectionState.CONNECTED.label)
        assertEquals("Streaming APK to Watch...", SideloadConnectionState.TRANSFERRING.label)
        assertEquals("Installing Package (-r -d -g)...", SideloadConnectionState.INSTALLING.label)
        assertEquals("Granting Sensor & Notification Permissions...", SideloadConnectionState.GRANTING_PERMISSIONS.label)
        assertEquals("Installation Successful!", SideloadConnectionState.SUCCESS.label)
        assertEquals("Error", SideloadConnectionState.ERROR.label)
    }
}
