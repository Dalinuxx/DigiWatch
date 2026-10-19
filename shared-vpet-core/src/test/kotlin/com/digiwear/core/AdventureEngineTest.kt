package com.digiwear.core

import com.digiwear.core.adventure.AdventureEngine
import com.digiwear.core.model.AdventureProgress
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Random

class AdventureEngineTest {

    @Test
    fun testParseAdventureCampaignJson() {
        val story = AdventureEngine.parseCampaign(AdventureEngine.DEFAULT_CAMPAIGN_JSON)
        assertEquals("file_island_corrupted_sector", story.campaignId)
        assertEquals(30, story.nodes.size)

        val node1 = story.nodes[0]
        assertEquals(101, node1.nodeId)
        assertEquals("Binary Coastline", node1.name)
        assertEquals(400, node1.requiredSteps)

        val node3 = story.nodes[2]
        assertNotNull(node3.bossEncounter)
        assertEquals(312, node3.bossEncounter!!.bossMonsterId)
        assertEquals(240, node3.bossEncounter!!.bossHp)

        val node30 = story.nodes[29]
        assertEquals(130, node30.nodeId)
        assertEquals("Dimensional Zero: The Final Horizon", node30.name)
        assertEquals(10000, node30.requiredSteps)
        assertNotNull(node30.bossEncounter)
        assertEquals(548, node30.bossEncounter!!.bossMonsterId)
        assertEquals(880, node30.bossEncounter!!.bossHp)
    }

    @Test
    fun testStepProgressionAndNodeClear() {
        val story = AdventureEngine.parseCampaign(AdventureEngine.DEFAULT_CAMPAIGN_JSON)
        var progress = AdventureProgress()

        // Add 200 steps (not enough to clear node 1, required: 400)
        val res1 = AdventureEngine.processSteps(story, progress, 200, Random(42))
        assertEquals(0, res1.updatedProgress.currentNodeIndex)
        assertEquals(200, res1.updatedProgress.accumulatedStepsCurrentNode)
        assertEquals(false, res1.nodeCleared)

        // Add 250 more steps (total 450 >= 400) -> Node 1 cleared! Advances to Node 2
        val res2 = AdventureEngine.processSteps(story, res1.updatedProgress, 250, Random(42))
        assertEquals(1, res2.updatedProgress.currentNodeIndex)
        assertEquals(0, res2.updatedProgress.accumulatedStepsCurrentNode)
        assertEquals(true, res2.nodeCleared)
        assertEquals(450, res2.updatedProgress.totalAdventureSteps)
    }

    @Test
    fun testBossEncounterGate() {
        val story = AdventureEngine.parseCampaign(AdventureEngine.DEFAULT_CAMPAIGN_JSON)
        // Position at Node 3 (index 2: Overclocked Jungle requires 1200 steps and has boss Ogremon)
        val atNode3 = AdventureProgress(
            currentNodeIndex = 2,
            accumulatedStepsCurrentNode = 1000,
            totalAdventureSteps = 2200
        )

        // Add 300 steps -> reaches 1200 step threshold -> triggers boss!
        val res = AdventureEngine.processSteps(story, atNode3, 300, Random(42))
        assertNotNull(res.bossEncounter)
        assertEquals(312, res.bossEncounter!!.bossMonsterId)
        // Does not advance past node 3 until boss is defeated
        assertEquals(2, res.updatedProgress.currentNodeIndex)

        // Defeat boss
        val afterBoss = AdventureEngine.recordBossVictory(story, res.updatedProgress, res.bossEncounter!!)
        assertEquals(3, afterBoss.currentNodeIndex)
        assertTrue(afterBoss.clearedFlags.contains("FLAG_JUNGLE_CLEARED"))
    }
}
