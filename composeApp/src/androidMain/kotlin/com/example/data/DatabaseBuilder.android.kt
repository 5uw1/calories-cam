package com.example.data

import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.AndroidApp
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

actual fun dbQueryContext(): CoroutineContext = Dispatchers.IO

actual fun getDatabaseBuilder(): RoomDatabase.Builder<FoodDatabase> {
    val ctx = AndroidApp.context
    val dbFile = ctx.getDatabasePath(DB_FILE_NAME)
    return Room.databaseBuilder<FoodDatabase>(
        context = ctx,
        name = dbFile.absolutePath
    )
}
