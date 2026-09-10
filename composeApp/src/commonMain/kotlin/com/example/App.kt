package com.example

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.example.ui.AnalysisState
import com.example.ui.AnalyzingLoadingDialog
import com.example.ui.FoodAnalysisResultScreen
import com.example.ui.FoodCameraScreen
import com.example.ui.FoodDashboardScreen
import com.example.ui.FoodDetailDialog
import com.example.ui.FoodHistoryScreen
import com.example.ui.FoodViewModel
import com.example.ui.theme.MyApplicationTheme
import com.example.util.LanguageManager
import com.example.util.ThemeManager
import com.example.util.ThemeMode
import kotlinx.coroutines.launch

/**
 * Root composable, shared by Android and iOS. Moved out of MainActivity so both
 * platform entry points render the same UI. `isSystemDark` is supplied by the caller
 * because system-dark detection differs per platform host.
 */
@Composable
fun App(viewModel: FoodViewModel, isSystemDark: Boolean) {
    val themeMode by ThemeManager.themeMode.collectAsState()
    val isDarkMode = when (themeMode) {
        ThemeMode.SYSTEM -> isSystemDark
        ThemeMode.DARK -> true
        ThemeMode.LIGHT -> false
    }

    MyApplicationTheme(themeMode = themeMode, darkTheme = isDarkMode) {
        FoodCalorieApp(viewModel = viewModel, isDarkMode = isDarkMode)
    }
}

@Composable
private fun FoodCalorieApp(viewModel: FoodViewModel, isDarkMode: Boolean) {
    val entries by viewModel.allEntries.collectAsState()
    val analysisState by viewModel.analysisState.collectAsState()
    val currentScreen by viewModel.currentScreen.collectAsState()
    val calorieGoal by viewModel.dailyCalorieGoal.collectAsState()
    val selectedDetailEntry by viewModel.selectedDetailEntry.collectAsState()
    val currentLanguage by LanguageManager.currentLanguage.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(analysisState) {
        if (analysisState is AnalysisState.Error) {
            val msg = (analysisState as AnalysisState.Error).message
            scope.launch { snackbarHostState.showSnackbar(msg) }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.navigationBars,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        AnimatedContent(
            targetState = analysisState,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "ScreenTransition",
            modifier = Modifier.padding(innerPadding)
        ) { state ->
            when (state) {
                is AnalysisState.Success -> {
                    FoodAnalysisResultScreen(
                        analysis = state.analysis,
                        language = currentLanguage,
                        onSave = { name, calories, portion, mealType ->
                            viewModel.saveAnalysis(
                                analysis = state.analysis,
                                mealType = mealType,
                                customFoodName = name,
                                customCalories = calories,
                                customPortion = portion
                            )
                        },
                        onRetake = {
                            viewModel.resetAnalysis()
                            viewModel.navigateTo(1)
                        }
                    )
                }
                else -> {
                    when (currentScreen) {
                        0 -> FoodDashboardScreen(
                            entries = entries,
                            goalCalories = calorieGoal,
                            language = currentLanguage,
                            isDarkMode = isDarkMode,
                            onToggleLanguage = { LanguageManager.toggleLanguage() },
                            onToggleTheme = { ThemeManager.toggleTheme() },
                            onOpenScanner = { viewModel.navigateTo(1) },
                            onOpenHistory = { viewModel.navigateTo(2) },
                            onSelectEntry = { viewModel.showEntryDetail(it) },
                            onDeleteEntry = { viewModel.deleteEntry(it) },
                            onUpdateGoal = { viewModel.setDailyGoal(it) }
                        )
                        1 -> FoodCameraScreen(
                            language = currentLanguage,
                            onImageCaptured = { bytes -> viewModel.analyzeImage(bytes) },
                            onSelectPreset = { preset -> viewModel.selectPresetSample(preset) },
                            onBack = { viewModel.navigateTo(0) }
                        )
                        2 -> FoodHistoryScreen(
                            entries = entries,
                            language = currentLanguage,
                            onBack = { viewModel.navigateTo(0) },
                            onSelectEntry = { viewModel.showEntryDetail(it) },
                            onDeleteEntry = { viewModel.deleteEntry(it) }
                        )
                    }
                }
            }
        }

        if (analysisState is AnalysisState.Analyzing) {
            AnalyzingLoadingDialog(language = currentLanguage)
        }

        selectedDetailEntry?.let { entry ->
            FoodDetailDialog(
                entry = entry,
                language = currentLanguage,
                onDismiss = { viewModel.showEntryDetail(null) },
                onDelete = { viewModel.deleteEntry(it) }
            )
        }
    }
}
