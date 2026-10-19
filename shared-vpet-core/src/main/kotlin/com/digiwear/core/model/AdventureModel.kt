package com.digiwear.core.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BossEncounter(
    @SerialName("boss_monster_id")
    val bossMonsterId: Int,
    @SerialName("boss_hp")
    val bossHp: Int,
    @SerialName("boss_power")
    val bossPower: Int,
    @SerialName("completion_flag")
    val completionFlag: String = "",
    @SerialName("boss_name")
    val bossName: String = "",
    @SerialName("intro_dialogue")
    val introDialogue: String = "",
    @SerialName("defeat_dialogue")
    val defeatDialogue: String = "",
    @SerialName("reward_item")
    val rewardItem: String = "",
    @SerialName("reward_egg_id")
    val rewardEggId: Int? = null
)

@Serializable
data class CampaignNode(
    @SerialName("node_id")
    val nodeId: Int,
    val name: String,
    @SerialName("required_steps")
    val requiredSteps: Int,
    @SerialName("environment_effect")
    val environmentEffect: String = "clear",
    @SerialName("encounter_rate")
    val encounterRate: Float = 0.20f,
    @SerialName("encounter_pool")
    val encounterPool: List<Int> = emptyList(),
    @SerialName("boss_encounter")
    val bossEncounter: BossEncounter? = null,
    @SerialName("narrative_text")
    val narrativeText: String,
    @SerialName("act_title")
    val actTitle: String = "",
    @SerialName("sub_area")
    val subArea: String = ""
)

@Serializable
data class CampaignStory(
    @SerialName("campaign_id")
    val campaignId: String,
    val title: String,
    val nodes: List<CampaignNode>,
    @SerialName("description")
    val description: String = "",
    @SerialName("recommended_stage")
    val recommendedStage: String = "",
    @SerialName("banner_theme")
    val bannerTheme: String = ""
)

@Serializable
data class AdventureProgress(
    val campaignId: String = "file_island_corrupted_sector",
    val currentNodeIndex: Int = 0,
    val accumulatedStepsCurrentNode: Int = 0,
    val totalAdventureSteps: Int = 0,
    val clearedFlags: Set<String> = emptySet(),
    val isCompleted: Boolean = false,
    val unlockedEggs: Set<Int> = emptySet(),
    val collectedItems: Set<String> = emptySet(),
    val unlockedCampaigns: Set<String> = emptySet()
)
