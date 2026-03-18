# KMP Room Core Library

A Kotlin Multiplatform library providing infrastructure for Room Database on Android and iOS platforms.

## Overview

This library provides platform abstractions and utilities for implementing Room Database in KMP projects. It handles the platform-specific initialization while allowing apps to define their own entities, DAOs, and database schemas.

## Features

- ✅ Platform abstractions for Android and iOS
- ✅ Base factory classes for easy database creation
- ✅ Migration utilities and DSL
- ✅ Flow extensions for common operations
- ✅ Database utilities (exists, delete, path)
- ✅ Consistent configuration across platforms

## Requirements

- Kotlin 2.1.20+
- Room 2.7.0+
- KSP 2.1.20-1.0.31
- Android minSdk 24+
- iOS 13+

## Installation

Add to `settings.gradle.kts`:
```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://maven.pkg.github.com/neeraj-brightly12/KMPDatabasePOC")
            credentials {
                username = System.getenv("GITHUB_ACTOR") ?: findProperty("gpr.user") as String?
                password = System.getenv("GITHUB_TOKEN") ?: findProperty("gpr.token") as String?
            }
        }
    }
}
```

Add dependency in your app's `build.gradle.kts`:
```kotlin
commonMain.dependencies {
    // Single dependency - Gradle automatically resolves the correct platform variant
    implementation("com.brightly:kmp-room-core:1.0.1")
}
```

**Note:** You'll need a GitHub Personal Access Token with `read:packages` permission.
See [PUBLISHING_QUICK_START.md](PUBLISHING_QUICK_START.md) for detailed setup.

### Important: KSP Configuration Required

**Your app still needs KSP** to process Room annotations:

```kotlin
plugins {
    id("com.google.devtools.ksp") version "2.1.20-1.0.31"
}

dependencies {
    add("kspAndroid", "androidx.room:room-compiler:2.7.0")
    add("kspIosArm64", "androidx.room:room-compiler:2.7.0")
    add("kspIosSimulatorArm64", "androidx.room:room-compiler:2.7.0")
}
```

## Publishing

To publish this library to your own GitHub Packages repository:

See detailed guides:
- **Quick Start**: [PUBLISHING_QUICK_START.md](PUBLISHING_QUICK_START.md) - 30-second setup
- **Full Guide**: [PUBLISHING.md](PUBLISHING.md) - Complete documentation with troubleshooting

## Usage

### 1. Define Your Entities (Common Code)

```kotlin
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val email: String
)
```

### 2. Define Your DAOs (Common Code)

```kotlin
@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM users")
    fun getUsers(): Flow<List<UserEntity>>

    @Query("DELETE FROM users WHERE id = :userId")
    suspend fun deleteUser(userId: Int)
}
```

### 3. Define Your Database (Common Code)

```kotlin
@Database(
    entities = [UserEntity::class],
    version = 1
)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase>
```

### 4. Create Common Factory Interface (Common Code)

```kotlin
expect class DatabaseFactory {
    fun createDatabase(): AppDatabase
}
```

### 5. Implement Android Factory (androidMain)

```kotlin
import com.brightly.kmp.room.core.android.AndroidDatabaseFactory
import com.brightly.kmp.room.core.DatabaseConfig

actual class DatabaseFactory(
    context: Context
) : AndroidDatabaseFactory<AppDatabase>(context) {

    actual fun createDatabase(): AppDatabase {
        return createDatabase(
            name = "app.db",
            migrations = emptyList()
        )
    }

    override fun createDatabase(
        name: String,
        migrations: List<Migration>
    ): AppDatabase {
        return buildDatabase(
            AppDatabase::class.java,
            DatabaseConfig(
                name = name,
                version = 1,
                enableLogging = BuildConfig.DEBUG,
                migrations = migrations
            )
        )
    }
}
```

### 6. Implement iOS Factory (iosMain)

```kotlin
import com.brightly.kmp.room.core.ios.IosDatabaseFactory
import com.brightly.kmp.room.core.DatabaseConfig

actual class DatabaseFactory : IosDatabaseFactory<AppDatabase>() {

    actual fun createDatabase(): AppDatabase {
        return createDatabase(
            name = "app.db",
            migrations = emptyList()
        )
    }

    override fun createDatabase(
        name: String,
        migrations: List<Migration>
    ): AppDatabase {
        return buildDatabase(
            DatabaseConfig(
                name = name,
                version = 1,
                migrations = migrations
            )
        )
    }
}
```

