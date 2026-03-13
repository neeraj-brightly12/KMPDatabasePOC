package com.brightly.kmpdatabasepoc.data.repository

import com.brightly.kmpdatabasepoc.data.database.AppDatabase
import com.brightly.kmpdatabasepoc.data.entity.UserEntity
import kotlinx.coroutines.flow.Flow

class UserRepository(
    database: AppDatabase
) {

    private val dao = database.userDao()

    suspend fun addUser(name: String) {

        val user = UserEntity(
            name = name
        )

        dao.insertUser(user)
    }

    fun getUsers(): Flow<List<UserEntity>> {

        return dao.getUsers()
    }
}