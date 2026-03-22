package com.brightly.kmpdatabasepoc.`data`.database

import androidx.room.RoomDatabaseConstructor

public actual object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
  actual override fun initialize(): AppDatabase =
      com.brightly.kmpdatabasepoc.`data`.database.AppDatabase_Impl()
}
