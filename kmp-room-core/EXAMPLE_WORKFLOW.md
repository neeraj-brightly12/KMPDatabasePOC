# Complete Workflow Example

This document shows a complete real-world example of publishing and consuming the library.

---

## Scenario

You have:
- **Library project**: `KMPDatabasePOC` (contains `kmp-room-core`)
- **App project**: `MyAwesomeApp` (wants to use `kmp-room-core`)

---

## Part 1: Publishing the Library (One-time Setup)

### 1. Update Repository URL

Edit `kmp-room-core/build.gradle.kts`:

```kotlin
publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/neerajsoni/KMPDatabasePOC")  // ✅ Updated
            credentials {
                username = System.getenv("GITHUB_ACTOR") ?: findProperty("gpr.user") as String?
                password = System.getenv("GITHUB_TOKEN") ?: findProperty("gpr.token") as String?
            }
        }
    }
}
```

### 2. Create GitHub Token

```
1. Go to: https://github.com/settings/tokens/new
2. Name: GITHUB_PACKAGES_TOKEN
3. Scopes: ✅ write:packages, ✅ read:packages, ✅ repo (if private)
4. Generate → Copy token: ghp_xxxxxxxxxxxxxxxxxxxx
```

### 3. Store Credentials Securely

Edit `~/.gradle/gradle.properties`:

```properties
gpr.user=neerajsoni
gpr.token=ghp_xxxxxxxxxxxxxxxxxxxx
```

### 4. Publish

```bash
cd /path/to/KMPDatabasePOC
./gradlew :kmp-room-core:publish
```

**Expected output:**
```
> Task :kmp-room-core:publishKotlinMultiplatformPublicationToGitHubPackagesRepository
> Task :kmp-room-core:publishAndroidReleasePublicationToGitHubPackagesRepository
> Task :kmp-room-core:publishIosArm64PublicationToGitHubPackagesRepository
> Task :kmp-room-core:publishIosSimulatorArm64PublicationToGitHubPackagesRepository

BUILD SUCCESSFUL in 8s
```

### 5. Verify

Visit: https://github.com/neerajsoni/KMPDatabasePOC/packages

You should see:
```
📦 kmp-room-core
   Version: 1.0.0
   Published: just now
```

---

## Part 2: Consuming in Another Project

### Project Structure

```
MyAwesomeApp/
├── settings.gradle.kts       ← Add repository here
├── build.gradle.kts
├── composeApp/
│   └── build.gradle.kts      ← Add dependency here
└── ...
```

### 1. Configure Repository Access

Edit `MyAwesomeApp/settings.gradle.kts`:

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()

        // ✨ Add this for kmp-room-core
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/neerajsoni/KMPDatabasePOC")
            credentials {
                username = System.getenv("GITHUB_ACTOR") ?:
                          project.findProperty("gpr.user") as String?
                password = System.getenv("GITHUB_TOKEN") ?:
                          project.findProperty("gpr.token") as String?
            }
        }
    }
}
```

### 2. Add Dependency

Edit `MyAwesomeApp/composeApp/build.gradle.kts`:

```kotlin
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    id("com.google.devtools.ksp") version "2.1.20-1.0.31"  // ⚠️ REQUIRED
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            // ... your other dependencies

            // ✨ Add kmp-room-core
            implementation("com.brightly:kmp-room-core:1.0.0")
        }
    }
}

dependencies {
    // ⚠️ REQUIRED: KSP for Room annotation processing
    add("kspAndroid", "androidx.room:room-compiler:2.7.0")
    add("kspIosArm64", "androidx.room:room-compiler:2.7.0")
    add("kspIosSimulatorArm64", "androidx.room:room-compiler:2.7.0")
}
```

### 3. Sync Gradle

```bash
cd /path/to/MyAwesomeApp
./gradlew clean build
```

### 4. Use the Library

Create your database in `MyAwesomeApp/composeApp/src/commonMain/kotlin/`:

**Entity:**
```kotlin
package com.myapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val price: Double
)
```

**DAO:**
```kotlin
package com.myapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Insert
    suspend fun insert(product: Product)

    @Query("SELECT * FROM products")
    fun getAll(): Flow<List<Product>>
}
```

**Database:**
```kotlin
package com.myapp.data

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor

@Database(entities = [Product::class], version = 1)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase>
```

**Factory (commonMain):**
```kotlin
package com.myapp.data

expect class DatabaseFactory {
    fun createDatabase(): AppDatabase
}
```

**Factory (androidMain):**
```kotlin
package com.myapp.data

import android.content.Context
import androidx.room.migration.Migration
import com.brightly.kmp.room.core.android.AndroidDatabaseFactory
import com.brightly.kmp.room.core.DatabaseConfig

