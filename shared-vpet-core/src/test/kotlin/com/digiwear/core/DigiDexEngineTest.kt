package com.digiwear.core

import com.digiwear.core.dex.DigiDexEngine
import com.digiwear.core.model.ElementalAttribute
import com.digiwear.core.model.PetSpecies
import com.digiwear.core.model.PetStage
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class DigiDexEngineTest {

    @Test
    fun testTotalSpeciesMatchesFullRoster550() {
        assertEquals(550, DigiDexEngine.TOTAL_SPECIES_COUNT)
    }

    @Test
    fun testSummaryCalculationWithEmptyAndPartialDiscovery() {
        val emptySummary = DigiDexEngine.calculateSummary(emptySet())
        assertEquals(550, emptySummary.totalSpecies)
        assertEquals(0, emptySummary.discoveredCount)
        assertEquals(0f, emptySummary.completionPercentage, 0.001f)

        // Discover starter Agumon (201) and WarGreymon (501)
        val partialSummary = DigiDexEngine.calculateSummary(setOf(201, 501))
        assertEquals(550, partialSummary.totalSpecies)
        assertEquals(2, partialSummary.discoveredCount)
        assertEquals(1, partialSummary.childCount)
        assertEquals(1, partialSummary.ultimateCount)
        assertTrue(partialSummary.completionPercentage > 0f)
    }

    @Test
    fun testDiscoveryHintsForAllStagesAndJogressTargets() {
        for (sp in PetSpecies.SPECIES_REGISTRY.values) {
            val hint = DigiDexEngine.getDiscoveryHint(sp.id)
            assertNotNull("Hint for ${sp.name} must not be null", hint)
            assertTrue("Hint for ${sp.name} must not be empty", hint.isNotBlank())
        }
        // Specific checks
        assertTrue(DigiDexEngine.getDiscoveryHint(101).contains("Starter Digi-Egg"))
        assertTrue(DigiDexEngine.getDiscoveryHint(201).contains("Koromon"))
        assertTrue(DigiDexEngine.getDiscoveryHint(596).contains("Durandamon"))
    }

    @Test
    fun testCatalogFilteringByStageAndAttribute() {
        val discovered = setOf(201, 301, 401, 501) // Agumon line
        val filteredVaccine = DigiDexEngine.filterCatalog(
            discoveredSpeciesIds = discovered,
            filterDiscoveredOnly = true,
            attributeFilter = ElementalAttribute.VACCINE
        )
        assertTrue(filteredVaccine.isNotEmpty())
        assertTrue(filteredVaccine.all { it.attribute == ElementalAttribute.VACCINE })
        assertTrue(filteredVaccine.all { it.id in discovered })

        val filteredChildren = DigiDexEngine.filterCatalog(
            discoveredSpeciesIds = emptySet(),
            filterDiscoveredOnly = false,
            stageFilter = PetStage.CHILD
        )
        assertEquals(88, filteredChildren.size)
    }
}
