# Build Configuration Reference

Quick reference for correct `composeApp/build.gradle.kts` configuration when using published `kmp-room-core` library.

---

## ✅ Correct Configuration (Current Implementation)

```kotlin
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.ksp) // ✅ REQUIRED - DO NOT COMMENT OUT
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.activity.compose)
        }
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            // ✅ Using published library from GitHub Packages
            implementation("com.brightly:kmp-room-core:1.0.2")

            // ❌ Commented out local project (only use during development)
            // implementation(project(":kmp-room-core"))
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "com.brightly.kmpdatabasepoc"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.brightly.kmpdatabasepoc"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    // ✅ REQUIRED - KSP dependencies for YOUR app's Room entities/DAOs
    // DO NOT COMMENT OUT - These process YOUR @Entity, @Dao, @Database annotations
    add("kspAndroid", "androidx.room:room-compiler:2.7.0")
    add("kspIosArm64", "androidx.room:room-compiler:2.7.0")
    add("kspIosSimulatorArm64", "androidx.room:room-compiler:2.7.0")

    debugImplementation(libs.compose.uiTooling)
}
```

---

## ❌ Common Mistakes

### Mistake 1: Commenting Out KSP Plugin

**Wrong:**
```kotlin
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
//  alias(libs.plugins.ksp) // ❌ Commented out
}
```

**Result:** Build fails with errors about missing Room implementation classes.

---

### Mistake 2: Commenting Out KSP Dependencies

**Wrong:**
```kotlin
dependencies {
//  add("kspAndroid", "androidx.room:room-compiler:2.7.0")
//  add("kspIosArm64", "androidx.room:room-compiler:2.7.0")
//  add("kspIosSimulatorArm64", "androidx.room:room-compiler:2.7.0")

    debugImplementation(libs.compose.uiTooling)
}
```

**Result:** Room annotations in your app are not processed, build fails.

---

### Mistake 3: Using Both Local and Published

**Wrong:**
```kotlin
commonMain.dependencies {
    implementation("com.brightly:kmp-room-core:1.0.2")
    implementation(project(":kmp-room-core")) // ❌ Conflict
}
```

**Result:** Duplicate classes, build errors.

**Choose one:**
- **Development:** `implementation(project(":kmp-room-core"))`
- **Production:** `implementation("com.brightly:kmp-room-core:1.0.2")`

---

## Understanding the Configuration

### What Each Part Does

#### 1. KSP Plugin
```kotlin
alias(libs.plugins.ksp)
```
**Purpose:** Enables Kotlin Symbol Processing for annotation processing
**Processes:** Your `@Entity`, `@Dao`, `@Database` annotations
**Required:** Always (when you have Room entities)

#### 2. Library Dependency
```kotlin
implementation("com.brightly:kmp-room-core:1.0.2")
```
**Purpose:** Provides platform abstractions and utilities
**Contains:** Factory base classes, utilities, migrations helpers
**Does NOT contain:** Your app's entities or DAOs

#### 3. KSP Dependencies
```kotlin
add("kspAndroid", "androidx.room:room-compiler:2.7.0")
add("kspIosArm64", "androidx.room:room-compiler:2.7.0")
add("kspIosSimulatorArm64", "androidx.room:room-compiler:2.7.0")
```
**Purpose:** Room compiler for each platform
**Processes:** Room annotations in YOUR code
**Generates:** DAO implementations, Database implementations
**Required:** Always (for each target platform)

---

## Why All Three Are Required

### The Complete Picture

```
Your App's Code                    Library Code
┌─────────────────┐               ┌──────────────────┐
│ @Entity         │               │ AndroidDatabase  │
│ UserEntity      │               │ Factory          │
│                 │               │                  │
│ @Dao            │               │ IosDatabaseFactory│
│ UserDao         │               │                  │
│                 │               │ DatabaseConfig   │
│ @Database       │               │                  │
│ AppDatabase     │               │ Utilities        │
└────────┬────────┘               └─────────┬────────┘
         │                                  │
         │ Needs KSP                        │ No KSP needed
         ▼                                  │
  ┌──────────────┐                         │
  │ KSP Plugin + │                         │
  │ Dependencies │                         │
  └──────┬───────┘                         │
         │                                  │
         ▼                                  ▼
  Generated Code                    Used Directly
  - UserDao_Impl                    - Factory classes
  - AppDatabase_Impl                - Config classes
  - Query Validators                - Utilities
```

**Key Points:**
1. Your entities/DAOs need KSP to generate implementations
2. Library classes are already compiled (no KSP needed)
3. Both are required: Library for infrastructure + KSP for your code

---

## Version Compatibility

```kotlin
// Ensure these versions match
Room: 2.7.0
KSP: 2.1.20-1.0.31
Kotlin: 2.1.20
Library: 1.0.2+
```

---

## Verification Checklist

After configuring, verify:

- [ ] ✅ KSP plugin is enabled (not commented)
- [ ] ✅ All three KSP dependencies present (Android, iOS targets)
- [ ] ✅ Library dependency added (`com.brightly:kmp-room-core:1.0.2`)
- [ ] ✅ Local project dependency commented out (if using published)
- [ ] ✅ GitHub credentials configured (for published library)
- [ ] ✅ Project synced: `./gradlew clean build`

---

## Quick Test

To verify configuration is correct:

```bash
# Clean build
./gradlew clean

# Build and check for KSP generated files
./gradlew :composeApp:kspCommonMainKotlinMetadata

# Should generate files in:
# composeApp/build/generated/ksp/metadata/commonMain/kotlin/
```

If successful, you should see generated files like:
- `UserDao_Impl.kt`
- `ProductDao_Impl.kt`
- `AppDatabase_Impl.kt`

---

## Related Documentation

- [CURRENT_IMPLEMENTATION_GUIDE.md](./CURRENT_IMPLEMENTATION_GUIDE.md) - Complete setup guide
- [kmp-room-core/README.md](./kmp-room-core/README.md) - Library documentation
- [ARCHITECTURE_DOCUMENT.md](./ARCHITECTURE_DOCUMENT.md) - Architecture overview

---

**Last Updated:** March 2026
**Library Version:** kmp-room-core v1.0.2+