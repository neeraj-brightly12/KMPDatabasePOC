# Library Architecture Guide

## What Goes Where: Library vs Consumer App

This guide answers the crucial question: **"Where should database manipulation methods (insert, delete, getValues) be placed?"**

---

## Quick Answer

| What | Where | Why |
|------|-------|-----|
| **Database manipulation methods** (insert, delete, queries) | **Consumer App** | Each app has different data models |
| **Infrastructure** (factories, config, utilities) | **Library** | Shared platform-specific setup |

---

## 🎯 Core Principle

> **The library provides the foundation. The consumer app provides the data model and business logic.**

Think of it like a framework:
- **Library = Android SDK / iOS UIKit** → Provides infrastructure
- **Consumer App = Your actual app** → Uses infrastructure for specific needs

---

## 📦 What Goes IN the Library (kmp-room-core)

### ✅ Include These:

#### 1. Platform-Specific Database Factories
```kotlin
// ✅ IN LIBRARY
abstract class AndroidDatabaseFactory<T : RoomDatabase>(
    protected val context: Context
) {
    protected fun buildDatabase(klass: Class<T>, config: DatabaseConfig): T {
        return Room.databaseBuilder(context, klass, config.name)
            .apply {
                config.migrations.forEach { addMigrations(it) }
                if (config.enableLogging) {
                    setQueryCallback({ sql, _ -> println("SQL: $sql") }, { it.run() })
                }
            }.build()
    }
    abstract fun createDatabase(name: String, migrations: List<Migration>): T
}

abstract class IosDatabaseFactory<T : RoomDatabase> {
    protected fun buildDatabase(config: DatabaseConfig): T {
        val dbFile = NSHomeDirectory() + "/${config.name}"
        return Room.databaseBuilder<T>(name = dbFile, factory = { getRoomDatabase() })
            .apply {
                config.migrations.forEach { addMigrations(it) }
                setDriver(BundledSQLiteDriver())
            }.build()
    }
    abstract fun createDatabase(name: String, migrations: List<Migration>): T
}
```

**Why?** Platform-specific setup is the same for all apps. Android needs Context, iOS needs path resolution.

---

#### 2. Configuration Classes
```kotlin
// ✅ IN LIBRARY
data class DatabaseConfig(
    val name: String,
    val version: Int = 1,
    val enableLogging: Boolean = false,
    val migrations: List<Migration> = emptyList()
)
```

**Why?** Standard configuration options that all apps need.

---

#### 3. Utility Extensions (Optional)
```kotlin
// ✅ IN LIBRARY (if needed across all apps)
suspend fun <T> RoomDatabase.withTransaction(block: suspend () -> T): T {
    beginTransaction()
    try {
        val result = block()
        setTransactionSuccessful()
        return result
    } finally {
        endTransaction()
    }
}
```

**Why?** Common patterns that all apps can benefit from.

---

#### 4. Base Classes / Interfaces (Optional)
```kotlin
// ✅ IN LIBRARY (if you want to enforce patterns)
interface BaseRepository<T> {
    suspend fun insert(entity: T)
    suspend fun delete(entity: T)
    fun getAll(): Flow<List<T>>
}
```

**Why?** If you want all consumer apps to follow the same architecture pattern.

---

### ❌ DO NOT Include These:

#### 1. Entities (Data Models)
```kotlin
// ❌ NOT IN LIBRARY
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val email: String
)
```

**Why?** Each app has different data models. A shopping app needs Products, a chat app needs Messages.

---

#### 2. DAOs (Data Access Objects)
```kotlin
// ❌ NOT IN LIBRARY
@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: UserEntity)

    @Delete
    suspend fun delete(user: UserEntity)

    @Query("SELECT * FROM users")
    fun getAll(): Flow<List<UserEntity>>

    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserById(userId: Int): UserEntity?
}
```

**Why?** DAOs are specific to each app's entities and business logic.

---

#### 3. Database Definition
```kotlin
// ❌ NOT IN LIBRARY
@Database(
    entities = [UserEntity::class, ProductEntity::class],
    version = 1
)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun productDao(): ProductDao
}
```

**Why?** Each app has different entities and DAOs.

---

#### 4. Repositories
```kotlin
// ❌ NOT IN LIBRARY
class UserRepository(private val userDao: UserDao) {
    suspend fun insert(user: UserEntity) = userDao.insert(user)
    suspend fun delete(user: UserEntity) = userDao.delete(user)
    fun getAll(): Flow<List<UserEntity>> = userDao.getAll()
}
```

