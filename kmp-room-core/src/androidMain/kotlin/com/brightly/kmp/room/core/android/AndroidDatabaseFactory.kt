package com.brightly.kmp.room.core.android

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import com.brightly.kmp.room.core.DatabaseConfig
import com.brightly.kmp.room.core.KmpDatabaseFactory

/**
 * Android-specific base factory for creating Room databases.
 * Extend this class in your Android app to create your specific database.
 *
 * Example usage:
 * ```
 * class AppDatabaseFactory(context: Context) :
 *     AndroidDatabaseFactory<AppDatabase>(context) {
 *
 *     override fun createDatabase(
 *         name: String,
 *         migrations: List<Migration>
 *     ): AppDatabase {
 *         return buildDatabase(
 *             AppDatabase::class.java,
 *             DatabaseConfig(name = name, version = 1, migrations = migrations)
 *         )
 *     }
 * }
 * ```
 *
 * @param context Android Context (use applicationContext to avoid memory leaks)
 */
abstract class AndroidDatabaseFactory<T : RoomDatabase>(
    protected val context: Context
) : KmpDatabaseFactory<T> {

    /**
     * Helper function to build a Room database with standard configuration.
     *
     * @param databaseClass The Room database class
     * @param config Database configuration
     * @return Configured RoomDatabase instance
     */
    protected fun buildDatabase(
        databaseClass: Class<T>,
        config: DatabaseConfig
    ): T {
        return Room.databaseBuilder(
            context,
            databaseClass,
            config.name
        ).apply {
            // Add migrations
            if (config.migrations.isNotEmpty()) {
                addMigrations(*config.migrations.toTypedArray())
            }

            // Note: Query logging can be enabled in debug builds
            // by using setQueryCallback in your concrete factory
        }.build()
    }

    /**
     * Alternative builder with individual parameters for convenience.
     */
    protected fun buildDatabase(
        databaseClass: Class<T>,
        name: String,
        version: Int,
        migrations: List<Migration> = emptyList(),
        enableLogging: Boolean = false
    ): T {
        return buildDatabase(
            databaseClass,
            DatabaseConfig(
                name = name,
                version = version,
                migrations = migrations,
                enableLogging = enableLogging
            )
        )
    }
}