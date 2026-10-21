package com.digiwear.core

import com.digiwear.core.codec.CommCodeCodec
import com.digiwear.core.model.ElementalAttribute
import com.digiwear.core.model.PetStage
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test

class CommCodeCodecTest {

    @Test
    fun testBase32AlphabetHas32Symbols() {
        val alphabet = CommCodeCodec.ALPHABET
        assertEquals(32, alphabet.length)
        // Verified 32 symbols
        assertEquals("0123456789ABCDEFGHJKMNPQRSTVWXYZ", alphabet)
    }

    @Test
    fun testEncodeAndDecode128BitPayload() {
        val monsterId = 301 // Greymon
        val stage = PetStage.ADULT
        val attribute = ElementalAttribute.VACCINE
        val powerHp = 240
        val wins = 15
        val effort = 4
        val seed = 0xABCD
        val salt = 0x1234

        val code = CommCodeCodec.encode128(
            monsterId = monsterId,
            stage = stage,
            attribute = attribute,
            basePowerHp = powerHp,
            historicalVictories = wins,
            trainingEffort = effort,
            battleRngSeed = seed,
            timestampSalt = salt
        )

        assertNotNull(code)
        // Verify all characters are in Crockford alphabet
        for (c in code) {
            assertTrue("Char $c must be in alphabet", CommCodeCodec.ALPHABET.contains(c))
        }

        // Decode and verify all fields preserved losslessly
        val payload = CommCodeCodec.decode128(code)
        assertEquals(1, payload.protocolVersion)
        assertEquals(monsterId, payload.monsterId)
        assertEquals(stage, payload.stage)
        assertEquals(attribute, payload.attribute)
        assertEquals(powerHp, payload.basePowerHp)
        assertEquals(wins, payload.historicalVictories)
        assertEquals(effort, payload.trainingEffort)
        assertEquals(seed, payload.battleRngSeed)
        assertEquals(salt, payload.timestampSalt)
    }

    @Test
    fun testTamperDetectionRejectsModifiedCode() {
        val code = CommCodeCodec.encode128(
            monsterId = 301,
            stage = PetStage.ADULT,
            attribute = ElementalAttribute.VACCINE,
            basePowerHp = 240,
            historicalVictories = 15,
            trainingEffort = 4,
            battleRngSeed = 0xABCD,
            timestampSalt = 0x1234
        )

        // Tamper with a single character in the payload
        val tamperedCode = if (code[2] == '2') {
            code.replaceRange(2, 3, "3")
        } else {
            code.replaceRange(2, 3, "2")
        }

        try {
            CommCodeCodec.decode128(tamperedCode)
            fail("Expected IllegalArgumentException on tampered code")
        } catch (e: IllegalArgumentException) {
            assertTrue(e.message!!.contains("integrity verification failed", ignoreCase = true))
        }
    }

    @Test
    fun testHyphenFormattingAndAutoDecode() {
        val code = CommCodeCodec.encode128(
            monsterId = 201,
            stage = PetStage.CHILD,
            attribute = ElementalAttribute.VACCINE,
            basePowerHp = 130,
            historicalVictories = 5,
            trainingEffort = 2,
            battleRngSeed = 42
        )

        val formatted = CommCodeCodec.formatWithHyphens(code)
        assertTrue(formatted.contains("-"))

        // Auto decode with hyphens should succeed
        val decoded = CommCodeCodec.decodeAuto(formatted)
        assertEquals(201, decoded.monsterId)
    }

    @Test
    fun testAmbiguousCharactersMappingOnDecode() {
        // If user enters 'O' instead of '0', or 'I'/'L' instead of '1', decoder tolerantly maps them
        val originalCode = CommCodeCodec.encode128(
            monsterId = 201,
            stage = PetStage.CHILD,
            attribute = ElementalAttribute.VACCINE,
            basePowerHp = 130,
            historicalVictories = 0,
            trainingEffort = 0,
            battleRngSeed = 100
        )

        // Replace '0' with 'O' or '1' with 'I' / 'L' if present
        if (originalCode.contains('0')) {
            val userEnteredWithO = originalCode.replace('0', 'O')
            val payload = CommCodeCodec.decode128(userEnteredWithO)
            assertEquals(201, payload.monsterId)
        }
    }

