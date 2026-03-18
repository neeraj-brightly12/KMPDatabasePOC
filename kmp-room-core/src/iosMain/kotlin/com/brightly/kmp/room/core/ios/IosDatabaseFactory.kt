package com.brightly.kmp.room.core.ios

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.brightly.kmp.room.core.DatabaseConfig
import com.brightly.kmp.room.core.KmpDatabaseFactory
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

/**
 * iOS-specific base factory for creating Room databases.
 * Extend this class in your iOS app to create your specific database.
 *
 * Example usage:
 * ```
 * class AppDatabaseFactory : IosDatabaseFactory<AppDatabase>() {
 *     override fun createDatabase(
 *         name: String,
 *         migrations: List<Migration>
 *     ): AppDatabase {
 *         return buildDatabase(
 *             DatabaseConfig(name = name, version = 1, migrations = migrations)
 *         )
 *     }
 * }
 * ```
 */
abstract class IosDatabaseFactory<T : RoomDatabase> : KmpDatabaseFactory<T> {

    /**
     * Helper function to build a Room database with iOS-specific configuration.
     * Uses BundledSQLiteDriver and stores database in Documents directory.
     *
     * @param config Database configuration
     * @return Configured RoomDatabase instance
     */
    @OptIn(ExperimentalForeignApi::class)
    protected inline fun <reified T : RoomDatabase> buildDatabase(
        config: DatabaseConfig
    ): T {
        // Get iOS Documents directory
        val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null
        )

        // Construct full database file path
        val dbFile = requireNotNull(documentDirectory?.path) + "/${config.name}"

        return Room.databaseBuilder<T>(
            name = dbFile
        ).apply {
            // Set the bundled SQLite driver (required for iOS)
            setDriver(BundledSQLiteDriver())

            // Add migrations
            if (config.migrations.isNotEmpty()) {
                addMigrations(*config.migrations.toTypedArray())
            }

            // Note: Query logging not yet supported on iOS
        }.build()
    }

    /**
     * Alternative builder with individual parameters for convenience.
     */
    protected inline fun <reified T : RoomDatabase> buildDatabase(
        name: String,
        version: Int,
        migrations: List<Migration> = emptyList()
    ): T {
        return buildDatabase(
            DatabaseConfig(
                name = name,
                version = version,
                migrations = migrations
            )
        )
    }
}