**Why?** Repository implements app-specific business logic.

---

## 🏗️ Complete Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                     CONSUMER APP                            │
│                                                             │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐    │
│  │   Entities   │  │     DAOs     │  │  Repositories│    │
│  │              │  │              │  │              │    │
│  │ UserEntity   │  │   UserDao    │  │ UserRepo     │    │
│  │ ProductEntity│  │  ProductDao  │  │ ProductRepo  │    │
│  └──────────────┘  └──────────────┘  └──────────────┘    │
│                                                             │
│  ┌──────────────────────────────────────────────────────┐ │
│  │         Database Definition (AppDatabase)            │ │
│  └──────────────────────────────────────────────────────┘ │
│                           │                                 │
│                           │ extends                         │
│                           ▼                                 │
│  ┌──────────────────────────────────────────────────────┐ │
│  │    Concrete Factory (DatabaseFactory)                │ │
│  │                                                        │ │
│  │    actual class DatabaseFactory(context):            │ │
│  │        AndroidDatabaseFactory<AppDatabase>(context)  │ │
│  └──────────────────────────────────────────────────────┘ │
│                           │                                 │
│                           │ extends                         │
└───────────────────────────┼─────────────────────────────────┘
                            │
┌───────────────────────────┼─────────────────────────────────┐
│                           │          LIBRARY                │
│                           ▼                                 │
│  ┌──────────────────────────────────────────────────────┐ │
│  │   Base Factories (AndroidDatabaseFactory,            │ │
│  │                   IosDatabaseFactory)                │ │
│  │                                                        │ │
│  │   - buildDatabase()                                  │ │
│  │   - Platform-specific setup                          │ │
│  │   - Room.databaseBuilder configuration               │ │
│  └──────────────────────────────────────────────────────┘ │
│                                                             │
│  ┌──────────────────────────────────────────────────────┐ │
│  │   Configuration (DatabaseConfig)                     │ │
│  │                                                        │ │
│  │   - name, version, migrations, logging               │ │
│  └──────────────────────────────────────────────────────┘ │
│                                                             │
│  ┌──────────────────────────────────────────────────────┐ │
│  │   Utilities (Optional)                               │ │
│  │                                                        │ │
│  │   - Extension functions                              │ │
│  │   - Helper classes                                   │ │
│  └──────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────┘
```

---

## 📝 Real Example: Two Different Apps

### App 1: E-Commerce App

```kotlin
// Consumer App Code
@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: String,
    val name: String,
    val price: Double,
    val stock: Int
)

@Dao
interface ProductDao {
    @Insert
    suspend fun insertProduct(product: ProductEntity)

    @Query("SELECT * FROM products WHERE stock > 0")
    fun getAvailableProducts(): Flow<List<ProductEntity>>

    @Query("UPDATE products SET stock = stock - 1 WHERE id = :productId")
    suspend fun decrementStock(productId: String)
}

@Database(entities = [ProductEntity::class, OrderEntity::class], version = 1)
abstract class EcommerceDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun orderDao(): OrderDao
}

// Uses library's AndroidDatabaseFactory
actual class DatabaseFactory(context: Context) :
    AndroidDatabaseFactory<EcommerceDatabase>(context) {
    override fun createDatabase(name: String, migrations: List<Migration>): EcommerceDatabase {
        return buildDatabase(
            EcommerceDatabase::class.java,
            DatabaseConfig(name = name, migrations = migrations)
        )
    }
}
```

---

### App 2: Chat App

```kotlin
// Consumer App Code
@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey val id: String,
    val senderId: String,
    val text: String,
    val timestamp: Long
)

@Dao
interface MessageDao {
    @Insert
    suspend fun insertMessage(message: MessageEntity)

    @Query("SELECT * FROM messages ORDER BY timestamp DESC LIMIT 50")
    fun getRecentMessages(): Flow<List<MessageEntity>>

    @Query("DELETE FROM messages WHERE timestamp < :cutoffTime")
    suspend fun deleteOldMessages(cutoffTime: Long)
}

@Database(entities = [MessageEntity::class, UserEntity::class], version = 1)
abstract class ChatDatabase : RoomDatabase() {
    abstract fun messageDao(): MessageDao
    abstract fun userDao(): UserDao
}

