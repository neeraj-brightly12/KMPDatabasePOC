package com.brightly.kmpdatabasepoc.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.brightly.kmpdatabasepoc.data.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM users")
    fun getUsers(): Flow<List<UserEntity>>
}