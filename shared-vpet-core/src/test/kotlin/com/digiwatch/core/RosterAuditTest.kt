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

    @Test
    fun auditPerfectToUltimateRequirementsAndAll44JogressRecipes() {
        val registry = PetSpecies.SPECIES_REGISTRY
        val perfectSpecies = registry.values.filter { it.stage == PetStage.PERFECT && it.evolutionRules.isNotEmpty() }
        assertTrue("Must have Perfect species with evolution rules", perfectSpecies.isNotEmpty())

        for (sp in perfectSpecies) {
            for (rule in sp.evolutionRules) {
                val target = registry[rule.targetSpeciesId]!!
                assertEquals("Perfect species ${sp.name} must evolve to ULTIMATE", PetStage.ULTIMATE, target.stage)
                assertTrue(
                    "Perfect -> Ultimate rule for ${sp.name} -> ${target.name} must require >= 3 days (259200s), had ${rule.minAgeSeconds}",
                    rule.minAgeSeconds >= 3 * 86400L
                )
                assertTrue(
                    "Perfect -> Ultimate rule for ${sp.name} -> ${target.name} must require >= 20 battles, had ${rule.minBattles}",
                    rule.minBattles >= 20
                )
            }
        }

        // Verify all 44 Jogress / DigiXros / Mode Change recipes reference valid species
        assertEquals(44, JogressEngine.RECIPES.size)
        for (recipe in JogressEngine.RECIPES) {
            assertTrue("Missing primary species ${recipe.primarySpeciesId}", registry.containsKey(recipe.primarySpeciesId))
            assertTrue("Missing secondary species ${recipe.secondarySpeciesId}", registry.containsKey(recipe.secondarySpeciesId))
            assertTrue("Missing target species ${recipe.targetSpeciesId}", registry.containsKey(recipe.targetSpeciesId))
        }
    }

    @Test
    fun audit100PercentReachabilityFromStarterEggsAndZeroShadowedRules() {
        val registry = PetSpecies.SPECIES_REGISTRY
        val unreachableSpecies = mutableListOf<String>()

        for (sp in registry.values.sortedBy { it.id }) {
            val path = com.digiwatch.core.engine.EvolutionEngine.findEggLineagePath(sp.id)
            if (path.isEmpty()) {
                unreachableSpecies.add("#${sp.id} ${sp.name} (${sp.stage})")
            } else {
                assertTrue("Lineage path for ${sp.name} must start with a starter egg", path.first() in com.digiwatch.core.engine.EvolutionEngine.STARTER_EGG_IDS)
                assertEquals("Lineage path for ${sp.name} must end at ${sp.id}", sp.id, path.last())
            }
        }
        assertTrue(
            "All 205 species must be reachable from the 10 starter eggs! Unreachable: $unreachableSpecies",
            unreachableSpecies.isEmpty()
        )

        // Verify that every single EvolutionRule in every species' evolutionRules list can be triggered
        // as the FIRST matching rule by at least one concrete PetState (zero shadowed rules).
        val shadowedRules = mutableListOf<String>()
        for (sp in registry.values.sortedBy { it.id }) {
            for ((index, rule) in sp.evolutionRules.withIndex()) {
                var canTrigger = false
                val targetAge = rule.minAgeSeconds.coerceAtLeast(300_000L)
                val cmCandidates = listOf(0, 1, 2, 3, 4, 5, 6, 7)
                val effortCandidates = listOf(0, 1, 2, 3, 4)
                val overfeedCandidates = listOf(0, 2, 3, 4)
                val battleCandidates = listOf(0, 10, 12, 15, 20, 25, 30, 35, 40)
                val winRatioCandidates = listOf(0.0f, 0.60f, 0.65f, 0.70f, 0.75f, 0.80f, 0.85f, 0.90f, 1.0f)
                val weightCandidates = listOf(10, 20, 25, 28, 30, 40)

                outer@ for (cm in cmCandidates) {
                    for (effort in effortCandidates) {
                        for (overfeed in overfeedCandidates) {
                            for (battles in battleCandidates) {
                                for (winRatio in winRatioCandidates) {
                                    val wins = kotlin.math.ceil(battles * winRatio).toInt()
                                    for (weight in weightCandidates) {
                                        val testState = com.digiwatch.core.model.PetState(
                                            speciesId = sp.id,
                                            ageSeconds = targetAge,
                                            careMistakes = cm,
                                            effort = effort,
                                            overfeedCount = overfeed,
                                            totalBattles = battles,
                                            victories = wins,
                                            weight = weight
                                        )
                                        val evolved = com.digiwatch.core.engine.EvolutionEngine.evaluateEvolution(testState)
                                        if (evolved?.id == rule.targetSpeciesId) {
                                            canTrigger = true
                                            break@outer
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (!canTrigger) {
                    val targetName = registry[rule.targetSpeciesId]?.name ?: "?"
                    shadowedRules.add("#${sp.id} ${sp.name} rule[$index] -> #${rule.targetSpeciesId} $targetName (${com.digiwatch.core.engine.EvolutionEngine.describeRule(rule)})")
                }
            }
        }
        assertTrue(
            "Zero evolution rules may be shadowed by earlier looser rules! Shadowed: $shadowedRules",
            shadowedRules.isEmpty()
        )
    }

    @Test
    fun generateAuthoritativeDigivolutionGuideMarkdown() {
        val registry = PetSpecies.SPECIES_REGISTRY.values.sortedBy { it.id }
        val sb = StringBuilder()

        sb.appendLine("# 🧬 DigiWatch Official Digivolution & DNA Jogress Guide (205 Species)")
        sb.appendLine()
        sb.appendLine("> **100% Complete & Mathematically Verified Lineage Chart**")
        sb.appendLine("> Every single one of the **205 Digimon** in DigiWatch is naturally reachable from the **10 Starter Digi-Eggs** via canonical Bandai 1997 V-Pet, Digimon Pendulum, and Digimon Color evolution rules, or via **44 DNA Jogress / DigiXros / Mode Change** combinations.")
        sb.appendLine()
        sb.appendLine("## 📊 Roster & Evolution Summary")
        sb.appendLine()
        sb.appendLine("| Stage | Species Count | Canonical Evolution Window | Key Branching Factors |")
        sb.appendLine("| :--- | :---: | :--- | :--- |")
        for (stage in PetStage.entries) {
            val count = registry.count { it.stage == stage }
            val window = when (stage) {
                PetStage.BABY_I -> "Hatch → 10 Mins (`600s`)"
                PetStage.BABY_II -> "6 Hours (`21,600s`)"
                PetStage.CHILD -> "24 Hours (`86,400s`)"
                PetStage.ADULT -> "36 Hours (`129,600s`)"
                PetStage.PERFECT -> "72 Hours (`259,200s`)"
                PetStage.ULTIMATE -> "Apex / Mode Change / Jogress"
            }
            val factors = when (stage) {
                PetStage.BABY_I -> "Starter Digi-Egg Selection (10 Lineages)"
                PetStage.BABY_II -> "Care Mistakes (`CM`), Training Effort (`♥`), Weight (`G`)"
                PetStage.CHILD -> "Care Mistakes (`CM`), Training Effort (`♥`), Overfeed Count, Weight (`G`)"
                PetStage.ADULT -> "Battles (`≥ 10–15`), Win Ratio (`≥ 60%–80%`), Care Mistakes (`CM`), Effort (`♥`)"
                PetStage.PERFECT -> "Battles (`≥ 20–35`), Win Ratio (`≥ 60%–85%`), Care Mistakes (`CM`), Effort (`♥`)"
                PetStage.ULTIMATE -> "DNA Jogress, Mode Change, or Super-Ultimate Fusion"
            }
            sb.appendLine("| **${stage.displayName}** | **$count** | $window | $factors |")
        }
        sb.appendLine()
        sb.appendLine("---")
        sb.appendLine()
        sb.appendLine("## 🥚 1. The 10 Starter Digi-Eggs (Baby I → Baby II)")
        sb.appendLine()
        sb.appendLine("You can hatch any of these **10 Starter Digi-Eggs** at any time from **Watch Settings → Hatch New Digi-Egg** or **Phone Companion → Remote Care Deck**:")
        sb.appendLine()
        sb.appendLine("| Egg ID | Baby I (Fresh) | Evolves Into (Baby II) | Version / Family Origin | Condition |")
        sb.appendLine("| :---: | :--- | :--- | :--- | :--- |")
        val eggOrigins = mapOf(
            101 to "Digital Monster Ver. 1",
            103 to "Digital Monster Ver. 2",
            105 to "Digital Monster Ver. 3",
            107 to "Digital Monster Ver. 4",
            109 to "Digital Monster Ver. 5",
            111 to "Digimon Pendulum 1.0 Nature Spirits",
            113 to "Armageddon Army / Virus Busters",
            130 to "Wind Guardians / Floramon Line",
            131 to "Deep Savers / Marine Line",
            132 to "Deep Savers II / Gomamon Line"
        )
        for (eggId in com.digiwatch.core.engine.EvolutionEngine.STARTER_EGG_IDS) {
            val eggSp = PetSpecies.getById(eggId)
            val rule = eggSp.evolutionRules.first()
            val nextSp = PetSpecies.getById(rule.targetSpeciesId)
            sb.appendLine("| `#$eggId` | **${eggSp.name}** | `#${nextSp.id}` **${nextSp.name}** | ${eggOrigins[eggId] ?: "Classic V-Pet"} | ${com.digiwatch.core.engine.EvolutionEngine.describeRule(rule)} |")
        }

        for (stage in listOf(PetStage.BABY_II, PetStage.CHILD, PetStage.ADULT, PetStage.PERFECT, PetStage.ULTIMATE)) {
            sb.appendLine()
            sb.appendLine("---")
            sb.appendLine()
            sb.appendLine("## 🐾 ${stage.displayName} Stage Digimon (${registry.count { it.stage == stage }} Species)")
            sb.appendLine()
            sb.appendLine("| ID | Digimon | Attr | HP / PWR | Starter Egg Lineage Path | Evolves Into (Target & Exact Requirements) |")
            sb.appendLine("| :---: | :--- | :---: | :---: | :--- | :--- |")
            for (sp in registry.filter { it.stage == stage }) {
                val lineage = com.digiwatch.core.engine.EvolutionEngine.findEggLineagePath(sp.id)
                    .joinToString(" → ") { id -> PetSpecies.getById(id).name }
                val branches = if (sp.evolutionRules.isEmpty()) {
                    val fusions = JogressEngine.getCompatibleRecipesFor(sp.id)
                    if (fusions.isEmpty()) {
                        "_Apex Form_"
                    } else {
                        fusions.joinToString("<br>") { r ->
                            val otherId = if (r.primarySpeciesId == sp.id) r.secondarySpeciesId else r.primarySpeciesId
                            val other = PetSpecies.getById(otherId)
                            val target = PetSpecies.getById(r.targetSpeciesId)
                            "⚡ **+#${other.id} ${other.name} → #${target.id} ${target.name}** (${r.fusionType.displayName})"
                        }
                    }
                } else {
                    sp.evolutionRules.joinToString("<br>") { rule ->
                        val target = PetSpecies.getById(rule.targetSpeciesId)
                        "→ **`#${target.id}` ${target.name}** (${target.stage.displayName}): `${com.digiwatch.core.engine.EvolutionEngine.describeRule(rule)}`"
                    }
                }
                sb.appendLine("| `#${sp.id}` | **${sp.name}** | ${sp.attribute.displayName} | `${sp.baseHp}HP / ${sp.basePower}P` | $lineage | $branches |")
            }
        }

        sb.appendLine()
        sb.appendLine("---")
        sb.appendLine()
        sb.appendLine("## ⚡ DNA Digivolution, Mode Change & DigiXros Recipes (All 44 Combinations)")
        sb.appendLine()
        sb.appendLine("Combine any two compatible Digimon via **Watch D-Com Battle Link**, **Phone Battle Command Center**, or **Phone DigiVault** to awaken these 44 special forms:")
        sb.appendLine()
        sb.appendLine("| # | Primary Partner | Secondary Partner | Awakened Form | Fusion Type | Lore / Description |")
        sb.appendLine("| :---: | :--- | :--- | :--- | :---: | :--- |")
        JogressEngine.RECIPES.forEachIndexed { idx, recipe ->
            val spA = PetSpecies.getById(recipe.primarySpeciesId)
            val spB = PetSpecies.getById(recipe.secondarySpeciesId)
            val target = PetSpecies.getById(recipe.targetSpeciesId)
            sb.appendLine("| ${idx + 1} | `#${spA.id}` **${spA.name}** | `#${spB.id}` **${spB.name}** | `#${target.id}` **${target.name}** | `${recipe.fusionType.displayName}` | ${recipe.description} |")
        }

        val outFile = java.io.File("../DIGIVOLUTION_GUIDE.md")
        outFile.writeText(sb.toString(), Charsets.UTF_8)
        assertTrue("DIGIVOLUTION_GUIDE.md must be written and non-empty", outFile.exists() && outFile.length() > 10_000L)
    }
}

