# How to Apply This to ANY KMP Application

This guide explains how to use the prompt library and this project structure to implement Room Database in **any** Kotlin Multiplatform application.

---

## Table of Contents

1. [Overview](#overview)
2. [Prerequisites](#prerequisites)
3. [Decision Tree: Choose Your Path](#decision-tree-choose-your-path)
4. [Path A: Use Published Library](#path-a-use-published-library)
5. [Path B: Create Your Own Library](#path-b-create-your-own-library)
6. [Path C: Integrate Directly (No Library)](#path-c-integrate-directly-no-library)
7. [Customization Guide](#customization-guide)
8. [Migration Guide](#migration-guide)
9. [Troubleshooting](#troubleshooting)

---

## Overview

### What This Project Provides

This project demonstrates **three approaches** to implement Room Database in KMP:

1. **Shared Library Approach**: Reusable library (`kmp-room-core`)
2. **Custom Library Approach**: Create your own library with customizations
3. **Direct Integration**: Integrate Room directly into your app

### What You'll Learn

- ✅ How to structure Room Database in KMP
- ✅ How to handle platform-specific initialization
- ✅ How to publish and consume KMP libraries
- ✅ How to configure KSP for Room
- ✅ How to implement clean architecture with Room

---

## Prerequisites

### Required Knowledge
- Kotlin Multiplatform basics
- Gradle build configuration
- Git and GitHub
- Android and iOS development basics

### Required Tools
- Android Studio or IntelliJ IDEA
- Xcode (for iOS builds)
- Git
- GitHub account (for publishing)
- JDK 11 or higher

### Existing Project Requirements
- ✅ Working KMP project with Android and iOS targets
- ✅ Compose Multiplatform (recommended) or platform-specific UI
- ✅ Gradle 8.0+ with version catalog

---

## Decision Tree: Choose Your Path

```
START: Do you need Room Database in your KMP app?
  │
  ├─ YES → Do you want to reuse across multiple apps?
  │         │
  │         ├─ YES → Do you want customizations?
  │         │        │
  │         │        ├─ YES → Path B: Create Custom Library
  │         │        └─ NO  → Path A: Use Published Library
  │         │
  │         └─ NO  → Is your team experienced with KMP?
  │                  │
  │                  ├─ YES → Path C: Direct Integration
  │                  └─ NO  → Path A: Use Published Library
  │
  └─ NO → This guide is not needed
```

### Quick Decision Guide

**Choose Path A (Use Published Library) if:**
- ⚡ You want the fastest setup
- 📦 You don't need customizations
- 🔄 You want easy updates
- 👥 Multiple apps will use the same pattern

**Choose Path B (Create Custom Library) if:**
- 🎨 You need custom features
- 🏢 Your company needs internal libraries
- 🔐 You need private modifications
- 📚 You'll maintain your own version

**Choose Path C (Direct Integration) if:**
- 🎯 Single app usage
- 💪 Team is experienced with KMP
- ⚙️ Need maximum control
- 🚀 Prototype or POC phase

---

## Path A: Use Published Library

**Recommended for: 80% of projects**

### Step 1: Add Repository Configuration

**File**: `settings.gradle.kts`

```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()

        // Add GitHub Packages repository
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/neeraj-brightly12/KMPDatabasePOC")
            credentials {
                username = System.getenv("GITHUB_ACTOR") ?: findProperty("gpr.user") as String?
                password = System.getenv("GITHUB_TOKEN") ?: findProperty("gpr.token") as String?
            }
        }
    }
}
```

### Step 2: Configure Credentials

**File**: `~/.gradle/gradle.properties`

```properties
# GitHub Packages credentials
gpr.user=YOUR_GITHUB_USERNAME
gpr.token=YOUR_GITHUB_TOKEN
```

**How to create token:**
1. Go to https://github.com/settings/tokens/new
2. Select scopes: `read:packages`
3. Generate token
4. Copy and save in gradle.properties

### Step 3: Add Dependencies

**File**: `app/build.gradle.kts` (your app module)

```kotlin
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication) // or androidLibrary
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)

    // ⚠️ CRITICAL: KSP is REQUIRED
    alias(libs.plugins.ksp)
}

kotlin {
    androidTarget()

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        // ... iOS configuration
    }

    sourceSets {
        commonMain.dependencies {
            // Published library
            implementation("com.brightly:kmp-room-core:1.0.2")

            // Room runtime (if not transitive)
            implementation("androidx.room:room-runtime:2.7.0")
        }
    }
}

// ⚠️ CRITICAL: KSP dependencies for YOUR app's entities
dependencies {
    add("kspAndroid", "androidx.room:room-compiler:2.7.0")
    add("kspIosArm64", "androidx.room:room-compiler:2.7.0")
    add("kspIosSimulatorArm64", "androidx.room:room-compiler:2.7.0")
}

// Room schema export (optional but recommended)
room {
    schemaDirectory("$projectDir/schemas")
}
```

### Step 4: Define Your Data Model

**File**: `commonMain/kotlin/your/package/data/entity/YourEntity.kt`

```kotlin
package your.package.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "your_table_name")
data class YourEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val field1: String,
    val field2: String,
    val createdAt: Long = System.currentTimeMillis()
)
```

### Step 5: Define Your DAO

**File**: `commonMain/kotlin/your/package/data/dao/YourDao.kt`

```kotlin
package your.package.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Delete
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import your.package.data.entity.YourEntity

@Dao
interface YourDao {
    @Insert
    suspend fun insert(entity: YourEntity)

    @Update
    suspend fun update(entity: YourEntity)

    @Delete
    suspend fun delete(entity: YourEntity)

    @Query("SELECT * FROM your_table_name ORDER BY createdAt DESC")
    fun getAll(): Flow<List<YourEntity>>

    @Query("SELECT * FROM your_table_name WHERE id = :id")
    suspend fun getById(id: Int): YourEntity?

    @Query("DELETE FROM your_table_name")
    suspend fun deleteAll()
}
```

### Step 6: Define Your Database

**File**: `commonMain/kotlin/your/package/data/database/AppDatabase.kt`

```kotlin
package your.package.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.ConstructedBy
import androidx.room.RoomDatabaseConstructor
import your.package.data.dao.YourDao
import your.package.data.entity.YourEntity

@Database(
    entities = [
        YourEntity::class,
        // Add more entities here
    ],
    version = 1,
    exportSchema = true
)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun yourDao(): YourDao
    // Add more DAO getters here
}

// Required for Room KSP
@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase>
```

### Step 7: Create Common Factory Interface

**File**: `commonMain/kotlin/your/package/data/database/DatabaseFactory.kt`

```kotlin
package your.package.data.database

expect class DatabaseFactory {
    fun createDatabase(): AppDatabase
}
```

### Step 8: Implement Android Factory

**File**: `androidMain/kotlin/your/package/data/database/DatabaseFactory.android.kt`

```kotlin
package your.package.data.database

import android.content.Context
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.brightly.kmp.room.core.android.AndroidDatabaseFactory
import com.brightly.kmp.room.core.DatabaseConfig
import androidx.room.migration.Migration

actual class DatabaseFactory(
    private val context: Context
) : AndroidDatabaseFactory<AppDatabase>(context) {

    actual fun createDatabase(): AppDatabase {
        return createDatabase(
            name = "app_database.db",
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
                enableLogging = true, // Set to BuildConfig.DEBUG in production
                migrations = migrations
            )
        )
    }
}
```

### Step 9: Implement iOS Factory

**File**: `iosMain/kotlin/your/package/data/database/DatabaseFactory.ios.kt`

```kotlin
package your.package.data.database

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.brightly.kmp.room.core.ios.IosDatabaseFactory
import com.brightly.kmp.room.core.DatabaseConfig
import androidx.room.migration.Migration

actual class DatabaseFactory : IosDatabaseFactory<AppDatabase>() {

    actual fun createDatabase(): AppDatabase {
        return createDatabase(
            name = "app_database.db",
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

### Step 10: Create Repository

**File**: `commonMain/kotlin/your/package/data/repository/YourRepository.kt`

```kotlin
package your.package.data.repository

import kotlinx.coroutines.flow.Flow
import your.package.data.database.AppDatabase
import your.package.data.entity.YourEntity

class YourRepository(
    database: AppDatabase
) {
    private val dao = database.yourDao()

    fun getAll(): Flow<List<YourEntity>> = dao.getAll()

    suspend fun getById(id: Int): YourEntity? = dao.getById(id)

    suspend fun insert(entity: YourEntity) = dao.insert(entity)

    suspend fun update(entity: YourEntity) = dao.update(entity)

    suspend fun delete(entity: YourEntity) = dao.delete(entity)

    suspend fun deleteAll() = dao.deleteAll()
}
```

### Step 11: Initialize in Your App

**Android**: `androidMain/kotlin/your/package/MainActivity.kt`

```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create database
        val databaseFactory = DatabaseFactory(applicationContext)
        val database = databaseFactory.createDatabase()

        // Create repository
        val repository = YourRepository(database)

        setContent {
            YourApp(repository = repository)
        }
    }
}
```

**iOS**: `iosMain/kotlin/your/package/MainViewController.kt`

```kotlin
fun MainViewController(): UIViewController {
    val databaseFactory = DatabaseFactory()
    val database = databaseFactory.createDatabase()
    val repository = YourRepository(database)

    return ComposeUIViewController {
        YourApp(repository = repository)
    }
}
```

### Step 12: Build and Test

```bash
# Clean
./gradlew clean

# Build Android
./gradlew :app:assembleDebug

# Build iOS
./gradlew :app:compileKotlinIosSimulatorArm64

# Run tests
./gradlew :app:testDebugUnitTest
```

### Path A Complete! 🎉

Your app now uses the published `kmp-room-core` library with Room Database on Android and iOS.

---

## Path B: Create Your Own Library

**Recommended for: Companies, custom requirements, multiple internal apps**

### When to Choose This Path

- 🏢 Need internal/private library
- 🎨 Need custom features not in public library
- 🔐 Security requirements
- 📚 Maintaining multiple apps with shared code

### Step 1: Fork or Clone This Repository

```bash
git clone https://github.com/neeraj-brightly12/KMPDatabasePOC.git
cd KMPDatabasePOC
```

### Step 2: Customize Library Module

**Rename and rebrand:**

1. Rename module: `kmp-room-core` → `your-company-room-core`
2. Update package: `com.brightly.kmp.room.core` → `com.yourcompany.room.core`
3. Update group ID in `build.gradle.kts`:
   ```kotlin
   group = "com.yourcompany"
   version = "1.0.0"
   ```

### Step 3: Add Your Custom Features

Use prompts from PROMPT_LIBRARY.md to add:
- Database encryption (Prompt 36)
- Multi-module support (Prompt 37)
- Backup/restore (Prompt 38)
- Database inspector (Prompt 39)
- Custom utilities specific to your needs

### Step 4: Configure Your GitHub Repository

```kotlin
// kmp-room-core/build.gradle.kts
publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/YOUR_USERNAME/YOUR_REPO")
            credentials {
                username = System.getenv("GITHUB_ACTOR") ?: findProperty("gpr.user") as String?
                password = System.getenv("GITHUB_TOKEN") ?: findProperty("gpr.token") as String?
            }
        }
    }
}
```

### Step 5: Publish Your Library

```bash
# Clean
./gradlew :your-company-room-core:clean

# Publish
./gradlew :your-company-room-core:publish

# Tag release
git tag v1.0.0
git push origin v1.0.0
```

### Step 6: Use in Your Apps

Follow Path A steps, but use your library:

```kotlin
implementation("com.yourcompany:your-company-room-core:1.0.0")
```

### Path B Complete! 🎉

You now have your own customized Room library for internal use.

---

## Path C: Integrate Directly (No Library)

**Recommended for: Single app, prototypes, maximum control**

### When to Choose This Path

- 🎯 Single app project
- 🚀 Quick prototype or POC
- 💪 Experienced team wants full control
- ⚙️ Highly customized requirements

### Step 1: Add Room Dependencies

**File**: `app/build.gradle.kts`

```kotlin
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.ksp)
}

kotlin {
    androidTarget()

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        // iOS config
    }

    sourceSets {
        commonMain.dependencies {
            implementation("androidx.room:room-runtime:2.7.0")
            implementation("androidx.sqlite:sqlite-bundled:2.4.0")
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
        }

        androidMain.dependencies {
            implementation("androidx.room:room-runtime:2.7.0")
        }
    }
}

dependencies {
    add("kspAndroid", "androidx.room:room-compiler:2.7.0")
    add("kspIosArm64", "androidx.room:room-compiler:2.7.0")
    add("kspIosSimulatorArm64", "androidx.room:room-compiler:2.7.0")
}

room {
    schemaDirectory("$projectDir/schemas")
}
```

### Step 2: Copy Core Classes from This Project

Copy these files into your project:

**From `kmp-room-core/src/commonMain/kotlin/`:**
- `DatabaseConfig.kt`
- `util/DatabaseUtils.kt` (expect/actual)
- `util/MigrationBuilder.kt`
- `extensions/FlowExtensions.kt`

**From `kmp-room-core/src/androidMain/kotlin/`:**
- `android/AndroidDatabaseFactory.kt`
- `util/DatabaseUtils.android.kt`

**From `kmp-room-core/src/iosMain/kotlin/`:**
- `ios/IosDatabaseFactory.kt`
- `util/DatabaseUtils.ios.kt`

### Step 3: Customize to Your Needs

Modify copied classes:
- Remove features you don't need
- Add app-specific logic
- Integrate with your DI framework
- Add custom utilities

### Step 4: Follow Steps 4-12 from Path A

Define entities, DAOs, database, repository, and initialize.

### Path C Complete! 🎉

You have Room Database integrated directly without external library dependency.

---

## Customization Guide

### Common Customizations

#### 1. Change Database Name

```kotlin
// In your DatabaseFactory
createDatabase(
    name = "my_custom_name.db", // Change here
    migrations = emptyList()
)
```

#### 2. Add Encryption

```kotlin
// Add SQLCipher dependency
commonMain.dependencies {
    implementation("net.zetetic:android-database-sqlcipher:4.5.4")
}

// Modify AndroidDatabaseFactory
Room.databaseBuilder(context, AppDatabase::class.java, name)
    .openHelperFactory(SupportFactory(SQLiteDatabase.getBytes(passphrase)))
    .build()
```

#### 3. Add Custom Migrations

```kotlin
import com.brightly.kmp.room.core.util.migration

val migration1to2 = migration(1, 2) { database ->
    database.execSQL("ALTER TABLE users ADD COLUMN age INTEGER DEFAULT 0 NOT NULL")
}

val migration2to3 = migration(2, 3) { database ->
    database.execSQL("CREATE INDEX idx_users_email ON users(email)")
}

// Use in factory
createDatabase(
    name = "app.db",
    migrations = listOf(migration1to2, migration2to3)
)
```

#### 4. Add Multiple Entities

```kotlin
@Database(
    entities = [
        UserEntity::class,
        PostEntity::class,
        CommentEntity::class,
        // Add all your entities
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun postDao(): PostDao
    abstract fun commentDao(): CommentDao
}
```

#### 5. Add Relations

```kotlin
data class UserWithPosts(
    @Embedded val user: UserEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "userId"
    )
    val posts: List<PostEntity>
)

@Dao
interface UserDao {
    @Transaction
    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserWithPosts(userId: Int): UserWithPosts
}
```

#### 6. Add TypeConverters

```kotlin
class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}

