# Base DAO & Repository Pattern Usage Guide

This module provides reusable base classes for Room database operations across different modules.

## Overview

- **BaseDao**: Generic DAO interface with common CRUD operations
- **BaseRepository**: Abstract repository class with common database operations

## How to Use

### 1. Create Your Entity

```kotlin
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String
)
```

### 2. Create DAO Extending BaseDao

```kotlin
@Dao
interface UserDao : BaseDao<UserEntity> {
    // Inherit: insert(), insertAll(), update(), updateAll(), delete(), deleteAll()

    // Add your custom queries
    @Query("SELECT * FROM users")
    fun getUsers(): Flow<List<UserEntity>>

    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getUserById(id: Int): UserEntity?
}
```

### 3. Create Repository Extending BaseRepository

```kotlin
class UserRepository(
    database: AppDatabase
) : BaseRepository<UserEntity, UserDao>(database.userDao()) {
    // Inherit: add(), addAll(), update(), updateAll(), delete(), deleteAll()

    // Add your custom methods
    fun getUsers(): Flow<List<UserEntity>> = dao.getUsers()

    suspend fun getUserById(id: Int): UserEntity? = dao.getUserById(id)
}
```

## Available Methods

### BaseDao Methods
- `suspend fun insert(entity: T)` - Insert single entity
- `suspend fun insertAll(entities: List<T>)` - Insert multiple entities
- `suspend fun update(entity: T)` - Update single entity
- `suspend fun updateAll(entities: List<T>)` - Update multiple entities
- `suspend fun delete(entity: T)` - Delete single entity
- `suspend fun deleteAll(entities: List<T>)` - Delete multiple entities

### BaseRepository Methods
- `suspend fun add(entity: T)` - Add single entity
- `suspend fun addAll(entities: List<T>)` - Add multiple entities
- `suspend fun update(entity: T)` - Update single entity
- `suspend fun updateAll(entities: List<T>)` - Update multiple entities
- `suspend fun delete(entity: T)` - Delete single entity
- `suspend fun deleteAll(entities: List<T>)` - Delete multiple entities

## Usage Examples

```kotlin
// Using inherited methods
val user = UserEntity(name = "John")
userRepository.add(user) // From BaseRepository

// Using custom methods
val allUsers = userRepository.getUsers() // Custom method

// Bulk operations
val users = listOf(
    UserEntity(name = "Alice"),
    UserEntity(name = "Bob")
)
userRepository.addAll(users) // From BaseRepository

// Update
user.copy(name = "John Doe").let {
    userRepository.update(it) // From BaseRepository
}

// Delete
userRepository.delete(user) // From BaseRepository
```

## Benefits

✅ **Code Reusability** - Write CRUD operations once, use everywhere
✅ **Type Safety** - Generic types ensure compile-time safety
✅ **Consistency** - Same operations across all entities
✅ **Maintainability** - Update base class to affect all repositories
✅ **Cross-Module** - Can be used in any module that depends on kmp-room-core

## Adding New Entities

Simply create new entity, DAO, and repository following the pattern:

```kotlin
// 1. Entity
@Entity(tableName = "products")
data class ProductEntity(...)

// 2. DAO
@Dao
interface ProductDao : BaseDao<ProductEntity> {
    // Custom queries
}

// 3. Repository
class ProductRepository(db: AppDatabase)
    : BaseRepository<ProductEntity, ProductDao>(db.productDao()) {
    // Custom methods
}
```

That's it! All CRUD operations are automatically available.