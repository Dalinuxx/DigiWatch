package com.digiwatch.core.model

import kotlinx.serialization.Serializable

/**
 * The 12 standard virtual pet animation frames arranged in a uniform 3x4 grid (64x64px per frame).
 */
@Serializable
enum class SpriteFrame(val row: Int, val col: Int, val frameIndex: Int) {
    // Row 0: Idle & Movement
    IDLE_A(0, 0, 0),
    IDLE_B(0, 1, 1),
    WALK_A(0, 2, 2),

    // Row 1: Feeding & Joy
    WALK_B(1, 0, 3),
    FEED_START(1, 1, 4),
    JOY_EAT(1, 2, 5),

    // Row 2: Sleep & Displeasure
    SLEEP_A(2, 0, 6),
    SLEEP_B(2, 1, 7),
    REFUSAL(2, 2, 8),

    // Row 3: Combat & Health
    ATTACK_CHARGE(3, 0, 9),
    ATTACK_RELEASE(3, 1, 10),
    INJURED(3, 2, 11);

    companion object {
        fun fromIndex(index: Int): SpriteFrame =
            entries.firstOrNull { it.frameIndex == index } ?: IDLE_A
    }
}
