# kmp-room-core Library - Dev Team Guide

**Library:** kmp-room-core
**Current Version:** 1.0.1
**Repository:** https://github.com/neeraj-brightly12/KMPDatabasePOC
**Packages:** https://github.com/neeraj-brightly12/KMPDatabasePOC/packages

---

## 📋 Table of Contents

1. [Quick Start for Consumers](#section-1-quick-start-for-consumers)
2. [Publishing New Versions](#section-2-publishing-new-versions)
3. [GitHub Token Setup](#section-3-github-token-setup)
4. [Complete Usage Example](#section-4-complete-usage-example)
5. [KSP Requirement Explained](#section-5-ksp-requirement-explained)
6. [Team Guidelines](#section-6-team-guidelines)

---

# Section 1: Quick Start for Consumers

## Who This Is For

Any developer who wants to use `kmp-room-core` in their KMP app (Android + iOS).

---

## Step 1: Get GitHub Token (5 minutes)

### Create Token

1. Visit: https://github.com/settings/tokens/new
2. Token name: `My App - GitHub Packages`
3. Expiration: **90 days** (recommended)
4. Permissions:
   - ✅ `read:packages` (required)
   - ✅ `repo` (only if repository is private)
5. Click **Generate token**
6. **Copy token** (starts with `ghp_...`)

### Save Token

**macOS/Linux:**
```bash
# Edit credentials file
nano ~/.gradle/gradle.properties

# Add these lines:
gpr.user=neeraj-brightly12
gpr.token=ghp_your_token_here_paste_it

# Save: Ctrl+O, Enter, Ctrl+X
```

**Windows:**
```bash
notepad %USERPROFILE%\.gradle\gradle.properties

# Add these lines:
gpr.user=neeraj-brightly12
gpr.token=ghp_your_token_here_paste_it

# Save and close
```

**Verify:**
```bash
cat ~/.gradle/gradle.properties
```

---

## Step 2: Configure Your App (10 minutes)

### 2.1: Add Repository

**File:** `settings.gradle.kts`

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()

        // Add this block ↓
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

### 2.2: Enable KSP Plugin

**File:** `gradle/libs.versions.toml`

```toml
[versions]
ksp = "2.1.20-1.0.31"

[plugins]
ksp = { id = "com.google.devtools.ksp", version.ref = "ksp" }
```

### 2.3: Add Dependencies

**File:** `app/build.gradle.kts` (or `composeApp/build.gradle.kts`)

```kotlin
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.ksp)  // ⚠️ REQUIRED
}

kotlin {
    androidTarget()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            // ✨ Single dependency for all platforms!
            implementation("com.brightly:kmp-room-core:1.0.1")
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

---

## Step 3: Sync and Build (2 minutes)

```bash
# Sync dependencies
./gradlew --refresh-dependencies

# Build Android
./gradlew :app:assembleDebug

# Build iOS
./gradlew :app:compileKotlinIosSimulatorArm64
```

**Expected output:**
```
BUILD SUCCESSFUL in 30s
```

---

## Step 4: Verify Setup

```bash
# Check library is downloaded
./gradlew :app:dependencies --configuration commonMainImplementationDependenciesMetadata | grep kmp-room-core
```

**Expected output:**
```
+--- com.brightly:kmp-room-core:1.0.1
```

✅ **Setup complete!** You're ready to use the library.

---

# Section 2: Publishing New Versions

## Who This Is For

Library maintainers who need to publish updates.

---

## Publishing Workflow (Complete Steps)

### Step 1: Get Publisher Token (One-time)

1. Visit: https://github.com/settings/tokens/new
2. Token name: `Library Publishing`
3. Expiration: **90 days** or custom
4. Permissions:
   - ✅ `repo` (Full control)
   - ✅ `write:packages` (Upload packages)
   - ✅ `read:packages` (Download packages)
5. Generate and copy token

**Save to `~/.gradle/gradle.properties`:**
```properties
gpr.user=neeraj-brightly12
gpr.token=ghp_publisher_token_here
```

---

### Step 2: Make Changes to Library

Edit library code in `kmp-room-core/src/`:

**Example: Add new utility function**

```kotlin
// kmp-room-core/src/commonMain/kotlin/com/brightly/kmp/room/core/util/DatabaseUtils.kt

object DatabaseUtils {
    // Existing functions...

    // New function
    fun getDatabaseSize(dbName: String): Long {
        val dbPath = getDatabasePath(dbName)
        // Implementation...
        return size
    }
}
```

---

### Step 3: Update Version

**File:** `kmp-room-core/build.gradle.kts`

```kotlin
group = "com.brightly"
version = "1.0.2"  // ← Increment version (was 1.0.1)
```

**Version Guidelines:**
- Bug fixes: `1.0.1` → `1.0.2` (patch)
- New features: `1.0.2` → `1.1.0` (minor)
- Breaking changes: `1.1.0` → `2.0.0` (major)

---

### Step 4: Test Changes Locally

**Option A: Test in same project**
```bash
# Build library
./gradlew :kmp-room-core:build

# Build test app
./gradlew :composeApp:assembleDebug
```

**Option B: Test as published artifact**
```bash
# Publish to local Maven
./gradlew :kmp-room-core:publishToMavenLocal

# In test app, temporarily add to settings.gradle.kts:
repositories {
    mavenLocal()
}

# Use library
implementation("com.brightly:kmp-room-core:1.0.2")

# Test
./gradlew :app:build
```

---

### Step 5: Clean Build

```bash
./gradlew :kmp-room-core:clean
```

---

### Step 6: Publish to GitHub Packages

```bash
./gradlew :kmp-room-core:publish
```

**What happens:**
```
Publishing to: https://maven.pkg.github.com/neeraj-brightly12/KMPDatabasePOC

✅ Publishing kotlinMultiplatform (root metadata)
   → kmp-room-core-1.0.2.module

✅ Publishing androidRelease
   → kmp-room-core-android-1.0.2.aar

✅ Publishing iosArm64
   → kmp-room-core-iosarm64-1.0.2.klib

✅ Publishing iosSimulatorArm64
   → kmp-room-core-iossimulatorarm64-1.0.2.klib

BUILD SUCCESSFUL in 2m 30s
```

**All 4 artifacts are published automatically.**

---

### Step 7: Create Git Tag

```bash
# Commit changes
git add kmp-room-core/
git commit -m "Release kmp-room-core v1.0.2

- Added getDatabaseSize utility
- Fixed iOS path issue
- Updated documentation"

# Create annotated tag
git tag -a v1.0.2 -m "Release v1.0.2"

# Push
git push origin main
git push origin v1.0.2
```

---

### Step 8: Verify Publication

**Check GitHub:**
```
https://github.com/neeraj-brightly12/KMPDatabasePOC/packages
```

Should show:
```
📦 kmp-room-core
   Versions:
   - 1.0.2 ← New!
   - 1.0.1
   - 1.0.0
```

**Test download:**
```bash
curl -I -H "Authorization: token YOUR_TOKEN" \
  "https://maven.pkg.github.com/neeraj-brightly12/KMPDatabasePOC/com/brightly/kmp-room-core/1.0.2/kmp-room-core-1.0.2.module"
```

Should return: `HTTP/2 200`

---

### Step 9: Notify Team

**Post in Slack/Teams:**
```
🚀 kmp-room-core v1.0.2 Released!

What's new:
✨ Added getDatabaseSize() utility
🐛 Fixed iOS database path issue
📚 Updated documentation

How to update:
implementation("com.brightly:kmp-room-core:1.0.2")

Details: https://github.com/neeraj-brightly12/KMPDatabasePOC/releases/v1.0.2
```

---

### Step 10: Update CHANGELOG

**File:** `kmp-room-core/CHANGELOG.md`

```markdown
# Changelog

## [1.0.2] - 2026-03-17
### Added
- `getDatabaseSize()` utility function

### Fixed
- iOS database path resolution issue (#42)

### Changed
- Updated internal error handling

---

## [1.0.1] - 2026-03-16
### Fixed
- Initial release fixes

## [1.0.0] - 2026-03-15
### Added
- Initial release
```

---

## Publishing Quick Commands

```bash
# Complete publishing workflow
./gradlew :kmp-room-core:clean
./gradlew :kmp-room-core:publish
git tag v1.0.2
git push origin main v1.0.2

# Verify
curl -I https://maven.pkg.github.com/neeraj-brightly12/KMPDatabasePOC/com/brightly/kmp-room-core/1.0.2/kmp-room-core-1.0.2.module
```

---

# Section 3: GitHub Token Setup

## Token Types

### Read-Only Token (For Consumers)

**Use for:** Developers using the library in their apps

**Permissions:**
- ✅ `read:packages`
- ✅ `repo` (if repository is private)

**Creation:**
```
https://github.com/settings/tokens/new
→ Name: "App Development"
→ Expiration: 90 days
→ Scopes: read:packages
→ Generate
```

---

### Write Token (For Publishers)

**Use for:** Library maintainers publishing updates

**Permissions:**
- ✅ `repo`
- ✅ `write:packages`
- ✅ `read:packages`
- ✅ `delete:packages` (optional)

**Creation:**
```
https://github.com/settings/tokens/new
→ Name: "Library Publishing"
→ Expiration: 90 days
→ Scopes: repo, write:packages, read:packages
→ Generate
```

---

## Credential Storage Options

### Option 1: Gradle Properties (Recommended)

**File:** `~/.gradle/gradle.properties`

```properties
gpr.user=your-github-username
gpr.token=ghp_your_token_here
```

**Pros:**
- ✅ Persistent across projects
- ✅ Not committed to Git
- ✅ Easy to update

**Cons:**
- ⚠️ Same token for all projects (can use different tokens per project if needed)

---

### Option 2: Environment Variables

**macOS/Linux:**
```bash
# Add to ~/.zshrc or ~/.bashrc
export GITHUB_ACTOR=your-github-username
export GITHUB_TOKEN=ghp_your_token_here

# Reload shell
source ~/.zshrc
```

**Windows:**
```bash
# Set permanently
setx GITHUB_ACTOR "your-github-username"
setx GITHUB_TOKEN "ghp_your_token_here"
```

**Pros:**
- ✅ Can be different per terminal session
- ✅ Easy for CI/CD

**Cons:**
- ⚠️ Must be set in each new terminal

---

### Option 3: CI/CD Secrets

**GitHub Actions:**

```yaml
- name: Publish Library
  env:
    GITHUB_ACTOR: ${{ github.actor }}
    GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}
  run: ./gradlew :kmp-room-core:publish
```

**GitLab CI:**

```yaml
publish:
  script:
    - export GITHUB_ACTOR=$GITHUB_USERNAME
    - export GITHUB_TOKEN=$GITHUB_TOKEN
    - ./gradlew :kmp-room-core:publish
```

---

## Token Management

### View Your Tokens

https://github.com/settings/tokens

### Revoke Token

1. Go to: https://github.com/settings/tokens
2. Find your token
3. Click **Delete** or **Revoke**

### Rotate Token

**Every 90 days (recommended):**

1. Generate new token
2. Update `~/.gradle/gradle.properties`
3. Test: `./gradlew --refresh-dependencies`
4. Revoke old token

---

## Security Checklist

- [ ] Token stored in `~/.gradle/gradle.properties` (not project)
- [ ] Token has minimal required permissions
- [ ] Token has expiration date
- [ ] `gradle.properties` is in `.gitignore`
- [ ] Never commit tokens to Git
- [ ] Each developer has their own token
- [ ] Tokens rotated regularly

---

# Section 4: Complete Usage Example

## Scenario: Building a Task Management App

Let's build a complete KMP task management app using `kmp-room-core`.

---

## Step 1: Setup (Already Done in Quick Start)

- ✅ GitHub token created and saved
- ✅ Repository configured in settings.gradle.kts
- ✅ Library dependency added
- ✅ KSP enabled

---

## Step 2: Define Database Schema (Your App)

### Entity: Task

**File:** `app/src/commonMain/kotlin/com/myapp/data/entity/TaskEntity.kt`

```kotlin
package com.myapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val title: String,

    val description: String,

    val isCompleted: Boolean = false,

    val createdAt: Long = System.currentTimeMillis()
)
```

---

### DAO: TaskDao

**File:** `app/src/commonMain/kotlin/com/myapp/data/dao/TaskDao.kt`

```kotlin
package com.myapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.myapp.data.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Insert
    suspend fun insert(task: TaskEntity)

    @Update
    suspend fun update(task: TaskEntity)

    @Query("SELECT * FROM tasks ORDER BY createdAt DESC")
    fun getAllTasks(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE isCompleted = 0 ORDER BY createdAt DESC")
    fun getActiveTasks(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE isCompleted = 1 ORDER BY createdAt DESC")
    fun getCompletedTasks(): Flow<List<TaskEntity>>

    @Query("DELETE FROM tasks WHERE id = :taskId")
    suspend fun delete(taskId: Int)

    @Query("UPDATE tasks SET isCompleted = :completed WHERE id = :taskId")
    suspend fun updateCompleted(taskId: Int, completed: Boolean)
}
```

---

### Database: AppDatabase

**File:** `app/src/commonMain/kotlin/com/myapp/data/database/AppDatabase.kt`

```kotlin
package com.myapp.data.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import com.myapp.data.dao.TaskDao
import com.myapp.data.entity.TaskEntity

@Database(
    entities = [TaskEntity::class],
    version = 1,
    exportSchema = false
)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase>
```

---

### DatabaseFactory (expect/actual)

**File:** `app/src/commonMain/kotlin/com/myapp/data/database/DatabaseFactory.kt`

```kotlin
package com.myapp.data.database

expect class DatabaseFactory {
    fun createDatabase(): AppDatabase
}
```

---

## Step 3: Implement Platform Factories

### Android Factory

**File:** `app/src/androidMain/kotlin/com/myapp/data/database/DatabaseFactory.android.kt`

```kotlin
package com.myapp.data.database

import android.content.Context
import androidx.room.migration.Migration
import com.brightly.kmp.room.core.android.AndroidDatabaseFactory
import com.brightly.kmp.room.core.DatabaseConfig

actual class DatabaseFactory(
    context: Context
) : AndroidDatabaseFactory<AppDatabase>(context) {

    actual fun createDatabase(): AppDatabase {
        return createDatabase("tasks.db", emptyList())
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
                enableLogging = true,  // Enable for debugging
                migrations = migrations
            )
        )
    }
}
```

---

### iOS Factory

**File:** `app/src/iosMain/kotlin/com/myapp/data/database/DatabaseFactory.ios.kt`

```kotlin
package com.myapp.data.database

import androidx.room.migration.Migration
import com.brightly.kmp.room.core.ios.IosDatabaseFactory
import com.brightly.kmp.room.core.DatabaseConfig

actual class DatabaseFactory : IosDatabaseFactory<AppDatabase>() {

    actual fun createDatabase(): AppDatabase {
        return createDatabase("tasks.db", emptyList())
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

## Step 4: Create Repository Layer

**File:** `app/src/commonMain/kotlin/com/myapp/data/repository/TaskRepository.kt`

```kotlin
package com.myapp.data.repository

import com.myapp.data.database.AppDatabase
import com.myapp.data.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

class TaskRepository(database: AppDatabase) {
    private val taskDao = database.taskDao()

    fun getAllTasks(): Flow<List<TaskEntity>> {
        return taskDao.getAllTasks()
    }

    fun getActiveTasks(): Flow<List<TaskEntity>> {
        return taskDao.getActiveTasks()
    }

    fun getCompletedTasks(): Flow<List<TaskEntity>> {
        return taskDao.getCompletedTasks()
    }

    suspend fun addTask(title: String, description: String) {
        val task = TaskEntity(
            title = title,
            description = description
        )
        taskDao.insert(task)
    }

    suspend fun toggleTaskCompletion(taskId: Int, completed: Boolean) {
        taskDao.updateCompleted(taskId, completed)
    }

    suspend fun deleteTask(taskId: Int) {
        taskDao.delete(taskId)
    }
}
```

---

## Step 5: Create ViewModel

**File:** `app/src/commonMain/kotlin/com/myapp/ui/TaskViewModel.kt`

```kotlin
package com.myapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myapp.data.entity.TaskEntity
import com.myapp.data.repository.TaskRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TaskViewModel(
    private val repository: TaskRepository
) : ViewModel() {

    val tasks: StateFlow<List<TaskEntity>> = repository.getAllTasks()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addTask(title: String, description: String) {
        viewModelScope.launch {
            repository.addTask(title, description)
        }
    }

    fun toggleTask(taskId: Int, completed: Boolean) {
        viewModelScope.launch {
            repository.toggleTaskCompletion(taskId, completed)
        }
    }

    fun deleteTask(taskId: Int) {
        viewModelScope.launch {
            repository.deleteTask(taskId)
        }
    }
}
```

---

## Step 6: Create UI

**File:** `app/src/commonMain/kotlin/com/myapp/ui/TaskScreen.kt`

```kotlin
package com.myapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TaskScreen(viewModel: TaskViewModel) {
    val tasks by viewModel.tasks.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Text("+")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            items(tasks) { task ->
                TaskItem(
                    task = task,
                    onToggle = { viewModel.toggleTask(task.id, !task.isCompleted) },
                    onDelete = { viewModel.deleteTask(task.id) }
                )
            }
        }
    }

    if (showDialog) {
        AddTaskDialog(
            onDismiss = { showDialog = false },
            onAdd = { title, description ->
                viewModel.addTask(title, description)
                showDialog = false
            }
        )
    }
}

@Composable
fun TaskItem(
    task: TaskEntity,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = task.description,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Row {
                Checkbox(
                    checked = task.isCompleted,
                    onCheckedChange = { onToggle() }
                )
                IconButton(onClick = onDelete) {
                    Text("🗑️")
                }
            }
        }
    }
}

@Composable
fun AddTaskDialog(
    onDismiss: () -> Unit,
    onAdd: (String, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Task") },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onAdd(title, description) },
                enabled = title.isNotBlank()
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
```

---

## Step 7: Initialize in App

### Android

**File:** `app/src/androidMain/kotlin/com/myapp/MainActivity.kt`

```kotlin
package com.myapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.myapp.data.database.DatabaseFactory
import com.myapp.data.repository.TaskRepository
import com.myapp.ui.TaskScreen
import com.myapp.ui.TaskViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create database using kmp-room-core
        val database = DatabaseFactory(applicationContext).createDatabase()
        val repository = TaskRepository(database)

        setContent {
            MaterialTheme {
                val viewModel = viewModel<TaskViewModel> {
                    TaskViewModel(repository)
                }
                TaskScreen(viewModel)
            }
        }
    }
}
```

---

### iOS

**File:** `iosApp/iosApp/ContentView.swift`

```swift
import SwiftUI
import ComposeApp

struct ContentView: View {
    var body: some View {
        ComposeView()
            .ignoresSafeArea()
    }
}

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.MainViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}
```

**File:** `app/src/iosMain/kotlin/com/myapp/MainViewController.kt`

```kotlin
package com.myapp

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import com.myapp.data.database.DatabaseFactory
import com.myapp.data.repository.TaskRepository
import com.myapp.ui.TaskScreen
import com.myapp.ui.TaskViewModel

fun MainViewController() = ComposeUIViewController {
    val database = remember { DatabaseFactory().createDatabase() }
    val repository = remember { TaskRepository(database) }
    val viewModel = remember { TaskViewModel(repository) }

    MaterialTheme {
        TaskScreen(viewModel)
    }
}
```

---

## Step 8: Build and Run

### Android

```bash
# Build
./gradlew :app:assembleDebug

# Install on device/emulator
./gradlew :app:installDebug

# Or run from Android Studio
```

### iOS

```bash
# Build Kotlin framework
./gradlew :app:compileKotlinIosSimulatorArm64

# Open Xcode
cd iosApp
open iosApp.xcodeproj

# Select simulator and click Run ▶️
```

---

## Complete App Features

The app now has:

✅ **Add tasks** - Title and description
✅ **View tasks** - List of all tasks
✅ **Complete tasks** - Checkbox to mark done
✅ **Delete tasks** - Remove tasks
✅ **Persistent storage** - Data saved in Room database
✅ **Cross-platform** - Works on Android and iOS
✅ **Reactive UI** - Flow automatically updates UI

---

## Project Structure

```
MyTaskApp/
├── settings.gradle.kts              # ✅ GitHub Packages configured
├── gradle/libs.versions.toml        # ✅ KSP enabled
│
├── app/
│   ├── build.gradle.kts             # ✅ Library dependency + KSP
│   └── src/
│       ├── commonMain/kotlin/
│       │   ├── data/
│       │   │   ├── entity/TaskEntity.kt
│       │   │   ├── dao/TaskDao.kt
│       │   │   ├── database/AppDatabase.kt
│       │   │   ├── database/DatabaseFactory.kt
│       │   │   └── repository/TaskRepository.kt
│       │   └── ui/
│       │       ├── TaskScreen.kt
│       │       └── TaskViewModel.kt
│       │
│       ├── androidMain/kotlin/
│       │   ├── MainActivity.kt
│       │   └── data/database/DatabaseFactory.android.kt
│       │
│       └── iosMain/kotlin/
│           ├── MainViewController.kt
│           └── data/database/DatabaseFactory.ios.kt
│
└── iosApp/                          # iOS wrapper
    └── iosApp.xcodeproj
```

---

# Section 5: KSP Requirement Explained

## ⚠️ Critical: KSP is ALWAYS Required in Consumer Apps

### The Question

**"I'm using the kmp-room-core library. Do I still need KSP in my app?"**

### The Answer

**YES! KSP is ABSOLUTELY REQUIRED in your app.**

---

## Why KSP is Needed

### What the Library Provides ✅

The `kmp-room-core` library gives you:
- ✅ `AndroidDatabaseFactory` - Platform factory for Android
- ✅ `IosDatabaseFactory` - Platform factory for iOS
- ✅ `DatabaseConfig` - Configuration data class
- ✅ `DatabaseUtils` - Utility functions
- ✅ Room runtime dependencies

**The library is pre-compiled infrastructure code.**

---

### What the Library DOES NOT Provide ❌

The library does NOT know about:
- ❌ Your entities (`@Entity`)
- ❌ Your DAOs (`@Dao`)
- ❌ Your database schema (`@Database`)
- ❌ Your queries (`@Query`)

**These are defined in YOUR app, not the library.**

---

### What KSP Generates

When you build your app, KSP processes **YOUR** Room annotations and generates:

#### 1. DAO Implementations

**You write:**
```kotlin
@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks")
    fun getAllTasks(): Flow<List<TaskEntity>>
}
```

**KSP generates:**
```kotlin
class TaskDao_Impl : TaskDao {
    override fun getAllTasks(): Flow<List<TaskEntity>> {
        // 200+ lines of generated SQL execution code
    }
}
```

#### 2. Database Constructor

**You write:**
```kotlin
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase()

expect object AppDatabaseConstructor
```

**KSP generates:**
```kotlin
actual object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase {
        // Generated database initialization code
        // Sets up tables, indexes, etc.
    }
}
```

#### 3. Query Validation

KSP validates at compile-time:
- ✅ SQL syntax is correct
- ✅ Column names exist
- ✅ Types match
- ✅ Return types are correct

**Without KSP:**
- ❌ No DAO implementations → Runtime crash
- ❌ No database constructor → Compilation error
- ❌ No query validation → Runtime SQL errors

---

## Configuration Required

### In gradle/libs.versions.toml:
```toml
ksp = { id = "com.google.devtools.ksp", version = "2.1.20-1.0.31" }
```

### In app/build.gradle.kts:
```kotlin
plugins {
    alias(libs.plugins.ksp)  // ⚠️ MUST HAVE
}

dependencies {
    add("kspAndroid", "androidx.room:room-compiler:2.7.0")       // ⚠️ MUST HAVE
    add("kspIosArm64", "androidx.room:room-compiler:2.7.0")      // ⚠️ MUST HAVE
    add("kspIosSimulatorArm64", "androidx.room:room-compiler:2.7.0")  // ⚠️ MUST HAVE
}
```

---

## What Happens Without KSP

### Build Error:

```
e: Object 'AppDatabaseConstructor' is not abstract and does not implement abstract member:
fun initialize(): T

FAILURE: Build failed with an exception.
```

### Why:

```
Your Code:
  @Database class AppDatabase
  expect object AppDatabaseConstructor

KSP (missing):
  ❌ Should generate: actual object AppDatabaseConstructor

Result:
  ❌ Compilation fails - expect without actual
```

---

## Summary Table

| Component | Where Defined | Who Processes | Generated Code |
|-----------|--------------|---------------|----------------|
| AndroidDatabaseFactory | kmp-room-core library | Pre-compiled | N/A |
| IosDatabaseFactory | kmp-room-core library | Pre-compiled | N/A |
| Your entities | Your app | KSP in your app | N/A (just compiled) |
| Your DAOs | Your app | KSP in your app | DAO implementations |
| Your database | Your app | KSP in your app | Database constructor |

**Conclusion:** KSP processes **YOUR** app's Room annotations, not the library's code.

---

# Section 6: Team Guidelines

## For Library Maintainers

### Before Publishing Checklist

- [ ] Code changes tested locally
- [ ] Version number updated (semantic versioning)
- [ ] CHANGELOG.md updated
- [ ] Tests pass on all platforms
- [ ] Documentation updated
- [ ] Breaking changes documented (if any)

### Publishing Checklist

- [ ] `./gradlew :kmp-room-core:clean`
- [ ] `./gradlew :kmp-room-core:publish`
- [ ] `git tag vX.Y.Z`
- [ ] `git push origin main vX.Y.Z`
- [ ] Verify on GitHub Packages
- [ ] Create GitHub release with notes
- [ ] Notify team

### Communication

**For minor updates:**
```
📦 kmp-room-core v1.0.2 available
Bug fixes and improvements
```

**For major updates:**
```
🚨 kmp-room-core v2.0.0 released

⚠️ BREAKING CHANGES:
- DatabaseFactory signature changed
- Migration required

Migration guide: [link]
```

---

## For Library Consumers

### Initial Setup Checklist

- [ ] GitHub token created (read:packages)
- [ ] Token saved to ~/.gradle/gradle.properties
- [ ] Repository added to settings.gradle.kts
- [ ] Library dependency added
- [ ] KSP plugin enabled
- [ ] KSP dependencies added
- [ ] Build successful

### Updating Library Version

```bash
# 1. Update dependency version
implementation("com.brightly:kmp-room-core:X.Y.Z")

# 2. Sync
./gradlew --refresh-dependencies

# 3. Build
./gradlew :app:build

# 4. Test thoroughly

# 5. Commit
git commit -am "Update kmp-room-core to vX.Y.Z"
```

---

## Code Review Guidelines

### When Reviewing Library Code

- ✅ Check backward compatibility
- ✅ Verify platform-specific code is correct
- ✅ Test on all platforms
- ✅ Review public API changes
- ✅ Check documentation updates

### When Reviewing App Code Using Library

- ✅ Verify library version is appropriate
- ✅ Check KSP is properly configured
- ✅ Ensure expect/actual pattern is followed
- ✅ Review database migrations

---

## Versioning Policy

### When to Increment

**Patch (1.0.X):**
- Bug fixes
- Documentation updates
- Internal refactoring
- Performance improvements

**Minor (1.X.0):**
- New features
- New optional parameters
- Deprecations (with migration path)

**Major (X.0.0):**
- Breaking API changes
- Removing deprecated features
- Major architectural changes
- Minimum version bumps

### Example Timeline

```
v1.0.0 - Mar 1, 2026 - Initial release
v1.0.1 - Mar 10, 2026 - Bug fix (iOS crash)
v1.0.2 - Mar 15, 2026 - Bug fix (Android path)
v1.1.0 - Apr 1, 2026 - New feature (migration DSL)
v1.1.1 - Apr 5, 2026 - Bug fix (migration issue)
v2.0.0 - May 1, 2026 - Breaking change (new API)
```

---

## Documentation Maintenance

### Required Documents

**In kmp-room-core/:**
- `README.md` - Installation and usage
- `CHANGELOG.md` - Version history
- `PUBLISHING.md` - Publishing guide
- `MIGRATION.md` - Migration guides for breaking changes

### Update on Every Release

```markdown
# CHANGELOG.md
## [1.0.2] - 2026-03-17
### Fixed
- iOS database path issue (#42)
```

---

## CI/CD Integration (Optional)

### GitHub Actions - Auto Publish on Tag

**File:** `.github/workflows/publish.yml`

```yaml
name: Publish Library

on:
  push:
    tags:
      - 'v*'

jobs:
  publish:
    runs-on: ubuntu-latest
    permissions:
      contents: read
      packages: write

    steps:
      - uses: actions/checkout@v4

      - name: Set up JDK 17
        uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: 'temurin'

      - name: Publish to GitHub Packages
        env:
          GITHUB_ACTOR: ${{ github.actor }}
          GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}
        run: ./gradlew :kmp-room-core:publish
```

**Usage:**
```bash
git tag v1.0.3
git push origin v1.0.3
# GitHub Actions automatically publishes
```

---

# Quick Reference Cards

## Card 1: Consumer Setup (Copy & Paste)

```kotlin
// settings.gradle.kts
maven {
    url = uri("https://maven.pkg.github.com/neeraj-brightly12/KMPDatabasePOC")
    credentials {
        username = providers.gradleProperty("gpr.user").orNull
        password = providers.gradleProperty("gpr.token").orNull
    }
}

// gradle/libs.versions.toml
ksp = { id = "com.google.devtools.ksp", version = "2.1.20-1.0.31" }

// app/build.gradle.kts
plugins {
    alias(libs.plugins.ksp)
}

commonMain.dependencies {
    implementation("com.brightly:kmp-room-core:1.0.1")
}

dependencies {
    add("kspAndroid", "androidx.room:room-compiler:2.7.0")
    add("kspIosArm64", "androidx.room:room-compiler:2.7.0")
    add("kspIosSimulatorArm64", "androidx.room:room-compiler:2.7.0")
}

// ~/.gradle/gradle.properties
gpr.user=neeraj-brightly12
gpr.token=ghp_YOUR_TOKEN_HERE
```

---

## Card 2: Publishing Workflow (Copy & Paste)

```bash
# 1. Update version in kmp-room-core/build.gradle.kts
version = "1.0.2"

# 2. Clean and publish
./gradlew :kmp-room-core:clean
./gradlew :kmp-room-core:publish

# 3. Tag and push
git add kmp-room-core/build.gradle.kts
git commit -m "Release v1.0.2"
git tag -a v1.0.2 -m "Release v1.0.2"
git push origin main
git push origin v1.0.2

# 4. Verify
# Visit: https://github.com/neeraj-brightly12/KMPDatabasePOC/packages
```

---

## Card 3: Essential Commands

```bash
# Build library
./gradlew :kmp-room-core:build

# Publish library
./gradlew :kmp-room-core:publish

# Build app (Android)
./gradlew :app:assembleDebug

# Build app (iOS)
./gradlew :app:compileKotlinIosSimulatorArm64

# Refresh dependencies
./gradlew --refresh-dependencies

# Check dependencies
./gradlew :app:dependencies | grep kmp-room-core

# List publishing tasks
./gradlew :kmp-room-core:tasks --group publishing
```

---

## Card 4: Version Management

```kotlin
// Semantic Versioning
MAJOR.MINOR.PATCH

// Examples:
1.0.0 → 1.0.1  // Bug fix (patch)
1.0.1 → 1.1.0  // New feature (minor)
1.1.0 → 2.0.0  // Breaking change (major)

// Update in:
kmp-room-core/build.gradle.kts
version = "X.Y.Z"
```

---

## Card 5: Troubleshooting Quick Fixes

```bash
# Issue: Library not found
./gradlew --refresh-dependencies

# Issue: 401 Unauthorized
# → Regenerate token, update ~/.gradle/gradle.properties

# Issue: KSP not generating code
# → Check KSP plugin and dependencies in build.gradle.kts

# Issue: Gradle cache issues
rm -rf ~/.gradle/caches/
./gradlew clean build

# Issue: Android Studio sync fails
# → Invalidate Caches: File → Invalidate Caches → Restart
```

---

# Important URLs

## GitHub

- **Packages:** https://github.com/neeraj-brightly12/KMPDatabasePOC/packages
- **Repository:** https://github.com/neeraj-brightly12/KMPDatabasePOC
- **Create Token:** https://github.com/settings/tokens/new
- **View Tokens:** https://github.com/settings/tokens

## Credentials

- **Location:** `~/.gradle/gradle.properties`
- **Format:**
  ```properties
  gpr.user=neeraj-brightly12
  gpr.token=ghp_token_here
  ```

---

# FAQ

## Q1: Do I need KSP if I'm using the published library?

**A:** YES! KSP generates code for YOUR app's Room entities and DAOs. The library only provides infrastructure, not generated code.

## Q2: Can multiple apps use different versions?

**A:** YES! App A can use v1.0.0 while App B uses v1.1.0. Versions coexist.

## Q3: How do I update to a new version?

**A:** Update dependency version, sync, and rebuild:
```kotlin
implementation("com.brightly:kmp-room-core:1.0.2")
```

## Q4: What if I get a 404 error?

**A:** Package doesn't exist or isn't published. Check GitHub Packages page to verify.

## Q5: Can I use the library without GitHub token?

**A:** NO. GitHub Packages requires authentication for both public and private packages.

## Q6: Should each developer have their own token?

**A:** YES! Never share tokens. Each developer creates their own.

## Q7: What platforms are supported?

**A:** Android (minSdk 24+) and iOS (13+). Both ARM64 architectures.

## Q8: How do I report bugs?

**A:** Create issue on GitHub with reproduction steps, expected vs actual behavior.

## Q9: Can I contribute to the library?

**A:** Yes! Fork, make changes, submit pull request. Maintainers will review.

## Q10: What if build fails with metadata error?

**A:** Use platform-specific builds:
- `./gradlew :app:assembleDebug` (Android)
- `./gradlew :app:compileKotlinIosSimulatorArm64` (iOS)

---

# Summary

## For Consumers (Using Library)

```
1. Create token (read:packages)
2. Save to ~/.gradle/gradle.properties
3. Add repository (settings.gradle.kts)
4. Add dependency
   implementation("com.brightly:kmp-room-core:1.0.1")
5. Enable KSP + dependencies
6. Define your database schema
7. Build and run
```

## For Publishers (Updating Library)

```
1. Make changes to library code
2. Update version number
3. Clean: ./gradlew :kmp-room-core:clean
4. Publish: ./gradlew :kmp-room-core:publish
5. Tag: git tag v1.0.2
6. Push: git push origin main v1.0.2
7. Notify team
```

## Key Points

- ✅ Single dependency works for all platforms
- ✅ Gradle auto-resolves correct variant
- ⚠️ **KSP always required in consumer apps**
- ✅ Multiple versions can coexist
- ✅ Each developer needs their own GitHub token

---

**This document contains everything your dev team needs to work with KMP libraries.**

For detailed examples and troubleshooting, see the respective sections above.

Last updated: March 2026