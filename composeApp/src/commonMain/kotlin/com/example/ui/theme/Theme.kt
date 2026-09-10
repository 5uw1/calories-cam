package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import com.example.util.ThemeMode

private val DarkColorScheme = darkColorScheme(
    primary = OrangePrimaryDark,
    onPrimary = OrangeOnContainer,
    primaryContainer = OrangeContainerDark,
    secondary = GreenSecondaryDark,
    onSecondary = GreenOnContainer,
    secondaryContainer = GreenContainerDark,
    tertiary = AmberTertiaryDark,
    background = DarkBackground,
    surface = DarkSurface,
    surfaceVariant = DarkSurfaceVariant,
    onBackground = DarkOnSurface,
    onSurface = DarkOnSurface,
    onSurfaceVariant = DarkOnSurfaceVariant
)

private val LightColorScheme = lightColorScheme(
    primary = OrangePrimary,
    onPrimary = OrangeOnPrimary,
    primaryContainer = OrangeContainer,
    onPrimaryContainer = OrangeOnContainer,
    secondary = GreenSecondary,
    onSecondary = GreenOnSecondary,
    secondaryContainer = GreenContainer,
    onSecondaryContainer = GreenOnContainer,
    tertiary = AmberTertiary,
    tertiaryContainer = AmberContainer,
    background = WarmBackground,
    surface = WarmSurface,
    surfaceVariant = WarmSurfaceVariant,
    onBackground = WarmOnSurface,
    onSurface = WarmOnSurface,
    onSurfaceVariant = WarmOnSurfaceVariant
)

/**
 * Multiplatform theme. Dynamic color (Android 12 Material You) is dropped because it is
 * Android-only; the app uses its cohesive brand palette on both platforms — which the
 * original already forced via dynamicColor = false.
 */
@Composable
fun MyApplicationTheme(
    themeMode: ThemeMode = ThemeMode.SYSTEM,
    darkTheme: Boolean,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
