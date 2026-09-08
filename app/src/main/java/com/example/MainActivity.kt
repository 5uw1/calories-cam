package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.AnalysisState
import com.example.ui.AnalyzingLoadingDialog
import com.example.ui.FoodAnalysisResultScreen
import com.example.ui.FoodCameraScreen
import com.example.ui.FoodDashboardScreen
import com.example.ui.FoodDetailDialog
import com.example.ui.FoodHistoryScreen
import com.example.ui.FoodViewModel
import com.example.ui.theme.MyApplicationTheme
import com.example.util.AppLanguage
import com.example.util.AppStrings
import com.example.util.LanguageManager
import com.example.util.ThemeManager
import com.example.util.ThemeMode
import androidx.compose.foundation.isSystemInDarkTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val viewModel: FoodViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeMode by ThemeManager.themeMode.collectAsStateWithLifecycle()
            MyApplicationTheme(themeMode = themeMode) {
                FoodCalorieApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun FoodCalorieApp(viewModel: FoodViewModel) {
    val entries by viewModel.allEntries.collectAsStateWithLifecycle()
    val analysisState by viewModel.analysisState.collectAsStateWithLifecycle()
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val calorieGoal by viewModel.dailyCalorieGoal.collectAsStateWithLifecycle()
    val selectedDetailEntry by viewModel.selectedDetailEntry.collectAsStateWithLifecycle()
    val currentLanguage by LanguageManager.currentLanguage.collectAsStateWithLifecycle()
    val themeMode by ThemeManager.themeMode.collectAsStateWithLifecycle()
    val systemInDark = isSystemInDarkTheme()
    val isDarkMode = when (themeMode) {
        ThemeMode.SYSTEM -> systemInDark
        ThemeMode.DARK -> true
        ThemeMode.LIGHT -> false
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Handle analysis errors
    LaunchedEffect(analysisState) {
        if (analysisState is AnalysisState.Error) {
            val msg = (analysisState as AnalysisState.Error).message
            scope.launch {
                snackbarHostState.showSnackbar(msg)
            }
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
                            viewModel.navigateTo(1) // back to camera
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
                            onImageCaptured = { bitmap ->
                                viewModel.analyzeImage(bitmap)
                            },
                            onSelectPreset = { preset ->
                                viewModel.selectPresetSample(preset)
                            },
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

        // Loading modal when AI is analyzing
        if (analysisState is AnalysisState.Analyzing) {
            AnalyzingLoadingDialog(language = currentLanguage)
        }

        // Entry Detail modal
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

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(text = "Hello $name!", modifier = modifier)
}

