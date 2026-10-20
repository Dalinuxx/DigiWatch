package com.digiwear.core

import com.digiwear.core.codec.CommCodeCodec
import com.digiwear.core.model.ElementalAttribute
import com.digiwear.core.model.PetStage
import com.digiwear.core.qrcode.QrMatrixGenerator
import com.google.zxing.BinaryBitmap
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.common.HybridBinarizer
import com.google.zxing.qrcode.QRCodeReader
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Assert.assertThrows
import org.junit.Test

class QrMatrixGeneratorTest {

    private fun decodeMatrix(matrix: Array<BooleanArray>): String {
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

    @Test
    fun testMatrixDimensionsDefaultAndExplicit() {
        val sampleCode = "0123456789ABCDEFGHJKMNPQRS"

        // Default size is 29 (Version 3)
        val defaultMatrix = QrMatrixGenerator.generateQrMatrix(sampleCode)
        assertEquals("Default matrix height must be 29", 29, defaultMatrix.size)
        assertEquals("Default matrix width must be 29", 29, defaultMatrix[0].size)

        // Explicit size 21 (Version 1)
        val v1Matrix = QrMatrixGenerator.generateQrMatrix("HELLO", 21)
        assertEquals("Version 1 height must be 21", 21, v1Matrix.size)
        assertEquals("Version 1 width must be 21", 21, v1Matrix[0].size)

        // Explicit size 25 (Version 2)
        val v2Matrix = QrMatrixGenerator.generateQrMatrix("0123456789ABCDEF", 25)
        assertEquals("Version 2 height must be 25", 25, v2Matrix.size)
        assertEquals("Version 2 width must be 25", 25, v2Matrix[0].size)
    }

    @Test
    fun testNonEmptyAndFinderPatterns() {
        val sampleCode = "0123456789ABCDEFGHJKMNPQRS"
        val matrix = QrMatrixGenerator.generateQrMatrix(sampleCode, 29)

        var darkCount = 0
        var lightCount = 0
        for (row in matrix) {
            for (cell in row) {
                if (cell) darkCount++ else lightCount++
            }
        }

        assertTrue("Matrix must contain dark modules", darkCount > 0)
        assertTrue("Matrix must contain light modules", lightCount > 0)
        assertEquals("Total modules in 29x29 must be 841", 841, darkCount + lightCount)

        // Top-Left Finder Pattern (7x7: outer dark, inner light ring, center 3x3 dark)
        for (x in 0..6) {
            assertTrue("Top row module ($x, 0) must be dark", matrix[0][x])
            assertTrue("Bottom row module ($x, 6) must be dark", matrix[6][x])
        }
        for (y in 0..6) {
            assertTrue("Left col module (0, $y) must be dark", matrix[y][0])
            assertTrue("Right col module (6, $y) must be dark", matrix[y][6])
        }
        assertFalse("Inner module (1, 1) must be light", matrix[1][1])
        for (y in 2..4) {
            for (x in 2..4) {
                assertTrue("Center module ($x, $y) must be dark", matrix[y][x])
            }
        }

        // Top-Right Finder Pattern
        for (x in 22..28) {
            assertTrue("Top row module ($x, 0) must be dark", matrix[0][x])
            assertTrue("Bottom row module ($x, 6) must be dark", matrix[6][x])
        }

        // Bottom-Left Finder Pattern
        for (y in 22..28) {
            assertTrue("Left col module (0, $y) must be dark", matrix[y][0])
            assertTrue("Right col module (6, $y) must be dark", matrix[y][6])
        }
    }

    @Test
    fun testDeterministicOutput() {
        val codeA = "0123456789ABCDEFGHJKMNPQRS"
        val codeB = "V1-03E8-1337-ABCD"

        val matrixA1 = QrMatrixGenerator.generateQrMatrix(codeA, 29)
        val matrixA2 = QrMatrixGenerator.generateQrMatrix(codeA, 29)
        val matrixB = QrMatrixGenerator.generateQrMatrix(codeB, 29)

        // Verify identical matrices for identical content
        for (y in 0 until 29) {
            for (x in 0 until 29) {
                assertEquals("Deterministic match failed at ($x, $y)", matrixA1[y][x], matrixA2[y][x])
            }
        }

        // Verify different content produces distinct matrices
        var differs = false
        for (y in 0 until 29) {
            for (x in 0 until 29) {
                if (matrixA1[y][x] != matrixB[y][x]) {
                    differs = true
                    break
                }
            }
        }
        assertTrue("Different content must produce distinct matrices", differs)
    }

    @Test
    fun testRoundtripVerificationWithQRCodeReader() {
        // 1. 128-bit Comm Code from CommCodeCodec
        val commCode128 = CommCodeCodec.encode128(
            monsterId = 301,
            stage = PetStage.ADULT,
            attribute = ElementalAttribute.VACCINE,
            basePowerHp = 240,
            historicalVictories = 15,
            trainingEffort = 4,
            battleRngSeed = 0xABCD,
            timestampSalt = 0x1234
        )
        val matrix128 = QrMatrixGenerator.generateQrMatrix(commCode128, 29)
        val decoded128 = decodeMatrix(matrix128)
        assertEquals("Decoded 128-bit Comm Code must match original", commCode128, decoded128)

        val payload = CommCodeCodec.decodeAuto(decoded128)
        assertEquals(301, payload.monsterId)
        assertEquals(PetStage.ADULT, payload.stage)
        assertEquals(ElementalAttribute.VACCINE, payload.attribute)

        // 2. Hyphenated 128-bit code
        val hyphenatedCode = CommCodeCodec.formatWithHyphens(commCode128)
        val matrixHyphen = QrMatrixGenerator.generateQrMatrix(hyphenatedCode, 29)
        assertEquals(hyphenatedCode, decodeMatrix(matrixHyphen))

        // 3. 16-character compact Comm Code
        val compactCode = CommCodeCodec.encode16(
            monsterId = 301,
            stage = PetStage.ADULT,
            attribute = ElementalAttribute.VACCINE,
            basePowerHp = 240,
            battleRngSeed = 0xABCD
        )
        val matrixCompact = QrMatrixGenerator.generateQrMatrix(compactCode, 29)
        assertEquals(compactCode, decodeMatrix(matrixCompact))

        // 4. Hex DigiROM packet
        val digiRom = CommCodeCodec.encodeDigiRom(
            monsterId = 301,
            stage = PetStage.ADULT,
            attribute = ElementalAttribute.VACCINE,
            basePowerHp = 240,
            battleRngSeed = 0xABCD
        )
        val matrixDigiRom = QrMatrixGenerator.generateQrMatrix(digiRom, 29)
        assertEquals(digiRom, decodeMatrix(matrixDigiRom))
    }

    @Test
    fun testEdgeCasesAndValidation() {
        // Empty content
        assertThrows(IllegalArgumentException::class.java) {
            QrMatrixGenerator.generateQrMatrix("")
        }

        // Size smaller than Version 1 (< 21)
        assertThrows(IllegalArgumentException::class.java) {
            QrMatrixGenerator.generateQrMatrix("TEST", 20)
        }

        // Invalid QR size not conforming to 17 + 4 * V
        assertThrows(IllegalArgumentException::class.java) {
            QrMatrixGenerator.generateQrMatrix("TEST", 28)
        }

        // Data exceeding version capacity (size 21 Version 1 holds max 20 chars at Level M)
        assertThrows(IllegalArgumentException::class.java) {
            QrMatrixGenerator.generateQrMatrix(
                "THIS_IS_DEFINITELY_TOO_LONG_FOR_QR_VERSION_1_LEVEL_M",
                21
            )
        }
    }
}
