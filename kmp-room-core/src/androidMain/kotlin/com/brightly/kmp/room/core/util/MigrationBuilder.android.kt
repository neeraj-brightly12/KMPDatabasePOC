package com.brightly.kmp.room.core.util

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Android-specific DSL builder for creating Room database migrations.
 *
 * Example usage:
 * ```
 * val migration1to2 = migration(1, 2) { database ->
 *     database.execSQL("ALTER TABLE users ADD COLUMN age INTEGER")
 * }
 * ```
 *
 * @param startVersion Starting database version
 * @param endVersion Target database version
 * @param block Migration logic to execute
 * @return Migration instance
 */
fun migration(
    startVersion: Int,
    endVersion: Int,
    block: (SupportSQLiteDatabase) -> Unit
): Migration {
    return object : Migration(startVersion, endVersion) {
        override fun migrate(database: SupportSQLiteDatabase) {
            block(database)
        }
    }
}