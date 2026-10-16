package com.digiwatch.companion

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
import com.digiwatch.companion.data.PhoneDatabase
import com.digiwatch.companion.datalayer.PhoneWearableManager
import com.digiwatch.companion.nfc.NfcDuelManager
import com.digiwatch.companion.ui.BattleHubScreen
import com.digiwatch.companion.ui.DigiVaultScreen
import com.digiwatch.companion.ui.DigiWatchPalette
import com.digiwatch.companion.ui.PetDashboardScreen
import com.digiwatch.companion.ui.SpriteStudioScreen
import com.digiwatch.companion.ui.StoryAndBackupScreen
import kotlinx.coroutines.flow.MutableStateFlow

object NfcBattleBridge {
    val incomingCodeFlow = MutableStateFlow<String?>(null)
    var pendingTagToWrite: Tag? = null
}

class MainActivity : ComponentActivity() {

    private lateinit var db: PhoneDatabase
    private lateinit var wearableManager: PhoneWearableManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = PhoneDatabase.getInstance(this)
        wearableManager = PhoneWearableManager(this)

        setContent {
            MaterialTheme(
                colorScheme = darkColorScheme(
                    primary = DigiWatchPalette.DigiviceCyan,
                    secondary = DigiWatchPalette.CrestGold,
                    background = DigiWatchPalette.CyberBg,
                    surface = DigiWatchPalette.CardSurface
                )
            ) {
                MainPhoneApp(db, wearableManager)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (NfcDuelManager.isNfcEnabled(this)) {
            NfcDuelManager.enableReaderMode(this) { opponentCode, tag ->
                NfcBattleBridge.pendingTagToWrite = tag
                NfcBattleBridge.incomingCodeFlow.value = opponentCode
            }
        }
    }

    override fun onPause() {
        super.onPause()
        NfcDuelManager.disableReaderMode(this)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
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
        containerColor = DigiWatchPalette.CyberBg,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
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
                            text = "DIGIWATCH // D-TERMINAL",
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
                    icon = { Icon(Icons.Default.AutoAwesome, contentDescription = "DigiDex") },
                    label = { Text("DIGIDEX", fontSize = 9.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold) },
                    colors = navItemColors
                )
                NavigationBarItem(
                    selected = selectedTab == 4,
                    onClick = { selectedTab = 4 },
                    icon = { Icon(Icons.Default.Explore, contentDescription = "Quest") },
                    label = { Text("QUEST", fontSize = 9.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold) },
                    colors = navItemColors
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (selectedTab) {
                0 -> PetDashboardScreen(db, wearableManager)
                1 -> DigiVaultScreen(db, wearableManager)
                2 -> BattleHubScreen(wearableManager)
                3 -> SpriteStudioScreen(db, wearableManager)
                4 -> StoryAndBackupScreen(db, wearableManager)
            }
        }
    }
}

