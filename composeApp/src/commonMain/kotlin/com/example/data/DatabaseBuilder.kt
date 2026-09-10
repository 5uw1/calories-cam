package com.example.data

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver

/** Each platform supplies a Room builder pointed at its own writable DB path. */
expect fun getDatabaseBuilder(): RoomDatabase.Builder<FoodDatabase>

/** IO-capable dispatcher; Dispatchers.IO is JVM-only, so it is provided per platform. */
expect fun dbQueryContext(): kotlin.coroutines.CoroutineContext

fun createFoodDatabase(): FoodDatabase =
    getDatabaseBuilder()
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(dbQueryContext())
        .fallbackToDestructiveMigration(dropAllTables = true)
        .build()
