package com.digiwear.core.qrcode

import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel

/**
 * Core QR Matrix Generator for offline peer-to-peer battle matchmaking.
 * Encodes Base32 Comm Codes, hyphenated codes, and DigiROM packets into a 2D boolean grid
 * suitable for pixel-perfect rendering on Wear OS and Android Companion displays.
 */
object QrMatrixGenerator {

    private val WRITER = QRCodeWriter()
    private val CACHE = java.util.concurrent.ConcurrentHashMap<String, Array<BooleanArray>>()

    /**
     * Generates a 2D BooleanArray matrix representing a QR code.
     * `true` indicates a black (dark) module; `false` indicates a white (light) module.
     *
     * @param content The text/payload to encode into the QR code.
     * @param size Target dimension (default 29 for standard Version 3 QR code).
     *             Must follow the QR formula (17 + 4 * version), e.g., 21, 25, 29, 33.
     * @return Array<BooleanArray> where matrix[y][x] corresponds to row y, column x.
     * @throws IllegalArgumentException if content is empty, size is invalid, or data exceeds version capacity.
     */
    fun generateQrMatrix(
        content: String,
        size: Int = 29
    ): Array<BooleanArray> {
        require(content.isNotEmpty()) { "QR content must not be empty" }
        require(size >= 21) { "QR matrix size must be at least 21 (Version 1 minimum)" }
        require((size - 17) % 4 == 0) {
            "Invalid QR matrix size: $size. QR code dimensions must follow (17 + 4 * version), such as 21, 25, 29, 33."
        }
        val version = (size - 17) / 4
        require(version in 1..40) { "QR version must be between 1 and 40 (size $size)" }

        val cacheKey = "$size:$content"
        CACHE[cacheKey]?.let { return it }

        val hints = mapOf(
            EncodeHintType.ERROR_CORRECTION to ErrorCorrectionLevel.M,
            EncodeHintType.MARGIN to 0,
            EncodeHintType.CHARACTER_SET to "UTF-8",
            EncodeHintType.QR_VERSION to version
        )

        val bitMatrix = try {
            WRITER.encode(content, BarcodeFormat.QR_CODE, size, size, hints)
        } catch (e: WriterException) {
            throw IllegalArgumentException(
                "Data too large for QR matrix size $size (Version $version): ${e.message}",
                e
            )
        }

        val matrix = Array(bitMatrix.height) { y ->
            BooleanArray(bitMatrix.width) { x -> bitMatrix.get(x, y) }
        }
        if (CACHE.size > 128) CACHE.clear()
        CACHE[cacheKey] = matrix
        return matrix
    }
}
