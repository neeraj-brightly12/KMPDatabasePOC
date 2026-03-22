# Room Database Library Creation Guide - KMP

## Table of Contents
1. [Should You Create a Library?](#should-you-create-a-library)
2. [Library Architecture Analysis](#library-architecture-analysis)
3. [What to Include in Library](#what-to-include-in-library)
4. [What to Keep in Each App](#what-to-keep-in-each-app)
5. [Library Structure](#library-structure)
6. [Step-by-Step Implementation](#step-by-step-implementation)
7. [Usage Examples](#usage-examples)
8. [Pros & Cons](#pros--cons)
9. [Publishing & Distribution](#publishing--distribution)

---

## Should You Create a Library?

### 🎯 RECOMMENDATION: **YES - Create an Infrastructure Library**

**BUT** - Create a **Database Infrastructure Library**, NOT a complete database with specific entities.

### Why Infrastructure Library?

```
❌ BAD: Complete Database Library
   ├─ AppDatabase (with specific entities)
   ├─ UserEntity, ProductEntity, etc.
   ├─ UserDao, ProductDao, etc.
   └─ Hard-coded business logic

   Problem: Every app has different entities!

✅ GOOD: Database Infrastructure Library
   ├─ Platform abstractions (expect/actual)
   ├─ Database factory utilities
   ├─ Common configurations
   ├─ Migration helpers
   ├─ Testing utilities
   └─ Apps provide their own entities/DAOs
```

---

## Library Architecture Analysis

### Current Code Analysis

**What's Generic (Library Material):**
```kotlin
✅ DatabaseFactory pattern (expect/actual)
✅ Platform-specific initialization
   - Android: Context-based
   - iOS: BundledSQLiteDriver setup
✅ Database configuration patterns
✅ Common utilities
```

**What's App-Specific (Should NOT be in Library):**
```kotlin
❌ UserEntity - Specific to this app
❌ UserDao - App's data access needs
❌ AppDatabase - App's schema
❌ UserRepository - Business logic
❌ UserViewModel - Presentation logic
```

### Recommended Approach

```
┌─────────────────────────────────────────────────────────┐
│          Your Library: "kmp-room-core"                  │
├─────────────────────────────────────────────────────────┤
│  - Platform abstractions                                │
│  - Database factory interfaces                          │
│  - Configuration builders                               │
│  - Migration utilities                                  │
│  - Testing helpers                                      │
└─────────────────────────────────────────────────────────┘
                          ▲
                          │ depends on
                          │
┌─────────────────────────────────────────────────────────┐
│              Consumer App                               │
├─────────────────────────────────────────────────────────┤
│  - App-specific entities (UserEntity)                  │
│  - App-specific DAOs (UserDao)                         │
│  - App database (AppDatabase)                          │
│  - Uses library's infrastructure                       │
└─────────────────────────────────────────────────────────┘
```

---

## What to Include in Library

### 1. Core Abstractions

```kotlin
// Package: com.yourcompany.kmp.room.core

/**
 * Platform-agnostic database factory interface
 */
interface KmpDatabaseFactory<T : RoomDatabase> {
    fun createDatabase(
        name: String,
        migrations: List<Migration> = emptyList()
    ): T
}

/**
 * Database configuration
 */
data class DatabaseConfig(
    val name: String,
    val version: Int,
    val enableLogging: Boolean = false,
    val migrations: List<Migration> = emptyList()
)
```

### 2. Platform-Specific Base Factories

```kotlin
// Android Implementation
// Package: com.yourcompany.kmp.room.core.android

abstract class AndroidDatabaseFactory<T : RoomDatabase>(
    protected val context: Context
) : KmpDatabaseFactory<T> {

    protected fun <T : RoomDatabase> buildDatabase(
        databaseClass: Class<T>,
        config: DatabaseConfig
    ): T {
        return Room.databaseBuilder(
            context,
            databaseClass,
            config.name
        ).apply {
            if (config.enableLogging) {
                setQueryCallback({ query, _ ->
                    println("Room Query: $query")
                }, Dispatchers.IO)
            }
            config.migrations.forEach { addMigrations(it) }
        }.build()
    }
}

// iOS Implementation
// Package: com.yourcompany.kmp.room.core.ios

abstract class IosDatabaseFactory<T : RoomDatabase> : KmpDatabaseFactory<T> {

    @OptIn(ExperimentalForeignApi::class)
    protected inline fun <reified T : RoomDatabase> buildDatabase(
        config: DatabaseConfig
    ): T {
        val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null
        )
        val dbFile = requireNotNull(documentDirectory?.path) + "/${config.name}"

        return Room.databaseBuilder<T>(name = dbFile)
            .setDriver(BundledSQLiteDriver())
            .apply {
                config.migrations.forEach { addMigrations(it) }
            }
            .build()
    }
}
```

### 3. Common Utilities

```kotlin
// Package: com.yourcompany.kmp.room.core.util

/**
 * Database utilities
 */
object DatabaseUtils {

    /**
     * Check if database exists
     */
    expect fun databaseExists(name: String): Boolean

    /**
     * Delete database
     */
    expect fun deleteDatabase(name: String): Boolean

    /**
     * Get database path
     */
    expect fun getDatabasePath(name: String): String
}

/**
 * Migration builder DSL
 */
fun migration(from: Int, to: Int, block: (SupportSQLiteDatabase) -> Unit): Migration {
    return object : Migration(from, to) {
        override fun migrate(database: SupportSQLiteDatabase) {
            block(database)
        }
    }
}
```

### 4. Testing Utilities

```kotlin
// Package: com.yourcompany.kmp.room.core.testing

/**
 * In-memory database factory for testing
 */
expect class TestDatabaseFactory<T : RoomDatabase>() {
    fun createInMemoryDatabase(databaseClass: KClass<T>): T
}

// Android implementation
actual class TestDatabaseFactory<T : RoomDatabase> actual constructor() {
    actual fun createInMemoryDatabase(databaseClass: KClass<T>): T {
        return Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            databaseClass.java
        ).build()
    }
}
```

### 5. Extension Functions

```kotlin
// Package: com.yourcompany.kmp.room.core.extensions

/**
 * Flow extensions for common operations
 */
fun <T> Flow<List<T>>.filterNotEmpty(): Flow<List<T>> =
    filter { it.isNotEmpty() }

fun <T> Flow<List<T>>.mapToSize(): Flow<Int> =
    map { it.size }

/**
 * Database transaction helper
 */
suspend fun <T : RoomDatabase, R> T.transaction(
    block: suspend () -> R
): R = withTransaction { block() }
```

---

## What to Keep in Each App

### Each App Implements

```kotlin
// 1. App-Specific Entities
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String
)

// 2. App-Specific DAOs
@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM users")
    fun getUsers(): Flow<List<UserEntity>>
}

// 3. App Database Definition
@Database(
    entities = [UserEntity::class],
    version = 1
)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}

// 4. Concrete Factory Implementation
// Android
class AppDatabaseFactory(context: Context) :
    AndroidDatabaseFactory<AppDatabase>(context) {

    override fun createDatabase(
        name: String,
        migrations: List<Migration>
    ): AppDatabase {
        return buildDatabase(
            AppDatabase::class.java,
            DatabaseConfig(
                name = name,
                version = 1,
                migrations = migrations
            )
        )
    }
}

// iOS
class AppDatabaseFactory : IosDatabaseFactory<AppDatabase>() {
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

// 5. Repository & ViewModel (app-specific business logic)
class UserRepository(database: AppDatabase) { ... }
class UserViewModel(repository: UserRepository) { ... }
```

---

## Library Structure

### Project Structure

```
kmp-room-core/
├── build.gradle.kts
├── src/
│   ├── commonMain/kotlin/com/yourcompany/kmp/room/core/
│   │   ├── KmpDatabaseFactory.kt          # Core interface
│   │   ├── DatabaseConfig.kt              # Configuration
│   │   ├── util/
│   │   │   ├── DatabaseUtils.kt           # Utilities
│   │   │   └── MigrationBuilder.kt        # Migration DSL
│   │   ├── extensions/
│   │   │   └── FlowExtensions.kt          # Flow helpers
│   │   └── testing/
│   │       └── TestDatabaseFactory.kt     # Test utilities
│   │
│   ├── androidMain/kotlin/com/yourcompany/kmp/room/core/
│   │   ├── android/
│   │   │   └── AndroidDatabaseFactory.kt  # Android base
│   │   ├── util/
│   │   │   └── DatabaseUtils.android.kt   # Android utils
│   │   └── testing/
│   │       └── TestDatabaseFactory.android.kt
│   │
│   └── iosMain/kotlin/com/yourcompany/kmp/room/core/
│       ├── ios/
│       │   └── IosDatabaseFactory.kt      # iOS base
│       ├── util/
│       │   └── DatabaseUtils.ios.kt       # iOS utils
│       └── testing/
│           └── TestDatabaseFactory.ios.kt
│
└── README.md
```

### build.gradle.kts for Library

```kotlin
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)  // Note: androidLibrary, not application
    id("maven-publish")  // For publishing
}

kotlin {
    androidTarget {
        publishLibraryVariants("release", "debug")
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "KmpRoomCore"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            // Only core Room dependencies
            api("androidx.room:room-runtime:2.7.0")
            api("androidx.sqlite:sqlite-bundled:2.4.0")
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
        }

        androidMain.dependencies {
            api("androidx.room:room-runtime:2.7.0")
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}

android {
    namespace = "com.yourcompany.kmp.room.core"
    compileSdk = 36

    defaultConfig {
        minSdk = 24
    }
}

// Publishing configuration
publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.yourcompany"
            artifactId = "kmp-room-core"
            version = "1.0.0"
        }
    }
}
```

---

## Step-by-Step Implementation

### Step 1: Create Library Module

```bash
# In your existing project
mkdir -p kmp-room-core/src/{commonMain,androidMain,iosMain}/kotlin
```

### Step 2: Add to settings.gradle.kts

```kotlin
include(":composeApp")
include(":kmp-room-core")  // Add library module
```

### Step 3: Move Core Code to Library

**Move these to library:**
1. Create `KmpDatabaseFactory` interface
2. Create `AndroidDatabaseFactory` base class
3. Create `IosDatabaseFactory` base class
4. Create utility classes

**Keep in app:**
1. UserEntity
2. UserDao
3. AppDatabase
4. Concrete DatabaseFactory implementations
5. Repository & ViewModel

### Step 4: Update App's build.gradle.kts

```kotlin
// composeApp/build.gradle.kts
dependencies {
    // Add library dependency
    commonMain.dependencies {
        implementation(project(":kmp-room-core"))
        // Room compiler still needed
        // ...
    }
}
```

### Step 5: Refactor App Code

```kotlin
// Android - Extend library's base
class AppDatabaseFactory(context: Context) :
    AndroidDatabaseFactory<AppDatabase>(context) {

    override fun createDatabase(
        name: String,
        migrations: List<Migration>
    ): AppDatabase {
        return buildDatabase(
            AppDatabase::class.java,
            DatabaseConfig(name = "app.db", version = 1)
        )
    }
}

// iOS - Extend library's base
class AppDatabaseFactory : IosDatabaseFactory<AppDatabase>() {
    override fun createDatabase(
        name: String,
        migrations: List<Migration>
    ): AppDatabase {
        return buildDatabase(
            DatabaseConfig(name = "app.db", version = 1)
        )
    }
}
```

---

## Usage Examples

### Example 1: Simple App

```kotlin
// Define your entities
@Entity(tableName = "products")
data class Product(
    @PrimaryKey val id: Int,
    val name: String,
    val price: Double
)

@Dao
interface ProductDao {
    @Query("SELECT * FROM products")
    fun getAll(): Flow<List<Product>>
}

@Database(entities = [Product::class], version = 1)
@ConstructedBy(ProductDatabaseConstructor::class)
abstract class ProductDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
}

// Android implementation
class ProductDatabaseFactory(context: Context) :
    AndroidDatabaseFactory<ProductDatabase>(context) {

    override fun createDatabase(
        name: String,
        migrations: List<Migration>
    ): ProductDatabase {
        return buildDatabase(
            ProductDatabase::class.java,
            DatabaseConfig(name = "products.db", version = 1)
        )
    }
}

// Usage
val factory = ProductDatabaseFactory(context)
val database = factory.createDatabase("products.db")
```

### Example 2: With Migrations

```kotlin
val migration1to2 = migration(1, 2) { db ->
    db.execSQL("ALTER TABLE products ADD COLUMN category TEXT")
}

val factory = ProductDatabaseFactory(context)
val database = factory.createDatabase(
    name = "products.db",
    migrations = listOf(migration1to2)
)
```

### Example 3: Multiple Apps Using Same Library

```kotlin
// App 1: E-commerce
@Database(entities = [Product::class, Order::class], version = 1)
abstract class EcommerceDatabase : RoomDatabase()

// App 2: Social Media
@Database(entities = [User::class, Post::class], version = 1)
abstract class SocialDatabase : RoomDatabase()

// Both use the same library infrastructure!
```

---

## Pros & Cons

### ✅ Advantages

1. **Code Reusability**
   - Write platform setup once
   - Reuse across multiple projects
   - Consistent patterns

2. **Easier Maintenance**
   - Update platform code in one place
   - Bug fixes benefit all apps
   - Centralized updates

3. **Standardization**
   - Same patterns across team/company
   - Easier onboarding
   - Best practices enforced

4. **Testing**
   - Shared testing utilities
   - Consistent test setup
   - Reusable test helpers

5. **Version Management**
   - Control Room version centrally
   - Easier to update dependencies
   - Compatibility tested once

6. **Documentation**
   - Single source of documentation
   - Examples in one place
   - Clear usage patterns

### ❌ Disadvantages

1. **Initial Setup Overhead**
   - Time to create library
   - Setup publishing
   - Documentation effort

2. **Version Management**
   - Apps might need different versions
   - Backward compatibility concerns
   - Breaking changes impact all apps

3. **Dependency Management**
   - Apps depend on library version
   - Must coordinate updates
   - Potential version conflicts

4. **Flexibility vs. Abstraction**
   - May not fit all use cases
   - Some apps need custom solutions
   - Balance generic vs. specific

5. **Learning Curve**
   - Team needs to understand library
   - Additional abstraction layer
   - Documentation maintenance

### 🎯 When to Use Library Approach

**Use Library If:**
- ✅ Multiple apps in your organization
- ✅ Consistent database patterns needed
- ✅ Team wants standardization
- ✅ Long-term maintenance planned
- ✅ Platform setup is complex

**Don't Use Library If:**
- ❌ Only one app
- ❌ Highly customized needs
- ❌ Rapid prototyping phase
- ❌ Different platforms/patterns per app
- ❌ Small team, simple needs

---

## Publishing & Distribution

### Option 1: Local Maven Repository

```kotlin
// build.gradle.kts (library)
publishing {
    repositories {
        maven {
            name = "LocalRepo"
            url = uri("file://${rootProject.buildDir}/local-repo")
        }
    }
}

// Publish
./gradlew publishToMavenLocal

// Consumer app
repositories {
    mavenLocal()
}

dependencies {
    implementation("com.yourcompany:kmp-room-core:1.0.0")
}
```

### Option 2: GitHub Packages

```kotlin
publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/yourorg/kmp-room-core")
            credentials {
                username = System.getenv("GITHUB_USERNAME")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}
```

### Option 3: JitPack (Easiest for Public)

```kotlin
// Just tag your repository
git tag v1.0.0
git push origin v1.0.0

// Consumer app
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation("com.github.yourorg:kmp-room-core:1.0.0")
}
```

### Option 4: Internal Maven/Artifactory

```kotlin
publishing {
    repositories {
        maven {
            url = uri("https://your-company.com/maven")
            credentials {
                username = project.findProperty("mavenUser") as String?
                password = project.findProperty("mavenPassword") as String?
            }
        }
    }
}
```

---

## Recommended Approach Summary

### 📦 Create Library With:

```
✅ Platform abstractions (expect/actual patterns)
✅ Base factory classes (Android/iOS)
✅ Common utilities and helpers
✅ Migration builders
✅ Testing utilities
✅ Configuration builders
✅ Extension functions
```

### 🚫 Don't Include in Library:

```
❌ Specific entities (UserEntity, ProductEntity)
❌ Specific DAOs
❌ Specific database implementations
❌ Business logic
❌ Repository implementations
❌ ViewModels
```

### 🎯 Each App Provides:

```
✓ Their own entities
✓ Their own DAOs
✓ Their own database definition
✓ Concrete factory implementation (extends library base)
✓ Business logic (Repository, ViewModel)
```

---

## Example: Complete Library Integration

### Library Code (kmp-room-core)

```kotlin
// Core interface
interface KmpDatabaseFactory<T : RoomDatabase> {
    fun createDatabase(name: String): T
}

// Android base
abstract class AndroidDatabaseFactory<T : RoomDatabase>(
    protected val context: Context
) : KmpDatabaseFactory<T>

// iOS base
abstract class IosDatabaseFactory<T : RoomDatabase> : KmpDatabaseFactory<T>
```

### App 1 Code (E-commerce App)

```kotlin
@Entity data class Product(...)
@Dao interface ProductDao { ... }
@Database(...) abstract class EcommerceDB : RoomDatabase()

class EcommerceDBFactory(context: Context) :
    AndroidDatabaseFactory<EcommerceDB>(context) {
    override fun createDatabase(name: String) =
        buildDatabase(EcommerceDB::class.java, ...)
}
```

### App 2 Code (Social Media App)

```kotlin
@Entity data class Post(...)
@Dao interface PostDao { ... }
@Database(...) abstract class SocialDB : RoomDatabase()

class SocialDBFactory(context: Context) :
    AndroidDatabaseFactory<SocialDB>(context) {
    override fun createDatabase(name: String) =
        buildDatabase(SocialDB::class.java, ...)
}
```

Both apps use the same infrastructure but have completely different schemas!

---

## Conclusion

### 🎯 Final Recommendation

**YES - Create a library**, but make it an **infrastructure/utility library** that provides:
- Platform abstractions
- Factory patterns
- Common utilities
- Testing helpers

**Each app should:**
- Define their own entities/DAOs/database
- Extend your library's base classes
- Implement their own business logic

This gives you:
✅ Reusable infrastructure
✅ App-specific flexibility
✅ Standardized patterns
✅ Easy maintenance

### Next Steps

1. **Start Small**: Extract just the factory pattern first
2. **Test**: Ensure it works in one app
3. **Expand**: Add utilities as needed
4. **Document**: Provide clear examples
5. **Publish**: Share with other apps
6. **Iterate**: Improve based on feedback

---

**Document Version:** 1.0
**Last Updated:** March 13, 2026
**Project:** KMPDatabasePOC
**Recommendation:** Create Infrastructure Library