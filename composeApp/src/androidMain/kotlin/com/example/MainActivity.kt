package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidApp.init(applicationContext)
        val container = AppContainer()
        val viewModel = container.createViewModel()

        enableEdgeToEdge()
        setContent {
            App(viewModel = viewModel, isSystemDark = isSystemInDarkTheme())
        }
    }
}
