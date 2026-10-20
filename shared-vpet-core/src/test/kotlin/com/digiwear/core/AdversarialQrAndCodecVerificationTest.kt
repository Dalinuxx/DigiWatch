package com.digiwear.core

import com.digiwear.core.codec.CommCodeCodec
import com.digiwear.core.codec.CommCodePayload
import com.digiwear.core.model.ElementalAttribute
import com.digiwear.core.model.PetSpecies
import com.digiwear.core.model.PetStage
import com.digiwear.core.qrcode.QrMatrixGenerator
import com.google.zxing.BinaryBitmap
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.common.HybridBinarizer
import com.google.zxing.qrcode.QRCodeReader
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test
import kotlin.random.Random

/**
 * Adversarial and empirical challenge test suite for Milestone 1.
 * Tests boundary conditions, cryptographic HMAC-16 integrity, standard QR finder pattern
 * and alignment pattern specifications, malformed character handling, and crash resilience.
 */
class AdversarialQrAndCodecVerificationTest {

    private fun decodeQrMatrix(matrix: Array<BooleanArray>, scale: Int = 4, quietZoneModules: Int = 4): String {
        val rawHeight = matrix.size
        val rawWidth = matrix[0].size
        val totalModulesW = rawWidth + quietZoneModules * 2
        val totalModulesH = rawHeight + quietZoneModules * 2
        val pixelWidth = totalModulesW * scale
        val pixelHeight = totalModulesH * scale

        val pixels = IntArray(pixelWidth * pixelHeight) { i ->
            val px = i % pixelWidth
            val py = i / pixelWidth
            val moduleX = (px / scale) - quietZoneModules
            val moduleY = (py / scale) - quietZoneModules

            if (moduleX in 0 until rawWidth && moduleY in 0 until rawHeight) {
                if (matrix[moduleY][moduleX]) 0xFF000000.toInt() else 0xFFFFFFFF.toInt()
            } else {
                0xFFFFFFFF.toInt()
            }
        }

        val source = RGBLuminanceSource(pixelWidth, pixelHeight, pixels)
        val bitmap = BinaryBitmap(HybridBinarizer(source))
        return QRCodeReader().decode(bitmap).text
    }

    // =========================================================================
    // SECTION 1: QR MATRIX GENERATOR SPECIFICATION & FINDER PATTERNS
    // =========================================================================

