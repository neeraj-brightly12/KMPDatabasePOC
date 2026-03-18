package com.brightly.kmp.room.core.base

/**
 * Base repository class providing common CRUD operations for all repositories.
 * All specific repositories should extend this class to inherit these operations.
 *
 * @param T The entity type this repository operates on
 * @param DAO The DAO type that extends BaseDao<T>
 * @property dao The DAO instance for database operations
 */
abstract class BaseRepository<T, DAO : BaseDao<T>>(
    protected val dao: DAO
) {

    /**
     * Insert a single entity into the database.
     */
    suspend fun add(entity: T) {
        dao.insert(entity)
    }

    /**
     * Insert multiple entities into the database.
     */
    suspend fun addAll(entities: List<T>) {
        dao.insertAll(entities)
    }

    /**
     * Update an existing entity in the database.
     */
    suspend fun update(entity: T) {
        dao.update(entity)
    }

    /**
     * Update multiple entities in the database.
     */
    suspend fun updateAll(entities: List<T>) {
        dao.updateAll(entities)
    }

    /**
     * Delete an entity from the database.
     */
    suspend fun delete(entity: T) {
        dao.delete(entity)
    }

    /**
     * Delete multiple entities from the database.
     */
    suspend fun deleteAll(entities: List<T>) {
        dao.deleteAll(entities)
    }
}