package com.digiwear.companion.sideload

import android.content.Context
import dadb.AdbKeyPair
import org.bouncycastle.asn1.x500.X500Name
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder
import java.io.File
import java.math.BigInteger
import java.security.KeyFactory
import java.security.MessageDigest
import java.security.PrivateKey
import java.security.PublicKey
import java.security.Security
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import java.security.interfaces.RSAPrivateCrtKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.RSAPublicKeySpec
import java.util.Base64
import java.util.Date
import java.util.Locale

/**
 * Manages the on-device RSA-2048 cryptographic key pair and X.509 certificate for wireless ADB authentication.
 * Keys and certificate are stored securely in the app's internal private storage (`context.filesDir/adb`).
 * Supports legacy unencrypted ADB (dadb) as well as TLS 1.3 / SPAKE2 wireless pairing (Wear OS 3/4/5, Android 11+).
 */
class AdbKeyManager(private val context: Context) {

    init {
        ensureBouncyCastleProvider()
    }

    private val adbDir: File
        get() = File(context.filesDir, "adb").apply { if (!exists()) mkdirs() }

    val privateKeyFile: File
        get() = File(adbDir, "adbkey")

    val publicKeyFile: File
        get() = File(adbDir, "adbkey.pub")

    val certificateFile: File
        get() = File(adbDir, "adbkey.crt")

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
        if (certificateFile.exists()) {
            certificateFile.delete()
        }
        AdbKeyPair.generate(privateKeyFile, publicKeyFile)
        return AdbKeyPair.read(privateKeyFile, publicKeyFile)
    }

    /**
     * Loads the RSA [PrivateKey] from [privateKeyFile] via [PKCS8EncodedKeySpec].
     */
    @Synchronized
    fun getPrivateKey(): PrivateKey {
        getOrCreateKeyPair()
        val rawBytes = privateKeyFile.readBytes()
        if (rawBytes.isNotEmpty() && rawBytes[0] == 0x30.toByte()) {
            return try {
                KeyFactory.getInstance("RSA").generatePrivate(PKCS8EncodedKeySpec(rawBytes))
            } catch (_: Exception) {
                parsePkcs1PrivateKey(rawBytes)
            }
        }
        val text = String(rawBytes, Charsets.UTF_8)
        val derBytes = if (text.contains("BEGIN")) {
            val cleaned = text
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replace("-----BEGIN RSA PRIVATE KEY-----", "")
                .replace("-----END RSA PRIVATE KEY-----", "")
                .replace("\\s+".toRegex(), "")
            Base64.getDecoder().decode(cleaned)
        } else {
            try {
                Base64.getDecoder().decode(text.replace("\\s+".toRegex(), ""))
            } catch (_: Exception) {
                rawBytes
            }
        }
        return try {
            KeyFactory.getInstance("RSA").generatePrivate(PKCS8EncodedKeySpec(derBytes))
        } catch (_: Exception) {
            parsePkcs1PrivateKey(derBytes)
        }
    }

    private fun parsePkcs1PrivateKey(bytes: ByteArray): PrivateKey {
        val rsaPriv = org.bouncycastle.asn1.pkcs.RSAPrivateKey.getInstance(bytes)
        val spec = java.security.spec.RSAPrivateCrtKeySpec(
            rsaPriv.modulus,
            rsaPriv.publicExponent,
            rsaPriv.privateExponent,
            rsaPriv.prime1,
            rsaPriv.prime2,
            rsaPriv.exponent1,
            rsaPriv.exponent2,
            rsaPriv.coefficient
        )
        return KeyFactory.getInstance("RSA").generatePrivate(spec)
    }

    /**
     * Reconstructs the [PublicKey] corresponding to [getPrivateKey].
     */
    @Synchronized
    fun getPublicKey(): PublicKey {
        val priv = getPrivateKey()
        if (priv is RSAPrivateCrtKey) {
            val pubSpec = RSAPublicKeySpec(priv.modulus, priv.publicExponent)
            return KeyFactory.getInstance("RSA").generatePublic(pubSpec)
        }
        return try {
            val pki = org.bouncycastle.asn1.pkcs.PrivateKeyInfo.getInstance(priv.encoded)
            val rsaPriv = org.bouncycastle.asn1.pkcs.RSAPrivateKey.getInstance(pki.parsePrivateKey())
            val pubSpec = RSAPublicKeySpec(rsaPriv.modulus, rsaPriv.publicExponent)
            KeyFactory.getInstance("RSA").generatePublic(pubSpec)
        } catch (e: Exception) {
            throw IllegalStateException("Unable to derive public key from private key: ${e.message}", e)
        }
    }

    /**
     * Retrieves the existing self-signed [X509Certificate] or generates a new 10-year certificate
     * matching the RSA key pair using BouncyCastle.
     */
    @Synchronized
    fun getCertificate(): X509Certificate {
        if (certificateFile.exists() && certificateFile.length() > 0) {
            try {
                val cf = CertificateFactory.getInstance("X.509")
                certificateFile.inputStream().use { stream ->
                    return cf.generateCertificate(stream) as X509Certificate
                }
            } catch (_: Exception) {
                // If corrupted, regenerate
            }
        }
        return generateAndSaveCertificate()
    }

    /**
     * Generates a 10-year self-signed X.509 certificate using BouncyCastle and persists it to [certificateFile].
     */
    @Synchronized
    fun generateAndSaveCertificate(): X509Certificate {
        ensureBouncyCastleProvider()
        val privKey = getPrivateKey()
        val pubKey = getPublicKey()

        val now = System.currentTimeMillis()
        val notBefore = Date(now - 24L * 60 * 60 * 1000) // 1 day in past for clock skew tolerance
        val notAfter = Date(now + 10L * 365 * 24 * 60 * 60 * 1000) // 10 years validity
        val serialNumber = BigInteger.valueOf(now)
        val issuerAndSubject = X500Name("CN=DigiWear-Companion")

        val certBuilder = JcaX509v3CertificateBuilder(
            issuerAndSubject,
            serialNumber,
            notBefore,
            notAfter,
            issuerAndSubject,
            pubKey
        )

        val signer = JcaContentSignerBuilder("SHA256withRSA").build(privKey)
        val certHolder = certBuilder.build(signer)
        val cert = JcaX509CertificateConverter().getCertificate(certHolder)

        // Save PEM format to certificateFile
        val mimeEncoder = Base64.getMimeEncoder(64, "\n".toByteArray(Charsets.US_ASCII))
        val pem = buildString {
            append("-----BEGIN CERTIFICATE-----\n")
            append(mimeEncoder.encodeToString(cert.encoded))
            append("\n-----END CERTIFICATE-----\n")
        }
        certificateFile.writeText(pem, Charsets.US_ASCII)

        return cert
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

    companion object {
        private fun ensureBouncyCastleProvider() {
            if (Security.getProvider("BC") == null) {
                try {
                    Security.addProvider(BouncyCastleProvider())
                } catch (_: Throwable) {}
            }
        }
    }
}
