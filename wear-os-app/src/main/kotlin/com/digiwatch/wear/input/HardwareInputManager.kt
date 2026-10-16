package com.digiwatch.wear.input

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

enum class VPetButton {
    A,      // Button A: Select / Cycle forward / Top Stem / Up
    B,      // Button B: Confirm / Execute / QTE Hit / Stem 2 / Enter / Center
    C,      // Button C: Cancel / Light toggle / Back / Stem 3
    PREV    // Cycle backwards / Rotary CCW / Down
}

/**
 * Low-latency dispatcher for physical smartwatch hardware buttons and rotary crown input.
 * Provides direct synchronous execution for Quick Time Events (QTE) with zero touch-digitizer latency,
 * and asynchronous SharedFlow event distribution for menu navigation.
 */
object HardwareInputManager {

    /**
     * Ultra-low latency callback specifically for Quick Time Events (QTEs).
     * When set, hardware button events invoke this callback synchronously on the UI thread
     * directly from MainActivity.onKeyDown / onGenericMotionEvent.
     * Return true to consume the event immediately (e.g. perfect QTE hit).
     */
    @Volatile
    var activeQteListener: ((VPetButton) -> Boolean)? = null

    private val _buttonEvents = MutableSharedFlow<VPetButton>(
        extraBufferCapacity = 32,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val buttonEvents: SharedFlow<VPetButton> = _buttonEvents.asSharedFlow()

    /**
     * Dispatches a hardware button down event.
     * Evaluates activeQteListener first for zero-latency response.
     * Returns true if consumed.
     */
    fun onButtonDown(button: VPetButton): Boolean {
        val qte = activeQteListener
        if (qte != null && qte.invoke(button)) {
            return true
        }
        return _buttonEvents.tryEmit(button)
    }
}