## Advanced Usage

### Using Migrations

```kotlin
import com.brightly.kmp.room.core.util.migration

val migration1to2 = migration(1, 2) { database ->
    database.execSQL("ALTER TABLE users ADD COLUMN age INTEGER DEFAULT 0 NOT NULL")
}

val migration2to3 = migration(2, 3) { database ->
    database.execSQL("CREATE TABLE posts (id INTEGER PRIMARY KEY, title TEXT NOT NULL)")
}

// Use in factory
actual class DatabaseFactory(context: Context) :
    AndroidDatabaseFactory<AppDatabase>(context) {

    override fun createDatabase(name: String, migrations: List<Migration>): AppDatabase {
        return buildDatabase(
            AppDatabase::class.java,
            DatabaseConfig(
                name = name,
                version = 3,
                migrations = listOf(migration1to2, migration2to3)
            )
        )
    }
}
```

### Using Flow Extensions

```kotlin
import com.brightly.kmp.room.core.extensions.*

class UserRepository(database: AppDatabase) {
    private val dao = database.userDao()

    fun getUsers(): Flow<List<UserEntity>> = dao.getUsers()

    fun getUserCount(): Flow<Int> = dao.getUsers().mapToSize()

    fun getNonEmptyUsers(): Flow<List<UserEntity>> = dao.getUsers().filterNotEmpty()

    fun getFirstUser(): Flow<UserEntity?> = dao.getUsers().mapToFirstOrNull()
}
```

### Using Database Utils

```kotlin
import com.brightly.kmp.room.core.util.DatabaseUtils

// Check if database exists
if (DatabaseUtils.databaseExists("app.db")) {
    println("Database found")
}

// Get database path
val path = DatabaseUtils.getDatabasePath("app.db")
println("Database located at: $path")

// Delete database
DatabaseUtils.deleteDatabase("app.db")
```

## Architecture Benefits

### What the Library Provides:
- ✅ Platform-specific initialization (Context for Android, Documents directory for iOS)
- ✅ BundledSQLiteDriver configuration for iOS
- ✅ Migration helpers and utilities
- ✅ Common configuration patterns
- ✅ Extension functions for Flow

### What Your App Provides:
- ✅ Entities (data models)
- ✅ DAOs (data access)
- ✅ Database schema
- ✅ Business logic (Repository, ViewModel)
- ✅ Specific migrations

## Example Project Structure

```
YourApp/
├── kmp-room-core/              # This library
│   ├── Platform abstractions
│   └── Utilities
└── composeApp/                  # Your app
    ├── Entity definitions
    ├── DAO interfaces
    ├── Database definition
    ├── Concrete factory implementations
    ├── Repository layer
    └── ViewModel layer
```

## Benefits

1. **Reusability**: Use the same infrastructure across multiple KMP apps
2. **Consistency**: Same patterns across Android and iOS
3. **Maintainability**: Update platform code in one place
4. **Flexibility**: Each app defines its own schema
5. **Type Safety**: Compile-time checks for database operations
6. **Testability**: Easy to test with consistent patterns

## Best Practices

1. **Always use applicationContext on Android** to avoid memory leaks
2. **Enable logging in debug builds** for easier debugging
3. **Plan migrations carefully** before releasing schema changes
4. **Use Flow for reactive queries** for automatic UI updates
5. **Use suspend functions for write operations** for proper coroutine handling
6. **Test migrations** before deploying to production

## Platform Notes

### Android
- Database stored in `/data/data/{package}/databases/`
- Uses Android's built-in SQLite
- Requires Context (use applicationContext)

### iOS
- Database stored in Documents directory
- Requires BundledSQLiteDriver
- No Context needed

## Troubleshooting

### Issue: Database not created
**Solution**: Ensure KSP is properly configured for all targets

### Issue: Context memory leak (Android)
**Solution**: Use applicationContext, not activity context

### Issue: iOS database not found
**Solution**: Library automatically handles path, check if BundledSQLiteDriver is included

### Issue: Migrations not running
**Solution**: Increment version number and ensure migrations are added in order

## Version Compatibility

| Library Version | Kotlin | Room | KSP |
|----------------|--------|------|-----|
| 1.0.0 | 2.1.20 | 2.7.0 | 2.1.20-1.0.31 |

## License

Copyright © 2026 Brightly

## Support

For issues and questions, refer to the project documentation or contact the development team.