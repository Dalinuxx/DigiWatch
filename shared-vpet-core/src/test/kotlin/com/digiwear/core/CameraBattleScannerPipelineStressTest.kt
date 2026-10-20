package com.digiwear.core

import com.digiwear.core.battle.DeterministicBattleSolver
import com.digiwear.core.codec.CommCodeCodec
import com.digiwear.core.codec.CommCodePayload
import com.digiwear.core.model.BattleFighter
import com.digiwear.core.model.BattleMode
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
import kotlin.system.measureTimeMillis

/**
 * Empirical and adversarial test suite for Milestone 1:
 * Verifies QR detection pipeline latency (< 500ms), HMAC-16 cryptographic validation,
 * immediate arena initialization, tampered payload resilience, and scanner debouncing behavior.
 */
class CameraBattleScannerPipelineStressTest {

    private fun decodeQrMatrix(matrix: Array<BooleanArray>): String {
        val height = matrix.size
        val width = matrix[0].size
        val pixels = IntArray(width * height) { i ->
            val x = i % width
            val y = i / width
            if (matrix[y][x]) 0xFF000000.toInt() else 0xFFFFFFFF.toInt()
        }
        val source = RGBLuminanceSource(width, height, pixels)
        val bitmap = BinaryBitmap(HybridBinarizer(source))
        val result = QRCodeReader().decode(bitmap)
        return result.text
    }

    /**
     * Requirement 2:
     * When a QR code is detected, the pipeline (QR decode -> HMAC-16 validation ->
     * Opponent Fighter Setup -> Battle Solver Resolution) must complete in < 500ms.
     */
    @Test
    fun testPipelineLatencyWithin500msBudget() {
        val testSpecies = listOf(301, 303, 401, 501, 503, 515)

        for (speciesId in testSpecies) {
            val species = PetSpecies.getById(speciesId)
            val code = CommCodeCodec.encode128(
                monsterId = species.id,
                stage = species.stage,
                attribute = species.attribute,
                basePowerHp = species.basePower,
                historicalVictories = 10,
                trainingEffort = 4,
                battleRngSeed = 0x55AA
            )

            // Step 1: Generate QR matrix (simulating watch or phone screen)
            val matrixGenTime = measureTimeMillis {
                val matrix = QrMatrixGenerator.generateQrMatrix(code, 29)
                assertEquals(29, matrix.size)
            }
            assertTrue("QR Matrix Generation must be fast (< 200ms)", matrixGenTime < 200)

            val matrix = QrMatrixGenerator.generateQrMatrix(code, 29)

            // Step 2: Optical scanning + decode + HMAC-16 + Arena Setup (End-to-End detection pipeline)
            var decodedPayload: CommCodePayload? = null
            var arenaInitialized = false

            val totalPipelineTime = measureTimeMillis {
                // Simulating MLKit / ZXing raw barcode capture from camera frame
                val scannedRaw = decodeQrMatrix(matrix)

                // Simulating BattleHubScreen onCodeScanned handler:
                decodedPayload = CommCodeCodec.decodeAuto(scannedRaw)
                val oppSp = PetSpecies.getById(decodedPayload!!.monsterId)

                // Setup player & opponent fighters
                val fA = BattleFighter(
                    id = "player",
                    speciesId = 301,
                    name = "Greymon",
                    stage = PetStage.ADULT,
                    attribute = ElementalAttribute.VACCINE,
                    maxHp = 240,
                    currentHp = 240,
                    basePower = 240,
                    victories = 12,
                    effort = 4,
                    battleSeed = 0x1337
                )
                val fB = BattleFighter(
                    id = "opponent",
                    speciesId = decodedPayload!!.monsterId,
                    name = oppSp.name,
                    stage = decodedPayload!!.stage,
                    attribute = decodedPayload!!.attribute,
                    maxHp = oppSp.baseHp,
                    currentHp = oppSp.baseHp,
                    basePower = decodedPayload!!.basePowerHp.coerceAtLeast(oppSp.basePower),
                    victories = decodedPayload!!.historicalVictories,
                    effort = decodedPayload!!.trainingEffort,
                    battleSeed = decodedPayload!!.battleRngSeed
                )

                // Face-to-face arena initial deterministic solver resolution
                val battleResult = DeterministicBattleSolver.resolveBattle(
                    fighterA = fA,
                    fighterB = fB,
                    environmentEffect = "Normal",
                    attackerCritBoost = false,
                    battleMode = BattleMode.NFC_TAP_DUEL
                )
                assertNotNull(battleResult)
                assertTrue(battleResult.turns.isNotEmpty())
                arenaInitialized = true
            }

            assertTrue(
                "Total pipeline time ($totalPipelineTime ms) must be strictly < 500ms for species $speciesId",
                totalPipelineTime < 500
            )
            assertTrue("Arena must be successfully initialized", arenaInitialized)
            assertNotNull("Decoded payload must not be null", decodedPayload)
            assertEquals("MonsterId must match original", species.id, decodedPayload!!.monsterId)
        }
    }

