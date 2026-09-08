package com.example.ui

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.api.GeminiFoodService
import com.example.data.FoodDatabase
import com.example.data.FoodRepository
import com.example.model.FoodEntry
import com.example.model.NutritionAnalysis
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar

sealed interface AnalysisState {
    data object Idle : AnalysisState
    data object Analyzing : AnalysisState
    data class Success(val analysis: NutritionAnalysis) : AnalysisState
    data class Error(val message: String) : AnalysisState
}

class FoodViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: FoodRepository

    init {
        val db = FoodDatabase.getDatabase(application)
        repository = FoodRepository(db.foodDao())
    }

    // All logged entries
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

    fun navigateTo(screenIndex: Int) {
        _currentScreen.value = screenIndex
    }

    fun setDailyGoal(goal: Int) {
        if (goal > 500) {
            _dailyCalorieGoal.value = goal
        }
    }

    fun showEntryDetail(entry: FoodEntry?) {
        _selectedDetailEntry.value = entry
    }

    fun analyzeImage(bitmap: Bitmap) {
        viewModelScope.launch {
            _analysisState.value = AnalysisState.Analyzing
            val result = GeminiFoodService.analyzeFoodImage(bitmap)
            result.onSuccess { analysis ->
                _analysisState.value = AnalysisState.Success(analysis)
            }.onFailure { error ->
                _analysisState.value = AnalysisState.Error(error.localizedMessage ?: "เกิดข้อผิดพลาดในการวิเคราะห์")
            }
        }
    }

    fun selectPresetSample(sample: NutritionAnalysis) {
        _analysisState.value = AnalysisState.Success(sample)
    }

    fun resetAnalysis() {
        _analysisState.value = AnalysisState.Idle
    }

    fun saveAnalysis(
        analysis: NutritionAnalysis,
        mealType: String,
        customFoodName: String? = null,
        customCalories: Int? = null,
        customPortion: String? = null
    ) {
        viewModelScope.launch {
            val thumbnailBase64 = analysis.bitmap?.let {
                GeminiFoodService.bitmapToBase64(it, quality = 60)
            }

            val entry = FoodEntry(
                foodName = customFoodName?.ifBlank { analysis.foodName } ?: analysis.foodName,
                foodNameEn = analysis.foodNameEn,
                calories = customCalories ?: analysis.calories,
                protein = analysis.protein,
                carbs = analysis.carbs,
                fat = analysis.fat,
                fiber = analysis.fiber,
                sugar = analysis.sugar,
                sodium = analysis.sodium,
                portionSize = customPortion?.ifBlank { analysis.portionSize } ?: analysis.portionSize,
                mealType = mealType,
                healthTip = analysis.healthTip,
                ingredients = analysis.ingredients.joinToString(", "),
                imageBase64 = thumbnailBase64,
                timestamp = System.currentTimeMillis()
            )

            repository.insert(entry)
            _analysisState.value = AnalysisState.Idle
            _currentScreen.value = 0 // Navigate back to Dashboard
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

    // Calculate today's entries
    fun isToday(timestamp: Long): Boolean {
        val cal1 = Calendar.getInstance().apply { timeInMillis = timestamp }
        val cal2 = Calendar.getInstance()
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
               cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
    }
}