@Database(
    entities = [YourEntity::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    // ...
}
```

#### 7. Add FTS (Full-Text Search)

```kotlin
@Entity(tableName = "posts_fts")
@Fts4(contentEntity = PostEntity::class)
data class PostFts(
    val title: String,
    val content: String
)

@Dao
interface PostDao {
    @Query("SELECT * FROM posts_fts WHERE posts_fts MATCH :query")
    fun searchPosts(query: String): Flow<List<PostEntity>>
}
```

---

## Migration Guide

### Migrating from SQLDelight

**Step 1: Analyze Current Schema**
- Export your SQLDelight schema
- Map tables to entities
- Map queries to DAOs

**Step 2: Create Equivalent Entities**
```kotlin
// SQLDelight
CREATE TABLE user (
    id INTEGER PRIMARY KEY,
    name TEXT NOT NULL
);

// Room
@Entity(tableName = "user")
data class User(
    @PrimaryKey val id: Int,
    val name: String
)
```

**Step 3: Create DAOs**
```kotlin
// SQLDelight
selectAll: SELECT * FROM user;

// Room
@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun selectAll(): Flow<List<User>>
}
```

**Step 4: Test Thoroughly**
- Unit tests for DAOs
- Integration tests for database
- Migration tests if upgrading existing app

### Migrating from Realm

Similar process:
1. Map RealmObject to @Entity
2. Map queries to @Query annotations
3. Handle relationships with @Relation
4. Test thoroughly

---

## Troubleshooting

### Issue: Could not find library

**Solution:**
```bash
# 1. Verify credentials
cat ~/.gradle/gradle.properties

