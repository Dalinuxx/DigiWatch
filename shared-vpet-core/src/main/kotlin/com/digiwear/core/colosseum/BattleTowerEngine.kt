package com.digiwear.core.colosseum

import com.digiwear.core.battle.DeterministicBattleSolver
import com.digiwear.core.model.BattleFighter
import com.digiwear.core.model.ElementalAttribute
import com.digiwear.core.model.PetSpecies
import com.digiwear.core.model.PetStage
import kotlinx.serialization.Serializable

/**
 * Floor metadata in the 100-Floor Endless Colosseum Gauntlet.
 */
@Serializable
data class TowerFloor(
    val floorNumber: Int,
    val tierNumber: Int,
    val tierName: String,
    val opponentSpeciesId: Int,
    val opponentName: String,
    val opponentStage: PetStage,
    val opponentAttribute: ElementalAttribute,
    val opponentHp: Int,
    val opponentPower: Int,
    val isBossFloor: Boolean,
    val environmentEffect: String = "none",
    val rewardDescription: String
)

/**
 * Result of challenging a floor in the Battle Tower.
 */
@Serializable
data class TowerBattleResult(
    val floorNumber: Int,
    val won: Boolean,
    val turnsTaken: Int,
    val remainingHp: Int,
    val unlockedNextFloor: Boolean,
    val rewardGiven: String?
)

/**
 * Engine driving the 100-Floor Battle Tower / Colosseum Tournament.
 * Features 10 difficulty tiers with deterministic canonical opponents, environmental hazards,
 * win-streak tracking, and milestone rewards.
 */
object BattleTowerEngine {

    const val MAX_FLOORS = 100

    private val TIER_NAMES = listOf(
        "Initiate Trial",       // T1: Floors 1-10
        "Champion Frontier",   // T2: Floors 11-20
        "Vanguard Coliseum",   // T3: Floors 21-30
        "Perfect Storm",       // T4: Floors 31-40
        "Digital Citadel",     // T5: Floors 41-50
        "Olympos Gate",        // T6: Floors 51-60
        "Royal Enclave",       // T7: Floors 61-70
        "X-Antibody Domain",   // T8: Floors 71-80
        "Apocalypse Sanctum",  // T9: Floors 81-90
        "Apex Sovereigns"      // T10: Floors 91-100
    )

    // Curated milestone floor bosses
    private val MILESTONE_BOSSES = mapOf(
        10 to 201,   // Floor 10 Boss: Agumon
        20 to 301,   // Floor 20 Boss: Greymon
        30 to 338,   // Floor 30 Boss: Gatomon
        40 to 401,   // Floor 40 Boss: MetalGreymon
        50 to 427,   // Floor 50 Boss: Angewomon
        60 to 501,   // Floor 60 Boss: WarGreymon
        70 to 510,   // Floor 70 Boss: Omnimon
        80 to 579,   // Floor 80 Boss: Gallantmon X
        90 to 567,   // Floor 90 Boss: Chaosmon
        100 to 597   // Floor 100 Apex: Proximamon (God of Destruction)
    )

