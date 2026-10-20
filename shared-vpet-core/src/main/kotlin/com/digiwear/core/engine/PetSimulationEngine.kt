package com.digiwear.core.engine

import com.digiwear.core.model.PetSpecies
import com.digiwear.core.model.PetStage
import com.digiwear.core.model.PetState
import java.time.Instant
import java.time.ZoneOffset
import kotlin.math.max
import kotlin.math.min

/**
 * High-precision physiological simulation engine conforming to V1-V6 virtual pet specifications.
 * All updates are calculated strictly via UTC timestamp deltas:
 * DeltaT = T_current - T_last_processed
 * Eliminating active CPU polling loops for optimal battery conservation.
 */
object PetSimulationEngine {

    /**
     * Advances the pet simulation to [currentTimestampUtc].
     */
    fun processDelta(
        currentState: PetState,
        currentTimestampUtc: Long,
        timeZoneOffset: ZoneOffset = ZoneOffset.UTC,
        isCanonicalLifespanEnabled: Boolean = false
    ): PetState {
        if (currentState.isDeceased) return currentState

        val lastTime = if (currentState.lastProcessedTimestampUtc <= 0L) {
            currentTimestampUtc
        } else {
            currentState.lastProcessedTimestampUtc
        }

        val deltaSeconds = max(0L, currentTimestampUtc - lastTime)
        if (deltaSeconds == 0L) return currentState

        // Cold Sleep / Freezer Mode: complete physiological stasis (all decay & aging paused)
        if (currentState.isFrozen) {
            return currentState.copy(lastProcessedTimestampUtc = currentTimestampUtc)
        }

        val species = PetSpecies.getById(currentState.speciesId)
        val stage = species.stage

        // Update accumulated live age
        var updatedAge = currentState.ageSeconds + deltaSeconds

        // Check canonical lifespan limit
        val maxLifespanSec = currentState.lifespanDays * 86400L
        val isPerished = isCanonicalLifespanEnabled && (updatedAge >= maxLifespanSec || (currentState.careMistakes >= 20 && currentState.isInjured))
        if (isPerished) {
            return currentState.copy(
                ageSeconds = updatedAge,
                isDeceased = true,
                lastProcessedTimestampUtc = currentTimestampUtc
            )
        }

        // Calculate sleep window status based on wall clock time
        val zonedDateTime = Instant.ofEpochSecond(currentTimestampUtc).atZone(timeZoneOffset)
        val hour = zonedDateTime.hour
        val minute = zonedDateTime.minute
        val inSleepWindow = stage.isSleepWindow(hour, minute)

        // Accumulate decay seconds
        var accHungerSec = currentState.hungerAccumulatedSeconds + deltaSeconds
        var accStrengthSec = currentState.strengthAccumulatedSeconds + deltaSeconds
        var accWasteSec = currentState.wasteAccumulatedSeconds + deltaSeconds

        val hungerIntervalSec = stage.hungerLossMinutes * 60L
        val strengthIntervalSec = stage.strengthLossMinutes * 60L
        val wasteIntervalSec = stage.wasteIntervalMinutes * 60L

        var currentHunger = currentState.hunger
        var currentStrength = currentState.strength
        var currentWaste = currentState.wasteCount

        // Process hunger decay (if sleeping, metabolic decay is typically frozen or half, but follows stage spec)
        if (!inSleepWindow) {
            val hungerDecayUnits = (accHungerSec / hungerIntervalSec).toInt()
            if (hungerDecayUnits > 0) {
                currentHunger = max(0, currentHunger - hungerDecayUnits)
                accHungerSec %= hungerIntervalSec
            }

            // Process strength decay
            val strengthDecayUnits = (accStrengthSec / strengthIntervalSec).toInt()
            if (strengthDecayUnits > 0) {
                currentStrength = max(0, currentStrength - strengthDecayUnits)
                accStrengthSec %= strengthIntervalSec
            }
        }

        // Process waste generation (up to 4 uncleaned units)
        val wasteUnits = (accWasteSec / wasteIntervalSec).toInt()
        if (wasteUnits > 0) {
            currentWaste = min(4, currentWaste + wasteUnits)
            accWasteSec %= wasteIntervalSec
        }

        // Check for injury due to uncleaned waste accumulation (4 waste units triggers injury)
        var isInjured = currentState.isInjured
        if (currentWaste >= 4) {
            isInjured = true
        }

        // Attention Call logic:
        // Triggered by: empty hunger meter, empty strength meter, or unextinguished lights during sleep window
        val needsAttention = (currentHunger == 0 || currentStrength == 0 || (inSleepWindow && !currentState.lightsOff))

        var callActive = currentState.attentionCallActive
        var callTimestamp = currentState.attentionCallTimestampUtc
        var careMistakes = currentState.careMistakes

        if (stage.careMistakesEnabled) {
            val graceWindowSec = stage.graceWindowMinutes * 60L

            if (needsAttention) {
                if (!callActive) {
                    // Signal just triggered
                    callActive = true
                    callTimestamp = currentTimestampUtc
                } else if (callTimestamp != null) {
                    // Check if elapsed time beyond designated grace window
                    val timeInCall = currentTimestampUtc - callTimestamp
                    if (timeInCall >= graceWindowSec) {
                        // Care Mistake recorded!
                        careMistakes++
                        // Reset call so subsequent mistake requires re-evaluating or waiting next window
                        callActive = false
                        callTimestamp = null
                    }
                }
            } else {
                // Needs were addressed before grace window expired
                callActive = false
                callTimestamp = null
            }
        } else {
            // Baby I is exempt from care mistakes
            callActive = needsAttention
            callTimestamp = null
        }

        return currentState.copy(
            hunger = currentHunger,
            strength = currentStrength,
            wasteCount = currentWaste,
            ageSeconds = updatedAge,
            careMistakes = careMistakes,
            isSleeping = inSleepWindow,
            isInjured = isInjured,
            lastProcessedTimestampUtc = currentTimestampUtc,
            attentionCallActive = callActive,
            attentionCallTimestampUtc = callTimestamp,
            hungerAccumulatedSeconds = accHungerSec,
            strengthAccumulatedSeconds = accStrengthSec,
            wasteAccumulatedSeconds = accWasteSec
        )
    }