    @Test
    fun testStandardQrFinderPatternsStrictCompliance() {
        val testPayload = "0123456789ABCDEFGHJKMNPQRS"
        val matrix = QrMatrixGenerator.generateQrMatrix(testPayload, 29)

        assertEquals("Height must be exactly 29 for Version 3", 29, matrix.size)
        assertEquals("Width must be exactly 29 for Version 3", 29, matrix[0].size)

        // Helper to verify a standard 7x7 finder pattern at (originY, originX)
        fun verifyFinderPattern(originY: Int, originX: Int, name: String) {
            // 1. Outer 7x7 square border must be 100% dark
            for (i in 0..6) {
                assertTrue("$name: Row 0 module ($originX + $i, $originY) must be dark", matrix[originY][originX + i])
                assertTrue("$name: Row 6 module ($originX + $i, $originY + 6) must be dark", matrix[originY + 6][originX + i])
                assertTrue("$name: Col 0 module ($originX, $originY + $i) must be dark", matrix[originY + i][originX])
                assertTrue("$name: Col 6 module ($originX + 6, $originY + $i) must be dark", matrix[originY + i][originX + 6])
            }

            // 2. 5x5 inner ring must be 100% light
            for (i in 1..5) {
                assertFalse("$name: Row 1 module ($originX + $i, $originY + 1) must be light", matrix[originY + 1][originX + i])
                assertFalse("$name: Row 5 module ($originX + $i, $originY + 5) must be light", matrix[originY + 5][originX + i])
                assertFalse("$name: Col 1 module ($originX + 1, $originY + i) must be light", matrix[originY + i][originX + 1])
                assertFalse("$name: Col 5 module ($originX + 5, $originY + i) must be light", matrix[originY + i][originX + 5])
            }

            // 3. 3x3 core must be 100% dark
            for (y in 2..4) {
                for (x in 2..4) {
                    assertTrue("$name: Center module ($originX + $x, $originY + $y) must be dark", matrix[originY + y][originX + x])
                }
            }
        }

        // Top-Left Finder Pattern at (0, 0)
        verifyFinderPattern(0, 0, "Top-Left Finder Pattern")

        // Top-Right Finder Pattern at (0, 22)
        verifyFinderPattern(0, 22, "Top-Right Finder Pattern")

        // Bottom-Left Finder Pattern at (22, 0)
        verifyFinderPattern(22, 0, "Bottom-Left Finder Pattern")

        // Bottom-Right Corner must NOT match a finder pattern
        var brMatchesFinder = true
        for (y in 2..4) {
            for (x in 24..26) {
                if (!matrix[y + 20][x]) {
                    brMatchesFinder = false
                    break
                }
            }
        }
        // At least the separator or border should differentiate bottom-right
        assertFalse("Bottom-Right corner (22, 22) must NOT be a 7x7 finder pattern",
            matrix[22][22] && matrix[22][28] && matrix[28][22] && matrix[28][28] &&
            !matrix[23][23] && !matrix[27][27] && matrix[24][24] && matrix[25][25] && matrix[26][26] &&
            !matrix[23][24] && !matrix[23][25] && !matrix[23][26]
        )

        // 4. Verify Separators (1-module white quiet line around finder patterns)
        // Top-Left Separators: Row 7 (cols 0..7) and Col 7 (rows 0..7) must be light
        for (x in 0..7) {
            assertFalse("Top-Left horizontal separator at ($x, 7) must be light", matrix[7][x])
        }
        for (y in 0..7) {
            assertFalse("Top-Left vertical separator at (7, $y) must be light", matrix[y][7])
        }

        // Top-Right Separators: Row 7 (cols 21..28) and Col 21 (rows 0..7) must be light
        for (x in 21..28) {
            assertFalse("Top-Right horizontal separator at ($x, 7) must be light", matrix[7][x])
        }
        for (y in 0..7) {
            assertFalse("Top-Right vertical separator at (21, $y) must be light", matrix[y][21])
        }

        // Bottom-Left Separators: Row 21 (cols 0..7) and Col 7 (rows 21..28) must be light
        for (x in 0..7) {
            assertFalse("Bottom-Left horizontal separator at ($x, 21) must be light", matrix[21][x])
        }
        for (y in 21..28) {
            assertFalse("Bottom-Left vertical separator at (7, $y) must be light", matrix[y][7])
        }
    }

    @Test
    fun testStandardQrTimingPatternsAndAlignmentPattern() {
        val testPayload = "0123456789ABCDEFGHJKMNPQRS"
        val matrix = QrMatrixGenerator.generateQrMatrix(testPayload, 29)

        // Horizontal Timing Pattern: Row 6, cols 8..20 must alternate dark/light starting with dark
        for (x in 8..20) {
            val expectedDark = (x % 2 == 0)
            assertEquals("Horizontal timing pattern mismatch at col $x", expectedDark, matrix[6][x])
        }

        // Vertical Timing Pattern: Col 6, rows 8..20 must alternate dark/light starting with dark
        for (y in 8..20) {
            val expectedDark = (y % 2 == 0)
            assertEquals("Vertical timing pattern mismatch at row $y", expectedDark, matrix[y][6])
        }

        // QR Version 3 Alignment Pattern centered at (22, 22)
        // 5x5 pattern centered at (22, 22) -> rows 20..24, cols 20..24
        // Center module (22, 22) must be dark
        assertTrue("Alignment pattern center (22, 22) must be dark", matrix[22][22])

        // 3x3 ring around center must be light
        assertFalse("Alignment pattern ring (21, 22) must be light", matrix[22][21])
        assertFalse("Alignment pattern ring (23, 22) must be light", matrix[22][23])
        assertFalse("Alignment pattern ring (22, 21) must be light", matrix[21][22])
        assertFalse("Alignment pattern ring (22, 23) must be light", matrix[23][22])

        // 5x5 outer border must be dark
        assertTrue("Alignment pattern border (20, 20) must be dark", matrix[20][20])
        assertTrue("Alignment pattern border (24, 20) must be dark", matrix[20][24])
        assertTrue("Alignment pattern border (20, 24) must be dark", matrix[24][20])
        assertTrue("Alignment pattern border (24, 24) must be dark", matrix[24][24])
    }

