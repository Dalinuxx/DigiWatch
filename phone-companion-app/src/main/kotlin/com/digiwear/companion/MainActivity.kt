package com.digiwear.companion

import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.IconButton
import com.digiwear.companion.ui.ExpeditionScreen
import com.digiwear.companion.ui.SettingsScreen
import com.digiwear.companion.data.PhoneDatabase
import com.digiwear.companion.datalayer.PhoneWearableManager
import com.digiwear.companion.nfc.NfcDuelManager
import com.digiwear.companion.ui.BattleHubScreen
import com.digiwear.companion.ui.DigiVaultScreen
import com.digiwear.companion.ui.DigiWearPalette
import com.digiwear.companion.ui.PetDashboardScreen
import com.digiwear.companion.ui.SpriteStudioScreen
import kotlinx.coroutines.flow.MutableStateFlow

object NfcBattleBridge {
    val incomingCodeFlow = MutableStateFlow<String?>(null)
    val incomingWatchRelayFlow = MutableStateFlow<String?>(null)
    var pendingTagToWrite: Tag? = null
}

class MainActivity : ComponentActivity() {

    private lateinit var db: PhoneDatabase
    private lateinit var wearableManager: PhoneWearableManager
    private val relayListener = com.google.android.gms.wearable.MessageClient.OnMessageReceivedListener { event ->
        if (event.path == "/sync/watch_acom_relay") {
            val digiRom = String(event.data, Charsets.UTF_8).trim()
            if (digiRom.isNotEmpty()) {
                NfcBattleBridge.incomingWatchRelayFlow.value = digiRom
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = PhoneDatabase.getInstance(this)
        wearableManager = PhoneWearableManager(this)
        handleNfcIntent(intent)

        setContent {
            MaterialTheme(
                colorScheme = darkColorScheme(
                    primary = DigiWearPalette.DigiviceCyan,
                    secondary = DigiWearPalette.CrestGold,
                    background = DigiWearPalette.CyberBg,
                    surface = DigiWearPalette.CardSurface
                )
            ) {
                MainPhoneApp(db, wearableManager)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        try {
            com.google.android.gms.wearable.Wearable.getMessageClient(this).addListener(relayListener)
        } catch (_: Exception) {}
        if (NfcDuelManager.isNfcEnabled(this)) {
            NfcDuelManager.enableReaderMode(this) { opponentCode, tag ->
                NfcBattleBridge.pendingTagToWrite = tag
                NfcBattleBridge.incomingCodeFlow.value = opponentCode
            }
        }
    }

    override fun onPause() {
        super.onPause()
        try {
            com.google.android.gms.wearable.Wearable.getMessageClient(this).removeListener(relayListener)
        } catch (_: Exception) {}
        NfcDuelManager.disableReaderMode(this)
        NfcBattleBridge.pendingTagToWrite = null
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleNfcIntent(intent)
    }

    private fun handleNfcIntent(intent: Intent?) {
        if (intent == null) return
        val tag = androidx.core.content.IntentCompat.getParcelableExtra(intent, NfcAdapter.EXTRA_TAG, Tag::class.java)
        if (tag != null) {
            NfcBattleBridge.pendingTagToWrite = tag
            val code = NfcDuelManager.extractCommCodeFromTag(tag)
            NfcBattleBridge.incomingCodeFlow.value = code
        }
    }
}

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun MainPhoneApp(
    db: PhoneDatabase,
    wearableManager: PhoneWearableManager
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    val navItemColors = NavigationBarItemDefaults.colors(
        selectedIconColor = Color(0xFF00E5FF),
        selectedTextColor = Color(0xFF00E5FF),
        indicatorColor = Color(0xFF142D38),
        unselectedIconColor = Color(0xFF78909C),
        unselectedTextColor = Color(0xFF78909C)
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = DigiWearPalette.CyberBg,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .background(Color(0xFF101722))
                    .border(width = 1.dp, color = Color(0xFF1E2C3D))
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(9.dp)
                            .background(Color(0xFF00E676), CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "DIGIWEAR // D-TERMINAL",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = "BANDAI 1997 V-PET & DIGIMON COLOR",
                            fontSize = 8.sp,
                            color = Color(0xFF80CBC4),
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        color = Color(0xFF142622),
                        shape = RoundedCornerShape(6.dp),
                        border = BorderStroke(1.dp, Color(0xFF00E676))
                    ) {
                        Text(
                            text = "0NL1NE • D-COM",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF00E676),
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }

                    IconButton(
                        onClick = { selectedTab = 4 },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = if (selectedTab == 4) Color(0xFF00E5FF) else Color(0xFF90A4AE),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFF101722)
            ) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Default.Pets, contentDescription = "V-Pet") },
                    label = { Text("V-PET", fontSize = 9.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold) },
                    colors = navItemColors
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.Default.Archive, contentDescription = "Vault") },
                    label = { Text("VAULT", fontSize = 9.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold) },
                    colors = navItemColors
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = { Icon(Icons.Default.FlashOn, contentDescription = "D-Com") },
                    label = { Text("D-COM", fontSize = 9.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold) },
                    colors = navItemColors
                )
                NavigationBarItem(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    icon = { Icon(Icons.Default.Explore, contentDescription = "Expedition") },
                    label = { Text("EXPEDITION", fontSize = 8.5.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold) },
                    colors = navItemColors
                )
                NavigationBarItem(
                    selected = selectedTab == 4,
                    onClick = { selectedTab = 4 },
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                    label = { Text("SETTINGS", fontSize = 8.5.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold) },
                    colors = navItemColors
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (selectedTab) {
                0 -> PetDashboardScreen(db, wearableManager, onNavigateToSettings = { selectedTab = 4 })
                1 -> DigiVaultScreen(db, wearableManager)
                2 -> BattleHubScreen(db, wearableManager)
                3 -> ExpeditionScreen(db, wearableManager)
                4 -> SettingsScreen(db, wearableManager)
            }
        }
    }
}

