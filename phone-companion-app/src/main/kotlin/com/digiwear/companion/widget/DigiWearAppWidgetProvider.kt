package com.digiwear.companion.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.digiwear.companion.MainActivity
import com.digiwear.companion.R
import com.digiwear.companion.data.PhoneDatabase
import com.digiwear.companion.data.SyncedPetEntity
import com.digiwear.companion.datalayer.PhoneWearableManager
import com.digiwear.companion.render.PixelArtMatrices
import com.digiwear.core.model.PetSpecies
import com.digiwear.core.model.SpriteFrame
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Android Home Screen AppWidget displaying live virtual pet partner, physiological meters,
 * and one-tap quick-care actions (Feed & Flush) directly from the launcher.
 */
class DigiWearAppWidgetProvider : AppWidgetProvider() {

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        when (intent.action) {
            ACTION_QUICK_FEED -> {
                CoroutineScope(Dispatchers.IO).launch {
                    val wearableManager = PhoneWearableManager(context)
                    val updatedState = wearableManager.sendRemoteCareCommand("FEED")
                    val db = PhoneDatabase.getInstance(context)
                    if (updatedState != null) {
                        val species = PetSpecies.SPECIES_REGISTRY[updatedState.speciesId] ?: PetSpecies.SPECIES_REGISTRY[101]!!
                        db.phoneDao().insertSnapshot(
                            SyncedPetEntity(
                                speciesId = updatedState.speciesId,
                                speciesName = species.name,
                                currentHp = updatedState.currentHp,
                                maxHp = updatedState.maxHp,
                                hunger = updatedState.hunger,
                                strength = updatedState.strength,
                                weight = updatedState.weight,
                                ageSeconds = updatedState.ageSeconds,
                                careMistakes = updatedState.careMistakes,
                                effort = updatedState.effort,
                                overfeedCount = updatedState.overfeedCount,
                                totalBattles = updatedState.totalBattles,
                                victories = updatedState.victories
                            )
                        )
                    } else {
                        val latest = db.phoneDao().getLatestSnapshot()
                        if (latest != null) {
                            db.phoneDao().insertSnapshot(
                                latest.copy(
                                    id = 0,
                                    hunger = (latest.hunger + 1).coerceAtMost(4),
                                    weight = latest.weight + 1,
                                    syncedTimestampUtc = System.currentTimeMillis() / 1000L
                                )
                            )
                        }
                    }
                    updateAllWidgets(context)
                }
            }
            ACTION_QUICK_CLEAN -> {
                CoroutineScope(Dispatchers.IO).launch {
                    val wearableManager = PhoneWearableManager(context)
                    val updatedState = wearableManager.sendRemoteCareCommand("CLEAN")
                    val db = PhoneDatabase.getInstance(context)
                    if (updatedState != null) {
                        val species = PetSpecies.SPECIES_REGISTRY[updatedState.speciesId] ?: PetSpecies.SPECIES_REGISTRY[101]!!
                        db.phoneDao().insertSnapshot(
                            SyncedPetEntity(
                                speciesId = updatedState.speciesId,
                                speciesName = species.name,
                                currentHp = updatedState.currentHp,
                                maxHp = updatedState.maxHp,
                                hunger = updatedState.hunger,
                                strength = updatedState.strength,
                                weight = updatedState.weight,
                                ageSeconds = updatedState.ageSeconds,
                                careMistakes = updatedState.careMistakes,
                                effort = updatedState.effort,
                                overfeedCount = updatedState.overfeedCount,
                                totalBattles = updatedState.totalBattles,
                                victories = updatedState.victories
                            )
                        )
                    }
                    updateAllWidgets(context)
                }
            }
        }
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        CoroutineScope(Dispatchers.IO).launch {
            val db = PhoneDatabase.getInstance(context)
            val latest = db.phoneDao().getLatestSnapshot()
            val speciesId = latest?.speciesId ?: 101
            val species = PetSpecies.SPECIES_REGISTRY[speciesId] ?: PetSpecies.SPECIES_REGISTRY[101]!!
            val hunger = latest?.hunger ?: 4
            val strength = latest?.strength ?: 4
            val weight = latest?.weight ?: 5
            val careMistakes = latest?.careMistakes ?: 0
            val battles = latest?.totalBattles ?: 0
            val victories = latest?.victories ?: 0

            val bitmap = PixelArtMatrices.getAndroidBitmap(speciesId, SpriteFrame.IDLE_A, false)

            for (appWidgetId in appWidgetIds) {
                val views = RemoteViews(context.packageName, R.layout.digiwear_app_widget)

                views.setImageViewBitmap(R.id.widget_pet_sprite, bitmap)
                views.setTextViewText(R.id.widget_pet_name, species.name)
                views.setTextViewText(R.id.widget_pet_stage_attr, "• ${species.stage.displayName}")
                views.setTextViewText(
                    R.id.widget_pet_stats,
                    "♥".repeat(hunger.coerceIn(0, 4)) + "♡".repeat(4 - hunger.coerceIn(0, 4)) + "  " +
                    "★".repeat(strength.coerceIn(0, 4)) + "☆".repeat(4 - strength.coerceIn(0, 4))
                )
                views.setTextViewText(
                    R.id.widget_pet_meta,
                    "Wt: ${weight}g • Care: $careMistakes • Battles: $victories/$battles"
                )

                // Launch MainActivity when widget is tapped
                val intent = Intent(context, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                }
                val pendingIntent = PendingIntent.getActivity(
                    context,
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                views.setOnClickPendingIntent(R.id.widget_root, pendingIntent)

                // Quick Care: Feed Button
                val feedIntent = Intent(context, DigiWearAppWidgetProvider::class.java).apply {
                    action = ACTION_QUICK_FEED
                }
                val feedPendingIntent = PendingIntent.getBroadcast(
                    context,
                    101,
                    feedIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                views.setOnClickPendingIntent(R.id.widget_btn_feed, feedPendingIntent)

                // Quick Care: Clean Button
                val cleanIntent = Intent(context, DigiWearAppWidgetProvider::class.java).apply {
                    action = ACTION_QUICK_CLEAN
                }
                val cleanPendingIntent = PendingIntent.getBroadcast(
                    context,
                    102,
                    cleanIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                views.setOnClickPendingIntent(R.id.widget_btn_clean, cleanPendingIntent)

                appWidgetManager.updateAppWidget(appWidgetId, views)
            }
        }
    }

    companion object {
        const val ACTION_QUICK_FEED = "com.digiwear.companion.widget.ACTION_QUICK_FEED"
        const val ACTION_QUICK_CLEAN = "com.digiwear.companion.widget.ACTION_QUICK_CLEAN"

        /**
         * Triggers an immediate refresh of all active DigiWear home screen widgets.
         */
        fun updateAllWidgets(context: Context) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val componentName = ComponentName(context, DigiWearAppWidgetProvider::class.java)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(componentName)
            if (appWidgetIds.isNotEmpty()) {
                val intent = Intent(context, DigiWearAppWidgetProvider::class.java).apply {
                    action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
                    putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds)
                }
                context.sendBroadcast(intent)
            }
        }
    }
}
