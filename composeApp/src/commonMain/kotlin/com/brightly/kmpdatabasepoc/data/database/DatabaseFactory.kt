package com.brightly.kmpdatabasepoc.data.database

expect class DatabaseFactory {

    fun createDatabase(): AppDatabase
}