# 2. Test token
curl -H "Authorization: token YOUR_TOKEN" \
  https://maven.pkg.github.com/USER/REPO/

# 3. Refresh dependencies
./gradlew --refresh-dependencies
```

### Issue: KSP not generating code

**Solution:**
```bash
# 1. Check KSP is in plugins
plugins {
    alias(libs.plugins.ksp) // Must be present
}

# 2. Check KSP dependencies
dependencies {
    add("kspAndroid", "androidx.room:room-compiler:2.7.0")
    add("kspIosArm64", "androidx.room:room-compiler:2.7.0")
    add("kspIosSimulatorArm64", "androidx.room:room-compiler:2.7.0")
}

# 3. Clean and rebuild
./gradlew clean
./gradlew :app:kspDebugKotlinAndroid
```

### Issue: Database not created on iOS

**Solution:**
```kotlin
// Ensure BundledSQLiteDriver is used
.setDriver(BundledSQLiteDriver())

// Check database path
import platform.Foundation.NSHomeDirectory
val dbPath = "${NSHomeDirectory()}/Documents/$name"
println("Database path: $dbPath")
```

### Issue: Context memory leak on Android

**Solution:**
```kotlin
// Always use applicationContext
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ Correct
        val factory = DatabaseFactory(applicationContext)

        // ❌ Wrong
        // val factory = DatabaseFactory(this)
    }
}
```

### Issue: Build fails with "metadata" errors

**This is NORMAL with KMP + KSP.**

**Solution: Use platform-specific builds**
```bash
# Don't use:
./gradlew build  # ❌ May fail

