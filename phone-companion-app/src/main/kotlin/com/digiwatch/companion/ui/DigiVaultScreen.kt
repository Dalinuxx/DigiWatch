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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Search
import com.digiwatch.companion.data.SyncedPetEntity
import com.digiwatch.core.fusion.JogressEngine
import com.digiwatch.core.fusion.JogressRecipe
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.digiwatch.companion.data.PhoneDatabase
import com.digiwatch.companion.data.StoredDigimonEntity
import com.digiwatch.companion.datalayer.PhoneWearableManager
import com.digiwatch.companion.render.SpriteCanvasRenderer
import com.digiwatch.companion.widget.DigiWatchAppWidgetProvider
import com.digiwatch.core.codec.CommCodeCodec
import com.digiwatch.core.model.PetSpecies
import com.digiwatch.core.model.PetStage
import com.digiwatch.core.model.PetState
import com.digiwatch.core.model.SpriteFrame
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DigiVaultScreen(
    db: PhoneDatabase,
    wearableManager: PhoneWearableManager
) {
    val vaultPets by db.phoneDao().getAllVaultPetsFlow().collectAsState(initial = emptyList())
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    var searchQuery by remember { mutableStateOf("") }
    var selectedStageFilter by remember { mutableStateOf<PetStage?>(null) }
    var activePetForDeposit by remember { mutableStateOf<PetState?>(null) }
    var nicknameInput by remember { mutableStateOf("") }
    var showDepositDialog by remember { mutableStateOf(false) }
    var commCodeDialogData by remember { mutableStateOf<Pair<String, String>?>(null) }
    var deleteConfirmPet by remember { mutableStateOf<StoredDigimonEntity?>(null) }
    var actionStatusMessage by remember { mutableStateOf<String?>(null) }
    var pendingFusionPair by remember { mutableStateOf<Pair<StoredDigimonEntity, JogressRecipe>?>(null) }

    // Sprite animation ticker
    var frameToggle by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        // Fetch latest snapshot to populate quick deposit
        val latest = db.phoneDao().getLatestSnapshot()
        if (latest != null) {
            activePetForDeposit = PetState(
                speciesId = latest.speciesId,
                currentHp = latest.currentHp,
                maxHp = latest.maxHp,
                hunger = latest.hunger,
                strength = latest.strength,
                weight = latest.weight,
                ageSeconds = latest.ageSeconds,
                careMistakes = latest.careMistakes,
                effort = latest.effort,
                overfeedCount = latest.overfeedCount,
                totalBattles = latest.totalBattles,
                victories = latest.victories
            )
        }
        while (true) {
            delay(500L)
            frameToggle = !frameToggle
        }
    }

    val filteredPets = vaultPets.filter { pet ->
        val matchesSearch = pet.speciesName.contains(searchQuery, ignoreCase = true) ||
                pet.customNickname.contains(searchQuery, ignoreCase = true)
        val matchesStage = selectedStageFilter == null || pet.stageName == selectedStageFilter?.displayName
        matchesSearch && matchesStage
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DigiWatchPalette.CyberBg)
            .padding(16.dp)
    ) {
        // Header
        DigiSectionHeader(
            tag = "DIGIVAULT // CRYO-STASIS ARCHIVE",
            title = "Preserved Partners (${vaultPets.size})",
            accentColor = Color(0xFF26A69A),
            trailingContent = {
                Button(
                    onClick = { showDepositDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00897B))
                ) {
                    Icon(Icons.Default.Archive, contentDescription = "Deposit", modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("DEPOSIT ACTIVE", fontSize = 10.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                }
            }
        )

        actionStatusMessage?.let { msg ->
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = msg,
                fontSize = 11.sp,
                color = Color(0xFF81C784),
                fontFamily = FontFamily.Monospace
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Search Bar & Stage Filter Chips
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search by species or nickname...", color = Color.Gray, fontSize = 12.sp) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.Gray) },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            FilterChip("All", selectedStageFilter == null) { selectedStageFilter = null }
            PetStage.entries.forEach { stage ->
                FilterChip(stage.displayName, selectedStageFilter == stage) {
                    selectedStageFilter = if (selectedStageFilter == stage) null else stage
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Active Partner DNA Jogress & DigiXros Quick-Fusion / Partner Spawner Banner
        val activePet = activePetForDeposit ?: PetState(speciesId = 201)
        val activeSp = remember(activePet.speciesId) { PetSpecies.getById(activePet.speciesId) }
        val vaultCompatibleRecipes = remember(activeSp.id) { JogressEngine.getCompatibleRecipesFor(activeSp.id) }

        if (vaultCompatibleRecipes.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1426)),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Color(0xFFAB47BC))
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(
                        text = "⚡ ACTIVE ${activeSp.name.uppercase()} — DNA JOGRESS & DIGIXROS LAB (${vaultCompatibleRecipes.size})",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFFEA80FC),
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        vaultCompatibleRecipes.forEach { recipe ->
                            val partnerId = if (recipe.primarySpeciesId == activeSp.id) recipe.secondarySpeciesId else recipe.primarySpeciesId
                            val partnerSp = PetSpecies.getById(partnerId)
                            val targetSp = PetSpecies.getById(recipe.targetSpeciesId)
                            Row(
                                modifier = Modifier
                                    .background(Color(0xFF120E1C), RoundedCornerShape(8.dp))
                                    .border(0.75.dp, Color(recipe.fusionType.badgeColorHex), RoundedCornerShape(8.dp))
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                SpriteCanvasRenderer(speciesId = partnerSp.id, frame = SpriteFrame.IDLE_A, size = 28.dp)
                                Text("→", fontSize = 11.sp, color = DigiWatchPalette.CrestGold, fontWeight = FontWeight.Bold)
                                SpriteCanvasRenderer(speciesId = targetSp.id, frame = SpriteFrame.IDLE_A, size = 32.dp)
                                Column {
                                    Text(
                                        text = "+${partnerSp.name} → ${targetSp.name}",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Text(
                                        text = recipe.fusionType.displayName,
                                        fontSize = 8.5.sp,
                                        color = Color(recipe.fusionType.badgeColorHex),
                                        fontFamily = FontFamily.Monospace
                                    )
                                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                        OutlinedButton(
                                            onClick = {
                                                scope.launch {
                                                    db.phoneDao().insertVaultPet(
                                                        StoredDigimonEntity(
                                                            speciesId = partnerSp.id,
                                                            speciesName = partnerSp.name,
                                                            customNickname = "${recipe.fusionType.displayName} Partner",
                                                            stageName = partnerSp.stage.displayName,
                                                            attributeName = partnerSp.attribute.displayName,
                                                            currentHp = partnerSp.baseHp,
                                                            maxHp = partnerSp.baseHp,
                                                            hunger = 4,
                                                            strength = 4,
                                                            weight = partnerSp.baselineWeight,
                                                            ageSeconds = 259200L,
                                                            careMistakes = 0,
                                                            effort = 4,
                                                            overfeedCount = 0,
                                                            totalBattles = 20,
                                                            victories = 16
                                                        )
                                                    )
                                                    actionStatusMessage = "Added ${partnerSp.name} to DigiVault — tap 'DNA / XROS FUSE' on its card below!"
                                                }
                                            }
                                        ) {
                                            Text("+VAULT", fontSize = 8.5.sp, fontFamily = FontFamily.Monospace)
                                        }
                                        Button(
                                            onClick = {
                                                val tempEntity = StoredDigimonEntity(
                                                    speciesId = partnerSp.id,
                                                    speciesName = partnerSp.name,
                                                    customNickname = partnerSp.name,
                                                    stageName = partnerSp.stage.displayName,
                                                    attributeName = partnerSp.attribute.displayName,
                                                    currentHp = partnerSp.baseHp,
                                                    maxHp = partnerSp.baseHp,
                                                    hunger = 4,
                                                    strength = 4,
                                                    weight = partnerSp.baselineWeight,
                                                    ageSeconds = 259200L,
                                                    careMistakes = 0,
                                                    effort = 4,
                                                    overfeedCount = 0,
                                                    totalBattles = 20,
                                                    victories = 16
                                                )
                                                pendingFusionPair = tempEntity to recipe
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(recipe.fusionType.badgeColorHex))
                                        ) {
                                            Text("⚡ FUSE", fontSize = 8.5.sp, fontWeight = FontWeight.Black, color = Color.Black, fontFamily = FontFamily.Monospace)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

        // Stored Pets List
        if (filteredPets.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "NO DIGIMON IN DIGIVAULT",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Deposit your raised partner to store them in cryo-stasis,\nor clear active filters.",
                        fontSize = 11.sp,
                        color = Color.DarkGray,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredPets, key = { it.id }) { storedPet ->
                    VaultPetCard(
                        pet = storedPet,
                        activePet = activePetForDeposit,
                        frameToggle = frameToggle,
                        onDeployToWatch = {
                            scope.launch {
                                val state = PetState(
                                    speciesId = storedPet.speciesId,
                                    currentHp = storedPet.currentHp,
                                    maxHp = storedPet.maxHp,
                                    hunger = storedPet.hunger,
                                    strength = storedPet.strength,
                                    weight = storedPet.weight,
                                    ageSeconds = storedPet.ageSeconds,
                                    careMistakes = storedPet.careMistakes,
                                    effort = storedPet.effort,
                                    overfeedCount = storedPet.overfeedCount,
                                    totalBattles = storedPet.totalBattles,
                                    victories = storedPet.victories
                                )
                                activePetForDeposit = state
                                db.phoneDao().insertSnapshot(
                                    SyncedPetEntity(
                                        syncedTimestampUtc = System.currentTimeMillis() / 1000L,
                                        speciesId = state.speciesId,
                                        speciesName = storedPet.speciesName,
                                        currentHp = state.currentHp,
                                        maxHp = state.maxHp,
                                        hunger = state.hunger,
                                        strength = state.strength,
                                        weight = state.weight,
                                        ageSeconds = state.ageSeconds,
                                        careMistakes = state.careMistakes,
                                        effort = state.effort,
                                        overfeedCount = state.overfeedCount,
                                        totalBattles = state.totalBattles,
                                        victories = state.victories
                                    )
                                )
                                val ok = wearableManager.sendPetStateToWatch(state)
                                DigiWatchAppWidgetProvider.updateAllWidgets(context)
                                actionStatusMessage = if (ok) {
                                    "${storedPet.speciesName} deployed to Smartwatch & Dashboard!"
                                } else {
                                    "${storedPet.speciesName} deployed to Phone Dashboard & Widget (Watch offline)"
                                }
                            }
                        },
                        onGenerateCommCode = {
                            val species = PetSpecies.getById(storedPet.speciesId)
                            val seed = (System.currentTimeMillis() and 0xFFFF).toInt()
                            val code = CommCodeCodec.encode128(
                                monsterId = storedPet.speciesId,
                                stage = species.stage,
                                attribute = species.attribute,
                                basePowerHp = species.baseHp + species.basePower,
                                historicalVictories = storedPet.victories,
                                trainingEffort = storedPet.effort,
                                battleRngSeed = seed
                            )
                            val digiRom = CommCodeCodec.encodeDigiRom(
                                monsterId = storedPet.speciesId,
                                stage = species.stage,
                                attribute = species.attribute,
                                basePowerHp = species.basePower,
                                battleRngSeed = seed
                            )
                            commCodeDialogData = code to digiRom
                        },
                        onDelete = {
                            deleteConfirmPet = storedPet
                        },
                        onFuseWithActive = { pet, recipe ->
                            pendingFusionPair = pet to recipe
                        }
                    )
                }
            }
        }
    }

    // Deposit Active Partner Dialog
    if (showDepositDialog) {
        AlertDialog(
            onDismissRequest = { showDepositDialog = false },
            title = { Text("Deposit Active Partner") },
            text = {
                Column {
                    val p = activePetForDeposit ?: PetState(speciesId = 201)
                    val spec = PetSpecies.getById(p.speciesId)
                    Text("Store ${spec.name} (${spec.stage.displayName}) into cryo-stasis vault?")
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = nicknameInput,
                        onValueChange = { nicknameInput = it },
                        label = { Text("Custom Nickname (Optional)") },
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        scope.launch {
                            val p = activePetForDeposit ?: PetState(speciesId = 201)
                            val spec = PetSpecies.getById(p.speciesId)
                            db.phoneDao().insertVaultPet(
                                StoredDigimonEntity(
                                    speciesId = p.speciesId,
                                    speciesName = spec.name,
                                    customNickname = nicknameInput.trim(),
                                    stageName = spec.stage.displayName,
                                    attributeName = spec.attribute.displayName,
                                    currentHp = p.currentHp,
                                    maxHp = p.maxHp,
                                    hunger = p.hunger,
                                    strength = p.strength,
                                    weight = p.weight,
                                    ageSeconds = p.ageSeconds,
                                    careMistakes = p.careMistakes,
                                    effort = p.effort,
                                    overfeedCount = p.overfeedCount,
                                    totalBattles = p.totalBattles,
                                    victories = p.victories
                                )
                            )
                            showDepositDialog = false
                            nicknameInput = ""
                            actionStatusMessage = "${spec.name} safely stored in DigiVault!"
                        }
                    }
                ) {
                    Text("Confirm Deposit")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDepositDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Comm Code & DigiROM Dialog
    commCodeDialogData?.let { (code, digiRom) ->
        AlertDialog(
            onDismissRequest = { commCodeDialogData = null },
            title = { Text("Comm Code & 0nl1ne DigiROM") },
            text = {
                Column {
                    Text("128-bit Base32 Comm Code:", fontSize = 11.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = CommCodeCodec.formatWithHyphens(code),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF80CBC4),
                        fontFamily = FontFamily.Monospace,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF14241D), RoundedCornerShape(8.dp))
                            .padding(10.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("Digimon 0nl1ne / D-Com / A-Com Packet:", fontSize = 11.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = digiRom,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFD54F),
                        fontFamily = FontFamily.Monospace,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF261E14), RoundedCornerShape(8.dp))
                            .padding(10.dp)
                    )
                }
            },
            confirmButton = {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Button(
                        onClick = {
                            clipboardManager.setText(AnnotatedString(code))
                            Toast.makeText(context, "Base32 Comm Code copied!", Toast.LENGTH_SHORT).show()
                            commCodeDialogData = null
                        }
                    ) {
                        Icon(Icons.Default.ContentCopy, contentDescription = "Copy", modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Base32", fontSize = 11.sp)
                    }
                    Button(
                        onClick = {
                            clipboardManager.setText(AnnotatedString(digiRom))
                            Toast.makeText(context, "DigiROM Packet copied!", Toast.LENGTH_SHORT).show()
                            commCodeDialogData = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFB8C00))
                    ) {
                        Icon(Icons.Default.ContentCopy, contentDescription = "Copy ROM", modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("DigiROM", fontSize = 11.sp)
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = { commCodeDialogData = null }) {
                    Text("Close")
                }
            }
        )
    }

    // Delete Confirmation Dialog
    deleteConfirmPet?.let { petToDelete ->
        AlertDialog(
            onDismissRequest = { deleteConfirmPet = null },
            title = { Text("Release Digimon?") },
            text = { Text("Are you sure you want to release ${petToDelete.speciesName} from DigiVault? This cannot be undone.") },
            confirmButton = {
                Button(
                    onClick = {
                        scope.launch {
                            db.phoneDao().deleteVaultPet(petToDelete.id)
                            deleteConfirmPet = null
                            actionStatusMessage = "${petToDelete.speciesName} released from vault."
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
                ) {
                    Text("Release")
                }
            },
            dismissButton = {
                TextButton(onClick = { deleteConfirmPet = null }) {
                    Text("Cancel")
                }
            }
        )
    }

    // DNA Digivolution / DigiXros Confirmation Dialog
    pendingFusionPair?.let { (vaultPet, recipe) ->
        val active = activePetForDeposit ?: PetState(speciesId = 201)
        val activeSp = PetSpecies.getById(active.speciesId)
        val vaultSp = PetSpecies.getById(vaultPet.speciesId)
        val targetSp = PetSpecies.getById(recipe.targetSpeciesId)

        AlertDialog(
            onDismissRequest = { pendingFusionPair = null },
            title = {
                Text(
                    text = "${recipe.fusionType.displayName} Awakening",
                    fontWeight = FontWeight.Bold,
                    color = Color(recipe.fusionType.badgeColorHex)
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = recipe.description,
                        fontSize = 12.sp,
                        color = Color.LightGray,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    // 3-Way Partner Fusion Visualizer
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            SpriteCanvasRenderer(
                                speciesId = activeSp.id,
                                frame = SpriteFrame.IDLE_A,
                                modifier = Modifier.size(46.dp)
                            )
                            Text(activeSp.name, fontSize = 10.sp, color = Color.White, fontWeight = FontWeight.Bold)
                            Text("Active Partner", fontSize = 8.sp, color = Color.Gray)
                        }

                        Text("+", fontSize = 18.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFB74D))

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            SpriteCanvasRenderer(
                                speciesId = vaultSp.id,
                                frame = SpriteFrame.IDLE_A,
                                modifier = Modifier.size(46.dp)
                            )
                            Text(vaultSp.name, fontSize = 10.sp, color = Color.White, fontWeight = FontWeight.Bold)
                            Text("Vault Partner", fontSize = 8.sp, color = Color.Gray)
                        }

                        Text("=", fontSize = 18.sp, fontWeight = FontWeight.Black, color = Color(0xFF64B5F6))

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            SpriteCanvasRenderer(
                                speciesId = targetSp.id,
                                frame = if (frameToggle) SpriteFrame.IDLE_A else SpriteFrame.IDLE_B,
                                modifier = Modifier.size(52.dp)
                            )
                            Text(targetSp.name, fontSize = 11.sp, color = Color(0xFF00E676), fontWeight = FontWeight.Black)
                            Text(targetSp.stage.displayName, fontSize = 8.sp, color = Color(0xFF80CBC4))
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Fusion Synergy Stat Preview
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF141A24))
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text("FUSION SYNERGY INHERITANCE", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF80D8FF))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("• Synergy Max HP: ${(targetSp.baseHp * 1.10f).toInt()} (+10% DNA Harmony)", fontSize = 11.sp, color = Color.LightGray)
                            Text("• Combined Victories: ${active.victories + vaultPet.victories} Wins", fontSize = 11.sp, color = Color.LightGray)
                            Text("• Inherited Effort: ${maxOf(active.effort, vaultPet.effort)}★", fontSize = 11.sp, color = Color.LightGray)
                            Text("• Digimon 0nl1ne / D-Com: 100% Wire Protocol Compatible", fontSize = 10.sp, color = Color(0xFF00E676), fontWeight = FontWeight.Bold)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        scope.launch {
                            val fusedState = JogressEngine.executeFusion(
                                primary = active,
                                secondarySpeciesId = vaultPet.speciesId,
                                secondaryVictories = vaultPet.victories,
                                secondaryEffort = vaultPet.effort
                            )
                            activePetForDeposit = fusedState

                            // 1. Send to Smartwatch
                            val ok = wearableManager.sendPetStateToWatch(fusedState)
                            // 2. Save snapshot in DB
                            val entity = SyncedPetEntity(
                                speciesId = fusedState.speciesId,
                                speciesName = targetSp.name,
                                currentHp = fusedState.currentHp,
                                maxHp = fusedState.maxHp,
                                hunger = fusedState.hunger,
                                strength = fusedState.strength,
                                weight = fusedState.weight,
                                ageSeconds = fusedState.ageSeconds,
                                careMistakes = fusedState.careMistakes,
                                effort = fusedState.effort,
                                overfeedCount = fusedState.overfeedCount,
                                totalBattles = fusedState.totalBattles,
                                victories = fusedState.victories
                            )
                            db.phoneDao().insertSnapshot(entity)
                            // 3. Remove consumed vault partner
                            db.phoneDao().deleteVaultPet(vaultPet.id)
                            // 4. Update home screen widget
                            DigiWatchAppWidgetProvider.updateAllWidgets(context)

                            pendingFusionPair = null
                            actionStatusMessage = "Awakened ${targetSp.name} via ${recipe.fusionType.displayName}!"
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00E676))
                ) {
                    Text("Awaken ${targetSp.name}!", fontWeight = FontWeight.Black, color = Color.Black)
                }
            },
            dismissButton = {
                TextButton(onClick = { pendingFusionPair = null }) {
                    Text("Cancel", color = Color.Gray)
                }
            }
        )
    }
}

