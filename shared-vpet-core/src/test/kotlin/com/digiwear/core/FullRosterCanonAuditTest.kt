package com.digiwear.core

import com.digiwear.core.model.PetSpecies
import com.digiwear.core.render.SpriteMatrixRegistry
import com.digiwear.core.model.SpriteFrame
import org.junit.Test
import org.junit.Assert.assertTrue

class FullRosterCanonAuditTest {

    @Test
    fun auditAllSpeciesForGenericBoxPatterns() {
        val allSpecies = PetSpecies.SPECIES_REGISTRY.values.sortedBy { it.id }
        println("Auditing all ${allSpecies.size} species for generic box patterns...")

        val genericBoxSignatures = listOf(
            "B##WW######WW##B",
            "B##EE######EE##B",
            "B#TT#SSSSSS#TT#B",
            "B#SSSSSSSSSS#B",
            "B##########B",
            "B##WW####WW#B",
            "B##EE####EE#B",
            "B#SSSS#SSSS#B",
            "B###SSSSSSSS###B",
            "B#TTSSSSSSSSTT#B"
        )

        val flaggedSpecies = mutableListOf<String>()

        for (species in allSpecies) {
            val matrix = SpriteMatrixRegistry.getMatrix(species.id, SpriteFrame.IDLE_A)
            var matchesGenericBox = false
            var matchReason = ""

            for ((rIdx, row) in matrix.withIndex()) {
                for (sig in genericBoxSignatures) {
                    if (row.contains(sig)) {
                        matchesGenericBox = true
                        matchReason = "Row $rIdx matches signature '$sig'"
                        break
                    }
                }
                if (matchesGenericBox) break
            }

            // Also check symmetry: if > 11 rows out of 16 are perfectly symmetric (left-right mirror)
            var symmetricRows = 0
            for (row in matrix) {
                if (row == row.reversed()) symmetricRows++
            }
            if (symmetricRows >= 11) {
                matchesGenericBox = true
                matchReason = if (matchReason.isEmpty()) "$symmetricRows/16 rows perfectly symmetric" else "$matchReason & $symmetricRows symmetric rows"
            }

            if (matchesGenericBox) {
                flaggedSpecies.add("[ID ${species.id}] ${species.name} (${species.stage}): $matchReason")
            }
        }

        println("=== FLAGGED SPECIES (${flaggedSpecies.size}) ===")
        flaggedSpecies.forEach { println(it) }
        println("================================================")
    }
}
