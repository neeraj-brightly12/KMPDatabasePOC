# Room Database Implementation Guide - KMP (Android & iOS)

## Table of Contents
1. [Overview](#overview)
2. [Dependencies & Version Compatibility](#dependencies--version-compatibility)
3. [Project Setup](#project-setup)
4. [Android Implementation](#android-implementation)
5. [iOS Implementation](#ios-implementation)
6. [Common Code Implementation](#common-code-implementation)
7. [Mandatory Configuration Points](#mandatory-configuration-points)
8. [Testing](#testing)
9. [Troubleshooting](#troubleshooting)

---

## Overview

This project implements Room Database in a Kotlin Multiplatform (KMP) project supporting both Android and iOS platforms. Room provides an abstraction layer over SQLite for easier database access while maintaining compile-time SQL query validation.

**Key Technologies:**
- Room Database 2.7.0
- Kotlin 2.1.20
- KSP (Kotlin Symbol Processing) 2.1.20-1.0.31
- SQLite Bundled Driver 2.4.0
- Jetpack Compose Multiplatform 1.9.0

---

## Dependencies & Version Compatibility

### ⚠️ CRITICAL VERSION REQUIREMENTS

#### 1. Kotlin & KSP Version Matching
```toml
# libs.versions.toml
[versions]
kotlin = "2.1.20"

[plugins]
ksp = { id = "com.google.devtools.ksp", version = "2.1.20-1.0.31" }
```

**MANDATORY:** KSP version MUST match Kotlin version. Format: `<kotlin-version>-<ksp-version>`
- Kotlin 2.1.20 requires KSP 2.1.20-x.x.x
- Mismatched versions will cause compilation failures

#### 2. Room Database Version
```kotlin
// build.gradle.kts (composeApp)
dependencies {
    // Common dependencies
    commonMain.dependencies {
        implementation("androidx.room:room-runtime:2.7.0")
        implementation("androidx.sqlite:sqlite-bundled:2.4.0") // Required for iOS
    }

    // Android-specific
    androidMain.dependencies {
        implementation("androidx.room:room-runtime:2.7.0")
    }

    // KSP processors for all targets
    add("kspAndroid", "androidx.room:room-compiler:2.7.0")
    add("kspIosArm64", "androidx.room:room-compiler:2.7.0")
    add("kspIosSimulatorArm64", "androidx.room:room-compiler:2.7.0")
}
```

**MANDATORY:** Room 2.7.0+ required for KMP support with proper iOS integration.

#### 3. Android SDK Versions
```toml
[versions]
android-compileSdk = "36"
android-minSdk = "24"        # Minimum supported
android-targetSdk = "36"
```

**MANDATORY:** `minSdk = 24` (Android 7.0) is the minimum for Room with KMP.

#### 4. AGP (Android Gradle Plugin)
```toml
agp = "8.11.2"
```

**MANDATORY:** AGP 8.x+ required for Kotlin 2.x compatibility.

---

## Project Setup

### 1. Enable KSP Plugin

```kotlin
// build.gradle.kts (composeApp)
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.ksp)  // ← Add KSP plugin
}
```

### 2. Configure Gradle Properties

```properties
# gradle.properties
kotlin.code.style=official
kotlin.daemon.jvmargs=-Xmx3072M
kotlin.native.cacheKind=none  # Important for KSP with native targets

org.gradle.jvmargs=-Xmx4096M -Dfile.encoding=UTF-8
org.gradle.configuration-cache=true
org.gradle.caching=true

android.nonTransitiveRClass=true
android.useAndroidX=true
ksp.incremental=true  # Enable incremental KSP processing
```

### 3. Configure Source Sets

```kotlin
kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosArm64(),           // Physical iOS devices
        iosSimulatorArm64()   // iOS Simulator (Apple Silicon)
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }
}
```

---

## Android Implementation

### 1. Database Factory (Android-specific)

**File:** `composeApp/src/androidMain/kotlin/com/brightly/kmpdatabasepoc/data/database/DatabaseFactory.android.kt`

```kotlin
package com.brightly.kmpdatabasepoc.data.database

import android.content.Context
import androidx.room.Room

actual class DatabaseFactory(
    private val context: Context
) {
    actual fun createDatabase(): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app.db"  // Database file name
        ).build()
    }
}
```

**Key Points:**
- ✅ Requires `Context` parameter (typically `applicationContext`)
- ✅ Uses `Room.databaseBuilder()` for Android
- ✅ Database stored in internal storage: `/data/data/<package>/databases/app.db`
- ✅ No additional driver configuration needed (SQLite included in Android)

### 2. Integration in MainActivity

**File:** `composeApp/src/androidMain/kotlin/com/brightly/kmpdatabasepoc/MainActivity.kt`

```kotlin
package com.brightly.kmpdatabasepoc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.brightly.kmpdatabasepoc.data.database.DatabaseFactory
import com.brightly.kmpdatabasepoc.ui.App

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // Pass applicationContext to DatabaseFactory
            App(DatabaseFactory(applicationContext))
        }
    }
}
```

**MANDATORY:**
- Use `applicationContext`, NOT `this` or activity context
- Prevents memory leaks as database outlives activity lifecycle

### 3. Android-Specific Gradle Configuration

```kotlin
android {
    namespace = "com.brightly.kmpdatabasepoc"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.brightly.kmpdatabasepoc"
        minSdk = 24  // Minimum for Room KMP
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}
```

---

## iOS Implementation

### 1. Database Factory (iOS-specific)

**File:** `composeApp/src/iosMain/kotlin/com/brightly/kmpdatabasepoc/data/database/DatabaseFactory.ios.kt`

```kotlin
package com.brightly.kmpdatabasepoc.data.database

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

actual class DatabaseFactory {
    @OptIn(ExperimentalForeignApi::class)
    actual fun createDatabase(): AppDatabase {
        // Get iOS Documents directory
        val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null
        )

        // Construct full database file path
        val dbFile = requireNotNull(documentDirectory?.path) + "/app.db"

        return Room.databaseBuilder<AppDatabase>(
            name = dbFile,  // Full path required for iOS
        )
            .setDriver(BundledSQLiteDriver())  // MANDATORY for iOS
            .build()
    }
}
```

**Key Points:**
- ✅ **MANDATORY:** Must use `BundledSQLiteDriver()` for iOS
- ✅ Database stored in Documents directory: `~/Documents/app.db`
- ✅ Full file path required (unlike Android)
- ✅ Uses `@OptIn(ExperimentalForeignApi::class)` for iOS interop
- ✅ No Context parameter needed

### 2. iOS-Specific Dependencies

**MANDATORY:** Add SQLite bundled driver in common source set:

```kotlin
commonMain.dependencies {
    implementation("androidx.room:room-runtime:2.7.0")
    implementation("androidx.sqlite:sqlite-bundled:2.4.0")  // Required for iOS
}
```

### 3. Integration in iOS

**File:** `composeApp/src/iosMain/kotlin/com/brightly/kmpdatabasepoc/MainViewController.kt`

```kotlin
package com.brightly.kmpdatabasepoc

import androidx.compose.ui.window.ComposeUIViewController
import com.brightly.kmpdatabasepoc.data.database.DatabaseFactory
import com.brightly.kmpdatabasepoc.ui.App

fun MainViewController() = ComposeUIViewController {
    App(DatabaseFactory())  // No parameters needed for iOS
}
```

### 4. KSP Configuration for iOS Targets

**MANDATORY:** Add KSP for all iOS targets:

```kotlin
dependencies {
    add("kspAndroid", "androidx.room:room-compiler:2.7.0")
    add("kspIosArm64", "androidx.room:room-compiler:2.7.0")           // Physical devices
    add("kspIosSimulatorArm64", "androidx.room:room-compiler:2.7.0")  // Simulator
}
```

---

## Common Code Implementation

### 1. Database Definition

**File:** `composeApp/src/commonMain/kotlin/com/brightly/kmpdatabasepoc/data/database/Appdatabase.kt`

```kotlin
package com.brightly.kmpdatabasepoc.data.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import com.brightly.kmpdatabasepoc.data.dao.UserDao
import com.brightly.kmpdatabasepoc.data.entity.UserEntity

@Database(
    entities = [UserEntity::class],
    version = 1  // Database schema version
)
@ConstructedBy(AppDatabaseConstructor::class)  // Required for KMP
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase>
```

**Key Points:**
- ✅ `@ConstructedBy` annotation MANDATORY for KMP
- ✅ `expect object` for constructor - KSP generates `actual` implementations
- ✅ Version number for schema migrations
- ✅ List all entities in `entities` array

### 2. Entity Definition

**File:** `composeApp/src/commonMain/kotlin/com/brightly/kmpdatabasepoc/data/entity/UserEntity.kt`

```kotlin
package com.brightly.kmpdatabasepoc.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val name: String
)
```

**Key Points:**
- ✅ `@Entity` defines database table
- ✅ `tableName` specifies table name (optional, defaults to class name)
- ✅ `@PrimaryKey(autoGenerate = true)` for auto-increment ID
- ✅ Default value `0` for auto-generated primary key

### 3. DAO (Data Access Object)

**File:** `composeApp/src/commonMain/kotlin/com/brightly/kmpdatabasepoc/data/dao/userDao.kt`

```kotlin
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
```

**Key Points:**
- ✅ `@Dao` interface defines database operations
- ✅ Use `suspend` for write operations (insert, update, delete)
- ✅ Use `Flow<T>` for reactive queries (auto-updates on data change)
- ✅ Compile-time SQL validation

### 4. DatabaseFactory (Expect Declaration)

**File:** `composeApp/src/commonMain/kotlin/com/brightly/kmpdatabasepoc/data/database/DatabaseFactory.kt`

```kotlin
package com.brightly.kmpdatabasepoc.data.database

expect class DatabaseFactory {
    fun createDatabase(): AppDatabase
}
```

### 5. Repository Layer

**File:** `composeApp/src/commonMain/kotlin/com/brightly/kmpdatabasepoc/data/repository/UserRepository.kt`

```kotlin
package com.brightly.kmpdatabasepoc.data.repository

import com.brightly.kmpdatabasepoc.data.database.AppDatabase
import com.brightly.kmpdatabasepoc.data.entity.UserEntity
import kotlinx.coroutines.flow.Flow

class UserRepository(
    database: AppDatabase
) {
    private val dao = database.userDao()

    suspend fun addUser(name: String) {
        val user = UserEntity(name = name)
        dao.insertUser(user)
    }

    fun getUsers(): Flow<List<UserEntity>> {
        return dao.getUsers()
    }
}
```

### 6. ViewModel

**File:** `composeApp/src/commonMain/kotlin/com/brightly/kmpdatabasepoc/viewmodel/UserViewModel.kt`

```kotlin
package com.brightly.kmpdatabasepoc.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brightly.kmpdatabasepoc.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class UserViewModel(
    private val repository: UserRepository
) : ViewModel() {

    private val _users = MutableStateFlow<List<String>>(emptyList())
    val users: StateFlow<List<String>> = _users

    init {
        loadUsers()
    }

    private fun loadUsers() {
        viewModelScope.launch {
            repository.getUsers()
                .map { list -> list.map { it.name } }
                .collect { userNames ->
                    _users.value = userNames
                }
        }
    }

    fun addUser(name: String) {
        viewModelScope.launch {
            repository.addUser(name)
        }
    }
}
```

---

## Mandatory Configuration Points

### ✅ Checklist for Successful Setup

#### 1. Version Compatibility
- [ ] Kotlin version matches KSP version (2.1.20 → 2.1.20-1.0.31)
- [ ] Room version 2.7.0+ (for KMP support)
- [ ] AGP 8.x+ (for Kotlin 2.x)
- [ ] Android minSdk ≥ 24

#### 2. Dependencies
- [ ] `androidx.room:room-runtime:2.7.0` in commonMain
- [ ] `androidx.sqlite:sqlite-bundled:2.4.0` in commonMain (for iOS)
- [ ] KSP compiler for all targets (Android, iosArm64, iosSimulatorArm64)

#### 3. Database Configuration
- [ ] `@Database` annotation with entities and version
- [ ] `@ConstructedBy` annotation for KMP constructor
- [ ] `expect object` for RoomDatabaseConstructor

#### 4. Platform-Specific Implementations
- [ ] Android: Context-based DatabaseFactory
- [ ] iOS: BundledSQLiteDriver + full file path
- [ ] Both: Proper actual class implementations

#### 5. Gradle Configuration
- [ ] KSP plugin enabled in build.gradle.kts
- [ ] `ksp.incremental=true` in gradle.properties
- [ ] Proper source sets for iOS targets

#### 6. Code Generation
- [ ] Build project to generate Room code via KSP
- [ ] Check `build/generated/ksp/` for generated implementations
- [ ] Verify no compilation errors in generated code

---

## Testing

### Test Database Creation

```kotlin
// Android Test
class DatabaseTest {
    @Test
    fun testDatabaseCreation() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val factory = DatabaseFactory(context)
        val database = factory.createDatabase()

        assertNotNull(database)
        assertNotNull(database.userDao())
    }
}
```

### Test Database Operations

```kotlin
@Test
fun testInsertAndRetrieve() = runTest {
    val database = createTestDatabase()
    val dao = database.userDao()

    val user = UserEntity(name = "Test User")
    dao.insertUser(user)

    dao.getUsers().first().let { users ->
        assertEquals(1, users.size)
        assertEquals("Test User", users[0].name)
    }
}
```

---

## Troubleshooting

### Common Issues

#### 1. KSP Version Mismatch
**Error:** `ksp version mismatch`
**Solution:** Ensure KSP version format matches: `<kotlin-version>-<ksp-release>`

#### 2. iOS Build Failure
**Error:** `No driver available`
**Solution:** Add `androidx.sqlite:sqlite-bundled:2.4.0` to commonMain dependencies

#### 3. Missing Generated Code
**Error:** `Cannot find AppDatabase_Impl`
**Solution:**
- Clean project: `./gradlew clean`
- Rebuild: `./gradlew build`
- Check `build/generated/ksp/` for generated files

#### 4. Context Memory Leak (Android)
**Error:** Memory leak warnings
**Solution:** Always use `applicationContext`, never activity context

#### 5. Database File Not Found (iOS)
**Error:** `Database file not accessible`
**Solution:**
- Use full file path with Documents directory
- Ensure BundledSQLiteDriver is set

#### 6. Coroutine Scope Issues
**Error:** `CoroutineScope required`
**Solution:**
- Use `viewModelScope` for ViewModel operations
- Use `suspend` functions for database writes
- Use `Flow` for reactive queries

---

## Best Practices

1. **Always use applicationContext** for Android database creation
2. **Set BundledSQLiteDriver** explicitly for iOS
3. **Use Flow** for reactive database queries
4. **Use suspend functions** for write operations
5. **Version your database** properly for migrations
6. **Test on both platforms** before release
7. **Clean build** after changing entity/dao definitions
8. **Enable incremental KSP** for faster builds

---

## Additional Resources

- [Room Documentation](https://developer.android.com/training/data-storage/room)
- [KMP Database Guide](https://developer.android.com/kotlin/multiplatform/data-and-networking)
- [KSP Documentation](https://kotlinlang.org/docs/ksp-overview.html)

---

**Document Version:** 1.0
**Last Updated:** March 12, 2026
**Project:** KMPDatabasePOC
