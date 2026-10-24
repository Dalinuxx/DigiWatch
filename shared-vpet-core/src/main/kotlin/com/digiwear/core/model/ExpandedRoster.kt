package com.digiwear.core.model

import com.digiwear.core.fusion.FusionType
import com.digiwear.core.fusion.JogressRecipe

/**
 * Expanded roster of 185 additional species layered on top of the base 165-species catalog (bringing the total to 350).
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
 *  - 130 Nyokimon -> Nyaromon / Poromon
 *  - 131 Pichimon -> Pyocomon / Upamon
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

    /** Baby II -> Child: 2 branches (good vs poor care) at 12h. */
    private fun hatch2(good: Int, bad: Int) = arrayOf(
        EvolutionRule(targetSpeciesId = good, minAgeSeconds = DAY / 2, minCareMistakes = 0, maxCareMistakes = 3, minEffort = 2),
        EvolutionRule(targetSpeciesId = bad, minAgeSeconds = DAY / 2, minCareMistakes = 4, maxCareMistakes = 999)
    )

    /** Baby II -> Child: 3 branches (effort/perfection, good care, poor care) at 12h. */
    private fun hatch3(best: Int, good: Int, bad: Int) = arrayOf(
        EvolutionRule(targetSpeciesId = best, minAgeSeconds = DAY / 2, minCareMistakes = 0, maxCareMistakes = 1, minEffort = 3),
        EvolutionRule(targetSpeciesId = good, minAgeSeconds = DAY / 2, minCareMistakes = 0, maxCareMistakes = 3, minEffort = 0),
        EvolutionRule(targetSpeciesId = bad, minAgeSeconds = DAY / 2, minCareMistakes = 4, maxCareMistakes = 999)
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
    private fun egg2(t1: Int, t2: Int) = arrayOf(
        EvolutionRule(targetSpeciesId = t1, minAgeSeconds = 3600L, minCareMistakes = 0, maxCareMistakes = 1, minEffort = 2),
        EvolutionRule(targetSpeciesId = t2, minAgeSeconds = 3600L)
    )

    private val F = ElementalAttribute.FREE
    private val VA = ElementalAttribute.VACCINE
    private val DA = ElementalAttribute.DATA
    private val VI = ElementalAttribute.VIRUS

    val SPECIES: List<PetSpecies> by lazy {
        listOf(
            // ---------------- Baby I (starter eggs) ----------------
            sp(130, "Nyokimon", PetStage.BABY_I, F, 50, 10, 5, arrayOf(
                EvolutionRule(targetSpeciesId = 133, minAgeSeconds = 3600L, minCareMistakes = 0, maxCareMistakes = 1, minEffort = 2),
                EvolutionRule(targetSpeciesId = 1114, minAgeSeconds = 3600L, minCareMistakes = 0, maxCareMistakes = 2, minEffort = 0),
                EvolutionRule(targetSpeciesId = 120, minAgeSeconds = 3600L)
            )),
            sp(131, "Pichimon", PetStage.BABY_I, F, 50, 10, 5, arrayOf(
                EvolutionRule(targetSpeciesId = 134, minAgeSeconds = 3600L, minCareMistakes = 0, maxCareMistakes = 1, minEffort = 2),
                EvolutionRule(targetSpeciesId = 1109, minAgeSeconds = 3600L, minCareMistakes = 0, maxCareMistakes = 2, minEffort = 0),
                EvolutionRule(targetSpeciesId = 121, minAgeSeconds = 3600L)
            )),
            sp(132, "Pukamon", PetStage.BABY_I, F, 50, 10, 5, arrayOf(
                EvolutionRule(targetSpeciesId = 1112, minAgeSeconds = 3600L, minCareMistakes = 0, maxCareMistakes = 1, minEffort = 2),
                EvolutionRule(targetSpeciesId = 122, minAgeSeconds = 3600L)
            )),

            // ---------------- Baby II (Original Expansion) ----------------
            sp(120, "Nyaromon", PetStage.BABY_II, F, 80, 20, 10, hatch4(223, 224, 241, 243)),
            sp(121, "Pyocomon", PetStage.BABY_II, F, 80, 20, 10, hatch4(225, 226, 230, 242)),
            sp(122, "Dorimon", PetStage.BABY_II, F, 80, 20, 10, hatch4(227, 232, 239, 246)),

            // ---------------- Baby II (Master Expansion) ----------------
            sp(123, "Tsumemon", PetStage.BABY_II, VI, 80, 20, 10, hatch(250, 231, 234)),
            sp(124, "Hopmon", PetStage.BABY_II, F, 80, 20, 10, hatch4(229, 244, 245, 257)),
            sp(125, "Kapurimon", PetStage.BABY_II, F, 80, 20, 10, hatch(237, 249, 233)),
            sp(126, "Minomon", PetStage.BABY_II, F, 80, 20, 10, hatch(238, 213, 228)),
            sp(127, "Sunmon", PetStage.BABY_II, F, 80, 20, 10, hatch4(247, 235, 201, 258)),
            sp(128, "Moonmon", PetStage.BABY_II, F, 80, 20, 10, hatch4(248, 236, 217, 259)),
            sp(129, "Ketomon", PetStage.BABY_II, F, 80, 20, 10, hatch(244, 240, 227)),
            sp(133, "Poromon", PetStage.BABY_II, F, 80, 20, 10, hatch4(225, 252, 253, 254)),
            sp(134, "Upamon", PetStage.BABY_II, F, 80, 20, 10, hatch4(226, 255, 256, 260)),

            // ---------------- Child (Original Expansion) ----------------
            sp(223, "Salamon", PetStage.CHILD, VA, 130, 35, 18, arrayOf(
                EvolutionRule(targetSpeciesId = 390, minAgeSeconds = DAY, minCareMistakes = 0, maxCareMistakes = 0, minEffort = 3),
                EvolutionRule(targetSpeciesId = 383, minAgeSeconds = DAY, minCareMistakes = 0, maxCareMistakes = 1, minEffort = 2),
                EvolutionRule(targetSpeciesId = 372, minAgeSeconds = DAY, minCareMistakes = 0, maxCareMistakes = 2, minEffort = 1),
                EvolutionRule(targetSpeciesId = 338, minAgeSeconds = DAY, minCareMistakes = 0, maxCareMistakes = 3, minEffort = 0),
                EvolutionRule(targetSpeciesId = 349, minAgeSeconds = DAY, minCareMistakes = 4, maxCareMistakes = 999)
            )),
            sp(224, "Gaomon", PetStage.CHILD, DA, 135, 36, 20, grow(346, 347)),
            sp(225, "Hawkmon", PetStage.CHILD, VA, 125, 34, 16, grow3(361, 344, 348)),
            sp(226, "Armadillomon", PetStage.CHILD, DA, 140, 33, 24, grow3(360, 345, 311)),
            sp(227, "Dorumon", PetStage.CHILD, DA, 135, 36, 20, grow(342, 316)),
            sp(228, "Kotemon", PetStage.CHILD, DA, 130, 37, 20, grow(343, 313)),
            sp(229, "Monodramon", PetStage.CHILD, VI, 130, 38, 20, grow3(384, 317, 318)),
            sp(230, "Floramon", PetStage.CHILD, DA, 120, 32, 15, grow(308, 330)),
            sp(231, "Goblimon", PetStage.CHILD, VI, 135, 37, 22, grow(312, 325)),

            // ---------------- Child (Master Expansion) ----------------
            sp(232, "Gomamon", PetStage.CHILD, VA, 135, 35, 20, grow(350, 347)),
            sp(233, "Gotsumon", PetStage.CHILD, DA, 140, 36, 25, grow(351, 311)),
            sp(234, "Otamamon", PetStage.CHILD, VI, 130, 34, 18, grow(352, 356)),
            sp(235, "Candlemon", PetStage.CHILD, DA, 125, 37, 16, grow(337, 304)),
            sp(236, "Bakumon", PetStage.CHILD, VA, 130, 33, 18, grow3(382, 343, 328)),
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

            // ---------------- Child (Ghost Game, Seekers & Vital Expansion) ----------------
            sp(251, "Pulsemon", PetStage.CHILD, VA, 130, 37, 18, arrayOf(
                EvolutionRule(targetSpeciesId = 374, minAgeSeconds = DAY, minCareMistakes = 0, maxCareMistakes = 1, minEffort = 3),
                EvolutionRule(targetSpeciesId = 391, minAgeSeconds = DAY, minCareMistakes = 0, maxCareMistakes = 2, minEffort = 1),
                EvolutionRule(targetSpeciesId = 392, minAgeSeconds = DAY, minCareMistakes = 2, maxCareMistakes = 3, minEffort = 0),
                EvolutionRule(targetSpeciesId = 393, minAgeSeconds = DAY, minCareMistakes = 4, maxCareMistakes = 999)
            )),
            sp(252, "Gammamon", PetStage.CHILD, VA, 135, 38, 19, arrayOf(
                EvolutionRule(targetSpeciesId = 375, minAgeSeconds = DAY, minCareMistakes = 0, maxCareMistakes = 1, minEffort = 3),
                EvolutionRule(targetSpeciesId = 376, minAgeSeconds = DAY, minCareMistakes = 0, maxCareMistakes = 2, minEffort = 0),
                EvolutionRule(targetSpeciesId = 377, minAgeSeconds = DAY, minCareMistakes = 3, maxCareMistakes = 4),
                EvolutionRule(targetSpeciesId = 378, minAgeSeconds = DAY, minCareMistakes = 5, maxCareMistakes = 999)
            )),
            sp(253, "Angoramon", PetStage.CHILD, VA, 140, 34, 22, grow(379, 347)),
            sp(254, "Jellymon", PetStage.CHILD, DA, 125, 35, 16, grow(380, 356)),
            sp(255, "Loogamon", PetStage.CHILD, VI, 135, 38, 20, grow(381, 385)),
            sp(256, "Shoutmon", PetStage.CHILD, F, 130, 36, 18, grow3(396, 373, 302)),
            sp(257, "Gumdramon", PetStage.CHILD, F, 130, 35, 17, grow(384, 318)),
            sp(258, "Agunimon", PetStage.CHILD, VA, 140, 40, 24, grow(304, 301)),
            sp(259, "Lobomon", PetStage.CHILD, VA, 135, 39, 22, grow(306, 343)),
            sp(260, "Dracmon", PetStage.CHILD, VI, 130, 36, 20, grow(386, 387)),
            sp(261, "Spadamon", PetStage.CHILD, F, 135, 38, 15, grow(389, 388)),
            sp(262, "Pteromon", PetStage.CHILD, VA, 130, 37, 16, grow(394, 308)),
            sp(263, "Shoemon", PetStage.CHILD, DA, 125, 35, 14, grow(395, 338)),

            // ---------------- Adult (Original Expansion) ----------------
            sp(342, "Dorugamon", PetStage.ADULT, DA, 370, 100, 32, mature(435, 440)),
            sp(343, "Sorcerymon", PetStage.ADULT, DA, 350, 98, 30, mature(436, 443)),
            sp(344, "Aquilamon", PetStage.ADULT, VA, 355, 100, 28, mature(437, 407)),
            sp(345, "Ankylomon", PetStage.ADULT, VA, 390, 95, 45, mature(441, 438)),
            sp(346, "Gaogamon", PetStage.ADULT, DA, 375, 102, 34, mature(466, 439)),
            sp(347, "Mojyamon", PetStage.ADULT, DA, 365, 92, 36, mature(462, 460)),
            sp(348, "Starmon", PetStage.ADULT, DA, 330, 94, 22, mature(402, 442)),
            sp(349, "Dokugumon", PetStage.ADULT, VI, 340, 96, 26, mature(458, 457)),

            // ---------------- Adult (Master Expansion) ----------------
            sp(350, "Ikkakumon", PetStage.ADULT, VA, 380, 98, 40, mature(438, 462)),
            sp(351, "Golemon", PetStage.ADULT, VI, 390, 95, 48, mature(445, 462)),
            sp(352, "Gekomon", PetStage.ADULT, DA, 350, 94, 30, mature(444, 446)),
            sp(353, "Clockmon", PetStage.ADULT, DA, 360, 96, 32, mature(464, 436)),
            sp(354, "Guardromon", PetStage.ADULT, VI, 385, 97, 42, mature(418, 463)),
            sp(355, "Woodmon", PetStage.ADULT, VI, 370, 92, 35, mature(474, 456)),
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
            sp(369, "Firamon", PetStage.ADULT, VA, 375, 102, 32, mature(476, 401)),
            sp(370, "Lekismon", PetStage.ADULT, DA, 365, 100, 29, mature(455, 404)),
            sp(371, "ExVeemon", PetStage.ADULT, F, 370, 102, 30, mature(475, 422)),
            sp(372, "Lynxmon", PetStage.ADULT, F, 365, 100, 28, mature(461, 427)),

            // ---------------- Adult (Ghost Game, Seekers, Armor & Commando) ----------------
            sp(373, "GeoGreymon", PetStage.ADULT, VA, 380, 104, 35, mature(465, 463)),
            sp(374, "Bulkmon", PetStage.ADULT, VA, 385, 105, 38, mature(467, 445)),
            sp(375, "BetelGammamon", PetStage.ADULT, VA, 375, 102, 32, mature(468, 454)),
            sp(376, "KausGammamon", PetStage.ADULT, DA, 365, 101, 28, mature(468, 451)),
            sp(377, "WezenGammamon", PetStage.ADULT, DA, 390, 98, 45, mature(468, 418)),
            sp(378, "GulusGammamon", PetStage.ADULT, VI, 380, 106, 30, mature(469, 448)),
            sp(379, "SymbareAngoramon", PetStage.ADULT, VA, 365, 100, 26, mature(470, 439)),
            sp(380, "TeslaJellymon", PetStage.ADULT, DA, 360, 99, 25, mature(471, 453)),
            sp(381, "Loogarmon", PetStage.ADULT, VI, 375, 103, 32, mature(472, 480)),
            sp(382, "Pegasusmon", PetStage.ADULT, VA, 370, 100, 30, mature(461, 449)),
            sp(383, "Nefertimon", PetStage.ADULT, VA, 365, 98, 28, mature(427, 437)),
            sp(384, "Strikedramon", PetStage.ADULT, VA, 375, 102, 32, mature(464, 416)),
            sp(385, "Dobermon", PetStage.ADULT, VA, 365, 99, 28, mature(477, 456)),
            sp(386, "Sangloupmon", PetStage.ADULT, VI, 370, 101, 30, mature(473, 457)),
            sp(387, "DarkKnightmon", PetStage.ADULT, VI, 390, 105, 48, mature(459, 440)),
            sp(388, "Zubaeagermon", PetStage.ADULT, VA, 360, 100, 28, mature(1423, 436)),
            sp(389, "TiaLudomon", PetStage.ADULT, DA, 375, 96, 32, mature(478, 441)),
            sp(390, "Meicoomon", PetStage.ADULT, F, 360, 98, 26, mature(479, 437)),
            sp(391, "Runnermon", PetStage.ADULT, VA, 355, 99, 24, mature(485, 467)),
            sp(392, "Exermon", PetStage.ADULT, DA, 365, 97, 28, mature(486, 488)),
            sp(393, "Namakemon", PetStage.ADULT, VI, 385, 92, 36, mature(487, 440)),
            sp(394, "Galemon", PetStage.ADULT, VA, 360, 100, 26, mature(481, 407)),
            sp(395, "Shoeshoemon", PetStage.ADULT, DA, 350, 96, 22, mature(482, 406)),
            sp(396, "Shoutmon X4", PetStage.ADULT, VA, 390, 106, 35, mature(484, 483)),
            sp(397, "Garmmon", PetStage.ADULT, DA, 375, 102, 30, mature(1412, 404)),
            sp(398, "Kuwagamon X", PetStage.ADULT, VI, 370, 104, 32, mature(424, 452)),

            // ---------------- Perfect (Original Expansion) ----------------
            sp(435, "Dorugreymon", PetStage.PERFECT, DA, 440, 130, 42, ascend(523, 550)),
            sp(436, "Knightmon", PetStage.PERFECT, DA, 420, 124, 40, arrayOf(
                EvolutionRule(targetSpeciesId = 578, minAgeSeconds = 3 * DAY, minBattles = 35, minWinRatio = 0.85f, minCareMistakes = 0, maxCareMistakes = 0, minEffort = 4),
                EvolutionRule(targetSpeciesId = 541, minAgeSeconds = 3 * DAY, minBattles = 30, minWinRatio = 0.80f, minCareMistakes = 0, maxCareMistakes = 1, minEffort = 4),
                EvolutionRule(targetSpeciesId = 544, minAgeSeconds = 3 * DAY, minBattles = 30, minWinRatio = 0.80f, minCareMistakes = 2, maxCareMistakes = 3, minEffort = 3),
                EvolutionRule(targetSpeciesId = 551, minAgeSeconds = 3 * DAY, minBattles = 25, minWinRatio = 0.75f, minCareMistakes = 4, maxCareMistakes = 999)
            )),
            sp(437, "Silphymon", PetStage.PERFECT, VA, 430, 128, 34, ascend(552, 556)),
            sp(438, "Zudomon", PetStage.PERFECT, DA, 460, 126, 55, ascend(553, 581)),
            sp(439, "Panjyamon", PetStage.PERFECT, DA, 440, 128, 48, ascend(568, 560)),
            sp(440, "Phantomon", PetStage.PERFECT, VI, 410, 125, 36, ascend(554, 532)),
            sp(441, "Shakkoumon", PetStage.PERFECT, VA, 460, 128, 48, ascend(553, 555)),
            sp(442, "Digitamamon", PetStage.PERFECT, VI, 470, 122, 60, ascend(530, 532)),
            sp(443, "Cherrymon", PetStage.PERFECT, DA, 445, 120, 50, ascend(557, 505)),

            // ---------------- Perfect (Master Expansion) ----------------
            sp(444, "ShogunGekomon", PetStage.PERFECT, DA, 460, 126, 50, ascend(570, 508)),
            sp(445, "Volcamon", PetStage.PERFECT, DA, 450, 128, 46, ascend(1563, 502)),
            sp(446, "MarineDevimon", PetStage.PERFECT, VI, 440, 127, 42, ascend(533, 532)),
            sp(447, "WaruSeadramon", PetStage.PERFECT, VI, 445, 129, 44, ascend(554, 533)),
            sp(448, "Infermon", PetStage.PERFECT, VI, 455, 132, 40, ascend(527, 528)),
            sp(449, "Chirinmon", PetStage.PERFECT, VA, 450, 130, 38, ascend(555, 543)),
            sp(450, "Hisyaryumon", PetStage.PERFECT, VA, 455, 133, 42, ascend(561, 580)),
            sp(451, "Wingdramon", PetStage.PERFECT, VA, 450, 132, 40, ascend(562, 576)),
            sp(452, "Yatagaramon", PetStage.PERFECT, VA, 445, 131, 36, ascend(575, 544)),
            sp(453, "Lilamon", PetStage.PERFECT, DA, 430, 126, 32, ascend(572, 505)),
            sp(454, "Flaremon", PetStage.PERFECT, VA, 460, 134, 44, ascend(563, 565)),
            sp(455, "Crescemon", PetStage.PERFECT, DA, 450, 132, 38, ascend(564, 565)),
            sp(456, "SkullSatamon", PetStage.PERFECT, VI, 440, 133, 36, ascend(573, 532)),
            sp(457, "Mummymon", PetStage.PERFECT, VI, 435, 125, 38, ascend(566, 534)),
            sp(458, "Arukenimon", PetStage.PERFECT, VI, 435, 127, 36, ascend(571, 531)),
            sp(459, "Grademon", PetStage.PERFECT, VA, 450, 132, 42, ascend(523, 551)),
            sp(460, "Astamon", PetStage.PERFECT, VI, 445, 130, 38, ascend(1555, 1556)),
            sp(461, "Gargoylemon", PetStage.PERFECT, VA, 440, 128, 36, ascend(552, 574)),
            sp(462, "Mammothmon", PetStage.PERFECT, VA, 465, 128, 55, ascend(581, 553)),
            sp(463, "MetalGreymon (Virus)", PetStage.PERFECT, VI, 450, 132, 45, ascend(559, 525)),
            sp(464, "Cyberdramon (Savers)", PetStage.PERFECT, VA, 445, 130, 38, ascend(567, 569)),

            // ---------------- Perfect (Ghost Game, Seekers & Modern Champions) ----------------
            sp(465, "RizeGreymon", PetStage.PERFECT, VA, 455, 133, 45, ascend(589, 576)),
            sp(466, "MachGaogamon", PetStage.PERFECT, DA, 450, 132, 42, ascend(590, 577)),
            sp(467, "Boutmon", PetStage.PERFECT, VA, 460, 134, 46, ascend(587, 568)),
            sp(468, "Canoweissmon", PetStage.PERFECT, VA, 460, 135, 42, ascend(583, 562)),
            sp(469, "Regulusmon", PetStage.PERFECT, VI, 465, 136, 44, ascend(588, 566)),
            sp(470, "Lamortemon", PetStage.PERFECT, VA, 450, 131, 48, ascend(584, 568)),
            sp(471, "Thetismon", PetStage.PERFECT, DA, 440, 130, 36, ascend(585, 570)),
            sp(472, "Soloogarmon", PetStage.PERFECT, VI, 455, 134, 42, ascend(586, 560)),
            sp(473, "Baalmon", PetStage.PERFECT, F, 445, 132, 38, ascend(519, 591)),
            sp(474, "JewelBeemon", PetStage.PERFECT, VA, 440, 130, 35, ascend(1560, 507)),
            sp(475, "Dinobeemon", PetStage.PERFECT, F, 450, 133, 40, ascend(506, 512)),
            sp(476, "SkullMeramon", PetStage.PERFECT, DA, 460, 133, 45, ascend(554, 502)),
            sp(477, "Cerberusmon", PetStage.PERFECT, VA, 455, 132, 42, ascend(582, 534)),
            sp(478, "RaijiLudomon", PetStage.PERFECT, DA, 470, 134, 48, ascend(595, 553)),
            sp(479, "Meicrackmon", PetStage.PERFECT, VA, 460, 132, 40, ascend(598, 552)),
            sp(480, "Helloogarmon", PetStage.PERFECT, VI, 465, 136, 44, ascend(586, 588)),
            sp(481, "GrandGalemon", PetStage.PERFECT, VA, 460, 135, 42, ascend(1551, 506)),
            sp(482, "Chaperomon", PetStage.PERFECT, DA, 445, 130, 36, ascend(1552, 505)),
            sp(483, "OmegaShoutmon", PetStage.PERFECT, VA, 480, 140, 44, ascend(1554, 510)),
            sp(484, "Shoutmon X5", PetStage.PERFECT, VA, 485, 142, 48, ascend(1554, 558)),
            sp(485, "Climbmon", PetStage.PERFECT, VA, 455, 133, 40, ascend(1548, 587)),
            sp(486, "Divemon", PetStage.PERFECT, DA, 450, 131, 38, ascend(1549, 587)),
            sp(487, "Shootmon", PetStage.PERFECT, VI, 465, 135, 46, ascend(1550, 587)),
            sp(488, "Tempomon", PetStage.PERFECT, DA, 450, 132, 38, ascend(1549, 587)),

            // ---------------- Ultimate (Original Expansion) ----------------
            sp(550, "Dorugoramon", PetStage.ULTIMATE, DA, 640, 200, 55, arrayOf(EvolutionRule(targetSpeciesId = 1557, minAgeSeconds = 3 * DAY, minBattles = 40, minWinRatio = 0.85f))),
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
            sp(582, "Beelzemon X", PetStage.ULTIMATE, VI, 705, 217, 65, emptyArray()),

            // ---------------- Ultimate (Ghost Game, Seekers & Apex Burst Fusions) ----------------
            sp(583, "Siriusmon", PetStage.ULTIMATE, VA, 690, 214, 64, arrayOf(EvolutionRule(targetSpeciesId = 597, minAgeSeconds = 3 * DAY, minBattles = 40, minWinRatio = 0.85f))),
            sp(584, "Diarbbitmon", PetStage.ULTIMATE, VA, 670, 208, 58, emptyArray()),
            sp(585, "Amphimon", PetStage.ULTIMATE, DA, 660, 205, 52, emptyArray()),
            sp(586, "Fenriloogamon", PetStage.ULTIMATE, VI, 705, 217, 65, arrayOf(EvolutionRule(targetSpeciesId = 1553, minAgeSeconds = 3 * DAY, minBattles = 40, minWinRatio = 0.85f))),
            sp(587, "Kazuchimon", PetStage.ULTIMATE, VA, 680, 212, 60, emptyArray()),
            sp(588, "Arcturusmon", PetStage.ULTIMATE, VI, 710, 218, 66, emptyArray()),
            sp(589, "ShineGreymon Burst Mode", PetStage.ULTIMATE, VA, 720, 220, 70, emptyArray()),
            sp(590, "MirageGaogamon Burst Mode", PetStage.ULTIMATE, DA, 710, 218, 66, emptyArray()),
            sp(591, "Beelzemon (2010)", PetStage.ULTIMATE, VI, 725, 222, 70, emptyArray()),
            sp(592, "Omnimon Merciful Mode", PetStage.ULTIMATE, VA, 740, 225, 72, emptyArray()),
            sp(593, "Merukimon", PetStage.ULTIMATE, VI, 700, 216, 56, emptyArray()),
            sp(594, "Durandamon", PetStage.ULTIMATE, VA, 715, 222, 65, arrayOf(EvolutionRule(targetSpeciesId = 596, minAgeSeconds = 3 * DAY, minBattles = 40, minWinRatio = 0.85f))),
            sp(595, "BryweLudramon", PetStage.ULTIMATE, DA, 725, 218, 70, arrayOf(EvolutionRule(targetSpeciesId = 596, minAgeSeconds = 3 * DAY, minBattles = 40, minWinRatio = 0.85f))),
            sp(596, "RagnaLoardmon", PetStage.ULTIMATE, VI, 745, 235, 75, emptyArray()),
            sp(597, "Proximamon", PetStage.ULTIMATE, F, 750, 240, 78, emptyArray()),
            sp(598, "Raguelmon", PetStage.ULTIMATE, F, 710, 220, 60, arrayOf(EvolutionRule(targetSpeciesId = 599, minAgeSeconds = 3 * DAY, minBattles = 40, minWinRatio = 0.85f))),
            sp(599, "Ordinemon", PetStage.ULTIMATE, F, 750, 238, 80, emptyArray()),
            // ---------------- BABY_I (500 Roster Expansion) ----------------
            sp(1101, "Cotsucomon", PetStage.BABY_I, F, 50, 10, 5, egg2(1102, 1103)),
            sp(1104, "Fufumon", PetStage.BABY_I, F, 50, 10, 5, egg2(1105, 1116)),
            sp(1106, "Algomon Baby I", PetStage.BABY_I, F, 50, 10, 5, egg(1107)),
            sp(1108, "Dokimon", PetStage.BABY_I, F, 50, 10, 5, egg2(1109, 1110)),
            sp(1111, "Choromon", PetStage.BABY_I, F, 50, 10, 5, egg2(1112, 1114)),
            sp(1113, "Bubbmon", PetStage.BABY_I, F, 50, 10, 5, egg2(1115, 1114)),
            // ---------------- BABY_II (500 Roster Expansion) ----------------
            sp(1102, "Kakkinmon", PetStage.BABY_II, F, 70, 15, 10, hatch4(252, 1220, 1218, 1205)),
            sp(1103, "Sakuttomon", PetStage.BABY_II, F, 70, 15, 10, hatch4(261, 1218, 1216, 1225)),
            sp(1105, "Chibimon", PetStage.BABY_II, F, 70, 15, 10, hatch4(262, 1204, 1201, 1217)),
            sp(1107, "Algomon Baby II", PetStage.BABY_II, F, 70, 15, 10, hatch2(1212, 1211)),
            sp(1109, "Bibimon", PetStage.BABY_II, F, 70, 15, 10, hatch4(251, 1221, 1207, 1210)),
            sp(1110, "Wanyamon", PetStage.BABY_II, F, 70, 15, 10, hatch4(263, 1203, 1202, 1223)),
            sp(1112, "Caprimon", PetStage.BABY_II, F, 70, 15, 10, hatch3(1219, 1213, 1214)),
            sp(1114, "Mochimon", PetStage.BABY_II, F, 70, 15, 10, hatch2(1215, 1206)),
            sp(1115, "Budmon", PetStage.BABY_II, F, 70, 15, 10, hatch2(1208, 1222)),
            sp(1116, "Babydmon", PetStage.BABY_II, F, 70, 15, 10, hatch3(1209, 1224, 1204)),
            // ---------------- CHILD (500 Roster Expansion) ----------------
            sp(1201, "Agumon X", PetStage.CHILD, VA, 190, 35, 16, grow3(1310, 1330, 1301)),
            sp(1202, "Agumon (Black) X", PetStage.CHILD, VI, 190, 36, 17, grow3(1301, 1302, 1315)),
            sp(1203, "Gabumon X", PetStage.CHILD, DA, 185, 34, 16, grow3(1311, 1329, 1309)),
            sp(1204, "Dracomon X", PetStage.CHILD, DA, 190, 35, 16, grow3(1330, 1325, 1302)),
            sp(1205, "Bearmon", PetStage.CHILD, VA, 195, 36, 18, grow3(1317, 1314, 1324)),
            sp(1206, "Ganimon", PetStage.CHILD, DA, 180, 32, 17, grow3(1303, 1322, 1326)),
            sp(1207, "Junkmon", PetStage.CHILD, VI, 175, 30, 15, grow3(1305, 1313, 1323)),
            sp(1208, "Pomumon", PetStage.CHILD, DA, 180, 32, 16, grow3(1307, 1306, 1328)),
            sp(1209, "Sangomon", PetStage.CHILD, DA, 175, 31, 15, grow3(1309, 1322, 1326)),
            sp(1210, "Sunarizamon", PetStage.CHILD, VI, 185, 34, 16, grow3(1316, 1303, 1324)),
            sp(1211, "Ghostmon", PetStage.CHILD, VI, 170, 33, 14, grow3(1315, 1308, 1323)),
            sp(1212, "Algomon Child", PetStage.CHILD, VI, 190, 35, 18, grow3(1304, 1305, 1319)),
            sp(1213, "Terriermon X", PetStage.CHILD, VA, 180, 32, 15, grow3(1327, 1321, 1314)),
            sp(1214, "Lopmon X", PetStage.CHILD, DA, 180, 32, 15, grow3(1329, 1320, 1312)),
            sp(1215, "KoKabuterimon", PetStage.CHILD, VI, 185, 33, 16, arrayOf(
                EvolutionRule(targetSpeciesId = 398, minAgeSeconds = DAY, minCareMistakes = 0, maxCareMistakes = 1, minEffort = 3),
                EvolutionRule(targetSpeciesId = 1313, minAgeSeconds = DAY, minCareMistakes = 0, maxCareMistakes = 2, minEffort = 0),
                EvolutionRule(targetSpeciesId = 1304, minAgeSeconds = DAY, minCareMistakes = 3, maxCareMistakes = 4),
                EvolutionRule(targetSpeciesId = 1320, minAgeSeconds = DAY, minCareMistakes = 5, maxCareMistakes = 999)
            )),
            sp(1216, "Liollmon", PetStage.CHILD, VA, 190, 35, 17, grow3(1318, 1317, 1327)),
            sp(1217, "Paledramon", PetStage.CHILD, DA, 185, 34, 16, grow3(1314, 1325, 1309)),
            sp(1218, "Zubamon", PetStage.CHILD, VA, 230, 68, 20, grow3(1318, 1310, 1327)),
            sp(1219, "Koemon", PetStage.CHILD, VI, 180, 32, 15, grow3(1316, 1308, 1324)),
            sp(1220, "Ludomon", PetStage.CHILD, VA, 250, 55, 22, grow3(1327, 1303, 1326)),
            sp(1221, "Herissmon", PetStage.CHILD, DA, 220, 62, 18, grow3(1313, 1318, 1310)),
            sp(1222, "Morphomon", PetStage.CHILD, F, 180, 32, 15, grow3(1312, 1319, 1321)),
            sp(1223, "Strabimon", PetStage.CHILD, F, 190, 35, 16, arrayOf(
                EvolutionRule(targetSpeciesId = 397, minAgeSeconds = DAY, minCareMistakes = 0, maxCareMistakes = 1, minEffort = 3),
                EvolutionRule(targetSpeciesId = 1311, minAgeSeconds = DAY, minCareMistakes = 0, maxCareMistakes = 2, minEffort = 0),
                EvolutionRule(targetSpeciesId = 1315, minAgeSeconds = DAY, minCareMistakes = 3, maxCareMistakes = 4),
                EvolutionRule(targetSpeciesId = 1317, minAgeSeconds = DAY, minCareMistakes = 5, maxCareMistakes = 999)
            )),
            sp(1224, "Plotmon", PetStage.CHILD, VA, 185, 33, 16, grow3(1321, 1306, 1307)),
            sp(1225, "Bokomon", PetStage.CHILD, F, 160, 25, 14, grow3(1324, 1328, 1312)),
            // ---------------- ADULT (500 Roster Expansion) ----------------
            sp(1301, "Darklizamon", PetStage.ADULT, VI, 340, 78, 28, mature(1418, 1424)),
            sp(1302, "Deltamon", PetStage.ADULT, VI, 350, 80, 30, mature(1408, 1420)),
            sp(1303, "Tortamon", PetStage.ADULT, VA, 360, 76, 32, mature(1404, 1406)),
            sp(1304, "Snimon", PetStage.ADULT, VI, 345, 82, 26, mature(1405, 1420)),
            sp(1305, "Gokimon", PetStage.ADULT, VI, 330, 74, 25, mature(1434, 1401)),
            sp(1306, "Cockatrimon", PetStage.ADULT, DA, 340, 76, 28, mature(1427, 1407)),
            sp(1307, "Kiwimon", PetStage.ADULT, DA, 335, 75, 27, mature(1407, 1425)),
            sp(1308, "Musyamon", PetStage.ADULT, VI, 360, 84, 30, mature(1408, 1426)),
            sp(1309, "Rukamon", PetStage.ADULT, VA, 350, 78, 29, mature(1417, 1404)),
            sp(1310, "Agnimon", PetStage.ADULT, F, 370, 85, 30, mature(1410, 1411)),
            sp(1311, "Wolfmon", PetStage.ADULT, F, 365, 84, 29, mature(1412, 1433)),
            sp(1312, "Fairimon", PetStage.ADULT, F, 340, 78, 26, mature(1407, 1427)),
            sp(1313, "Blitzmon", PetStage.ADULT, F, 375, 86, 32, mature(1414, 1430)),
            sp(1314, "Chackmon", PetStage.ADULT, F, 350, 80, 27, mature(1433, 1431)),
            sp(1315, "Duskmon", PetStage.ADULT, VI, 370, 88, 30, mature(1413, 1415)),
            sp(1316, "Baboongamon", PetStage.ADULT, DA, 365, 84, 30, mature(1426, 1425)),
            sp(1317, "Kaiser Leomon", PetStage.ADULT, F, 375, 87, 31, mature(1414, 1428)),
            sp(1318, "Aegiomon", PetStage.ADULT, VA, 360, 82, 28, mature(1409, 1422)),
            sp(1319, "Shadramon", PetStage.ADULT, F, 355, 84, 28, mature(1410, 1434)),
            sp(1320, "Flybeemon", PetStage.ADULT, F, 340, 80, 26, mature(1405, 1420)),
            sp(1321, "Harpymon", PetStage.ADULT, F, 345, 81, 27, mature(1427, 1408)),
            sp(1322, "Manbomon", PetStage.ADULT, F, 350, 78, 30, mature(1403, 1401)),
            sp(1323, "Pipismon", PetStage.ADULT, F, 335, 76, 25, mature(1415, 1432)),
            sp(1324, "Baromon", PetStage.ADULT, F, 355, 83, 28, mature(1421, 1408)),
            sp(1325, "Pteranomon", PetStage.ADULT, F, 350, 82, 29, mature(1419, 1417)),
            sp(1326, "Marin Bullmon", PetStage.ADULT, F, 360, 85, 32, mature(1402, 1416)),
            sp(1327, "Kongoumon", PetStage.ADULT, F, 365, 84, 33, mature(1423, 1429)),
            sp(1328, "Peacockmon", PetStage.ADULT, F, 345, 80, 28, mature(1425, 1427)),
            sp(1329, "Moosemon", PetStage.ADULT, F, 360, 82, 31, mature(1433, 1428)),
            sp(1330, "Fladramon", PetStage.ADULT, F, 370, 86, 30, mature(1419, 1411)),
            // ---------------- PERFECT (500 Roster Expansion) ----------------
            sp(1401, "Tonosama Gekomon", PetStage.PERFECT, VI, 480, 142, 46, ascend(1506, 1530)),
            sp(1402, "Dagomon", PetStage.PERFECT, VI, 510, 154, 52, ascend(1503, 1537)),
            sp(1403, "Pukumon", PetStage.PERFECT, VI, 490, 146, 48, ascend(1521, 1503)),
            sp(1404, "Hangyomon", PetStage.PERFECT, DA, 475, 140, 45, ascend(1521, 1503)),
            sp(1405, "Anomalocarimon", PetStage.PERFECT, DA, 495, 148, 49, ascend(1503, 1527)),
            sp(1406, "Scorpiomon", PetStage.PERFECT, DA, 500, 150, 50, ascend(1520, 1545)),
            sp(1407, "Blossomon", PetStage.PERFECT, DA, 485, 144, 46, ascend(1505, 1502)),
            sp(1408, "Asuramon", PetStage.PERFECT, VA, 515, 156, 52, ascend(1504, 1511)),
            sp(1409, "Aegiochusmon", PetStage.PERFECT, VA, 505, 152, 48, ascend(1507, 1501)),
            sp(1410, "Vritramon", PetStage.PERFECT, F, 520, 158, 52, ascend(1513, 1519)),
            sp(1411, "Aldamon", PetStage.PERFECT, F, 535, 164, 54, ascend(1523, 1513)),
            sp(1412, "Beowolfmon", PetStage.PERFECT, F, 530, 162, 53, ascend(1524, 1514)),
            sp(1413, "Velgrmon", PetStage.PERFECT, VI, 525, 160, 53, ascend(1518, 1509)),
            sp(1414, "Raihimon", PetStage.PERFECT, F, 530, 163, 54, ascend(1515, 1507)),
            sp(1415, "Fantomon", PetStage.PERFECT, VI, 480, 145, 45, ascend(1533, 1534)),
            sp(1416, "Marin Devimon", PetStage.PERFECT, VI, 505, 152, 50, ascend(1503, 1509)),
            sp(1417, "Mega Seadramon", PetStage.PERFECT, DA, 510, 155, 51, ascend(1535, 1526)),
            sp(1418, "Waru Seadramon", PetStage.PERFECT, VI, 515, 156, 52, ascend(1535, 1545)),
            sp(1419, "Aero V-dramon", PetStage.PERFECT, VA, 480, 145, 42, ascend(1540, 1544)),
            sp(1420, "Arachnemon", PetStage.PERFECT, VI, 460, 140, 44, ascend(1508, 1537)),
            sp(1421, "Lucemon Falldown Mode", PetStage.PERFECT, VI, 545, 168, 55, ascend(1537, 1536)),
            sp(1422, "Cerberumon", PetStage.PERFECT, VA, 490, 148, 46, ascend(1509, 1518)),
            sp(1423, "Duramon", PetStage.PERFECT, VA, 485, 144, 40, ascend(594, 1539)),
            sp(1424, "Mephismon", PetStage.PERFECT, VI, 510, 154, 50, ascend(1533, 1512)),
            sp(1425, "Mihiramon", PetStage.PERFECT, DA, 505, 152, 49, ascend(1525, 1528)),
            sp(1426, "Vajramon", PetStage.PERFECT, VA, 515, 156, 53, ascend(1527, 1529)),
            sp(1427, "Andiramon Deva", PetStage.PERFECT, DA, 510, 153, 50, ascend(1510, 1516)),
            sp(1428, "Cerberumon Werewolf Mode", PetStage.PERFECT, VA, 520, 157, 52, ascend(1509, 1518)),
            sp(1429, "Justimon Blitz Arm", PetStage.PERFECT, VA, 525, 160, 53, ascend(1541, 1543)),
            sp(1430, "Thunderballmon", PetStage.PERFECT, DA, 480, 142, 45, ascend(1515, 1532)),
            sp(1431, "Crys Paledramon", PetStage.PERFECT, DA, 520, 158, 52, ascend(1517, 1542)),
            sp(1432, "Metal Fantomon", PetStage.PERFECT, VI, 515, 156, 51, ascend(1534, 1531)),
            sp(1433, "Blizzarmon", PetStage.PERFECT, F, 520, 156, 52, ascend(1517, 1514)),
            sp(1434, "Algomon Perfect", PetStage.PERFECT, VI, 530, 162, 54, ascend(1522, 1537)),
            // ---------------- ULTIMATE (500 Roster Expansion) ----------------
            sp(1501, "Marsmon", PetStage.ULTIMATE, VA, 680, 210, 62, emptyArray()),
            sp(1502, "Minervamon", PetStage.ULTIMATE, VI, 675, 208, 58, emptyArray()),
            sp(1503, "Neptunemon", PetStage.ULTIMATE, VA, 685, 212, 64, emptyArray()),
            sp(1504, "Vulcanusmon", PetStage.ULTIMATE, DA, 690, 214, 66, emptyArray()),
            sp(1505, "Ceresmon", PetStage.ULTIMATE, DA, 680, 209, 62, emptyArray()),
            sp(1506, "Bacchusmon", PetStage.ULTIMATE, VI, 670, 206, 70, emptyArray()),
            sp(1507, "Jupitermon", PetStage.ULTIMATE, VA, 710, 220, 68, emptyArray()),
            sp(1508, "Junomon", PetStage.ULTIMATE, VI, 700, 216, 65, emptyArray()),
            sp(1509, "Plutomon", PetStage.ULTIMATE, VI, 705, 218, 67, emptyArray()),
            sp(1510, "Venusmon", PetStage.ULTIMATE, VA, 660, 202, 54, emptyArray()),
            sp(1511, "Titamon", PetStage.ULTIMATE, VI, 715, 222, 72, emptyArray()),
            sp(1512, "Apollomon Whispered", PetStage.ULTIMATE, VI, 695, 215, 66, emptyArray()),
            sp(1513, "Ancient Greymon", PetStage.ULTIMATE, DA, 710, 220, 68, emptyArray()),
            sp(1514, "Ancient Garurumon", PetStage.ULTIMATE, DA, 705, 218, 66, emptyArray()),
            sp(1515, "Ancient Beatmon", PetStage.ULTIMATE, VA, 700, 216, 70, emptyArray()),
            sp(1516, "Ancient Irismon", PetStage.ULTIMATE, VA, 680, 210, 60, emptyArray()),
            sp(1517, "Ancient Megatheriumon", PetStage.ULTIMATE, DA, 715, 222, 75, emptyArray()),
            sp(1518, "Ancient Sphinxmon", PetStage.ULTIMATE, VI, 705, 218, 68, emptyArray()),
            sp(1519, "Ancient Volcamon", PetStage.ULTIMATE, DA, 710, 220, 74, emptyArray()),
            sp(1520, "Ancient Troiamon", PetStage.ULTIMATE, DA, 720, 224, 78, emptyArray()),
            sp(1521, "Ancient Mermaimon", PetStage.ULTIMATE, DA, 685, 212, 62, emptyArray()),
            sp(1522, "Ancient Wisemon", PetStage.ULTIMATE, VI, 690, 214, 64, emptyArray()),
            sp(1523, "Kaiser Greymon", PetStage.ULTIMATE, F, 725, 225, 70, emptyArray()),
            sp(1524, "Magna Garurumon", PetStage.ULTIMATE, F, 720, 224, 68, emptyArray()),
            sp(1525, "Baihumon", PetStage.ULTIMATE, DA, 715, 222, 72, emptyArray()),
            sp(1526, "Qinglongmon", PetStage.ULTIMATE, DA, 720, 224, 74, emptyArray()),
            sp(1527, "Xuanwumon", PetStage.ULTIMATE, VI, 730, 226, 80, emptyArray()),
            sp(1528, "Zhuqiaomon", PetStage.ULTIMATE, VI, 715, 222, 70, emptyArray()),
            sp(1529, "Huanglongmon", PetStage.ULTIMATE, F, 750, 235, 82, emptyArray()),
            sp(1530, "Metal Etemon", PetStage.ULTIMATE, VI, 665, 205, 58, emptyArray()),
            sp(1531, "King Etemon", PetStage.ULTIMATE, VI, 675, 208, 60, emptyArray()),
            sp(1532, "Ebemon", PetStage.ULTIMATE, VI, 660, 204, 56, emptyArray()),
            sp(1533, "Belial Vamdemon", PetStage.ULTIMATE, VI, 710, 220, 70, emptyArray()),
            sp(1534, "Venom Vamdemon", PetStage.ULTIMATE, VI, 720, 223, 75, emptyArray()),
            sp(1535, "Aegisdramon", PetStage.ULTIMATE, VA, 680, 205, 62, emptyArray()),
            sp(1536, "Belphemon Rage Mode", PetStage.ULTIMATE, VI, 730, 228, 76, emptyArray()),
            sp(1537, "Ogudomon", PetStage.ULTIMATE, VI, 745, 232, 80, emptyArray()),
            sp(1538, "Jesmon X", PetStage.ULTIMATE, DA, 725, 226, 68, emptyArray()),
            sp(1539, "Gankoomon X", PetStage.ULTIMATE, DA, 720, 224, 67, emptyArray()),
            sp(1540, "Dynasmon X", PetStage.ULTIMATE, DA, 715, 222, 68, emptyArray()),
            sp(1541, "Craniummon X", PetStage.ULTIMATE, VA, 720, 225, 70, emptyArray()),
            sp(1542, "Sleipmon X", PetStage.ULTIMATE, VA, 715, 222, 69, emptyArray()),
            sp(1543, "Duftmon X", PetStage.ULTIMATE, DA, 710, 220, 66, emptyArray()),
            sp(1544, "Magnamon X", PetStage.ULTIMATE, F, 730, 228, 72, emptyArray()),
            sp(1545, "Chaosdramon X", PetStage.ULTIMATE, VI, 735, 230, 78, emptyArray()),
            sp(1546, "Agumon (Bond of Courage)", PetStage.ULTIMATE, VA, 740, 232, 64, emptyArray()),
            sp(1547, "Gabumon (Bond of Friendship)", PetStage.ULTIMATE, DA, 735, 230, 62, emptyArray()),
            sp(1548, "Achillesmon", PetStage.ULTIMATE, VA, 715, 224, 58, emptyArray()),
            sp(1549, "Shivamon", PetStage.ULTIMATE, DA, 710, 222, 54, emptyArray()),
            sp(1550, "Shroudmon", PetStage.ULTIMATE, VI, 725, 228, 62, emptyArray()),
            sp(1551, "Zephagamon", PetStage.ULTIMATE, VA, 720, 225, 60, emptyArray()),
            sp(1552, "Cendrillmon", PetStage.ULTIMATE, DA, 715, 222, 55, emptyArray()),
            sp(1553, "Fenriloogamon: Takemikazuchi", PetStage.ULTIMATE, VI, 750, 238, 72, emptyArray()),
            sp(1554, "Shoutmon X7", PetStage.ULTIMATE, VA, 745, 234, 76, emptyArray()),
            sp(1555, "Bagramon", PetStage.ULTIMATE, VI, 730, 228, 68, emptyArray()),
            sp(1556, "Tactimon", PetStage.ULTIMATE, VI, 725, 226, 70, emptyArray()),
            sp(1557, "Death-X-DORUgoramon", PetStage.ULTIMATE, VI, 740, 232, 78, emptyArray()),
            sp(1558, "Omnimon Alter-B", PetStage.ULTIMATE, VI, 750, 238, 75, emptyArray()),
            sp(1559, "Omnimon Zwart Defeat", PetStage.ULTIMATE, VI, 745, 235, 74, emptyArray()),
            sp(1560, "BanchoStingmon", PetStage.ULTIMATE, F, 715, 225, 60, emptyArray()),
            sp(1561, "BanchoLilimon", PetStage.ULTIMATE, DA, 705, 220, 52, emptyArray()),
            sp(1562, "BanchoMamemon", PetStage.ULTIMATE, DA, 710, 222, 54, emptyArray()),
            sp(1563, "BanchoGolemon", PetStage.ULTIMATE, VI, 735, 228, 85, emptyArray()),


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
        JogressRecipe(515, 227, 579, FusionType.MODE_CHANGE, "Gallantmon assimilates the X-Antibody core to awaken Gallantmon X."),

        // Expansion Wave: Ghost Game, Seekers, Vital Bracelet, and Apex Form Fusions
        JogressRecipe(469, 476, 588, FusionType.DNA_JOGRESS, "Regulusmon assimilates the abyssal hellfire of SkullMeramon to awaken Arcturusmon."),
        JogressRecipe(322, 371, 475, FusionType.DNA_JOGRESS, "Kuwagamon and ExVeemon mutate into the mutant dragon-wasp chimera Dinobeemon."),
        JogressRecipe(576, 563, 589, FusionType.MODE_CHANGE, "ShineGreymon ignites blazing solar corona energy to awaken ShineGreymon Burst Mode."),
        JogressRecipe(577, 564, 590, FusionType.MODE_CHANGE, "MirageGaogamon reflects maximum full moon lunar energy into MirageGaogamon Burst Mode."),
        JogressRecipe(519, 473, 591, FusionType.MODE_CHANGE, "Beelzemon absorbs Baalmon's dark mantle to awaken Beelzemon (2010)."),
        JogressRecipe(510, 504, 592, FusionType.MODE_CHANGE, "Omnimon dons the seraphic wings of sorrow and hope into Omnimon Merciful Mode."),
        JogressRecipe(468, 501, 583, FusionType.DNA_JOGRESS, "Canoweissmon and WarGreymon combine white dragon blades into Siriusmon."),
        JogressRecipe(470, 404, 584, FusionType.DNA_JOGRESS, "Lamortemon and WereGarurumon channel fencing precision into Diarbbitmon."),
        JogressRecipe(471, 570, 585, FusionType.DNA_JOGRESS, "Thetismon and MarineAngemon synchronize deep sea virtue into Amphimon."),
        JogressRecipe(472, 460, 586, FusionType.DNA_JOGRESS, "Soloogarmon and Astamon ignite the demonic core into Fenriloogamon."),
        JogressRecipe(1501, 1502, 1523, FusionType.DNA_JOGRESS, "Holy sword and shield resonance awaken the sovereign Legend-Arms emperor."),

        // 500 Roster Expansion Canonical Fusions & X-Antibodies
        JogressRecipe(1507, 1508, 1529, FusionType.DNA_JOGRESS, "King and Queen of Olympos harmonize sovereign divinity into Huanglongmon."),
        JogressRecipe(1513, 1514, 549, FusionType.DNA_JOGRESS, "Ten Ancient Warriors flame and light spirits unite into God of Destruction Susanoomon."),
        JogressRecipe(1310, 1410, 1411, FusionType.MODE_CHANGE, "Human and Beast Spirits of Fire merge into Aldamon."),
        JogressRecipe(1311, 1412, 1524, FusionType.MODE_CHANGE, "Human and Beast Spirits of Light merge into MagnaGarurumon."),
        JogressRecipe(1411, 1513, 1523, FusionType.MODE_CHANGE, "Emperor dragon spirits unify into KaiserGreymon."),
        JogressRecipe(1315, 1413, 1414, FusionType.MODE_CHANGE, "Corrupted darkness purified into the true Thunder-Light spirit Raihimon."),
        JogressRecipe(1526, 1525, 1529, FusionType.DNA_JOGRESS, "Eastern Azure Dragon and Western White Tiger harmonize into Central Golden Dragon Huanglongmon."),
        JogressRecipe(1527, 1528, 1529, FusionType.DNA_JOGRESS, "Northern Black Tortoise and Southern Vermilion Bird harmonize into Central Golden Dragon Huanglongmon."),
        JogressRecipe(1533, 1534, 1537, FusionType.DNA_JOGRESS, "Abyssal vampire lords awaken the Super Demon Lord of Seven Sins Ogudomon."),
        JogressRecipe(1421, 1536, 1537, FusionType.DNA_JOGRESS, "Pride and Sloth converge into the Grand Super Demon Lord Ogudomon."),
        JogressRecipe(1501, 1504, 1511, FusionType.DNA_JOGRESS, "War and forge titan wrath fuse into One-Man Army Titamon."),
        JogressRecipe(1510, 1505, 565, FusionType.DNA_JOGRESS, "Love and harvest grace radiate cosmic energy into GraceNovamon."),
        JogressRecipe(1503, 1403, 1535, FusionType.DNA_JOGRESS, "Deep sea ocean king and golden puffer armor synthesize into Aegisdramon."),
        JogressRecipe(1417, 1418, 1535, FusionType.DNA_JOGRESS, "Light and dark sea dragon cybernetics unite into Aegisdramon."),
        JogressRecipe(538, 1423, 1538, FusionType.MODE_CHANGE, "Jesmon assimilates the Legend-Arms blade core into Jesmon X."),
        JogressRecipe(539, 1408, 1539, FusionType.MODE_CHANGE, "Gankoomon manifests Hinukamuy's apex wrath into Gankoomon X."),
        JogressRecipe(542, 1327, 1541, FusionType.MODE_CHANGE, "Craniummon reinforces Black Digizoid with golden armor into Craniummon X."),
        JogressRecipe(540, 1419, 1540, FusionType.MODE_CHANGE, "Dynasmon unleashes dragon aura into Dynasmon X."),
        JogressRecipe(543, 1433, 1542, FusionType.MODE_CHANGE, "Sleipmon arms six legs with glacial fortress armor into Sleipmon X."),
        JogressRecipe(544, 1317, 1543, FusionType.MODE_CHANGE, "Duftmon adopts dark leonine strategist form into Duftmon X."),

        // 550 Roster Expansion: Legend-Arms, Ghost Game, Seekers, Tri & Banchos
        JogressRecipe(594, 595, 596, FusionType.DNA_JOGRESS, "Durandamon holy sword and BryweLudramon holy shield achieve sovereign Legend-Arms fusion into RagnaLoardmon."),
        JogressRecipe(583, 588, 597, FusionType.DNA_JOGRESS, "Siriusmon white dragon light and Arcturusmon abyssal darkness harmonize into the God of Creation and Destruction Proximamon."),
        JogressRecipe(574, 598, 599, FusionType.DNA_JOGRESS, "Ophanimon Falldown Mode and Raguelmon merge their grief and corrupted code into the world-ending Ordinemon."),
        JogressRecipe(537, 598, 599, FusionType.DNA_JOGRESS, "Mastemon and Raguelmon harmonize dimensional rifts into Ordinemon."),
        JogressRecipe(586, 587, 1553, FusionType.DNA_JOGRESS, "Fenriloogamon and Kazuchimon channel Thunder God data into Fenriloogamon: Takemikazuchi."),
        JogressRecipe(483, 484, 1554, FusionType.DIGIXROS, "OmegaShoutmon and Shoutmon X5 ignite blazing digisoul into the Great Xros Shoutmon X7."),
        JogressRecipe(568, 1560, 567, FusionType.DNA_JOGRESS, "BanchoLeomon and BanchoStingmon unite rebellious honor into Chaosmon.")

    )
}
