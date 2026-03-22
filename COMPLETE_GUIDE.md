# Complete Guide: Using & Publishing kmp-room-core Library

This guide covers everything you need to know about using and publishing the `kmp-room-core` library.

---

## Table of Contents
1. [Using the Library in Your App](#part-1-using-the-library-in-your-app)
2. [Publishing New Versions](#part-2-publishing-new-versions)
3. [KSP Requirement](#part-3-ksp-requirement-important)
4. [Troubleshooting](#troubleshooting)

---

# Part 1: Using the Library in Your App

## Prerequisites

### 1. GitHub Personal Access Token

**Create token (one-time setup):**

1. Go to: https://github.com/settings/tokens/new
2. Fill in:
   - **Note:** `My App - GitHub Packages`
   - **Expiration:** 90 days (recommended) or your preference
   - **Scopes:** ✅ Check `read:packages`
3. Click **Generate token**
4. **Copy the token** (starts with `ghp_...`)

### 2. Store Credentials

Add to `~/.gradle/gradle.properties`:
```properties
gpr.user=neeraj-brightly12
gpr.token=ghp_YOUR_TOKEN_HERE
```

**Security Note:** This file is in your home directory and won't be committed to Git.

---

## Step-by-Step: Add Library to Your App

### Step 1: Configure Repository Access

**File:** `settings.gradle.kts`

```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()

        // GitHub Packages - kmp-room-core library
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/neeraj-brightly12/KMPDatabasePOC")
            credentials {
                username = System.getenv("GITHUB_ACTOR") ?:
                          providers.gradleProperty("gpr.user").orNull
                password = System.getenv("GITHUB_TOKEN") ?:
                          providers.gradleProperty("gpr.token").orNull
            }
        }
    }
}

// Only include composeApp, NOT kmp-room-core
include(":composeApp")
```

**What this does:**
- Adds GitHub Packages as a repository
- Uses credentials from `~/.gradle/gradle.properties` or environment variables
- Removes local `kmp-room-core` module (using published version instead)

---

### Step 2: Enable KSP Plugin

**File:** `gradle/libs.versions.toml`

```toml
[plugins]
# ... other plugins
ksp = { id = "com.google.devtools.ksp", version = "2.1.20-1.0.31" }
```

**Uncomment the KSP line** if it's commented out.

---

### Step 3: Add Library Dependency

**File:** `composeApp/build.gradle.kts`

```kotlin
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.ksp)  // ⚠️ REQUIRED for Room
}

kotlin {
    androidTarget()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            // ... your other dependencies

            // ✨ Add the library - Single dependency for all platforms!
            implementation("com.brightly:kmp-room-core:1.0.1")
        }
    }
}

dependencies {
    // ⚠️ REQUIRED: KSP processors for Room
    add("kspAndroid", "androidx.room:room-compiler:2.7.0")
    add("kspIosArm64", "androidx.room:room-compiler:2.7.0")
    add("kspIosSimulatorArm64", "androidx.room:room-compiler:2.7.0")
}
```

**Key points:**
- ✅ Single dependency: `com.brightly:kmp-room-core:1.0.1`
- ✅ Works for all platforms (Android, iOS)
- ⚠️ **KSP is REQUIRED** (see Part 3 for why)

---

### Step 4: Sync Gradle

```bash
./gradlew --refresh-dependencies
```

**Or in Android Studio:**
- Click "Sync Now" when prompted
- Or: File → Sync Project with Gradle Files

---

### Step 5: Define Your Database (in Your App)

**Your app defines the entities, DAOs, and database:**

#### Entity (commonMain)
```kotlin
// composeApp/src/commonMain/kotlin/your/package/data/entity/UserEntity.kt
package your.package.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val email: String
)
```

#### DAO (commonMain)
```kotlin
// composeApp/src/commonMain/kotlin/your/package/data/dao/UserDao.kt
package your.package.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import your.package.data.entity.UserEntity

@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: UserEntity)

    @Query("SELECT * FROM users")
    fun getAll(): Flow<List<UserEntity>>

    @Query("DELETE FROM users WHERE id = :id")
    suspend fun delete(id: Int)
}
```

#### Database (commonMain)
```kotlin
// composeApp/src/commonMain/kotlin/your/package/data/database/AppDatabase.kt
package your.package.data.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import your.package.data.dao.UserDao
import your.package.data.entity.UserEntity

@Database(
    entities = [UserEntity::class],
    version = 1,
    exportSchema = false
)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase>
```

#### Database Factory (commonMain)
```kotlin
// composeApp/src/commonMain/kotlin/your/package/data/database/DatabaseFactory.kt
package your.package.data.database

expect class DatabaseFactory {
    fun createDatabase(): AppDatabase
}
```

---

### Step 6: Implement Platform-Specific Factory

#### Android (androidMain)
```kotlin
// composeApp/src/androidMain/kotlin/your/package/data/database/DatabaseFactory.android.kt
package your.package.data.database

import android.content.Context
import androidx.room.migration.Migration
import com.brightly.kmp.room.core.android.AndroidDatabaseFactory
import com.brightly.kmp.room.core.DatabaseConfig

actual class DatabaseFactory(
    private val context: Context
) : AndroidDatabaseFactory<AppDatabase>(context) {

    actual fun createDatabase(): AppDatabase {
        return createDatabase("app.db", emptyList())
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
                enableLogging = true,
                migrations = migrations
            )
        )
    }
}
```

#### iOS (iosMain)
```kotlin
// composeApp/src/iosMain/kotlin/your/package/data/database/DatabaseFactory.ios.kt
package your.package.data.database

import androidx.room.migration.Migration
import com.brightly.kmp.room.core.ios.IosDatabaseFactory
import com.brightly.kmp.room.core.DatabaseConfig

actual class DatabaseFactory : IosDatabaseFactory<AppDatabase>() {

    actual fun createDatabase(): AppDatabase {
        return createDatabase("app.db", emptyList())
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

---

### Step 7: Use the Database in Your App

```kotlin
// Android
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = DatabaseFactory(applicationContext).createDatabase()

        setContent {
            App(database)
        }
    }
}

// iOS (in MainViewController.kt)
fun MainViewController() = ComposeUIViewController {
    val database = remember { DatabaseFactory().createDatabase() }
    App(database)
}

// Common App
@Composable
fun App(database: AppDatabase) {
    val userDao = remember { database.userDao() }
    val users by userDao.getAll().collectAsState(initial = emptyList())

    // Your UI
    Column {
        users.forEach { user ->
            Text("${user.name} - ${user.email}")
        }

        Button(onClick = {
            scope.launch {
                userDao.insert(UserEntity(name = "John", email = "john@example.com"))
            }
        }) {
            Text("Add User")
        }
    }
}
```

---

### Step 8: Build and Run

#### Android
```bash
./gradlew :composeApp:assembleDebug
# or
./gradlew :composeApp:installDebug
```

#### iOS
```bash
./gradlew :composeApp:compileKotlinIosSimulatorArm64
# Then open in Xcode
cd iosApp
open iosApp.xcodeproj
```

---

## Verification

### Check Library is Downloaded
```bash
./gradlew :composeApp:dependencies --configuration commonMainImplementationDependenciesMetadata | grep kmp-room-core
```

**Expected output:**
```
+--- com.brightly:kmp-room-core:1.0.1
```

### Check KSP Generated Code
After building, check:
```bash
# Android
ls composeApp/build/generated/ksp/android/androidDebug/kotlin/

# iOS
ls composeApp/build/generated/ksp/iosSimulatorArm64/iosSimulatorArm64Main/kotlin/
```

Should contain:
- `AppDatabaseConstructor.kt`
- `UserDao_Impl.kt`

---

# Part 2: Publishing New Versions

When you need to update the `kmp-room-core` library and publish a new version:

## Step 1: Make Changes to Library Code

Edit files in `kmp-room-core/src/`:
```
kmp-room-core/
├── src/
│   ├── commonMain/kotlin/com/brightly/kmp/room/core/
│   ├── androidMain/kotlin/com/brightly/kmp/room/core/
│   └── iosMain/kotlin/com/brightly/kmp/room/core/
└── build.gradle.kts
```

**Example changes:**
- Add new utility functions
- Fix bugs
- Add new features
- Update dependencies

---

## Step 2: Update Version Number

**File:** `kmp-room-core/build.gradle.kts`

```kotlin
group = "com.brightly"
version = "1.0.2"  // ← Increment version

publishing {
    repositories {
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

**Version Scheme (Semantic Versioning):**
- **Major (2.0.0):** Breaking changes (API changes that break existing code)
- **Minor (1.1.0):** New features (backward compatible)
- **Patch (1.0.1):** Bug fixes (backward compatible)

---

## Step 3: Test Library Locally (Optional but Recommended)

Before publishing, test your changes:

```bash
# Publish to local Maven (.m2)
./gradlew :kmp-room-core:publishToMavenLocal

# Then in your app's build.gradle.kts, temporarily use:
repositories {
    mavenLocal()  // Add this
    // ... other repos
}

# Test your app
./gradlew :composeApp:assembleDebug

# If tests pass, remove mavenLocal() and continue to publish
```

---

## Step 4: Clean Build

```bash
./gradlew :kmp-room-core:clean
```

---

## Step 5: Publish to GitHub Packages

```bash
./gradlew :kmp-room-core:publish
```

**What happens:**
```
Publishing to: https://maven.pkg.github.com/neeraj-brightly12/KMPDatabasePOC

✅ publishAndroidReleasePublicationToGitHubPackagesRepository
   Uploads: kmp-room-core-android-1.0.2.aar

✅ publishIosArm64PublicationToGitHubPackagesRepository
   Uploads: kmp-room-core-iosarm64-1.0.2.klib

✅ publishIosSimulatorArm64PublicationToGitHubPackagesRepository
   Uploads: kmp-room-core-iossimulatorarm64-1.0.2.klib

✅ publishKotlinMultiplatformPublicationToGitHubPackagesRepository
   Uploads: kmp-room-core-1.0.2.module (root metadata)
```

**Expected output:**
```
BUILD SUCCESSFUL in 2m 30s
```

---

## Step 6: Create Git Tag (Recommended)

```bash
git add kmp-room-core/build.gradle.kts
git commit -m "Release kmp-room-core v1.0.2"
git tag v1.0.2
git push origin main
git push origin v1.0.2
```

---

## Step 7: Verify Publication

Visit: https://github.com/neeraj-brightly12/KMPDatabasePOC/packages

You should see:
```
📦 kmp-room-core
   Versions:
   - 1.0.2 (just now)
   - 1.0.1
   - 1.0.0
```

---

## Step 8: Update Apps Using the Library

In consuming apps, update the version:

**File:** `composeApp/build.gradle.kts`

```kotlin
commonMain.dependencies {
    implementation("com.brightly:kmp-room-core:1.0.2")  // ← Update version
}
```

**Sync and rebuild:**
```bash
./gradlew --refresh-dependencies
./gradlew :composeApp:build
```

---

## Complete Publishing Workflow Summary

```bash
# 1. Make changes to kmp-room-core code
# 2. Update version
echo 'version = "1.0.2"' # in build.gradle.kts

# 3. Clean
./gradlew :kmp-room-core:clean

# 4. Publish
./gradlew :kmp-room-core:publish

# 5. Tag (optional but recommended)
git tag v1.0.2
git push origin v1.0.2

# 6. Verify
# Visit: https://github.com/neeraj-brightly12/KMPDatabasePOC/packages
```

---

# Part 3: KSP Requirement (IMPORTANT)

## ⚠️ YES, KSP is REQUIRED in Consumer Apps

### Why KSP is Needed

**The library (`kmp-room-core`) provides:**
- ✅ Platform abstractions (AndroidDatabaseFactory, IosDatabaseFactory)
- ✅ Utility functions
- ✅ Configuration helpers
- ✅ Room runtime dependencies

**The library does NOT provide:**
- ❌ Your app's entities (`@Entity`)
- ❌ Your app's DAOs (`@Dao`)
- ❌ Your app's database schema (`@Database`)

**Room needs to generate code for YOUR entities and DAOs.**

### What KSP Does in Your App

KSP processes Room annotations in **your app's code** and generates:

1. **DAO Implementations:**
   ```kotlin
   // You write:
   @Dao
   interface UserDao {
       @Query("SELECT * FROM users")
       fun getAll(): Flow<List<UserEntity>>
   }

   // KSP generates:
   class UserDao_Impl : UserDao {
       override fun getAll(): Flow<List<UserEntity>> {
           // Generated SQL execution code
       }
   }
   ```

2. **Database Constructor:**
   ```kotlin
   // You write:
   @ConstructedBy(AppDatabaseConstructor::class)
   abstract class AppDatabase : RoomDatabase()

   expect object AppDatabaseConstructor

   // KSP generates:
   actual object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
       override fun initialize(): AppDatabase {
           // Generated database initialization code
       }
   }
   ```

3. **Query Validation:**
   - Validates SQL syntax at compile-time
   - Type-checks query results
   - Generates type-safe database access code

### Configuration in Your App

**Required:**
```kotlin
plugins {
    alias(libs.plugins.ksp)  // ⚠️ MUST HAVE
}

dependencies {
    // ⚠️ MUST HAVE for each platform
    add("kspAndroid", "androidx.room:room-compiler:2.7.0")
    add("kspIosArm64", "androidx.room:room-compiler:2.7.0")
    add("kspIosSimulatorArm64", "androidx.room:room-compiler:2.7.0")
}
```

### What Happens Without KSP?

**Build Error:**
```
Object 'AppDatabaseConstructor' is not abstract and does not implement abstract member
```

**Why?** Room expects generated code that doesn't exist without KSP.

### Summary Table

| Component | Where Defined | Processed By |
|-----------|--------------|-------------|
| Platform abstractions | `kmp-room-core` library | N/A (already compiled) |
| Your entities (`@Entity`) | Your app | KSP in your app |
| Your DAOs (`@Dao`) | Your app | KSP in your app |
| Your database (`@Database`) | Your app | KSP in your app |
| DAO implementations | Generated | KSP in your app |
| Database constructor | Generated | KSP in your app |

**Bottom line:** KSP is **ALWAYS REQUIRED** in apps using Room, whether using a library or not.

---

# Complete Configuration Checklist

## For Library Users (Consumer Apps)

- [ ] GitHub token created with `read:packages` permission
- [ ] Token saved in `~/.gradle/gradle.properties`
- [ ] GitHub Packages repository added to `settings.gradle.kts`
- [ ] KSP plugin enabled in `gradle/libs.versions.toml`
- [ ] KSP plugin applied in `composeApp/build.gradle.kts`
- [ ] Library dependency added: `implementation("com.brightly:kmp-room-core:1.0.1")`
- [ ] KSP dependencies added for all platforms (Android, iOS)
- [ ] Entities, DAOs, Database defined in your app
- [ ] Platform-specific DatabaseFactory implemented
- [ ] Gradle sync successful
- [ ] Build successful

## For Library Publishers

- [ ] GitHub token created with `write:packages` permission
- [ ] Token saved in `~/.gradle/gradle.properties`
- [ ] Repository URL configured in `kmp-room-core/build.gradle.kts`
- [ ] Version number updated
- [ ] Changes tested locally (optional)
- [ ] Published with `./gradlew :kmp-room-core:publish`
- [ ] Git tag created and pushed
- [ ] Package visible on GitHub

---

# Troubleshooting

## Issue: "Could not find com.brightly:kmp-room-core:1.0.1"

**Cause:** Library not accessible or credentials wrong

**Solution:**
1. Check `~/.gradle/gradle.properties` has correct credentials
2. Verify token at: https://github.com/settings/tokens
3. Ensure token has `read:packages` scope
4. Try:
   ```bash
   ./gradlew --refresh-dependencies
   ```

---

## Issue: "401 Unauthorized"

**Cause:** Invalid or expired token

**Solution:**
1. Generate new token: https://github.com/settings/tokens/new
2. Update `~/.gradle/gradle.properties`
3. Clear Gradle cache:
   ```bash
   rm -rf ~/.gradle/caches/
   ./gradlew --refresh-dependencies
   ```

---

## Issue: "Object 'AppDatabaseConstructor' is not abstract..."

**Cause:** KSP hasn't generated code yet or KSP not configured

**Solution:**
1. Verify KSP plugin is applied
2. Build platform-specific target (not metadata):
   ```bash
   ./gradlew :composeApp:compileDebugKotlinAndroid
   ```
3. Check generated code exists:
   ```bash
   ls composeApp/build/generated/ksp/android/androidDebug/kotlin/
   ```

---

## Issue: ":composeApp:compileCommonMainKotlinMetadata FAILED"

**Cause:** KMP + KSP timing issue (known limitation)

**Solution:**
This is normal. Build platform targets directly:
```bash
# Android
./gradlew :composeApp:assembleDebug

# iOS
./gradlew :composeApp:compileKotlinIosSimulatorArm64
```

Both work fine! The metadata task failure doesn't affect actual builds.

---

## Issue: "Multiple publications with same coordinates"

**Cause:** Publishing configuration issue

**Solution:**
Ensure `kmp-room-core/build.gradle.kts` has:
```kotlin
group = "com.brightly"
version = "1.0.1"

publishing {
    repositories {
        maven {
            // ... repo config
        }
    }
}

// No manual publications configuration needed
// Kotlin Multiplatform plugin auto-generates them
```

---

# Quick Reference

## Essential Commands

### Using Library (Consumer)
```bash
# Sync dependencies
./gradlew --refresh-dependencies

# Build Android
./gradlew :composeApp:assembleDebug

# Build iOS
./gradlew :composeApp:compileKotlinIosSimulatorArm64

# Check dependencies
./gradlew :composeApp:dependencies | grep kmp-room-core
```

### Publishing Library
```bash
# Publish new version
./gradlew :kmp-room-core:clean
./gradlew :kmp-room-core:publish

# Tag version
git tag v1.0.2
git push origin v1.0.2
```

## File Locations

### Consumer App
- Repository config: `settings.gradle.kts`
- Dependency: `composeApp/build.gradle.kts`
- KSP config: `composeApp/build.gradle.kts`
- Credentials: `~/.gradle/gradle.properties`

### Library
- Version: `kmp-room-core/build.gradle.kts`
- Source code: `kmp-room-core/src/`
- Publish config: `kmp-room-core/build.gradle.kts`

## URLs

- **Published library:** https://github.com/neeraj-brightly12/KMPDatabasePOC/packages
- **Create token:** https://github.com/settings/tokens/new
- **Repository:** https://github.com/neeraj-brightly12/KMPDatabasePOC

---

# Summary

## Using Library in Your App:
1. ✅ Create GitHub token (read:packages)
2. ✅ Save credentials
3. ✅ Add repository to settings.gradle.kts
4. ✅ Add library dependency
5. ✅ Enable KSP plugin
6. ✅ Add KSP dependencies
7. ✅ Define your entities/DAOs/database
8. ✅ Build and run

## Publishing New Version:
1. ✅ Make changes to library code
2. ✅ Update version number
3. ✅ Clean build
4. ✅ Publish: `./gradlew :kmp-room-core:publish`
5. ✅ Tag and push
6. ✅ Update consuming apps

## KSP Requirement:
- ✅ **YES, always required** in consumer apps
- ✅ Generates DAO implementations
- ✅ Generates database constructors
- ✅ Validates queries at compile-time

---

**You're all set!** 🎉

For questions or issues, refer to the Troubleshooting section above.