    /**
     * Feeding meat/meal increments hunger by 1 heart (max 4) and adds 1g weight.
     * Feeding when hunger is full records an Overfeed instance.
     */
    fun feedMeal(state: PetState, currentTimestampUtc: Long): PetState {
        val isFull = state.hunger >= 4
        val newOverfeed = if (isFull) state.overfeedCount + 1 else state.overfeedCount
        val newHunger = min(4, state.hunger + 1)
        val newWeight = state.weight + 1

        val updatedState = state.copy(
            hunger = newHunger,
            overfeedCount = newOverfeed,
            weight = newWeight,
            lastProcessedTimestampUtc = currentTimestampUtc
        )
        return resolveAttentionCall(updatedState, currentTimestampUtc)
    }

    /**
     * Feeding vitamin/protein increments strength by 1 heart (max 4) and adds 2g weight.
     */
    fun feedProtein(state: PetState, currentTimestampUtc: Long): PetState {
        val newStrength = min(4, state.strength + 1)
        val newWeight = state.weight + 2

        val updatedState = state.copy(
            strength = newStrength,
            weight = newWeight,
            lastProcessedTimestampUtc = currentTimestampUtc
        )
        return resolveAttentionCall(updatedState, currentTimestampUtc)
    }

    /**
     * Cleans all waste units and prevents injury buildup.
     */
    fun cleanWaste(state: PetState): PetState {
        return state.copy(
            wasteCount = 0,
            wasteAccumulatedSeconds = 0L
        )
    }

    /**
     * Toggles lights on or off (essential during sleep windows to prevent care mistakes).
     */
    /**
     * Toggles Cold Sleep (Rest Mode / Freezer Mode).
     * Pauses all aging, hunger decay, strength decay, and care mistake accumulation.
     */
    fun toggleColdSleep(state: PetState, currentTimestampUtc: Long = System.currentTimeMillis() / 1000L): PetState {
        val nextFrozen = !state.isFrozen
        return state.copy(
            isFrozen = nextFrozen,
            attentionCallActive = if (nextFrozen) false else state.attentionCallActive,
            attentionCallTimestampUtc = if (nextFrozen) null else state.attentionCallTimestampUtc,
            lastProcessedTimestampUtc = currentTimestampUtc
        )
    }

