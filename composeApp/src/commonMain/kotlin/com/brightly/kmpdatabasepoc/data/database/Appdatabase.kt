package com.brightly.kmpdatabasepoc.data.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import com.brightly.kmpdatabasepoc.data.dao.ProductDao
import com.brightly.kmpdatabasepoc.data.dao.UserDao
import com.brightly.kmpdatabasepoc.data.entity.ProductEntity
import com.brightly.kmpdatabasepoc.data.entity.UserEntity

@Database(
    entities = [UserEntity::class, ProductEntity::class],
    version = 2,
    exportSchema = false
)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun productDao(): ProductDao
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase>