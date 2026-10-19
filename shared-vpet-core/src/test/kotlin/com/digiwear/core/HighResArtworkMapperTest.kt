package com.digiwear.core

import com.digiwear.core.model.PetSpecies
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

class HighResArtworkMapperTest {

    @Test
    fun verifyAll500SpeciesHighResPortraitsExist() {
        val portraitsDir = File("c:/dev/DigiWatch/shared-assets/portraits")
        assertTrue("shared-assets/portraits directory must exist", portraitsDir.exists() && portraitsDir.isDirectory)

        val files = portraitsDir.listFiles { f -> f.extension == "png" } ?: emptyArray()
        assertEquals("Must contain high-resolution portraits for all 500 canonical species", 500, files.size)

        for (sp in PetSpecies.SPECIES_REGISTRY.values) {
            val assetFile = File(portraitsDir, "${sp.id}.png")
            assertTrue("Portrait asset for #${sp.id} ${sp.name} must exist", assetFile.exists())
            assertTrue("Portrait asset for #${sp.id} ${sp.name} must not be empty", assetFile.length() > 1000L)
        }
    }
}
