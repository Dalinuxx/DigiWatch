package com.digiwatch.core.model

import com.digiwatch.core.fusion.FusionType
import com.digiwatch.core.fusion.JogressRecipe

/**
 * Expanded roster of 40 additional species layered on top of the base 165-species catalog.
 *
 * All IDs are < 0xFFF, so they transmit through both the 128-bit Base32 Comm Code (16-bit monster ID)
 * and the Digimon 0nl1ne / D-Com `V1-XXXX-XXXX-XXXX` DigiROM (12-bit monster ID) without any protocol change.
 *
 * New starter lines (hatchable from the Digi-Egg nursery):
 *  - 130 Nyokimon -> 120 Nyaromon -> Salamon / Gaomon / Goblimon
 *  - 131 Pichimon -> 121 Pyocomon -> Hawkmon / Armadillomon / Floramon
 *  - 132 Pukamon  -> 122 Dorimon  -> Dorumon / Kotemon / Monodramon
 */
object ExpandedRoster {

    private const val DAY = 86400L

    private fun sp(
        id: Int, name: String, stage: PetStage, attr: ElementalAttribute,
        hp: Int, power: Int, weight: Int, rules: Array<EvolutionRule>
    ) = PetSpecies(
        id = id, name = name, stage = stage, attribute = attr,
        baseHp = hp, basePower = power, baselineWeight = weight,
        spriteKey = name.lowercase().replace(" ", ""),
        evolutionRules = rules.toList()
    )

    /** Baby II -> Child: three care-mistake bands (0-2, 3-5, 6+). */
    private fun hatch(a: Int, b: Int, c: Int) = arrayOf(
        EvolutionRule(targetSpeciesId = a, minAgeSeconds = DAY / 2, minCareMistakes = 0, maxCareMistakes = 2),
        EvolutionRule(targetSpeciesId = b, minAgeSeconds = DAY / 2, minCareMistakes = 3, maxCareMistakes = 5),
        EvolutionRule(targetSpeciesId = c, minAgeSeconds = DAY / 2, minCareMistakes = 6, maxCareMistakes = 999)
    )

    /** Child -> Adult: good care (with effort) vs poor care. */
    private fun grow(good: Int, bad: Int) = arrayOf(
        EvolutionRule(targetSpeciesId = good, minAgeSeconds = DAY, minCareMistakes = 0, maxCareMistakes = 3, minEffort = 2),
        EvolutionRule(targetSpeciesId = bad, minAgeSeconds = DAY, minCareMistakes = 4, maxCareMistakes = 999)
    )

    /** Adult -> Perfect: good care vs poor care, requires battle experience. */
    private fun mature(good: Int, bad: Int) = arrayOf(
        EvolutionRule(targetSpeciesId = good, minAgeSeconds = 2 * DAY, minBattles = 15, minWinRatio = 0.7f, minCareMistakes = 0, maxCareMistakes = 3),
        EvolutionRule(targetSpeciesId = bad, minAgeSeconds = 2 * DAY, minBattles = 15, minWinRatio = 0.7f, minCareMistakes = 4, maxCareMistakes = 999)
    )

    private fun mature(only: Int) = arrayOf(
        EvolutionRule(targetSpeciesId = only, minAgeSeconds = 2 * DAY, minBattles = 15, minWinRatio = 0.7f)
    )

    /** Perfect -> Ultimate. */
    private fun ascend(target: Int) = arrayOf(
        EvolutionRule(targetSpeciesId = target, minAgeSeconds = 3 * DAY, minBattles = 30, minWinRatio = 0.8f, minEffort = 4)
    )

    private fun ascend(good: Int, bad: Int) = arrayOf(
        EvolutionRule(targetSpeciesId = good, minAgeSeconds = 3 * DAY, minBattles = 30, minWinRatio = 0.8f, minCareMistakes = 0, maxCareMistakes = 3, minEffort = 3),
        EvolutionRule(targetSpeciesId = bad, minAgeSeconds = 3 * DAY, minBattles = 30, minWinRatio = 0.75f, minCareMistakes = 4, maxCareMistakes = 999)
    )

    private fun egg(target: Int) = arrayOf(EvolutionRule(targetSpeciesId = target, minAgeSeconds = 3600L))


    private val F = ElementalAttribute.FREE
    private val VA = ElementalAttribute.VACCINE
    private val DA = ElementalAttribute.DATA
    private val VI = ElementalAttribute.VIRUS

