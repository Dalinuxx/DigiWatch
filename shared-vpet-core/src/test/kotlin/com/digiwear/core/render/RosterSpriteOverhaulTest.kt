package com.digiwear.core.render

import com.digiwear.core.model.PetSpecies
import com.digiwear.core.model.PetStage
import com.digiwear.core.model.SpriteFrame
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test

class RosterSpriteOverhaulTest {

    @Test
    fun testAllSpeciesHaveNonZeroPrimary() {
        for ((sid, spec) in PetSpecies.SPECIES_REGISTRY) {
            val pal = SpriteMatrixRegistry.getPalette(sid)
            val r = (pal.primary ushr 16) and 0xFF
            val g = (pal.primary ushr 8) and 0xFF
            val b = pal.primary and 0xFF
            val maxC = maxOf(r, maxOf(g, b))
            if (maxC < 30) {
                fail("Species $sid (${spec.name}) has overly dark primary: rgb($r, $g, $b)")
            }
        }
    }

    @Test
    fun testAllSpeciesHaveSubstantialInteriorFill() {
        val failures = mutableListOf<String>()
        for ((sid, spec) in PetSpecies.SPECIES_REGISTRY) {
            val matrix = SpriteMatrixRegistry.getMatrix(sid, SpriteFrame.IDLE_A)
            var solidCount = 0
            var totalPixels = 0
            for (r in 0 until 16) {
                for (c in 0 until 16) {
                    val ch = matrix[r][c]
                    if (ch != '.') totalPixels++
                    if (ch in setOf('#', 'S', 'T', 'W', 'R', 'G', 'C', 'E')) {
                        solidCount++
                    }
                }
            }

            val minSolid = when (spec.stage) {
                PetStage.BABY_I -> 6
                PetStage.BABY_II -> 8
                else -> 18
            }

            if (solidCount < minSolid) {
                failures.add("Species $sid (${spec.name}, ${spec.stage}): solidCount=$solidCount < minSolid=$minSolid (totalPixels=$totalPixels)")
            }
        }

        if (failures.isNotEmpty()) {
            fail("Underfilled species found (${failures.size}):\n" + failures.joinToString("\n"))
        }
    }

    @Test
    fun testAllMatricesHaveValidDimensionsAndCharacters() {
        val allowedChars = setOf('.', '#', 'S', 'T', 'W', 'R', 'G', 'C', 'B', 'K', 'E')
        val zeroOutline = mutableListOf<String>()
        for ((sid, spec) in PetSpecies.SPECIES_REGISTRY) {
            val matrix = SpriteMatrixRegistry.getMatrix(sid, SpriteFrame.IDLE_A)
            assertEquals("Matrix for $sid (${spec.name}) must have 16 rows", 16, matrix.size)
            var outlineCount = 0
            for (r in 0 until 16) {
                val row = matrix[r]
                assertEquals("Row $r for $sid (${spec.name}) must have 16 cols", 16, row.length)
                for (c in 0 until 16) {
                    val ch = row[c]
                    assertTrue("Invalid char '$ch' at ($r,$c) in $sid (${spec.name})", ch in allowedChars)
                    if (ch == 'B' || ch == 'K') outlineCount++
                }
            }
            if (outlineCount == 0) {
                zeroOutline.add("Species $sid (${spec.name}) has 0 outline pixels!")
            }
        }
        if (zeroOutline.isNotEmpty()) {
            fail("Species with 0 outline found:\n" + zeroOutline.joinToString("\n"))
        }
    }
}
