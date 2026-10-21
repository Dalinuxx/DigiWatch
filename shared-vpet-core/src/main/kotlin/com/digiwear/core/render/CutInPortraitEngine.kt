package com.digiwear.core.render

import com.digiwear.core.model.ElementalAttribute
import com.digiwear.core.model.PetSpecies
import com.digiwear.core.model.PetStage
import com.digiwear.core.model.SpriteFrame

/**
 * Data model for high-impact battle cut-in portraits, close-up headshot renders,
 * and official Color V-Pet album graphics.
 */
data class CutInPortraitData(
    val speciesId: Int,
    val name: String,
    val stage: PetStage,
    val attribute: ElementalAttribute,
    val attributeColorArgb: Int,
    val secondaryAuraArgb: Int,
    val palette: SpritePalette,
    val fullMatrix: Array<String>,
    val headCropMatrix: Array<String>,
    val cropStartX: Int,
    val cropStartY: Int,
    val cropWidth: Int,
    val cropHeight: Int,
    val eyePoints: List<Pair<Int, Int>>, // Coordinates relative to headCropMatrix
    val battleCry: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CutInPortraitData) return false
        return speciesId == other.speciesId &&
            name == other.name &&
            stage == other.stage &&
            attribute == other.attribute &&
            attributeColorArgb == other.attributeColorArgb &&
            secondaryAuraArgb == other.secondaryAuraArgb &&
            palette == other.palette &&
            fullMatrix.contentEquals(other.fullMatrix) &&
            headCropMatrix.contentEquals(other.headCropMatrix) &&
            cropStartX == other.cropStartX &&
            cropStartY == other.cropStartY &&
            cropWidth == other.cropWidth &&
            cropHeight == other.cropHeight &&
            eyePoints == other.eyePoints &&
            battleCry == other.battleCry
    }

    override fun hashCode(): Int {
        var result = speciesId
        result = 31 * result + name.hashCode()
        result = 31 * result + stage.hashCode()
        result = 31 * result + attribute.hashCode()
        result = 31 * result + attributeColorArgb
        result = 31 * result + secondaryAuraArgb
        result = 31 * result + palette.hashCode()
        result = 31 * result + fullMatrix.contentHashCode()
        result = 31 * result + headCropMatrix.contentHashCode()
        result = 31 * result + cropStartX
        result = 31 * result + cropStartY
        result = 31 * result + cropWidth
        result = 31 * result + cropHeight
        result = 31 * result + eyePoints.hashCode()
        result = 31 * result + battleCry.hashCode()
        return result
    }
}

/**
 * Single source of truth for generating authentic, mathematically framed,
 * consistent close-up headshot renders for all 500 Digimon species.
 *
 * Emulates the authentic Digimon Pendulum Color (Saiyu Warriors / Toho Braves)
 * Album Cut-In system and Digital Monster Color Mega Hit action portraits.
 */
object CutInPortraitEngine {

    private val PORTRAIT_CACHE = java.util.concurrent.ConcurrentHashMap<Int, CutInPortraitData>()

