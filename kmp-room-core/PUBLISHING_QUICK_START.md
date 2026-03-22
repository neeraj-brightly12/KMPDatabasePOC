# Quick Start: Publishing & Consuming kmp-room-core

**Current Version:** 1.0.1 ✅ Published
**Repository:** https://github.com/neeraj-brightly12/KMPDatabasePOC

## Publish to GitHub Packages (30 seconds)

### 1. Get GitHub Token
- Go to: https://github.com/settings/tokens/new
- Select: `write:packages`, `read:packages`, `repo` (if private)
- Generate and copy token

### 2. Update Repository URL and Version
Edit `kmp-room-core/build.gradle.kts`:
```kotlin
group = "com.brightly"
version = "1.0.2"  // Update version as needed

publishing {
    repositories {
        maven {
            url = uri("https://maven.pkg.github.com/neeraj-brightly12/KMPDatabasePOC")
            // ...
        }
    }
}
```

### 3. Publish
```bash
export GITHUB_ACTOR=your-github-username
export GITHUB_TOKEN=your-token-here
./gradlew :kmp-room-core:publish
```

Done! Verify at: https://github.com/YOUR_USERNAME/KMPDatabasePOC/packages

---

## Use in Another Project (30 seconds)

### 1. Add Repository
In `settings.gradle.kts`:
```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven {
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

### 2. Add Dependency
In `build.gradle.kts`:
```kotlin
kotlin {
    sourceSets {
        commonMain.dependencies {
            // Single dependency - works for all platforms!
            implementation("com.brightly:kmp-room-core:1.0.1")
        }
    }
}
```

### 3. Set Credentials
```bash
export GITHUB_ACTOR=your-github-username
export GITHUB_TOKEN=your-token-here
```

OR add to `~/.gradle/gradle.properties`:
```properties
gpr.user=your-github-username
gpr.token=your-token-here
```

### 4. Sync Gradle
```bash
./gradlew build
```

Done!

---

## Update Version

Edit `kmp-room-core/build.gradle.kts`:
```kotlin
group = "com.brightly"
version = "1.0.2"  // Change this
```

Then republish all artifacts:
```bash
./gradlew :kmp-room-core:publish
```

This publishes all 4 artifacts:
- kotlinMultiplatform (root metadata)
- androidRelease
- iosArm64
- iosSimulatorArm64

---

For detailed documentation, see [PUBLISHING.md](PUBLISHING.md)