package com.brightly.kmpdatabasepoc.data.database

import androidx.room.migration.Migration
import com.brightly.kmp.room.core.DatabaseConfig
import com.brightly.kmp.room.core.ios.IosDatabaseFactory

actual class DatabaseFactory : IosDatabaseFactory<AppDatabase>() {

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
            DatabaseConfig(
                name = name,
                version = 1,
                migrations = migrations
            )
        )
    }
}