    // Curated species pool for non-boss floors by tier
    private val TIER_SPECIES_POOLS = listOf(
        // Tier 1: Child species
        listOf(201, 202, 203, 204, 205, 206, 207, 208, 209, 210, 223, 224, 225, 226, 227, 251, 252, 255, 256, 261, 262, 263),
        // Tier 2: Early Adult species
        listOf(301, 302, 303, 304, 305, 306, 307, 308, 309, 310, 311, 312, 342, 343, 344, 345, 346, 373, 374, 388, 389, 390),
        // Tier 3: Elite Adult / Armor
        listOf(313, 314, 315, 316, 317, 318, 321, 322, 323, 331, 332, 333, 334, 337, 350, 351, 371, 375, 381, 391, 396, 397),
        // Tier 4: Early Perfect species
        listOf(401, 402, 403, 404, 405, 406, 407, 408, 409, 410, 435, 436, 437, 438, 439, 440, 441, 465, 466, 467, 478, 479),
        // Tier 5: Elite Perfect / Apex Champions
        listOf(411, 412, 413, 414, 415, 416, 417, 418, 419, 420, 421, 422, 423, 424, 425, 442, 445, 468, 469, 480, 483, 484),
        // Tier 6: Ultimate / Mega Vanguard
        listOf(501, 502, 503, 504, 505, 506, 507, 508, 509, 529, 530, 531, 532, 533, 534, 550, 551, 552, 553, 593, 594, 595),
        // Tier 7: Royal Knights & Olympos XII
        listOf(511, 512, 513, 514, 515, 516, 517, 518, 519, 520, 521, 522, 523, 537, 538, 539, 540, 541, 542, 543, 544, 545),
        // Tier 8: X-Antibody Ultimates
        listOf(579, 580, 581, 582, 1538, 1539, 1540, 1541, 1542, 1543, 1544, 1545, 1557, 1558, 1559, 1560, 1561, 1562, 1563),
        // Tier 9: Ancient Titans & Burst Fusions
        listOf(549, 554, 558, 559, 560, 561, 562, 563, 564, 565, 566, 568, 569, 576, 577, 583, 586, 588, 589, 590, 596, 598),
        // Tier 10: Super Ultimate Apex Sovereigns
        listOf(510, 524, 526, 528, 535, 536, 549, 567, 578, 592, 596, 597, 599, 1546, 1547, 1551, 1552, 1553, 1554, 1555, 1556)
    )

    private val ENVIRONMENTS = listOf("none", "Data Storm", "Glitch Fog", "Cosmic Nihilism", "Overclock Heat", "Subzero Chill")

    /**
     * Retrieves the complete metadata for a given floor [1..100].
     */
    fun getFloor(floorNumber: Int): TowerFloor {
        val f = floorNumber.coerceIn(1, MAX_FLOORS)
        val tierIndex = (f - 1) / 10
        val tierName = TIER_NAMES[tierIndex]
        val isBoss = f % 10 == 0

        val speciesId = MILESTONE_BOSSES[f] ?: run {
            val pool = TIER_SPECIES_POOLS[tierIndex]
            val pickIndex = (f * 7 + tierIndex * 13) % pool.size
            pool[pickIndex]
        }

        val sp = PetSpecies.getById(speciesId)

        // Stat scaling across the 100 floors
        val hpBonus = (f * 3.5f).toInt() + (if (isBoss) 50 else 0)
        val pwrBonus = (f * 0.75f).toInt() + (if (isBoss) 15 else 0)
        val finalHp = sp.baseHp + hpBonus
        val finalPwr = sp.basePower + pwrBonus

        // Environmental hazards on higher floors
        val env = if (f > 20 && f % 3 == 0) {
            ENVIRONMENTS[(f / 3) % ENVIRONMENTS.size]
        } else "none"

        val reward = when {
            f == 100 -> "Grand Colosseum Trophy & +2 Traited Egg Bonus"
            f % 20 == 0 -> "Colosseum Crest Badge & +1 Traited Egg Bonus"
            isBoss -> "Milestone Victory Star & +5 Max HP"
            else -> "+1 Colosseum Win Point"
        }

        return TowerFloor(
            floorNumber = f,
            tierNumber = tierIndex + 1,
            tierName = tierName,
            opponentSpeciesId = sp.id,
            opponentName = sp.name,
            opponentStage = sp.stage,
            opponentAttribute = sp.attribute,
            opponentHp = finalHp,
            opponentPower = finalPwr,
            isBossFloor = isBoss,
            environmentEffect = env,
            rewardDescription = reward
        )
    }

    /**
     * Builds a [BattleFighter] instance for the opponent on [floorNumber].
     */
    fun createOpponentFighter(floorNumber: Int): BattleFighter {
        val floor = getFloor(floorNumber)
        return BattleFighter(
            id = "tower_floor_$floorNumber",
            speciesId = floor.opponentSpeciesId,
            name = floor.opponentName,
            stage = floor.opponentStage,
            attribute = floor.opponentAttribute,
            maxHp = floor.opponentHp,
            currentHp = floor.opponentHp,
            basePower = floor.opponentPower,
            victories = floorNumber * 2,
            effort = (floor.tierNumber / 3).coerceIn(0, 4),
            strength = 4,
            battleSeed = 0x54321 + floorNumber * 101
        )
    }
}
