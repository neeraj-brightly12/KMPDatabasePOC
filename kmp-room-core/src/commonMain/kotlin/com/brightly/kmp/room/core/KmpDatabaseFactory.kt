package com.brightly.kmp.room.core

import androidx.room.RoomDatabase
import androidx.room.migration.Migration

/**
 * Platform-agnostic database factory interface.
 * Implement this interface to create Room databases across Android and iOS.
 *
 * @param T The RoomDatabase type to be created
 */
interface KmpDatabaseFactory<T : RoomDatabase> {

    /**
     * Creates a database instance with the specified configuration.
     *
     * @param name Database file name
     * @param migrations List of migration strategies for schema changes
     * @return Configured RoomDatabase instance
     */
    fun createDatabase(
        name: String,
        migrations: List<Migration> = emptyList()
    ): T
}