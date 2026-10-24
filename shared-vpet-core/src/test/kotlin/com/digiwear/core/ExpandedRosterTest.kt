package com.digiwear.core

import com.digiwear.core.codec.CommCodeCodec
import com.digiwear.core.fusion.JogressEngine
import com.digiwear.core.model.ExpandedRoster
import com.digiwear.core.model.PetSpecies
import com.digiwear.core.model.PetStage
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExpandedRosterTest {

    private val expandedIds = ExpandedRoster.SPECIES.map { it.id }.toSet()

    @Test
    fun expandedIdsAreUniqueAndNeverCollideWithBaseRoster() {
        assertEquals(385, ExpandedRoster.SPECIES.size)
        assertEquals(385, expandedIds.size)
        assertEquals(550, PetSpecies.SPECIES_REGISTRY.size)
    }

    @Test
    fun everyEvolutionTargetExistsAndExpandedLinesAdvanceOneStage() {
        for (sp in PetSpecies.SPECIES_REGISTRY.values) {
            for (rule in sp.evolutionRules) {
                val target = PetSpecies.SPECIES_REGISTRY[rule.targetSpeciesId]
                assertNotNull("${sp.name} -> ${rule.targetSpeciesId}", target)
                if (sp.id in expandedIds && sp.stage != PetStage.ULTIMATE) {
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
        val queue = ArrayDeque(com.digiwear.core.engine.EvolutionEngine.STARTER_EGG_IDS)
        while (queue.isNotEmpty()) {
            val id = queue.removeFirst()
            if (reachable.add(id)) {
                PetSpecies.getById(id).evolutionRules.forEach { queue.add(it.targetSpeciesId) }
            }
        }
        val recipeOnly = (ExpandedRoster.RECIPES + JogressEngine.BASE_RECIPES).map { it.targetSpeciesId }.toSet()
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

    @Test
    fun yuramonEvolutionsAreCanonAndDistinct() {
        val yuramon = PetSpecies.getById(107)
        val targets = yuramon.evolutionRules.map { it.targetSpeciesId }.toSet()
        assertTrue("Yuramon should evolve to Tanemon (108)", targets.contains(108))
        assertTrue("Yuramon should evolve to Viximon (116)", targets.contains(116))
        assertTrue("Yuramon should evolve to Minomon (126)", targets.contains(126))
        assertTrue("Yuramon should evolve to Budmon (1115)", targets.contains(1115))
        assertEquals(4, targets.size)
    }
}
