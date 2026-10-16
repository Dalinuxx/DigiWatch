package com.digiwatch.core.adventure

import com.digiwatch.core.model.AdventureProgress
import com.digiwatch.core.model.BossEncounter
import com.digiwatch.core.model.CampaignNode
import com.digiwatch.core.model.CampaignStory
import kotlinx.serialization.json.Json
import java.util.Random

data class AdventureStepResult(
    val updatedProgress: AdventureProgress,
    val nodeCleared: Boolean = false,
    val campaignCompleted: Boolean = false,
    val randomEncounterMonsterId: Int? = null,
    val bossEncounter: BossEncounter? = null,
    val narrativeEvent: String? = null
)

/**
 * Adventure Mode narrative campaign processor driven by physical step counting.
 * Expanded to 30 continuous campaign sectors spanning File Island, Server Continent,
 * Spiral Mountain, and the Digital Kernel Dimension.
 */
object AdventureEngine {

    val jsonParser = Json {
        ignoreUnknownKeys = true
        isLenient = true
        prettyPrint = true
    }

    const val DEFAULT_CAMPAIGN_JSON = """
{
  "campaign_id": "file_island_corrupted_sector",
  "title": "Grand Odyssey: 30 Sectors of the Digital World",
  "nodes": [
    {
      "node_id": 101,
      "name": "Binary Coastline",
      "required_steps": 400,
      "environment_effect": "glitch_fog",
      "encounter_rate": 0.20,
      "encounter_pool": [101, 102, 201, 202],
      "narrative_text": "Static washes over the shoreline. Pixelated tracks lead into the digital thicket."
    },
    {
      "node_id": 102,
      "name": "Native Forest",
      "required_steps": 800,
      "environment_effect": "cyber_rain",
      "encounter_rate": 0.25,
      "encounter_pool": [201, 203, 204, 207],
      "narrative_text": "Towering digital redwoods hum with data packets. Whispers echo through the leaves."
    },
    {
      "node_id": 103,
      "name": "Overclocked Jungle",
      "required_steps": 1200,
      "environment_effect": "heat_wave",
      "encounter_rate": 0.30,
      "encounter_pool": [201, 206, 308],
      "boss_encounter": {
        "boss_monster_id": 312,
        "boss_hp": 240,
        "boss_power": 65,
        "completion_flag": "FLAG_JUNGLE_CLEARED"
      },
      "narrative_text": "A brutish rogue Ogremon blocks the jungle pass brandishing a bone club!"
    },
    {
      "node_id": 104,
      "name": "Coela Reef",
      "required_steps": 1600,
      "environment_effect": "tide_surge",
      "encounter_rate": 0.30,
      "encounter_pool": [202, 208, 314],
      "boss_encounter": {
        "boss_monster_id": 314,
        "boss_hp": 260,
        "boss_power": 68,
        "completion_flag": "FLAG_REEF_CLEARED"
      },
      "narrative_text": "Turbulent waves crash against digital reefs. A serpentine guardian coils below."
    },
    {
      "node_id": 105,
      "name": "Great Canyon",
      "required_steps": 2000,
      "environment_effect": "electromagnetic_storm",
      "encounter_rate": 0.35,
      "encounter_pool": [302, 304, 306],
      "boss_encounter": {
        "boss_monster_id": 301,
        "boss_hp": 280,
        "boss_power": 72,
        "completion_flag": "FLAG_CANYON_CLEARED"
      },
      "narrative_text": "Lightning crackles across red stone chasms. A feral Greymon roars at the canyon rim!"
    },
    {
      "node_id": 106,
      "name": "Gear Savanna",
      "required_steps": 2400,
      "environment_effect": "clockwork_drift",
      "encounter_rate": 0.35,
      "encounter_pool": [304, 310, 313],
      "boss_encounter": {
        "boss_monster_id": 313,
        "boss_hp": 300,
        "boss_power": 75,
        "completion_flag": "FLAG_SAVANNA_CLEARED"
      },
      "narrative_text": "Interlocking brass gears turn the desert soil. The noble guardian Leomon tests your resolve."
    },
    {
      "node_id": 107,
      "name": "Freezeland Glacier",
      "required_steps": 2800,
      "environment_effect": "subzero_frost",
      "encounter_rate": 0.35,
      "encounter_pool": [306, 307, 329],
      "boss_encounter": {
        "boss_monster_id": 404,
        "boss_hp": 380,
        "boss_power": 105,
        "completion_flag": "FLAG_GLACIER_CLEARED"
      },
      "narrative_text": "Sub-zero blizzard winds bite. WereGarurumon prowls the moonlit snowdrifts."
    },
    {
      "node_id": 108,
      "name": "Misty Sanctuary",
      "required_steps": 3200,
      "environment_effect": "miasma_haze",
      "encounter_rate": 0.40,
      "encounter_pool": [303, 305, 326],
      "boss_encounter": {
        "boss_monster_id": 303,
        "boss_hp": 400,
        "boss_power": 110,
        "completion_flag": "FLAG_SANCTUARY_CLEARED"
      },
      "narrative_text": "Poisonous purple fog obscures ancient crypts. Devimon descends with shadow wings extended!"
    },
    {
      "node_id": 109,
      "name": "Dragon Eye Lake",
      "required_steps": 3600,
      "environment_effect": "whirlpool_vortex",
      "encounter_rate": 0.35,
      "encounter_pool": [314, 324, 409],
      "boss_encounter": {
        "boss_monster_id": 409,
        "boss_hp": 420,
        "boss_power": 115,
        "completion_flag": "FLAG_LAKE_CLEARED"
      },
      "narrative_text": "The deepest lake on File Island bubbles violently. MegaSeadramon surges from the depths!"
    },
    {
      "node_id": 110,
      "name": "Mt. Infinity Spire",
      "required_steps": 4000,
      "environment_effect": "dark_gravity",
      "encounter_rate": 0.40,
      "encounter_pool": [401, 403, 410],
      "boss_encounter": {
        "boss_monster_id": 410,
        "boss_hp": 460,
        "boss_power": 125,
        "completion_flag": "FLAG_INFINITY_CLEARED"
      },
      "narrative_text": "Monolithic servers pierce the clouds. Myotismon summons clouds of crimson data bats!"
    },
    {
      "node_id": 111,
      "name": "Sub-Oceanic Cable Highway",
      "required_steps": 4200,
      "environment_effect": "high_pressure_data",
      "encounter_rate": 0.35,
      "encounter_pool": [210, 324, 409],
      "boss_encounter": {
        "boss_monster_id": 327,
        "boss_hp": 340,
        "boss_power": 85,
        "completion_flag": "FLAG_CABLE_CLEARED"
      },
      "narrative_text": "Massive undersea optical conduits pulse with inter-continental data. Shellmon attacks from the reef!"
    },
    {
      "node_id": 112,
      "name": "Net Ocean Continental Shelf",
      "required_steps": 4400,
      "environment_effect": "hydro_thermal_vent",
      "encounter_rate": 0.35,
      "encounter_pool": [314, 324, 508],
      "boss_encounter": {
        "boss_monster_id": 508,
        "boss_hp": 520,
        "boss_power": 145,
        "completion_flag": "FLAG_NETOCEAN_CLEARED"
      },
      "narrative_text": "Thermal underwater vents illuminate a sunken kingdom. Plesiomon protects the deep passage."
    },
    {
      "node_id": 113,
      "name": "Server Continent Shoreline",
      "required_steps": 4600,
      "environment_effect": "scorching_sunlight",
      "encounter_rate": 0.35,
      "encounter_pool": [209, 214, 316],
      "boss_encounter": {
        "boss_monster_id": 316,
        "boss_hp": 380,
        "boss_power": 95,
        "completion_flag": "FLAG_SERVER_SHORE_CLEARED"
      },
      "narrative_text": "Red dunes stretch past the horizon. DarkTyrannomon prowls the baking coastal sands!"
    },
    {
      "node_id": 114,
      "name": "Searing Desert Oasis",
      "required_steps": 4800,
      "environment_effect": "blinding_sandstorm",
      "encounter_rate": 0.35,
      "encounter_pool": [205, 318, 325],
      "boss_encounter": {
        "boss_monster_id": 411,
        "boss_hp": 480,
        "boss_power": 130,
        "completion_flag": "FLAG_DESERT_CLEARED"
      },
      "narrative_text": "A roaring caravan echoes discordant rock chords. Etemon challenges you with his Love Serenade!"
    },
    {
      "node_id": 115,
      "name": "Ancient Colosseum",
      "required_steps": 5000,
      "environment_effect": "arena_resonance",
      "encounter_rate": 0.35,
      "encounter_pool": [313, 319, 331],
      "boss_encounter": {
        "boss_monster_id": 331,
        "boss_hp": 400,
        "boss_power": 105,
        "completion_flag": "FLAG_COLOSSEUM_CLEARED"
      },
      "narrative_text": "An amphitheater carved into black stone. Veedramon lunges with supersonic breath!"
    },
    {
      "node_id": 116,
      "name": "Subterranean Magma Caves",
      "required_steps": 5200,
      "environment_effect": "pyroclastic_flow",
      "encounter_rate": 0.40,
      "encounter_pool": [304, 332, 416],
      "boss_encounter": {
        "boss_monster_id": 416,
        "boss_hp": 500,
        "boss_power": 138,
        "completion_flag": "FLAG_MAGMA_CLEARED"
      },
      "narrative_text": "Rivers of molten code heat the cavern walls. Megadramon dives through the smoke!"
    },
    {
      "node_id": 117,
      "name": "Clockwork Iron City",
      "required_steps": 5400,
      "environment_effect": "electromagnetic_pulse",
      "encounter_rate": 0.40,
      "encounter_pool": [310, 418, 419],
      "boss_encounter": {
        "boss_monster_id": 418,
        "boss_hp": 520,
        "boss_power": 142,
        "completion_flag": "FLAG_IRONCITY_CLEARED"
      },
      "narrative_text": "Automated factories churn out cybernetic components. The cyber cyborg Andromon tests your strength!"
    },
    {
      "node_id": 118,
      "name": "Labyrinth of Corrupted Code",
      "required_steps": 5600,
      "environment_effect": "spatial_distortion",
      "encounter_rate": 0.40,
      "encounter_pool": [321, 415, 420],
      "boss_encounter": {
        "boss_monster_id": 420,
        "boss_hp": 530,
        "boss_power": 144,
        "completion_flag": "FLAG_LABYRINTH_CLEARED"
      },
      "narrative_text": "Corrupted geometry loops endlessly. The strange alien brain Vademon attempts to delete your memories!"
    },
    {
      "node_id": 119,
      "name": "Vampiric Shadow Dungeon",
      "required_steps": 5800,
      "environment_effect": "blood_moon_curse",
      "encounter_rate": 0.40,
      "encounter_pool": [303, 428, 430],
      "boss_encounter": {
        "boss_monster_id": 428,
        "boss_hp": 550,
        "boss_power": 150,
        "completion_flag": "FLAG_SHADOW_CLEARED"
      },
      "narrative_text": "Chains rattle within the dark stone keep. LadyDevimon descends with razor-sharp darkness!"
    },
    {
      "node_id": 120,
      "name": "Celestial Floating Sanctum",
      "required_steps": 6000,
      "environment_effect": "aurora_blessing",
      "encounter_rate": 0.35,
      "encounter_pool": [307, 405, 427],
      "boss_encounter": {
        "boss_monster_id": 405,
        "boss_hp": 570,
        "boss_power": 155,
        "completion_flag": "FLAG_SANCTUM_CLEARED"
      },
      "narrative_text": "Floating marble pillars glow with radiant light. MagnaAngemon unsheathes the Excalibur sword!"
    },
    {
      "node_id": 121,
      "name": "Spiral Mountain - Deep Sea Trench",
      "required_steps": 6200,
      "environment_effect": "abyssal_crush",
      "encounter_rate": 0.40,
      "encounter_pool": [324, 409, 533],
      "boss_encounter": {
        "boss_monster_id": 533,
        "boss_hp": 620,
        "boss_power": 180,
        "completion_flag": "FLAG_ABYSS_CLEARED"
      },
      "narrative_text": "The abyssal trench of Spiral Mountain yawns open. The Demon Lord Leviamon emerges from the void!"
    },
    {
      "node_id": 122,
      "name": "Spiral Mountain - Puppet Woods",
      "required_steps": 6400,
      "environment_effect": "marionette_threads",
      "encounter_rate": 0.40,
      "encounter_pool": [315, 402, 412],
      "boss_encounter": {
        "boss_monster_id": 402,
        "boss_hp": 590,
        "boss_power": 165,
        "completion_flag": "FLAG_PUPPET_CLEARED"
      },
      "narrative_text": "Toy trees squeak as invisible strings tighten. Monzaemon unleashes Heartbreak Attack!"
    },
    {
      "node_id": 123,
      "name": "Spiral Mountain - Steel Megalopolis",
      "required_steps": 6600,
      "environment_effect": "giga_radiation",
      "encounter_rate": 0.45,
      "encounter_pool": [414, 502, 518],
      "boss_encounter": {
        "boss_monster_id": 502,
        "boss_hp": 660,
        "boss_power": 195,
        "completion_flag": "FLAG_STEEL_CLEARED"
      },
      "narrative_text": "Smokestacks poison the sky above a metallic city. Machinedramon charges dual Giga Cannons!"
    },
    {
      "node_id": 124,
      "name": "Spiral Mountain - Wasteland of Sin",
      "required_steps": 6800,
      "environment_effect": "hellfire_cataclysm",
      "encounter_rate": 0.45,
      "encounter_pool": [410, 434, 532],
      "boss_encounter": {
        "boss_monster_id": 532,
        "boss_hp": 680,
        "boss_power": 200,
        "completion_flag": "FLAG_SIN_CLEARED"
      },
      "narrative_text": "Crimson flames consume the cursed earth. Demon Lord Daemon unleashes demonic wrath!"
    },
    {
      "node_id": 125,
      "name": "Spiral Mountain - Pinnacle Peak",
      "required_steps": 7000,
      "environment_effect": "chaos_gravity",
      "encounter_rate": 0.45,
      "encounter_pool": [509, 525, 526],
      "boss_encounter": {
        "boss_monster_id": 509,
        "boss_hp": 700,
        "boss_power": 205,
        "completion_flag": "FLAG_PINNACLE_CLEARED"
      },
      "narrative_text": "The apex of Spiral Mountain reaches the void. Piedmon draws four Trump Swords with eerie laughter!"
    },
    {
      "node_id": 126,
      "name": "The Wall of Fire & Apocalypse",
      "required_steps": 7500,
      "environment_effect": "cosmic_nihilism",
      "encounter_rate": 0.45,
      "encounter_pool": [403, 527, 529],
      "boss_encounter": {
        "boss_monster_id": 529,
        "boss_hp": 740,
        "boss_power": 215,
        "completion_flag": "FLAG_WALL_FIRE_CLEARED"
      },
      "narrative_text": "Space and time shatter beyond the firewall. Apocalymon laments in eternal despair!"
    },
    {
      "node_id": 127,
      "name": "Royal Knights: Bastion of Light",
      "required_steps": 8000,
      "environment_effect": "sacred_chivalry",
      "encounter_rate": 0.45,
      "encounter_pool": [521, 540, 541],
      "boss_encounter": {
        "boss_monster_id": 541,
        "boss_hp": 720,
        "boss_power": 210,
        "completion_flag": "FLAG_CHIVALRY_CLEARED"
      },
      "narrative_text": "Crystal spires guard the sacred realm. Royal Knight LordKnightmon initiates the trial of duty!"
    },
    {
      "node_id": 128,
      "name": "Digital Kernel: Citadel of Evolution",
      "required_steps": 8500,
      "environment_effect": "matrix_ascension",
      "encounter_rate": 0.45,
      "encounter_pool": [513, 515, 516],
      "boss_encounter": {
        "boss_monster_id": 516,
        "boss_hp": 760,
        "boss_power": 220,
        "completion_flag": "FLAG_KERNEL_CLEARED"
      },
      "narrative_text": "The core of all Digital Monster data unfolds. Gallantmon Crimson Mode raises the Gungnir lance!"
    },
    {
      "node_id": 129,
      "name": "Chrono Rupture: Alpha Dimension",
      "required_steps": 9000,
      "environment_effect": "temporal_singularity",
      "encounter_rate": 0.50,
      "encounter_pool": [522, 523, 524],
      "boss_encounter": {
        "boss_monster_id": 524,
        "boss_hp": 800,
        "boss_power": 230,
        "completion_flag": "FLAG_ALPHA_CLEARED"
      },
      "narrative_text": "Time halts. Alphamon Ouryuken commands the supreme blade of empty space!"
    },
    {
      "node_id": 130,
      "name": "Dimensional Zero: The Final Horizon",
      "required_steps": 10000,
      "environment_effect": "quantum_erasure",
      "encounter_rate": 0.50,
      "encounter_pool": [510, 535, 548, 549],
      "boss_encounter": {
        "boss_monster_id": 548,
        "boss_hp": 880,
        "boss_power": 250,
        "completion_flag": "FLAG_GENESIS_CLEARED"
      },
      "narrative_text": "Reality unravels at the edge of the multiverse. The bound god ZeedMillenniummon threatens existence itself!"
    }
  ]
}
"""