# Use instead:
./gradlew :app:assembleDebug  # ✅ Android
./gradlew :app:compileKotlinIosSimulatorArm64  # ✅ iOS
```

---

## Best Practices

### 1. Project Structure

```
your-app/
├── commonMain/
│   ├── data/
│   │   ├── entity/          # All @Entity classes
│   │   ├── dao/             # All @Dao interfaces
│   │   ├── database/        # Database and factory
│   │   └── repository/      # Repository layer
│   ├── domain/              # Business logic
│   └── presentation/        # ViewModels and UI
├── androidMain/
│   └── data/database/       # Android factory
└── iosMain/
    └── data/database/       # iOS factory
```

### 2. Naming Conventions

- Entities: `UserEntity`, `PostEntity`
- DAOs: `UserDao`, `PostDao`
- Database: `AppDatabase`
- Repository: `UserRepository`, `PostRepository`
- ViewModel: `UserViewModel`, `PostViewModel`

### 3. Database Design

- Use appropriate primary keys
- Add indexes for frequently queried fields
- Use foreign keys for relationships
- Plan migrations before releasing

### 4. Error Handling

```kotlin
class YourRepository(database: AppDatabase) {
    suspend fun insert(entity: YourEntity): Result<Unit> = runCatching {
        dao.insert(entity)
    }

