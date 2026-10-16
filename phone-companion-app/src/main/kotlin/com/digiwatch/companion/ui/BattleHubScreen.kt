package com.digiwatch.companion.ui

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.LaunchedEffect
import com.digiwatch.companion.NfcBattleBridge
import com.digiwatch.companion.nfc.NfcDuelManager
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.digiwatch.companion.datalayer.PhoneWearableManager
import com.digiwatch.companion.render.SpriteCanvasRenderer
import com.digiwatch.companion.widget.DigiWatchAppWidgetProvider
import com.digiwatch.core.battle.DeterministicBattleSolver
import com.digiwatch.core.codec.CommCodeCodec
import com.digiwatch.core.codec.CommCodePayload
import com.digiwatch.core.fusion.JogressEngine
import com.digiwatch.core.model.BattleFighter
import com.digiwatch.core.model.BattleResult
import com.digiwatch.core.model.ElementalAttribute
import com.digiwatch.core.model.PetSpecies
import com.digiwatch.core.model.PetStage
import com.digiwatch.core.model.PetState
import com.digiwatch.core.model.SpriteFrame
import kotlinx.coroutines.launch
import kotlin.random.Random

private val FEATURED_FIGHTERS = listOf(201, 301, 304, 401, 501, 503, 515, 227, 439)

