package com.brightly.kmpdatabasepoc.data.database

import android.content.Context
import androidx.room.migration.Migration
import com.brightly.kmp.room.core.DatabaseConfig
import com.brightly.kmp.room.core.android.AndroidDatabaseFactory

actual class DatabaseFactory(
    context: Context
) : AndroidDatabaseFactory<AppDatabase>(context) {

    actual fun createDatabase(): AppDatabase {
        return createDatabase(
            name = "app.db",
            migrations = emptyList()
        )
    }

    override fun createDatabase(
        name: String,
        migrations: List<Migration>
    ): AppDatabase {
        return buildDatabase(
            AppDatabase::class.java,
            DatabaseConfig(
                name = name,
                version = 1,
                enableLogging = false,
                migrations = migrations
            )
        )
    }
}