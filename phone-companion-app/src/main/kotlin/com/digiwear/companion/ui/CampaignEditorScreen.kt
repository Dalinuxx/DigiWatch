package com.digiwear.companion.ui

import androidx.compose.runtime.Composable
import com.digiwear.companion.data.PhoneDatabase
import com.digiwear.companion.datalayer.PhoneWearableManager

/**
 * Backward compatibility wrapper delegating to the streamlined [ExpeditionScreen].
 */
@Composable
fun CampaignEditorScreen(
    db: PhoneDatabase,
    wearableManager: PhoneWearableManager
) {
    ExpeditionScreen(db, wearableManager)
}