// Uses SAME library's AndroidDatabaseFactory
actual class DatabaseFactory(context: Context) :
    AndroidDatabaseFactory<ChatDatabase>(context) {
    override fun createDatabase(name: String, migrations: List<Migration>): ChatDatabase {
        return buildDatabase(
            ChatDatabase::class.java,
            DatabaseConfig(name = name, migrations = migrations)
        )
    }
}
```

---

## 🎓 Key Takeaways

### 1. Library is a Tool, Not the Application
- Library provides infrastructure (how to build a database)
- Consumer app provides data model (what to store in database)

### 2. Separation of Concerns
- **Library**: Platform-specific concerns (Android vs iOS setup)
- **Consumer App**: Business-specific concerns (users, products, messages)

### 3. Reusability
- Same library can be used by shopping app, chat app, todo app
- Each app has different entities and DAOs
- All apps benefit from same platform-specific setup

### 4. Maintainability
- Update library once → all apps get platform improvements
- Update app's entities → doesn't affect library
- Clear boundaries = easier maintenance

---

## 🔍 Common Mistakes

### ❌ Mistake 1: Putting Entities in Library
```kotlin
// ❌ WRONG - Don't do this in library
@Entity
data class UserEntity(...)  // This is app-specific!
```

**Problem:** Not all apps need a "User" entity. A weather app doesn't have users.

---

### ❌ Mistake 2: Putting DAOs in Library
```kotlin
// ❌ WRONG - Don't do this in library
@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: UserEntity)  // App-specific!
}
```

**Problem:** DAOs are tied to entities, which are app-specific.

---

### ❌ Mistake 3: Making Library Too Specific
```kotlin
// ❌ WRONG - Too specific
class ShoppingDatabaseFactory : AndroidDatabaseFactory<ShoppingDatabase>

// ✅ CORRECT - Generic
abstract class AndroidDatabaseFactory<T : RoomDatabase>
```

**Problem:** Generic library can be reused by any app.

---

## ✅ What You Should Do

### In Library (kmp-room-core)
1. Keep AndroidDatabaseFactory and IosDatabaseFactory
2. Keep DatabaseConfig
3. Add utility functions if they're useful for ALL apps
4. Keep it generic and reusable

### In Consumer App
1. Define your entities (UserEntity, ProductEntity, etc.)
2. Define your DAOs with specific queries
3. Define your Database class
4. Create concrete DatabaseFactory extending library's base class
5. Implement repositories for business logic

---

## 📊 Summary Table

| Functionality | Location | Reason |
|--------------|----------|--------|
| Platform-specific DB setup | Library | Same for all apps |
| Context handling (Android) | Library | Android platform concern |
| Path resolution (iOS) | Library | iOS platform concern |
| DatabaseConfig | Library | Standard configuration |
| Generic utilities | Library | Useful for all apps |
| **Entities** | **Consumer App** | **App-specific data model** |
| **DAOs** | **Consumer App** | **App-specific queries** |
| **Database class** | **Consumer App** | **App-specific schema** |
| **Repositories** | **Consumer App** | **App-specific logic** |
| **Database manipulation** | **Consumer App** | **App-specific operations** |

---

## 🚀 Benefits of This Architecture

### 1. Flexibility
- Each app can have completely different data models
- Library doesn't restrict what apps can do

### 2. Maintainability
- Update platform code once in library
- Apps don't need to worry about Android vs iOS differences

### 3. Testability
- Library provides consistent interface
- Apps can focus on testing business logic

### 4. Scalability
- Add new entities/DAOs without changing library
- Library changes don't break existing apps (if API stable)

---

## 📖 Answer to Your Question

**Q: "Where should database manipulation methods (insert, delete, getValues) be placed?"**

**A: In the CONSUMER APP, not in the library.**

**Why?**
- These methods are specific to YOUR data model (UserEntity, ProductEntity, etc.)
- Different apps need different operations
- Library provides the infrastructure to make these methods work
- You define what operations your app needs

**Example:**
```kotlin
// Consumer App - YOUR CODE
@Dao
interface UserDao {
    @Insert                                    // ← You define this
    suspend fun insert(user: UserEntity)      // ← You define this

    @Delete                                    // ← You define this
    suspend fun delete(user: UserEntity)      // ← You define this

    @Query("SELECT * FROM users")             // ← You define this
    fun getAll(): Flow<List<UserEntity>>      // ← You define this
}

// Library - INFRASTRUCTURE CODE
abstract class AndroidDatabaseFactory<T> {
    protected fun buildDatabase(...): T {     // ← Library provides this
        return Room.databaseBuilder(...)      // ← Library provides this
    }
}
```

---

**Remember:** The library is a foundation, not the complete house. You build your house (entities, DAOs, queries) on top of the foundation (library's factories and config).

---

Last updated: March 2026