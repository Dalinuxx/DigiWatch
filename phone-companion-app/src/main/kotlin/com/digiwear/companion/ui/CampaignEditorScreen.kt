package com.digiwear.companion.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.digiwear.companion.data.CustomCampaignEntity
import com.digiwear.companion.data.PhoneDatabase
import com.digiwear.companion.datalayer.PhoneWearableManager
import com.digiwear.companion.render.SpriteCanvasRenderer
import com.digiwear.core.adventure.AdventureEngine
import com.digiwear.core.model.CampaignStory
import com.digiwear.core.model.PetSpecies
import com.digiwear.core.model.SpriteFrame
import kotlinx.coroutines.launch

@Composable
fun CampaignEditorScreen(
    db: PhoneDatabase,
    wearableManager: PhoneWearableManager
) {
    var rawJson by remember { mutableStateOf(AdventureEngine.DEFAULT_CAMPAIGN_JSON.trim()) }
    var parsedStory by remember { mutableStateOf<CampaignStory?>(null) }
    var parseError by remember { mutableStateOf<String?>(null) }
    var deployStatus by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val builtInSagas = remember { AdventureEngine.getBuiltInCampaigns() }

    // Parse on start
    LaunchedEffect(rawJson) {
        try {
            parsedStory = AdventureEngine.parseCampaign(rawJson)
            parseError = null
        } catch (e: Exception) {
            parseError = "JSON Schema Error: ${e.message}"
            parsedStory = null
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DigiWearPalette.CyberBg)
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DigiSectionHeader(
            tag = "ADVENTURE QUEST // CANONICAL ARCHITECT",
            title = "Digital World Expedition Pipeline",
            subtitle = "Select from 5 Grand Canonical Sagas or author custom step campaigns",
            accentColor = Color(0xFF26A69A)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Canonical Sagas Quick Presets Carousel
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = DigiWearPalette.CardSurface),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, DigiWearPalette.CardBorder)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "▸ 5 GRAND CANONICAL ADVENTURE SAGAS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF80CBC4),
                    fontFamily = FontFamily.Monospace
                )
                Text(
                    text = "Choose an authentic saga incorporating the full 500-species roster and deploy to your watch:",
                    fontSize = 10.sp,
                    color = Color(0xFF90A4AE)
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    builtInSagas.forEach { saga ->
                        val isCurrent = parsedStory?.campaignId.equals(saga.campaignId, ignoreCase = true)
                        Card(
                            modifier = Modifier
                                .width(220.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isCurrent) Color(0xFF004D40) else DigiWearPalette.CardInner
                            ),
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(
                                1.dp,
                                if (isCurrent) Color(0xFF64FFDA) else Color(0xFF263238)
                            )
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = saga.title,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isCurrent) Color(0xFF64FFDA) else Color.White,
                                    maxLines = 2
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "${saga.nodes.size} Sectors • ${saga.recommendedStage}",
                                    fontSize = 9.sp,
                                    color = Color(0xFF80CBC4),
                                    fontFamily = FontFamily.Monospace
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = saga.description,
                                    fontSize = 9.sp,
                                    color = Color(0xFFB0BEC5),
                                    maxLines = 3
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Button(
                                    onClick = {
                                        rawJson = AdventureEngine.serializeCampaign(saga)
                                    },
                                    modifier = Modifier.fillMaxWidth().height(28.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (isCurrent) Color(0xFF00897B) else Color(0xFF263238)
                                    )
                                ) {
                                    Text(
                                        text = if (isCurrent) "LOADED ✓" else "LOAD SAGA",
                                        fontSize = 9.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Visual Campaign Node Inspector
        parsedStory?.let { story ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = DigiWearPalette.CardSurface),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, DigiWearPalette.CardBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "▸ ROADMAP: ${story.title.uppercase()}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF80CBC4),
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = "ID: ${story.campaignId} • Total Sectors: ${story.nodes.size} • Rec: ${story.recommendedStage}",
                        fontSize = 10.sp,
                        color = Color(0xFF90A4AE),
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    story.nodes.forEachIndexed { index, node ->
                        val actLabel = node.actTitle.ifBlank { "SECTOR ${index + 1}" }
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            colors = CardDefaults.cardColors(containerColor = DigiWearPalette.CardInner),
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, Color(0xFF222E3C))
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = actLabel,
                                            fontSize = 8.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF26A69A),
                                            fontFamily = FontFamily.Monospace
                                        )
                                        Text(
                                            text = "Sector ${index + 1}: ${node.name}",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    }
                                    Box(
                                        modifier = Modifier
                                            .background(Color(0xFF263238), RoundedCornerShape(6.dp))
                                            .padding(horizontal = 8.dp, vertical = 3.dp)
                                    ) {
                                        Text(
                                            text = "${node.requiredSteps} STEPS",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = DigiWearPalette.CrestGold,
                                            fontFamily = FontFamily.Monospace
                                        )
                                    }
                                }

                                Text(
                                    text = "\"${node.narrativeText}\"",
                                    fontSize = 11.sp,
                                    color = Color(0xFFB0BEC5),
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )

                                Text(
                                    text = "Env: ${node.environmentEffect} • Encounter Rate: ${(node.encounterRate * 100).toInt()}%",
                                    fontSize = 9.sp,
                                    color = Color(0xFF78909C),
                                    fontFamily = FontFamily.Monospace
                                )

                                // Encounter Pool Mini Sprites
                                if (node.encounterPool.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "WILD:",
                                            fontSize = 8.sp,
                                            color = Color.Gray,
                                            fontFamily = FontFamily.Monospace
                                        )
                                        node.encounterPool.take(6).forEach { encId ->
                                            Box(
                                                modifier = Modifier
                                                    .size(24.dp)
                                                    .background(Color(0xFF161F28), RoundedCornerShape(4.dp))
                                                    .padding(2.dp)
                                            ) {
                                                SpriteCanvasRenderer(
                                                    speciesId = encId,
                                                    frame = SpriteFrame.IDLE_A,
                                                    size = 20.dp
                                                )
                                            }
                                        }
                                    }
                                }

                                node.bossEncounter?.let { boss ->
                                    val bossSpecies = PetSpecies.getById(boss.bossMonsterId)
                                    val bossDisplayName = boss.bossName.ifBlank { bossSpecies.name }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(Color(0xFF2C1518), RoundedCornerShape(8.dp))
                                            .border(1.dp, Color(0xFFEF5350), RoundedCornerShape(8.dp))
                                            .padding(8.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            SpriteCanvasRenderer(
                                                speciesId = bossSpecies.id,
                                                frame = SpriteFrame.IDLE_A,
                                                size = 32.dp
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Column {
                                                Text(
                                                    text = "☠ BOSS: ${bossDisplayName.uppercase()}",
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Black,
                                                    color = Color(0xFFFF8A80),
                                                    fontFamily = FontFamily.Monospace
                                                )
                                                if (boss.introDialogue.isNotBlank()) {
                                                    Text(
                                                        text = "\"${boss.introDialogue}\"",
                                                        fontSize = 8.5.sp,
                                                        color = Color(0xFFFFCCBC)
                                                    )
                                                }
                                                Text(
                                                    text = "HP: ${boss.bossHp} • PWR: ${boss.bossPower} • ${boss.completionFlag}",
                                                    fontSize = 8.5.sp,
                                                    color = Color(0xFFCFD8DC),
                                                    fontFamily = FontFamily.Monospace
                                                )
                                                if (boss.rewardItem.isNotBlank() || boss.rewardEggId != null) {
                                                    val rewardStr = buildString {
                                                        if (boss.rewardEggId != null) append("🥚 Unlocks Egg #${boss.rewardEggId} • ")
                                                        if (boss.rewardItem.isNotBlank()) append(boss.rewardItem)
                                                    }
                                                    Text(
                                                        text = "🎁 $rewardStr",
                                                        fontSize = 8.sp,
                                                        color = Color(0xFFFFD54F),
                                                        fontFamily = FontFamily.Monospace
                                                    )
                                                }
                                            }
                                        }
                                        StageBadge(bossSpecies.stage.displayName)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Raw JSON Story Editor
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = DigiWearPalette.CardSurface),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, DigiWearPalette.CardBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "▸ JSON STORY SCHEMA EDITOR",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF80CBC4),
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = rawJson,
                    onValueChange = { rawJson = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp),
                    textStyle = androidx.compose.ui.text.TextStyle(
                        fontFamily = FontFamily.Monospace,
                        fontSize = 10.sp,
                        color = Color(0xFFE0E0E0)
                    )
                )

                parseError?.let {
                    Text(
                        text = it,
                        fontSize = 11.sp,
                        color = Color(0xFFFF5252),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedButton(
                        onClick = {
                            rawJson = AdventureEngine.DEFAULT_CAMPAIGN_JSON.trim()
                        }
                    ) {
                        Text("RESET DEFAULT", fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                    }

                    Button(
                        onClick = {
                            scope.launch {
                                parsedStory?.let { story ->
                                    db.phoneDao().insertOrUpdateCampaign(
                                        CustomCampaignEntity(
                                            campaignId = story.campaignId,
                                            title = story.title,
                                            jsonContent = rawJson
                                        )
                                    )
                                    val ok = wearableManager.sendCampaignToWatch(rawJson)
                                    deployStatus = if (ok) {
                                        "Campaign '${story.title}' deployed to Smartwatch!"
                                    } else {
                                        "Campaign saved locally (Smartwatch offline)"
                                    }
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00897B))
                    ) {
                        Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Deploy", modifier = Modifier.size(15.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("DEPLOY TO WATCH", fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                    }
                }
            }
        }

        deployStatus?.let {
            Text(
                text = "▸ $it",
                fontSize = 11.sp,
                color = Color(0xFF00E676),
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}
