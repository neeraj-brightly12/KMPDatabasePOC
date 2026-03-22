# Current Implementation Guide

**Last Updated:** March 2026
**Library Version:** kmp-room-core v1.0.2+
**Status:** ✅ Using Published GitHub Library

---

## Table of Contents

1. [Current Architecture](#current-architecture)
2. [Critical Build Configuration](#critical-build-configuration)
3. [Why KSP is Required](#why-ksp-is-required)
4. [Complete Setup](#complete-setup)
5. [Common Mistakes](#common-mistakes)
6. [Troubleshooting](#troubleshooting)

---

## Current Architecture

### What We're Using

```
┌─────────────────────────────────────────────────────┐
│         GitHub Packages (Published)                 │
│   com.brightly:kmp-room-core:1.0.2                 │
│   - Platform abstractions                           │
│   - Database utilities                              │
│   - Migration helpers                               │
└────────────────┬────────────────────────────────────┘
                 │ implementation()
                 ▼
┌─────────────────────────────────────────────────────┐
│              Your composeApp                        │
│   ├── Entities (UserEntity, ProductEntity)         │
│   ├── DAOs (UserDao, ProductDao)                   │
│   ├── Database (AppDatabase)                       │
│   ├── Repositories (UserRepository)                │
│   └── ViewModels (UserViewModel)                   │
│                                                      │
│   ⚠️ REQUIRES KSP for annotation processing        │
└─────────────────────────────────────────────────────┘
```

### What's Published vs What's Local

**Published Library (`kmp-room-core`):**
- ✅ Platform-specific factory base classes
- ✅ DatabaseConfig & utilities
- ✅ Migration DSL
- ✅ Flow extensions
- ✅ Database helper functions

**Your App (composeApp):**
- ✅ Your specific entities with `@Entity`
- ✅ Your DAOs with `@Dao`
- ✅ Your database with `@Database`
- ✅ Concrete factory implementations
- ✅ Business logic (Repository, ViewModel)

---

## Critical Build Configuration

### ⚠️ CORRECT Configuration

```kotlin
// composeApp/build.gradle.kts

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.ksp) // ✅ REQUIRED - DO NOT COMMENT OUT
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            // Using published library from GitHub Packages
            implementation("com.brightly:kmp-room-core:1.0.2")
        }
    }
}

dependencies {
    // ✅ REQUIRED - DO NOT COMMENT OUT
    // These process YOUR entities and DAOs, not the library's
    add("kspAndroid", "androidx.room:room-compiler:2.7.0")
    add("kspIosArm64", "androidx.room:room-compiler:2.7.0")
    add("kspIosSimulatorArm64", "androidx.room:room-compiler:2.7.0")
}
```

### ❌ INCORRECT Configuration (DO NOT DO THIS)

```kotlin
// ❌ WRONG - This will cause build failures
plugins {
    // ...
//  alias(libs.plugins.ksp) // ❌ Commented out - BUILD WILL FAIL
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation("com.brightly:kmp-room-core:1.0.2")
        }
    }
}

dependencies {
    // ❌ WRONG - Commenting these out will cause build failures
//  add("kspAndroid", "androidx.room:room-compiler:2.7.0")
//  add("kspIosArm64", "androidx.room:room-compiler:2.7.0")
//  add("kspIosSimulatorArm64", "androidx.room:room-compiler:2.7.0")
}
```

---

## Why KSP is Required

### Understanding the Architecture

The `kmp-room-core` library **does NOT contain Room entities or DAOs**. It only provides:
- Infrastructure classes
- Platform abstractions
- Utility functions

### Your App Contains Room Annotations

Your `composeApp` module has these files that use Room annotations:

```kotlin
// composeApp/src/commonMain/.../entity/UserEntity.kt
@Entity(tableName = "users")  // ⚠️ Requires KSP
data class UserEntity(
    @PrimaryKey(autoGenerate = true)  // ⚠️ Requires KSP
    val id: Int = 0,
    val name: String
)

// composeApp/src/commonMain/.../entity/ProductEntity.kt
@Entity(tableName = "products")  // ⚠️ Requires KSP
data class ProductEntity(...)

// composeApp/src/commonMain/.../dao/UserDao.kt
@Dao  // ⚠️ Requires KSP
interface UserDao {
    @Insert  // ⚠️ Requires KSP
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM users")  // ⚠️ Requires KSP
    fun getUsers(): Flow<List<UserEntity>>
}

// composeApp/src/commonMain/.../dao/ProductDao.kt
@Dao  // ⚠️ Requires KSP
interface ProductDao { ... }

// composeApp/src/commonMain/.../database/AppDatabase.kt
@Database(  // ⚠️ Requires KSP
    entities = [UserEntity::class, ProductEntity::class],
    version = 1
)
@ConstructedBy(AppDatabaseConstructor::class)  // ⚠️ Requires KSP
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun productDao(): ProductDao
}
```

### What KSP Does

KSP (Kotlin Symbol Processing) processes these annotations and generates:

1. **Implementation code** for your DAOs
   - `UserDao_Impl.kt`
   - `ProductDao_Impl.kt`

2. **Database implementation**
   - `AppDatabase_Impl.kt`

3. **Query validation** at compile time

4. **Type converters** and other supporting code

**Without KSP, none of this code is generated, and your build will fail.**

---

## Complete Setup

### Step 1: Add GitHub Token (One-time setup)

Add to `~/.gradle/gradle.properties`:

```properties
gpr.user=your-github-username
gpr.token=your-github-personal-access-token
```

Or set environment variables:
```bash
export GITHUB_ACTOR=your-github-username
export GITHUB_TOKEN=your-github-token
```

### Step 2: Configure Repository Access

In `settings.gradle.kts`:

```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://maven.pkg.github.com/neeraj-brightly12/KMPDatabasePOC")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                    ?: findProperty("gpr.user") as String?
                password = System.getenv("GITHUB_TOKEN")
                    ?: findProperty("gpr.token") as String?
            }
        }
    }
}
```

### Step 3: Configure Build File

In `composeApp/build.gradle.kts`:

```kotlin
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.ksp) // ✅ Must be enabled
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation("com.brightly:kmp-room-core:1.0.2")

            // Other dependencies...
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
        }
    }
}

dependencies {
    // ✅ Required for Room annotation processing
    add("kspAndroid", "androidx.room:room-compiler:2.7.0")
    add("kspIosArm64", "androidx.room:room-compiler:2.7.0")
    add("kspIosSimulatorArm64", "androidx.room:room-compiler:2.7.0")
}
```

### Step 4: Sync and Build

```bash
# Clean build
./gradlew clean

# Build project
./gradlew build

# Run Android
./gradlew :composeApp:assembleDebug

# Run iOS (in Xcode)
# Open iosApp/iosApp.xcodeproj
```

---

## Common Mistakes

### ❌ Mistake 1: Commenting Out KSP

**Problem:**
```kotlin
plugins {
//  alias(libs.plugins.ksp) // ❌ Commented out
}
```

**Error You'll See:**
```
error: cannot find symbol
symbol: class UserDao_Impl
error: @Dao class UserDao has no implementation
```

**Solution:** Uncomment the KSP plugin.

---

### ❌ Mistake 2: Commenting Out KSP Dependencies

**Problem:**
```kotlin
dependencies {
//  add("kspAndroid", "androidx.room:room-compiler:2.7.0")
//  add("kspIosArm64", "androidx.room:room-compiler:2.7.0")
//  add("kspIosSimulatorArm64", "androidx.room:room-compiler:2.7.0")
}
```

**Error You'll See:**
```
error: Room cannot find implementation for AppDatabase
error: UserEntity is not a database table
```

**Solution:** Uncomment all KSP dependencies for all target platforms.

---

### ❌ Mistake 3: Thinking Library Includes KSP

**Wrong Assumption:**
> "The `kmp-room-core` library already handles Room, so I don't need KSP in my app."

**Reality:**
- The library provides **infrastructure** (factory classes, utilities)
- Your app provides **data models** (entities, DAOs)
- KSP must process **your app's annotations**, not the library's

---

### ❌ Mistake 4: Using Local Project Path

**Problem:**
```kotlin
commonMain.dependencies {
    implementation(project(":kmp-room-core")) // ❌ Only for development
}
```

**When to Use Each:**

**Local Project (Development):**
```kotlin
implementation(project(":kmp-room-core"))
// Use when actively developing the library
```

**Published Library (Production):**
```kotlin
implementation("com.brightly:kmp-room-core:1.0.2")
// Use for production apps
```

---

## Troubleshooting

### Issue: "Cannot resolve com.brightly:kmp-room-core"

**Causes:**
1. GitHub token not configured
2. Wrong repository URL
3. No read:packages permission

**Solution:**
```bash
# Verify token has read:packages permission
# Check gradle.properties has correct credentials
cat ~/.gradle/gradle.properties | grep gpr

# Or check environment variables
echo $GITHUB_ACTOR
echo $GITHUB_TOKEN
```

---

### Issue: "Room annotation processor not found"

**Error:**
```
error: Room requires annotation processing
```

**Solution:**
Enable KSP plugin and dependencies:
```kotlin
plugins {
    alias(libs.plugins.ksp) // Add this
}

dependencies {
    add("kspAndroid", "androidx.room:room-compiler:2.7.0")
    add("kspIosArm64", "androidx.room:room-compiler:2.7.0")
    add("kspIosSimulatorArm64", "androidx.room:room-compiler:2.7.0")
}
```

---

### Issue: "Cannot find UserDao_Impl"

**Causes:**
- KSP plugin disabled
- KSP dependencies missing
- Build not synced

**Solution:**
1. Enable KSP plugin
2. Add KSP dependencies
3. Sync project: `./gradlew clean build`

---

### Issue: Gradle lock timeout

**Error:**
```
Timeout waiting to lock checksums cache
Owner PID: 5913
```

**Solution:**
```bash
# Kill stuck Gradle daemon
kill -9 5913

# Or stop all daemons
./gradlew --stop

# Clean lock files
rm -rf .gradle/*/checksums/*.lock
```

---

## Summary: What You Must Have

### ✅ Required Configuration Checklist

- [ ] KSP plugin enabled in `build.gradle.kts`
- [ ] All three KSP dependencies (Android, iOsArm64, iOsSimulatorArm64)
- [ ] Published library dependency: `implementation("com.brightly:kmp-room-core:1.0.2")`
- [ ] GitHub credentials configured (token with read:packages)
- [ ] Repository configured in `settings.gradle.kts`

### ✅ What Gets Processed by KSP

Your app module contains:
- ✅ `UserEntity` with `@Entity` annotation
- ✅ `ProductEntity` with `@Entity` annotation
- ✅ `UserDao` with `@Dao` annotation
- ✅ `ProductDao` with `@Dao` annotation
- ✅ `AppDatabase` with `@Database` annotation

All of these **require KSP** to generate implementation code.

### ✅ What the Library Provides

The `kmp-room-core` library provides:
- ✅ `AndroidDatabaseFactory` base class
- ✅ `IosDatabaseFactory` base class
- ✅ `DatabaseConfig` data class
- ✅ Migration utilities
- ✅ Flow extensions
- ✅ Database utilities

None of these require KSP because they don't use Room annotations.

---

## Quick Reference

### Correct Build Configuration

```kotlin
// ✅ CORRECT SETUP
plugins {
    alias(libs.plugins.ksp) // Required
}

commonMain.dependencies {
    implementation("com.brightly:kmp-room-core:1.0.2")
}

dependencies {
    add("kspAndroid", "androidx.room:room-compiler:2.7.0")
    add("kspIosArm64", "androidx.room:room-compiler:2.7.0")
    add("kspIosSimulatorArm64", "androidx.room:room-compiler:2.7.0")
}
```

### Key Points to Remember

1. **Library ≠ KSP**: The library doesn't eliminate the need for KSP
2. **Your Code Needs KSP**: Your entities and DAOs require annotation processing
3. **Two Separate Things**: Library dependency + KSP dependencies (both required)
4. **Don't Comment Out**: Never disable KSP if you have Room annotations

---

**For more information:**
- Library documentation: [kmp-room-core/README.md](./kmp-room-core/README.md)
- Architecture details: [ARCHITECTURE_DOCUMENT.md](./ARCHITECTURE_DOCUMENT.md)
- Usage examples: [kmp-room-core/USAGE_EXAMPLE.md](./kmp-room-core/USAGE_EXAMPLE.md)