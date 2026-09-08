package com.example

import com.example.util.AppLanguage
import com.example.util.AppStrings
import com.example.util.LanguageManager
import org.junit.Assert.assertEquals
import org.junit.Test

class LanguageManagerTest {

    @Test
    fun testDefaultLanguageIsEnglish() {
        assertEquals(AppLanguage.EN, LanguageManager.currentLanguage.value)
    }

    @Test
    fun testToggleLanguageChangesBetweenEnglishAndThai() {
        LanguageManager.setLanguage(AppLanguage.EN)
        assertEquals(AppLanguage.EN, LanguageManager.currentLanguage.value)

        LanguageManager.toggleLanguage()
        assertEquals(AppLanguage.TH, LanguageManager.currentLanguage.value)

        LanguageManager.toggleLanguage()
        assertEquals(AppLanguage.EN, LanguageManager.currentLanguage.value)
    }

    @Test
    fun testAppStringsProvideBothEnglishAndThai() {
        assertEquals("Today", AppStrings.navToday(AppLanguage.EN))
        assertEquals("วันนี้", AppStrings.navToday(AppLanguage.TH))

        assertEquals("Scan Food", AppStrings.navScan(AppLanguage.EN))
        assertEquals("สแกนอาหาร", AppStrings.navScan(AppLanguage.TH))

        assertEquals("Breakfast", AppStrings.mealBreakfast(AppLanguage.EN))
        assertEquals("มื้อเช้า", AppStrings.mealBreakfast(AppLanguage.TH))

        assertEquals("Protein", AppStrings.protein(AppLanguage.EN))
        assertEquals("โปรตีน", AppStrings.protein(AppLanguage.TH))

        assertEquals("🇨🇭 Swiss Dishes", AppStrings.tabSwiss(AppLanguage.EN))
        assertEquals("🇨🇭 อาหารสวิส", AppStrings.tabSwiss(AppLanguage.TH))
    }

    @Test
    fun testSwissDishesArePopulated() {
        val swissDishes = com.example.model.FoodSamples.swissDishes
        org.junit.Assert.assertTrue(swissDishes.isNotEmpty())
        org.junit.Assert.assertTrue(swissDishes.any { it.foodNameEn.contains("Fondue") })
        org.junit.Assert.assertTrue(swissDishes.any { it.foodNameEn.contains("Raclette") })
        org.junit.Assert.assertTrue(swissDishes.any { it.foodNameEn.contains("Rösti") })
        org.junit.Assert.assertTrue(swissDishes.any { it.foodNameEn.contains("Muesli") })
    }
}
