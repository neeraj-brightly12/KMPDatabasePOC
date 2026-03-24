# Library Setup Guide - Quick Reference

This guide shows how to add the `kmp-room-core` library to your KMP project.

---

## 🚀 Quick Setup (3 Steps)

### 1️⃣ Add Repository Configuration

**Edit `settings.gradle.kts` (Recommended):**

```kotlin
pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

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
```

### 2️⃣ Add Dependency

**Edit `composeApp/build.gradle.kts`:**

```kotlin
kotlin {
    sourceSets {
        commonMain.dependencies {
            // Add this line
            implementation("com.brightly:kmp-room-core:1.0.2")
        }
    }
}
```

### 3️⃣ Setup Credentials

**Create/Edit `~/.gradle/gradle.properties`:**

```properties
gpr.user=your-github-username
gpr.token=ghp_your_token_here
```

⚠️ **How to get token:**
1. Go to: https://github.com/settings/tokens
2. "Generate new token (classic)"
3. Name: "Read Packages"
4. Scope: ✅ `read:packages`
5. Generate and copy

---

## 📝 Detailed Explanation

### Why Two Credential Sources?

The configuration checks credentials in this order:

```
1. Environment Variables (GITHUB_ACTOR, GITHUB_TOKEN)
   ↓ (if not found)
2. Gradle Properties (gpr.user, gpr.token)
```

**Benefits:**
- ✅ Works locally (gradle.properties)
- ✅ Works in CI/CD (environment variables)
- ✅ No credentials in code
- ✅ Flexible

---

## 🔧 Configuration Options

### Option 1: gradle.properties (Local Development)

**Location:** `~/.gradle/gradle.properties`

```properties
gpr.user=neeraj-brightly12
gpr.token=ghp_abc123xyz789...
```

**Pros:**
- Easy to set up
- Works automatically
- Persists across projects

**Cons:**
- Not available in CI/CD
- Must setup on each machine

---

### Option 2: Environment Variables (CI/CD or Local)

**For Local Development:**

**macOS/Linux (add to ~/.zshrc or ~/.bashrc):**
```bash
export GITHUB_ACTOR="your-username"
export GITHUB_TOKEN="your-token"
```

Then reload:
```bash
source ~/.zshrc  # or source ~/.bashrc
```

**Windows PowerShell:**
```powershell
$env:GITHUB_ACTOR="your-username"
$env:GITHUB_TOKEN="your-token"
```

**Windows Command Prompt:**
```cmd
set GITHUB_ACTOR=your-username
set GITHUB_TOKEN=your-token
```

**For CI/CD (GitHub Actions):**
```yaml
jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - name: Build
        env:
          GITHUB_ACTOR: ${{ github.actor }}
          GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}
        run: ./gradlew build
```

**Pros:**
- Works in CI/CD
- Easy to change
- Can be project-specific

**Cons:**
- More setup required
- Lost on terminal close (unless in profile)

---

## 🏗️ settings.gradle.kts vs build.gradle.kts

### Use settings.gradle.kts ✅ (Recommended)

**When:**
- Kotlin Multiplatform projects
- Modern Gradle projects
- Need centralized dependency management

**Example:**
```kotlin
// settings.gradle.kts
dependencyResolutionManagement {
    repositories {
        maven {
            url = uri("https://maven.pkg.github.com/...")
            credentials { /* ... */ }
        }
    }
}
```

---

### Use build.gradle.kts (Alternative)

**When:**
- Older projects
- Need per-module configuration
- Compatibility reasons

**Example:**
```kotlin
// Root build.gradle.kts
allprojects {
    repositories {
        maven {
            url = uri("https://maven.pkg.github.com/...")
            credentials { /* ... */ }
        }
    }
}
```

---

## ✅ Verification

### Test Your Setup

```bash
# Clear cache
./gradlew clean

# Build with refresh
./gradlew build --refresh-dependencies
```

**Success indicators:**
```
✅ BUILD SUCCESSFUL
✅ Downloading com.brightly:kmp-room-core:1.0.2
✅ No credential errors
```

**Error indicators:**
```
❌ Could not resolve com.brightly:kmp-room-core:1.0.2
❌ 401 Unauthorized
❌ Could not find com.brightly:kmp-room-core
```

---

## 🔍 Troubleshooting

### Error: "Could not resolve dependency"

**Problem:** Credentials not found or invalid

**Solutions:**

