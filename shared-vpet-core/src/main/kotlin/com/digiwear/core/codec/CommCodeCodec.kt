package com.digiwear.core.codec

import com.digiwear.core.model.ElementalAttribute
import com.digiwear.core.model.PetStage
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

/**
 * 128-bit payload Comm Code representation.
 */
data class CommCodePayload(
    val protocolVersion: Int,        // 8-bit (0x01)
    val monsterId: Int,              // 16-bit
    val stage: PetStage,             // 4-bit (0x1..0x6)
    val attribute: ElementalAttribute, // 4-bit (0x0..0x3)
    val basePowerHp: Int,            // 16-bit (current HP / base power)
    val historicalVictories: Int,    // 16-bit
    val trainingEffort: Int,         // 16-bit
    val battleRngSeed: Int,          // 16-bit
    val timestampSalt: Int,          // 16-bit
    val hmacChecksum: Int            // 16-bit
)

/**
 * Base32 codec adhering to the standard 32-character Crockford Base32 alphabet:
 * 0123456789ABCDEFGHJKMNPQRSTVWXYZ
 * Designed for error-tolerant human entry.
 * Explicitly maps ambiguous characters: 'O' -> '0', 'I' and 'L' -> '1'.
 */
object CommCodeCodec {

    const val ALPHABET = "0123456789ABCDEFGHJKMNPQRSTVWXYZ"
    private const val PROTOCOL_VERSION = 0x01
    private const val HMAC_SECRET_KEY = "DIGIWEAR_COMM_V1_SECRET_KEY"

    private val HMAC_KEY_SPEC = SecretKeySpec(HMAC_SECRET_KEY.toByteArray(Charsets.UTF_8), "HmacSHA256")
    private val THREAD_LOCAL_MAC = object : ThreadLocal<Mac>() {
        override fun initialValue(): Mac = Mac.getInstance("HmacSHA256").apply {
            init(HMAC_KEY_SPEC)
        }
    }

    private val CHAR_TO_INDEX: Map<Char, Int> by lazy {
        val map = mutableMapOf<Char, Int>()
        ALPHABET.forEachIndexed { index, c -> map[c] = index }
        // Tolerant mappings for ambiguous glyphs
        map['O'] = 0
        map['I'] = 1
        map['L'] = 1
        map
    }

    /**
     * Builds and encodes a 128-bit payload into a Base32 Comm Code (26 characters).
     */
    fun encode128(
        monsterId: Int,
        stage: PetStage,
        attribute: ElementalAttribute,
        basePowerHp: Int,
        historicalVictories: Int,
        trainingEffort: Int,
        battleRngSeed: Int,
        timestampSalt: Int = (System.currentTimeMillis() / 1000L).toInt() and 0xFFFF
    ): String {
        // Build 14-byte pre-HMAC buffer
        val buffer = ByteBuffer.allocate(16).order(ByteOrder.BIG_ENDIAN)
        buffer.put(PROTOCOL_VERSION.toByte())
        buffer.putShort((monsterId and 0xFFFF).toShort())

        val stageAndAttr = ((stage.stageIndex and 0x0F) shl 4) or (attribute.code and 0x0F)
        buffer.put(stageAndAttr.toByte())

        buffer.putShort((basePowerHp and 0xFFFF).toShort())
        buffer.putShort((historicalVictories and 0xFFFF).toShort())
        buffer.putShort((trainingEffort and 0xFFFF).toShort())
        buffer.putShort((battleRngSeed and 0xFFFF).toShort())
        buffer.putShort((timestampSalt and 0xFFFF).toShort())

        val preHmacBytes = ByteArray(14)
        System.arraycopy(buffer.array(), 0, preHmacBytes, 0, 14)

        val hmac16 = computeHmac16(preHmacBytes)
        buffer.putShort(hmac16.toShort())

        return bytesToBase32(buffer.array())
    }

