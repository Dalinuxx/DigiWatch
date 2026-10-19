package com.digiwear.wear.data

import android.content.Context
import androidx.room.Room
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.digiwear.core.adventure.AdventureEngine
import com.digiwear.core.adventure.AdventureStepResult
import com.digiwear.core.engine.EvolutionEngine
import com.digiwear.core.engine.PetSimulationEngine
import com.digiwear.core.model.AdventureProgress
import com.digiwear.core.model.BackgroundTheme
import com.digiwear.core.model.BossEncounter
import com.digiwear.core.model.CampaignStory
import com.digiwear.core.model.PetSpecies
import com.digiwear.core.model.PetState
import com.digiwear.wear.worker.PetDecayWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class PetRepository private constructor(context: Context) {

    private val appContext = context.applicationContext

    private val db = Room.databaseBuilder(
        appContext,
        DigiWearDatabase::class.java,
        "digiwear_wear.db"
    ).fallbackToDestructiveMigration().build()

    private val dao = db.petDao()

    private val prefs = appContext.getSharedPreferences("digiwear_wear_prefs", Context.MODE_PRIVATE)

    private val _themeFlow = MutableStateFlow(
        BackgroundTheme.fromId(prefs.getString("selected_theme", BackgroundTheme.CLASSIC_VPET_LCD.id))
    )
    val themeFlow: Flow<BackgroundTheme> = _themeFlow.asStateFlow()

    private val _vibrationFlow = MutableStateFlow(
        prefs.getBoolean("vibration_enabled", true)
    )
    val vibrationFlow: Flow<Boolean> = _vibrationFlow.asStateFlow()

    private val _threeButtonModeFlow = MutableStateFlow(
        prefs.getBoolean("vpet_3button_mode", true)
    )
    val threeButtonModeFlow: Flow<Boolean> = _threeButtonModeFlow.asStateFlow()

    private val _pendulumShakeModeFlow = MutableStateFlow(
        prefs.getBoolean("pendulum_shake_mode", false)
    )
    val pendulumShakeModeFlow: StateFlow<Boolean> = _pendulumShakeModeFlow.asStateFlow()

    private val _soundEnabledFlow = MutableStateFlow(
        prefs.getBoolean("sound_enabled", false)
    )
    val soundEnabledFlow: StateFlow<Boolean> = _soundEnabledFlow.asStateFlow()

    private val _nfcBridgeModeFlow = MutableStateFlow(
        prefs.getString("nfc_bridge_mode", "NFC_DUEL") ?: "NFC_DUEL"
    )
    val nfcBridgeModeFlow: StateFlow<String> = _nfcBridgeModeFlow.asStateFlow()

    private val _displayShapeOverrideFlow = MutableStateFlow(
        prefs.getString("display_shape_override", "AUTO") ?: "AUTO"
    )
    val displayShapeOverrideFlow: StateFlow<String> = _displayShapeOverrideFlow.asStateFlow()

    private val _dailyStepGoalFlow = MutableStateFlow(
        prefs.getInt("daily_step_goal", 5000)
    )
    val dailyStepGoalFlow: StateFlow<Int> = _dailyStepGoalFlow.asStateFlow()

    private val _contrastLevelFlow = MutableStateFlow(
        prefs.getString("lcd_contrast_level", "MEDIUM") ?: "MEDIUM"
    )
    val contrastLevelFlow: StateFlow<String> = _contrastLevelFlow.asStateFlow()

    private val _sleepScheduleFlow = MutableStateFlow(
        prefs.getString("sleep_schedule", "21:00_07:00") ?: "21:00_07:00"
    )
    val sleepScheduleFlow: StateFlow<String> = _sleepScheduleFlow.asStateFlow()

    private val _ambientStyleFlow = MutableStateFlow(
        prefs.getString("ambient_display_style", "MINIMAL_DOT") ?: "MINIMAL_DOT"
    )
    val ambientStyleFlow: StateFlow<String> = _ambientStyleFlow.asStateFlow()

    private val _hazardAlertsFlow = MutableStateFlow(
        prefs.getBoolean("hazard_alerts_enabled", true)
    )
    val hazardAlertsFlow: StateFlow<Boolean> = _hazardAlertsFlow.asStateFlow()

    private val _clockFormatFlow = MutableStateFlow(
        prefs.getBoolean("clock_format_24h", false)
    )
    val clockFormatFlow: StateFlow<Boolean> = _clockFormatFlow.asStateFlow()

    private val _lifespanCanonicalEnabledFlow = MutableStateFlow(
        prefs.getBoolean("lifespan_canonical_enabled", false)
    )
    val lifespanCanonicalEnabledFlow: StateFlow<Boolean> = _lifespanCanonicalEnabledFlow.asStateFlow()

    private val _beamedCommCodeFlow = MutableStateFlow<String?>(null)
    val beamedCommCodeFlow: StateFlow<String?> = _beamedCommCodeFlow.asStateFlow()

    private val _campaignJsonFlow = MutableStateFlow(
        prefs.getString("custom_campaign_json", null) ?: AdventureEngine.DEFAULT_CAMPAIGN_JSON
    )
    val campaignJsonFlow: StateFlow<String> = _campaignJsonFlow.asStateFlow()

    @Volatile
    private var cachedCampaign: Pair<String, CampaignStory>? = null

    fun setTheme(theme: BackgroundTheme) {
        prefs.edit().putString("selected_theme", theme.id).apply()
        _themeFlow.value = theme
    }

    fun setVibrationEnabled(enabled: Boolean) {
        prefs.edit().putBoolean("vibration_enabled", enabled).apply()
        _vibrationFlow.value = enabled
    }

    fun setThreeButtonMode(enabled: Boolean) {
        prefs.edit().putBoolean("vpet_3button_mode", enabled).apply()
        _threeButtonModeFlow.value = enabled
    }

    fun setPendulumShakeMode(enabled: Boolean) {
        prefs.edit().putBoolean("pendulum_shake_mode", enabled).apply()
        _pendulumShakeModeFlow.value = enabled
    }

    fun setSoundEnabled(enabled: Boolean) {
        prefs.edit().putBoolean("sound_enabled", enabled).apply()
        _soundEnabledFlow.value = enabled
    }

    fun setNfcBridgeMode(mode: String) {
        prefs.edit().putString("nfc_bridge_mode", mode).apply()
        _nfcBridgeModeFlow.value = mode
    }

    fun setDisplayShapeOverride(mode: String) {
        prefs.edit().putString("display_shape_override", mode).apply()
        _displayShapeOverrideFlow.value = mode
    }

    fun setDailyStepGoal(goal: Int) {
        prefs.edit().putInt("daily_step_goal", goal).apply()
        _dailyStepGoalFlow.value = goal
    }

    fun setContrastLevel(level: String) {
        prefs.edit().putString("lcd_contrast_level", level).apply()
        _contrastLevelFlow.value = level
    }

    fun setSleepSchedule(schedule: String) {
        prefs.edit().putString("sleep_schedule", schedule).apply()
        _sleepScheduleFlow.value = schedule
    }

    fun setAmbientStyle(style: String) {
        prefs.edit().putString("ambient_display_style", style).apply()
        _ambientStyleFlow.value = style
    }

    fun setHazardAlertsEnabled(enabled: Boolean) {
        prefs.edit().putBoolean("hazard_alerts_enabled", enabled).apply()
        _hazardAlertsFlow.value = enabled
    }

    fun setClockFormat24h(is24h: Boolean) {
        prefs.edit().putBoolean("clock_format_24h", is24h).apply()
        _clockFormatFlow.value = is24h
    }

    fun setLifespanCanonicalEnabled(enabled: Boolean) {
        prefs.edit().putBoolean("lifespan_canonical_enabled", enabled).apply()
        _lifespanCanonicalEnabledFlow.value = enabled
    }

    fun getSettingsMap(): Map<String, String> = mapOf(
        "theme" to _themeFlow.value.id,
        "vibration" to _vibrationFlow.value.toString(),
        "vpet_3button_mode" to _threeButtonModeFlow.value.toString(),
        "pendulum_shake_mode" to _pendulumShakeModeFlow.value.toString(),
        "sound_enabled" to _soundEnabledFlow.value.toString(),
        "nfc_bridge_mode" to _nfcBridgeModeFlow.value,
        "display_shape_override" to _displayShapeOverrideFlow.value,
        "daily_step_goal" to _dailyStepGoalFlow.value.toString(),
        "lcd_contrast_level" to _contrastLevelFlow.value,
        "sleep_schedule" to _sleepScheduleFlow.value,
        "ambient_display_style" to _ambientStyleFlow.value,
        "hazard_alerts_enabled" to _hazardAlertsFlow.value.toString(),
        "clock_format_24h" to _clockFormatFlow.value.toString(),
        "lifespan_canonical_enabled" to _lifespanCanonicalEnabledFlow.value.toString()
    )

    fun setBeamedCommCode(code: String?) {
        _beamedCommCodeFlow.value = code
    }

    fun saveCustomCampaignJson(jsonStr: String) {
        val validJson = try {
            val parsed = AdventureEngine.parseCampaign(jsonStr)
            if (parsed.nodes.isNotEmpty()) {
                cachedCampaign = jsonStr to parsed
                jsonStr
            } else AdventureEngine.DEFAULT_CAMPAIGN_JSON
        } catch (e: Exception) {
            AdventureEngine.DEFAULT_CAMPAIGN_JSON
        }
        prefs.edit().putString("custom_campaign_json", validJson).apply()
        _campaignJsonFlow.value = validJson
    }

    fun getActiveCampaign(): CampaignStory {
        val rawJson = _campaignJsonFlow.value
        val existing = cachedCampaign
        if (existing != null && existing.first == rawJson) {
            return existing.second
        }
        val parsed = try {
            val c = AdventureEngine.parseCampaign(rawJson)
            if (c.nodes.isNotEmpty()) c else AdventureEngine.parseCampaign(AdventureEngine.DEFAULT_CAMPAIGN_JSON)
        } catch (e: Exception) {
            AdventureEngine.parseCampaign(AdventureEngine.DEFAULT_CAMPAIGN_JSON)
        }
        cachedCampaign = rawJson to parsed
        return parsed
    }

    val petStateFlow: Flow<PetState> = dao.getPetStateFlow().map { entity ->
        val domain = entity?.toDomain() ?: PetState(speciesId = 201)
        if (domain.speciesId <= 1 || !PetSpecies.SPECIES_REGISTRY.containsKey(domain.speciesId)) {
            domain.copy(speciesId = 201)
        } else {
            domain
        }
    }.distinctUntilChanged()

    val adventureStateFlow: Flow<AdventureProgress> = dao.getAdventureStateFlow().map { entity ->
        if (entity != null) {
            val flags = if (entity.clearedFlagsCsv.isBlank()) emptySet() else entity.clearedFlagsCsv.split(",").toSet()
            val eggs = if (entity.unlockedEggsCsv.isBlank()) emptySet() else entity.unlockedEggsCsv.split(",").mapNotNull { it.toIntOrNull() }.toSet()
            val items = if (entity.collectedItemsCsv.isBlank()) emptySet() else entity.collectedItemsCsv.split(",").filter { it.isNotBlank() }.toSet()
            val campaigns = if (entity.unlockedCampaignsCsv.isBlank()) emptySet() else entity.unlockedCampaignsCsv.split(",").filter { it.isNotBlank() }.toSet()
            AdventureProgress(
                campaignId = entity.campaignId,
                currentNodeIndex = entity.currentNodeIndex,
                accumulatedStepsCurrentNode = entity.accumulatedStepsCurrentNode,
                totalAdventureSteps = entity.totalAdventureSteps,
                clearedFlags = flags,
                isCompleted = entity.isCompleted,
                unlockedEggs = eggs,
                collectedItems = items,
                unlockedCampaigns = campaigns
            )
        } else {
            AdventureProgress()
        }
    }.distinctUntilChanged()

    suspend fun getPetState(): PetState = withContext(Dispatchers.IO) {
        dao.getPetState()?.toDomain() ?: PetState()
    }

    suspend fun getAdventureProgress(): AdventureProgress = withContext(Dispatchers.IO) {
        val currentEntity = dao.getAdventureState()
        if (currentEntity != null) {
            val flags = if (currentEntity.clearedFlagsCsv.isBlank()) emptySet() else currentEntity.clearedFlagsCsv.split(",").toSet()
            val eggs = if (currentEntity.unlockedEggsCsv.isBlank()) emptySet() else currentEntity.unlockedEggsCsv.split(",").mapNotNull { it.toIntOrNull() }.toSet()
            val items = if (currentEntity.collectedItemsCsv.isBlank()) emptySet() else currentEntity.collectedItemsCsv.split(",").filter { it.isNotBlank() }.toSet()
            val campaigns = if (currentEntity.unlockedCampaignsCsv.isBlank()) emptySet() else currentEntity.unlockedCampaignsCsv.split(",").filter { it.isNotBlank() }.toSet()
            AdventureProgress(
                campaignId = currentEntity.campaignId,
                currentNodeIndex = currentEntity.currentNodeIndex,
                accumulatedStepsCurrentNode = currentEntity.accumulatedStepsCurrentNode,
                totalAdventureSteps = currentEntity.totalAdventureSteps,
                clearedFlags = flags,
                isCompleted = currentEntity.isCompleted,
                unlockedEggs = eggs,
                collectedItems = items,
                unlockedCampaigns = campaigns
            )
        } else {
            AdventureProgress()
        }
    }

    suspend fun savePetState(state: PetState) = withContext(Dispatchers.IO) {
        dao.insertOrUpdatePet(PetEntity.fromDomain(state))
    }

    val memorialsFlow: Flow<List<com.digiwear.core.model.MemorialRecord>> = dao.getAllMemorialsFlow().map { list ->
        list.map { it.toDomain() }
    }.distinctUntilChanged()

    suspend fun getMemorials(): List<com.digiwear.core.model.MemorialRecord> = withContext(Dispatchers.IO) {
        dao.getAllMemorials().map { it.toDomain() }
    }

    suspend fun recordMemorial(record: com.digiwear.core.model.MemorialRecord): Long = withContext(Dispatchers.IO) {
        dao.insertMemorial(MemorialEntity.fromDomain(record))
    }

    /**
     * Resets current state to hatch a new Digi-Egg.
     */
    suspend fun hatchNewEgg(starterSpeciesId: Int = 101): PetState = withContext(Dispatchers.IO) {
        val species = PetSpecies.getById(starterSpeciesId)
        val fresh = PetState(
            speciesId = starterSpeciesId,
            currentHp = species.baseHp,
            maxHp = species.baseHp,
            hunger = 4,
            strength = 4,
            weight = species.baselineWeight,
            ageSeconds = 0L,
            careMistakes = 0,
            effort = 0,
            overfeedCount = 0,
            totalBattles = 0,
            victories = 0,
            wasteCount = 0,
            isSleeping = false,
            lightsOff = false,
            isInjured = false,
            lastProcessedTimestampUtc = System.currentTimeMillis() / 1000L,
            attentionCallActive = false,
            attentionCallTimestampUtc = null
        )
        savePetState(fresh)
        fresh
    }

    /**
     * Hatches a Generation N+1 Traited Digitama inheriting stat and lifespan bonuses from the departed partner.
     */
    suspend fun hatchTraitedEgg(starterSpeciesId: Int = 101): PetState = withContext(Dispatchers.IO) {
        val current = getPetState()
        val memorial = PetSimulationEngine.createMemorialRecord(current)
        dao.insertMemorial(MemorialEntity.fromDomain(memorial))
        val nextPet = PetSimulationEngine.createTraitedEggState(current, starterSpeciesId)
        dao.insertOrUpdatePet(PetEntity.fromDomain(nextPet))
        nextPet
    }

    /**
     * Processes time-delta decay based on UTC timestamps.
     * Coalesces SQLite writes so flash storage is only written when physiological metrics change
     * or at least 60 seconds have elapsed since the last persisted checkpoint.
     */
    suspend fun processDecay(currentUtc: Long = System.currentTimeMillis() / 1000L): PetState = withContext(Dispatchers.IO) {
        val current = getPetState()
        val isCanonical = _lifespanCanonicalEnabledFlow.value
        val updated = PetSimulationEngine.processDelta(
            currentState = current,
            currentTimestampUtc = currentUtc,
            isCanonicalLifespanEnabled = isCanonical
        )
        if (updated.isDeceased && !current.isDeceased) {
            val memorial = PetSimulationEngine.createMemorialRecord(updated)
            dao.insertMemorial(MemorialEntity.fromDomain(memorial))
        }
        val evolved = if (!updated.isDeceased) EvolutionEngine.evaluateEvolution(updated) else null
        val finalState = if (evolved != null) {
            EvolutionEngine.evolve(updated, evolved, currentUtc)
        } else {
            updated
        }

        val biologicalStateChanged =
            finalState.speciesId != current.speciesId ||
            finalState.hunger != current.hunger ||
            finalState.strength != current.strength ||
            finalState.wasteCount != current.wasteCount ||
            finalState.careMistakes != current.careMistakes ||
            finalState.isSleeping != current.isSleeping ||
            finalState.isInjured != current.isInjured ||
            finalState.isDeceased != current.isDeceased ||
            finalState.attentionCallActive != current.attentionCallActive ||
            finalState.attentionCallTimestampUtc != current.attentionCallTimestampUtc

        val checkpointDue = current.lastProcessedTimestampUtc <= 0L ||
            (currentUtc - current.lastProcessedTimestampUtc) >= 60L

        if (biologicalStateChanged || checkpointDue) {
            savePetState(finalState)
        }
        finalState
    }

    suspend fun feedMeal(currentUtc: Long = System.currentTimeMillis() / 1000L): PetState = withContext(Dispatchers.IO) {
        val current = getPetState()
        val updated = PetSimulationEngine.feedMeal(current, currentUtc)
        savePetState(updated)
        updated
    }

    suspend fun feedProtein(currentUtc: Long = System.currentTimeMillis() / 1000L): PetState = withContext(Dispatchers.IO) {
        val current = getPetState()
        val updated = PetSimulationEngine.feedProtein(current, currentUtc)
        savePetState(updated)
        updated
    }

    suspend fun cleanWaste(): PetState = withContext(Dispatchers.IO) {
        val current = getPetState()
        val updated = PetSimulationEngine.cleanWaste(current)
        savePetState(updated)
        updated
    }

    suspend fun toggleLights(currentUtc: Long = System.currentTimeMillis() / 1000L): PetState = withContext(Dispatchers.IO) {
        val current = getPetState()
        val updated = PetSimulationEngine.toggleLights(current, currentUtc)
        savePetState(updated)
        updated
    }

    suspend fun train(won: Boolean, currentUtc: Long = System.currentTimeMillis() / 1000L): PetState = withContext(Dispatchers.IO) {
        val current = getPetState()
        val updated = PetSimulationEngine.train(current, won, currentUtc)
        savePetState(updated)
        updated
    }

    suspend fun cureInjury(): PetState = withContext(Dispatchers.IO) {
        val current = getPetState()
        val updated = PetSimulationEngine.cureInjury(current)
        savePetState(updated)
        updated
    }

    suspend fun recordBattleResult(won: Boolean): PetState = withContext(Dispatchers.IO) {
        val current = getPetState()
        val updated = current.copy(
            totalBattles = current.totalBattles + 1,
            victories = if (won) current.victories + 1 else current.victories
        )
        val evolved = EvolutionEngine.evaluateEvolution(updated)
        val finalState = if (evolved != null) {
            EvolutionEngine.evolve(updated, evolved, System.currentTimeMillis() / 1000L)
        } else {
            updated
        }
        savePetState(finalState)
        finalState
    }

    suspend fun addAdventureSteps(steps: Int): AdventureStepResult = withContext(Dispatchers.IO) {
        val story = getActiveCampaign()
        val currentEntity = dao.getAdventureState()
        val flags = if (currentEntity?.clearedFlagsCsv.isNullOrBlank()) emptySet() else currentEntity!!.clearedFlagsCsv.split(",").toSet()
        val eggs = if (currentEntity?.unlockedEggsCsv.isNullOrBlank()) emptySet() else currentEntity!!.unlockedEggsCsv.split(",").mapNotNull { it.toIntOrNull() }.toSet()
        val items = if (currentEntity?.collectedItemsCsv.isNullOrBlank()) emptySet() else currentEntity!!.collectedItemsCsv.split(",").filter { it.isNotBlank() }.toSet()
        val campaigns = if (currentEntity?.unlockedCampaignsCsv.isNullOrBlank()) emptySet() else currentEntity!!.unlockedCampaignsCsv.split(",").filter { it.isNotBlank() }.toSet()
        val progress = AdventureProgress(
            campaignId = currentEntity?.campaignId ?: story.campaignId,
            currentNodeIndex = currentEntity?.currentNodeIndex ?: 0,
            accumulatedStepsCurrentNode = currentEntity?.accumulatedStepsCurrentNode ?: 0,
            totalAdventureSteps = currentEntity?.totalAdventureSteps ?: 0,
            clearedFlags = flags,
            isCompleted = currentEntity?.isCompleted ?: false,
            unlockedEggs = eggs,
            collectedItems = items,
            unlockedCampaigns = campaigns
        )

        val result = AdventureEngine.processSteps(story, progress, steps)
        val updated = result.updatedProgress
        dao.insertOrUpdateAdventure(
            AdventureEntity(
                id = 1,
                campaignId = updated.campaignId,
                currentNodeIndex = updated.currentNodeIndex,
                accumulatedStepsCurrentNode = updated.accumulatedStepsCurrentNode,
                totalAdventureSteps = updated.totalAdventureSteps,
                clearedFlagsCsv = updated.clearedFlags.joinToString(","),
                isCompleted = updated.isCompleted,
                unlockedEggsCsv = updated.unlockedEggs.joinToString(","),
                collectedItemsCsv = updated.collectedItems.joinToString(","),
                unlockedCampaignsCsv = updated.unlockedCampaigns.joinToString(",")
            )
        )
        result
    }

    suspend fun recordBossVictory(boss: BossEncounter): AdventureProgress = withContext(Dispatchers.IO) {
        val story = getActiveCampaign()
        val currentEntity = dao.getAdventureState()
        val flags = if (currentEntity?.clearedFlagsCsv.isNullOrBlank()) emptySet() else currentEntity!!.clearedFlagsCsv.split(",").toSet()
        val eggs = if (currentEntity?.unlockedEggsCsv.isNullOrBlank()) emptySet() else currentEntity!!.unlockedEggsCsv.split(",").mapNotNull { it.toIntOrNull() }.toSet()
        val items = if (currentEntity?.collectedItemsCsv.isNullOrBlank()) emptySet() else currentEntity!!.collectedItemsCsv.split(",").filter { it.isNotBlank() }.toSet()
        val campaigns = if (currentEntity?.unlockedCampaignsCsv.isNullOrBlank()) emptySet() else currentEntity!!.unlockedCampaignsCsv.split(",").filter { it.isNotBlank() }.toSet()
        val progress = AdventureProgress(
            campaignId = currentEntity?.campaignId ?: story.campaignId,
            currentNodeIndex = currentEntity?.currentNodeIndex ?: 0,
            accumulatedStepsCurrentNode = currentEntity?.accumulatedStepsCurrentNode ?: 0,
            totalAdventureSteps = currentEntity?.totalAdventureSteps ?: 0,
            clearedFlags = flags,
            isCompleted = currentEntity?.isCompleted ?: false,
            unlockedEggs = eggs,
            collectedItems = items,
            unlockedCampaigns = campaigns
        )

        val updated = AdventureEngine.recordBossVictory(story, progress, boss)
        dao.insertOrUpdateAdventure(
            AdventureEntity(
                id = 1,
                campaignId = updated.campaignId,
                currentNodeIndex = updated.currentNodeIndex,
                accumulatedStepsCurrentNode = updated.accumulatedStepsCurrentNode,
                totalAdventureSteps = updated.totalAdventureSteps,
                clearedFlagsCsv = updated.clearedFlags.joinToString(","),
                isCompleted = updated.isCompleted,
                unlockedEggsCsv = updated.unlockedEggs.joinToString(","),
                collectedItemsCsv = updated.collectedItems.joinToString(","),
                unlockedCampaignsCsv = updated.unlockedCampaigns.joinToString(",")
            )
        )
        updated
    }

    /**
     * Switches the active campaign on the watch to one of the built-in sagas.
     */
    suspend fun switchCampaign(campaignId: String) = withContext(Dispatchers.IO) {
        val builtIn = AdventureEngine.getCampaignById(campaignId)
        val json = if (builtIn != null) {
            AdventureEngine.serializeCampaign(builtIn)
        } else {
            AdventureEngine.DEFAULT_CAMPAIGN_JSON
        }
        saveCustomCampaignJson(json)
        val current = dao.getAdventureState()
        dao.insertOrUpdateAdventure(
            AdventureEntity(
                id = 1,
                campaignId = campaignId,
                currentNodeIndex = 0,
                accumulatedStepsCurrentNode = 0,
                totalAdventureSteps = current?.totalAdventureSteps ?: 0,
                clearedFlagsCsv = "",
                isCompleted = false,
                unlockedEggsCsv = current?.unlockedEggsCsv ?: "",
                collectedItemsCsv = current?.collectedItemsCsv ?: "",
                unlockedCampaignsCsv = current?.unlockedCampaignsCsv ?: ""
            )
        )
    }

    /**
     * Enqueues periodic WorkManager worker (every 15 minutes) for low-power background decay calculations.
     */
    fun scheduleBackgroundWorkers() {
        val workRequest = PeriodicWorkRequestBuilder<PetDecayWorker>(15, TimeUnit.MINUTES)
            .setConstraints(Constraints.NONE)
            .build()

        WorkManager.getInstance(appContext).enqueueUniquePeriodicWork(
            "DigiWearPetDecayWork",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }

    companion object {
        @Volatile
        private var instance: PetRepository? = null

        fun getInstance(context: Context): PetRepository {
            return instance ?: synchronized(this) {
                instance ?: PetRepository(context.applicationContext).also {
                    instance = it
                    CoroutineScope(Dispatchers.IO).launch {
                        it.scheduleBackgroundWorkers()
                    }
                }
            }
        }
    }
}
