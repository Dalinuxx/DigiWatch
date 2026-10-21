package com.digiwear.core

import com.digiwear.core.model.PetSpecies
import com.digiwear.core.render.CutInPortraitEngine
import org.junit.Assert.*
import org.junit.Test

class CutInPortraitEngineTest {

    @Test
    fun testAll550SpeciesGenerateValidHeadshotPortraits() {
        val allSpecies = PetSpecies.SPECIES_REGISTRY.values
        assertEquals("Must test all 550 canonical species", 550, allSpecies.size)

        for (sp in allSpecies) {
            val cutIn = CutInPortraitEngine.getCutInPortrait(sp.id)

            // Verify basic metadata
            assertEquals("Species ID must match", sp.id, cutIn.speciesId)
            assertEquals("Species name must match", sp.name, cutIn.name)
            assertEquals("Stage must match", sp.stage, cutIn.stage)
            assertEquals("Attribute must match", sp.attribute, cutIn.attribute)
            assertTrue("Battle cry must not be empty", cutIn.battleCry.isNotBlank())

            // Verify head crop dimensions
            assertEquals("Crop height must be 10 rows", 10, cutIn.headCropMatrix.size)
            for (row in cutIn.headCropMatrix) {
                assertEquals("Crop row width must be 10 characters", 10, row.length)
            }

            // Verify crop contains actual non-transparent pixels
            val nonBlankCells = cutIn.headCropMatrix.sumOf { r -> r.count { it != '.' } }
            assertTrue(
                "Species ${sp.name} (#${sp.id}) headshot must contain non-transparent character pixels (found $nonBlankCells)",
                nonBlankCells >= 10
            )

            // Verify eye points exist within valid crop bounds (0..9)
            assertTrue(
                "Species ${sp.name} (#${sp.id}) must have at least one eye / focal point",
                cutIn.eyePoints.isNotEmpty()
            )
            for (eye in cutIn.eyePoints) {
                assertTrue("Eye X in bounds", eye.first in 0 until cutIn.cropWidth)
                assertTrue("Eye Y in bounds", eye.second in 0 until cutIn.cropHeight)
            }

            // Verify color palettes are valid ARGB
            assertTrue("Attribute color must be opaque ARGB", (cutIn.attributeColorArgb ushr 24) == 0xFF)
            assertTrue("Secondary color must be opaque ARGB", (cutIn.secondaryAuraArgb ushr 24) == 0xFF)
        }
    }

    @Test
    fun testIconicSpeciesHeadshotFraming() {
        // Agumon (#201)
        val agu = CutInPortraitEngine.getCutInPortrait(201)
        assertEquals("Agumon", agu.name)
        assertTrue("Agumon headshot must capture his head/eyes", agu.eyePoints.isNotEmpty())

        // WarGreymon (#501)
        val wargrey = CutInPortraitEngine.getCutInPortrait(501)
        assertEquals("WarGreymon", wargrey.name)
        assertTrue("WarGreymon headshot must capture his dragon helmet/eyes", wargrey.eyePoints.isNotEmpty())

        // Omnimon (#510)
        val omni = CutInPortraitEngine.getCutInPortrait(510)
        assertEquals("Omnimon", omni.name)
        assertTrue("Omnimon headshot must capture his holy knight helm", omni.eyePoints.isNotEmpty())

        // Siriusmon (#583)
        val sirius = CutInPortraitEngine.getCutInPortrait(583)
        assertEquals("Siriusmon", sirius.name)
        assertTrue("Siriusmon headshot must capture his solar crest", sirius.eyePoints.isNotEmpty())
    }
}
