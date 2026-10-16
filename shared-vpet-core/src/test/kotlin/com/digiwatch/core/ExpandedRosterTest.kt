package com.digiwatch.core

import com.digiwatch.core.codec.CommCodeCodec
import com.digiwatch.core.fusion.JogressEngine
import com.digiwatch.core.model.ExpandedRoster
import com.digiwatch.core.model.PetSpecies
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExpandedRosterTest {

    private val expandedIds = ExpandedRoster.SPECIES.map { it.id }.toSet()

    @Test
    fun expandedIdsAreUniqueAndNeverCollideWithBaseRoster() {
        assertEquals(40, ExpandedRoster.SPECIES.size)
        assertEquals(40, expandedIds.size)
        assertEquals(205, PetSpecies.SPECIES_REGISTRY.size)
    }

    @Test
    fun everyEvolutionTargetExistsAndExpandedLinesAdvanceOneStage() {
        for (sp in PetSpecies.SPECIES_REGISTRY.values) {
            for (rule in sp.evolutionRules) {
                val target = PetSpecies.SPECIES_REGISTRY[rule.targetSpeciesId]
                assertNotNull("${sp.name} -> ${rule.targetSpeciesId}", target)
                if (sp.id in expandedIds) {
                    assertEquals(
                        "${sp.name} -> ${target!!.name}",
                        sp.stage.stageIndex + 1, target.stage.stageIndex
                    )
                }
            }
        }
    }

    @Test
    fun everyExpandedSpeciesIsReachableFromAStarterEgg() {
        val reachable = mutableSetOf<Int>()
        val queue = ArrayDeque(listOf(130, 131, 132))
        while (queue.isNotEmpty()) {
            val id = queue.removeFirst()
            if (reachable.add(id)) {
                PetSpecies.getById(id).evolutionRules.forEach { queue.add(it.targetSpeciesId) }
            }
        }
        // Ultimate Gallantmon-class fusion results (550 via DigiXros, 555/556 via Jogress) are recipe-only,
        // everything else must be reachable through normal evolution.
        val recipeOnly = ExpandedRoster.RECIPES.map { it.targetSpeciesId }.toSet()
        for (sp in ExpandedRoster.SPECIES) {
            assertTrue(
                "${sp.name} (${sp.id}) not reachable from a starter egg",
                sp.id in reachable || sp.id in recipeOnly
            )
        }
    }

    @Test
    fun expandedSpeciesRoundTripThroughBothWireFormats() {
        for (sp in ExpandedRoster.SPECIES) {
            val b32 = CommCodeCodec.encode128(sp.id, sp.stage, sp.attribute, sp.basePower, 3, 2, 0x1234)
            assertEquals(sp.id, CommCodeCodec.decodeAuto(b32).monsterId)

            val rom = CommCodeCodec.encodeDigiRom(sp.id, sp.stage, sp.attribute, sp.basePower, 0x1234)
            val decoded = CommCodeCodec.decodeAuto(rom)
            assertEquals(sp.id, decoded.monsterId)
            assertEquals(sp.stage, decoded.stage)
            assertEquals(sp.attribute, decoded.attribute)
        }
    }

    @Test
    fun expandedRecipesReferenceValidSpecies() {
        for (r in ExpandedRoster.RECIPES) {
            assertNotNull(PetSpecies.SPECIES_REGISTRY[r.primarySpeciesId])
            assertNotNull(PetSpecies.SPECIES_REGISTRY[r.secondarySpeciesId])
            assertNotNull(PetSpecies.SPECIES_REGISTRY[r.targetSpeciesId])
            assertEquals(r, JogressEngine.findFusionRecipe(r.primarySpeciesId, r.secondarySpeciesId))
        }
    }
}
