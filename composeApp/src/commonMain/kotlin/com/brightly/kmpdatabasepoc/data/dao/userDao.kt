package com.brightly.kmpdatabasepoc.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.brightly.kmpdatabasepoc.data.entity.UserEntity
import com.brightly.kmp.room.core.base.BaseDao
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao : BaseDao<UserEntity> {

    @Query("SELECT * FROM users")
    fun getUsers(): Flow<List<UserEntity>>

    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getUserById(id: Int): UserEntity?

    @Query("DELETE FROM users")
    suspend fun deleteAllUsers()
}