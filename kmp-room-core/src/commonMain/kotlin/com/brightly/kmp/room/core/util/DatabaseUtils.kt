package com.brightly.kmp.room.core.util

/**
 * Platform-specific database utilities.
 * Implement these functions for each platform to handle database file operations.
 */
expect object DatabaseUtils {

    /**
     * Check if database file exists.
     *
     * @param name Database file name
     * @return true if database exists, false otherwise
     */
    fun databaseExists(name: String): Boolean

    /**
     * Delete database file.
     *
     * @param name Database file name
     * @return true if successfully deleted, false otherwise
     */
    fun deleteDatabase(name: String): Boolean

    /**
     * Get full path to database file.
     *
     * @param name Database file name
     * @return Full file system path to database
     */
    fun getDatabasePath(name: String): String
}