    /**
     * Decodes and cryptographically verifies a 128-bit Base32 Comm Code.
     * Throws [IllegalArgumentException] on checksum mismatch or invalid formatting.
     */
    fun decode128(code: String): CommCodePayload {
        val cleanCode = code.replace("-", "").replace(" ", "").trim().uppercase()
        val rawBytes = base32ToBytes(cleanCode)
        require(rawBytes.size >= 16) { "Comm Code binary payload too short (${rawBytes.size} bytes, expected 16)" }

        val buffer = ByteBuffer.wrap(rawBytes).order(ByteOrder.BIG_ENDIAN)
        val version = buffer.get().toInt() and 0xFF
        val monsterId = buffer.short.toInt() and 0xFFFF

        val stageAndAttr = buffer.get().toInt() and 0xFF
        val stageIndex = (stageAndAttr shr 4) and 0x0F
        val attrCode = stageAndAttr and 0x0F

        val powerHp = buffer.short.toInt() and 0xFFFF
        val victories = buffer.short.toInt() and 0xFFFF
        val effort = buffer.short.toInt() and 0xFFFF
        val seed = buffer.short.toInt() and 0xFFFF
        val salt = buffer.short.toInt() and 0xFFFF
        val recordedChecksum = buffer.short.toInt() and 0xFFFF

        // Verify cryptographic integrity HMAC-16
        val preHmacBytes = ByteArray(14)
        System.arraycopy(rawBytes, 0, preHmacBytes, 0, 14)
        val expectedChecksum = computeHmac16(preHmacBytes)

        require(recordedChecksum == expectedChecksum) {
            "Comm Code integrity verification failed! (expected=0x${Integer.toHexString(expectedChecksum)}, got=0x${Integer.toHexString(recordedChecksum)})"
        }

        return CommCodePayload(
            protocolVersion = version,
            monsterId = monsterId,
            stage = PetStage.fromIndex(stageIndex),
            attribute = ElementalAttribute.fromCode(attrCode),
            basePowerHp = powerHp,
            historicalVictories = victories,
            trainingEffort = effort,
            battleRngSeed = seed,
            timestampSalt = salt,
            hmacChecksum = recordedChecksum
        )
    }

    /**
     * Compact 16-character encoding (80 bits) optimized for fast touch input on smartwatches.
     */
    fun encode16(
        monsterId: Int,
        stage: PetStage,
        attribute: ElementalAttribute,
        basePowerHp: Int,
        battleRngSeed: Int
    ): String {
        val buffer = ByteBuffer.allocate(10).order(ByteOrder.BIG_ENDIAN)
        buffer.put(PROTOCOL_VERSION.toByte())

        val idAndStage = ((monsterId and 0x0FFF) shl 4) or (stage.stageIndex and 0x0F)
        buffer.putShort(idAndStage.toShort())

        val attrAndPower = ((attribute.code and 0x0F) shl 12) or (basePowerHp and 0x0FFF)
        buffer.putShort(attrAndPower.toShort())

        buffer.putShort((battleRngSeed and 0xFFFF).toShort())

        val preHmac = ByteArray(7)
        System.arraycopy(buffer.array(), 0, preHmac, 0, 7)
        val hmac16 = computeHmac16(preHmac)
        buffer.putShort(hmac16.toShort())
        buffer.put((hmac16 and 0xFF).toByte())

        val base32 = bytesToBase32(buffer.array())
        return base32.take(16)
    }

    /**
     * Encodes a Digimon 0nl1ne / D-Com / A-Com compatible Hex DigiROM packet string
     * formatted as `V1-XXXX-XXXX-XXXX`.
     */
    fun encodeDigiRom(
        monsterId: Int,
        stage: PetStage,
        attribute: ElementalAttribute,
        basePowerHp: Int,
        battleRngSeed: Int
    ): String {
        val word0 = ((monsterId and 0x0FFF) shl 4) or (stage.stageIndex and 0x0F)
        val word1 = ((attribute.code and 0x0F) shl 12) or (basePowerHp.coerceIn(0, 0x0FFF) and 0x0FFF)
        val word2 = battleRngSeed and 0xFFFF
        return String.format(java.util.Locale.US, "V1-%04X-%04X-%04X", word0, word1, word2)
    }

