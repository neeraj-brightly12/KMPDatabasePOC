package com.brightly.kmpdatabasepoc.data.database

import android.content.Context
import androidx.room.Room

actual class DatabaseFactory(
    private val context: Context
) {

    actual fun createDatabase(): AppDatabase {

        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app.db"
        ).build()
    }
}