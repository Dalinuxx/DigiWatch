package com.digiwatch.companion.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.digiwatch.companion.MainActivity
import com.digiwatch.companion.R
import com.digiwatch.companion.data.PhoneDatabase
import com.digiwatch.companion.render.PixelArtMatrices
import com.digiwatch.core.model.PetSpecies
import com.digiwatch.core.model.SpriteFrame
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Android Home Screen AppWidget displaying live virtual pet partner, physiological meters, and stats.
 */
class DigiWatchAppWidgetProvider : AppWidgetProvider() {

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
                val views = RemoteViews(context.packageName, R.layout.digiwatch_app_widget)

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

                appWidgetManager.updateAppWidget(appWidgetId, views)
            }
        }
    }

    companion object {
        /**
         * Triggers an immediate refresh of all active DigiWatch home screen widgets.
         */
        fun updateAllWidgets(context: Context) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val componentName = ComponentName(context, DigiWatchAppWidgetProvider::class.java)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(componentName)
            if (appWidgetIds.isNotEmpty()) {
                val intent = Intent(context, DigiWatchAppWidgetProvider::class.java).apply {
                    action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
                    putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds)
                }
                context.sendBroadcast(intent)
            }
        }
    }
}