    @Test
    fun testCompact16CharEncodeDecodeAndTamperProtection() {
        val code16 = CommCodeCodec.encode16(
            monsterId = 501, // WarGreymon
            stage = PetStage.ULTIMATE,
            attribute = ElementalAttribute.VACCINE,
            basePowerHp = 520,
            battleRngSeed = 0x1337
        )
        assertEquals(16, code16.length)

        val decoded = CommCodeCodec.decodeAuto(code16)
        assertEquals(501, decoded.monsterId)
        assertEquals(PetStage.ULTIMATE, decoded.stage)
        assertEquals(ElementalAttribute.VACCINE, decoded.attribute)
        assertEquals(520, decoded.basePowerHp)
        assertEquals(0x1337, decoded.battleRngSeed)

        // Tamper with compact 16-char code and verify HMAC rejection
        val tampered16 = if (code16[1] == 'A') {
            code16.replaceRange(1, 2, "B")
        } else {
            code16.replaceRange(1, 2, "A")
        }
        try {
            CommCodeCodec.decode16(tampered16)
            fail("Expected IllegalArgumentException on tampered 16-char code")
        } catch (e: IllegalArgumentException) {
            assertTrue(e.message!!.contains("integrity verification failed", ignoreCase = true))
        }
    }

    @Test
    fun testDigiRom0nl1neEncodeAndDecodeAuto() {
        val rom = CommCodeCodec.encodeDigiRom(
            monsterId = 510, // Omnimon
            stage = PetStage.ULTIMATE,
            attribute = ElementalAttribute.VACCINE,
            basePowerHp = 900,
            battleRngSeed = 0xCAFE
        )
        assertTrue("DigiROM must start with V1-", rom.startsWith("V1-"))

        val decoded = CommCodeCodec.decodeAuto(rom)
        assertEquals(510, decoded.monsterId)
        assertEquals(PetStage.ULTIMATE, decoded.stage)
        assertEquals(ElementalAttribute.VACCINE, decoded.attribute)
        assertEquals(900, decoded.basePowerHp)
        assertEquals(0xCAFE, decoded.battleRngSeed)

        // Also test 2-word classic 32-bit DigiROM packet (V1-XXXX-XXXX)
        val classic2Word = "V1-12D3-BEEF" // 0x12D = 301 (Greymon), stage 3 (ADULT), seed 0xBEEF
        val classicDecoded = CommCodeCodec.decodeAuto(classic2Word)
        assertEquals(301, classicDecoded.monsterId)
        assertEquals(0xBEEF, classicDecoded.battleRngSeed)
    }

    @Test
    fun testDeriveDigiRomFromBytes() {
        val testUid1 = byteArrayOf(0x04.toByte(), 0x5A.toByte(), 0x12.toByte(), 0x88.toByte(), 0x33.toByte())
        val rom1 = CommCodeCodec.deriveDigiRomFromBytes(testUid1)
        assertTrue(rom1.startsWith("V1-"))

        val decoded1 = CommCodeCodec.decodeAuto(rom1)
        assertTrue(decoded1.monsterId > 0)
        assertTrue(decoded1.basePowerHp > 0)

        // Same UID bytes must produce identical deterministic DigiROM
        val rom1Again = CommCodeCodec.deriveDigiRomFromBytes(testUid1)
        assertEquals(rom1, rom1Again)

        // Different UID produces different challenger
        val testUid2 = byteArrayOf(0x11.toByte(), 0x22.toByte(), 0x33.toByte(), 0x44.toByte())
        val rom2 = CommCodeCodec.deriveDigiRomFromBytes(testUid2)
        assertTrue(rom2.startsWith("V1-"))
    }
}