    @Test
    fun testMultiVersionDimensionsAndFormula() {
        // Valid QR sizes follow (17 + 4 * version):
        // V1=21, V2=25, V3=29, V4=33, V5=37, V6=41
        val versions = listOf(1 to 21, 2 to 25, 3 to 29, 4 to 33, 5 to 37, 6 to 41)
        for ((v, size) in versions) {
            val m = QrMatrixGenerator.generateQrMatrix("TEST_V$v", size)
            assertEquals("Height for Version $v must be $size", size, m.size)
            assertEquals("Width for Version $v must be $size", size, m[0].size)
        }
    }

    @Test
    fun testQrMatrixGeneratorBoundaryAndInvalidInputs() {
        // Empty content
        try {
            QrMatrixGenerator.generateQrMatrix("")
            fail("Expected IllegalArgumentException for empty content")
        } catch (e: IllegalArgumentException) {
            assertTrue(e.message!!.contains("empty", ignoreCase = true))
        }

        // Sizes below Version 1 minimum (< 21)
        val invalidSmallSizes = listOf(-100, -1, 0, 1, 10, 16, 17, 20)
        for (sz in invalidSmallSizes) {
            try {
                QrMatrixGenerator.generateQrMatrix("TEST", sz)
                fail("Expected IllegalArgumentException for size $sz")
            } catch (e: IllegalArgumentException) {
                // pass
            }
        }

        // Sizes not matching formula (17 + 4 * V)
        val nonFormulaSizes = listOf(22, 23, 24, 26, 27, 28, 30, 31, 32, 50, 100)
        for (sz in nonFormulaSizes) {
            try {
                QrMatrixGenerator.generateQrMatrix("TEST", sz)
                fail("Expected IllegalArgumentException for non-formula size $sz")
            } catch (e: IllegalArgumentException) {
                assertTrue(e.message!!.contains("formula", ignoreCase = true) || e.message!!.contains("Invalid QR matrix size", ignoreCase = true))
            }
        }

        // Version exceeding 40 (size > 177)
        try {
            QrMatrixGenerator.generateQrMatrix("TEST", 181)
            fail("Expected IllegalArgumentException for Version 41 (size 181)")
        } catch (e: IllegalArgumentException) {
            assertTrue(e.message!!.contains("QR version must be between 1 and 40", ignoreCase = true))
        }
    }

    @Test
    fun testQrMatrixCapacityExceededHandledGracefully() {
        // In QR Version 1 (21x21, Level M), max byte capacity is ~14-17 bytes.
        // A 50-character string must exceed capacity and throw clean IllegalArgumentException.
        try {
            QrMatrixGenerator.generateQrMatrix("A".repeat(50), 21)
            fail("Expected IllegalArgumentException when capacity exceeded in Version 1")
        } catch (e: IllegalArgumentException) {
            assertTrue("Message should indicate data too large: ${e.message}",
                e.message!!.contains("Data too large", ignoreCase = true))
        }

        // In QR Version 3 (29x29, Level M), max byte capacity is ~42 bytes.
        // A 100-character string must cleanly throw IllegalArgumentException.
        try {
            QrMatrixGenerator.generateQrMatrix("A".repeat(100), 29)
            fail("Expected IllegalArgumentException when capacity exceeded in Version 3")
        } catch (e: IllegalArgumentException) {
            assertTrue("Message should indicate data too large: ${e.message}",
                e.message!!.contains("Data too large", ignoreCase = true))
        }
    }

    @Test
    fun testQrMatrixExtremeContentStrings() {
        // Strings with Unicode, special chars, symbols, injections
        val extremeStrings = listOf(
            "V1-03E8-CAFE-1337",
            "0123456789ABCDEFGHJKMNPQRS",
            "DIGIMON_BATTLE_2026!@#$%^&*()_+",
            "日本語デジモンバトル",
            "🦖💥⚡🛡️⚔️",
            "{\"player\":\"Taichi\",\"id\":401}",
            "SELECT * FROM digimon WHERE id = 1; --",
            "<battle fighter=\"greymon\" power=\"240\"/>",
            "A".repeat(30) // Boundary payload for V3-M
        )

        for (str in extremeStrings) {
            val matrix = QrMatrixGenerator.generateQrMatrix(str, 29)
            assertNotNull(matrix)
            assertEquals(29, matrix.size)

            val decoded = decodeQrMatrix(matrix)
            assertEquals("Decoded extreme string must match original perfectly", str, decoded)
        }
    }