    /**
     * Decodes a Digimon 0nl1ne / D-Com / A-Com compatible Hex DigiROM string
     * (`V1-XXXX-XXXX-XXXX` 3-word or `V1-XXXX-XXXX` 2-word format).
     */
    fun decodeDigiRom(romCode: String): CommCodePayload {
        val normalized = romCode.trim().uppercase()
        val withoutPrefix = when {
            normalized.startsWith("V1-") || normalized.startsWith("V2-") ||
            normalized.startsWith("DM-") || normalized.startsWith("P1-") -> normalized.substring(3)
            else -> normalized
        }
        val cleanHex = withoutPrefix.replace("-", "").replace(" ", "")
        require(cleanHex.length in listOf(8, 12) && cleanHex.all { it in '0'..'9' || it in 'A'..'F' }) {
            "Invalid DigiROM hex packet: expected V1-XXXX-XXXX or V1-XXXX-XXXX-XXXX"
        }

        val word0 = cleanHex.substring(0, 4).toInt(16)
        val word1 = cleanHex.substring(4, 8).toInt(16)

        return if (cleanHex.length == 12) {
            val word2 = cleanHex.substring(8, 12).toInt(16)
            val monsterId = (word0 shr 4) and 0x0FFF
            val stageIndex = word0 and 0x0F
            val attrCode = (word1 shr 12) and 0x0F
            val power = word1 and 0x0FFF
            CommCodePayload(
                protocolVersion = PROTOCOL_VERSION,
                monsterId = monsterId,
                stage = PetStage.fromIndex(stageIndex),
                attribute = ElementalAttribute.fromCode(attrCode),
                basePowerHp = power,
                historicalVictories = 0,
                trainingEffort = 4,
                battleRngSeed = word2,
                timestampSalt = 0,
                hmacChecksum = 0
            )
        } else {
            // Classic 2-word 32-bit 2-prong DigiROM packet (V1-XXXX-XXXX)
            val candidateId = (word0 shr 4) and 0x0FFF
            val allSpecies = com.digiwear.core.model.PetSpecies.SORTED_SPECIES
            val species = com.digiwear.core.model.PetSpecies.SPECIES_REGISTRY[candidateId]
                ?: allSpecies[word0 % allSpecies.size]
            CommCodePayload(
                protocolVersion = PROTOCOL_VERSION,
                monsterId = species.id,
                stage = species.stage,
                attribute = species.attribute,
                basePowerHp = species.basePower,
                historicalVictories = (word1 shr 12) and 0x0F,
                trainingEffort = 4,
                battleRngSeed = word1 and 0xFFFF,
                timestampSalt = 0,
                hmacChecksum = 0
            )
        }
    }

    /**
     * Derives a deterministic Digimon 0nl1ne compatible Hex DigiROM opponent packet
     * from raw hardware bytes (e.g. NFC Tag UID, physical amiibo/card chips, BLE MAC).
     */
    fun deriveDigiRomFromBytes(tagBytes: ByteArray): String {
        val seed = if (tagBytes.isEmpty()) 0x1337 else {
            var hash = 0
            for (b in tagBytes) {
                hash = ((hash * 31) + (b.toInt() and 0xFF)) and 0xFFFF
            }
            if (hash == 0) 0x1337 else hash
        }
        val eligibleSpecies = com.digiwear.core.model.PetSpecies.COMBAT_ELIGIBLE_SPECIES
        val species = eligibleSpecies[seed % eligibleSpecies.size]
        return encodeDigiRom(
            monsterId = species.id,
            stage = species.stage,
            attribute = species.attribute,
            basePowerHp = species.basePower,
            battleRngSeed = seed
        )
    }

    fun decodeAuto(code: String): CommCodePayload {
        val trimmed = code.trim().uppercase()
        if (trimmed.startsWith("V1-") || trimmed.startsWith("V2-") ||
            trimmed.startsWith("DM-") || trimmed.startsWith("P1-")
        ) {
            return decodeDigiRom(trimmed)
        }
        val clean = trimmed.replace("-", "").replace(" ", "")
        return if (clean.length >= 24) {
            decode128(clean)
        } else {
            decode16(clean)
        }
    }

