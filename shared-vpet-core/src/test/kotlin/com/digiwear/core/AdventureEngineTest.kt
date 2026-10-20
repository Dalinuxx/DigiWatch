package com.digiwear.core

import com.digiwear.core.adventure.AdventureEngine
import com.digiwear.core.adventure.CampaignStoryRegistry
import com.digiwear.core.model.AdventureProgress
import com.digiwear.core.model.BossEncounter
import com.digiwear.core.model.PetSpecies
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
    fun testAllFiveBuiltInCampaignsAreValidAndReferenceRegistered500Species() {
        val campaigns = AdventureEngine.getBuiltInCampaigns()
        assertEquals(5, campaigns.size)

        val expectedIds = setOf(
            "file_island_corrupted_sector",
            "project_ark_dmx",
            "frontier_ten_warriors",
            "seven_demon_lords",
            "ghost_game_crypt"
        )
        val actualIds = campaigns.map { it.campaignId }.toSet()
        assertEquals(expectedIds, actualIds)

        val missingList = mutableListOf<String>()
        var totalSectors = 0
        for (campaign in campaigns) {
            assertTrue("Title must not be empty", campaign.title.isNotBlank())
            assertTrue("Description must not be empty", campaign.description.isNotBlank())
            assertTrue("Recommended stage must not be empty", campaign.recommendedStage.isNotBlank())
            assertTrue("Nodes must not be empty", campaign.nodes.isNotEmpty())
            totalSectors += campaign.nodes.size

            for (node in campaign.nodes) {
                assertTrue("Node ${node.name} steps must be > 0", node.requiredSteps > 0)
                assertTrue("Node ${node.name} must have narrative text", node.narrativeText.isNotBlank())

                // Check encounter pool species exist in 500-species registry
                for (id in node.encounterPool) {
                    val sp = PetSpecies.SPECIES_REGISTRY[id]
                    if (sp == null) {
                        missingList.add("MISSING ENCOUNTER: campaign=${campaign.campaignId}, node=${node.nodeId} (${node.name}), id=$id")
                    }
                }

                // Check boss encounter
                node.bossEncounter?.let { boss ->
                    val bossSp = PetSpecies.SPECIES_REGISTRY[boss.bossMonsterId]
                    if (bossSp == null) {
                        missingList.add("MISSING BOSS: campaign=${campaign.campaignId}, node=${node.nodeId} (${node.name}), bossMonsterId=${boss.bossMonsterId}, bossName=${boss.bossName}")
                    } else if (!bossSp.name.equals(boss.bossName, ignoreCase = true)) {
                        missingList.add("MISMATCH in ${campaign.campaignId} Node ${node.nodeId} (${node.name}): BossEncounter name='${boss.bossName}' vs SPECIES_REGISTRY[${boss.bossMonsterId}]='${bossSp.name}'")
                    }
                    assertTrue("Boss HP must be > 0", boss.bossHp > 0)
                    assertTrue("Boss Power must be > 0", boss.bossPower > 0)
                    assertTrue("Boss completion flag must not be blank", boss.completionFlag.isNotBlank())

                    boss.rewardEggId?.let { eggId ->
                        val eggSp = PetSpecies.SPECIES_REGISTRY[eggId]
                        if (eggSp == null) {
                            missingList.add("MISSING EGG: campaign=${campaign.campaignId}, node=${node.nodeId} (${node.name}), rewardEggId=$eggId")
                        }
                    }
                }
            }
        }
        if (missingList.isNotEmpty()) {
            org.junit.Assert.fail("Missing species found in campaigns:\n" + missingList.joinToString("\n"))
        }

        // 30 (Odyssey) + 25 (Project Ark) + 20 (Frontier) + 21 (Demon Lords) + 18 (Ghost Game) = 114 total sectors!
        assertEquals(114, totalSectors)
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
    fun testBossEncounterGateAndRewardsCollection() {
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
        assertTrue("Must collect boss reward item", afterBoss.collectedItems.contains("CREST_OF_COURAGE_FRAGMENT"))
    }

    @Test
    fun testProjectArkSagaEggUnlocks() {
        val saga = CampaignStoryRegistry.PROJECT_ARK_DMX
        val node6 = saga.nodes[5] // Versandi Core Gateway has GranKuwagamon boss rewarding Dokimon Egg 1108
        val boss = node6.bossEncounter!!
        assertEquals(1108, boss.rewardEggId)

        val progress = AdventureProgress(campaignId = saga.campaignId, currentNodeIndex = 5)
        val afterBoss = AdventureEngine.recordBossVictory(saga, progress, boss)
        assertTrue("Must unlock Dokimon Egg 1108", afterBoss.unlockedEggs.contains(1108))
    }
}
