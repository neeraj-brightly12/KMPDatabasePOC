package com.brightly.kmp.room.core.base

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

/**
 * Base DAO interface providing common CRUD operations for all entities.
 * All specific DAOs should extend this interface to inherit these operations.
 *
 * @param T The entity type this DAO operates on
 */
interface BaseDao<T> {

    /**
     * Insert a single entity into the database.
     * If conflict occurs, it will replace the existing entity.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: T)

    /**
     * Insert multiple entities into the database.
     * If conflict occurs, it will replace the existing entities.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<T>)

    /**
     * Update an existing entity in the database.
     */
    @Update
    suspend fun update(entity: T)

    /**
     * Update multiple entities in the database.
     */
    @Update
    suspend fun updateAll(entities: List<T>)

    /**
     * Delete an entity from the database.
     */
    @Delete
    suspend fun delete(entity: T)

    /**
     * Delete multiple entities from the database.
     */
    @Delete
    suspend fun deleteAll(entities: List<T>)
}