package com.brightly.kmp.room.core

import androidx.room.migration.Migration

/**
 * Configuration for Room database creation.
 *
 * @property name Database file name
 * @property version Database schema version
 * @property enableLogging Enable SQL query logging for debugging
 * @property migrations List of migration strategies
 */
data class DatabaseConfig(
    val name: String,
    val version: Int,
    val enableLogging: Boolean = false,
    val migrations: List<Migration> = emptyList()
)