    fun parseCampaign(jsonString: String): CampaignStory {
        return jsonParser.decodeFromString<CampaignStory>(jsonString)
    }

    /**
     * Adds physical [addedSteps] to the adventure journey.
     */
    fun processSteps(
        story: CampaignStory,
        progress: AdventureProgress,
        addedSteps: Int,
        rng: Random = Random()
    ): AdventureStepResult {
        if (progress.isCompleted || addedSteps <= 0) {
            return AdventureStepResult(progress)
        }

        val nodes = story.nodes
        val currentIdx = progress.currentNodeIndex
        if (currentIdx >= nodes.size) {
            return AdventureStepResult(progress.copy(isCompleted = true), campaignCompleted = true)
        }

        val currentNode = nodes[currentIdx]
        val newAccumulatedSteps = progress.accumulatedStepsCurrentNode + addedSteps
        val newTotalSteps = progress.totalAdventureSteps + addedSteps

        // Check if node step goal reached
        if (newAccumulatedSteps >= currentNode.requiredSteps) {
            // Check for boss encounter
            if (currentNode.bossEncounter != null &&
                !progress.clearedFlags.contains(currentNode.bossEncounter.completionFlag)
            ) {
                // Must defeat boss to clear node!
                return AdventureStepResult(
                    updatedProgress = progress.copy(
                        accumulatedStepsCurrentNode = currentNode.requiredSteps,
                        totalAdventureSteps = newTotalSteps
                    ),
                    bossEncounter = currentNode.bossEncounter,
                    narrativeEvent = currentNode.narrativeText
                )
            }

            // Move to next node
            val nextIdx = currentIdx + 1
            val completed = nextIdx >= nodes.size
            val updatedProgress = progress.copy(
                currentNodeIndex = nextIdx,
                accumulatedStepsCurrentNode = 0,
                totalAdventureSteps = newTotalSteps,
                isCompleted = completed
            )

            return AdventureStepResult(
                updatedProgress = updatedProgress,
                nodeCleared = true,
                campaignCompleted = completed,
                narrativeEvent = if (!completed) nodes[nextIdx].narrativeText else "Campaign Completed! All 30 sectors restored."
            )
        }

        // Within node: roll for random encounter
        var encounterMonsterId: Int? = null
        if (currentNode.encounterPool.isNotEmpty() && rng.nextFloat() < currentNode.encounterRate) {
            encounterMonsterId = currentNode.encounterPool[rng.nextInt(currentNode.encounterPool.size)]
        }

        return AdventureStepResult(
            updatedProgress = progress.copy(
                accumulatedStepsCurrentNode = newAccumulatedSteps,
                totalAdventureSteps = newTotalSteps
            ),
            randomEncounterMonsterId = encounterMonsterId
        )
    }

    /**
     * Records a boss victory and marks completion flag.
     */
    fun recordBossVictory(
        story: CampaignStory,
        progress: AdventureProgress,
        boss: BossEncounter
    ): AdventureProgress {
        val newFlags = progress.clearedFlags + boss.completionFlag
        val nextIdx = progress.currentNodeIndex + 1
        val completed = nextIdx >= story.nodes.size

        return progress.copy(
            currentNodeIndex = nextIdx,
            accumulatedStepsCurrentNode = 0,
            clearedFlags = newFlags,
            isCompleted = completed
        )
    }
}
