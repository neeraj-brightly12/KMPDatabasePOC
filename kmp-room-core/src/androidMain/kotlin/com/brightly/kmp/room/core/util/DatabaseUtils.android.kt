package com.brightly.kmp.room.core.util

import android.content.Context
import java.io.File

/**
 * Android implementation of DatabaseUtils.
 * Note: Requires Context to be set before use.
 */
actual object DatabaseUtils {

    private var applicationContext: Context? = null

    /**
     * Initialize DatabaseUtils with application context.
     * Call this from your Application class or MainActivity.
     */
    fun init(context: Context) {
        applicationContext = context.applicationContext
    }

    private fun getContext(): Context {
        return applicationContext
            ?: throw IllegalStateException(
                "DatabaseUtils not initialized. Call DatabaseUtils.init(context) first."
            )
    }

    actual fun databaseExists(name: String): Boolean {
        val dbFile = getContext().getDatabasePath(name)
        return dbFile.exists()
    }

    actual fun deleteDatabase(name: String): Boolean {
        return getContext().deleteDatabase(name)
    }

    actual fun getDatabasePath(name: String): String {
        return getContext().getDatabasePath(name).absolutePath
    }
}