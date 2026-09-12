package com.example.model

import com.example.util.AppLanguage
import kotlin.test.Test
import kotlin.test.assertEquals

class FoodEntryDisplayTest {

    private val entry = FoodEntry(
        foodName = "ข้าวมันไก่ต้ม",
        foodNameEn = "Hainanese Chicken Rice",
        foodNameDe = "Hainan-Hühnchenreis",
        calories = 585,
        protein = 28.5f,
        carbs = 72.0f,
        fat = 20.2f
    )

    // A saved entry's foodNameEn/foodNameDe are always populated in their own language regardless
    // of which language was active at save time, so switching languages after saving must still
    // show the correct name — not whatever language happened to be primary when it was logged.
    @Test
    fun each_language_shows_its_own_stored_name() {
        assertEquals("ข้าวมันไก่ต้ม", entry.displayFoodName(AppLanguage.TH))
        assertEquals("Hainanese Chicken Rice", entry.displayFoodName(AppLanguage.EN))
        assertEquals("Hainan-Hühnchenreis", entry.displayFoodName(AppLanguage.DE))
    }

    @Test
    fun en_and_de_fall_back_to_primary_name_when_blank() {
        val thaiOnlyEntry = entry.copy(foodNameEn = "", foodNameDe = "")
        assertEquals("ข้าวมันไก่ต้ม", thaiOnlyEntry.displayFoodName(AppLanguage.EN))
        assertEquals("ข้าวมันไก่ต้ม", thaiOnlyEntry.displayFoodName(AppLanguage.DE))
    }
}