    fun getAll(): Flow<Result<List<YourEntity>>> = flow {
        dao.getAll().collect { entities ->
            emit(Result.success(entities))
        }
    }.catch { e ->
        emit(Result.failure(e))
    }
}
```

### 5. Testing

```kotlin
class UserRepositoryTest {
    private lateinit var database: AppDatabase
    private lateinit var repository: UserRepository

    @Before
    fun setup() {
        // Use in-memory database for tests
        database = Room.inMemoryDatabaseBuilder(
            context,
            AppDatabase::class.java
        ).build()

        repository = UserRepository(database)
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun testInsertAndRetrieve() = runTest {
        val user = UserEntity(name = "Test", email = "test@example.com")
        repository.insert(user)

        val users = repository.getAll().first()
        assertEquals(1, users.size)
        assertEquals("Test", users[0].name)
    }
}
```

---

## Quick Command Reference

### Build Commands
```bash
# Clean
./gradlew clean

# Android
./gradlew :app:assembleDebug
./gradlew :app:assembleRelease

# iOS
./gradlew :app:compileKotlinIosSimulatorArm64
./gradlew :app:compileKotlinIosArm64

# Tests
./gradlew :app:testDebugUnitTest
./gradlew :app:iosSimulatorArm64Test
```

### Publishing Commands (if creating library)
```bash
# Publish
./gradlew :your-library:clean
./gradlew :your-library:publish

# Tag
git tag v1.0.0
git push origin v1.0.0
```

### Dependency Management
```bash
# Refresh dependencies
./gradlew --refresh-dependencies

# Check for updates
./gradlew dependencyUpdates

# View dependency tree
./gradlew :app:dependencies
```

---

## Success Checklist

### Before You Start
- [ ] Choose Path A, B, or C based on your needs
- [ ] Set up GitHub token (if using library)
- [ ] Verify KMP project is working
- [ ] Read relevant documentation

### During Implementation
- [ ] Added Room dependencies
- [ ] Configured KSP for all targets
- [ ] Created entities with proper annotations
- [ ] Created DAOs with queries
- [ ] Defined database with version
- [ ] Implemented platform factories
- [ ] Created repository layer
- [ ] Tested on Android
- [ ] Tested on iOS

### After Implementation
- [ ] Database operations work
- [ ] Data persists across restarts
- [ ] No memory leaks (use profiler)
- [ ] Error handling implemented
- [ ] Tests written and passing
- [ ] Documentation updated
- [ ] Code reviewed
- [ ] Deployed to staging

---

## Next Steps

After completing this guide:

1. **Read Advanced Documentation**
   - ARCHITECTURE_DOCUMENT.md
   - CODE_FLOW_EXPLANATION.md
   - LIBRARY_ARCHITECTURE_GUIDE.md

2. **Implement Advanced Features**
   - Database encryption
   - Backup/restore
   - Multi-module support
   - Database inspector

3. **Set Up CI/CD**
   - GitHub Actions for builds
   - Automated testing
   - Library publishing automation

4. **Monitor and Optimize**
   - Database performance
   - Query optimization
   - Index strategy
   - Memory usage

---

## Support and Resources

### Documentation in This Project
- `README_START_HERE.md` - Documentation index
- `COMPLETE_GUIDE.md` - Comprehensive guide
- `PROMPT_LIBRARY.md` - All prompts used
- `QUICK_REFERENCE.md` - Command reference

### External Resources
- [Room Documentation](https://developer.android.com/training/data-storage/room)
- [KMP Documentation](https://kotlinlang.org/docs/multiplatform.html)
- [KSP Documentation](https://kotlinlang.org/docs/ksp-overview.html)
- [Compose Multiplatform](https://www.jetbrains.com/compose-multiplatform/)

### Community
- [Kotlin Slack](https://surveys.jetbrains.com/s3/kotlin-slack-sign-up)
- [Stack Overflow](https://stackoverflow.com/questions/tagged/kotlin-multiplatform)
- [GitHub Discussions](https://github.com/JetBrains/compose-multiplatform/discussions)

---

## Conclusion

You now have three clear paths to implement Room Database in any KMP application:

- **Path A**: Fastest, use published library
- **Path B**: Most flexible, create custom library
- **Path C**: Most control, direct integration

Choose the path that best fits your project needs, follow the steps, and you'll have a working Room Database implementation in your KMP app.

**Remember**: The key to success is proper KSP configuration and understanding the platform-specific requirements.

Happy coding! 🚀

---

**Document Version**: 1.0.0
**Last Updated**: 2026-03-20
**Author**: Brightly Development Team