package com.example

import com.example.api.GeminiFoodService
import com.example.api.createHttpClient
import com.example.data.FoodRepository
import com.example.data.createFoodDatabase

/**
 * Minimal manual DI graph shared by both platforms.
 * Platform entry points build one AppContainer and hand its ViewModel to App().
 */
class AppContainer {
    private val database by lazy { createFoodDatabase() }
    private val repository by lazy { FoodRepository(database.foodDao()) }
    private val httpClient by lazy { createHttpClient() }
    private val geminiService by lazy { GeminiFoodService(httpClient) }

    fun createViewModel(): com.example.ui.FoodViewModel =
        com.example.ui.FoodViewModel(repository, geminiService)
}
