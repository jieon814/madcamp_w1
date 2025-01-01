// SharedPickManager.kt
package com.example.myapplication

import com.example.myapplication.PickManager

object SharedPickManager {
    lateinit var pickManager: PickManager
    private val listeners = mutableListOf<() -> Unit>()

    fun registerListener(listener: () -> Unit) {
        listeners.add(listener)
    }

    fun notifyListeners() {
        for (listener in listeners) {
            listener()
        }
    }
}