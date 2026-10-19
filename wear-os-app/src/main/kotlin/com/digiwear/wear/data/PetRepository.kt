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
    ).build()

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

    fun getSettingsMap(): Map<String, String> = mapOf(
        "theme" to _themeFlow.value.id,
        "vibration" to _vibrationFlow.value.toString(),
        "vpet_3button_mode" to _threeButtonModeFlow.value.toString(),
        "pendulum_shake_mode" to _pendulumShakeModeFlow.value.toString(),
        "sound_enabled" to _soundEnabledFlow.value.toString(),
        "nfc_bridge_mode" to _nfcBridgeModeFlow.value,
        "display_shape_override" to _displayShapeOverrideFlow.value
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
            AdventureProgress(
                campaignId = entity.campaignId,
                currentNodeIndex = entity.currentNodeIndex,
                accumulatedStepsCurrentNode = entity.accumulatedStepsCurrentNode,
                totalAdventureSteps = entity.totalAdventureSteps,
                clearedFlags = flags,
                isCompleted = entity.isCompleted
            )
        } else {
            AdventureProgress()
        }
    }.distinctUntilChanged()

    suspend fun getPetState(): PetState = withContext(Dispatchers.IO) {
        dao.getPetState()?.toDomain() ?: PetState()
    }

    suspend fun savePetState(state: PetState) = withContext(Dispatchers.IO) {
        dao.insertOrUpdatePet(PetEntity.fromDomain(state))
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
     * Processes time-delta decay based on UTC timestamps.
     * Coalesces SQLite writes so flash storage is only written when physiological metrics change
     * or at least 60 seconds have elapsed since the last persisted checkpoint.
     */
    suspend fun processDecay(currentUtc: Long = System.currentTimeMillis() / 1000L): PetState = withContext(Dispatchers.IO) {
        val current = getPetState()
        val updated = PetSimulationEngine.processDelta(current, currentUtc)
        val evolved = EvolutionEngine.evaluateEvolution(updated)
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
        val progress = AdventureProgress(
            campaignId = currentEntity?.campaignId ?: story.campaignId,
            currentNodeIndex = currentEntity?.currentNodeIndex ?: 0,
            accumulatedStepsCurrentNode = currentEntity?.accumulatedStepsCurrentNode ?: 0,
            totalAdventureSteps = currentEntity?.totalAdventureSteps ?: 0,
            clearedFlags = flags,
            isCompleted = currentEntity?.isCompleted ?: false
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
                isCompleted = updated.isCompleted
            )
        )
        result
    }

    suspend fun recordBossVictory(boss: BossEncounter): AdventureProgress = withContext(Dispatchers.IO) {
        val story = getActiveCampaign()
        val currentEntity = dao.getAdventureState()
        val flags = if (currentEntity?.clearedFlagsCsv.isNullOrBlank()) emptySet() else currentEntity!!.clearedFlagsCsv.split(",").toSet()
        val progress = AdventureProgress(
            campaignId = currentEntity?.campaignId ?: story.campaignId,
            currentNodeIndex = currentEntity?.currentNodeIndex ?: 0,
            accumulatedStepsCurrentNode = currentEntity?.accumulatedStepsCurrentNode ?: 0,
            totalAdventureSteps = currentEntity?.totalAdventureSteps ?: 0,
            clearedFlags = flags,
            isCompleted = currentEntity?.isCompleted ?: false
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
                isCompleted = updated.isCompleted
            )
        )
        updated
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
