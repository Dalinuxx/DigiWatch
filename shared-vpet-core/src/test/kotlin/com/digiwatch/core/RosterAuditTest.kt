package com.digiwatch.core

import com.digiwatch.core.codec.CommCodeCodec
import com.digiwatch.core.fusion.FusionType
import com.digiwatch.core.fusion.JogressEngine
import com.digiwatch.core.model.ElementalAttribute
import com.digiwatch.core.model.ExpandedRoster
import com.digiwatch.core.model.PetSpecies
import com.digiwatch.core.model.PetStage
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class RosterAuditTest {

    @Test
    fun auditAllSpeciesAndEvolutions() {
        val registry = PetSpecies.SPECIES_REGISTRY
        println("=== TOTAL REGISTERED SPECIES: ${registry.size} ===")

        val stageCounts = registry.values.groupBy { it.stage }.mapValues { it.value.size }
        println("Stage distribution: $stageCounts")

        val invalidTargets = mutableListOf<String>()
        val stageSkips = mutableListOf<String>()
        val backwardEvolutions = mutableListOf<String>()
        val lateralEvolutions = mutableListOf<String>()

        for (sp in registry.values) {
            assertTrue("Species ${sp.name} has empty spriteKey", sp.spriteKey.isNotBlank())
            assertTrue("Species ${sp.name} baseHp must be > 0", sp.baseHp > 0)
            assertTrue("Species ${sp.name} basePower must be > 0", sp.basePower > 0)
            assertTrue("Species ${sp.name} baselineWeight must be > 0", sp.baselineWeight > 0)

            // Verify strict ID-to-stage range mapping across all 205 species
            when (sp.id) {
                in 100..199 -> assertTrue("${sp.name} (${sp.id}) must be Baby", sp.stage == PetStage.BABY_I || sp.stage == PetStage.BABY_II)
                in 200..299 -> assertEquals("${sp.name} (${sp.id}) must be Child", PetStage.CHILD, sp.stage)
                in 300..399 -> assertEquals("${sp.name} (${sp.id}) must be Adult", PetStage.ADULT, sp.stage)
                in 400..499 -> assertEquals("${sp.name} (${sp.id}) must be Perfect", PetStage.PERFECT, sp.stage)
                in 500..599 -> assertEquals("${sp.name} (${sp.id}) must be Ultimate", PetStage.ULTIMATE, sp.stage)
                else -> throw AssertionError("Unknown ID range for species: ${sp.id} ${sp.name}")
            }

            for (rule in sp.evolutionRules) {
                val target = registry[rule.targetSpeciesId]
                if (target == null) {
                    invalidTargets.add("${sp.id} (${sp.name}) -> ${rule.targetSpeciesId} (MISSING)")
                    continue
                }

                val stageDiff = target.stage.stageIndex - sp.stage.stageIndex
                when {
                    stageDiff < 0 -> backwardEvolutions.add("${sp.name} (${sp.stage}) -> ${target.name} (${target.stage})")
                    stageDiff == 0 -> lateralEvolutions.add("${sp.name} (${sp.stage}) -> ${target.name} (${target.stage})")
                    stageDiff > 1 -> {
                        // Allow only canonical exceptions: Lucemon (Child->Perfect) and Flamedramon (Adult->Magnamon Ultimate)
                        val isAllowedSkip = (sp.id == 221 && target.id == 434) || (sp.id == 332 && target.id == 521)
                        if (!isAllowedSkip) {
                            stageSkips.add("${sp.name} (${sp.stage}) -> ${target.name} (${target.stage}) [jump: +$stageDiff]")
                        }
                    }
                }
            }
        }

        println("=== EVOLUTION AUDIT RESULTS ===")
        println("Invalid targets (${invalidTargets.size}): $invalidTargets")
        println("Stage skips (${stageSkips.size}): $stageSkips")
        println("Backward evolutions (${backwardEvolutions.size}): $backwardEvolutions")
        println("Lateral evolutions (${lateralEvolutions.size}): $lateralEvolutions")

        assertTrue("Found invalid evolution targets: $invalidTargets", invalidTargets.isEmpty())
        assertTrue("Found uncanonical stage skips: $stageSkips", stageSkips.isEmpty())
        assertTrue("Found backward evolutions: $backwardEvolutions", backwardEvolutions.isEmpty())
    }

    @Test
    fun auditCanonicalAdventure02JogressAndAscensions() {
        val registry = PetSpecies.SPECIES_REGISTRY

        // 1. Gatomon (338) + Aquilamon (344) -> Silphymon (437, PERFECT)
        val silphyRecipe = JogressEngine.findFusionRecipe(338, 344)
        assertNotNull("Silphymon Jogress recipe missing", silphyRecipe)
        assertEquals(437, silphyRecipe!!.targetSpeciesId)
        assertEquals(PetStage.PERFECT, registry[437]!!.stage)

        // 2. Angemon (307) + Ankylomon (345) -> Shakkoumon (441, PERFECT)
        val shakkouRecipe = JogressEngine.findFusionRecipe(307, 345)
        assertNotNull("Shakkoumon Jogress recipe missing", shakkouRecipe)
        assertEquals(441, shakkouRecipe!!.targetSpeciesId)
        assertEquals(PetStage.PERFECT, registry[441]!!.stage)

        // 3. Veedramon/Flamedramon + Stingmon -> Paildramon (423, PERFECT)
        val paildraRecipe = JogressEngine.findFusionRecipe(331, 333)
        assertNotNull("Paildramon Jogress recipe missing", paildraRecipe)
        assertEquals(423, paildraRecipe!!.targetSpeciesId)
        assertEquals(PetStage.PERFECT, registry[423]!!.stage)

        // 4. Shakkoumon (441) ascends into Vikemon (553)
        val shakkoumon = registry[441]!!
        val shakkouTargets = shakkoumon.evolutionRules.map { it.targetSpeciesId }
        assertTrue("Shakkoumon must evolve into Vikemon (553)", 553 in shakkouTargets)

        // 5. Angewomon (427) evolves into Magnadramon (556) and Ophanimon (552)
        val angewomon = registry[427]!!
        val angewomonTargets = angewomon.evolutionRules.map { it.targetSpeciesId }
        assertTrue("Angewomon must evolve into Magnadramon (556)", 556 in angewomonTargets)
        assertTrue("Angewomon must evolve into Ophanimon (552)", 552 in angewomonTargets)

        // 6. Magnadramon (556) must be ULTIMATE
        val magnadramon = registry[556]!!
        assertEquals("Magnadramon must be ULTIMATE", PetStage.ULTIMATE, magnadramon.stage)
        assertEquals("Magnadramon must be Vaccine", ElementalAttribute.VACCINE, magnadramon.attribute)
    }

    @Test
    fun auditDigimon0nl1neCompatibilityForAllSpecies() {
        val registry = PetSpecies.SPECIES_REGISTRY
        assertEquals(205, registry.size)

        for (sp in registry.values) {
            // Guarantee 12-bit monster ID for Digimon 0nl1ne / D-Com wire packets (0x001..0xFFF)
            assertTrue("Species ${sp.name} (${sp.id}) exceeds 12-bit limit", sp.id in 1..0x0FFF)
            assertTrue("Base power must fit 12-bit limit", sp.basePower in 1..0x0FFF)

            // DigiROM test: encode to V1-XXXX-XXXX-XXXX and decode
            val digiRom = CommCodeCodec.encodeDigiRom(
                monsterId = sp.id,
                stage = sp.stage,
                attribute = sp.attribute,
                basePowerHp = sp.basePower,
                battleRngSeed = 0xABCD
            )
            val romPayload = CommCodeCodec.decodeDigiRom(digiRom)
            assertEquals("DigiROM ID roundtrip mismatch for ${sp.name}", sp.id, romPayload.monsterId)
            assertEquals("DigiROM Stage roundtrip mismatch for ${sp.name}", sp.stage, romPayload.stage)
            assertEquals("DigiROM Attribute roundtrip mismatch for ${sp.name}", sp.attribute, romPayload.attribute)

            // 128-bit Base32 test: encode and decode
            val b32 = CommCodeCodec.encode128(
                monsterId = sp.id,
                stage = sp.stage,
                attribute = sp.attribute,
                basePowerHp = sp.basePower,
                historicalVictories = 10,
                trainingEffort = 4,
                battleRngSeed = 0x4321
            )
            val b32Payload = CommCodeCodec.decode128(b32)
            assertEquals("Base32 ID roundtrip mismatch for ${sp.name}", sp.id, b32Payload.monsterId)
            assertEquals("Base32 Stage roundtrip mismatch for ${sp.name}", sp.stage, b32Payload.stage)
            assertEquals("Base32 Attribute roundtrip mismatch for ${sp.name}", sp.attribute, b32Payload.attribute)

            // 16-character compact code test: encode and decode
            val b16 = CommCodeCodec.encode16(
                monsterId = sp.id,
                stage = sp.stage,
                attribute = sp.attribute,
                basePowerHp = sp.basePower,
                battleRngSeed = 0x5678
            )
            val b16Payload = CommCodeCodec.decode16(b16)
            assertEquals("Base16 ID roundtrip mismatch for ${sp.name}", sp.id, b16Payload.monsterId)
            assertEquals("Base16 Stage roundtrip mismatch for ${sp.name}", sp.stage, b16Payload.stage)
            assertEquals("Base16 Attribute roundtrip mismatch for ${sp.name}", sp.attribute, b16Payload.attribute)
        }
    }
}