    fun toggleLights(state: PetState, currentTimestampUtc: Long): PetState {
        val newLightsOff = !state.lightsOff
        val updated = state.copy(
            lightsOff = newLightsOff,
            lastProcessedTimestampUtc = currentTimestampUtc
        )
        return resolveAttentionCall(updated, currentTimestampUtc)
    }

    /**
     * Executes mini-game training session.
     * Win grants strength +1, effort +1, reduces weight by 1g.
     */
    fun train(state: PetState, success: Boolean, currentTimestampUtc: Long): PetState {
        val newStrength = if (success) min(4, state.strength + 1) else state.strength
        val newEffort = if (success) min(4, state.effort + 1) else state.effort
        val newWeight = max(PetSpecies.getById(state.speciesId).baselineWeight, state.weight - 1)

        val updated = state.copy(
            strength = newStrength,
            effort = newEffort,
            weight = newWeight,
            lastProcessedTimestampUtc = currentTimestampUtc
        )
        return resolveAttentionCall(updated, currentTimestampUtc)
    }

    /**
     * Treats illness or combat injury.
     */
    fun cureInjury(state: PetState): PetState {
        return state.copy(isInjured = false)
    }

    private fun resolveAttentionCall(state: PetState, currentTimestampUtc: Long): PetState {
        val species = PetSpecies.getById(state.speciesId)
        val inSleep = state.isSleeping
        val needsAttention = (state.hunger == 0 || state.strength == 0 || (inSleep && !state.lightsOff))

        return if (!needsAttention && state.attentionCallActive) {
            state.copy(
                attentionCallActive = false,
                attentionCallTimestampUtc = null,
                lastProcessedTimestampUtc = currentTimestampUtc
            )
        } else {
            state
        }
    }

    /**
     * Hatches a Generation N+1 Traited Digitama inheriting stat and lifespan bonuses from the departed partner.
     */
    fun createTraitedEggState(
        departedState: PetState,
        starterSpeciesId: Int = 101,
        nowUtc: Long = System.currentTimeMillis() / 1000L
    ): PetState {
        val nextGen = departedState.generation + 1
        val bonus = (departedState.traitedEggBonus + 1).coerceAtMost(5)
        return PetState(
            speciesId = starterSpeciesId,
            currentHp = 50 + (bonus * 5),
            maxHp = 50 + (bonus * 5),
            hunger = 4,
            strength = 4,
            weight = 5,
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
            lastProcessedTimestampUtc = nowUtc,
            generation = nextGen,
            lifespanDays = 14 + (bonus * 2),
            isDeceased = false,
            traitedEggBonus = bonus
        )
    }

    /**
     * Creates an immortalized Memorial Graveyard record of a departed partner.
     */
    fun createMemorialRecord(
        state: PetState,
        causeOfDeath: String = "OLD_AGE",
        epitaph: String = "Beloved partner of Generation ${state.generation}."
    ): com.digiwear.core.model.MemorialRecord {
        val species = PetSpecies.getById(state.speciesId)
        return com.digiwear.core.model.MemorialRecord(
            speciesId = state.speciesId,
            speciesName = species.name,
            generation = state.generation,
            finalStage = species.stage.displayName,
            ageDays = (state.ageSeconds / 86400L).toInt(),
            battlesWon = state.victories,
            battlesTotal = state.totalBattles,
            causeOfDeath = causeOfDeath,
            epitaph = epitaph,
            timestampUtc = System.currentTimeMillis() / 1000L,
            traitedEggBonusGiven = state.traitedEggBonus + 1
        )
    }

    /**
     * Authentic Bandai combat result processing:
     * Increments total battles, and increments victories on win.
     * On defeat (or knockout), rolls a canonical ~25% chance of battle injury if [combatInjuriesEnabled] is true.
     * Returns Pair<UpdatedState, WasInjuredInBattle>.
     */
    fun processBattleOutcome(
        state: PetState,
        won: Boolean,
        combatInjuriesEnabled: Boolean = true,
        injuryRoll: Int = (0..99).random()
    ): Pair<PetState, Boolean> {
        val newBattles = state.totalBattles + 1
        val newWins = if (won) state.victories + 1 else state.victories
        val getsInjured = !won && combatInjuriesEnabled && (injuryRoll < 25)
        val finalInjured = state.isInjured || getsInjured

        val updated = state.copy(
            totalBattles = newBattles,
            victories = newWins,
            isInjured = finalInjured
        )
        return Pair(updated, getsInjured)
    }
}
