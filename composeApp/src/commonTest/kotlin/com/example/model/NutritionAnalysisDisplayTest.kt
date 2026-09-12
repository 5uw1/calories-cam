package com.example.model

import com.example.util.AppLanguage
import kotlin.test.Test
import kotlin.test.assertEquals

class NutritionAnalysisDisplayTest {

    private val fullyTranslated = NutritionAnalysis(
        foodName = "ข้าวมันไก่ต้ม",
        foodNameEn = "Hainanese Chicken Rice",
        foodNameDe = "Hainan-Hühnchenreis",
        calories = 585,
        protein = 28.5f,
        carbs = 72.0f,
        fat = 20.2f,
        portionSize = "1 จาน (350g)",
        portionSizeEn = "1 plate (350g)",
        portionSizeDe = "1 Teller (350g)",
        healthTip = "โปรตีนสูงจากเนื้อไก่",
        healthTipEn = "High in protein from chicken.",
        healthTipDe = "Reich an Protein durch Hühnchen.",
        ingredients = listOf("ข้าว", "ไก่ต้ม"),
        ingredientsEn = listOf("Rice", "Boiled chicken"),
        ingredientsDe = listOf("Reis", "Gekochtes Hühnchen")
    )

    private val thaiOnly = NutritionAnalysis(
        foodName = "ข้าวมันไก่ต้ม",
        foodNameEn = "",
        foodNameDe = "",
        calories = 585,
        protein = 28.5f,
        carbs = 72.0f,
        fat = 20.2f,
        portionSize = "1 จาน (350g)",
        portionSizeEn = "",
        portionSizeDe = "",
        healthTip = "โปรตีนสูงจากเนื้อไก่",
        healthTipEn = "",
        healthTipDe = "",
        ingredients = listOf("ข้าว", "ไก่ต้ม"),
        ingredientsEn = emptyList(),
        ingredientsDe = emptyList()
    )

    // TH language must always show the Thai fields, even when other translations exist.
    @Test
    fun th_language_uses_thai_fields_when_all_are_present() {
        assertEquals("ข้าวมันไก่ต้ม", fullyTranslated.displayFoodName(AppLanguage.TH))
        assertEquals("1 จาน (350g)", fullyTranslated.displayPortionSize(AppLanguage.TH))
        assertEquals("โปรตีนสูงจากเนื้อไก่", fullyTranslated.displayHealthTip(AppLanguage.TH))
        assertEquals(listOf("ข้าว", "ไก่ต้ม"), fullyTranslated.displayIngredients(AppLanguage.TH))
    }

    // EN language must prefer the English fields when they were translated (AI response or preset data).
    @Test
    fun en_language_uses_english_fields_when_present() {
        assertEquals("Hainanese Chicken Rice", fullyTranslated.displayFoodName(AppLanguage.EN))
        assertEquals("1 plate (350g)", fullyTranslated.displayPortionSize(AppLanguage.EN))
        assertEquals("High in protein from chicken.", fullyTranslated.displayHealthTip(AppLanguage.EN))
        assertEquals(listOf("Rice", "Boiled chicken"), fullyTranslated.displayIngredients(AppLanguage.EN))
    }

    // DE language must prefer the German fields when they were translated (AI response or preset data).
    @Test
    fun de_language_uses_german_fields_when_present() {
        assertEquals("Hainan-Hühnchenreis", fullyTranslated.displayFoodName(AppLanguage.DE))
        assertEquals("1 Teller (350g)", fullyTranslated.displayPortionSize(AppLanguage.DE))
        assertEquals("Reich an Protein durch Hühnchen.", fullyTranslated.displayHealthTip(AppLanguage.DE))
        assertEquals(listOf("Reis", "Gekochtes Hühnchen"), fullyTranslated.displayIngredients(AppLanguage.DE))
    }

    // Regression test for the reported bug: when the AI/preset only produced Thai text (blank/empty
    // En/De fields), EN/DE mode must fall back to the Thai default instead of rendering blank content.
    @Test
    fun en_language_falls_back_to_thai_when_english_translation_missing() {
        assertEquals("ข้าวมันไก่ต้ม", thaiOnly.displayFoodName(AppLanguage.EN))
        assertEquals("1 จาน (350g)", thaiOnly.displayPortionSize(AppLanguage.EN))
        assertEquals("โปรตีนสูงจากเนื้อไก่", thaiOnly.displayHealthTip(AppLanguage.EN))
        assertEquals(listOf("ข้าว", "ไก่ต้ม"), thaiOnly.displayIngredients(AppLanguage.EN))
    }

    @Test
    fun de_language_falls_back_to_thai_when_german_translation_missing() {
        assertEquals("ข้าวมันไก่ต้ม", thaiOnly.displayFoodName(AppLanguage.DE))
        assertEquals("1 จาน (350g)", thaiOnly.displayPortionSize(AppLanguage.DE))
        assertEquals("โปรตีนสูงจากเนื้อไก่", thaiOnly.displayHealthTip(AppLanguage.DE))
        assertEquals(listOf("ข้าว", "ไก่ต้ม"), thaiOnly.displayIngredients(AppLanguage.DE))
    }
}
