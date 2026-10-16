package com.digiwatch.core.render

import com.digiwatch.core.model.PetSpecies
import com.digiwatch.core.model.SpriteFrame
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class SpriteMatrixRegistryTest {

    private val validChars = setOf('.', '#', 'B', 'K', 'E', 'W', 'T', 'S', 'R', 'G', 'C')

    @Test
    fun testAllSpeciesHaveValid16x16Matrices() {
        for ((speciesId, species) in PetSpecies.SPECIES_REGISTRY) {
            val matrix = SpriteMatrixRegistry.getMatrix(speciesId, SpriteFrame.IDLE_A)
            assertEquals("Matrix row count for species $speciesId (${species.name}) must be 16", 16, matrix.size)
            for (r in 0 until 16) {
                val row = matrix[r]
                assertEquals("Row $r in species $speciesId (${species.name}) must be 16 cols wide", 16, row.length)
                for (c in 0 until 16) {
                    val ch = row[c]
                    assertTrue(
                        "Char '$ch' at ($r,$c) in species $speciesId (${species.name}) must be valid dot character",
                        ch in validChars
                    )
                }
            }
        }
    }

    @Test
    fun testAllSpeciesHaveValidPalettes() {
        for ((speciesId, species) in PetSpecies.SPECIES_REGISTRY) {
            val palette = SpriteMatrixRegistry.getPalette(speciesId)
            assertNotNull("Palette for species $speciesId must not be null", palette)
            // Check that alpha channel is 0xFF
            assertEquals("Alpha channel of primary must be 0xFF", 0xFF, (palette.primary ushr 24) and 0xFF)
        }
    }

    @Test
    fun testAgumonMatricesFidelity() {
        val idleA = SpriteMatrixRegistry.getMatrix(201, SpriteFrame.IDLE_A)
        val idleB = SpriteMatrixRegistry.getMatrix(201, SpriteFrame.IDLE_B)
        val sleep = SpriteMatrixRegistry.getMatrix(201, SpriteFrame.SLEEP_A)
        val attack = SpriteMatrixRegistry.getMatrix(201, SpriteFrame.ATTACK_RELEASE)

        assertEquals(16, idleA.size)
        assertEquals(16, idleB.size)
        assertEquals(16, sleep.size)
        assertEquals(16, attack.size)
    }
}