    // =========================================================================
    // SECTION 2: COMM CODE CODEC ADVERSARIAL CRYPTOGRAPHIC INTEGRITY
    // =========================================================================

    @Test
    fun testHmac16RejectsEverySingleBitFlipInPayload() {
        val originalCode = CommCodeCodec.encode128(
            monsterId = 401,
            stage = PetStage.PERFECT,
            attribute = ElementalAttribute.VACCINE,
            basePowerHp = 380,
            historicalVictories = 24,
            trainingEffort = 4,
            battleRngSeed = 0x7E1A,
            timestampSalt = 0x9B42
        )

        val rawBytes = CommCodeCodec.base32ToBytes(originalCode)
        assertEquals("Comm Code binary length must be 16 bytes", 16, rawBytes.size)

        var totalBitFlipsTested = 0
        var totalRejections = 0

        // Test every single bit in the 14 data bytes (14 bytes * 8 bits = 112 individual bit flips)
        for (byteIdx in 0 until 14) {
            for (bit in 0 until 8) {
                totalBitFlipsTested++
                val tamperedBytes = rawBytes.copyOf()
                tamperedBytes[byteIdx] = (tamperedBytes[byteIdx].toInt() xor (1 shl bit)).toByte()

                val tamperedBase32 = CommCodeCodec.bytesToBase32(tamperedBytes)

                try {
                    CommCodeCodec.decode128(tamperedBase32)
                    fail("Security Breach: Tampered payload bit ($byteIdx, $bit) was accepted!")
                } catch (e: IllegalArgumentException) {
                    if (e.message!!.contains("integrity verification failed", ignoreCase = true)) {
                        totalRejections++
                    } else {
                        fail("Unexpected exception message on tampered bit ($byteIdx, $bit): ${e.message}")
                    }
                }
            }
        }

        assertEquals("All 112 data bit flips must be tested", 112, totalBitFlipsTested)
        assertEquals("HMAC-16 must reject 100% of single bit flips in the data payload", 112, totalRejections)
    }

    @Test
    fun testHmac16RejectsEverySingleBitFlipInChecksum() {
        val originalCode = CommCodeCodec.encode128(
            monsterId = 501,
            stage = PetStage.ULTIMATE,
            attribute = ElementalAttribute.VACCINE,
            basePowerHp = 520,
            historicalVictories = 50,
            trainingEffort = 4,
            battleRngSeed = 0xABCD,
            timestampSalt = 0x1234
        )

        val rawBytes = CommCodeCodec.base32ToBytes(originalCode)
        var totalChecksumBitsTested = 0
        var totalChecksumRejections = 0

        // Bytes 14 and 15 are the 16-bit HMAC checksum itself
        for (byteIdx in 14..15) {
            for (bit in 0 until 8) {
                totalChecksumBitsTested++
                val tamperedBytes = rawBytes.copyOf()
                tamperedBytes[byteIdx] = (tamperedBytes[byteIdx].toInt() xor (1 shl bit)).toByte()

                val tamperedBase32 = CommCodeCodec.bytesToBase32(tamperedBytes)

                try {
                    CommCodeCodec.decode128(tamperedBase32)
                    fail("Security Breach: Tampered checksum bit ($byteIdx, $bit) was accepted!")
                } catch (e: IllegalArgumentException) {
                    if (e.message!!.contains("integrity verification failed", ignoreCase = true)) {
                        totalChecksumRejections++
                    }
                }
            }
        }

        assertEquals(16, totalChecksumBitsTested)
        assertEquals(16, totalChecksumRejections)
    }

