# Complete Library Development Guide for Dev Team

**Document for:** Development Team
**Last Updated:** March 2026
**Library Example:** kmp-room-core v1.0.1
**Repository:** https://github.com/neeraj-brightly12/KMPDatabasePOC

---

## Table of Contents

1. [Overview](#overview)
2. [Creating a KMP Library from Scratch](#part-1-creating-a-kmp-library-from-scratch)
3. [Publishing to GitHub Packages](#part-2-publishing-to-github-packages)
4. [Using Library in Multiple Apps](#part-3-using-library-in-multiple-apps)
5. [Updating Library Versions](#part-4-updating-library-versions)
6. [GitHub Token Setup](#part-5-github-token-setup)
7. [Complete Example](#part-6-complete-example)
8. [Team Workflow](#part-7-team-workflow)
9. [Best Practices](#part-8-best-practices)
10. [Troubleshooting](#troubleshooting)

---

## Overview

### What This Guide Covers

This is a complete guide for:
- ✅ Creating Kotlin Multiplatform (KMP) libraries
- ✅ Publishing libraries to GitHub Packages
- ✅ Consuming libraries in multiple apps
- ✅ Versioning and updating libraries
- ✅ Team collaboration workflows

### Example Library: kmp-room-core

**What it provides:**
- Room Database infrastructure for KMP
- Platform-specific factory classes (Android & iOS)
- Database utilities and helpers
- Migration support

**Published at:** https://maven.pkg.github.com/neeraj-brightly12/KMPDatabasePOC

---

# Part 1: Creating a KMP Library from Scratch

## Step 1.1: Create Library Module

### Using Android Studio / IntelliJ IDEA

1. **File → New → New Module**
2. Select: **Kotlin Multiplatform Library**
3. Name: `kmp-room-core` (or your library name)
4. Package: `com.brightly.kmp.room.core`
5. Click **Finish**

### Manual Creation

Create directory structure:
```
kmp-room-core/
├── build.gradle.kts
└── src/
    ├── commonMain/
    │   └── kotlin/
    │       └── com/brightly/kmp/room/core/
    ├── androidMain/
    │   └── kotlin/
    │       └── com/brightly/kmp/room/core/
    └── iosMain/
        └── kotlin/
            └── com/brightly/kmp/room/core/
```

---

## Step 1.2: Configure Library Build Script

**File:** `kmp-room-core/build.gradle.kts`

```kotlin
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    id("maven-publish")  // For publishing
}

// Library identity
group = "com.brightly"  // Your organization/group
version = "1.0.0"       // Library version

kotlin {
    // Android target
    androidTarget {
        publishLibraryVariants("release")  // Publish release variant only
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    // iOS targets
    listOf(
        iosArm64(),           // Physical devices
        iosSimulatorArm64()   // Simulator
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "KmpRoomCore"
            isStatic = true
        }
    }

    // Dependencies
    sourceSets {
        commonMain.dependencies {
            // Your library dependencies
            api("androidx.room:room-runtime:2.7.0")
            api("androidx.sqlite:sqlite-bundled:2.4.0")
            api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
        }

        androidMain.dependencies {
            api("androidx.room:room-runtime:2.7.0")
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}

// Android configuration
android {
    namespace = "com.brightly.kmp.room.core"
    compileSdk = 36

    defaultConfig {
        minSdk = 24
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

// Publishing configuration
publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/YOUR_USERNAME/YOUR_REPO")
            credentials {
                username = System.getenv("GITHUB_ACTOR") ?:
                          findProperty("gpr.user") as String?
                password = System.getenv("GITHUB_TOKEN") ?:
                          findProperty("gpr.token") as String?
            }
        }
    }
}
```

**Update these values:**
- `group = "com.brightly"` → Your group ID
- `baseName = "KmpRoomCore"` → Your framework name
- `namespace = "com.brightly.kmp.room.core"` → Your package
- `url = uri("...")` → Your repository URL

---

## Step 1.3: Add to Root Settings

**File:** `settings.gradle.kts`

```kotlin
include(":kmp-room-core")
```

---

## Step 1.4: Write Library Code

### Common Code (commonMain)

**File:** `src/commonMain/kotlin/com/brightly/kmp/room/core/DatabaseConfig.kt`

```kotlin
package com.brightly.kmp.room.core

import androidx.room.migration.Migration

data class DatabaseConfig(
    val name: String,
    val version: Int = 1,
    val enableLogging: Boolean = false,
    val migrations: List<Migration> = emptyList()
)
```

### Android-Specific Code (androidMain)

**File:** `src/androidMain/kotlin/com/brightly/kmp/room/core/android/AndroidDatabaseFactory.kt`

```kotlin
package com.brightly.kmp.room.core.android

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import com.brightly.kmp.room.core.DatabaseConfig

abstract class AndroidDatabaseFactory<T : RoomDatabase>(
    protected val context: Context
) {
    protected fun buildDatabase(
        klass: Class<T>,
        config: DatabaseConfig
    ): T {
        return Room.databaseBuilder(
            context,
            klass,
            config.name
        ).apply {
            config.migrations.forEach { addMigrations(it) }
            if (config.enableLogging) {
                setQueryCallback({ sqlQuery, _ ->
                    println("SQL: $sqlQuery")
                }, { it.run() })
            }
        }.build()
    }

    abstract fun createDatabase(
        name: String,
        migrations: List<Migration>
    ): T
}
```

### iOS-Specific Code (iosMain)

**File:** `src/iosMain/kotlin/com/brightly/kmp/room/core/ios/IosDatabaseFactory.kt`

```kotlin
package com.brightly.kmp.room.core.ios

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.brightly.kmp.room.core.DatabaseConfig
import platform.Foundation.NSHomeDirectory

abstract class IosDatabaseFactory<T : RoomDatabase> {
    protected fun buildDatabase(config: DatabaseConfig): T {
        val dbFile = NSHomeDirectory() + "/${config.name}"

        return Room.databaseBuilder<T>(
            name = dbFile,
            factory = { getRoomDatabase() }
        ).apply {
            config.migrations.forEach { addMigrations(it) }
            setDriver(BundledSQLiteDriver())
        }.build()
    }

    abstract fun createDatabase(
        name: String,
        migrations: List<Migration>
    ): T

    protected abstract fun getRoomDatabase(): T
}
```

---

## Step 1.5: Test Library Locally

### Build the library
```bash
./gradlew :kmp-room-core:build
```

### Test in same project
```kotlin
// In your app's build.gradle.kts
implementation(project(":kmp-room-core"))
```

### Test locally published version
```bash
# Publish to local Maven repository
./gradlew :kmp-room-core:publishToMavenLocal

# In your app's settings.gradle.kts, add:
repositories {
    mavenLocal()
}

# In your app's build.gradle.kts:
implementation("com.brightly:kmp-room-core:1.0.0")
```

---

# Part 2: Publishing to GitHub Packages

## Step 2.1: Create GitHub Repository

### Option A: New Repository

1. Go to: https://github.com/new
2. Repository name: `KMPDatabasePOC` (or your name)
3. Visibility: **Public** or **Private**
4. Click **Create repository**

### Option B: Use Existing Repository

You can use any existing repository that contains your library code.

---

## Step 2.2: Create GitHub Personal Access Token

### For Publishers (Library Developers)

1. **Go to:** https://github.com/settings/tokens/new
2. **Token name:** `GitHub Packages - Write`
3. **Expiration:** 90 days (recommended) or custom
4. **Select scopes:**
   - ✅ `repo` (Full control of private repositories)
   - ✅ `write:packages` (Upload packages)
   - ✅ `read:packages` (Download packages)
   - ✅ `delete:packages` (Delete packages - optional)
5. Click **Generate token**
6. **COPY THE TOKEN** (starts with `ghp_...`)
   - You won't see it again!
   - Save it securely

### For Consumers (App Developers)

1. **Go to:** https://github.com/settings/tokens/new
2. **Token name:** `GitHub Packages - Read`
3. **Expiration:** 90 days (recommended) or custom
4. **Select scopes:**
   - ✅ `read:packages` (Download packages)
   - ✅ `repo` (if repository is private)
5. Click **Generate token**
6. **COPY THE TOKEN**

---

## Step 2.3: Save Credentials Securely

### Local Machine (For Development)

**File:** `~/.gradle/gradle.properties`

```properties
# GitHub Packages Credentials
gpr.user=your-github-username
gpr.token=ghp_your_personal_access_token_here
```

**Create file if it doesn't exist:**
```bash
# macOS/Linux
touch ~/.gradle/gradle.properties
nano ~/.gradle/gradle.properties

# Windows
notepad %USERPROFILE%\.gradle\gradle.properties
```

**Security:**
- ✅ This file is in your home directory (not project)
- ✅ Won't be committed to Git
- ✅ Each developer has their own file
- ❌ Never commit tokens to repository

---

## Step 2.4: Update Library Configuration

**File:** `kmp-room-core/build.gradle.kts`

Update the publishing section:

```kotlin
publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/neeraj-brightly12/KMPDatabasePOC")
            credentials {
                username = System.getenv("GITHUB_ACTOR") ?:
                          findProperty("gpr.user") as String?
                password = System.getenv("GITHUB_TOKEN") ?:
                          findProperty("gpr.token") as String?
            }
        }
    }
}
```

**Replace:**
- `neeraj-brightly12` → Your GitHub username
- `KMPDatabasePOC` → Your repository name

---

## Step 2.5: Publish Library

### Clean build
```bash
./gradlew :kmp-room-core:clean
```

### Publish all platforms
```bash
./gradlew :kmp-room-core:publish
```

**This command publishes 4 artifacts:**
1. `kmp-room-core-1.0.0.module` (Root metadata)
2. `kmp-room-core-android-1.0.0.aar` (Android library)
3. `kmp-room-core-iosarm64-1.0.0.klib` (iOS device)
4. `kmp-room-core-iossimulatorarm64-1.0.0.klib` (iOS simulator)

**Expected output:**
```
> Task :kmp-room-core:publishAndroidReleasePublicationToGitHubPackagesRepository
> Task :kmp-room-core:publishIosArm64PublicationToGitHubPackagesRepository
> Task :kmp-room-core:publishIosSimulatorArm64PublicationToGitHubPackagesRepository
> Task :kmp-room-core:publishKotlinMultiplatformPublicationToGitHubPackagesRepository

BUILD SUCCESSFUL in 2m 30s
```

---

## Step 2.6: Verify Publication

### Check GitHub Packages

Visit:
```
https://github.com/YOUR_USERNAME/YOUR_REPO/packages
```

You should see:
```
📦 kmp-room-core
   Version: 1.0.0
   Published: just now
```

### Test Download

```bash
curl -H "Authorization: token YOUR_TOKEN" \
  https://maven.pkg.github.com/YOUR_USERNAME/YOUR_REPO/com/brightly/kmp-room-core/1.0.0/kmp-room-core-1.0.0.module
```

Should return HTTP 200 and the module metadata.

---

## Step 2.7: Create Git Tag (Recommended)

```bash
# Commit any changes
git add kmp-room-core/build.gradle.kts
git commit -m "Release kmp-room-core v1.0.0"

# Create annotated tag
git tag -a v1.0.0 -m "Release version 1.0.0"

# Push commits and tags
git push origin main
git push origin v1.0.0
```

**Benefits:**
- ✅ Track releases in Git history
- ✅ Can trigger automated publishing with GitHub Actions
- ✅ Easy to see what code is in each version

---

# Part 3: Using Library in Multiple Apps

## Step 3.1: Configure App Repository Access

### For App #1 (Example: MyApp)

**File:** `MyApp/settings.gradle.kts`

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
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
```

### For App #2 (Example: AnotherApp)

**Same configuration as App #1** - just copy the maven block to `settings.gradle.kts`

### For App #3, #4, #5...

**Same configuration** - Every app that wants to use the library adds the same repository configuration.

---

## Step 3.2: Add Library Dependency

### Enable KSP Plugin

**File:** `gradle/libs.versions.toml`

```toml
[versions]
ksp = "2.1.20-1.0.31"

[plugins]
ksp = { id = "com.google.devtools.ksp", version.ref = "ksp" }
```

### Configure App Module

**File:** `app/build.gradle.kts` (or `composeApp/build.gradle.kts`)

```kotlin
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.ksp)  // ⚠️ REQUIRED for Room
}

kotlin {
    androidTarget()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            // ✨ Single dependency - works for all platforms!
            implementation("com.brightly:kmp-room-core:1.0.0")

            // Your other dependencies...
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

**Key Points:**
- ✅ Single dependency: `com.brightly:kmp-room-core:1.0.0`
- ✅ Gradle automatically resolves correct platform variant
- ⚠️ **KSP is REQUIRED** (explained later)

---

## Step 3.3: Sync and Verify

### Sync Gradle
```bash
./gradlew --refresh-dependencies
```

### Verify library downloaded
```bash
./gradlew :app:dependencies --configuration commonMainImplementationDependenciesMetadata | grep kmp-room-core
```

**Expected output:**
```
+--- com.brightly:kmp-room-core:1.0.0
     +--- androidx.room:room-runtime:2.7.0
     +--- androidx.sqlite:sqlite-bundled:2.4.0
     +--- org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0
```

---

## Step 3.4: Each Team Member Setup

### What Each Developer Needs:

1. **GitHub Personal Access Token**
   - Go to: https://github.com/settings/tokens/new
   - Scopes: `read:packages` (+ `repo` if private)
   - Copy token

2. **Save Credentials**
   ```bash
   # Edit ~/.gradle/gradle.properties
   gpr.user=their-github-username
   gpr.token=their-personal-token
   ```

3. **Clone Project and Build**
   ```bash
   git clone https://github.com/team/MyApp.git
   cd MyApp
   ./gradlew build
   ```

**Note:** Each developer creates their own token. Tokens are NOT shared!

---

# Part 4: Updating Library Versions

## Step 4.1: Make Changes to Library

Edit files in `kmp-room-core/src/`:

```
kmp-room-core/
└── src/
    ├── commonMain/kotlin/
    │   └── (Add new utilities, fix bugs, etc.)
    ├── androidMain/kotlin/
    │   └── (Android-specific changes)
    └── iosMain/kotlin/
        └── (iOS-specific changes)
```

**Example changes:**
- Add new utility functions
- Fix bugs
- Add new features
- Update dependencies
- Improve performance

---

## Step 4.2: Update Version Number

**File:** `kmp-room-core/build.gradle.kts`

```kotlin
group = "com.brightly"
version = "1.0.1"  // ← Increment version (was 1.0.0)

publishing {
    // ... rest of config
}
```

### Version Numbering (Semantic Versioning)

**Format:** `MAJOR.MINOR.PATCH`

- **MAJOR (2.0.0):** Breaking changes (incompatible API changes)
  - Example: Renaming public functions, removing features

- **MINOR (1.1.0):** New features (backward compatible)
  - Example: Adding new functions, new optional parameters

- **PATCH (1.0.1):** Bug fixes (backward compatible)
  - Example: Fixing crashes, performance improvements

**Examples:**
- `1.0.0` → `1.0.1` (bug fix)
- `1.0.1` → `1.1.0` (new feature)
- `1.1.0` → `2.0.0` (breaking change)

---

## Step 4.3: Test Changes Locally

### Option A: Test in same project
```bash
# In your test app, temporarily use:
implementation(project(":kmp-room-core"))

# Test thoroughly
./gradlew :app:build
```

### Option B: Publish to local Maven
```bash
# Publish to ~/.m2/repository
./gradlew :kmp-room-core:publishToMavenLocal

# In test app's settings.gradle.kts:
repositories {
    mavenLocal()  // Add this temporarily
    // ... other repos
}

# Test
./gradlew :app:build
```

**If tests pass, proceed to publish.**

---

## Step 4.4: Publish New Version

```bash
# Clean previous build
./gradlew :kmp-room-core:clean

# Publish new version
./gradlew :kmp-room-core:publish
```

**Expected output:**
```
> Task :kmp-room-core:publishAndroidReleasePublicationToGitHubPackagesRepository
> Task :kmp-room-core:publishIosArm64PublicationToGitHubPackagesRepository
> Task :kmp-room-core:publishIosSimulatorArm64PublicationToGitHubPackagesRepository
> Task :kmp-room-core:publishKotlinMultiplatformPublicationToGitHubPackagesRepository

BUILD SUCCESSFUL in 2m 45s
```

---

## Step 4.5: Create Git Tag for New Version

```bash
# Commit version bump
git add kmp-room-core/build.gradle.kts
git commit -m "Release kmp-room-core v1.0.1"

# Create tag
git tag -a v1.0.1 -m "Release version 1.0.1 - Bug fixes"

# Push
git push origin main
git push origin v1.0.1
```

---

## Step 4.6: Update Apps to New Version

### In Each Consuming App

**File:** `app/build.gradle.kts`

```kotlin
commonMain.dependencies {
    implementation("com.brightly:kmp-room-core:1.0.1")  // ← Update version
}
```

### Sync and Rebuild

```bash
./gradlew --refresh-dependencies
./gradlew :app:build
```

### Gradual Rollout (Optional)

You can update apps gradually:
- **App A:** Still using `1.0.0`
- **App B:** Updated to `1.0.1`
- **App C:** Still using `1.0.0`

Both versions coexist on GitHub Packages. Each app chooses its version.

---

## Complete Publishing Workflow Summary

```bash
# 1. Make changes to library code
# 2. Update version in kmp-room-core/build.gradle.kts
version = "1.0.1"

# 3. Clean and publish
./gradlew :kmp-room-core:clean
./gradlew :kmp-room-core:publish

# 4. Create Git tag
git add kmp-room-core/build.gradle.kts
git commit -m "Release v1.0.1"
git tag -a v1.0.1 -m "Release v1.0.1"
git push origin main
git push origin v1.0.1

# 5. Update consuming apps
# Change: implementation("com.brightly:kmp-room-core:1.0.1")

# 6. Verify on GitHub
# Visit: https://github.com/YOUR_USERNAME/YOUR_REPO/packages
```

---

# Part 5: GitHub Token Setup

## For Library Publishers

### Required Permissions

```
✅ repo - Full control of private repositories
✅ write:packages - Upload packages to GitHub Package Registry
✅ read:packages - Download packages from GitHub Package Registry
✅ delete:packages - Delete packages (optional)
```

### Create Token

1. Go to: https://github.com/settings/tokens/new
2. Name: `Library Publishing - Write Access`
3. Expiration: 90 days (or custom)
4. Select permissions above
5. Generate token
6. Copy: `ghp_xxxxxxxxxxxxxxxxxxxx`

### Save Token

**File:** `~/.gradle/gradle.properties`

```properties
gpr.user=your-github-username
gpr.token=ghp_your_write_token_here
```

---

## For Library Consumers

### Required Permissions

```
✅ read:packages - Download packages from GitHub Package Registry
✅ repo - Full control (only if repository is private)
```

### Create Token

1. Go to: https://github.com/settings/tokens/new
2. Name: `Library Consumer - Read Access`
3. Expiration: 90 days (or custom)
4. Select permissions above
5. Generate token
6. Copy: `ghp_yyyyyyyyyyyyyyyyyyyy`

### Save Token

**File:** `~/.gradle/gradle.properties`

```properties
gpr.user=your-github-username
gpr.token=ghp_your_read_token_here
```

---

## Token Security Best Practices

### DO ✅

- ✅ Create separate tokens for different purposes (publish vs consume)
- ✅ Set expiration dates (90 days recommended)
- ✅ Store in `~/.gradle/gradle.properties` (not in project)
- ✅ Use environment variables in CI/CD
- ✅ Rotate tokens regularly
- ✅ Revoke tokens you're not using

### DON'T ❌

- ❌ Commit tokens to Git
- ❌ Share your personal token with team members
- ❌ Use tokens without expiration
- ❌ Store tokens in project files
- ❌ Reuse same token for everything

---

## Environment Variables (CI/CD)

### GitHub Actions

```yaml
env:
  GITHUB_ACTOR: ${{ github.actor }}
  GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}
```

### GitLab CI

```yaml
variables:
  GITHUB_ACTOR: $CI_PROJECT_NAME
  GITHUB_TOKEN: $GITHUB_TOKEN
```

### Jenkins

```groovy
environment {
    GITHUB_ACTOR = credentials('github-username')
    GITHUB_TOKEN = credentials('github-token')
}
```

---

## Token Troubleshooting

### Issue: 401 Unauthorized

**Cause:** Token invalid or expired

**Solution:**
1. Generate new token
2. Update `~/.gradle/gradle.properties`
3. Try again:
   ```bash
   ./gradlew --refresh-dependencies
   ```

### Issue: 403 Forbidden

**Cause:** Token lacks required permissions

**Solution:**
1. Check token permissions at: https://github.com/settings/tokens
2. Ensure `write:packages` (publisher) or `read:packages` (consumer)
3. Regenerate token with correct permissions

### Issue: Token not found

**Cause:** `~/.gradle/gradle.properties` doesn't exist or is empty

**Solution:**
```bash
# Create file
touch ~/.gradle/gradle.properties

# Add credentials
echo "gpr.user=your-username" >> ~/.gradle/gradle.properties
echo "gpr.token=your-token" >> ~/.gradle/gradle.properties
```

---

# Part 6: Complete Example

## Example: Creating & Using a KMP Utility Library

Let's create a complete example from scratch.

---

## Step 1: Create Library "kmp-utilities"

### Directory Structure

```
kmp-utilities/
├── build.gradle.kts
└── src/
    ├── commonMain/kotlin/com/company/utils/
    │   ├── StringUtils.kt
    │   └── DateUtils.kt
    ├── androidMain/kotlin/com/company/utils/
    │   └── PlatformUtils.android.kt
    └── iosMain/kotlin/com/company/utils/
        └── PlatformUtils.ios.kt
```

### build.gradle.kts

```kotlin
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    id("maven-publish")
}

group = "com.company"
version = "1.0.0"

kotlin {
    androidTarget {
        publishLibraryVariants("release")
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(iosArm64(), iosSimulatorArm64()).forEach {
        it.binaries.framework {
            baseName = "KmpUtilities"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.5.0")
        }
    }
}

android {
    namespace = "com.company.utils"
    compileSdk = 36
    defaultConfig {
        minSdk = 24
    }
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/mycompany/kmp-utilities")
            credentials {
                username = System.getenv("GITHUB_ACTOR") ?:
                          findProperty("gpr.user") as String?
                password = System.getenv("GITHUB_TOKEN") ?:
                          findProperty("gpr.token") as String?
            }
        }
    }
}
```

### StringUtils.kt (commonMain)

```kotlin
package com.company.utils

object StringUtils {
    fun capitalize(text: String): String {
        return text.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase() else it.toString()
        }
    }

    fun truncate(text: String, maxLength: Int): String {
        return if (text.length > maxLength) {
            "${text.take(maxLength)}..."
        } else {
            text
        }
    }
}
```

### PlatformUtils.kt (expect/actual)

**commonMain:**
```kotlin
package com.company.utils

expect object PlatformUtils {
    fun getPlatformName(): String
    fun getDeviceModel(): String
}
```

**androidMain:**
```kotlin
package com.company.utils

import android.os.Build

actual object PlatformUtils {
    actual fun getPlatformName(): String = "Android"
    actual fun getDeviceModel(): String = "${Build.MANUFACTURER} ${Build.MODEL}"
}
```

**iosMain:**
```kotlin
package com.company.utils

import platform.UIKit.UIDevice

actual object PlatformUtils {
    actual fun getPlatformName(): String = "iOS"
    actual fun getDeviceModel(): String = UIDevice.currentDevice.model
}
```

---

## Step 2: Publish Library

```bash
# Set credentials
export GITHUB_ACTOR=mycompany
export GITHUB_TOKEN=ghp_xxxxxxxxxxxxx

# Publish
./gradlew :kmp-utilities:clean
./gradlew :kmp-utilities:publish

# Tag
git tag v1.0.0
git push origin v1.0.0
```

---

## Step 3: Use in App #1 - "MyShoppingApp"

### settings.gradle.kts

```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://maven.pkg.github.com/mycompany/kmp-utilities")
            credentials {
                username = providers.gradleProperty("gpr.user").orNull
                password = providers.gradleProperty("gpr.token").orNull
            }
        }
    }
}
```

### app/build.gradle.kts

```kotlin
commonMain.dependencies {
    implementation("com.company:kmp-utilities:1.0.0")
}
```

### Use in code

```kotlin
// MyShoppingApp/src/commonMain/kotlin/com/shop/ui/ProductScreen.kt
import com.company.utils.StringUtils
import com.company.utils.PlatformUtils

@Composable
fun ProductScreen() {
    val productName = "amazing product"
    val displayName = StringUtils.capitalize(productName)  // "Amazing product"

    val platform = PlatformUtils.getPlatformName()  // "Android" or "iOS"

    Text("$displayName on $platform")
}
```

---

## Step 4: Use in App #2 - "MyNewsApp"

### settings.gradle.kts

```kotlin
// Same repository configuration as MyShoppingApp
maven {
    url = uri("https://maven.pkg.github.com/mycompany/kmp-utilities")
    credentials {
        username = providers.gradleProperty("gpr.user").orNull
        password = providers.gradleProperty("gpr.token").orNull
    }
}
```

### app/build.gradle.kts

```kotlin
commonMain.dependencies {
    implementation("com.company:kmp-utilities:1.0.0")
}
```

### Use in code

```kotlin
// MyNewsApp/src/commonMain/kotlin/com/news/ui/ArticleScreen.kt
import com.company.utils.StringUtils

@Composable
fun ArticleScreen(article: Article) {
    val shortTitle = StringUtils.truncate(article.title, maxLength = 50)
    Text(shortTitle)
}
```

---

## Step 5: Update Library to v1.1.0

### Add new feature

**File:** `kmp-utilities/src/commonMain/kotlin/com/company/utils/NumberUtils.kt`

```kotlin
package com.company.utils

object NumberUtils {
    fun formatCurrency(amount: Double): String {
        return "$%.2f".format(amount)
    }
}
```

### Update version

```kotlin
// kmp-utilities/build.gradle.kts
version = "1.1.0"  // Changed from 1.0.0
```

### Publish

```bash
./gradlew :kmp-utilities:clean
./gradlew :kmp-utilities:publish
git tag v1.1.0
git push origin v1.1.0
```

---

## Step 6: Update MyShoppingApp to v1.1.0

```kotlin
// MyShoppingApp/app/build.gradle.kts
commonMain.dependencies {
    implementation("com.company:kmp-utilities:1.1.0")  // Updated
}
```

```kotlin
// Use new feature
import com.company.utils.NumberUtils

val price = 19.99
val formatted = NumberUtils.formatCurrency(price)  // "$19.99"
```

---

## Step 7: MyNewsApp Stays on v1.0.0

```kotlin
// MyNewsApp/app/build.gradle.kts
commonMain.dependencies {
    implementation("com.company:kmp-utilities:1.0.0")  // Still old version
}
```

**This is fine!** Both versions coexist. Update when ready.

---

## Complete Example Summary

```
Library: kmp-utilities
├── v1.0.0 (Initial release)
│   └── StringUtils, PlatformUtils
└── v1.1.0 (New feature)
    └── Added NumberUtils

Apps:
├── MyShoppingApp
│   └── Uses v1.1.0 (latest)
└── MyNewsApp
    └── Uses v1.0.0 (stable, will update later)

Both apps work perfectly with their chosen versions!
```

---

# Part 7: Team Workflow

## Team Roles

### Library Maintainer (1-2 people)

**Responsibilities:**
- Create and maintain library code
- Review pull requests
- Publish new versions
- Manage GitHub releases
- Update documentation

**Required:**
- GitHub token with `write:packages` permission
- Write access to repository

---

### Library Consumer (All developers)

**Responsibilities:**
- Use library in their apps
- Report bugs and request features
- Test new versions

**Required:**
- GitHub token with `read:packages` permission
- Read access to repository

---

## Workflow Scenarios

### Scenario 1: Adding New Feature to Library

**Developer A (Maintainer):**

1. Create feature branch
   ```bash
   git checkout -b feature/add-date-utils
   ```

2. Implement feature
   ```kotlin
   // Add code to library
   ```

3. Test locally
   ```bash
   ./gradlew :kmp-room-core:publishToMavenLocal
   # Test in sample app
   ```

4. Create pull request
5. After review, merge to main
6. Update version and publish
   ```bash
   # Update version to 1.1.0
   ./gradlew :kmp-room-core:publish
   git tag v1.1.0
   git push origin v1.1.0
   ```

7. Notify team
   ```
   📢 New library version available!

   Version: 1.1.0
   Changes:
   - Added DateUtils with new formatting functions

   To update:
   implementation("com.brightly:kmp-room-core:1.1.0")
   ```

---

### Scenario 2: Using Library in New App

**Developer B (Consumer):**

1. Get GitHub token (if don't have one)
2. Save credentials
   ```properties
   gpr.user=developer-b
   gpr.token=ghp_their_token
   ```

3. Add repository to app
   ```kotlin
   // settings.gradle.kts
   maven {
       url = uri("https://maven.pkg.github.com/company/library-repo")
       credentials { ... }
   }
   ```

4. Add dependency
   ```kotlin
   implementation("com.brightly:kmp-room-core:1.0.0")
   ```

5. Use library
   ```kotlin
   val db = DatabaseFactory(context).createDatabase()
   ```

---

### Scenario 3: Reporting Bug and Getting Fix

**Developer C finds bug:**

1. Report issue on GitHub
   ```markdown
   Title: Database creation fails on iOS Simulator

   Steps to reproduce:
   1. Create database on iOS simulator
   2. App crashes

   Expected: Database created successfully
   Actual: Crash with error...
   ```

**Maintainer fixes:**

2. Fix bug in library
3. Publish new version (1.0.1 - patch version)
   ```bash
   # Update version = "1.0.1"
   ./gradlew :kmp-room-core:publish
   git tag v1.0.1
   git push origin v1.0.1
   ```

4. Comment on issue
   ```markdown
   Fixed in v1.0.1!

   Update your dependency:
   implementation("com.brightly:kmp-room-core:1.0.1")
   ```

**Developer C updates:**

5. Update dependency
6. Test fix
7. Close issue

---

## Team Communication

### When Publishing New Version

**Slack/Teams message:**
```
🚀 Library Update: kmp-room-core v1.1.0

What's new:
- ✨ Added migration builder DSL
- 🐛 Fixed iOS database path issue
- 📚 Updated documentation

Breaking changes: None
Upgrade: implementation("com.brightly:kmp-room-core:1.1.0")

Details: https://github.com/company/repo/releases/v1.1.0
```

### Version Compatibility Matrix

Maintain a document:

```markdown
# Library Compatibility

| App | Current Version | Latest Version | Status |
|-----|----------------|----------------|---------|
| MyApp1 | 1.0.0 | 1.1.0 | ⚠️ Update available |
| MyApp2 | 1.1.0 | 1.1.0 | ✅ Up to date |
| MyApp3 | 1.0.1 | 1.1.0 | ⚠️ Update available |
```

---

# Part 8: Best Practices

## Library Design

### DO ✅

1. **Semantic Versioning**
   - MAJOR.MINOR.PATCH
   - Follow semantic versioning rules

2. **Backward Compatibility**
   - Don't break existing APIs in minor/patch versions
   - Deprecate before removing

3. **Documentation**
   - Document all public APIs
   - Provide usage examples
   - Maintain changelog

4. **Testing**
   - Test on all target platforms
   - Test with real apps before publishing
   - Include unit tests

5. **Dependencies**
   - Use `api()` for dependencies consumers need
   - Use `implementation()` for internal dependencies
   - Document required dependencies

### DON'T ❌

1. **Breaking Changes in Patches**
   - Don't change function signatures
   - Don't remove public APIs
   - Don't change behavior drastically

2. **Large Binaries**
   - Keep library size small
   - Avoid unnecessary dependencies

3. **Platform-Specific Code in Common**
   - Use expect/actual pattern correctly
   - Keep platform code in respective sourcesets

---

## Version Management

### Changelog

Maintain `CHANGELOG.md`:

```markdown
# Changelog

## [1.1.0] - 2026-03-17
### Added
- Migration builder DSL for easier migrations
- New utility functions in DatabaseUtils

### Fixed
- iOS database path issue (#15)
- Memory leak in Android factory (#18)

### Changed
- Updated Room to 2.7.0

## [1.0.1] - 2026-03-10
### Fixed
- Crash on iOS Simulator (#12)

## [1.0.0] - 2026-03-01
### Added
- Initial release
- Android and iOS support
- Basic database factory
```

### Git Tags

```bash
# List all versions
git tag

# See what's in a version
git show v1.0.0

# Compare versions
git diff v1.0.0..v1.1.0
```

---

## Security

### Credentials

```properties
# ✅ Good - in ~/.gradle/gradle.properties
gpr.user=username
gpr.token=token

# ❌ Bad - in project gradle.properties
# DON'T DO THIS!

# ❌ Bad - in build.gradle.kts
# DON'T DO THIS!
credentials {
    username = "hardcoded-user"
    password = "hardcoded-token"
}
```

### .gitignore

```gitignore
# Ensure these are ignored
gradle.properties
local.properties
*.keystore
*.jks

# IDE
.idea/
.DS_Store
```

---

## Performance

### Build Speed

```kotlin
// Use implementation instead of api when possible
commonMain.dependencies {
    implementation("...") // ✅ Better for build speed
    api("...")            // Only when consumers need it
}
```

### Binary Size

```kotlin
// Exclude unused dependencies
android {
    packagingOptions {
        exclude("META-INF/*.kotlin_module")
    }
}
```

---

## Documentation

### README.md

```markdown
# Library Name

Brief description

## Installation

\`\`\`kotlin
implementation("com.company:library:1.0.0")
\`\`\`

## Usage

\`\`\`kotlin
val instance = LibraryClass()
instance.doSomething()
\`\`\`

## Documentation

See [full documentation](docs/)

## License

MIT
```

### API Documentation

```kotlin
/**
 * Creates a database instance.
 *
 * @param name Database file name (without path)
 * @param migrations List of migrations to apply
 * @return Configured database instance
 *
 * Example:
 * ```kotlin
 * val db = createDatabase("app.db", listOf(migration1to2))
 * ```
 */
fun createDatabase(name: String, migrations: List<Migration>): AppDatabase
```

---

# Troubleshooting

## Common Issues

### Issue 1: "Could not find library"

**Error:**
```
Could not find com.brightly:kmp-room-core:1.0.0
```

**Causes:**
- Library not published
- Repository not configured
- Credentials wrong/missing
- Version doesn't exist

**Solution:**
```bash
# 1. Verify package exists
# Visit: https://github.com/USERNAME/REPO/packages

# 2. Check credentials
cat ~/.gradle/gradle.properties

# 3. Refresh dependencies
./gradlew --refresh-dependencies

# 4. Check repository configuration in settings.gradle.kts
```

---

### Issue 2: "401 Unauthorized"

**Error:**
```
Received status code 401 from server: Unauthorized
```

**Cause:** Token invalid, expired, or wrong permissions

**Solution:**
```bash
# 1. Generate new token
# https://github.com/settings/tokens/new

# 2. Update ~/.gradle/gradle.properties
gpr.user=your-username
gpr.token=ghp_NEW_TOKEN_HERE

# 3. Clear Gradle cache
rm -rf ~/.gradle/caches/

# 4. Try again
./gradlew --refresh-dependencies
```

---

### Issue 3: "403 Forbidden"

**Error:**
```
Received status code 403 from server: Forbidden
```

**Cause:** Token lacks required permissions

**Solution:**
```bash
# For consumers: Ensure token has read:packages
# For publishers: Ensure token has write:packages

# Regenerate token with correct permissions:
# https://github.com/settings/tokens
```

---

### Issue 4: "Multiple publications with same coordinates"

**Error:**
```
Multiple publications with coordinates 'com.brightly:kmp-room-core:1.0.0'
```

**Cause:** Publishing configuration issue

**Solution:**
```kotlin
// In kmp-room-core/build.gradle.kts
// Use this pattern:

group = "com.brightly"
version = "1.0.0"

publishing {
    repositories {
        maven { /* config */ }
    }
}

// Don't manually create publications
// Kotlin Multiplatform plugin auto-generates them
```

---

### Issue 5: "Object 'AppDatabaseConstructor' is not abstract"

**Error:**
```
Object 'AppDatabaseConstructor' is not abstract and does not implement abstract member
```

**Cause:** KSP not configured or not run yet

**Solution:**
```kotlin
// 1. Ensure KSP plugin is applied
plugins {
    alias(libs.plugins.ksp)
}

// 2. Ensure KSP dependencies added
dependencies {
    add("kspAndroid", "androidx.room:room-compiler:2.7.0")
    add("kspIosArm64", "androidx.room:room-compiler:2.7.0")
    add("kspIosSimulatorArm64", "androidx.room:room-compiler:2.7.0")
}

// 3. Build platform-specific target (not metadata)
./gradlew :app:compileDebugKotlinAndroid
```

---

### Issue 6: "Package not found on GitHub"

**Cause:** Package not visible or deleted

**Solution:**
```bash
# 1. Check package visibility
# Visit: https://github.com/USERNAME/REPO/packages

# 2. If private, ensure token has 'repo' scope

# 3. If deleted, republish:
./gradlew :kmp-room-core:publish
```

---

## Debugging Commands

### Check dependencies
```bash
./gradlew :app:dependencies --configuration commonMainImplementationDependenciesMetadata
```

### Check published versions
```bash
# List all tasks
./gradlew :kmp-room-core:tasks --group publishing

# Dry run
./gradlew :kmp-room-core:publish --dry-run
```

### Test credentials
```bash
# Using curl
curl -H "Authorization: token YOUR_TOKEN" \
  https://api.github.com/user
```

### Clear caches
```bash
# Gradle cache
rm -rf ~/.gradle/caches/

# Android Studio cache
# File → Invalidate Caches → Invalidate and Restart
```

---

## Getting Help

### Internal Team

1. Check this documentation first
2. Ask maintainer on Slack/Teams
3. Check GitHub issues

### External Resources

- **Kotlin Multiplatform:** https://kotlinlang.org/docs/multiplatform.html
- **GitHub Packages:** https://docs.github.com/en/packages
- **Gradle Publishing:** https://docs.gradle.org/current/userguide/publishing_maven.html
- **Semantic Versioning:** https://semver.org/

---

## Summary

### Creating Library
```bash
1. Create module with KMP plugin
2. Write common and platform-specific code
3. Configure build.gradle.kts with group, version, publishing
4. Test locally
```

### Publishing Library
```bash
1. Create GitHub token (write:packages)
2. Save credentials to ~/.gradle/gradle.properties
3. Update version in build.gradle.kts
4. ./gradlew :library:publish
5. git tag version && git push tags
```

### Using Library
```bash
1. Create GitHub token (read:packages)
2. Save credentials to ~/.gradle/gradle.properties
3. Add repository to settings.gradle.kts
4. Add dependency: implementation("group:artifact:version")
5. Enable KSP if library uses Room
6. ./gradlew --refresh-dependencies
```

### Updating Version
```bash
1. Make changes to library
2. Update version number
3. Publish: ./gradlew :library:publish
4. Tag: git tag vX.Y.Z
5. Update consumers to new version
```

---

**End of Document**

This guide covers everything your team needs to create, publish, and consume KMP libraries using GitHub Packages.

For questions or updates, contact the library maintainer.

Happy coding! 🚀