@Composable
fun BattleHubScreen(wearableManager: PhoneWearableManager) {
    var ownMonsterId by remember { mutableIntStateOf(301) } // Greymon
    var ownSeed by remember { mutableIntStateOf(0x1337) }
    var generatedCode by remember { mutableStateOf("") }
    var generatedDigiRom by remember { mutableStateOf("") }
    var inputCode by remember { mutableStateOf("") }
    var decodedOpponent by remember { mutableStateOf<CommCodePayload?>(null) }
    var validationError by remember { mutableStateOf<String?>(null) }
    var battleResult by remember { mutableStateOf<BattleResult?>(null) }
    var beamStatus by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    val ownSpecies = remember(ownMonsterId) { PetSpecies.getById(ownMonsterId) }

    LaunchedEffect(Unit) {
        NfcBattleBridge.incomingCodeFlow.collect { scannedCode ->
            if (!scannedCode.isNullOrBlank()) {
                inputCode = scannedCode
                validationError = null
                decodedOpponent = null
                beamStatus = "📶 Opponent scanned via NFC Tap-to-Duel!"
                NfcBattleBridge.incomingCodeFlow.value = null
            }
        }
    }

    // Generate own code on load / change
    remember(ownMonsterId, ownSeed) {
        val species = PetSpecies.getById(ownMonsterId)
        generatedCode = CommCodeCodec.encode128(
            monsterId = ownMonsterId,
            stage = species.stage,
            attribute = species.attribute,
            basePowerHp = species.basePower,
            historicalVictories = 12,
            trainingEffort = 4,
            battleRngSeed = ownSeed
        )
        generatedDigiRom = CommCodeCodec.encodeDigiRom(
            monsterId = ownMonsterId,
            stage = species.stage,
            attribute = species.attribute,
            basePowerHp = species.basePower,
            battleRngSeed = ownSeed
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DigiWatchPalette.CyberBg)
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DigiSectionHeader(
            tag = "D-COM // 0NL1NE BATTLE TERMINAL",
            title = "128-Bit & DigiROM Transceiver",
            subtitle = "Deterministic PRNG combat & DNA Jogress link compatibility",
            accentColor = Color(0xFFEF5350)
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Quick Fighter Selector Strip
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FEATURED_FIGHTERS.forEach { id ->
                val sp = PetSpecies.getById(id)
                val selected = ownMonsterId == id
                Row(
                    modifier = Modifier
                        .background(
                            if (selected) Color(0xFF281C24) else DigiWatchPalette.CardSurface,
                            RoundedCornerShape(10.dp)
                        )
                        .border(
                            1.dp,
                            if (selected) Color(0xFFEF5350) else DigiWatchPalette.CardBorder,
                            RoundedCornerShape(10.dp)
                        )
                        .clickable { ownMonsterId = id }
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SpriteCanvasRenderer(
                        speciesId = sp.id,
                        frame = SpriteFrame.IDLE_A,
                        size = 26.dp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text(
                            text = sp.name,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (selected) Color(0xFFFF8A80) else Color.White
                        )
                        Text(
                            text = "${sp.stage.displayName} • ${sp.basePower}P",
                            fontSize = 8.sp,
                            color = Color(0xFF90A4AE),
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Generator Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = DigiWatchPalette.CardSurface),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, DigiWatchPalette.CardBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "▸ YOUR D-COM PACKET (${ownSpecies.name.uppercase()})",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = DigiWatchPalette.DigiviceCyan,
                        fontFamily = FontFamily.Monospace
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        StageBadge(ownSpecies.stage.displayName)
                        AttributeBadge(ownSpecies.attribute.displayName)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(DigiWatchPalette.CardInner, RoundedCornerShape(10.dp))
                        .border(1.dp, Color(0xFF263238), RoundedCornerShape(10.dp))
                        .padding(12.dp)
                ) {
                    Column {
                        Text(
                            text = "128-BIT BASE32 COMM CODE:",
                            fontSize = 9.sp,
                            color = Color(0xFF78909C),
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = CommCodeCodec.formatWithHyphens(generatedCode),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF00E676),
                            fontFamily = FontFamily.Monospace
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "DIGIMON 0NL1NE / D-COM DIGIROM:",
                            fontSize = 9.sp,
                            color = Color(0xFF78909C),
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = generatedDigiRom,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = DigiWatchPalette.CrestGold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Copy & Reroll Action Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            clipboardManager.setText(AnnotatedString(generatedCode))
                            Toast.makeText(context, "128-Bit Base32 Code copied!", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.ContentCopy, contentDescription = "Copy Base32", modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Base32", fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                    }

                    OutlinedButton(
                        onClick = {
                            clipboardManager.setText(AnnotatedString(generatedDigiRom))
                            Toast.makeText(context, "0nl1ne DigiROM copied!", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.ContentCopy, contentDescription = "Copy ROM", modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("DigiROM", fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                    }

                    Button(
                        onClick = { ownSeed = Random.nextInt(0xFFFF) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF37474F))
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = "Reroll", modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Seed", fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Button(
                    onClick = {
                        scope.launch {
                            val ok = wearableManager.sendCommCodeToWatch(generatedCode)
                            beamStatus = if (ok) "Dispatched Comm Code to Watch!" else "Watch not reachable (Offline Mode)"
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00897B))
                ) {
                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Beam", modifier = Modifier.size(15.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("BEAM CODE TO SMARTWATCH", fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                }
            }
        }

        beamStatus?.let {
            Text("▸ $it", fontSize = 11.sp, color = Color(0xFF00E676), fontFamily = FontFamily.Monospace, modifier = Modifier.padding(top = 6.dp))
        }

        Spacer(modifier = Modifier.height(14.dp))

        // NFC Tap-to-Duel & Tag Writer Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = DigiWatchPalette.CardSurface),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, Color(0xFF00E676).copy(alpha = 0.5f))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(Color(0xFF00E676), CircleShape)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "NFC TAP-TO-DUEL",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF00E676),
                            fontFamily = FontFamily.Monospace
                        )
                    }
                    Text(
                        text = if (NfcDuelManager.isNfcEnabled(context)) "STATUS: READY" else "NFC DISABLED",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (NfcDuelManager.isNfcEnabled(context)) Color(0xFF00E676) else Color.Red,
                        fontFamily = FontFamily.Monospace
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Tap the back of this phone against another DigiWatch phone, smartwatch, or any physical NFC tag/card to challenge instantly!",
                    fontSize = 10.sp,
                    color = Color.LightGray
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            val tag = NfcBattleBridge.pendingTagToWrite
                            if (tag != null) {
                                val ok = NfcDuelManager.writeDuelCodeToTag(tag, generatedDigiRom)
                                beamStatus = if (ok) "✓ Wrote ${ownSpecies.name} DigiROM to physical NFC Tag!" else "Tag write failed (Check tag write-lock)"
                            } else {
                                beamStatus = "Hold a physical NFC tag to back of phone, then click Write"
                            }
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("WRITE TO NFC TAG", fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                    }

                    if (inputCode.isNotEmpty()) {
                        Button(
                            onClick = {
                                scope.launch {
                                    val ok = wearableManager.sendCommCodeToWatch(inputCode)
                                    beamStatus = if (ok) "Dispatched scanned opponent to Watch!" else "Watch not reachable"
                                }
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00897B))
                        ) {
                            Text("BEAM TO WATCH", fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Opponent Code Input & Combat Simulator
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = DigiWatchPalette.CardSurface),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, DigiWatchPalette.CardBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "▸ OPPONENT D-COM / DIGIROM SCANNER",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFF8A80),
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = inputCode,
                    onValueChange = {
                        inputCode = it
                        validationError = null
                        decodedOpponent = null
                    },
                    label = { Text("Paste 26-Char Base32 or V1-XXXX-XXXX-XXXX DigiROM", fontSize = 11.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            val devimonCode = CommCodeCodec.encode128(
                                monsterId = 303, // Devimon (Virus)
                                stage = PetStage.ADULT,
                                attribute = ElementalAttribute.VIRUS,
                                basePowerHp = 210,
                                historicalVictories = 9,
                                trainingEffort = 3,
                                battleRngSeed = 0xBEEF
                            )
                            inputCode = CommCodeCodec.formatWithHyphens(devimonCode)
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Devimon", fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                    }

                    OutlinedButton(
                        onClick = {
                            val metalGaruruCode = CommCodeCodec.encode128(
                                monsterId = 503, // MetalGarurumon (Data)
                                stage = PetStage.ULTIMATE,
                                attribute = ElementalAttribute.DATA,
                                basePowerHp = 280,
                                historicalVictories = 18,
                                trainingEffort = 4,
                                battleRngSeed = 0xCAFE
                            )
                            inputCode = CommCodeCodec.formatWithHyphens(metalGaruruCode)
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("MetalGaruru", fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                    }

                    Button(
                        onClick = {
                            try {
                                val payload = CommCodeCodec.decodeAuto(inputCode)
                                decodedOpponent = payload
                                validationError = null

                                // Execute Deterministic Combat Simulation!
                                val ownSp = PetSpecies.getById(ownMonsterId)
                                val oppSp = PetSpecies.getById(payload.monsterId)

                                val fA = BattleFighter(
                                    id = "player",
                                    speciesId = ownMonsterId,
                                    name = ownSp.name,
                                    stage = ownSp.stage,
                                    attribute = ownSp.attribute,
                                    maxHp = ownSp.baseHp,
                                    currentHp = ownSp.baseHp,
                                    basePower = ownSp.basePower,
                                    victories = 12,
                                    effort = 4,
                                    battleSeed = ownSeed
                                )
                                val fB = BattleFighter(
                                    id = "opponent",
                                    speciesId = payload.monsterId,
                                    name = oppSp.name,
                                    stage = payload.stage,
                                    attribute = payload.attribute,
                                    maxHp = oppSp.baseHp,
                                    currentHp = oppSp.baseHp,
                                    basePower = oppSp.basePower,
                                    victories = payload.historicalVictories,
                                    effort = payload.trainingEffort,
                                    battleSeed = payload.battleRngSeed
                                )

                                battleResult = DeterministicBattleSolver.resolveBattle(fA, fB)
                            } catch (e: Exception) {
                                validationError = "Validation Failed: ${e.message}"
                            }
                        },
                        modifier = Modifier.weight(1.2f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = "Simulate", modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(2.dp))
                        Text("FIGHT!", fontSize = 10.sp, fontWeight = FontWeight.Black, fontFamily = FontFamily.Monospace)
                    }
                }

                // Visual LCD VS Arena & DNA Jogress Compatibility
                decodedOpponent?.let { opp ->
                    val oppSp = PetSpecies.getById(opp.monsterId)
                    val ownSp = PetSpecies.getById(ownMonsterId)
                    val fusionRecipe = JogressEngine.findFusionRecipe(ownMonsterId, opp.monsterId)

                    Spacer(modifier = Modifier.height(12.dp))

                    // Visual Bandai LCD VS Arena Card
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF15221D), RoundedCornerShape(12.dp))
                            .border(1.dp, Color(0xFF4CAF50), RoundedCornerShape(12.dp))
                            .padding(12.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Player Fighter
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    SpriteCanvasRenderer(
                                        speciesId = ownSp.id,
                                        frame = SpriteFrame.ATTACK_CHARGE,
                                        size = 56.dp
                                    )
                                    Text(ownSp.name, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF80CBC4))
                                    Text("${ownSp.baseHp}HP • ${ownSp.basePower}P", fontSize = 9.sp, color = Color.LightGray, fontFamily = FontFamily.Monospace)
                                }

                                Box(
                                    modifier = Modifier
                                        .background(Color(0xFF3E1C1C), RoundedCornerShape(6.dp))
                                        .border(1.dp, Color(0xFFFF5252), RoundedCornerShape(6.dp))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text("VS", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD54F), fontFamily = FontFamily.Monospace)
                                }

                                // Decoded Opponent Fighter
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    SpriteCanvasRenderer(
                                        speciesId = oppSp.id,
                                        frame = SpriteFrame.ATTACK_CHARGE,
                                        size = 56.dp
                                    )
                                    Text(oppSp.name, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF8A80))
                                    Text("${oppSp.baseHp}HP • ${opp.basePowerHp}P", fontSize = 9.sp, color = Color.LightGray, fontFamily = FontFamily.Monospace)
                                }
                            }

                            // DNA Jogress / DigiXros Trigger Card
                            if (fusionRecipe != null) {
                                val targetSp = PetSpecies.getById(fusionRecipe.targetSpeciesId)
                                Spacer(modifier = Modifier.height(10.dp))
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1B2836)),
                                    shape = RoundedCornerShape(8.dp),
                                    border = BorderStroke(1.dp, Color(fusionRecipe.fusionType.badgeColorHex))
                                ) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Column(modifier = Modifier.weight(1f)) {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Icon(
                                                        imageVector = Icons.Default.Bolt,
                                                        contentDescription = null,
                                                        tint = Color(fusionRecipe.fusionType.badgeColorHex),
                                                        modifier = Modifier.size(16.dp)
                                                    )
                                                    Spacer(modifier = Modifier.width(4.dp))
                                                    Text(
                                                        text = "${fusionRecipe.fusionType.displayName.uppercase()} COMPATIBLE!",
                                                        fontSize = 10.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = Color(fusionRecipe.fusionType.badgeColorHex),
                                                        fontFamily = FontFamily.Monospace
                                                    )
                                                }
                                                Spacer(modifier = Modifier.height(2.dp))
                                                Text(
                                                    text = "${ownSp.name} + ${oppSp.name} → ${targetSp.name}",
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.ExtraBold,
                                                    color = Color.White
                                                )
                                                Text(
                                                    text = fusionRecipe.description,
                                                    fontSize = 10.sp,
                                                    color = Color.LightGray
                                                )
                                            }
                                            SpriteCanvasRenderer(
                                                speciesId = targetSp.id,
                                                frame = SpriteFrame.IDLE_A,
                                                size = 46.dp
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Button(
                                            onClick = {
                                                scope.launch {
                                                    val fusedState = JogressEngine.executeFusion(
                                                        primary = PetState(
                                                            speciesId = ownMonsterId,
                                                            maxHp = ownSp.baseHp,
                                                            currentHp = ownSp.baseHp,
                                                            victories = 12,
                                                            effort = 4
                                                        ),
                                                        secondarySpeciesId = opp.monsterId,
                                                        secondaryVictories = opp.historicalVictories,
                                                        secondaryEffort = opp.trainingEffort
                                                    )
                                                    ownMonsterId = fusedState.speciesId
                                                    wearableManager.sendPetStateToWatch(fusedState)
                                                    DigiWatchAppWidgetProvider.updateAllWidgets(context)
                                                    beamStatus = "DNA Digivolution Complete: Awakened ${targetSp.name}!"
                                                }
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(fusionRecipe.fusionType.badgeColorHex)),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text("DNA DIGIVOLVE WITH FRIEND'S CODE!", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color.Black, fontFamily = FontFamily.Monospace)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                validationError?.let {
                    Text(
                        text = it,
                        fontSize = 11.sp,
                        color = Color(0xFFFF5252),
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }

        // Battle Simulation Log Results
        battleResult?.let { result ->
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = DigiWatchPalette.CardSurface),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, if (result.winnerIndex == 0) Color(0xFF00E676) else Color(0xFFEF5350))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "▸ DETERMINISTIC COMBAT RESOLUTION LOG",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = DigiWatchPalette.CrestGold,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = "WINNER: ${result.winnerName ?: "DRAW"} (${result.totalRounds} ROUNDS)",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        color = if (result.winnerIndex == 0) Color(0xFF00E676) else Color(0xFFFF5252),
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    for (turn in result.turns) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp)
                                .background(DigiWatchPalette.CardInner, RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "R${turn.round}: ${turn.attackerName} → ${turn.defenderName}",
                                fontSize = 10.sp,
                                color = Color.White,
                                fontFamily = FontFamily.Monospace
                            )
                            Text(
                                text = "-${turn.damage} HP ${if (turn.isAdvantage) "[1.2x]" else ""} (${turn.defenderRemainingHp}HP)",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (turn.isAdvantage) Color(0xFF00E676) else Color(0xFFFF8A80),
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Codec Bit-Map Inspector
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = DigiWatchPalette.CardSurface),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, DigiWatchPalette.CardBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "▸ 128-BIT PAYLOAD ARCHITECTURE",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFCC80),
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.height(6.dp))
                BitFieldRow("000 - 007", "Version (8-bit)", "0x01")
                BitFieldRow("008 - 023", "Monster ID (16-bit)", "$ownMonsterId (${ownSpecies.name})")
                BitFieldRow("024 - 027", "Stage Index (4-bit)", "${ownSpecies.stage.stageIndex}")
                BitFieldRow("028 - 031", "Attribute (4-bit)", ownSpecies.attribute.displayName)
                BitFieldRow("032 - 047", "Base Power (16-bit)", "${ownSpecies.basePower} PWR")
                BitFieldRow("080 - 095", "PRNG Seed (16-bit)", "0x${Integer.toHexString(ownSeed).uppercase()}")
                BitFieldRow("112 - 127", "HMAC-16 Hash (16-bit)", "Cryptographic Tamper-Proof")
            }
        }
    }
}

@Composable
private fun BitFieldRow(bits: String, name: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "Bits $bits", fontSize = 10.sp, color = Color.Gray, fontFamily = FontFamily.Monospace)
        Text(text = name, fontSize = 10.sp, color = Color.LightGray)
        Text(text = value, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White, fontFamily = FontFamily.Monospace)
    }
}