    @Test
    fun testAdversarialFieldSpoofingExhaustive() {
        // Base authentic fighter
        val monsterId = 301 // Greymon
        val stage = PetStage.ADULT
        val attribute = ElementalAttribute.VACCINE
        val basePower = 240
        val wins = 10
        val effort = 4
        val seed = 0x1234
        val salt = 0x5678

        val authenticCode = CommCodeCodec.encode128(
            monsterId, stage, attribute, basePower, wins, effort, seed, salt
        )
        val raw = CommCodeCodec.base32ToBytes(authenticCode)

        // Function to test an altered byte buffer
        fun assertTamperedPayloadRejected(alteredBytes: ByteArray, description: String) {
            val tamperedBase32 = CommCodeCodec.bytesToBase32(alteredBytes)
            try {
                CommCodeCodec.decodeAuto(tamperedBase32)
                fail("Adversarial payload was accepted: $description")
            } catch (e: IllegalArgumentException) {
                assertTrue("Must reject with integrity error or format error: ${e.message}",
                    e.message!!.contains("integrity verification failed", ignoreCase = true) ||
                    e.message!!.contains("Invalid", ignoreCase = true)
                )
            }
        }

        // Attack 1: Spoof Monster ID to 510 (Omnimon) while keeping other fields unchanged
        val spoofOmnimon = raw.copyOf()
        spoofOmnimon[1] = 0x02.toByte() // 510 = 0x01FE
        spoofOmnimon[2] = 0x00.toByte()
        assertTamperedPayloadRejected(spoofOmnimon, "Monster ID spoofed to Omnimon")

        // Attack 2: Spoof Base Power HP to 9999 (0x270F)
        val spoofPower = raw.copyOf()
        spoofPower[4] = 0x27.toByte()
        spoofPower[5] = 0x0F.toByte()
        assertTamperedPayloadRejected(spoofPower, "Base power spoofed to 9999")

        // Attack 3: Spoof Historical Victories to 65535 (0xFFFF)
        val spoofWins = raw.copyOf()
        spoofWins[6] = 0xFF.toByte()
        spoofWins[7] = 0xFF.toByte()
        assertTamperedPayloadRejected(spoofWins, "Victories spoofed to 65535")

        // Attack 4: Spoof Stage to ULTIMATE (0x5)
        val spoofStage = raw.copyOf()
        spoofStage[3] = ((0x5 shl 4) or (attribute.code and 0x0F)).toByte()
        assertTamperedPayloadRejected(spoofStage, "Stage spoofed to ULTIMATE")

        // Attack 5: Spoof Attribute to VIRUS (2)
        val spoofAttr = raw.copyOf()
        spoofAttr[3] = ((stage.stageIndex shl 4) or 0x02).toByte()
        assertTamperedPayloadRejected(spoofAttr, "Attribute spoofed to VIRUS")

        // Attack 6: Spoof Protocol Version to 0x02
        val spoofVersion = raw.copyOf()
        spoofVersion[0] = 0x02.toByte()
        assertTamperedPayloadRejected(spoofVersion, "Protocol version spoofed to 0x02")
    }

    @Test
    fun testCompact16BitFuzzingAndTamperDetection() {
        val code16 = CommCodeCodec.encode16(
            monsterId = 401,
            stage = PetStage.PERFECT,
            attribute = ElementalAttribute.VACCINE,
            basePowerHp = 380,
            battleRngSeed = 0x4321
        )
        assertEquals(16, code16.length)

        val rawBytes = CommCodeCodec.base32ToBytes(code16)

        var testedBits = 0
        var rejectedBits = 0

        // Test every bit in the 7 data bytes of the compact representation
        for (byteIdx in 0 until 7) {
            for (bit in 0 until 8) {
                testedBits++
                val tamperedBytes = rawBytes.copyOf()
                tamperedBytes[byteIdx] = (tamperedBytes[byteIdx].toInt() xor (1 shl bit)).toByte()

                val tamperedCode16 = CommCodeCodec.bytesToBase32(tamperedBytes).take(16)

                try {
                    CommCodeCodec.decode16(tamperedCode16)
                    fail("Security Breach: Tampered compact code bit ($byteIdx, $bit) was accepted!")
                } catch (e: IllegalArgumentException) {
                    if (e.message!!.contains("integrity verification failed", ignoreCase = true)) {
                        rejectedBits++
                    }
                }
            }
        }

        assertEquals(56, testedBits)
        assertEquals(56, rejectedBits)
    }

