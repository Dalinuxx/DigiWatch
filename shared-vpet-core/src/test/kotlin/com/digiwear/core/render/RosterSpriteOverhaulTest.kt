package com.digiwear.core.render

import com.digiwear.core.model.PetSpecies
import com.digiwear.core.model.PetStage
import com.digiwear.core.model.SpriteFrame
import org.junit.Assert.fail
import org.junit.Test

class RosterSpriteOverhaulTest {

    @Test
    fun testAllSpeciesHaveNonZeroPrimary() {
        for ((sid, spec) in PetSpecies.SPECIES_REGISTRY) {
            val pal = SpriteMatrixRegistry.getPalette(sid)
            val r = (pal.primary ushr 16) and 0xFF
            val g = (pal.primary ushr 8) and 0xFF
            val b = pal.primary and 0xFF
            val maxC = maxOf(r, maxOf(g, b))
            if (maxC < 30) {
                fail("Species $sid (${spec.name}) has overly dark primary: rgb($r, $g, $b)")
            }
        }
    }

    @Test
    fun testAllSpeciesHaveSubstantialInteriorFill() {
        val underfilled = mutableListOf<String>()
        for ((sid, spec) in PetSpecies.SPECIES_REGISTRY) {
            val matrix = SpriteMatrixRegistry.getMatrix(sid, SpriteFrame.IDLE_A)
            var solidCount = 0
            for (r in 0 until 16) {
                for (c in 0 until 16) {
                    val ch = matrix[r][c]
                    if (ch in setOf('#', 'S', 'T', 'W', 'R', 'G', 'C')) {
                        solidCount++
                    }
                }
            }
            val minRequired = when (spec.stage) {
                PetStage.BABY_I -> 8
                PetStage.BABY_II -> 10
                PetStage.CHILD -> 20
                else -> 22
            }
            if (solidCount < minRequired) {
                underfilled.add("Species $sid (${spec.name}, stage ${spec.stage}): solidCount=$solidCount < minRequired=$minRequired")
            }
        }
        if (underfilled.isNotEmpty()) {
            fail("Underfilled species found (${underfilled.size}):\n" + underfilled.joinToString("\n"))
        }
    }
}
