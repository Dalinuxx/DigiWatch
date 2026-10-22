package com.digiwear.core.adventure

import com.digiwear.core.model.AdventureProgress
import com.digiwear.core.model.BossEncounter
import com.digiwear.core.model.CampaignNode
import com.digiwear.core.model.CampaignStory
import com.digiwear.core.model.SagaProgressRecord
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
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
            val newHighest = maxOf(progress.highestUnlockedNodeIndex, nextIdx.coerceAtMost(nodes.lastIndex))
            val newCampaigns = if (completed) progress.unlockedCampaigns + story.campaignId else progress.unlockedCampaigns
            val updatedProgress = progress.copy(
                currentNodeIndex = nextIdx.coerceAtMost(nodes.lastIndex),
                highestUnlockedNodeIndex = newHighest,
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
        val newHighest = maxOf(progress.highestUnlockedNodeIndex, nextIdx.coerceAtMost(story.nodes.lastIndex))
        val newCampaigns = if (completed) progress.unlockedCampaigns + story.campaignId else progress.unlockedCampaigns

        return progress.copy(
            currentNodeIndex = nextIdx.coerceAtMost(story.nodes.lastIndex),
            highestUnlockedNodeIndex = newHighest,
            accumulatedStepsCurrentNode = 0,
            clearedFlags = newFlags,
            unlockedEggs = newEggs,
            collectedItems = newItems,
            unlockedCampaigns = newCampaigns,
            isCompleted = completed
        )
    }

    /**
     * Seamlessly swaps the active adventure campaign without losing progress.
     * Snapshots the outgoing campaign's node index, unlocked sectors, steps, and flags into [sagaStates],
     * and restores the incoming campaign's previously saved state (or initializes node 0 if new).
     */
    fun switchCampaign(
        currentProgress: AdventureProgress,
        newStory: CampaignStory
    ): AdventureProgress {
        if (currentProgress.campaignId.equals(newStory.campaignId, ignoreCase = true)) {
            return currentProgress
        }

        // 1. Snapshot outgoing active saga
        val currentSnapshot = SagaProgressRecord(
            campaignId = currentProgress.campaignId,
            currentNodeIndex = currentProgress.currentNodeIndex,
            highestUnlockedNodeIndex = currentProgress.highestUnlockedNodeIndex,
            accumulatedStepsCurrentNode = currentProgress.accumulatedStepsCurrentNode,
            clearedFlags = currentProgress.clearedFlags,
            isCompleted = currentProgress.isCompleted
        )
        val updatedSagas = currentProgress.sagaStates + (currentProgress.campaignId to currentSnapshot)

        // 2. Restore incoming saga if already exists, or start fresh at Sector 1 (index 0)
        val existing = updatedSagas[newStory.campaignId]
        val maxTargetIdx = (newStory.nodes.size - 1).coerceAtLeast(0)
        return if (existing != null) {
            currentProgress.copy(
                campaignId = newStory.campaignId,
                currentNodeIndex = existing.currentNodeIndex.coerceIn(0, maxTargetIdx),
                highestUnlockedNodeIndex = existing.highestUnlockedNodeIndex.coerceIn(0, maxTargetIdx),
                accumulatedStepsCurrentNode = existing.accumulatedStepsCurrentNode,
                clearedFlags = existing.clearedFlags,
                isCompleted = existing.isCompleted,
                sagaStates = updatedSagas
            )
        } else {
            currentProgress.copy(
                campaignId = newStory.campaignId,
                currentNodeIndex = 0,
                highestUnlockedNodeIndex = 0,
                accumulatedStepsCurrentNode = 0,
                clearedFlags = emptySet(),
                isCompleted = false,
                sagaStates = updatedSagas
            )
        }
    }

    /**
     * Changes the active playable sector within the current campaign to [targetIndex].
     * The player can freely choose any sector between 0 and [highestUnlockedNodeIndex].
     */
    fun selectNode(
        progress: AdventureProgress,
        targetIndex: Int,
        story: CampaignStory
    ): AdventureProgress {
        val maxUnlocked = progress.highestUnlockedNodeIndex.coerceIn(0, (story.nodes.size - 1).coerceAtLeast(0))
        val validTarget = targetIndex.coerceIn(0, maxUnlocked)
        if (validTarget == progress.currentNodeIndex) {
            return progress
        }
        return progress.copy(
            currentNodeIndex = validTarget,
            accumulatedStepsCurrentNode = 0
        )
    }

    /**
     * Serializes [sagaStates] map to a JSON string for persistent database storage.
     */
    fun serializeSagaStates(sagaStates: Map<String, SagaProgressRecord>): String =
        try {
            jsonParser.encodeToString(
                MapSerializer(String.serializer(), SagaProgressRecord.serializer()),
                sagaStates
            )
        } catch (_: Exception) {
            "{}"
        }

    /**
     * Deserializes a persistent JSON string into a [sagaStates] map.
     */
    fun parseSagaStates(jsonStr: String): Map<String, SagaProgressRecord> =
        if (jsonStr.isBlank() || jsonStr == "{}") emptyMap()
        else try {
            jsonParser.decodeFromString(
                MapSerializer(String.serializer(), SagaProgressRecord.serializer()),
                jsonStr
            )
        } catch (_: Exception) {
            emptyMap()
        }
}
