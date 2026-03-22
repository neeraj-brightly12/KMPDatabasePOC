# Quick Usage Example

## Complete Example: Todo App

This example shows how to create a simple Todo app using the kmp-room-core library.

### Step 1: Add Dependency

```kotlin
// build.gradle.kts (app module)
plugins {
    alias(libs.plugins.ksp) // ✅ REQUIRED for Room annotation processing
}

dependencies {
    commonMain.dependencies {
        // Option 1: Use published library (recommended for production)
        implementation("com.brightly:kmp-room-core:1.0.2")

        // Option 2: Use local project (for development/testing)
        // implementation(project(":kmp-room-core"))
    }

    // ✅ CRITICAL: KSP dependencies required for your app's entities/DAOs
    add("kspAndroid", "androidx.room:room-compiler:2.7.0")
    add("kspIosArm64", "androidx.room:room-compiler:2.7.0")
    add("kspIosSimulatorArm64", "androidx.room:room-compiler:2.7.0")
}
```

**⚠️ Important:** Do NOT comment out KSP plugin and dependencies. They are required to process your app's Room annotations (`@Entity`, `@Dao`, `@Database`).

### Step 2: Define Entity (commonMain)

```kotlin
package com.example.todoapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todos")
data class TodoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
```

### Step 3: Define DAO (commonMain)

```kotlin
package com.example.todoapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.todoapp.data.entity.TodoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {

    @Insert
    suspend fun insertTodo(todo: TodoEntity)

    @Query("SELECT * FROM todos ORDER BY createdAt DESC")
    fun getAllTodos(): Flow<List<TodoEntity>>

    @Query("SELECT * FROM todos WHERE isCompleted = 0")
    fun getActiveTodos(): Flow<List<TodoEntity>>

    @Query("SELECT * FROM todos WHERE isCompleted = 1")
    fun getCompletedTodos(): Flow<List<TodoEntity>>

    @Update
    suspend fun updateTodo(todo: TodoEntity)

    @Query("DELETE FROM todos WHERE id = :todoId")
    suspend fun deleteTodo(todoId: Int)

    @Query("DELETE FROM todos WHERE isCompleted = 1")
    suspend fun deleteCompletedTodos()
}
```

### Step 4: Define Database (commonMain)

```kotlin
package com.example.todoapp.data.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import com.example.todoapp.data.dao.TodoDao
import com.example.todoapp.data.entity.TodoEntity

@Database(
    entities = [TodoEntity::class],
    version = 1,
    exportSchema = true
)
@ConstructedBy(TodoDatabaseConstructor::class)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object TodoDatabaseConstructor : RoomDatabaseConstructor<TodoDatabase>
```

### Step 5: Factory Interface (commonMain)

```kotlin
package com.example.todoapp.data.database

expect class DatabaseFactory {
    fun createDatabase(): TodoDatabase
}
```

### Step 6: Android Factory (androidMain)

```kotlin
package com.example.todoapp.data.database

import android.content.Context
import androidx.room.migration.Migration
import com.brightly.kmp.room.core.DatabaseConfig
import com.brightly.kmp.room.core.android.AndroidDatabaseFactory

actual class DatabaseFactory(
    context: Context
) : AndroidDatabaseFactory<TodoDatabase>(context) {

    actual fun createDatabase(): TodoDatabase {
        return createDatabase(name = "todos.db")
    }

    override fun createDatabase(
        name: String,
        migrations: List<Migration>
    ): TodoDatabase {
        return buildDatabase(
            TodoDatabase::class.java,
            DatabaseConfig(
                name = name,
                version = 1,
                enableLogging = true, // Enable for debugging
                migrations = migrations
            )
        )
    }
}
```

### Step 7: iOS Factory (iosMain)

```kotlin
package com.example.todoapp.data.database

import androidx.room.migration.Migration
import com.brightly.kmp.room.core.DatabaseConfig
import com.brightly.kmp.room.core.ios.IosDatabaseFactory

actual class DatabaseFactory : IosDatabaseFactory<TodoDatabase>() {

    actual fun createDatabase(): TodoDatabase {
        return createDatabase(name = "todos.db")
    }

    override fun createDatabase(
        name: String,
        migrations: List<Migration>
    ): TodoDatabase {
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

### Step 8: Repository (commonMain)

```kotlin
package com.example.todoapp.data.repository

import com.example.todoapp.data.database.TodoDatabase
import com.example.todoapp.data.entity.TodoEntity
import kotlinx.coroutines.flow.Flow

class TodoRepository(database: TodoDatabase) {

    private val dao = database.todoDao()

    fun getAllTodos(): Flow<List<TodoEntity>> = dao.getAllTodos()

