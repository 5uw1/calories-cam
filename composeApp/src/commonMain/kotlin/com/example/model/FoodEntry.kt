package com.example.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.util.AppLanguage
import kotlinx.datetime.Clock

@Entity(tableName = "food_entries")
data class FoodEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val foodName: String,
    val foodNameEn: String = "",
    val foodNameDe: String = "",
    val calories: Int,
    val protein: Float, // in grams
    val carbs: Float,   // in grams
    val fat: Float,     // in grams
    val fiber: Float = 0f,  // in grams
    val sugar: Float = 0f,  // in grams
    val sodium: Int = 0,    // in milligrams
    val portionSize: String = "1 portion (300g)",
    val mealType: String = "Lunch", // Breakfast, Lunch, Dinner, Snack
    val healthTip: String = "",
    val ingredients: String = "", // comma-separated or notes
    val timestamp: Long = Clock.System.now().toEpochMilliseconds(),
    val imageBase64: String? = null // compressed thumbnail for local display
)

// foodNameEn/foodNameDe are always populated in their language regardless of which
// language was active when the entry was saved, so history displays correctly after
// switching languages later. Falls back to the primary (save-time) name when blank.
fun FoodEntry.displayFoodName(language: AppLanguage): String = when (language) {
    AppLanguage.EN -> foodNameEn.ifBlank { foodName }
    AppLanguage.DE -> foodNameDe.ifBlank { foodName }
    AppLanguage.TH -> foodName
}
