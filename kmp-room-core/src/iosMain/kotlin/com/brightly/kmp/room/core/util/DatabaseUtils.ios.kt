package com.brightly.kmp.room.core.util

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

/**
 * iOS implementation of DatabaseUtils.
 */
@OptIn(ExperimentalForeignApi::class)
actual object DatabaseUtils {

    /**
     * Get the iOS Documents directory path.
     */
    private fun getDocumentsDirectory(): String {
        val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null
        )
        return requireNotNull(documentDirectory?.path) {
            "Failed to get Documents directory"
        }
    }

    actual fun databaseExists(name: String): Boolean {
        val dbPath = getDatabasePath(name)
        return NSFileManager.defaultManager.fileExistsAtPath(dbPath)
    }

    actual fun deleteDatabase(name: String): Boolean {
        val dbPath = getDatabasePath(name)
        return try {
            NSFileManager.defaultManager.removeItemAtPath(dbPath, null)
            true
        } catch (e: Exception) {
            false
        }
    }

    actual fun getDatabasePath(name: String): String {
        return "${getDocumentsDirectory()}/$name"
    }
}