package com.example

import android.content.Context

/** Holds the application context for platform code that needs it (Room DB path). */
object AndroidApp {
    lateinit var context: Context
        private set

    fun init(context: Context) {
        this.context = context.applicationContext
    }
}
