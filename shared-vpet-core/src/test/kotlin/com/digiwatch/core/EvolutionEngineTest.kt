package com.digiwatch.core

import com.digiwatch.core.engine.EvolutionEngine
import com.digiwatch.core.model.PetSpecies
import com.digiwatch.core.model.PetState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class EvolutionEngineTest {

    @Test
    fun testAgumonToGreymonEvolution() {
        val readyForGreymon = PetState(
            speciesId = 201, // Agumon
            ageSeconds = 90000L, // >= 86400s (24h)
            careMistakes = 1,    // <= 3
            effort = 3,          // >= 3
            weight = 32          // >= 30
        )

        val target = EvolutionEngine.evaluateEvolution(readyForGreymon)
        assertNotNull(target)
        assertEquals(301, target!!.id) // Greymon
        assertEquals("Greymon", target.name)

        val evolved = EvolutionEngine.evolve(readyForGreymon, target, 100000L)
        assertEquals(301, evolved.speciesId)
        assertEquals(0, evolved.careMistakes) // Reset on form change
        assertEquals(0, evolved.effort)
    }

    @Test
    fun testAgumonToNumemonOnHighCareMistakes() {
        val neglectedAgumon = PetState(
            speciesId = 201, // Agumon
            ageSeconds = 90000L,
            careMistakes = 7 // >= 6
        )

        val target = EvolutionEngine.evaluateEvolution(neglectedAgumon)
        assertNotNull(target)
        assertEquals(305, target!!.id) // Numemon!
        assertEquals("Numemon", target.name)
    }

    @Test
    fun testNumemonToMonzaemonRedemption() {
        val dedicatedNumemon = PetState(
            speciesId = 305, // Numemon
            ageSeconds = 180000L, // >= 172800s (48h)
            totalBattles = 20,
            victories = 16, // win ratio = 0.80 >= 0.70
            effort = 4
        )

        val target = EvolutionEngine.evaluateEvolution(dedicatedNumemon)
        assertNotNull(target)
        assertEquals(402, target!!.id) // Monzaemon!
        assertEquals("Monzaemon", target.name)
    }

    @Test
    fun testTooYoungDoesNotEvolve() {
        val babyAgumon = PetState(
            speciesId = 201,
            ageSeconds = 1000L // Not yet 24 hours
        )
        assertNull(EvolutionEngine.evaluateEvolution(babyAgumon))
    }
}
