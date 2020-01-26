package com.fnovellon.buildonmusic

typealias Switch = Boolean

class SwitchOnOff(var value: Switch) {

    private val onValueChangedListeners: MutableList<(Boolean) -> Unit> = mutableListOf()

    companion object {
        const val ON : Switch = true
        const val OFF : Switch = false
    }

    private fun callListenersWithValue(value: Boolean) {
        onValueChangedListeners.forEach {
            it(value)
        }
    }

    fun addOnValueChangedListener(callback: (Boolean) -> Unit) {
        onValueChangedListeners.add(callback)
    }

    fun turnOn() {
        if (value != ON) {
            value = ON
            callListenersWithValue(ON)
        }
    }

    fun turnOff() {
        if (value != OFF) {
            value = OFF
            callListenersWithValue(OFF)
        }
    }

}