    @Test
    fun testDigiRomMalformedAndTamperedStrings() {
        // Valid DigiROM
        val validRom = "V1-01FE-0200-ABCD"
        val payload = CommCodeCodec.decodeAuto(validRom)
        assertNotNull(payload)

        // Invalid lengths (not 8 or 12 hex digits)
        val invalidLengths = listOf(
            "V1-",
            "V1-1234",
            "V1-1234-567",
            "V1-1234-5678-9",
            "V1-1234-5678-90AB-CDEF"
        )
        for (inv in invalidLengths) {
            try {
                CommCodeCodec.decodeAuto(inv)
                fail("Expected IllegalArgumentException for invalid DigiROM length: $inv")
            } catch (e: IllegalArgumentException) {
                assertTrue(e.message!!.contains("Invalid DigiROM hex packet", ignoreCase = true))
            }
        }

        // Invalid hex characters ('G', 'Z', special symbols)
        val invalidChars = listOf(
            "V1-123G-5678-90AB",
            "V1-ZZZZ-XXXX-YYYY",
            "V1-1234-56!8-90AB"
        )
        for (inv in invalidChars) {
            try {
                CommCodeCodec.decodeAuto(inv)
                fail("Expected IllegalArgumentException for invalid DigiROM hex characters: $inv")
            } catch (e: IllegalArgumentException) {
                assertTrue(e.message!!.contains("Invalid DigiROM hex packet", ignoreCase = true))
            }
        }
    }

    // =========================================================================
    // SECTION 3: MALFORMED CHARACTERS, ENCODINGS & BOUNDARY CONDITIONS
    // =========================================================================

    @Test
    fun testMalformedCharactersAndCrockfordAlphabetSafety() {
        // Invalid Crockford characters: 'U', 'u' (excluded to prevent profanity)
        val invalidUCode = "0123456789ABCDEFGHJKMNPQUR"
        try {
            CommCodeCodec.decode128(invalidUCode)
            fail("Expected rejection for character 'U'")
        } catch (e: IllegalArgumentException) {
            assertTrue(e.message!!.contains("Invalid Base32 character: 'U'", ignoreCase = true))
        }

        // Special characters and symbols
        val invalidSymbols = "!@#$%^&*()_+={}[]|\\:;\"'<>,.?/~`"
        for (sym in invalidSymbols) {
            val codeWithSym = "0123456789ABCDEFGHJKMNPQ$sym" + "S"
            try {
                CommCodeCodec.decodeAuto(codeWithSym)
                fail("Expected rejection for symbol '$sym'")
            } catch (e: IllegalArgumentException) {
                assertTrue(e.message!!.contains("Invalid Base32 character", ignoreCase = true) ||
                           e.message!!.contains("Invalid", ignoreCase = true))
            }
        }

        // Non-ASCII and Unicode characters
        val unicodeStrings = listOf("デジモン", "0123456789ABCDEFGHJKMNPQR💥", "0123456789\u0000BCDEFGHJKMNPQRS")
        for (uStr in unicodeStrings) {
            try {
                CommCodeCodec.decodeAuto(uStr)
                fail("Expected rejection for unicode string: $uStr")
            } catch (e: IllegalArgumentException) {
                // Expected
            }
        }
    }

    @Test
    fun testCrockfordAmbiguityMappingsAndCaseInsensitivity() {
        val validCode = CommCodeCodec.encode128(
            monsterId = 201,
            stage = PetStage.CHILD,
            attribute = ElementalAttribute.DATA,
            basePowerHp = 150,
            historicalVictories = 5,
            trainingEffort = 3,
            battleRngSeed = 0x5555
        )

        // Mapping '0' <-> 'O', '1' <-> 'I', '1' <-> 'L'
        val withSubstitutions = validCode
            .replace('0', 'O')
            .replace('1', 'I')

        val payloadSubstituted = CommCodeCodec.decodeAuto(withSubstitutions)
        val payloadOriginal = CommCodeCodec.decodeAuto(validCode)

        assertEquals(payloadOriginal.monsterId, payloadSubstituted.monsterId)
        assertEquals(payloadOriginal.basePowerHp, payloadSubstituted.basePowerHp)
        assertEquals(payloadOriginal.hmacChecksum, payloadSubstituted.hmacChecksum)

        // Case insensitivity: full lowercase
        val lowerCaseCode = validCode.lowercase()
        val payloadLower = CommCodeCodec.decodeAuto(lowerCaseCode)
        assertEquals(payloadOriginal.monsterId, payloadLower.monsterId)
        assertEquals(payloadOriginal.hmacChecksum, payloadLower.hmacChecksum)
    }

