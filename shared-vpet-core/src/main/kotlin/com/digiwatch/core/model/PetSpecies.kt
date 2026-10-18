package com.digiwatch.core.model

import kotlinx.serialization.Serializable

/**
 * Definition of a monster species including base stats and branching evolution paths.
 */
@Serializable
data class PetSpecies(
    val id: Int,
    val name: String,
    val stage: PetStage,
    val attribute: ElementalAttribute,
    val baseHp: Int,
    val basePower: Int,
    val baselineWeight: Int,
    val spriteKey: String,
    val evolutionRules: List<EvolutionRule> = emptyList()
) {
    companion object {
        // Complete catalog of authentic virtual pet species
        val SPECIES_REGISTRY: Map<Int, PetSpecies> by lazy {
            listOf(
                // ==========================================
                // Stage 1: Baby I
                // ==========================================
                PetSpecies(
                    id = 101,
                    name = "Botamon",
                    stage = PetStage.BABY_I,
                    attribute = ElementalAttribute.FREE,
                    baseHp = 50,
                    basePower = 10,
                    baselineWeight = 5,
                    spriteKey = "botamon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 102, minAgeSeconds = 3600L) // 1 hr -> Koromon
                    )
                ),
                PetSpecies(
                    id = 103,
                    name = "Punimon",
                    stage = PetStage.BABY_I,
                    attribute = ElementalAttribute.FREE,
                    baseHp = 50,
                    basePower = 10,
                    baselineWeight = 5,
                    spriteKey = "punimon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 118, minAgeSeconds = 3600L, minCareMistakes = 0, maxCareMistakes = 1, minEffort = 2), // Chocomon
                        EvolutionRule(targetSpeciesId = 104, minAgeSeconds = 3600L) // 1 hr -> Tsunomon
                    )
                ),
                PetSpecies(
                    id = 105,
                    name = "Poyomon",
                    stage = PetStage.BABY_I,
                    attribute = ElementalAttribute.FREE,
                    baseHp = 50,
                    basePower = 10,
                    baselineWeight = 5,
                    spriteKey = "poyomon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 117, minAgeSeconds = 3600L, minCareMistakes = 0, maxCareMistakes = 1, minEffort = 2), // Gummymon
                        EvolutionRule(targetSpeciesId = 106, minAgeSeconds = 3600L) // 1 hr -> Tokomon
                    )
                ),
                PetSpecies(
                    id = 107,
                    name = "Yuramon",
                    stage = PetStage.BABY_I,
                    attribute = ElementalAttribute.FREE,
                    baseHp = 50,
                    basePower = 10,
                    baselineWeight = 5,
                    spriteKey = "yuramon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 116, minAgeSeconds = 3600L, minCareMistakes = 0, maxCareMistakes = 1, minEffort = 2), // Viximon
                        EvolutionRule(targetSpeciesId = 108, minAgeSeconds = 3600L) // 1 hr -> Tanemon
                    )
                ),

                // ==========================================
                // Stage 2: Baby II
                // ==========================================
                PetSpecies(
                    id = 102,
                    name = "Koromon",
                    stage = PetStage.BABY_II,
                    attribute = ElementalAttribute.FREE,
                    baseHp = 80,
                    basePower = 20,
                    baselineWeight = 10,
                    spriteKey = "koromon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 201, minAgeSeconds = 43200L, minCareMistakes = 0, maxCareMistakes = 2), // Agumon
                        EvolutionRule(targetSpeciesId = 219, minAgeSeconds = 43200L, minCareMistakes = 3, maxCareMistakes = 4), // BlackAgumon
                        EvolutionRule(targetSpeciesId = 202, minAgeSeconds = 43200L, minCareMistakes = 5, maxCareMistakes = 999) // Betamon
                    )
                ),
                PetSpecies(
                    id = 104,
                    name = "Tsunomon",
                    stage = PetStage.BABY_II,
                    attribute = ElementalAttribute.FREE,
                    baseHp = 80,
                    basePower = 20,
                    baselineWeight = 10,
                    spriteKey = "tsunomon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 203, minAgeSeconds = 43200L, minCareMistakes = 0, maxCareMistakes = 2), // Gabumon
                        EvolutionRule(targetSpeciesId = 220, minAgeSeconds = 43200L, minCareMistakes = 3, maxCareMistakes = 4), // BlackGabumon
                        EvolutionRule(targetSpeciesId = 204, minAgeSeconds = 43200L, minCareMistakes = 5, maxCareMistakes = 999) // Elecmon
                    )
                ),
                PetSpecies(
                    id = 106,
                    name = "Tokomon",
                    stage = PetStage.BABY_II,
                    attribute = ElementalAttribute.FREE,
                    baseHp = 80,
                    basePower = 20,
                    baselineWeight = 10,
                    spriteKey = "tokomon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 221, minAgeSeconds = 43200L, minCareMistakes = 0, maxCareMistakes = 0, minEffort = 4), // Lucemon
                        EvolutionRule(targetSpeciesId = 205, minAgeSeconds = 43200L, minCareMistakes = 0, maxCareMistakes = 3), // Patamon
                        EvolutionRule(targetSpeciesId = 206, minAgeSeconds = 43200L, minCareMistakes = 4, maxCareMistakes = 999) // Biyomon
                    )
                ),
                PetSpecies(
                    id = 108,
                    name = "Tanemon",
                    stage = PetStage.BABY_II,
                    attribute = ElementalAttribute.FREE,
                    baseHp = 80,
                    basePower = 20,
                    baselineWeight = 10,
                    spriteKey = "tanemon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 207, minAgeSeconds = 43200L, minCareMistakes = 0, maxCareMistakes = 2), // Palmon
                        EvolutionRule(targetSpeciesId = 208, minAgeSeconds = 43200L, minCareMistakes = 3, maxCareMistakes = 4), // Tentomon
                        EvolutionRule(targetSpeciesId = 211, minAgeSeconds = 43200L, minCareMistakes = 5, maxCareMistakes = 999) // Kunemon
                    )
                ),

                // ==========================================
                // Stage 3: Child
                // ==========================================
                PetSpecies(
                    id = 201,
                    name = "Agumon",
                    stage = PetStage.CHILD,
                    attribute = ElementalAttribute.VACCINE,
                    baseHp = 130,
                    basePower = 35,
                    baselineWeight = 20,
                    spriteKey = "agumon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 301, minAgeSeconds = 86400L, minCareMistakes = 0, maxCareMistakes = 2, minEffort = 3, minWeight = 30), // Greymon
                        EvolutionRule(targetSpeciesId = 302, minAgeSeconds = 86400L, minCareMistakes = 0, maxCareMistakes = 3, minEffort = 2, minWeight = 25), // Tyrannomon
                        EvolutionRule(targetSpeciesId = 304, minAgeSeconds = 86400L, minCareMistakes = 0, maxCareMistakes = 4, minOverfeed = 3), // Meramon
                        EvolutionRule(targetSpeciesId = 303, minAgeSeconds = 86400L, minCareMistakes = 3, maxCareMistakes = 5), // Devimon
                        EvolutionRule(targetSpeciesId = 305, minAgeSeconds = 86400L, minCareMistakes = 6, maxCareMistakes = 999) // Numemon
                    )
                ),
                PetSpecies(
                    id = 202,
                    name = "Betamon",
                    stage = PetStage.CHILD,
                    attribute = ElementalAttribute.VIRUS,
                    baseHp = 120,
                    basePower = 32,
                    baselineWeight = 20,
                    spriteKey = "betamon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 314, minAgeSeconds = 86400L, minCareMistakes = 0, maxCareMistakes = 2, minEffort = 3), // Seadramon
                        EvolutionRule(targetSpeciesId = 327, minAgeSeconds = 86400L, minCareMistakes = 0, maxCareMistakes = 3, minWeight = 30), // Shellmon
                        EvolutionRule(targetSpeciesId = 324, minAgeSeconds = 86400L, minCareMistakes = 0, maxCareMistakes = 4, minEffort = 1), // Coelamon
                        EvolutionRule(targetSpeciesId = 303, minAgeSeconds = 86400L, minCareMistakes = 3, maxCareMistakes = 5), // Devimon
                        EvolutionRule(targetSpeciesId = 305, minAgeSeconds = 86400L, minCareMistakes = 6, maxCareMistakes = 999) // Numemon
                    )
                ),
                PetSpecies(
                    id = 203,
                    name = "Gabumon",
                    stage = PetStage.CHILD,
                    attribute = ElementalAttribute.DATA,
                    baseHp = 135,
                    basePower = 36,
                    baselineWeight = 20,
                    spriteKey = "gabumon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 306, minAgeSeconds = 86400L, minCareMistakes = 0, maxCareMistakes = 2, minEffort = 3), // Garurumon
                        EvolutionRule(targetSpeciesId = 329, minAgeSeconds = 86400L, minCareMistakes = 0, maxCareMistakes = 3, minOverfeed = 2), // Frigimon
                        EvolutionRule(targetSpeciesId = 312, minAgeSeconds = 86400L, minCareMistakes = 0, maxCareMistakes = 5), // Ogremon
                        EvolutionRule(targetSpeciesId = 305, minAgeSeconds = 86400L, minCareMistakes = 6, maxCareMistakes = 999) // Numemon
                    )
                ),
                PetSpecies(
                    id = 204,
                    name = "Elecmon",
                    stage = PetStage.CHILD,
                    attribute = ElementalAttribute.DATA,
                    baseHp = 125,
                    basePower = 33,
                    baselineWeight = 20,
                    spriteKey = "elecmon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 313, minAgeSeconds = 86400L, minCareMistakes = 0, maxCareMistakes = 2, minEffort = 3), // Leomon
                        EvolutionRule(targetSpeciesId = 312, minAgeSeconds = 86400L, minCareMistakes = 0, maxCareMistakes = 5), // Ogremon
                        EvolutionRule(targetSpeciesId = 315, minAgeSeconds = 86400L, minCareMistakes = 6, maxCareMistakes = 999) // Sukamon
                    )
                ),
                PetSpecies(
                    id = 205,
                    name = "Patamon",
                    stage = PetStage.CHILD,
                    attribute = ElementalAttribute.DATA,
                    baseHp = 120,
                    basePower = 30,
                    baselineWeight = 20,
                    spriteKey = "patamon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 307, minAgeSeconds = 86400L, minCareMistakes = 0, maxCareMistakes = 2, minEffort = 3), // Angemon
                        EvolutionRule(targetSpeciesId = 328, minAgeSeconds = 86400L, minCareMistakes = 0, maxCareMistakes = 3, minEffort = 1), // Unimon
                        EvolutionRule(targetSpeciesId = 303, minAgeSeconds = 86400L, minCareMistakes = 3, maxCareMistakes = 5), // Devimon
                        EvolutionRule(targetSpeciesId = 315, minAgeSeconds = 86400L, minCareMistakes = 6, maxCareMistakes = 999) // Sukamon
                    )
                ),
                PetSpecies(
                    id = 206,
                    name = "Biyomon",
                    stage = PetStage.CHILD,
                    attribute = ElementalAttribute.VACCINE,
                    baseHp = 125,
                    basePower = 32,
                    baselineWeight = 20,
                    spriteKey = "biyomon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 309, minAgeSeconds = 86400L, minCareMistakes = 0, maxCareMistakes = 2, minEffort = 3), // Birdramon
                        EvolutionRule(targetSpeciesId = 323, minAgeSeconds = 86400L, minCareMistakes = 0, maxCareMistakes = 5), // Kokatorimon
                        EvolutionRule(targetSpeciesId = 305, minAgeSeconds = 86400L, minCareMistakes = 6, maxCareMistakes = 999) // Numemon
                    )
                ),
                PetSpecies(
                    id = 207,
                    name = "Palmon",
                    stage = PetStage.CHILD,
                    attribute = ElementalAttribute.DATA,
                    baseHp = 120,
                    basePower = 30,
                    baselineWeight = 20,
                    spriteKey = "palmon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 308, minAgeSeconds = 86400L, minCareMistakes = 0, maxCareMistakes = 3, minEffort = 2), // Togemon
                        EvolutionRule(targetSpeciesId = 330, minAgeSeconds = 86400L, minCareMistakes = 0, maxCareMistakes = 5), // Vegiemon
                        EvolutionRule(targetSpeciesId = 305, minAgeSeconds = 86400L, minCareMistakes = 6, maxCareMistakes = 999) // Numemon
                    )
                ),
                PetSpecies(
                    id = 208,
                    name = "Tentomon",
                    stage = PetStage.CHILD,
                    attribute = ElementalAttribute.VACCINE,
                    baseHp = 130,
                    basePower = 34,
                    baselineWeight = 20,
                    spriteKey = "tentomon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 310, minAgeSeconds = 86400L, minCareMistakes = 0, maxCareMistakes = 3, minEffort = 2), // Kabuterimon
                        EvolutionRule(targetSpeciesId = 322, minAgeSeconds = 86400L, minCareMistakes = 0, maxCareMistakes = 5), // Kuwagamon
                        EvolutionRule(targetSpeciesId = 315, minAgeSeconds = 86400L, minCareMistakes = 6, maxCareMistakes = 999) // Sukamon
                    )
                ),

                // ==========================================
                // Stage 4: Adult
                // ==========================================
                PetSpecies(
                    id = 301,
                    name = "Greymon",
                    stage = PetStage.ADULT,
                    attribute = ElementalAttribute.VACCINE,
                    baseHp = 240,
                    basePower = 65,
                    baselineWeight = 30,
                    spriteKey = "greymon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 401, minAgeSeconds = 172800L, minBattles = 15, minWinRatio = 0.70f, minCareMistakes = 0, maxCareMistakes = 3), // MetalGreymon
                        EvolutionRule(targetSpeciesId = 403, minAgeSeconds = 172800L, minBattles = 15, minWinRatio = 0.70f, minCareMistakes = 4, maxCareMistakes = 999) // SkullGreymon
                    )
                ),
                PetSpecies(
                    id = 302,
                    name = "Tyrannomon",
                    stage = PetStage.ADULT,
                    attribute = ElementalAttribute.DATA,
                    baseHp = 220,
                    basePower = 60,
                    baselineWeight = 30,
                    spriteKey = "tyrannomon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 401, minAgeSeconds = 172800L, minBattles = 15, minWinRatio = 0.75f),
                        EvolutionRule(targetSpeciesId = 412, minAgeSeconds = 172800L, minBattles = 10, minWinRatio = 0.60f)
                    )
                ),
                PetSpecies(
                    id = 303,
                    name = "Devimon",
                    stage = PetStage.ADULT,
                    attribute = ElementalAttribute.VIRUS,
                    baseHp = 230,
                    basePower = 68,
                    baselineWeight = 30,
                    spriteKey = "devimon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 410, minAgeSeconds = 172800L, minBattles = 20, minWinRatio = 0.75f, minCareMistakes = 0, maxCareMistakes = 2), // Myotismon
                        EvolutionRule(targetSpeciesId = 428, minAgeSeconds = 172800L, minBattles = 15, minWinRatio = 0.70f, minCareMistakes = 0, maxCareMistakes = 4, minEffort = 3), // LadyDevimon
                        EvolutionRule(targetSpeciesId = 403, minAgeSeconds = 172800L, minBattles = 12, minWinRatio = 0.65f) // SkullGreymon
                    )
                ),
                PetSpecies(
                    id = 304,
                    name = "Meramon",
                    stage = PetStage.ADULT,
                    attribute = ElementalAttribute.DATA,
                    baseHp = 210,
                    basePower = 62,
                    baselineWeight = 28,
                    spriteKey = "meramon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 401, minAgeSeconds = 172800L, minBattles = 15, minWinRatio = 0.75f),
                        EvolutionRule(targetSpeciesId = 413, minAgeSeconds = 172800L, minBattles = 10, minWinRatio = 0.60f)
                    )
                ),
                PetSpecies(
                    id = 305,
                    name = "Numemon",
                    stage = PetStage.ADULT,
                    attribute = ElementalAttribute.VIRUS,
                    baseHp = 170,
                    basePower = 40,
                    baselineWeight = 25,
                    spriteKey = "numemon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 402, minAgeSeconds = 172800L, minBattles = 15, minWinRatio = 0.70f, minEffort = 4) // Monzaemon
                    )
                ),
                PetSpecies(
                    id = 306,
                    name = "Garurumon",
                    stage = PetStage.ADULT,
                    attribute = ElementalAttribute.DATA,
                    baseHp = 235,
                    basePower = 64,
                    baselineWeight = 30,
                    spriteKey = "garurumon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 404, minAgeSeconds = 172800L, minBattles = 15, minWinRatio = 0.70f, minCareMistakes = 0, maxCareMistakes = 3) // WereGarurumon
                    )
                ),
                PetSpecies(
                    id = 307,
                    name = "Angemon",
                    stage = PetStage.ADULT,
                    attribute = ElementalAttribute.VACCINE,
                    baseHp = 230,
                    basePower = 66,
                    baselineWeight = 28,
                    spriteKey = "angemon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 405, minAgeSeconds = 172800L, minBattles = 15, minWinRatio = 0.75f, minEffort = 4) // MagnaAngemon
                    )
                ),
                PetSpecies(
                    id = 308,
                    name = "Togemon",
                    stage = PetStage.ADULT,
                    attribute = ElementalAttribute.DATA,
                    baseHp = 225,
                    basePower = 61,
                    baselineWeight = 30,
                    spriteKey = "togemon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 406, minAgeSeconds = 172800L, minBattles = 15, minWinRatio = 0.70f) // Lillymon
                    )
                ),
                PetSpecies(
                    id = 309,
                    name = "Birdramon",
                    stage = PetStage.ADULT,
                    attribute = ElementalAttribute.VACCINE,
                    baseHp = 220,
                    basePower = 63,
                    baselineWeight = 28,
                    spriteKey = "birdramon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 407, minAgeSeconds = 172800L, minBattles = 15, minWinRatio = 0.70f) // Garudamon
                    )
                ),
                PetSpecies(
                    id = 310,
                    name = "Kabuterimon",
                    stage = PetStage.ADULT,
                    attribute = ElementalAttribute.VACCINE,
                    baseHp = 240,
                    basePower = 65,
                    baselineWeight = 32,
                    spriteKey = "kabuterimon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 408, minAgeSeconds = 172800L, minBattles = 15, minWinRatio = 0.70f, minCareMistakes = 0, maxCareMistakes = 2), // MegaKabuterimon
                        EvolutionRule(targetSpeciesId = 418, minAgeSeconds = 172800L, minBattles = 15, minWinRatio = 0.65f, minCareMistakes = 3, maxCareMistakes = 999) // Andromon
                    )
                ),
                PetSpecies(
                    id = 312,
                    name = "Ogremon",
                    stage = PetStage.ADULT,
                    attribute = ElementalAttribute.VIRUS,
                    baseHp = 235,
                    basePower = 67,
                    baselineWeight = 32,
                    spriteKey = "ogremon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 403, minAgeSeconds = 172800L, minBattles = 15, minWinRatio = 0.70f),
                        EvolutionRule(targetSpeciesId = 411, minAgeSeconds = 172800L, minBattles = 12, minWinRatio = 0.65f)
                    )
                ),
                PetSpecies(
                    id = 313,
                    name = "Leomon",
                    stage = PetStage.ADULT,
                    attribute = ElementalAttribute.VACCINE,
                    baseHp = 245,
                    basePower = 68,
                    baselineWeight = 30,
                    spriteKey = "leomon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 404, minAgeSeconds = 172800L, minBattles = 15, minWinRatio = 0.75f, minCareMistakes = 0, maxCareMistakes = 2), // WereGarurumon
                        EvolutionRule(targetSpeciesId = 419, minAgeSeconds = 172800L, minBattles = 12, minWinRatio = 0.65f, minCareMistakes = 3, maxCareMistakes = 999) // Giromon
                    )
                ),
                PetSpecies(
                    id = 314,
                    name = "Seadramon",
                    stage = PetStage.ADULT,
                    attribute = ElementalAttribute.DATA,
                    baseHp = 230,
                    basePower = 64,
                    baselineWeight = 30,
                    spriteKey = "seadramon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 409, minAgeSeconds = 172800L, minBattles = 15, minWinRatio = 0.70f) // MegaSeadramon
                    )
                ),
                PetSpecies(
                    id = 315,
                    name = "Sukamon",
                    stage = PetStage.ADULT,
                    attribute = ElementalAttribute.VIRUS,
                    baseHp = 165,
                    basePower = 38,
                    baselineWeight = 25,
                    spriteKey = "sukamon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 411, minAgeSeconds = 172800L, minBattles = 15, minWinRatio = 0.70f, minEffort = 4) // Etemon
                    )
                ),

                // ==========================================
                // Stage 5: Perfect
                // ==========================================
                PetSpecies(
                    id = 401,
                    name = "MetalGreymon",
                    stage = PetStage.PERFECT,
                    attribute = ElementalAttribute.VACCINE,
                    baseHp = 390,
                    basePower = 110,
                    baselineWeight = 40,
                    spriteKey = "metalgreymon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 501, minAgeSeconds = 259200L, minBattles = 30, minWinRatio = 0.80f, minEffort = 4) // WarGreymon
                    )
                ),
                PetSpecies(
                    id = 402,
                    name = "Monzaemon",
                    stage = PetStage.PERFECT,
                    attribute = ElementalAttribute.VACCINE,
                    baseHp = 370,
                    basePower = 100,
                    baselineWeight = 40,
                    spriteKey = "monzaemon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 501, minAgeSeconds = 259200L, minBattles = 30, minWinRatio = 0.80f)
                    )
                ),
                PetSpecies(
                    id = 403,
                    name = "SkullGreymon",
                    stage = PetStage.PERFECT,
                    attribute = ElementalAttribute.VIRUS,
                    baseHp = 420,
                    basePower = 125,
                    baselineWeight = 45,
                    spriteKey = "skullgreymon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 502, minAgeSeconds = 259200L, minBattles = 35, minWinRatio = 0.80f) // Machinedramon
                    )
                ),
                PetSpecies(
                    id = 404,
                    name = "WereGarurumon",
                    stage = PetStage.PERFECT,
                    attribute = ElementalAttribute.DATA,
                    baseHp = 385,
                    basePower = 112,
                    baselineWeight = 42,
                    spriteKey = "weregarurumon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 503, minAgeSeconds = 259200L, minBattles = 30, minWinRatio = 0.80f, minEffort = 4) // MetalGarurumon
                    )
                ),
                PetSpecies(
                    id = 405,
                    name = "MagnaAngemon",
                    stage = PetStage.PERFECT,
                    attribute = ElementalAttribute.VACCINE,
                    baseHp = 400,
                    basePower = 118,
                    baselineWeight = 38,
                    spriteKey = "magnaangemon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 504, minAgeSeconds = 259200L, minBattles = 30, minWinRatio = 0.80f, minCareMistakes = 0, maxCareMistakes = 2), // Seraphimon
                        EvolutionRule(targetSpeciesId = 543, minAgeSeconds = 259200L, minBattles = 25, minWinRatio = 0.75f, minCareMistakes = 3, maxCareMistakes = 999) // Sleipmon
                    )
                ),
                PetSpecies(
                    id = 406,
                    name = "Lillymon",
                    stage = PetStage.PERFECT,
                    attribute = ElementalAttribute.DATA,
                    baseHp = 360,
                    basePower = 102,
                    baselineWeight = 35,
                    spriteKey = "lillymon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 505, minAgeSeconds = 259200L, minBattles = 30, minWinRatio = 0.80f) // Rosemon
                    )
                ),
                PetSpecies(
                    id = 407,
                    name = "Garudamon",
                    stage = PetStage.PERFECT,
                    attribute = ElementalAttribute.VACCINE,
                    baseHp = 380,
                    basePower = 108,
                    baselineWeight = 40,
                    spriteKey = "garudamon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 506, minAgeSeconds = 259200L, minBattles = 30, minWinRatio = 0.80f) // Phoenixmon
                    )
                ),
                PetSpecies(
                    id = 408,
                    name = "MegaKabuterimon",
                    stage = PetStage.PERFECT,
                    attribute = ElementalAttribute.VACCINE,
                    baseHp = 410,
                    basePower = 115,
                    baselineWeight = 46,
                    spriteKey = "megakabuterimon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 507, minAgeSeconds = 259200L, minBattles = 30, minWinRatio = 0.80f) // HerculesKabuterimon
                    )
                ),
                PetSpecies(
                    id = 409,
                    name = "MegaSeadramon",
                    stage = PetStage.PERFECT,
                    attribute = ElementalAttribute.DATA,
                    baseHp = 395,
                    basePower = 110,
                    baselineWeight = 42,
                    spriteKey = "megaseadramon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 508, minAgeSeconds = 259200L, minBattles = 30, minWinRatio = 0.80f, minCareMistakes = 0, maxCareMistakes = 2), // Plesiomon
                        EvolutionRule(targetSpeciesId = 533, minAgeSeconds = 259200L, minBattles = 30, minWinRatio = 0.75f, minCareMistakes = 3, maxCareMistakes = 999) // Leviamon
                    )
                ),
                PetSpecies(
                    id = 410,
                    name = "Myotismon",
                    stage = PetStage.PERFECT,
                    attribute = ElementalAttribute.VIRUS,
                    baseHp = 415,
                    basePower = 120,
                    baselineWeight = 44,
                    spriteKey = "myotismon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 519, minAgeSeconds = 259200L, minBattles = 35, minWinRatio = 0.85f, minEffort = 4, minCareMistakes = 0, maxCareMistakes = 2), // Beelzemon
                        EvolutionRule(targetSpeciesId = 509, minAgeSeconds = 259200L, minBattles = 30, minWinRatio = 0.75f, minCareMistakes = 0, maxCareMistakes = 4), // Piedmon
                        EvolutionRule(targetSpeciesId = 534, minAgeSeconds = 259200L, minBattles = 25, minWinRatio = 0.70f, minCareMistakes = 5, maxCareMistakes = 999) // Barbamon
                    )
                ),
                PetSpecies(
                    id = 411,
                    name = "Etemon",
                    stage = PetStage.PERFECT,
                    attribute = ElementalAttribute.VIRUS,
                    baseHp = 380,
                    basePower = 105,
                    baselineWeight = 40,
                    spriteKey = "etemon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 511, minAgeSeconds = 259200L, minBattles = 30, minWinRatio = 0.80f) // MetalEtemon
                    )
                ),
                PetSpecies(
                    id = 412,
                    name = "Mamemon",
                    stage = PetStage.PERFECT,
                    attribute = ElementalAttribute.DATA,
                    baseHp = 350,
                    basePower = 108,
                    baselineWeight = 30,
                    spriteKey = "mamemon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 502, minAgeSeconds = 259200L, minBattles = 25, minWinRatio = 0.75f)
                    )
                ),
                PetSpecies(
                    id = 413,
                    name = "MetalMamemon",
                    stage = PetStage.PERFECT,
                    attribute = ElementalAttribute.DATA,
                    baseHp = 365,
                    basePower = 114,
                    baselineWeight = 32,
                    spriteKey = "metalmamemon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 502, minAgeSeconds = 259200L, minBattles = 25, minWinRatio = 0.75f)
                    )
                ),

                // ==========================================
                // Stage 6: Ultimate
                // ==========================================
                PetSpecies(
                    id = 501,
                    name = "WarGreymon",
                    stage = PetStage.ULTIMATE,
                    attribute = ElementalAttribute.VACCINE,
                    baseHp = 560,
                    basePower = 165,
                    baselineWeight = 50,
                    spriteKey = "wargreymon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 510, minBattles = 40, minWinRatio = 0.85f, minEffort = 4) // Omnimon
                    )
                ),
                PetSpecies(
                    id = 502,
                    name = "Machinedramon",
                    stage = PetStage.ULTIMATE,
                    attribute = ElementalAttribute.VIRUS,
                    baseHp = 590,
                    basePower = 175,
                    baselineWeight = 55,
                    spriteKey = "machinedramon",
                    evolutionRules = emptyList()
                ),
                PetSpecies(
                    id = 503,
                    name = "MetalGarurumon",
                    stage = PetStage.ULTIMATE,
                    attribute = ElementalAttribute.DATA,
                    baseHp = 550,
                    basePower = 162,
                    baselineWeight = 48,
                    spriteKey = "metalgarurumon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 535, minBattles = 40, minWinRatio = 0.85f, minEffort = 4) // Omnimon Alter-S
                    )
                ),
                PetSpecies(
                    id = 504,
                    name = "Seraphimon",
                    stage = PetStage.ULTIMATE,
                    attribute = ElementalAttribute.VACCINE,
                    baseHp = 570,
                    basePower = 168,
                    baselineWeight = 46,
                    spriteKey = "seraphimon",
                    evolutionRules = emptyList()
                ),
                PetSpecies(
                    id = 505,
                    name = "Rosemon",
                    stage = PetStage.ULTIMATE,
                    attribute = ElementalAttribute.DATA,
                    baseHp = 530,
                    basePower = 158,
                    baselineWeight = 44,
                    spriteKey = "rosemon",
                    evolutionRules = emptyList()
                ),
                PetSpecies(
                    id = 506,
                    name = "Phoenixmon",
                    stage = PetStage.ULTIMATE,
                    attribute = ElementalAttribute.VACCINE,
                    baseHp = 545,
                    basePower = 160,
                    baselineWeight = 45,
                    spriteKey = "phoenixmon",
                    evolutionRules = emptyList()
                ),
                PetSpecies(
                    id = 507,
                    name = "HerculesKabuterimon",
                    stage = PetStage.ULTIMATE,
                    attribute = ElementalAttribute.VACCINE,
                    baseHp = 580,
                    basePower = 170,
                    baselineWeight = 52,
                    spriteKey = "herculeskabuterimon",
                    evolutionRules = emptyList()
                ),
                PetSpecies(
                    id = 508,
                    name = "Plesiomon",
                    stage = PetStage.ULTIMATE,
                    attribute = ElementalAttribute.DATA,
                    baseHp = 555,
                    basePower = 164,
                    baselineWeight = 50,
                    spriteKey = "plesiomon",
                    evolutionRules = emptyList()
                ),
                PetSpecies(
                    id = 509,
                    name = "Piedmon",
                    stage = PetStage.ULTIMATE,
                    attribute = ElementalAttribute.VIRUS,
                    baseHp = 575,
                    basePower = 172,
                    baselineWeight = 48,
                    spriteKey = "piedmon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 529, minBattles = 40, minWinRatio = 0.85f) // Apocalymon
                    )
                ),
                PetSpecies(
                    id = 510,
                    name = "Omnimon",
                    stage = PetStage.ULTIMATE,
                    attribute = ElementalAttribute.VACCINE,
                    baseHp = 620,
                    basePower = 185,
                    baselineWeight = 55,
                    spriteKey = "omnimon",
                    evolutionRules = emptyList()
                ),
                PetSpecies(
                    id = 511,
                    name = "MetalEtemon",
                    stage = PetStage.ULTIMATE,
                    attribute = ElementalAttribute.VIRUS,
                    baseHp = 560,
                    basePower = 160,
                    baselineWeight = 50,
                    spriteKey = "metaletemon",
                    evolutionRules = emptyList()
                ),
                PetSpecies(
                    id = 109,
                    name = "Zurumon",
                    stage = PetStage.BABY_I,
                    attribute = ElementalAttribute.VIRUS,
                    baseHp = 50,
                    basePower = 10,
                    baselineWeight = 5,
                    spriteKey = "zurumon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 119, minAgeSeconds = 3600L, minCareMistakes = 0, maxCareMistakes = 1, minEffort = 2), // Yaamon
                        EvolutionRule(targetSpeciesId = 110, minAgeSeconds = 3600L) // Pagumon
                    )
                ),
                PetSpecies(
                    id = 111,
                    name = "Mokumon",
                    stage = PetStage.BABY_I,
                    attribute = ElementalAttribute.FREE,
                    baseHp = 50,
                    basePower = 10,
                    baselineWeight = 5,
                    spriteKey = "mokumon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 115, minAgeSeconds = 3600L, minCareMistakes = 0, maxCareMistakes = 1, minEffort = 2), // Gigimon
                        EvolutionRule(targetSpeciesId = 112, minAgeSeconds = 3600L) // DemiMeramon
                    )
                ),
                PetSpecies(
                    id = 113,
                    name = "Kuramon",
                    stage = PetStage.BABY_I,
                    attribute = ElementalAttribute.FREE,
                    baseHp = 50,
                    basePower = 10,
                    baselineWeight = 5,
                    spriteKey = "kuramon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 114, minAgeSeconds = 3600L)
                    )
                ),
                PetSpecies(
                    id = 110,
                    name = "Pagumon",
                    stage = PetStage.BABY_II,
                    attribute = ElementalAttribute.VIRUS,
                    baseHp = 80,
                    basePower = 20,
                    baselineWeight = 10,
                    spriteKey = "pagumon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 209, minAgeSeconds = 43200L, minCareMistakes = 0, maxCareMistakes = 3),
                        EvolutionRule(targetSpeciesId = 210, minAgeSeconds = 43200L, minCareMistakes = 4, maxCareMistakes = 999)
                    )
                ),
                PetSpecies(
                    id = 112,
                    name = "DemiMeramon",
                    stage = PetStage.BABY_II,
                    attribute = ElementalAttribute.FREE,
                    baseHp = 80,
                    basePower = 20,
                    baselineWeight = 10,
                    spriteKey = "demimeramon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 214, minAgeSeconds = 43200L, minCareMistakes = 0, maxCareMistakes = 3),
                        EvolutionRule(targetSpeciesId = 218, minAgeSeconds = 43200L, minCareMistakes = 4, maxCareMistakes = 999)
                    )
                ),
                PetSpecies(
                    id = 114,
                    name = "DemiVeemon",
                    stage = PetStage.BABY_II,
                    attribute = ElementalAttribute.FREE,
                    baseHp = 80,
                    basePower = 20,
                    baselineWeight = 10,
                    spriteKey = "demiveemon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 222, minAgeSeconds = 43200L, minCareMistakes = 0, maxCareMistakes = 1, minEffort = 4), // Hackmon
                        EvolutionRule(targetSpeciesId = 212, minAgeSeconds = 43200L, minCareMistakes = 0, maxCareMistakes = 3), // Veemon
                        EvolutionRule(targetSpeciesId = 213, minAgeSeconds = 43200L, minCareMistakes = 4, maxCareMistakes = 999) // Wormmon
                    )
                ),
                PetSpecies(
                    id = 115,
                    name = "Gigimon",
                    stage = PetStage.BABY_II,
                    attribute = ElementalAttribute.FREE,
                    baseHp = 80,
                    basePower = 20,
                    baselineWeight = 10,
                    spriteKey = "gigimon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 214, minAgeSeconds = 43200L, minCareMistakes = 0, maxCareMistakes = 3), // Guilmon
                        EvolutionRule(targetSpeciesId = 219, minAgeSeconds = 43200L, minCareMistakes = 4, maxCareMistakes = 999) // BlackAgumon
                    )
                ),
                PetSpecies(
                    id = 116,
                    name = "Viximon",
                    stage = PetStage.BABY_II,
                    attribute = ElementalAttribute.FREE,
                    baseHp = 80,
                    basePower = 20,
                    baselineWeight = 10,
                    spriteKey = "viximon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 215, minAgeSeconds = 43200L, minCareMistakes = 0, maxCareMistakes = 3), // Renamon
                        EvolutionRule(targetSpeciesId = 206, minAgeSeconds = 43200L, minCareMistakes = 4, maxCareMistakes = 999) // Biyomon
                    )
                ),
                PetSpecies(
                    id = 117,
                    name = "Gummymon",
                    stage = PetStage.BABY_II,
                    attribute = ElementalAttribute.FREE,
                    baseHp = 80,
                    basePower = 20,
                    baselineWeight = 10,
                    spriteKey = "gummymon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 216, minAgeSeconds = 43200L, minCareMistakes = 0, maxCareMistakes = 3), // Terriermon
                        EvolutionRule(targetSpeciesId = 222, minAgeSeconds = 43200L, minCareMistakes = 4, maxCareMistakes = 999) // Hackmon
                    )
                ),
                PetSpecies(
                    id = 118,
                    name = "Chocomon",
                    stage = PetStage.BABY_II,
                    attribute = ElementalAttribute.FREE,
                    baseHp = 80,
                    basePower = 20,
                    baselineWeight = 10,
                    spriteKey = "chocomon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 217, minAgeSeconds = 43200L, minCareMistakes = 0, maxCareMistakes = 3), // Lopmon
                        EvolutionRule(targetSpeciesId = 220, minAgeSeconds = 43200L, minCareMistakes = 4, maxCareMistakes = 999) // BlackGabumon
                    )
                ),
                PetSpecies(
                    id = 119,
                    name = "Yaamon",
                    stage = PetStage.BABY_II,
                    attribute = ElementalAttribute.FREE,
                    baseHp = 80,
                    basePower = 20,
                    baselineWeight = 10,
                    spriteKey = "yaamon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 218, minAgeSeconds = 43200L, minCareMistakes = 0, maxCareMistakes = 3), // Impmon
                        EvolutionRule(targetSpeciesId = 211, minAgeSeconds = 43200L, minCareMistakes = 4, maxCareMistakes = 999) // Kunemon
                    )
                ),
                PetSpecies(
                    id = 209,
                    name = "Gazimon",
                    stage = PetStage.CHILD,
                    attribute = ElementalAttribute.VIRUS,
                    baseHp = 220,
                    basePower = 46,
                    baselineWeight = 20,
                    spriteKey = "gazimon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 319, minAgeSeconds = 86400L, minCareMistakes = 0, maxCareMistakes = 2, minEffort = 3), // Tuskmon
                        EvolutionRule(targetSpeciesId = 316, minAgeSeconds = 86400L, minCareMistakes = 0, maxCareMistakes = 3), // DarkTyrannomon
                        EvolutionRule(targetSpeciesId = 317, minAgeSeconds = 86400L, minCareMistakes = 4, maxCareMistakes = 999) // Devidramon
                    )
                ),
                PetSpecies(
                    id = 210,
                    name = "Gizamon",
                    stage = PetStage.CHILD,
                    attribute = ElementalAttribute.VIRUS,
                    baseHp = 215,
                    basePower = 45,
                    baselineWeight = 20,
                    spriteKey = "gizamon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 318, minAgeSeconds = 86400L, minCareMistakes = 0, maxCareMistakes = 3),
                        EvolutionRule(targetSpeciesId = 321, minAgeSeconds = 86400L, minCareMistakes = 4, maxCareMistakes = 999)
                    )
                ),
                PetSpecies(
                    id = 211,
                    name = "Kunemon",
                    stage = PetStage.CHILD,
                    attribute = ElementalAttribute.VIRUS,
                    baseHp = 205,
                    basePower = 42,
                    baselineWeight = 20,
                    spriteKey = "kunemon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 322, minAgeSeconds = 86400L, minCareMistakes = 0, maxCareMistakes = 3),
                        EvolutionRule(targetSpeciesId = 320, minAgeSeconds = 86400L, minCareMistakes = 4, maxCareMistakes = 999)
                    )
                ),
                PetSpecies(
                    id = 212,
                    name = "Veemon",
                    stage = PetStage.CHILD,
                    attribute = ElementalAttribute.FREE,
                    baseHp = 230,
                    basePower = 52,
                    baselineWeight = 20,
                    spriteKey = "veemon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 331, minAgeSeconds = 86400L, minCareMistakes = 0, maxCareMistakes = 3, minBattles = 10, minWinRatio = 0.6f),
                        EvolutionRule(targetSpeciesId = 332, minAgeSeconds = 86400L, minCareMistakes = 4, maxCareMistakes = 999)
                    )
                ),
                PetSpecies(
                    id = 213,
                    name = "Wormmon",
                    stage = PetStage.CHILD,
                    attribute = ElementalAttribute.FREE,
                    baseHp = 210,
                    basePower = 44,
                    baselineWeight = 20,
                    spriteKey = "wormmon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 333, minAgeSeconds = 86400L, minCareMistakes = 0, maxCareMistakes = 999)
                    )
                ),
                PetSpecies(
                    id = 214,
                    name = "Guilmon",
                    stage = PetStage.CHILD,
                    attribute = ElementalAttribute.VIRUS,
                    baseHp = 240,
                    basePower = 55,
                    baselineWeight = 25,
                    spriteKey = "guilmon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 334, minAgeSeconds = 86400L, minCareMistakes = 0, maxCareMistakes = 3, minBattles = 10, minWinRatio = 0.6f),
                        EvolutionRule(targetSpeciesId = 302, minAgeSeconds = 86400L, minCareMistakes = 4, maxCareMistakes = 999)
                    )
                ),
                PetSpecies(
                    id = 215,
                    name = "Renamon",
                    stage = PetStage.CHILD,
                    attribute = ElementalAttribute.DATA,
                    baseHp = 225,
                    basePower = 50,
                    baselineWeight = 20,
                    spriteKey = "renamon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 335, minAgeSeconds = 86400L, minCareMistakes = 0, maxCareMistakes = 999)
                    )
                ),
                PetSpecies(
                    id = 216,
                    name = "Terriermon",
                    stage = PetStage.CHILD,
                    attribute = ElementalAttribute.VACCINE,
                    baseHp = 225,
                    basePower = 48,
                    baselineWeight = 20,
                    spriteKey = "terriermon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 336, minAgeSeconds = 86400L, minCareMistakes = 0, maxCareMistakes = 999)
                    )
                ),
                PetSpecies(
                    id = 217,
                    name = "Lopmon",
                    stage = PetStage.CHILD,
                    attribute = ElementalAttribute.DATA,
                    baseHp = 220,
                    basePower = 47,
                    baselineWeight = 20,
                    spriteKey = "lopmon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 337, minAgeSeconds = 86400L, minCareMistakes = 0, maxCareMistakes = 999)
                    )
                ),
                PetSpecies(
                    id = 218,
                    name = "Impmon",
                    stage = PetStage.CHILD,
                    attribute = ElementalAttribute.VIRUS,
                    baseHp = 220,
                    basePower = 50,
                    baselineWeight = 20,
                    spriteKey = "impmon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 303, minAgeSeconds = 86400L, minCareMistakes = 0, maxCareMistakes = 3),
                        EvolutionRule(targetSpeciesId = 326, minAgeSeconds = 86400L, minCareMistakes = 4, maxCareMistakes = 999)
                    )
                ),
                PetSpecies(
                    id = 219,
                    name = "BlackAgumon",
                    stage = PetStage.CHILD,
                    attribute = ElementalAttribute.VIRUS,
                    baseHp = 230,
                    basePower = 50,
                    baselineWeight = 20,
                    spriteKey = "blackagumon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 339, minAgeSeconds = 86400L, minCareMistakes = 0, maxCareMistakes = 3),
                        EvolutionRule(targetSpeciesId = 316, minAgeSeconds = 86400L, minCareMistakes = 4, maxCareMistakes = 999)
                    )
                ),
                PetSpecies(
                    id = 220,
                    name = "BlackGabumon",
                    stage = PetStage.CHILD,
                    attribute = ElementalAttribute.VIRUS,
                    baseHp = 230,
                    basePower = 50,
                    baselineWeight = 20,
                    spriteKey = "blackgabumon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 340, minAgeSeconds = 86400L, minCareMistakes = 0, maxCareMistakes = 999)
                    )
                ),
                PetSpecies(
                    id = 221,
                    name = "Lucemon",
                    stage = PetStage.CHILD,
                    attribute = ElementalAttribute.VACCINE,
                    baseHp = 280,
                    basePower = 70,
                    baselineWeight = 20,
                    spriteKey = "lucemon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 434, minAgeSeconds = 86400L, minBattles = 20, minWinRatio = 0.8f)
                    )
                ),
                PetSpecies(
                    id = 222,
                    name = "Hackmon",
                    stage = PetStage.CHILD,
                    attribute = ElementalAttribute.DATA,
                    baseHp = 235,
                    basePower = 52,
                    baselineWeight = 22,
                    spriteKey = "hackmon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 341, minAgeSeconds = 86400L, minCareMistakes = 0, maxCareMistakes = 999)
                    )
                ),
                PetSpecies(
                    id = 311,
                    name = "Monochromon",
                    stage = PetStage.ADULT,
                    attribute = ElementalAttribute.DATA,
                    baseHp = 360,
                    basePower = 95,
                    baselineWeight = 35,
                    spriteKey = "monochromon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 418, minAgeSeconds = 172800L, minBattles = 15, minWinRatio = 0.75f, minEffort = 3), // Andromon
                        EvolutionRule(targetSpeciesId = 401, minAgeSeconds = 172800L, minBattles = 15, minWinRatio = 0.6f) // MetalGreymon
                    )
                ),
                PetSpecies(
                    id = 316,
                    name = "DarkTyrannomon",
                    stage = PetStage.ADULT,
                    attribute = ElementalAttribute.VIRUS,
                    baseHp = 370,
                    basePower = 100,
                    baselineWeight = 35,
                    spriteKey = "darktyrannomon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 414, minAgeSeconds = 172800L, minBattles = 15, minWinRatio = 0.6f, minCareMistakes = 0, maxCareMistakes = 5),
                        EvolutionRule(targetSpeciesId = 403, minAgeSeconds = 172800L, minCareMistakes = 6, maxCareMistakes = 999)
                    )
                ),
                PetSpecies(
                    id = 317,
                    name = "Devidramon",
                    stage = PetStage.ADULT,
                    attribute = ElementalAttribute.VIRUS,
                    baseHp = 365,
                    basePower = 98,
                    baselineWeight = 30,
                    spriteKey = "devidramon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 431, minAgeSeconds = 172800L, minBattles = 15, minWinRatio = 0.75f, minEffort = 3), // Cyberdramon
                        EvolutionRule(targetSpeciesId = 416, minAgeSeconds = 172800L, minBattles = 15, minWinRatio = 0.6f) // Megadramon
                    )
                ),
                PetSpecies(
                    id = 318,
                    name = "Cyclonemon",
                    stage = PetStage.ADULT,
                    attribute = ElementalAttribute.VIRUS,
                    baseHp = 360,
                    basePower = 96,
                    baselineWeight = 35,
                    spriteKey = "cyclonemon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 431, minAgeSeconds = 172800L, minBattles = 15, minWinRatio = 0.75f, minEffort = 3), // Cyberdramon
                        EvolutionRule(targetSpeciesId = 416, minAgeSeconds = 172800L, minBattles = 15, minWinRatio = 0.6f) // Megadramon
                    )
                ),
                PetSpecies(
                    id = 319,
                    name = "Tuskmon",
                    stage = PetStage.ADULT,
                    attribute = ElementalAttribute.VIRUS,
                    baseHp = 375,
                    basePower = 102,
                    baselineWeight = 40,
                    spriteKey = "tuskmon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 414, minAgeSeconds = 172800L, minBattles = 15, minWinRatio = 0.6f)
                    )
                ),
                PetSpecies(
                    id = 320,
                    name = "Flymon",
                    stage = PetStage.ADULT,
                    attribute = ElementalAttribute.VIRUS,
                    baseHp = 345,
                    basePower = 92,
                    baselineWeight = 25,
                    spriteKey = "flymon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 408, minAgeSeconds = 172800L, minBattles = 15, minWinRatio = 0.6f)
                    )
                ),
                PetSpecies(
                    id = 321,
                    name = "Raremon",
                    stage = PetStage.ADULT,
                    attribute = ElementalAttribute.VIRUS,
                    baseHp = 330,
                    basePower = 85,
                    baselineWeight = 30,
                    spriteKey = "raremon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 415, minAgeSeconds = 172800L, minCareMistakes = 5, maxCareMistakes = 999)
                    )
                ),
                PetSpecies(
                    id = 322,
                    name = "Kuwagamon",
                    stage = PetStage.ADULT,
                    attribute = ElementalAttribute.VIRUS,
                    baseHp = 360,
                    basePower = 96,
                    baselineWeight = 30,
                    spriteKey = "kuwagamon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 408, minAgeSeconds = 172800L, minBattles = 15, minWinRatio = 0.6f)
                    )
                ),
                PetSpecies(
                    id = 323,
                    name = "Kokatorimon",
                    stage = PetStage.ADULT,
                    attribute = ElementalAttribute.VACCINE,
                    baseHp = 345,
                    basePower = 90,
                    baselineWeight = 25,
                    spriteKey = "kokatorimon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 417, minAgeSeconds = 172800L, minBattles = 15, minWinRatio = 0.6f)
                    )
                ),
                PetSpecies(
                    id = 324,
                    name = "Coelamon",
                    stage = PetStage.ADULT,
                    attribute = ElementalAttribute.DATA,
                    baseHp = 355,
                    basePower = 94,
                    baselineWeight = 30,
                    spriteKey = "coelamon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 409, minAgeSeconds = 172800L, minBattles = 15, minWinRatio = 0.6f)
                    )
                ),
                PetSpecies(
                    id = 325,
                    name = "Nanimon",
                    stage = PetStage.ADULT,
                    attribute = ElementalAttribute.VIRUS,
                    baseHp = 325,
                    basePower = 82,
                    baselineWeight = 25,
                    spriteKey = "nanimon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 420, minAgeSeconds = 172800L, minCareMistakes = 6, maxCareMistakes = 999)
                    )
                ),
                PetSpecies(
                    id = 326,
                    name = "Bakemon",
                    stage = PetStage.ADULT,
                    attribute = ElementalAttribute.VIRUS,
                    baseHp = 340,
                    basePower = 88,
                    baselineWeight = 25,
                    spriteKey = "bakemon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 421, minAgeSeconds = 172800L, minBattles = 15, minWinRatio = 0.6f, minCareMistakes = 0, maxCareMistakes = 3),
                        EvolutionRule(targetSpeciesId = 410, minAgeSeconds = 172800L, minCareMistakes = 4, maxCareMistakes = 999)
                    )
                ),
                PetSpecies(
                    id = 327,
                    name = "Shellmon",
                    stage = PetStage.ADULT,
                    attribute = ElementalAttribute.DATA,
                    baseHp = 365,
                    basePower = 94,
                    baselineWeight = 40,
                    spriteKey = "shellmon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 409, minAgeSeconds = 172800L, minBattles = 15, minWinRatio = 0.6f)
                    )
                ),
                PetSpecies(
                    id = 328,
                    name = "Unimon",
                    stage = PetStage.ADULT,
                    attribute = ElementalAttribute.VACCINE,
                    baseHp = 360,
                    basePower = 95,
                    baselineWeight = 30,
                    spriteKey = "unimon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 405, minAgeSeconds = 172800L, minBattles = 15, minWinRatio = 0.6f)
                    )
                ),
                PetSpecies(
                    id = 329,
                    name = "Frigimon",
                    stage = PetStage.ADULT,
                    attribute = ElementalAttribute.VACCINE,
                    baseHp = 350,
                    basePower = 92,
                    baselineWeight = 30,
                    spriteKey = "frigimon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 432, minAgeSeconds = 172800L, minBattles = 15, minWinRatio = 0.75f, minEffort = 3), // Antylamon
                        EvolutionRule(targetSpeciesId = 402, minAgeSeconds = 172800L, minBattles = 15, minWinRatio = 0.6f) // Monzaemon
                    )
                ),
                PetSpecies(
                    id = 330,
                    name = "Vegiemon",
                    stage = PetStage.ADULT,
                    attribute = ElementalAttribute.VIRUS,
                    baseHp = 320,
                    basePower = 80,
                    baselineWeight = 25,
                    spriteKey = "vegiemon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 420, minAgeSeconds = 172800L, minCareMistakes = 6, maxCareMistakes = 999)
                    )
                ),
                PetSpecies(
                    id = 331,
                    name = "Veedramon",
                    stage = PetStage.ADULT,
                    attribute = ElementalAttribute.VACCINE,
                    baseHp = 380,
                    basePower = 105,
                    baselineWeight = 35,
                    spriteKey = "veedramon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 422, minAgeSeconds = 172800L, minBattles = 15, minWinRatio = 0.7f)
                    )
                ),
                PetSpecies(
                    id = 332,
                    name = "Flamedramon",
                    stage = PetStage.ADULT,
                    attribute = ElementalAttribute.FREE,
                    baseHp = 380,
                    basePower = 104,
                    baselineWeight = 30,
                    spriteKey = "flamedramon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 521, minAgeSeconds = 172800L, minBattles = 20, minWinRatio = 0.75f)
                    )
                ),
                PetSpecies(
                    id = 333,
                    name = "Stingmon",
                    stage = PetStage.ADULT,
                    attribute = ElementalAttribute.FREE,
                    baseHp = 365,
                    basePower = 98,
                    baselineWeight = 30,
                    spriteKey = "stingmon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 423, minAgeSeconds = 172800L, minBattles = 15, minWinRatio = 0.65f)
                    )
                ),
                PetSpecies(
                    id = 334,
                    name = "Growlmon",
                    stage = PetStage.ADULT,
                    attribute = ElementalAttribute.VIRUS,
                    baseHp = 390,
                    basePower = 108,
                    baselineWeight = 40,
                    spriteKey = "growlmon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 424, minAgeSeconds = 172800L, minBattles = 15, minWinRatio = 0.7f)
                    )
                ),
                PetSpecies(
                    id = 335,
                    name = "Kyubimon",
                    stage = PetStage.ADULT,
                    attribute = ElementalAttribute.DATA,
                    baseHp = 360,
                    basePower = 96,
                    baselineWeight = 30,
                    spriteKey = "kyubimon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 425, minAgeSeconds = 172800L, minBattles = 15, minWinRatio = 0.65f)
                    )
                ),
                PetSpecies(
                    id = 336,
                    name = "Gargomon",
                    stage = PetStage.ADULT,
                    attribute = ElementalAttribute.VACCINE,
                    baseHp = 370,
                    basePower = 100,
                    baselineWeight = 35,
                    spriteKey = "gargomon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 426, minAgeSeconds = 172800L, minBattles = 15, minWinRatio = 0.65f)
                    )
                ),
                PetSpecies(
                    id = 337,
                    name = "Wizardmon",
                    stage = PetStage.ADULT,
                    attribute = ElementalAttribute.DATA,
                    baseHp = 350,
                    basePower = 94,
                    baselineWeight = 28,
                    spriteKey = "wizardmon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 432, minAgeSeconds = 172800L, minBattles = 15, minWinRatio = 0.70f, minCareMistakes = 0, maxCareMistakes = 2), // Antylamon
                        EvolutionRule(targetSpeciesId = 410, minAgeSeconds = 172800L, minBattles = 15, minWinRatio = 0.65f, minCareMistakes = 3, maxCareMistakes = 999) // Myotismon
                    )
                ),
                PetSpecies(
                    id = 338,
                    name = "Gatomon",
                    stage = PetStage.ADULT,
                    attribute = ElementalAttribute.VACCINE,
                    baseHp = 360,
                    basePower = 96,
                    baselineWeight = 25,
                    spriteKey = "gatomon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 427, minAgeSeconds = 172800L, minBattles = 15, minWinRatio = 0.7f, minCareMistakes = 0, maxCareMistakes = 2), // Angewomon
                        EvolutionRule(targetSpeciesId = 428, minAgeSeconds = 172800L, minBattles = 15, minWinRatio = 0.65f, minCareMistakes = 3, maxCareMistakes = 999) // LadyDevimon
                    )
                ),
                PetSpecies(
                    id = 339,
                    name = "BlackGreymon",
                    stage = PetStage.ADULT,
                    attribute = ElementalAttribute.VIRUS,
                    baseHp = 385,
                    basePower = 106,
                    baselineWeight = 35,
                    spriteKey = "blackgreymon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 429, minAgeSeconds = 172800L, minBattles = 15, minWinRatio = 0.7f)
                    )
                ),
                PetSpecies(
                    id = 340,
                    name = "BlackGarurumon",
                    stage = PetStage.ADULT,
                    attribute = ElementalAttribute.VIRUS,
                    baseHp = 375,
                    basePower = 102,
                    baselineWeight = 35,
                    spriteKey = "blackgarurumon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 430, minAgeSeconds = 172800L, minBattles = 15, minWinRatio = 0.7f)
                    )
                ),
                PetSpecies(
                    id = 341,
                    name = "BaoHackmon",
                    stage = PetStage.ADULT,
                    attribute = ElementalAttribute.DATA,
                    baseHp = 380,
                    basePower = 104,
                    baselineWeight = 35,
                    spriteKey = "baohackmon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 433, minAgeSeconds = 172800L, minBattles = 15, minWinRatio = 0.7f)
                    )
                ),
                PetSpecies(
                    id = 414,
                    name = "MetalTyrannomon",
                    stage = PetStage.PERFECT,
                    attribute = ElementalAttribute.VIRUS,
                    baseHp = 480,
                    basePower = 135,
                    baselineWeight = 48,
                    spriteKey = "metaltyrannomon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 525, minAgeSeconds = 259200L, minBattles = 25, minWinRatio = 0.8f, minCareMistakes = 0, maxCareMistakes = 2), // BlackWarGreymon
                        EvolutionRule(targetSpeciesId = 502, minAgeSeconds = 259200L, minBattles = 20, minWinRatio = 0.75f) // Machinedramon
                    )
                ),
                PetSpecies(
                    id = 415,
                    name = "Ex-Tyrannomon",
                    stage = PetStage.PERFECT,
                    attribute = ElementalAttribute.VACCINE,
                    baseHp = 460,
                    basePower = 128,
                    baselineWeight = 40,
                    spriteKey = "extyrannomon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 530, minAgeSeconds = 259200L, minBattles = 20, minWinRatio = 0.7f)
                    )
                ),
                PetSpecies(
                    id = 416,
                    name = "Megadramon",
                    stage = PetStage.PERFECT,
                    attribute = ElementalAttribute.VIRUS,
                    baseHp = 490,
                    basePower = 138,
                    baselineWeight = 48,
                    spriteKey = "megadramon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 545, minAgeSeconds = 259200L, minBattles = 30, minWinRatio = 0.85f, minCareMistakes = 0, maxCareMistakes = 2, minEffort = 4), // Examon
                        EvolutionRule(targetSpeciesId = 502, minAgeSeconds = 259200L, minBattles = 20, minWinRatio = 0.75f) // Machinedramon
                    )
                ),
                PetSpecies(
                    id = 417,
                    name = "Piximon",
                    stage = PetStage.PERFECT,
                    attribute = ElementalAttribute.DATA,
                    baseHp = 450,
                    basePower = 125,
                    baselineWeight = 35,
                    spriteKey = "piximon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 505, minAgeSeconds = 259200L, minBattles = 20, minWinRatio = 0.7f)
                    )
                ),
                PetSpecies(
                    id = 418,
                    name = "Andromon",
                    stage = PetStage.PERFECT,
                    attribute = ElementalAttribute.VACCINE,
                    baseHp = 485,
                    basePower = 136,
                    baselineWeight = 50,
                    spriteKey = "andromon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 542, minAgeSeconds = 259200L, minBattles = 25, minWinRatio = 0.8f, minCareMistakes = 0, maxCareMistakes = 2), // Craniummon
                        EvolutionRule(targetSpeciesId = 541, minAgeSeconds = 259200L, minBattles = 25, minWinRatio = 0.75f, minCareMistakes = 3, maxCareMistakes = 4), // LordKnightmon
                        EvolutionRule(targetSpeciesId = 502, minAgeSeconds = 259200L, minBattles = 20, minWinRatio = 0.70f, minCareMistakes = 5, maxCareMistakes = 999) // Machinedramon
                    )
                ),
                PetSpecies(
                    id = 419,
                    name = "Giromon",
                    stage = PetStage.PERFECT,
                    attribute = ElementalAttribute.VACCINE,
                    baseHp = 475,
                    basePower = 132,
                    baselineWeight = 45,
                    spriteKey = "giromon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 542, minAgeSeconds = 259200L, minBattles = 20, minWinRatio = 0.7f)
                    )
                ),
                PetSpecies(
                    id = 420,
                    name = "Vademon",
                    stage = PetStage.PERFECT,
                    attribute = ElementalAttribute.VIRUS,
                    baseHp = 450,
                    basePower = 124,
                    baselineWeight = 38,
                    spriteKey = "vademon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 527, minAgeSeconds = 259200L, minBattles = 25, minWinRatio = 0.8f, minCareMistakes = 0, maxCareMistakes = 3), // Diaboromon
                        EvolutionRule(targetSpeciesId = 534, minAgeSeconds = 259200L, minBattles = 20, minWinRatio = 0.7f, minCareMistakes = 4, maxCareMistakes = 999) // Barbamon
                    )
                ),
                PetSpecies(
                    id = 421,
                    name = "SkullMamemon",
                    stage = PetStage.PERFECT,
                    attribute = ElementalAttribute.VACCINE,
                    baseHp = 480,
                    basePower = 135,
                    baselineWeight = 45,
                    spriteKey = "skullmamemon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 532, minAgeSeconds = 259200L, minBattles = 20, minWinRatio = 0.7f)
                    )
                ),
                PetSpecies(
                    id = 422,
                    name = "AeroVeedramon",
                    stage = PetStage.PERFECT,
                    attribute = ElementalAttribute.VACCINE,
                    baseHp = 500,
                    basePower = 142,
                    baselineWeight = 48,
                    spriteKey = "aeroveedramon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 522, minAgeSeconds = 259200L, minBattles = 25, minWinRatio = 0.8f, minCareMistakes = 0, maxCareMistakes = 2), // UlforceVeedramon
                        EvolutionRule(targetSpeciesId = 540, minAgeSeconds = 259200L, minBattles = 25, minWinRatio = 0.75f, minCareMistakes = 3, maxCareMistakes = 999) // Dynasmon
                    )
                ),
                PetSpecies(
                    id = 423,
                    name = "Paildramon",
                    stage = PetStage.PERFECT,
                    attribute = ElementalAttribute.FREE,
                    baseHp = 505,
                    basePower = 144,
                    baselineWeight = 48,
                    spriteKey = "paildramon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 512, minAgeSeconds = 259200L, minBattles = 25, minWinRatio = 0.8f)
                    )
                ),
                PetSpecies(
                    id = 424,
                    name = "WarGrowlmon",
                    stage = PetStage.PERFECT,
                    attribute = ElementalAttribute.VIRUS,
                    baseHp = 515,
                    basePower = 148,
                    baselineWeight = 50,
                    spriteKey = "wargrowlmon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 515, minAgeSeconds = 259200L, minBattles = 25, minWinRatio = 0.8f)
                    )
                ),
                PetSpecies(
                    id = 425,
                    name = "Taomon",
                    stage = PetStage.PERFECT,
                    attribute = ElementalAttribute.DATA,
                    baseHp = 480,
                    basePower = 136,
                    baselineWeight = 42,
                    spriteKey = "taomon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 517, minAgeSeconds = 259200L, minBattles = 20, minWinRatio = 0.75f)
                    )
                ),
                PetSpecies(
                    id = 426,
                    name = "Rapidmon",
                    stage = PetStage.PERFECT,
                    attribute = ElementalAttribute.VACCINE,
                    baseHp = 490,
                    basePower = 138,
                    baselineWeight = 45,
                    spriteKey = "rapidmon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 518, minAgeSeconds = 259200L, minBattles = 20, minWinRatio = 0.75f)
                    )
                ),
                PetSpecies(
                    id = 427,
                    name = "Angewomon",
                    stage = PetStage.PERFECT,
                    attribute = ElementalAttribute.VACCINE,
                    baseHp = 495,
                    basePower = 140,
                    baselineWeight = 40,
                    spriteKey = "angewomon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 556, minAgeSeconds = 259200L, minBattles = 30, minWinRatio = 0.85f, minCareMistakes = 0, maxCareMistakes = 1), // Magnadramon
                        EvolutionRule(targetSpeciesId = 552, minAgeSeconds = 259200L, minBattles = 25, minWinRatio = 0.80f, minCareMistakes = 0, maxCareMistakes = 3), // Ophanimon
                        EvolutionRule(targetSpeciesId = 537, minAgeSeconds = 259200L, minBattles = 25, minWinRatio = 0.75f, minCareMistakes = 4, maxCareMistakes = 999)  // Mastemon
                    )
                ),
                PetSpecies(
                    id = 428,
                    name = "LadyDevimon",
                    stage = PetStage.PERFECT,
                    attribute = ElementalAttribute.VIRUS,
                    baseHp = 495,
                    basePower = 142,
                    baselineWeight = 40,
                    spriteKey = "ladydevimon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 537, minAgeSeconds = 259200L, minBattles = 25, minWinRatio = 0.8f, minCareMistakes = 0, maxCareMistakes = 2, minEffort = 4), // Mastemon
                        EvolutionRule(targetSpeciesId = 531, minAgeSeconds = 259200L, minBattles = 25, minWinRatio = 0.75f) // Lilithmon
                    )
                ),
                PetSpecies(
                    id = 429,
                    name = "BlackMetalGreymon",
                    stage = PetStage.PERFECT,
                    attribute = ElementalAttribute.VIRUS,
                    baseHp = 510,
                    basePower = 146,
                    baselineWeight = 50,
                    spriteKey = "blackmetalgreymon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 525, minAgeSeconds = 259200L, minBattles = 25, minWinRatio = 0.8f)
                    )
                ),
                PetSpecies(
                    id = 430,
                    name = "BlackWereGarurumon",
                    stage = PetStage.PERFECT,
                    attribute = ElementalAttribute.VIRUS,
                    baseHp = 500,
                    basePower = 144,
                    baselineWeight = 48,
                    spriteKey = "blackweregarurumon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 536, minAgeSeconds = 259200L, minBattles = 25, minWinRatio = 0.8f)
                    )
                ),
                PetSpecies(
                    id = 431,
                    name = "Cyberdramon",
                    stage = PetStage.PERFECT,
                    attribute = ElementalAttribute.VACCINE,
                    baseHp = 505,
                    basePower = 145,
                    baselineWeight = 48,
                    spriteKey = "cyberdramon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 523, minAgeSeconds = 259200L, minBattles = 30, minWinRatio = 0.85f, minCareMistakes = 0, maxCareMistakes = 2, minEffort = 4), // Alphamon
                        EvolutionRule(targetSpeciesId = 546, minAgeSeconds = 259200L, minBattles = 25, minWinRatio = 0.75f) // Millenniummon
                    )
                ),
                PetSpecies(
                    id = 432,
                    name = "Antylamon",
                    stage = PetStage.PERFECT,
                    attribute = ElementalAttribute.DATA,
                    baseHp = 490,
                    basePower = 138,
                    baselineWeight = 45,
                    spriteKey = "antylamon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 557, minAgeSeconds = 259200L, minBattles = 25, minWinRatio = 0.80f, minCareMistakes = 0, maxCareMistakes = 2), // Cherubimon
                        EvolutionRule(targetSpeciesId = 549, minAgeSeconds = 259200L, minBattles = 25, minWinRatio = 0.75f, minCareMistakes = 3, maxCareMistakes = 999) // Susanoomon
                    )
                ),
                PetSpecies(
                    id = 433,
                    name = "SaviorHackmon",
                    stage = PetStage.PERFECT,
                    attribute = ElementalAttribute.DATA,
                    baseHp = 510,
                    basePower = 146,
                    baselineWeight = 48,
                    spriteKey = "saviorhackmon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 538, minAgeSeconds = 259200L, minBattles = 30, minWinRatio = 0.80f, minCareMistakes = 0, maxCareMistakes = 2), // Jesmon
                        EvolutionRule(targetSpeciesId = 539, minAgeSeconds = 259200L, minBattles = 25, minWinRatio = 0.75f, minCareMistakes = 3, maxCareMistakes = 999) // Gankoomon
                    )
                ),
                PetSpecies(
                    id = 434,
                    name = "Lucemon Chaos Mode",
                    stage = PetStage.PERFECT,
                    attribute = ElementalAttribute.VIRUS,
                    baseHp = 580,
                    basePower = 175,
                    baselineWeight = 45,
                    spriteKey = "lucemon_cm",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 532, minAgeSeconds = 259200L, minBattles = 30, minWinRatio = 0.85f)
                    )
                ),
                PetSpecies(
                    id = 512,
                    name = "Imperialdramon Dragon Mode",
                    stage = PetStage.ULTIMATE,
                    attribute = ElementalAttribute.FREE,
                    baseHp = 630,
                    basePower = 188,
                    baselineWeight = 58,
                    spriteKey = "imperialdramon_dm",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 513, minBattles = 20, minWinRatio = 0.8f)
                    )
                ),
                PetSpecies(
                    id = 513,
                    name = "Imperialdramon Fighter Mode",
                    stage = PetStage.ULTIMATE,
                    attribute = ElementalAttribute.FREE,
                    baseHp = 650,
                    basePower = 195,
                    baselineWeight = 55,
                    spriteKey = "imperialdramon_fm",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 514, minBattles = 30, minWinRatio = 0.85f)
                    )
                ),
                PetSpecies(
                    id = 514,
                    name = "Imperialdramon Paladin Mode",
                    stage = PetStage.ULTIMATE,
                    attribute = ElementalAttribute.VACCINE,
                    baseHp = 680,
                    basePower = 205,
                    baselineWeight = 55,
                    spriteKey = "imperialdramon_pm",
                    evolutionRules = emptyList()
                ),
                PetSpecies(
                    id = 515,
                    name = "Gallantmon",
                    stage = PetStage.ULTIMATE,
                    attribute = ElementalAttribute.VIRUS,
                    baseHp = 635,
                    basePower = 190,
                    baselineWeight = 55,
                    spriteKey = "gallantmon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 516, minBattles = 25, minWinRatio = 0.8f)
                    )
                ),
                PetSpecies(
                    id = 516,
                    name = "Gallantmon Crimson Mode",
                    stage = PetStage.ULTIMATE,
                    attribute = ElementalAttribute.VIRUS,
                    baseHp = 675,
                    basePower = 202,
                    baselineWeight = 55,
                    spriteKey = "gallantmon_cm",
                    evolutionRules = emptyList()
                ),
                PetSpecies(
                    id = 517,
                    name = "Sakuyamon",
                    stage = PetStage.ULTIMATE,
                    attribute = ElementalAttribute.DATA,
                    baseHp = 610,
                    basePower = 180,
                    baselineWeight = 50,
                    spriteKey = "sakuyamon",
                    evolutionRules = emptyList()
                ),
                PetSpecies(
                    id = 518,
                    name = "MegaGargomon",
                    stage = PetStage.ULTIMATE,
                    attribute = ElementalAttribute.VACCINE,
                    baseHp = 640,
                    basePower = 192,
                    baselineWeight = 60,
                    spriteKey = "megagargomon",
                    evolutionRules = emptyList()
                ),
                PetSpecies(
                    id = 519,
                    name = "Beelzemon",
                    stage = PetStage.ULTIMATE,
                    attribute = ElementalAttribute.VIRUS,
                    baseHp = 645,
                    basePower = 194,
                    baselineWeight = 52,
                    spriteKey = "beelzemon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 520, minBattles = 25, minWinRatio = 0.8f)
                    )
                ),
                PetSpecies(
                    id = 520,
                    name = "Beelzemon Blast Mode",
                    stage = PetStage.ULTIMATE,
                    attribute = ElementalAttribute.VIRUS,
                    baseHp = 675,
                    basePower = 204,
                    baselineWeight = 52,
                    spriteKey = "beelzemon_bm",
                    evolutionRules = emptyList()
                ),
                PetSpecies(
                    id = 521,
                    name = "Magnamon",
                    stage = PetStage.ULTIMATE,
                    attribute = ElementalAttribute.FREE,
                    baseHp = 625,
                    basePower = 186,
                    baselineWeight = 50,
                    spriteKey = "magnamon",
                    evolutionRules = emptyList()
                ),
                PetSpecies(
                    id = 522,
                    name = "UlforceVeedramon",
                    stage = PetStage.ULTIMATE,
                    attribute = ElementalAttribute.VACCINE,
                    baseHp = 645,
                    basePower = 194,
                    baselineWeight = 52,
                    spriteKey = "ulforceveedramon",
                    evolutionRules = emptyList()
                ),
                PetSpecies(
                    id = 523,
                    name = "Alphamon",
                    stage = PetStage.ULTIMATE,
                    attribute = ElementalAttribute.VACCINE,
                    baseHp = 660,
                    basePower = 198,
                    baselineWeight = 58,
                    spriteKey = "alphamon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 524, minBattles = 25, minWinRatio = 0.85f)
                    )
                ),
                PetSpecies(
                    id = 524,
                    name = "Alphamon Ouryuken",
                    stage = PetStage.ULTIMATE,
                    attribute = ElementalAttribute.VACCINE,
                    baseHp = 690,
                    basePower = 210,
                    baselineWeight = 60,
                    spriteKey = "alphamon_ouryuken",
                    evolutionRules = emptyList()
                ),
                PetSpecies(
                    id = 525,
                    name = "BlackWarGreymon",
                    stage = PetStage.ULTIMATE,
                    attribute = ElementalAttribute.VIRUS,
                    baseHp = 635,
                    basePower = 192,
                    baselineWeight = 55,
                    spriteKey = "blackwargreymon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 526, minBattles = 30, minWinRatio = 0.85f) // ChaosBlackWarGreymon
                    )
                ),
                PetSpecies(
                    id = 526,
                    name = "ChaosBlackWarGreymon",
                    stage = PetStage.ULTIMATE,
                    attribute = ElementalAttribute.VIRUS,
                    baseHp = 650,
                    basePower = 196,
                    baselineWeight = 55,
                    spriteKey = "chaosblackwargreymon",
                    evolutionRules = emptyList()
                ),
                PetSpecies(
                    id = 527,
                    name = "Diaboromon",
                    stage = PetStage.ULTIMATE,
                    attribute = ElementalAttribute.VIRUS,
                    baseHp = 645,
                    basePower = 194,
                    baselineWeight = 52,
                    spriteKey = "diaboromon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 528, minBattles = 30, minWinRatio = 0.85f)
                    )
                ),
                PetSpecies(
                    id = 528,
                    name = "Armaggeddemon",
                    stage = PetStage.ULTIMATE,
                    attribute = ElementalAttribute.VIRUS,
                    baseHp = 685,
                    basePower = 208,
                    baselineWeight = 62,
                    spriteKey = "armaggeddemon",
                    evolutionRules = emptyList()
                ),
                PetSpecies(
                    id = 529,
                    name = "Apocalymon",
                    stage = PetStage.ULTIMATE,
                    attribute = ElementalAttribute.VIRUS,
                    baseHp = 670,
                    basePower = 200,
                    baselineWeight = 65,
                    spriteKey = "apocalymon",
                    evolutionRules = emptyList()
                ),
                PetSpecies(
                    id = 530,
                    name = "Belphemon",
                    stage = PetStage.ULTIMATE,
                    attribute = ElementalAttribute.VIRUS,
                    baseHp = 655,
                    basePower = 198,
                    baselineWeight = 56,
                    spriteKey = "belphemon",
                    evolutionRules = emptyList()
                ),
                PetSpecies(
                    id = 531,
                    name = "Lilithmon",
                    stage = PetStage.ULTIMATE,
                    attribute = ElementalAttribute.VIRUS,
                    baseHp = 630,
                    basePower = 190,
                    baselineWeight = 50,
                    spriteKey = "lilithmon",
                    evolutionRules = emptyList()
                ),
                PetSpecies(
                    id = 532,
                    name = "Daemon",
                    stage = PetStage.ULTIMATE,
                    attribute = ElementalAttribute.VIRUS,
                    baseHp = 655,
                    basePower = 198,
                    baselineWeight = 56,
                    spriteKey = "daemon",
                    evolutionRules = emptyList()
                ),
                PetSpecies(
                    id = 533,
                    name = "Leviamon",
                    stage = PetStage.ULTIMATE,
                    attribute = ElementalAttribute.VIRUS,
                    baseHp = 665,
                    basePower = 200,
                    baselineWeight = 65,
                    spriteKey = "leviamon",
                    evolutionRules = emptyList()
                ),
                PetSpecies(
                    id = 534,
                    name = "Barbamon",
                    stage = PetStage.ULTIMATE,
                    attribute = ElementalAttribute.VIRUS,
                    baseHp = 640,
                    basePower = 192,
                    baselineWeight = 52,
                    spriteKey = "barbamon",
                    evolutionRules = emptyList()
                ),
                PetSpecies(
                    id = 535,
                    name = "Omnimon Alter-S",
                    stage = PetStage.ULTIMATE,
                    attribute = ElementalAttribute.VIRUS,
                    baseHp = 680,
                    basePower = 206,
                    baselineWeight = 56,
                    spriteKey = "omnimon_alters",
                    evolutionRules = emptyList()
                ),
                PetSpecies(
                    id = 536,
                    name = "Omnimon Zwart",
                    stage = PetStage.ULTIMATE,
                    attribute = ElementalAttribute.VIRUS,
                    baseHp = 675,
                    basePower = 204,
                    baselineWeight = 56,
                    spriteKey = "omnimon_zwart",
                    evolutionRules = emptyList()
                ),
                PetSpecies(
                    id = 537,
                    name = "Mastemon",
                    stage = PetStage.ULTIMATE,
                    attribute = ElementalAttribute.VACCINE,
                    baseHp = 650,
                    basePower = 196,
                    baselineWeight = 52,
                    spriteKey = "mastemon",
                    evolutionRules = emptyList()
                ),
                PetSpecies(
                    id = 538,
                    name = "Jesmon",
                    stage = PetStage.ULTIMATE,
                    attribute = ElementalAttribute.DATA,
                    baseHp = 655,
                    basePower = 198,
                    baselineWeight = 54,
                    spriteKey = "jesmon",
                    evolutionRules = emptyList()
                ),
                PetSpecies(
                    id = 539,
                    name = "Gankoomon",
                    stage = PetStage.ULTIMATE,
                    attribute = ElementalAttribute.DATA,
                    baseHp = 650,
                    basePower = 196,
                    baselineWeight = 58,
                    spriteKey = "gankoomon",
                    evolutionRules = emptyList()
                ),
                PetSpecies(
                    id = 540,
                    name = "Dynasmon",
                    stage = PetStage.ULTIMATE,
                    attribute = ElementalAttribute.DATA,
                    baseHp = 645,
                    basePower = 194,
                    baselineWeight = 55,
                    spriteKey = "dynasmon",
                    evolutionRules = emptyList()
                ),
                PetSpecies(
                    id = 541,
                    name = "LordKnightmon",
                    stage = PetStage.ULTIMATE,
                    attribute = ElementalAttribute.VIRUS,
                    baseHp = 645,
                    basePower = 194,
                    baselineWeight = 52,
                    spriteKey = "lordknightmon",
                    evolutionRules = emptyList()
                ),
                PetSpecies(
                    id = 542,
                    name = "Craniummon",
                    stage = PetStage.ULTIMATE,
                    attribute = ElementalAttribute.VACCINE,
                    baseHp = 655,
                    basePower = 198,
                    baselineWeight = 58,
                    spriteKey = "craniummon",
                    evolutionRules = emptyList()
                ),
                PetSpecies(
                    id = 543,
                    name = "Sleipmon",
                    stage = PetStage.ULTIMATE,
                    attribute = ElementalAttribute.VACCINE,
                    baseHp = 645,
                    basePower = 194,
                    baselineWeight = 56,
                    spriteKey = "sleipmon",
                    evolutionRules = emptyList()
                ),
                PetSpecies(
                    id = 544,
                    name = "Duftmon",
                    stage = PetStage.ULTIMATE,
                    attribute = ElementalAttribute.DATA,
                    baseHp = 640,
                    basePower = 192,
                    baselineWeight = 52,
                    spriteKey = "duftmon",
                    evolutionRules = emptyList()
                ),
                PetSpecies(
                    id = 545,
                    name = "Examon",
                    stage = PetStage.ULTIMATE,
                    attribute = ElementalAttribute.DATA,
                    baseHp = 675,
                    basePower = 205,
                    baselineWeight = 65,
                    spriteKey = "examon",
                    evolutionRules = emptyList()
                ),
                PetSpecies(
                    id = 546,
                    name = "Millenniummon",
                    stage = PetStage.ULTIMATE,
                    attribute = ElementalAttribute.VIRUS,
                    baseHp = 665,
                    basePower = 200,
                    baselineWeight = 60,
                    spriteKey = "millenniummon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 547, minBattles = 30, minWinRatio = 0.85f)
                    )
                ),
                PetSpecies(
                    id = 547,
                    name = "MoonMillenniummon",
                    stage = PetStage.ULTIMATE,
                    attribute = ElementalAttribute.VIRUS,
                    baseHp = 680,
                    basePower = 206,
                    baselineWeight = 60,
                    spriteKey = "moonmillenniummon",
                    evolutionRules = listOf(
                        EvolutionRule(targetSpeciesId = 548, minBattles = 35, minWinRatio = 0.9f)
                    )
                ),
                PetSpecies(
                    id = 548,
                    name = "ZeedMillenniummon",
                    stage = PetStage.ULTIMATE,
                    attribute = ElementalAttribute.VIRUS,
                    baseHp = 700,
                    basePower = 215,
                    baselineWeight = 65,
                    spriteKey = "zeedmillenniummon",
                    evolutionRules = emptyList()
                ),
                PetSpecies(
                    id = 549,
                    name = "Susanoomon",
                    stage = PetStage.ULTIMATE,
                    attribute = ElementalAttribute.VACCINE,
                    baseHp = 690,
                    basePower = 210,
                    baselineWeight = 60,
                    spriteKey = "susanoomon",
                    evolutionRules = emptyList()
                )

            ).plus(ExpandedRoster.SPECIES).associateBy { it.id }
        }

        fun getById(speciesId: Int): PetSpecies =
            SPECIES_REGISTRY[speciesId] ?: SPECIES_REGISTRY[101]!!
    }
}