    fun decode16(code: String): CommCodePayload {
        val clean = code.replace("-", "").replace(" ", "").trim().uppercase()
        require(clean.length == 16) { "Compact Comm Code must be exactly 16 characters" }

        val bytes = base32ToBytes(clean)
        require(bytes.size >= 9) { "Invalid compact payload length" }

        val buffer = ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN)
        val version = buffer.get().toInt() and 0xFF

        val idAndStage = buffer.short.toInt() and 0xFFFF
        val monsterId = (idAndStage shr 4) and 0x0FFF
        val stageIndex = idAndStage and 0x0F

        val attrAndPower = buffer.short.toInt() and 0xFFFF
        val attrCode = (attrAndPower shr 12) and 0x0F
        val power = attrAndPower and 0x0FFF

        val seed = buffer.short.toInt() and 0xFFFF
        val recordedChecksum = buffer.short.toInt() and 0xFFFF

        val preHmac = ByteArray(7)
        System.arraycopy(bytes, 0, preHmac, 0, 7)
        val expectedChecksum = computeHmac16(preHmac)
        require(recordedChecksum == expectedChecksum) {
            "Compact Comm Code integrity verification failed! (expected=0x${Integer.toHexString(expectedChecksum)}, got=0x${Integer.toHexString(recordedChecksum)})"
        }

        return CommCodePayload(
            protocolVersion = version,
            monsterId = monsterId,
            stage = PetStage.fromIndex(stageIndex),
            attribute = ElementalAttribute.fromCode(attrCode),
            basePowerHp = power,
            historicalVictories = 0,
            trainingEffort = 0,
            battleRngSeed = seed,
            timestampSalt = 0,
            hmacChecksum = recordedChecksum
        )
    }

    fun formatWithHyphens(code: String): String {
        val trimmed = code.trim().uppercase()
        if (trimmed.startsWith("V1-") || trimmed.startsWith("V2-") ||
            trimmed.startsWith("DM-") || trimmed.startsWith("P1-")
        ) {
            return trimmed
        }
        val clean = trimmed.replace("-", "").replace(" ", "")
        return clean.chunked(4).joinToString("-")
    }

    // --- Base32 Encoding / Decoding using Crockford 32-character alphabet ---

    fun bytesToBase32(data: ByteArray): String {
        val sb = StringBuilder()
        var buffer = 0
        var bitsLeft = 0

        for (b in data) {
            buffer = (buffer shl 8) or (b.toInt() and 0xFF)
            bitsLeft += 8
            while (bitsLeft >= 5) {
                val index = (buffer shr (bitsLeft - 5)) and 0x1F
                sb.append(ALPHABET[index])
                bitsLeft -= 5
            }
        }

        if (bitsLeft > 0) {
            val index = (buffer shl (5 - bitsLeft)) and 0x1F
            sb.append(ALPHABET[index])
        }

        return sb.toString()
    }

    fun base32ToBytes(text: String): ByteArray {
        val clean = text.trim().uppercase()
        val result = mutableListOf<Byte>()
        var buffer = 0
        var bitsLeft = 0

        for (c in clean) {
            val value = CHAR_TO_INDEX[c]
                ?: throw IllegalArgumentException("Invalid Base32 character: '$c'")
            buffer = (buffer shl 5) or value
            bitsLeft += 5

            if (bitsLeft >= 8) {
                val b = ((buffer shr (bitsLeft - 8)) and 0xFF).toByte()
                result.add(b)
                bitsLeft -= 8
            }
        }

        return result.toByteArray()
    }

    private fun computeHmac16(data: ByteArray): Int {
        val mac = THREAD_LOCAL_MAC.get()
        val hash = mac.doFinal(data)
        return ((hash[0].toInt() and 0xFF) shl 8) or (hash[1].toInt() and 0xFF)
    }
}
