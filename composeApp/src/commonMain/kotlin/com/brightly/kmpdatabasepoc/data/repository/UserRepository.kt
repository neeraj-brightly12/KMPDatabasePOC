package com.brightly.kmpdatabasepoc.data.repository

import com.brightly.kmpdatabasepoc.data.dao.UserDao
import com.brightly.kmpdatabasepoc.data.database.AppDatabase
import com.brightly.kmpdatabasepoc.data.entity.UserEntity
import com.brightly.kmp.room.core.base.BaseRepository
import kotlinx.coroutines.flow.Flow

class UserRepository(
    database: AppDatabase
    ) : BaseRepository<UserEntity, UserDao>(database.userDao()) {

    suspend fun addUser(name: String) {
        val user = UserEntity(name = name)
        add(user) // Using generalized add() method from BaseRepository
    }

    fun getUsers(): Flow<List<UserEntity>> {
        return dao.getUsers()
    }

    suspend fun getUserById(id: Int): UserEntity? {
        return dao.getUserById(id)
    }

    suspend fun deleteAllUsers() {
        dao.deleteAllUsers()
    }
}