actual class DatabaseFactory(
    context: Context
) : AndroidDatabaseFactory<AppDatabase>(context) {

    actual fun createDatabase(): AppDatabase {
        return createDatabase("app.db", emptyList())
    }

    override fun createDatabase(name: String, migrations: List<Migration>): AppDatabase {
        return buildDatabase(
            AppDatabase::class.java,
            DatabaseConfig(name = name, version = 1)
        )
    }
}
```

**Factory (iosMain):**
```kotlin
package com.myapp.data

import androidx.room.migration.Migration
import com.brightly.kmp.room.core.ios.IosDatabaseFactory
import com.brightly.kmp.room.core.DatabaseConfig

actual class DatabaseFactory : IosDatabaseFactory<AppDatabase>() {

    actual fun createDatabase(): AppDatabase {
        return createDatabase("app.db", emptyList())
    }

    override fun createDatabase(name: String, migrations: List<Migration>): AppDatabase {
        return buildDatabase(
            DatabaseConfig(name = name, version = 1)
        )
    }
}
```

**Usage in App:**
```kotlin
// Android
val database = DatabaseFactory(applicationContext).createDatabase()

// iOS
val database = DatabaseFactory().createDatabase()

// Use the database
val productDao = database.productDao()
launch {
    productDao.insert(Product(name = "Widget", price = 9.99))
}
```

---

## Part 3: Updating the Library

### When You Need to Release v1.0.1

**In Library Project (KMPDatabasePOC):**

1. Make your changes to `kmp-room-core`
2. Update version in `kmp-room-core/build.gradle.kts`:
   ```kotlin
   version = "1.0.1"  // Changed from 1.0.0
   ```
3. Publish:
   ```bash
   ./gradlew :kmp-room-core:publish
   ```
4. (Optional) Tag release:
   ```bash
   git tag v1.0.1
   git push origin v1.0.1
   ```

**In App Project (MyAwesomeApp):**

1. Update dependency:
   ```kotlin
   implementation("com.brightly:kmp-room-core:1.0.1")  // Changed from 1.0.0
   ```
2. Sync:
   ```bash
   ./gradlew --refresh-dependencies
   ```

---

## Part 4: Automated Publishing with GitHub Actions

### Setup (One-time)

The workflow file already exists at:
`.github/workflows/publish-library.yml`

### Usage

**Automatic on Tag:**
```bash
# In KMPDatabasePOC project
git tag v1.0.2
git push origin v1.0.2
```

GitHub Actions will:
1. Build the library
2. Publish to GitHub Packages
3. Create GitHub Release with instructions

**Manual Trigger:**
1. Go to: https://github.com/neerajsoni/KMPDatabasePOC/actions
2. Select "Publish Library to GitHub Packages"
3. Click "Run workflow"

---

## Troubleshooting

### Issue: Could not resolve com.brightly:kmp-room-core:1.0.0

**Causes:**
- Token missing or expired
- Repository URL wrong
- Package not published

**Solution:**
```bash
# Check credentials
echo $GITHUB_TOKEN
# Or check ~/.gradle/gradle.properties

# Refresh dependencies
./gradlew --refresh-dependencies clean build

# Verify package exists
open https://github.com/neerajsoni/KMPDatabasePOC/packages
```

### Issue: 401 Unauthorized

**Solution:**
```bash
# Regenerate token with correct permissions
# Update ~/.gradle/gradle.properties
# Clear Gradle cache
rm -rf ~/.gradle/caches/
```

### Issue: Room compiler not generating code

**Solution:**
Ensure KSP is configured in **consuming project**:
```kotlin
plugins {
    id("com.google.devtools.ksp") version "2.1.20-1.0.31"
}

dependencies {
    add("kspAndroid", "androidx.room:room-compiler:2.7.0")
    add("kspIosArm64", "androidx.room:room-compiler:2.7.0")
    add("kspIosSimulatorArm64", "androidx.room:room-compiler:2.7.0")
}
```

---

## Summary

| Task | Command/Location |
|------|------------------|
| Publish library | `./gradlew :kmp-room-core:publish` |
| Check packages | https://github.com/YOUR_USERNAME/REPO/packages |
| Add repository | `settings.gradle.kts` |
| Add dependency | `implementation("com.brightly:kmp-room-core:1.0.0")` |
| Update version | Edit `build.gradle.kts` → republish |
| Auto-publish | Push git tag: `git push origin v1.0.x` |

---

## Real-World Tips

1. **Version Tags**: Use git tags for tracking releases
   ```bash
   git tag -a v1.0.0 -m "Initial release"
   git push origin v1.0.0
   ```

2. **CHANGELOG**: Keep a CHANGELOG.md with release notes
   ```markdown
   ## [1.0.1] - 2026-03-13
   ### Fixed
   - Migration helper bug fix
   ```

3. **Testing**: Test locally before publishing
   ```bash
   ./gradlew :kmp-room-core:publishToMavenLocal
   # Then test in app with: implementation("com.brightly:kmp-room-core:1.0.0")
   ```

4. **Documentation**: Update README when adding features
5. **Semantic Versioning**: Follow semver.org guidelines