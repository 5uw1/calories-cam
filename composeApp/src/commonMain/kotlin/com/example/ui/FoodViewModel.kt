package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.api.GeminiFoodService
import com.example.data.FoodRepository
import com.example.model.FoodEntry
import com.example.model.NutritionAnalysis
import com.example.model.displayHealthTip
import com.example.model.displayIngredients
import com.example.platform.ImageCodec
import com.example.util.AppLanguage
import com.example.util.DateFormat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface AnalysisState {
    data object Idle : AnalysisState
    data object Analyzing : AnalysisState
    data class Success(val analysis: NutritionAnalysis) : AnalysisState
    data class Error(val message: String) : AnalysisState
}

/**
 * Platform-neutral ViewModel (androidx.lifecycle ViewModel is multiplatform in Compose MP).
 * Dependencies are injected instead of pulled from an Android Application context.
 */
class FoodViewModel(
    private val repository: FoodRepository,
    private val geminiService: GeminiFoodService
) : ViewModel() {

    val allEntries: StateFlow<List<FoodEntry>> = repository.allEntries
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _analysisState = MutableStateFlow<AnalysisState>(AnalysisState.Idle)
    val analysisState: StateFlow<AnalysisState> = _analysisState.asStateFlow()

    private val _currentScreen = MutableStateFlow(0) // 0 = Dashboard, 1 = Camera, 2 = History
    val currentScreen: StateFlow<Int> = _currentScreen.asStateFlow()

    private val _dailyCalorieGoal = MutableStateFlow(2000)
    val dailyCalorieGoal: StateFlow<Int> = _dailyCalorieGoal.asStateFlow()

    private val _selectedDetailEntry = MutableStateFlow<FoodEntry?>(null)
    val selectedDetailEntry: StateFlow<FoodEntry?> = _selectedDetailEntry.asStateFlow()

    fun navigateTo(screenIndex: Int) { _currentScreen.value = screenIndex }

    fun setDailyGoal(goal: Int) { if (goal > 500) _dailyCalorieGoal.value = goal }

    fun showEntryDetail(entry: FoodEntry?) { _selectedDetailEntry.value = entry }

    fun analyzeImage(imageBytes: ByteArray) {
        viewModelScope.launch {
            _analysisState.value = AnalysisState.Analyzing
            val result = geminiService.analyzeFoodImage(imageBytes)
            result.onSuccess { analysis ->
                _analysisState.value = AnalysisState.Success(analysis)
            }.onFailure { error ->
                _analysisState.value = AnalysisState.Error(error.message ?: "เกิดข้อผิดพลาดในการวิเคราะห์")
            }
        }
    }

    fun selectPresetSample(sample: NutritionAnalysis) {
        _analysisState.value = AnalysisState.Success(sample)
    }

    fun resetAnalysis() { _analysisState.value = AnalysisState.Idle }

    fun saveAnalysis(
        analysis: NutritionAnalysis,
        mealType: String,
        customFoodName: String? = null,
        customCalories: Int? = null,
        customPortion: String? = null,
        language: AppLanguage = AppLanguage.EN
    ) {
        viewModelScope.launch {
            val thumbnailBase64 = analysis.imageBytes?.let { ImageCodec.encodeToBase64(it) }

            val healthTip = analysis.displayHealthTip(language)
            val ingredients = analysis.displayIngredients(language)

            val entry = FoodEntry(
                foodName = customFoodName?.ifBlank { analysis.foodName } ?: analysis.foodName,
                foodNameEn = analysis.foodNameEn,
                foodNameDe = analysis.foodNameDe,
                calories = customCalories ?: analysis.calories,
                protein = analysis.protein,
                carbs = analysis.carbs,
                fat = analysis.fat,
                fiber = analysis.fiber,
                sugar = analysis.sugar,
                sodium = analysis.sodium,
                portionSize = customPortion?.ifBlank { analysis.portionSize } ?: analysis.portionSize,
                mealType = mealType,
                healthTip = healthTip,
                ingredients = ingredients.joinToString(", "),
                imageBase64 = thumbnailBase64,
                timestamp = DateFormat.nowMillis()
            )

            repository.insert(entry)
            _analysisState.value = AnalysisState.Idle
            _currentScreen.value = 0
        }
    }

    fun deleteEntry(entry: FoodEntry) {
        viewModelScope.launch {
            repository.delete(entry)
            if (_selectedDetailEntry.value?.id == entry.id) {
                _selectedDetailEntry.value = null
            }
        }
    }

    fun isToday(timestamp: Long): Boolean = DateFormat.isToday(timestamp)
}