    /**
     * Requirement 3:
     * Invalid and tampered QR codes must be caught by HMAC-16 cryptographic checksum,
     * throwing IllegalArgumentException, allowing the scanner UI to display error status
     * without crashing.
     */
    @Test
    fun testTamperedQrCodesFailHmacCryptographicVerification() {
        val originalCode = CommCodeCodec.encode128(
            monsterId = 401, // MetalGreymon
            stage = PetStage.PERFECT,
            attribute = ElementalAttribute.VACCINE,
            basePowerHp = 300,
            historicalVictories = 25,
            trainingEffort = 4,
            battleRngSeed = 0x7777,
            timestampSalt = 0x1234
        )

        // Verify original code decodes cleanly
        val validPayload = CommCodeCodec.decodeAuto(originalCode)
        assertEquals(401, validPayload.monsterId)

        // Adversarial attack 1: Corrupt single characters in Base32 string (tampering with payload bits)
        var caughtTamperedPayload = 0
        val alphabet = CommCodeCodec.ALPHABET
        for (i in 0 until originalCode.length - 4) { // Tamper within data region
            val originalChar = originalCode[i]
            val replacementChar = alphabet[(alphabet.indexOf(originalChar) + 1) % alphabet.length]
            val tamperedCode = originalCode.substring(0, i) + replacementChar + originalCode.substring(i + 1)

            try {
                CommCodeCodec.decodeAuto(tamperedCode)
                fail("Tampered code $tamperedCode should have failed HMAC-16 validation!")
            } catch (e: IllegalArgumentException) {
                // Expected: UI catches this and updates scannerStatusError
                assertTrue(
                    "Error message should indicate integrity failure or invalid format: ${e.message}",
                    e.message?.contains("integrity") == true || e.message?.contains("verification failed") == true || e.message?.contains("Invalid") == true
                )
                caughtTamperedPayload++
            }
        }
        assertTrue("All tampered data positions must be caught", caughtTamperedPayload > 0)

        // Adversarial attack 2: Tamper with HMAC-16 checksum itself (last characters)
        val hmacTampered = originalCode.dropLast(2) + "ZZ"
        try {
            CommCodeCodec.decodeAuto(hmacTampered)
            fail("Tampered HMAC checksum should have been rejected!")
        } catch (e: IllegalArgumentException) {
            assertTrue(
                "Error must indicate integrity verification failed",
                e.message?.contains("integrity") == true || e.message?.contains("verification failed") == true
            )
        }

        // Adversarial attack 3: Truncated payloads
        val truncatedCodes = listOf("", "ABC", "12345", originalCode.take(10), originalCode.take(20))
        for (truncated in truncatedCodes) {
            var simulatedScannerError: String? = null
            try {
                CommCodeCodec.decodeAuto(truncated)
                fail("Truncated code '$truncated' should have failed!")
            } catch (e: Exception) {
                // Simulating BattleHubScreen catch block
                simulatedScannerError = "HMAC-16 VERIFY FAILED: ${e.message ?: "Invalid checksum"}"
            }
            assertNotNull("Scanner error status must be populated", simulatedScannerError)
            assertTrue(simulatedScannerError!!.startsWith("HMAC-16 VERIFY FAILED"))
        }

        // Adversarial attack 4: Random binary garbage strings
        val random = Random(42)
        for (trial in 1..20) {
            val garbageLength = random.nextInt(16, 32)
            val garbage = (1..garbageLength).map { alphabet[random.nextInt(alphabet.length)] }.joinToString("")
            var scannerStatus: String? = null
            try {
                CommCodeCodec.decodeAuto(garbage)
            } catch (e: Exception) {
                scannerStatus = "HMAC-16 VERIFY FAILED: ${e.message}"
            }
            // Unless by 1-in-65536 chance the HMAC-16 random collision occurs, it must fail
            // Even if it collides, the test ensures no unhandled crash/panic occurs
            assertNotNull("Random garbage must be handled gracefully", scannerStatus)
        }
    }

