package com.example

import androidx.compose.ui.window.ComposeUIViewController
import platform.UIKit.UIViewController

private val container = AppContainer()

/** Entry point consumed by SwiftUI (see iosApp). */
fun MainViewController(): UIViewController = ComposeUIViewController {
    // Compose MP does not expose system dark mode statically here; default to light,
    // and the in-app theme toggle still cycles LIGHT/DARK/SYSTEM.
    App(viewModel = container.createViewModel(), isSystemDark = false)
}
