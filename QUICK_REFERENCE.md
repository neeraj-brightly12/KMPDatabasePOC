# Quick Reference Card

## 🔑 One-Time Setup

### 1. Create GitHub Token
https://github.com/settings/tokens/new
- Scopes: `read:packages` (for using library) + `write:packages` (for publishing)

### 2. Save Credentials
Edit `~/.gradle/gradle.properties`:
```properties
gpr.user=neeraj-brightly12
gpr.token=ghp_YOUR_TOKEN_HERE
```

---

## 📦 Using Library in App

### Add to `settings.gradle.kts`:
```kotlin
maven {
    url = uri("https://maven.pkg.github.com/neeraj-brightly12/KMPDatabasePOC")
    credentials {
        username = providers.gradleProperty("gpr.user").orNull
        password = providers.gradleProperty("gpr.token").orNull
    }
}
```

### Add to `composeApp/build.gradle.kts`:
```kotlin
plugins {
    alias(libs.plugins.ksp)  // ⚠️ REQUIRED
}

commonMain.dependencies {
    implementation("com.brightly:kmp-room-core:1.0.1")
}

dependencies {
    add("kspAndroid", "androidx.room:room-compiler:2.7.0")
    add("kspIosArm64", "androidx.room:room-compiler:2.7.0")
    add("kspIosSimulatorArm64", "androidx.room:room-compiler:2.7.0")
}
```

### Build Commands:
```bash
# Android
./gradlew :composeApp:assembleDebug

# iOS
./gradlew :composeApp:compileKotlinIosSimulatorArm64
```

---

## 🚀 Publishing New Version

### 1. Update Version
Edit `kmp-room-core/build.gradle.kts`:
```kotlin
version = "1.0.2"  // Increment
```

### 2. Publish
```bash
./gradlew :kmp-room-core:clean
./gradlew :kmp-room-core:publish
```

### 3. Tag (Optional)
```bash
git tag v1.0.2
git push origin v1.0.2
```

### 4. Update Apps
```kotlin
implementation("com.brightly:kmp-room-core:1.0.2")
```

---

## ⚠️ KSP: Always Required

**YES**, KSP is required in consumer apps because:
- Your app defines entities, DAOs, database
- Room needs to generate implementations
- Library provides infrastructure, not generated code

**Without KSP:**
```
❌ Build Error: Object 'AppDatabaseConstructor' is not abstract
```

**With KSP:**
```
✅ Generates DAO implementations
✅ Generates database constructors
✅ Validates SQL queries
```

---

## 🔍 Verification

```bash
# Check library downloaded
./gradlew :composeApp:dependencies | grep kmp-room-core

# Check KSP generated code
ls composeApp/build/generated/ksp/android/androidDebug/kotlin/
```

---

## 📚 Full Documentation

- **COMPLETE_GUIDE.md** - Full step-by-step guide
- **BUILD_AND_RUN.md** - Build commands
- **PUBLISHED_V1.0.1_STANDARD.md** - Library usage

---

## 🆘 Common Issues

| Issue | Solution |
|-------|----------|
| Library not found | Check credentials in `~/.gradle/gradle.properties` |
| 401 Unauthorized | Regenerate token, update credentials |
| KSP error | Ensure KSP plugin and dependencies added |
| Metadata build fails | Normal, use platform-specific builds |

---

## 📍 Important URLs

- **Packages:** https://github.com/neeraj-brightly12/KMPDatabasePOC/packages
- **Create Token:** https://github.com/settings/tokens/new
- **Repository:** https://github.com/neeraj-brightly12/KMPDatabasePOC