    fun getActiveTodos(): Flow<List<TodoEntity>> = dao.getActiveTodos()

    fun getCompletedTodos(): Flow<List<TodoEntity>> = dao.getCompletedTodos()

    suspend fun addTodo(title: String, description: String) {
        dao.insertTodo(
            TodoEntity(
                title = title,
                description = description
            )
        )
    }

    suspend fun toggleTodo(todo: TodoEntity) {
        dao.updateTodo(todo.copy(isCompleted = !todo.isCompleted))
    }

    suspend fun deleteTodo(todoId: Int) {
        dao.deleteTodo(todoId)
    }

    suspend fun clearCompleted() {
        dao.deleteCompletedTodos()
    }
}
```

### Step 9: ViewModel (commonMain)

```kotlin
package com.example.todoapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.entity.TodoEntity
import com.example.todoapp.data.repository.TodoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TodoViewModel(
    private val repository: TodoRepository
) : ViewModel() {

    private val _todos = MutableStateFlow<List<TodoEntity>>(emptyList())
    val todos: StateFlow<List<TodoEntity>> = _todos

    init {
        loadTodos()
    }

    private fun loadTodos() {
        viewModelScope.launch {
            repository.getAllTodos().collect { todoList ->
                _todos.value = todoList
            }
        }
    }

    fun addTodo(title: String, description: String) {
        viewModelScope.launch {
            repository.addTodo(title, description)
        }
    }

    fun toggleTodo(todo: TodoEntity) {
        viewModelScope.launch {
            repository.toggleTodo(todo)
        }
    }

    fun deleteTodo(todoId: Int) {
        viewModelScope.launch {
            repository.deleteTodo(todoId)
        }
    }

    fun clearCompleted() {
        viewModelScope.launch {
            repository.clearCompleted()
        }
    }
}
```

### Step 10: Android MainActivity

```kotlin
package com.example.todoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.todoapp.data.database.DatabaseFactory
import com.example.todoapp.ui.App

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            App(DatabaseFactory(applicationContext))
        }
    }
}
```

### Step 11: iOS MainViewController

```kotlin
package com.example.todoapp

import androidx.compose.ui.window.ComposeUIViewController
import com.example.todoapp.data.database.DatabaseFactory
import com.example.todoapp.ui.App

fun MainViewController() = ComposeUIViewController {
    App(DatabaseFactory())
}
```

## Adding Migrations

When you need to change the schema:

```kotlin
import com.brightly.kmp.room.core.util.migration

// Version 1 to 2: Add priority field
val MIGRATION_1_2 = migration(1, 2) { database ->
    database.execSQL(
        "ALTER TABLE todos ADD COLUMN priority INTEGER NOT NULL DEFAULT 0"
    )
}

// Version 2 to 3: Add category table
val MIGRATION_2_3 = migration(2, 3) { database ->
    database.execSQL("""
        CREATE TABLE categories (
            id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
            name TEXT NOT NULL,
            color TEXT NOT NULL
        )
    """)
    database.execSQL(
        "ALTER TABLE todos ADD COLUMN categoryId INTEGER"
    )
}

// Use in factory
override fun createDatabase(name: String, migrations: List<Migration>): TodoDatabase {
    return buildDatabase(
        TodoDatabase::class.java,
        DatabaseConfig(
            name = name,
            version = 3, // Updated version
            migrations = listOf(MIGRATION_1_2, MIGRATION_2_3)
        )
    )
}
```

## Using Flow Extensions

```kotlin
import com.brightly.kmp.room.core.extensions.*

class TodoRepository(database: TodoDatabase) {
    private val dao = database.todoDao()

    // Get only if there are todos
    fun getTodosIfNotEmpty() = dao.getAllTodos().filterNotEmpty()

    // Get count
    fun getTodoCount() = dao.getAllTodos().mapToSize()

    // Get first todo
    fun getFirstTodo() = dao.getAllTodos().mapToFirstOrNull()
}
```

## Database Utilities

```kotlin
import com.brightly.kmp.room.core.util.DatabaseUtils

// Check database
fun checkDatabase() {
    if (DatabaseUtils.databaseExists("todos.db")) {
        println("Database exists at: ${DatabaseUtils.getDatabasePath("todos.db")}")
    }
}

// Clear database (e.g., for logout)
fun clearAllData() {
    DatabaseUtils.deleteDatabase("todos.db")
}
```

## That's It!

This complete example shows:
- ✅ How to structure your entities and DAOs
- ✅ How to create platform-specific factories using the library
- ✅ How to implement repository and ViewModel
- ✅ How to handle migrations
- ✅ How to use utility functions

The library handles all platform-specific setup, while your app defines the business logic!