package com.digiwatch.core.model

import com.digiwatch.core.fusion.FusionType
import com.digiwatch.core.fusion.JogressRecipe

/**
 * Expanded roster of 135 additional species layered on top of the base 165-species catalog (bringing the total to 300).
 *
 * All IDs are < 0xFFF, so they transmit through both the 128-bit Base32 Comm Code (16-bit monster ID)
 * and the Digimon 0nl1ne / D-Com `V1-XXXX-XXXX-XXXX` DigiROM (12-bit monster ID) without any protocol change.
 *
 * Starter lines (hatchable from the 10 Digi-Egg nursery):
 *  - 101 Botamon -> Koromon / Ketomon
 *  - 103 Punimon -> Tsunomon / Chocomon
 *  - 105 Poyomon -> Tokomon / Gummymon / Moonmon
 *  - 107 Yuramon -> Tanemon / Viximon / Minomon
 *  - 109 Zurumon -> Pagumon / Yaamon / Kapurimon
 *  - 111 Mokumon -> DemiMeramon / Gigimon / Hopmon / Sunmon
 *  - 113 Kuramon -> DemiVeemon / Tsumemon
 *  - 130 Nyokimon -> Nyaromon
 *  - 131 Pichimon -> Pyocomon
 *  - 132 Pukamon  -> Dorimon
 */
object ExpandedRoster {

    private const val DAY = 86400L

    private fun sp(
        id: Int, name: String, stage: PetStage, attr: ElementalAttribute,
        hp: Int, power: Int, weight: Int, rules: Array<EvolutionRule>
    ) = PetSpecies(
        id = id, name = name, stage = stage, attribute = attr,
        baseHp = hp, basePower = power, baselineWeight = weight,
        spriteKey = name.lowercase().replace(" ", "").replace("(", "").replace(")", ""),
        evolutionRules = rules.toList()
    )

    /** Baby II -> Child: three care-mistake bands (0-2, 3-5, 6+). */
    private fun hatch(a: Int, b: Int, c: Int) = arrayOf(
        EvolutionRule(targetSpeciesId = a, minAgeSeconds = DAY / 2, minCareMistakes = 0, maxCareMistakes = 2),
        EvolutionRule(targetSpeciesId = b, minAgeSeconds = DAY / 2, minCareMistakes = 3, maxCareMistakes = 5),
        EvolutionRule(targetSpeciesId = c, minAgeSeconds = DAY / 2, minCareMistakes = 6, maxCareMistakes = 999)
    )

    /** Baby II -> Child: 4 branches with effort & CM differentiation */
    private fun hatch4(a: Int, b: Int, c: Int, d: Int) = arrayOf(
        EvolutionRule(targetSpeciesId = a, minAgeSeconds = DAY / 2, minCareMistakes = 0, maxCareMistakes = 1, minEffort = 3),
        EvolutionRule(targetSpeciesId = b, minAgeSeconds = DAY / 2, minCareMistakes = 0, maxCareMistakes = 2, minEffort = 0),
        EvolutionRule(targetSpeciesId = c, minAgeSeconds = DAY / 2, minCareMistakes = 3, maxCareMistakes = 4),
        EvolutionRule(targetSpeciesId = d, minAgeSeconds = DAY / 2, minCareMistakes = 5, maxCareMistakes = 999)
    )

    /** Child -> Adult: good care (with effort) vs poor care. */
    private fun grow(good: Int, bad: Int) = arrayOf(
        EvolutionRule(targetSpeciesId = good, minAgeSeconds = DAY, minCareMistakes = 0, maxCareMistakes = 3, minEffort = 2),
        EvolutionRule(targetSpeciesId = bad, minAgeSeconds = DAY, minCareMistakes = 4, maxCareMistakes = 999)
    )

