package com.digiwatch.companion.ui

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.digiwatch.companion.data.PhoneDatabase
import com.digiwatch.companion.datalayer.PhoneWearableManager
import com.digiwatch.companion.render.SpriteCanvasRenderer
import com.digiwatch.core.model.PetState
import com.digiwatch.core.model.SpriteFrame
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun BackupRestoreScreen(
    db: PhoneDatabase,
    wearableManager: PhoneWearableManager
) {
    val snapshots by db.phoneDao().getAllSnapshotsFlow().collectAsState(initial = emptyList())
    val json = remember { Json { prettyPrint = true; ignoreUnknownKeys = true } }
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    var jsonExportString by remember { mutableStateOf(json.encodeToString(PetState())) }
    var statusMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DigiWatchPalette.CyberBg)
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DigiSectionHeader(
            tag = "SYSTEM ARCHIVE // BACKUP & RECOVERY",
            title = "State Persistence & JSON",
            subtitle = "Room database snapshot history & unified JSON serialization",
            accentColor = Color(0xFFCE93D8)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Synced Snapshots History
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = DigiWatchPalette.CardSurface),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, DigiWatchPalette.CardBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "▸ SYNCED WATCH STATE HISTORY (${snapshots.size} SNAPSHOTS)",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFE1BEE7),
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.height(8.dp))

                if (snapshots.isEmpty()) {
                    Text(
                        text = "No saved snapshots yet. Sync with watch on dashboard to create backups.",
                        fontSize = 12.sp,
                        color = Color.LightGray
                    )
                } else {
                    snapshots.take(5).forEach { snap ->
                        val dateStr = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault())
                            .format(Date(snap.syncedTimestampUtc * 1000L))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp)
                                .background(DigiWatchPalette.CardInner, RoundedCornerShape(8.dp))
                                .border(1.dp, Color(0xFF222E3C), RoundedCornerShape(8.dp))
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                SpriteCanvasRenderer(
                                    speciesId = snap.speciesId,
                                    frame = SpriteFrame.IDLE_A,
                                    size = 32.dp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = "#${snap.speciesId} ${snap.speciesName}",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Text(
                                        text = "Wt: ${snap.weight}g • CM: ${snap.careMistakes} • Wins: ${snap.victories}/${snap.totalBattles}",
                                        fontSize = 10.sp,
                                        color = Color.Gray,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }
                            }
                            Text(
                                text = dateStr,
                                fontSize = 10.sp,
                                color = Color(0xFFCE93D8),
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // JSON Backup Contract
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
                        text = "▸ UNIFIED JSON STATE CONTRACT",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFCE93D8),
                        fontFamily = FontFamily.Monospace
                    )
                    OutlinedButton(
                        onClick = {
                            clipboardManager.setText(AnnotatedString(jsonExportString))
                            Toast.makeText(context, "PetState JSON copied!", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.height(30.dp)
                    ) {
                        Icon(Icons.Default.ContentCopy, contentDescription = "Copy JSON", modifier = Modifier.size(13.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("COPY", fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = jsonExportString,
                    onValueChange = { jsonExportString = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp),
                    textStyle = androidx.compose.ui.text.TextStyle(
                        fontFamily = FontFamily.Monospace,
                        fontSize = 10.sp,
                        color = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedButton(
                        onClick = {
                            try {
                                val state = json.decodeFromString<PetState>(jsonExportString)
                                scope.launch {
                                    val ok = wearableManager.sendPetStateToWatch(state)
                                    statusMessage = if (ok) "Restored state to Smartwatch!" else "Saved locally (Watch offline)"
                                }
                            } catch (e: Exception) {
                                statusMessage = "Invalid JSON: ${e.message}"
                            }
                        }
                    ) {
                        Text("RESTORE TO WATCH", fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                    }

                    Button(
                        onClick = {
                            val eggState = PetState(speciesId = 101, weight = 5, ageSeconds = 0L)
                            jsonExportString = json.encodeToString(eggState)
                            scope.launch {
                                wearableManager.sendPetStateToWatch(eggState)
                                statusMessage = "New Digi-Egg (Botamon) initialized!"
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8E24AA))
                    ) {
                        Text("HATCH NEW EGG", fontSize = 10.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                    }
                }
            }
        }

        statusMessage?.let {
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