    val SPECIES: List<PetSpecies> by lazy {
        listOf(
            // ---------------- Baby I (starter eggs) ----------------
            sp(130, "Nyokimon", PetStage.BABY_I, F, 50, 10, 5, egg(120)),
            sp(131, "Pichimon", PetStage.BABY_I, F, 50, 10, 5, egg(121)),
            sp(132, "Pukamon", PetStage.BABY_I, F, 50, 10, 5, egg(122)),

            // ---------------- Baby II ----------------
            sp(120, "Nyaromon", PetStage.BABY_II, F, 80, 20, 10, hatch(223, 224, 231)),
            sp(121, "Pyocomon", PetStage.BABY_II, F, 80, 20, 10, hatch(225, 226, 230)),
            sp(122, "Dorimon", PetStage.BABY_II, F, 80, 20, 10, hatch(227, 228, 229)),

            // ---------------- Child ----------------
            sp(223, "Salamon", PetStage.CHILD, VA, 130, 35, 18, grow(338, 349)),
            sp(224, "Gaomon", PetStage.CHILD, DA, 135, 36, 20, grow(346, 347)),
            sp(225, "Hawkmon", PetStage.CHILD, VA, 125, 34, 16, grow(344, 348)),
            sp(226, "Armadillomon", PetStage.CHILD, DA, 140, 33, 24, grow(345, 311)),
            sp(227, "Dorumon", PetStage.CHILD, DA, 135, 36, 20, grow(342, 316)),
            sp(228, "Kotemon", PetStage.CHILD, DA, 130, 37, 20, grow(343, 313)),
            sp(229, "Monodramon", PetStage.CHILD, VI, 130, 38, 20, grow(317, 318)),
            sp(230, "Floramon", PetStage.CHILD, DA, 120, 32, 15, grow(308, 330)),
            sp(231, "Goblimon", PetStage.CHILD, VI, 135, 37, 22, grow(312, 325)),

            // ---------------- Adult ----------------
            sp(342, "Dorugamon", PetStage.ADULT, DA, 370, 100, 32, mature(435, 440)),
            sp(343, "Sorcerymon", PetStage.ADULT, DA, 350, 98, 30, mature(436, 443)),
            sp(344, "Aquilamon", PetStage.ADULT, VA, 355, 100, 28, mature(437, 407)), // Silphymon / Garudamon
            sp(345, "Ankylomon", PetStage.ADULT, VA, 390, 95, 45, mature(441, 438)), // Shakkoumon / Zudomon
            sp(346, "Gaogamon", PetStage.ADULT, DA, 375, 102, 34, mature(404, 439)),
            sp(347, "Mojyamon", PetStage.ADULT, DA, 365, 92, 36, mature(439)),
            sp(348, "Starmon", PetStage.ADULT, DA, 330, 94, 22, mature(402, 442)),
            sp(349, "Dokugumon", PetStage.ADULT, VI, 340, 96, 26, mature(440)),

            // ---------------- Perfect ----------------
            sp(435, "Dorugreymon", PetStage.PERFECT, DA, 440, 130, 42, ascend(523, 550)), // Alphamon / Dorugoramon
            sp(436, "Knightmon", PetStage.PERFECT, DA, 420, 124, 40, arrayOf(
                EvolutionRule(targetSpeciesId = 541, minAgeSeconds = 3 * DAY, minBattles = 30, minWinRatio = 0.85f, minCareMistakes = 0, maxCareMistakes = 1, minEffort = 4), // LordKnightmon
                EvolutionRule(targetSpeciesId = 544, minAgeSeconds = 3 * DAY, minBattles = 30, minWinRatio = 0.80f, minCareMistakes = 2, maxCareMistakes = 3, minEffort = 3), // Duftmon
                EvolutionRule(targetSpeciesId = 551, minAgeSeconds = 3 * DAY, minBattles = 25, minWinRatio = 0.75f, minCareMistakes = 4, maxCareMistakes = 999) // Gaiomon
            )),
            sp(437, "Silphymon", PetStage.PERFECT, VA, 430, 128, 34, ascend(552, 555)), // Ophanimon / Kentaurosmon
            sp(438, "Zudomon", PetStage.PERFECT, DA, 460, 126, 55, ascend(553)),
            sp(439, "Panjyamon", PetStage.PERFECT, DA, 440, 128, 48, ascend(544, 555)), // Duftmon / Kentaurosmon
            sp(440, "Phantomon", PetStage.PERFECT, VI, 410, 125, 36, ascend(554, 532)), // Chaosdramon / Daemon
            sp(441, "Shakkoumon", PetStage.PERFECT, VA, 460, 128, 48, ascend(553, 555)), // Vikemon / Kentaurosmon
            sp(442, "Digitamamon", PetStage.PERFECT, VI, 470, 122, 60, ascend(530, 532)), // Belphemon / Daemon
            sp(443, "Cherrymon", PetStage.PERFECT, DA, 445, 120, 50, ascend(557, 505)), // Cherubimon / Rosemon

            // ---------------- Ultimate ----------------
            sp(550, "Dorugoramon", PetStage.ULTIMATE, DA, 640, 200, 55, emptyArray()),
            sp(551, "Gaiomon", PetStage.ULTIMATE, VI, 650, 198, 58, emptyArray()),
            sp(552, "Ophanimon", PetStage.ULTIMATE, VA, 630, 196, 45, emptyArray()),
            sp(553, "Vikemon", PetStage.ULTIMATE, DA, 665, 190, 65, emptyArray()),
            sp(554, "Chaosdramon", PetStage.ULTIMATE, VI, 680, 205, 70, emptyArray()),
            sp(555, "Kentaurosmon", PetStage.ULTIMATE, VA, 655, 198, 60, emptyArray()),
            sp(556, "Magnadramon", PetStage.ULTIMATE, VA, 660, 200, 50, emptyArray()),
            sp(557, "Cherubimon", PetStage.ULTIMATE, VA, 640, 194, 52, emptyArray())
        )
    }

