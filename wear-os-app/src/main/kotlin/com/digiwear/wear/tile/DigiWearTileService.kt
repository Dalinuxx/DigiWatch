package com.digiwear.wear.tile

import android.content.Context
import androidx.concurrent.futures.ResolvableFuture
import androidx.wear.protolayout.ActionBuilders
import androidx.wear.protolayout.ColorBuilders.argb
import androidx.wear.protolayout.DimensionBuilders.expand
import androidx.wear.protolayout.DimensionBuilders.sp
import androidx.wear.protolayout.LayoutElementBuilders
import androidx.wear.protolayout.ModifiersBuilders
import androidx.wear.protolayout.ResourceBuilders
import androidx.wear.protolayout.TimelineBuilders
import androidx.wear.tiles.RequestBuilders
import androidx.wear.tiles.TileBuilders
import androidx.wear.tiles.TileService
import com.digiwear.core.model.CampaignStory
import com.digiwear.core.model.PetSpecies
import com.digiwear.wear.MainActivity
import com.digiwear.wear.data.PetRepository
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

/**
 * Glanceable Wear OS Tile for instant wrist access to active partner status,
 * hunger/strength hearts, and today's adventure steps.
 */
class DigiWearTileService : TileService() {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onDestroy() {
        scope.cancel()
        super.onDestroy()
    }

    override fun onTileRequest(requestParams: RequestBuilders.TileRequest): ListenableFuture<TileBuilders.Tile> {
        val future = ResolvableFuture.create<TileBuilders.Tile>()
        scope.launch {
            try {
                val repo = PetRepository.getInstance(applicationContext)
                val pet = repo.getPetState()
                val species = PetSpecies.getById(pet.speciesId)
                val campaign = repo.getActiveCampaign()
                val progress = repo.getAdventureProgress()
                val activeNode = storyNode(campaign, progress.currentNodeIndex)

                val layout = buildTileLayout(
                    context = applicationContext,
                    speciesName = species.name,
                    stageName = species.stage.displayName,
                    hunger = pet.hunger,
                    strength = pet.strength,
                    weight = pet.weight,
                    activeSector = activeNode?.name ?: "File Island",
                    currentSteps = progress.accumulatedStepsCurrentNode,
                    targetSteps = activeNode?.requiredSteps ?: 1000
                )

                val tile = TileBuilders.Tile.Builder()
                    .setResourcesVersion(RESOURCES_VERSION)
                    .setTileTimeline(
                        TimelineBuilders.Timeline.Builder()
                            .addTimelineEntry(
                                TimelineBuilders.TimelineEntry.Builder()
                                    .setLayout(layout)
                                    .build()
                            )
                            .build()
                    )
                    .build()

                future.set(tile)
            } catch (e: Exception) {
                future.setException(e)
            }
        }
        return future
    }

    override fun onTileResourcesRequest(requestParams: RequestBuilders.ResourcesRequest): ListenableFuture<ResourceBuilders.Resources> {
        val future = ResolvableFuture.create<ResourceBuilders.Resources>()
        val res = ResourceBuilders.Resources.Builder()
            .setVersion(RESOURCES_VERSION)
            .build()
        future.set(res)
        return future
    }

    private fun storyNode(campaign: CampaignStory, index: Int) =
        campaign.nodes.getOrNull(index.coerceIn(0, (campaign.nodes.size - 1).coerceAtLeast(0)))

    private fun buildTileLayout(
        context: Context,
        speciesName: String,
        stageName: String,
        hunger: Int,
        strength: Int,
        weight: Int,
        activeSector: String,
        currentSteps: Int,
        targetSteps: Int
    ): LayoutElementBuilders.Layout {
        val launchIntent = ActionBuilders.LaunchAction.Builder()
            .setAndroidActivity(
                ActionBuilders.AndroidActivity.Builder()
                    .setPackageName(context.packageName)
                    .setClassName(MainActivity::class.java.name)
                    .build()
            )
            .build()

        val clickModifier = ModifiersBuilders.Modifiers.Builder()
            .setClickable(
                ModifiersBuilders.Clickable.Builder()
                    .setOnClick(launchIntent)
                    .setId("open_app")
                    .build()
            )
            .build()

        val column = LayoutElementBuilders.Column.Builder()
            .setWidth(expand())
            .setHeight(expand())
            .setModifiers(clickModifier)
            .setHorizontalAlignment(LayoutElementBuilders.HORIZONTAL_ALIGN_CENTER)

        // Top Header
        column.addContent(
            LayoutElementBuilders.Text.Builder()
                .setText("DIGIWEAR V-PET")
                .setFontStyle(
                    LayoutElementBuilders.FontStyle.Builder()
                        .setSize(sp(10f))
                        .setColor(argb(0xFF00E5FF.toInt()))
                        .build()
                )
                .build()
        )

        // Species Name & Stage
        column.addContent(
            LayoutElementBuilders.Text.Builder()
                .setText("$speciesName • $stageName")
                .setFontStyle(
                    LayoutElementBuilders.FontStyle.Builder()
                        .setSize(sp(14f))
                        .setColor(argb(0xFFFFFFFF.toInt()))
                        .build()
                )
                .build()
        )

        // Physiological Meters (LCD Block Bar Style)
        val hVal = hunger.coerceIn(0, 4)
        val sVal = strength.coerceIn(0, 4)
        val hungerBar = "HGR [" + "#".repeat(hVal) + "-".repeat(4 - hVal) + "]"
        val strengthBar = "STR [" + "#".repeat(sVal) + "-".repeat(4 - sVal) + "]"

        column.addContent(
            LayoutElementBuilders.Text.Builder()
                .setText("$hungerBar  $strengthBar")
                .setFontStyle(
                    LayoutElementBuilders.FontStyle.Builder()
                        .setSize(sp(11f))
                        .setColor(argb(0xFFFFD54F.toInt()))
                        .build()
                )
                .build()
        )

        // Weight
        column.addContent(
            LayoutElementBuilders.Text.Builder()
                .setText("Weight: ${weight}g")
                .setFontStyle(
                    LayoutElementBuilders.FontStyle.Builder()
                        .setSize(sp(10f))
                        .setColor(argb(0xFFB0BEC5.toInt()))
                        .build()
                )
                .build()
        )

        // Adventure Progress
        column.addContent(
            LayoutElementBuilders.Text.Builder()
                .setText("Adv: $currentSteps / $targetSteps steps")
                .setFontStyle(
                    LayoutElementBuilders.FontStyle.Builder()
                        .setSize(sp(11f))
                        .setColor(argb(0xFF00E676.toInt()))
                        .build()
                )
                .build()
        )

        column.addContent(
            LayoutElementBuilders.Text.Builder()
                .setText("TAP TO OPEN")
                .setFontStyle(
                    LayoutElementBuilders.FontStyle.Builder()
                        .setSize(sp(8f))
                        .setColor(argb(0xFF80D8FF.toInt()))
                        .build()
                )
                .build()
        )

        return LayoutElementBuilders.Layout.Builder().setRoot(column.build()).build()
    }

    companion object {
        private const val RESOURCES_VERSION = "1"

        fun requestUpdate(context: Context) {
            try {
                TileService.getUpdater(context).requestUpdate(DigiWearTileService::class.java)
            } catch (_: Exception) {
            }
        }
    }
}