    /**
     * Generates a calibrated CutInPortraitData for any valid species ID (1..2000).
     */
    fun getCutInPortrait(speciesId: Int): CutInPortraitData {
        PORTRAIT_CACHE[speciesId]?.let { return it }

        val species = PetSpecies.getById(speciesId)
        val palette = SpriteMatrixRegistry.getPalette(speciesId)
        val fullMatrix = SpriteMatrixRegistry.getMatrix(speciesId, SpriteFrame.IDLE_A)

        // 1. Scan full 16x16 matrix for bounding box and eye features
        var minY = 16
        var maxY = 0
        var minX = 16
        var maxX = 0

        val eyePositions = mutableListOf<Pair<Int, Int>>()
        val whiteFeatures = mutableListOf<Pair<Int, Int>>()

        for (r in fullMatrix.indices) {
            val row = fullMatrix[r]
            for (c in row.indices) {
                val ch = row[c]
                if (ch != '.') {
                    if (r < minY) minY = r
                    if (r > maxY) maxY = r
                    if (c < minX) minX = c
                    if (c > maxX) maxX = c

                    if (ch == 'E') {
                        eyePositions.add(Pair(c, r))
                    } else if (ch == 'W') {
                        // Check if adjacent to black outline (often eye glint/sclera or horn tip)
                        whiteFeatures.add(Pair(c, r))
                    }
                }
            }
        }

        // Fallbacks if empty
        if (minY > maxY) {
            minY = 2
            maxY = 13
            minX = 2
            maxX = 13
        }

        // 2. Determine primary facial focal anchor
        val focalX: Int
        val focalY: Int

        if (eyePositions.isNotEmpty()) {
            focalX = (eyePositions.map { it.first }.average()).toInt()
            focalY = (eyePositions.map { it.second }.average()).toInt()
        } else if (whiteFeatures.isNotEmpty()) {
            // Find upper white features (eyes / horn glints)
            val upperWhite = whiteFeatures.filter { it.second <= minY + (maxY - minY) * 0.5f }
            if (upperWhite.isNotEmpty()) {
                focalX = (upperWhite.map { it.first }.average()).toInt()
                focalY = (upperWhite.map { it.second }.average()).toInt()
            } else {
                focalX = (minX + maxX) / 2
                focalY = (minY + (maxY - minY) * 0.35f).toInt()
            }
        } else {
            focalX = (minX + maxX) / 2
            focalY = (minY + (maxY - minY) * 0.35f).toInt()
        }

        // 3. Compute optimal 10x10 to 11x11 headshot bounding box centered on focal anchor
        val cropSize = 10
        var cropStartX = (focalX - cropSize / 2).coerceIn(0, 16 - cropSize)
        var cropStartY = (focalY - cropSize / 2).coerceIn(0, 16 - cropSize)

        // Ensure we capture the top of head if minY is within reach
        if (minY < cropStartY && cropStartY - minY <= 2) {
            cropStartY = minY.coerceAtLeast(0)
        }

        // 4. Extract cropped headshot matrix
        val headCrop = Array(cropSize) { r ->
            val origY = cropStartY + r
            val origRow = if (origY in fullMatrix.indices) fullMatrix[origY] else "................"
            val sb = StringBuilder(cropSize)
            for (c in 0 until cropSize) {
                val origX = cropStartX + c
                sb.append(if (origX in origRow.indices) origRow[origX] else '.')
            }
            sb.toString()
        }

        // 5. Compute relative eye points inside the crop
        val relativeEyes = mutableListOf<Pair<Int, Int>>()
        for (r in 0 until cropSize) {
            val row = headCrop[r]
            for (c in 0 until cropSize) {
                val ch = row[c]
                if (ch == 'E' || (ch == 'W' && r in 2..6 && c in 2..8)) {
                    relativeEyes.add(Pair(c, r))
                }
            }
        }
        if (relativeEyes.isEmpty()) {
            // Default center glint if no explicit tokens
            val relX = (focalX - cropStartX).coerceIn(2, cropSize - 3)
            val relY = (focalY - cropStartY).coerceIn(2, cropSize - 3)
            relativeEyes.add(Pair(relX, relY))
        }

        // 6. Attribute colors and authentic action cry
        val (attrColor, secColor) = when (species.attribute) {
            ElementalAttribute.VACCINE -> Pair(
                SpritePalette.rgb(0, 229, 255), // Radiant Cyan
                SpritePalette.rgb(255, 214, 0)   // Holy Solar Gold
            )
            ElementalAttribute.DATA -> Pair(
                SpritePalette.rgb(0, 230, 118), // Emerald Matrix Green
                SpritePalette.rgb(64, 196, 255)  // Data Stream Blue
            )
            ElementalAttribute.VIRUS -> Pair(
                SpritePalette.rgb(255, 23, 68),  // Neon Crimson
                SpritePalette.rgb(170, 0, 255)   // Abyssal Violet
            )
            ElementalAttribute.FREE -> Pair(
                SpritePalette.rgb(255, 215, 64), // Radiant Gold
                SpritePalette.rgb(255, 255, 255) // Pure White
            )
        }

        val battleCry = when (species.stage) {
            PetStage.BABY_I, PetStage.BABY_II -> "BUBBLE BURST!"
            PetStage.CHILD -> "DIGI-STRIKE!"
            PetStage.ADULT -> "BURNING CLASH!"
            PetStage.PERFECT -> "OVERDRIVE IMPACT!"
            PetStage.ULTIMATE -> "APEX CRISIS DRIVE!"
        }

        return CutInPortraitData(
            speciesId = speciesId,
            name = species.name,
            stage = species.stage,
            attribute = species.attribute,
            attributeColorArgb = attrColor,
            secondaryAuraArgb = secColor,
            palette = palette,
            fullMatrix = fullMatrix,
            headCropMatrix = headCrop,
            cropStartX = cropStartX,
            cropStartY = cropStartY,
            cropWidth = cropSize,
            cropHeight = cropSize,
            eyePoints = relativeEyes,
            battleCry = battleCry
        ).also { PORTRAIT_CACHE[speciesId] = it }
    }
}