    /** Canonical DNA Jogress / DigiXros recipes for the expanded roster. */
    val RECIPES: List<JogressRecipe> = listOf(
        // Canonical Adventure 02 & Royal Knight Jogress / DigiXros / Mode Change Fusions
        JogressRecipe(344, 338, 437, FusionType.DNA_JOGRESS, "Aquilamon and Gatomon unite sky and light into Silphymon."),
        JogressRecipe(345, 307, 441, FusionType.DNA_JOGRESS, "Ankylomon and Angemon combine earthen defense and sacred power into Shakkoumon."),
        JogressRecipe(344, 346, 437, FusionType.DNA_JOGRESS, "Aquilamon and Gaogamon unite their wind and fury into Silphymon."),
        JogressRecipe(435, 523, 550, FusionType.DIGIXROS, "Dorugreymon fuses with Alphamon's royal power into Dorugoramon."),
        JogressRecipe(552, 437, 555, FusionType.DNA_JOGRESS, "Ophanimon and Silphymon's celestial wings merge into Kentaurosmon."),
        JogressRecipe(438, 441, 553, FusionType.DNA_JOGRESS, "Zudomon and Shakkoumon channel arctic sovereignty into Vikemon."),
        JogressRecipe(427, 437, 556, FusionType.DNA_JOGRESS, "Angewomon and Silphymon unleash heavenly radiance into Magnadramon."),
        JogressRecipe(501, 507, 549, FusionType.DNA_JOGRESS, "WarGreymon and HerculesKabuterimon unite fire and thunder into Susanoomon."),
        JogressRecipe(512, 521, 513, FusionType.MODE_CHANGE, "Imperialdramon Dragon Mode resonates with Miracle armor to awaken Fighter Mode."),
        JogressRecipe(502, 401, 554, FusionType.DIGIXROS, "Machinedramon upgrades with Red Digizoid cybernetics into Chaosdramon."),
        JogressRecipe(525, 554, 526, FusionType.MODE_CHANGE, "BlackWarGreymon absorbs Chaosdramon's dark core into ChaosBlackWarGreymon."),
        JogressRecipe(527, 113, 528, FusionType.DNA_JOGRESS, "Diaboromon swarms coalesce with Kuramon into Armageddemon."),
        JogressRecipe(501, 436, 551, FusionType.DIGIXROS, "WarGreymon and Knightmon forge eastern samurai armor into Gaiomon."),
        JogressRecipe(504, 552, 557, FusionType.DNA_JOGRESS, "Seraphimon and Ophanimon harmonize celestial virtue into Cherubimon."),
        JogressRecipe(407, 406, 505, FusionType.DNA_JOGRESS, "Garudamon and Lillymon blossom in winds of beauty into Rosemon."),
        JogressRecipe(408, 415, 507, FusionType.DNA_JOGRESS, "MegaKabuterimon and Ex-Tyrannomon awaken golden insectoid HerculesKabuterimon."),
        JogressRecipe(422, 436, 522, FusionType.DNA_JOGRESS, "AeroVeedramon and Knightmon ascend in Blue Digizoid armor to UlforceVeedramon.")
    )
}