    /** Child -> Adult: 3 branches (effort/perfection, good care, poor care). */
    private fun grow3(best: Int, good: Int, bad: Int) = arrayOf(
        EvolutionRule(targetSpeciesId = best, minAgeSeconds = DAY, minCareMistakes = 0, maxCareMistakes = 1, minEffort = 3),
        EvolutionRule(targetSpeciesId = good, minAgeSeconds = DAY, minCareMistakes = 0, maxCareMistakes = 3, minEffort = 0),
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

            // ---------------- Baby II (Original Expansion) ----------------
            sp(120, "Nyaromon", PetStage.BABY_II, F, 80, 20, 10, hatch4(223, 224, 241, 243)),
            sp(121, "Pyocomon", PetStage.BABY_II, F, 80, 20, 10, hatch4(225, 226, 230, 242)),
            sp(122, "Dorimon", PetStage.BABY_II, F, 80, 20, 10, hatch4(227, 232, 239, 246)),

            // ---------------- Baby II (Master Expansion) ----------------
            sp(123, "Tsumemon", PetStage.BABY_II, VI, 80, 20, 10, hatch(250, 231, 234)),
            sp(124, "Hopmon", PetStage.BABY_II, F, 80, 20, 10, hatch(229, 244, 245)),
            sp(125, "Kapurimon", PetStage.BABY_II, F, 80, 20, 10, hatch(237, 249, 233)),
            sp(126, "Minomon", PetStage.BABY_II, F, 80, 20, 10, hatch(238, 213, 228)),
            sp(127, "Sunmon", PetStage.BABY_II, F, 80, 20, 10, hatch(247, 235, 201)),
            sp(128, "Moonmon", PetStage.BABY_II, F, 80, 20, 10, hatch(248, 236, 217)),
            sp(129, "Ketomon", PetStage.BABY_II, F, 80, 20, 10, hatch(244, 240, 227)),

            // ---------------- Child (Original Expansion) ----------------
            sp(223, "Salamon", PetStage.CHILD, VA, 130, 35, 18, grow3(372, 338, 349)),
            sp(224, "Gaomon", PetStage.CHILD, DA, 135, 36, 20, grow(346, 347)),
            sp(225, "Hawkmon", PetStage.CHILD, VA, 125, 34, 16, grow3(361, 344, 348)),
            sp(226, "Armadillomon", PetStage.CHILD, DA, 140, 33, 24, grow3(360, 345, 311)),
            sp(227, "Dorumon", PetStage.CHILD, DA, 135, 36, 20, grow(342, 316)),
            sp(228, "Kotemon", PetStage.CHILD, DA, 130, 37, 20, grow(343, 313)),
            sp(229, "Monodramon", PetStage.CHILD, VI, 130, 38, 20, grow(317, 318)),
            sp(230, "Floramon", PetStage.CHILD, DA, 120, 32, 15, grow(308, 330)),
            sp(231, "Goblimon", PetStage.CHILD, VI, 135, 37, 22, grow(312, 325)),

            // ---------------- Child (Master Expansion) ----------------
            sp(232, "Gomamon", PetStage.CHILD, VA, 135, 35, 20, grow(350, 347)),
            sp(233, "Gotsumon", PetStage.CHILD, DA, 140, 36, 25, grow(351, 311)),
            sp(234, "Otamamon", PetStage.CHILD, VI, 130, 34, 18, grow(352, 356)),
            sp(235, "Candlemon", PetStage.CHILD, DA, 125, 37, 16, grow(337, 304)),
            sp(236, "Bakumon", PetStage.CHILD, VA, 130, 33, 18, grow(343, 328)),
            sp(237, "Hagurumon", PetStage.CHILD, VI, 135, 36, 22, grow(354, 353)),
            sp(238, "Mushroomon", PetStage.CHILD, VI, 125, 34, 15, grow(355, 330)),
            sp(239, "Crabmon", PetStage.CHILD, DA, 140, 35, 24, grow(358, 324)),
            sp(240, "Syakomon", PetStage.CHILD, VI, 135, 35, 22, grow(356, 357)),
            sp(241, "Lalamon", PetStage.CHILD, DA, 120, 32, 14, grow(364, 308)),
            sp(242, "Falcomon", PetStage.CHILD, VA, 130, 35, 17, grow(363, 344)),
            sp(243, "Kudamon", PetStage.CHILD, VA, 125, 36, 15, grow(365, 343)),
            sp(244, "Ryudamon", PetStage.CHILD, VA, 135, 37, 20, grow(366, 342)),
            sp(245, "Dracomon", PetStage.CHILD, DA, 135, 38, 20, grow(367, 331)),
            sp(246, "Kamemon", PetStage.CHILD, DA, 140, 33, 25, grow(362, 352)),
            sp(247, "Coronamon", PetStage.CHILD, VA, 130, 37, 18, grow(369, 304)),
            sp(248, "Lunamon", PetStage.CHILD, DA, 130, 36, 18, grow(370, 329)),
            sp(249, "ToyAgumon", PetStage.CHILD, VA, 130, 34, 19, grow(353, 348)),
            sp(250, "Keramon", PetStage.CHILD, VI, 135, 39, 20, grow(368, 326)),

            // ---------------- Adult (Original Expansion) ----------------
            sp(342, "Dorugamon", PetStage.ADULT, DA, 370, 100, 32, mature(435, 440)),
            sp(343, "Sorcerymon", PetStage.ADULT, DA, 350, 98, 30, mature(436, 443)),
            sp(344, "Aquilamon", PetStage.ADULT, VA, 355, 100, 28, mature(437, 407)), // Silphymon / Garudamon
            sp(345, "Ankylomon", PetStage.ADULT, VA, 390, 95, 45, mature(441, 438)), // Shakkoumon / Zudomon
            sp(346, "Gaogamon", PetStage.ADULT, DA, 375, 102, 34, mature(404, 439)),
            sp(347, "Mojyamon", PetStage.ADULT, DA, 365, 92, 36, mature(462, 460)),
            sp(348, "Starmon", PetStage.ADULT, DA, 330, 94, 22, mature(402, 442)),
            sp(349, "Dokugumon", PetStage.ADULT, VI, 340, 96, 26, mature(458, 457)),

            // ---------------- Adult (Master Expansion) ----------------
            sp(350, "Ikkakumon", PetStage.ADULT, VA, 380, 98, 40, mature(438, 462)),
            sp(351, "Golemon", PetStage.ADULT, VI, 390, 95, 48, mature(445, 462)),
            sp(352, "Gekomon", PetStage.ADULT, DA, 350, 94, 30, mature(444, 446)),
            sp(353, "Clockmon", PetStage.ADULT, DA, 360, 96, 32, mature(464, 436)),
            sp(354, "Guardromon", PetStage.ADULT, VI, 385, 97, 42, mature(418, 463)),
            sp(355, "Woodmon", PetStage.ADULT, VI, 370, 92, 35, mature(443, 456)),
            sp(356, "Octomon", PetStage.ADULT, VI, 355, 95, 28, mature(446, 447)),
            sp(357, "Gesomon", PetStage.ADULT, VI, 360, 96, 26, mature(446, 447)),
            sp(358, "Ebidramon", PetStage.ADULT, DA, 370, 99, 34, mature(409, 447)),
            sp(359, "Raidramon", PetStage.ADULT, F, 380, 104, 30, mature(422, 431)),
            sp(360, "Digmon", PetStage.ADULT, F, 380, 98, 38, mature(441, 438)),
            sp(361, "Shurimon", PetStage.ADULT, F, 360, 100, 25, mature(437, 407)),
            sp(362, "Submarimon", PetStage.ADULT, F, 375, 96, 36, mature(438, 446)),
            sp(363, "Peckmon", PetStage.ADULT, VA, 360, 98, 26, mature(452, 407)),
            sp(364, "Sunflowmon", PetStage.ADULT, DA, 355, 95, 27, mature(453, 406)),
            sp(365, "Reppamon", PetStage.ADULT, VA, 365, 101, 28, mature(449, 436)),
            sp(366, "Ginryumon", PetStage.ADULT, VA, 375, 102, 32, mature(450, 459)),
            sp(367, "Coredramon", PetStage.ADULT, VA, 375, 103, 33, mature(451, 416)),
            sp(368, "Chrysalimon", PetStage.ADULT, VI, 370, 99, 35, mature(448, 410)),
            sp(369, "Firamon", PetStage.ADULT, VA, 375, 102, 32, mature(454, 401)),
            sp(370, "Lekismon", PetStage.ADULT, DA, 365, 100, 29, mature(455, 404)),
            sp(371, "ExVeemon", PetStage.ADULT, F, 370, 102, 30, mature(423, 422)),
            sp(372, "Lynxmon", PetStage.ADULT, F, 365, 100, 28, mature(461, 427)),

            // ---------------- Perfect (Original Expansion) ----------------
            sp(435, "Dorugreymon", PetStage.PERFECT, DA, 440, 130, 42, ascend(523, 550)), // Alphamon / Dorugoramon
            sp(436, "Knightmon", PetStage.PERFECT, DA, 420, 124, 40, arrayOf(
                EvolutionRule(targetSpeciesId = 578, minAgeSeconds = 3 * DAY, minBattles = 35, minWinRatio = 0.85f, minCareMistakes = 0, maxCareMistakes = 0, minEffort = 4), // Jesmon GX
                EvolutionRule(targetSpeciesId = 541, minAgeSeconds = 3 * DAY, minBattles = 30, minWinRatio = 0.80f, minCareMistakes = 0, maxCareMistakes = 1, minEffort = 4), // LordKnightmon
                EvolutionRule(targetSpeciesId = 544, minAgeSeconds = 3 * DAY, minBattles = 30, minWinRatio = 0.80f, minCareMistakes = 2, maxCareMistakes = 3, minEffort = 3), // Duftmon
                EvolutionRule(targetSpeciesId = 551, minAgeSeconds = 3 * DAY, minBattles = 25, minWinRatio = 0.75f, minCareMistakes = 4, maxCareMistakes = 999) // Gaiomon
            )),
            sp(437, "Silphymon", PetStage.PERFECT, VA, 430, 128, 34, ascend(552, 555)), // Ophanimon / Kentaurosmon
            sp(438, "Zudomon", PetStage.PERFECT, DA, 460, 126, 55, ascend(553, 581)), // Vikemon / MetalGarurumon X
            sp(439, "Panjyamon", PetStage.PERFECT, DA, 440, 128, 48, ascend(568, 560)), // BanchoLeomon / CresGarurumon
            sp(440, "Phantomon", PetStage.PERFECT, VI, 410, 125, 36, ascend(554, 532)), // Chaosdramon / Daemon
            sp(441, "Shakkoumon", PetStage.PERFECT, VA, 460, 128, 48, ascend(553, 555)), // Vikemon / Kentaurosmon
            sp(442, "Digitamamon", PetStage.PERFECT, VI, 470, 122, 60, ascend(530, 532)), // Belphemon / Daemon
            sp(443, "Cherrymon", PetStage.PERFECT, DA, 445, 120, 50, ascend(557, 505)), // Cherubimon / Rosemon

            // ---------------- Perfect (Master Expansion) ----------------
            sp(444, "ShogunGekomon", PetStage.PERFECT, DA, 460, 126, 50, ascend(570, 508)),
            sp(445, "Volcamon", PetStage.PERFECT, DA, 450, 128, 46, ascend(558, 502)),
            sp(446, "MarineDevimon", PetStage.PERFECT, VI, 440, 127, 42, ascend(533, 532)),
            sp(447, "WaruSeadramon", PetStage.PERFECT, VI, 445, 129, 44, ascend(554, 533)),
            sp(448, "Infermon", PetStage.PERFECT, VI, 455, 132, 40, ascend(527, 571)),
            sp(449, "Chirinmon", PetStage.PERFECT, VA, 450, 130, 38, ascend(577, 543)),
            sp(450, "Hisyaryumon", PetStage.PERFECT, VA, 455, 133, 42, ascend(561, 580)),
            sp(451, "Wingdramon", PetStage.PERFECT, VA, 450, 132, 40, ascend(562, 576)),
            sp(452, "Yatagaramon", PetStage.PERFECT, VA, 445, 131, 36, ascend(575, 544)),
            sp(453, "Lilamon", PetStage.PERFECT, DA, 430, 126, 32, ascend(572, 505)),
            sp(454, "Flaremon", PetStage.PERFECT, VA, 460, 134, 44, ascend(563, 565)),
            sp(455, "Crescemon", PetStage.PERFECT, DA, 450, 132, 38, ascend(564, 565)),
            sp(456, "SkullSatamon", PetStage.PERFECT, VI, 440, 133, 36, ascend(573, 532)),
            sp(457, "Mummymon", PetStage.PERFECT, VI, 435, 125, 38, ascend(566, 534)),
            sp(458, "Arukenimon", PetStage.PERFECT, VI, 435, 127, 36, ascend(571, 531)),
            sp(459, "Grademon", PetStage.PERFECT, VA, 450, 132, 42, ascend(567, 579)),
            sp(460, "Astamon", PetStage.PERFECT, VI, 445, 130, 38, ascend(582, 530)),
            sp(461, "Gargoylemon", PetStage.PERFECT, VA, 440, 128, 36, ascend(574, 552)),
            sp(462, "Mammothmon", PetStage.PERFECT, VA, 465, 128, 55, ascend(581, 553)),
            sp(463, "MetalGreymon (Virus)", PetStage.PERFECT, VI, 450, 132, 45, ascend(559, 525)),
            sp(464, "Cyberdramon (Savers)", PetStage.PERFECT, VA, 445, 130, 38, ascend(569, 515)),

            // ---------------- Ultimate (Original Expansion) ----------------
            sp(550, "Dorugoramon", PetStage.ULTIMATE, DA, 640, 200, 55, emptyArray()),
            sp(551, "Gaiomon", PetStage.ULTIMATE, VI, 650, 198, 58, emptyArray()),
            sp(552, "Ophanimon", PetStage.ULTIMATE, VA, 630, 196, 45, emptyArray()),
            sp(553, "Vikemon", PetStage.ULTIMATE, DA, 665, 190, 65, emptyArray()),
            sp(554, "Chaosdramon", PetStage.ULTIMATE, VI, 680, 205, 70, emptyArray()),
            sp(555, "Kentaurosmon", PetStage.ULTIMATE, VA, 655, 198, 60, emptyArray()),
            sp(556, "Magnadramon", PetStage.ULTIMATE, VA, 660, 200, 50, emptyArray()),
            sp(557, "Cherubimon", PetStage.ULTIMATE, VA, 640, 194, 52, emptyArray()),

            // ---------------- Ultimate (Master Expansion) ----------------
            sp(558, "RustTyrannomon", PetStage.ULTIMATE, VI, 685, 212, 72, emptyArray()),
            sp(559, "BlitzGreymon", PetStage.ULTIMATE, VI, 675, 210, 68, emptyArray()),
            sp(560, "CresGarurumon", PetStage.ULTIMATE, DA, 660, 206, 60, emptyArray()),
            sp(561, "Ouryumon", PetStage.ULTIMATE, VA, 670, 208, 65, emptyArray()),
            sp(562, "Slayerdramon", PetStage.ULTIMATE, VA, 665, 209, 62, emptyArray()),
            sp(563, "Apollomon", PetStage.ULTIMATE, VA, 680, 210, 64, emptyArray()),
            sp(564, "Dianamon", PetStage.ULTIMATE, DA, 660, 205, 55, emptyArray()),
            sp(565, "GraceNovamon", PetStage.ULTIMATE, VA, 720, 220, 70, emptyArray()),
            sp(566, "Lucemon Satan Mode", PetStage.ULTIMATE, VI, 710, 218, 75, emptyArray()),
            sp(567, "Chaosmon", PetStage.ULTIMATE, VA, 700, 216, 65, emptyArray()),
            sp(568, "BanchoLeomon", PetStage.ULTIMATE, VA, 670, 208, 62, emptyArray()),
            sp(569, "Darkdramon", PetStage.ULTIMATE, VI, 675, 210, 68, emptyArray()),
            sp(570, "MarineAngemon", PetStage.ULTIMATE, VA, 620, 190, 40, emptyArray()),
            sp(571, "TigerVespamon", PetStage.ULTIMATE, VI, 645, 202, 50, emptyArray()),
            sp(572, "Kuzuhamon", PetStage.ULTIMATE, DA, 650, 200, 52, emptyArray()),
            sp(573, "BlackSeraphimon", PetStage.ULTIMATE, VI, 680, 208, 65, emptyArray()),
            sp(574, "Ophanimon Falldown Mode", PetStage.ULTIMATE, VI, 675, 207, 58, emptyArray()),
            sp(575, "Ravemon", PetStage.ULTIMATE, VA, 655, 204, 54, emptyArray()),
            sp(576, "ShineGreymon", PetStage.ULTIMATE, VA, 680, 210, 66, emptyArray()),
            sp(577, "MirageGaogamon", PetStage.ULTIMATE, DA, 665, 205, 60, emptyArray()),
            sp(578, "Jesmon GX", PetStage.ULTIMATE, DA, 730, 222, 68, emptyArray()),
            sp(579, "Gallantmon X", PetStage.ULTIMATE, VA, 700, 215, 65, emptyArray()),
            sp(580, "WarGreymon X", PetStage.ULTIMATE, VA, 695, 214, 66, emptyArray()),
            sp(581, "MetalGarurumon X", PetStage.ULTIMATE, DA, 690, 212, 64, emptyArray()),
            sp(582, "Beelzemon X", PetStage.ULTIMATE, VI, 705, 217, 65, emptyArray())
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
        JogressRecipe(422, 436, 522, FusionType.DNA_JOGRESS, "AeroVeedramon and Knightmon ascend in Blue Digizoid armor to UlforceVeedramon."),

        // Master Expansion Canonical Fusions
        JogressRecipe(559, 560, 535, FusionType.DNA_JOGRESS, "BlitzGreymon and CresGarurumon combine heavy plasma and golden blades into Omnimon Alter-S."),
        JogressRecipe(563, 564, 565, FusionType.DNA_JOGRESS, "Apollomon and Dianamon unite solar flame and lunar radiance into GraceNovamon."),
        JogressRecipe(568, 569, 567, FusionType.DNA_JOGRESS, "BanchoLeomon and Darkdramon clash and fuse their conflicting codes into Chaosmon."),
        JogressRecipe(523, 561, 524, FusionType.DIGIXROS, "Alphamon wields the invincible blade of Ouryumon to awaken Alphamon Ouryuken."),
        JogressRecipe(538, 539, 578, FusionType.DIGIXROS, "Jesmon inherits Gankoomon's fiery soul to transcend into Jesmon GX."),
        JogressRecipe(371, 333, 423, FusionType.DNA_JOGRESS, "ExVeemon and Stingmon achieve true dragon-insectoid DNA Jogress into Paildramon."),
        JogressRecipe(414, 445, 558, FusionType.DIGIXROS, "MetalTyrannomon and Volcamon fuse heavy magma cybernetics into RustTyrannomon."),
        JogressRecipe(434, 532, 566, FusionType.MODE_CHANGE, "Lucemon Chaos Mode absorbs Daemon's dark wrath to awaken Lucemon Satan Mode."),
        JogressRecipe(451, 562, 545, FusionType.DNA_JOGRESS, "Wingdramon and Slayerdramon unite into the Dragon Emperor Examon."),
        JogressRecipe(515, 227, 579, FusionType.MODE_CHANGE, "Gallantmon assimilates the X-Antibody core to awaken Gallantmon X.")
    )
}
