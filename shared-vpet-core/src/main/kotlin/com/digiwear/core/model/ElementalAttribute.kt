package com.digiwear.core.model

import kotlinx.serialization.Serializable

/**
 * Elemental attribute determining the classic triangular battle advantage:
 * Vaccine > Virus > Data > Vaccine (1.2x damage multiplier).
 * Free attribute has no inherent advantage or disadvantage.
 */
@Serializable
enum class ElementalAttribute(
    val code: Int,
    val displayName: String
) {
    VACCINE(0, "Vaccine"),
    DATA(1, "Data"),
    VIRUS(2, "Virus"),
    FREE(3, "Free");

    /**
     * Calculates the damage multiplier when attacking a defender of [target] attribute.
     */
    fun damageMultiplierAgainst(target: ElementalAttribute): Float {
        return when {
            this == VACCINE && target == VIRUS -> 1.2f
            this == VIRUS && target == DATA -> 1.2f
            this == DATA && target == VACCINE -> 1.2f
            // Reverse disadvantage or neutral
            this == VIRUS && target == VACCINE -> 0.833f
            this == DATA && target == VIRUS -> 0.833f
            this == VACCINE && target == DATA -> 0.833f
            else -> 1.0f
        }
    }

    companion object {
        fun fromCode(code: Int): ElementalAttribute =
            entries.firstOrNull { it.code == code } ?: FREE
    }
}
