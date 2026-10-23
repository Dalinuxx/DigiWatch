package com.digiwear.core.model

/**
 * Authentic Soundpack Profiles for the DigiWear audio engine.
 *
 * - [ORIGINAL_1997]: Faithful 2.048 kHz square-wave piezo buzzer sound effects matching
 *   the physical piezoelectric buzzer of the original 1997 Digital Monster virtual pet
 *   (classic 4-beep call, 18ms button chirp, attack impact, alternating evolution warning, death siren).
 * - [DIGIVICE_1999]: Classic tri-tone anime/digivice chimes (encounter chime, evolution fanfare,
 *   victory jingle, gentle glass-like clicks).
 * - [CHIPTUNE_RETRO]: Rich polyphonic 8-bit procedural synthesizer with multi-tone sweeps,
 *   arpeggios, and classic video game audio.
 */
enum class SoundpackProfile(
    val id: String,
    val displayName: String,
    val subtitle: String,
    val description: String
) {
    ORIGINAL_1997(
        id = "ORIGINAL_1997",
        displayName = "1997 Piezo (2.048 kHz)",
        subtitle = "Bandai OG Piezo",
        description = "Faithful 2.048 kHz square-wave piezo beeps (classic call, attack chirp, evolution warning, death siren)"
    ),
    DIGIVICE_1999(
        id = "DIGIVICE_1999",
        displayName = "1999 Digivice Chimes",
        subtitle = "Anime Tri-Tone Chimes",
        description = "Classic tri-tone anime/digivice chimes (encounter chime, evolution fanfare, victory jingle)"
    ),
    CHIPTUNE_RETRO(
        id = "CHIPTUNE_RETRO",
        displayName = "Chiptune Retro (8-Bit)",
        subtitle = "Polyphonic Synthesizer",
        description = "Rich polyphonic 8-bit procedural synthesizer with multi-tone sweeps and arpeggios"
    );

    companion object {
        fun fromId(id: String?): SoundpackProfile {
            return entries.firstOrNull { it.id.equals(id, ignoreCase = true) } ?: ORIGINAL_1997
        }
    }
}