    @Test
    fun testArbitraryTruncatedAndOversizedStringsHandling() {
        // Truncated lengths (1 to 23 chars)
        for (len in 1..23) {
            val truncated = "0123456789ABCDEFGHJKMNPQRS".take(len)
            try {
                CommCodeCodec.decodeAuto(truncated)
                fail("Expected rejection for truncated string length $len")
            } catch (e: IllegalArgumentException) {
                // Expected
            }
        }

        // Whitespace only, empty, or only hyphens
        val emptyOrBlank = listOf("", "   ", "-", "--", " - - ")
        for (empty in emptyOrBlank) {
            try {
                CommCodeCodec.decodeAuto(empty)
                fail("Expected rejection for empty/blank string: '$empty'")
            } catch (e: IllegalArgumentException) {
                // Expected
            }
        }

        // Very large string (10,000 chars) must not throw OutOfMemoryError or StackOverflowError
        val hugeString = "0123456789ABCDEFGHJKMNPQRS".repeat(400)
        try {
            CommCodeCodec.decodeAuto(hugeString)
            fail("Expected rejection for huge string")
        } catch (e: IllegalArgumentException) {
            // Expected clean rejection
        }
    }

    // =========================================================================
    // SECTION 4: STRESS FUZZING & FULL ROUNDTRIP ORACLE
    // =========================================================================

    @Test
    fun testFullRoundtripFidelityOverMultipleSpecies() {
        val testSpeciesIds = listOf(201, 203, 301, 303, 304, 401, 404, 501, 503, 510, 515)
        val random = Random(42)

        for (speciesId in testSpeciesIds) {
            val species = PetSpecies.getById(speciesId)
            val power = species.basePower
            val wins = random.nextInt(0, 100)
            val effort = random.nextInt(0, 5)
            val seed = random.nextInt(0, 0xFFFF)
            val salt = random.nextInt(0, 0xFFFF)

            // 1. Encode 128
            val code128 = CommCodeCodec.encode128(
                monsterId = species.id,
                stage = species.stage,
                attribute = species.attribute,
                basePowerHp = power,
                historicalVictories = wins,
                trainingEffort = effort,
                battleRngSeed = seed,
                timestampSalt = salt
            )

            // 2. Generate 29x29 QR matrix
            val matrix = QrMatrixGenerator.generateQrMatrix(code128, 29)
            assertEquals(29, matrix.size)
            assertEquals(29, matrix[0].size)

            // 3. Optical Decode from QR Matrix
            val scannedText = decodeQrMatrix(matrix)
            assertEquals(code128, scannedText)

            // 4. Decode Comm Code and cryptographically verify
            val decodedPayload = CommCodeCodec.decodeAuto(scannedText)
            assertEquals(species.id, decodedPayload.monsterId)
            assertEquals(species.stage, decodedPayload.stage)
            assertEquals(species.attribute, decodedPayload.attribute)
            assertEquals(power, decodedPayload.basePowerHp)
            assertEquals(wins, decodedPayload.historicalVictories)
            assertEquals(effort, decodedPayload.trainingEffort)
            assertEquals(seed, decodedPayload.battleRngSeed)
            assertEquals(salt, decodedPayload.timestampSalt)
        }
    }

    @Test
    fun testChaosFuzzingCommCodeDecodeAutoNeverCrashes() {
        // Fuzz 1,000 completely random byte arrays / garbage strings into decodeAuto
        val random = Random(1337)
        var handledCleanly = 0

        for (i in 0 until 1000) {
            val fuzzLen = random.nextInt(0, 60)
            val fuzzChars = CharArray(fuzzLen) {
                // Randomly mix valid Crockford characters, symbols, and random ASCII
                when (random.nextInt(4)) {
                    0 -> CommCodeCodec.ALPHABET.random(random)
                    1 -> ('a'..'z').random(random)
                    2 -> ('!'..'/').random(random)
                    else -> random.nextInt(32, 127).toChar()
                }
            }
            val fuzzString = String(fuzzChars)

            try {
                CommCodeCodec.decodeAuto(fuzzString)
                // If it happened to be a valid string (extremely unlikely), that's fine
            } catch (e: IllegalArgumentException) {
                // Allowed and expected!
                handledCleanly++
            } catch (e: Throwable) {
                fail("CRASH / UNEXPECTED EXCEPTION on fuzz input '$fuzzString': ${e::class.java.name} - ${e.message}")
            }
        }

        assertTrue("Fuzzer must handle hundreds of corrupt strings cleanly", handledCleanly > 950)
    }
}
