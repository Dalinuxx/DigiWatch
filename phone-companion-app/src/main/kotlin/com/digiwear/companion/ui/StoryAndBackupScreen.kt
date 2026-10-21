package com.digiwear.companion.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.digiwear.companion.data.PhoneDatabase
import com.digiwear.companion.datalayer.PhoneWearableManager

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun StoryAndBackupScreen(
    db: PhoneDatabase,
    wearableManager: PhoneWearableManager
) {
    var selectedSubTab by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F141C))
    ) {
        PrimaryTabRow(
            selectedTabIndex = selectedSubTab,
            containerColor = Color(0xFF141A24),
            contentColor = Color(0xFF26A69A),
            modifier = Modifier.fillMaxWidth()
        ) {
            Tab(
                selected = selectedSubTab == 0,
                onClick = { selectedSubTab = 0 },
                text = {
                    Text(
                        text = "30-SECTOR QUEST",
                        fontSize = 11.sp,
                        fontWeight = if (selectedSubTab == 0) FontWeight.Bold else FontWeight.Normal,
                        fontFamily = FontFamily.Monospace,
                        color = if (selectedSubTab == 0) Color(0xFF26A69A) else Color.Gray
                    )
                }
            )
            Tab(
                selected = selectedSubTab == 1,
                onClick = { selectedSubTab = 1 },
                text = {
                    Text(
                        text = "BACKUP & JSON",
                        fontSize = 11.sp,
                        fontWeight = if (selectedSubTab == 1) FontWeight.Bold else FontWeight.Normal,
                        fontFamily = FontFamily.Monospace,
                        color = if (selectedSubTab == 1) Color(0xFFCE93D8) else Color.Gray
                    )
                }
            )
        }

        Box(modifier = Modifier.weight(1f)) {
            when (selectedSubTab) {
                0 -> CampaignEditorScreen(db, wearableManager)
                1 -> BackupRestoreScreen(db, wearableManager)
            }
        }
    }
}