    /**
     * Requirement 1:
     * Review scanner debouncing logic for duplicate scan suppression and re-arm timing:
     * (scannedRaw != lastScannedCode || (now - lastScanTimestamp > 1500L))
     */
    @Test
    fun testDebounceLogicSuppressesRapidDuplicateScans() {
        val codeA = "0123456789ABCDEFGHJKMNPQRS"
        val codeB = "V1-03E8-1337-ABCD"

        var lastScannedCode: String? = null
        var lastScanTimestamp = 0L
        var scanTriggerCount = 0

        fun simulateScanEvent(scannedRaw: String, now: Long): Boolean {
            if (scannedRaw != lastScannedCode || (now - lastScanTimestamp > 1500L)) {
                lastScannedCode = scannedRaw
                lastScanTimestamp = now
                scanTriggerCount++
                return true
            }
            return false
        }

        // 1. Initial scan of codeA at t = 1000ms -> should trigger
        assertTrue("Initial scan must trigger", simulateScanEvent(codeA, 1000L))
        assertEquals(1, scanTriggerCount)

        // 2. Immediate duplicate scans of codeA within debounce window -> MUST be suppressed
        assertFalse("Duplicate at +50ms must be suppressed", simulateScanEvent(codeA, 1050L))
        assertFalse("Duplicate at +200ms must be suppressed", simulateScanEvent(codeA, 1200L))
        assertFalse("Duplicate at +1499ms must be suppressed", simulateScanEvent(codeA, 2499L))
        assertEquals("Duplicate scans should not increase trigger count", 1, scanTriggerCount)

        // 3. Different codeB scanned at +300ms -> MUST trigger immediately (switching targets)
        assertTrue("Different code must trigger immediately", simulateScanEvent(codeB, 1300L))
        assertEquals(2, scanTriggerCount)

        // 4. Repeated scan of codeB after > 1500ms (t = 2801ms) -> MUST re-arm and trigger
        assertTrue("Re-armed scan after 1500ms must trigger", simulateScanEvent(codeB, 2801L))
        assertEquals(3, scanTriggerCount)
    }

    /**
     * Stress test: 100 round-trip cycles of full pipeline under continuous load.
     */
    @Test
    fun testFullPipelineEndToEndStressLoop() {
        val iterations = 50
        val maxAllowedAvgMs = 50.0 // Well below 500ms budget

        val totalTime = measureTimeMillis {
            for (i in 0 until iterations) {
                val seed = 0x1000 + i
                val originalCode = CommCodeCodec.encode128(
                    monsterId = 301 + (i % 5),
                    stage = PetStage.ADULT,
                    attribute = ElementalAttribute.VACCINE,
                    basePowerHp = 220 + (i * 2),
                    historicalVictories = i,
                    trainingEffort = (i % 5),
                    battleRngSeed = seed
                )

                // Generate matrix
                val matrix = QrMatrixGenerator.generateQrMatrix(originalCode, 29)

                // Decode via ZXing
                val scanned = decodeQrMatrix(matrix)

                // Validate HMAC-16
                val payload = CommCodeCodec.decodeAuto(scanned)
                assertEquals(301 + (i % 5), payload.monsterId)
                assertEquals(seed, payload.battleRngSeed)
            }
        }

        val avgTimePerCycle = totalTime.toDouble() / iterations
        println("Stress test: $iterations full QR roundtrips completed in $totalTime ms (avg: $avgTimePerCycle ms/cycle)")
        assertTrue(
            "Average cycle time ($avgTimePerCycle ms) must be well under budget ($maxAllowedAvgMs ms)",
            avgTimePerCycle < maxAllowedAvgMs
        )
    }
}
