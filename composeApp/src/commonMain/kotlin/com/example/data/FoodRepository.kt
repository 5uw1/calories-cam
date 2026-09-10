package com.example.data

import com.example.model.FoodEntry
import kotlinx.coroutines.flow.Flow

class FoodRepository(private val foodDao: FoodDao) {
    val allEntries: Flow<List<FoodEntry>> = foodDao.getAllFoodEntries()

    fun getEntriesForDay(startOfDay: Long, endOfDay: Long): Flow<List<FoodEntry>> {
        return foodDao.getEntriesBetween(startOfDay, endOfDay)
    }

    suspend fun insert(entry: FoodEntry): Long {
        return foodDao.insertEntry(entry)
    }

    suspend fun delete(entry: FoodEntry) {
        foodDao.deleteEntry(entry)
    }

    suspend fun deleteById(id: Long) {
        foodDao.deleteById(id)
    }

    suspend fun update(entry: FoodEntry) {
        foodDao.updateEntry(entry)
    }
}
