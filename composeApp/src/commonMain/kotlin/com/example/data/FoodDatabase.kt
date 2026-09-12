package com.example.data

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import com.example.model.FoodEntry

@Database(entities = [FoodEntry::class], version = 2, exportSchema = false)
@ConstructedBy(FoodDatabaseConstructor::class)
abstract class FoodDatabase : RoomDatabase() {
    abstract fun foodDao(): FoodDao
}

// KSP generates the actual implementation for each platform target.
@Suppress("NO_ACTUAL_FOR_EXPECT", "KotlinNoActualForExpect")
expect object FoodDatabaseConstructor : RoomDatabaseConstructor<FoodDatabase> {
    override fun initialize(): FoodDatabase
}

const val DB_FILE_NAME = "food_calorie_db"
