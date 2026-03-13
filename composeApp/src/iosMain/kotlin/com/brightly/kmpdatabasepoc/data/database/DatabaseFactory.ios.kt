package com.brightly.kmpdatabasepoc.data.database

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

actual class DatabaseFactory {
    @OptIn(ExperimentalForeignApi::class)
    actual fun createDatabase(): AppDatabase {
        val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null
        )
        val dbFile = requireNotNull(documentDirectory?.path) + "/app.db"
        return Room.databaseBuilder<AppDatabase>(
            name = dbFile,
        )
            .setDriver(BundledSQLiteDriver())
            .build()
    }
}