1. **Check gradle.properties exists:**
```bash
cat ~/.gradle/gradle.properties
```

2. **Verify token has correct scope:**
   - Go to https://github.com/settings/tokens
   - Check token has `read:packages` scope

3. **Test with environment variables:**
```bash
export GITHUB_ACTOR="your-username"
export GITHUB_TOKEN="your-token"
./gradlew build --refresh-dependencies
```

4. **Check token is not revoked:**
   - Token might be expired or deleted
   - Create new token

---

### Error: "401 Unauthorized"

**Problem:** Token exists but is invalid

**Solutions:**

1. **Regenerate token:**
   - https://github.com/settings/tokens
   - Delete old token
   - Create new one with `read:packages`

2. **Update gradle.properties:**
```properties
gpr.token=NEW_TOKEN_HERE
```

3. **Clear Gradle cache:**
```bash
rm -rf ~/.gradle/caches
./gradlew build --refresh-dependencies
```

---

### Error: "Repository not found"

**Problem:** URL is incorrect or repository is private

**Check:**

1. **Verify URL:**
```kotlin
url = uri("https://maven.pkg.github.com/neeraj-brightly12/KMPDatabasePOC")
//                                    ↑ username       ↑ repo name
```

2. **Ensure you have access:**
   - Repository must be public OR
   - Your token must have access to private repo

---

### Error: "Package version not found"

**Problem:** Version doesn't exist or not published

**Solutions:**

1. **Check available versions:**
   - Go to https://github.com/neeraj-brightly12/KMPDatabasePOC/packages
   - Verify version exists

2. **Use correct version:**
```kotlin
implementation("com.brightly:kmp-room-core:1.0.2")
//                                             ↑ check this
```

---

## 🎓 Complete Example

**Your settings.gradle.kts should look like:**

```kotlin
pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()

        // GitHub Packages for kmp-room-core
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

rootProject.name = "YourProjectName"
include(":composeApp")
```

**Your composeApp/build.gradle.kts:**

```kotlin
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.ksp)
}

kotlin {
    androidTarget()
    listOf(iosArm64(), iosSimulatorArm64()).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)

            // Room Database dependencies
            implementation(libs.androidx.room.runtime)
            implementation(libs.kotlinx.coroutines.core)

            // kmp-room-core library ✨
            implementation("com.brightly:kmp-room-core:1.0.2")
        }
    }
}

// KSP for Room
dependencies {
    add("kspAndroid", libs.androidx.room.compiler)
    add("kspIosArm64", libs.androidx.room.compiler)
    add("kspIosSimulatorArm64", libs.androidx.room.compiler)
}
```

**Your ~/.gradle/gradle.properties:**

```properties
gpr.user=your-github-username
gpr.token=ghp_your_actual_token_here

# Other gradle properties
kotlin.code.style=official
android.useAndroidX=true
kotlin.native.cacheKind=none
```

---

## 🔐 Security Best Practices

### ✅ DO:
- Store credentials in `~/.gradle/gradle.properties`
- Use environment variables in CI/CD
- Add `.gradle/` to `.gitignore`
- Use tokens with minimal scopes
- Rotate tokens periodically

### ❌ DON'T:
- Commit credentials to git
- Share tokens publicly
- Use personal tokens in production
- Hardcode credentials in code
- Commit gradle.properties

---

## 📚 Additional Resources

- **Complete Documentation**: [COMPLETE_LIBRARY_DOCUMENTATION.md](./COMPLETE_LIBRARY_DOCUMENTATION.md)
- **GitHub Packages Guide**: https://docs.github.com/en/packages
- **Creating Access Tokens**: https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/creating-a-personal-access-token

---

## 🆘 Still Having Issues?

1. **Check GitHub Package page:**
   https://github.com/neeraj-brightly12/KMPDatabasePOC/packages

2. **Verify package exists:**
   - Package name: `kmp-room-core`
   - Group: `com.brightly`
   - Version: `1.0.2`

3. **Test with curl:**
```bash
curl -H "Authorization: token YOUR_TOKEN" \
  https://maven.pkg.github.com/neeraj-brightly12/KMPDatabasePOC/com/brightly/kmp-room-core/1.0.2/kmp-room-core-1.0.2.pom
```

Should return XML if accessible.

4. **Open an issue:**
   https://github.com/neeraj-brightly12/KMPDatabasePOC/issues

---

**Last Updated:** March 24, 2026