@Composable
private fun VaultPetCard(
    pet: StoredDigimonEntity,
    activePet: PetState?,
    frameToggle: Boolean,
    onDeployToWatch: () -> Unit,
    onGenerateCommCode: () -> Unit,
    onDelete: () -> Unit,
    onFuseWithActive: (StoredDigimonEntity, JogressRecipe) -> Unit
) {
    val dateStr = SimpleDateFormat("MMM dd, yyyy • HH:mm", Locale.getDefault())
        .format(Date(pet.storedTimestampUtc * 1000L))

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = DigiWatchPalette.CardSurface),
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.dp, DigiWatchPalette.CardBorder)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Recessed LCD Cryo-Viewport
                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .background(Color(0xFF16241E), RoundedCornerShape(10.dp))
                        .border(1.5.dp, Color(0x6680CBC4), RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    SpriteCanvasRenderer(
                        speciesId = pet.speciesId,
                        frame = if (frameToggle) SpriteFrame.IDLE_A else SpriteFrame.IDLE_B,
                        modifier = Modifier.size(54.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Metadata Column
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = if (pet.customNickname.isNotEmpty()) "${pet.customNickname} (#${pet.speciesId} ${pet.speciesName})" else "#${pet.speciesId} ${pet.speciesName}",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        StageBadge(pet.stageName)
                        AttributeBadge(pet.attributeName)
                    }

                    Spacer(modifier = Modifier.height(5.dp))

                    Text(
                        text = "H:${"♥".repeat(pet.hunger.coerceIn(0, 4))}  S:${"★".repeat(pet.strength.coerceIn(0, 4))}  EFF:${pet.effort}★",
                        fontSize = 11.sp,
                        color = Color(0xFFFF8A80),
                        fontFamily = FontFamily.Monospace
                    )

                    Text(
                        text = "Wt: ${pet.weight}g • Battles: ${pet.victories}W/${pet.totalBattles} • Stored: $dateStr",
                        fontSize = 9.sp,
                        color = Color(0xFF90A4AE),
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            val fusionRecipe = activePet?.let {
                JogressEngine.findFusionRecipe(it.speciesId, pet.speciesId)
            }

            if (fusionRecipe != null) {
                val targetSp = PetSpecies.getById(fusionRecipe.targetSpeciesId)
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF152230), RoundedCornerShape(8.dp))
                        .border(1.dp, Color(fusionRecipe.fusionType.badgeColorHex), RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        SpriteCanvasRenderer(
                            speciesId = targetSp.id,
                            frame = SpriteFrame.IDLE_A,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "⚡ ${fusionRecipe.fusionType.displayName.uppercase()} READY!",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(fusionRecipe.fusionType.badgeColorHex),
                                fontFamily = FontFamily.Monospace
                            )
                            Text(
                                text = "Fuses into ${targetSp.name} (${targetSp.stage.displayName})",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                        }
                    }

                    Button(
                        onClick = { onFuseWithActive(pet, fusionRecipe) },
                        modifier = Modifier.height(28.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(fusionRecipe.fusionType.badgeColorHex))
                    ) {
                        Text("DNA FUSE", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color.Black, fontFamily = FontFamily.Monospace)
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Action Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = onDelete,
                    modifier = Modifier.height(32.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFEF5350))
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", modifier = Modifier.size(14.dp))
                }

                Spacer(modifier = Modifier.width(8.dp))

                OutlinedButton(
                    onClick = onGenerateCommCode,
                    modifier = Modifier.height(32.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = DigiWatchPalette.CrestGold)
                ) {
                    Icon(Icons.Default.FlashOn, contentDescription = "Comm Code", modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("D-COM CODE", fontSize = 9.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = onDeployToWatch,
                    modifier = Modifier.height(32.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00897B))
                ) {
                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Deploy", modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("DEPLOY TO WATCH", fontSize = 9.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                }
            }
        }
    }
}

@Composable
private fun FilterChip(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .background(
                if (isSelected) Color(0xFF00897B) else DigiWatchPalette.CardSurface,
                RoundedCornerShape(8.dp)
            )
            .border(
                1.dp,
                if (isSelected) Color(0xFF80CBC4) else DigiWatchPalette.CardBorder,
                RoundedCornerShape(8.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 5.dp)
    ) {
        Text(
            text = text,
            fontSize = 10.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) Color.White else Color.LightGray,
            fontFamily = FontFamily.Monospace
        )
    }
}
