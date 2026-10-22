package com.digiwear.companion.sideload

import android.content.Context
import dadb.AdbKeyPair
import java.io.File
import java.security.MessageDigest
import java.util.Locale

/**
 * Manages the on-device RSA-2048 cryptographic key pair for wireless ADB authentication.
 * Keys are stored securely in the app's internal private storage (`context.filesDir/adb`).
 */
class AdbKeyManager(private val context: Context) {

    private val adbDir: File
        get() = File(context.filesDir, "adb").apply { if (!exists()) mkdirs() }

    val privateKeyFile: File
        get() = File(adbDir, "adbkey")

    val publicKeyFile: File
        get() = File(adbDir, "adbkey.pub")

    /**
     * Retrieves the existing [AdbKeyPair] or generates a new RSA-2048 pair if not present.
     */
    @Synchronized
    fun getOrCreateKeyPair(): AdbKeyPair {
        return if (privateKeyFile.exists() && publicKeyFile.exists()) {
            try {
                AdbKeyPair.read(privateKeyFile, publicKeyFile)
            } catch (_: Exception) {
                // If corrupted, regenerate
                generateNewKeyPair()
            }
        } else {
            generateNewKeyPair()
        }
    }

    private fun generateNewKeyPair(): AdbKeyPair {
        adbDir.mkdirs()
        AdbKeyPair.generate(privateKeyFile, publicKeyFile)
        return AdbKeyPair.read(privateKeyFile, publicKeyFile)
    }

    /**
     * Calculates the SHA-256 fingerprint of the public key for display in the UI.
     * When the watch shows "Allow USB debugging? RSA fingerprint: ...",
     * the user can verify this fingerprint matches.
     */
    fun getPublicKeyFingerprintSha256(): String {
        return try {
            val pubBytes = if (publicKeyFile.exists()) {
                publicKeyFile.readBytes()
            } else {
                getOrCreateKeyPair()
                if (publicKeyFile.exists()) publicKeyFile.readBytes() else "DIGIWEAR_COMPANION_KEY".toByteArray()
            }
            val md = MessageDigest.getInstance("SHA-256")
            val digest = md.digest(pubBytes)
            digest.joinToString(":") { String.format(Locale.US, "%02X", it) }
        } catch (e: Exception) {
            "UNAVAILABLE (${e.message})"
        }
    }

    /**
     * Calculates the MD5 fingerprint of the public key (legacy Android prompt format).
     */
    fun getPublicKeyFingerprintMd5(): String {
        return try {
            val pubBytes = if (publicKeyFile.exists()) {
                publicKeyFile.readBytes()
            } else {
                getOrCreateKeyPair()
                if (publicKeyFile.exists()) publicKeyFile.readBytes() else "DIGIWEAR_COMPANION_KEY".toByteArray()
            }
            val md = MessageDigest.getInstance("MD5")
            val digest = md.digest(pubBytes)
            digest.joinToString(":") { String.format(Locale.US, "%02X", it) }
        } catch (e: Exception) {
            "UNAVAILABLE"
        }
    }

    /**
     * Reads the public key content string (e.g. `base64 user@host`).
     */
    fun getPublicKeyString(): String {
        return if (publicKeyFile.exists()) {
            publicKeyFile.readText(Charsets.UTF_8).trim()
        } else {
            getOrCreateKeyPair()
            if (publicKeyFile.exists()) publicKeyFile.readText(Charsets.UTF_8).trim() else ""
        }
    }
}
