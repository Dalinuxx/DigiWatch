package com.digiwear.core.adventure

import com.digiwear.core.model.AdventureProgress
import com.digiwear.core.model.BossEncounter
import com.digiwear.core.model.CampaignNode
import com.digiwear.core.model.CampaignStory
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.Random

data class AdventureStepResult(
    val updatedProgress: AdventureProgress,
    val nodeCleared: Boolean = false,
    val campaignCompleted: Boolean = false,
    val randomEncounterMonsterId: Int? = null,
    val bossEncounter: BossEncounter? = null,
    val narrativeEvent: String? = null,
    val rewardItem: String? = null,
    val rewardEggId: Int? = null
)

/**
 * Adventure Mode narrative campaign processor driven by physical step counting.
 * Expanded to support all 5 Grand Canonical Sagas in DigiWear:
 *
 * 1. The Original Odyssey: File Island to the Dark Masters (30 Sectors)
 * 2. Project Ark: The X-Antibody Crisis & Digital Monster X (25 Sectors)
 * 3. Frontier of the Ancients: Spirits of the Ten Warriors (20 Sectors)
 * 4. Catastrophe of the Seven Sins: The Dark Area Awakening (21 Sectors)
 * 5. Ghost Crypt: Hologram GRB Wave & The Quantum Void (18 Sectors)
 */
object AdventureEngine {

    val jsonParser = Json {
        ignoreUnknownKeys = true
        isLenient = true
        prettyPrint = true
    }

    /**
     * Default canonical campaign JSON (File Island Odyssey).
     */
    val DEFAULT_CAMPAIGN_JSON: String by lazy {
        jsonParser.encodeToString(CampaignStoryRegistry.FILE_ISLAND_ODYSSEY)
    }

    fun parseCampaign(jsonString: String): CampaignStory {
        return jsonParser.decodeFromString<CampaignStory>(jsonString)
    }

    fun serializeCampaign(story: CampaignStory): String {
        return jsonParser.encodeToString(story)
    }

    /**
     * Returns all 5 built-in canonical sagas.
     */
    fun getBuiltInCampaigns(): List<CampaignStory> = CampaignStoryRegistry.ALL_CAMPAIGNS

    /**
     * Retrieves a built-in saga by ID, or null if custom.
     */
    fun getCampaignById(id: String): CampaignStory? = CampaignStoryRegistry.getById(id)

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
            val newCampaigns = if (completed) progress.unlockedCampaigns + story.campaignId else progress.unlockedCampaigns
            val updatedProgress = progress.copy(
                currentNodeIndex = nextIdx,
                accumulatedStepsCurrentNode = 0,
                totalAdventureSteps = newTotalSteps,
                unlockedCampaigns = newCampaigns,
                isCompleted = completed
            )

            return AdventureStepResult(
                updatedProgress = updatedProgress,
                nodeCleared = true,
                campaignCompleted = completed,
                narrativeEvent = if (!completed) nodes[nextIdx].narrativeText else "Campaign Completed! All sectors of '${story.title}' restored.",
                rewardItem = currentNode.bossEncounter?.rewardItem?.takeIf { it.isNotBlank() },
                rewardEggId = currentNode.bossEncounter?.rewardEggId
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
     * Records a boss victory, unlocks milestone rewards, and marks completion flag.
     */
    fun recordBossVictory(
        story: CampaignStory,
        progress: AdventureProgress,
        boss: BossEncounter
    ): AdventureProgress {
        if (progress.isCompleted || progress.currentNodeIndex !in story.nodes.indices) {
            return progress
        }
        val newFlags = progress.clearedFlags + boss.completionFlag
        val newEggs = if (boss.rewardEggId != null) progress.unlockedEggs + boss.rewardEggId else progress.unlockedEggs
        val newItems = if (boss.rewardItem.isNotBlank()) progress.collectedItems + boss.rewardItem else progress.collectedItems
        val nextIdx = progress.currentNodeIndex + 1
        val completed = nextIdx >= story.nodes.size
        val newCampaigns = if (completed) progress.unlockedCampaigns + story.campaignId else progress.unlockedCampaigns

        return progress.copy(
            currentNodeIndex = nextIdx,
            accumulatedStepsCurrentNode = 0,
            clearedFlags = newFlags,
            unlockedEggs = newEggs,
            collectedItems = newItems,
            unlockedCampaigns = newCampaigns,
            isCompleted = completed
        )
    }
}
