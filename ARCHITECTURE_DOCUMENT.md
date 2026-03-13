# Room Database Architecture Document - KMP Project

## Table of Contents
1. [Architecture Overview](#architecture-overview)
2. [System Architecture](#system-architecture)
3. [Layer-by-Layer Architecture](#layer-by-layer-architecture)
4. [Design Patterns](#design-patterns)
5. [Data Flow](#data-flow)
6. [Platform-Specific Architecture](#platform-specific-architecture)
7. [Component Interactions](#component-interactions)
8. [Threading & Concurrency](#threading--concurrency)
9. [State Management](#state-management)
10. [Scalability Considerations](#scalability-considerations)

---

## Architecture Overview

### High-Level Architecture

This project implements a **Clean Architecture** approach with **MVVM (Model-View-ViewModel)** pattern for a Kotlin Multiplatform (KMP) application using Room Database for data persistence.

```
┌─────────────────────────────────────────────────────────────┐
│                     Presentation Layer                       │
│  ┌───────────────┐         ┌──────────────────────────┐    │
│  │  Compose UI   │ ◄─────► │      ViewModel          │    │
│  │  (UserScreen) │         │  (UserViewModel)        │    │
│  └───────────────┘         └──────────────────────────┘    │
└────────────────────────────────┬────────────────────────────┘
                                 │
┌────────────────────────────────▼────────────────────────────┐
│                      Domain Layer                            │
│  ┌──────────────────────────────────────────────────────┐  │
│  │              Repository                               │  │
│  │         (UserRepository)                             │  │
│  └──────────────────────────────────────────────────────┘  │
└────────────────────────────────┬────────────────────────────┘
                                 │
┌────────────────────────────────▼────────────────────────────┐
│                      Data Layer                              │
│  ┌──────────────┐    ┌──────────────┐   ┌──────────────┐  │
│  │    DAO       │    │   Entity     │   │  Database    │  │
│  │  (UserDao)   │    │ (UserEntity) │   │ (AppDatabase)│  │
│  └──────────────┘    └──────────────┘   └──────────────┘  │
│                                                              │
│  ┌──────────────────────────────────────────────────────┐  │
│  │         Platform-Specific Factory                     │  │
│  │  Android: Context-based  │  iOS: Path-based          │  │
│  └──────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
```

### Architecture Principles

1. **Separation of Concerns**: Each layer has a distinct responsibility
2. **Dependency Inversion**: High-level modules don't depend on low-level modules
3. **Platform Abstraction**: Common code with platform-specific implementations
4. **Reactive Programming**: Using Kotlin Flow for reactive data streams
5. **Single Source of Truth**: Database as the single source of truth

---

## System Architecture

### Project Structure

```
KMPDatabasePOC/
├── composeApp/
│   ├── src/
│   │   ├── commonMain/kotlin/com/brightly/kmpdatabasepoc/
│   │   │   ├── data/
│   │   │   │   ├── dao/
│   │   │   │   │   └── UserDao.kt              # Data Access Object
│   │   │   │   ├── database/
│   │   │   │   │   ├── AppDatabase.kt          # Database definition (expect)
│   │   │   │   │   └── DatabaseFactory.kt      # Factory (expect)
│   │   │   │   ├── entity/
│   │   │   │   │   └── UserEntity.kt           # Data model
│   │   │   │   └── repository/
│   │   │   │       └── UserRepository.kt       # Repository pattern
│   │   │   ├── ui/
│   │   │   │   ├── App.kt                      # Root composable
│   │   │   │   └── UserScreen.kt               # UI screen
│   │   │   └── viewmodel/
│   │   │       └── UserViewModel.kt            # ViewModel
│   │   ├── androidMain/kotlin/com/brightly/kmpdatabasepoc/
│   │   │   ├── MainActivity.kt                 # Android entry point
│   │   │   └── data/database/
│   │   │       └── DatabaseFactory.android.kt  # Android implementation
│   │   └── iosMain/kotlin/com/brightly/kmpdatabasepoc/
│   │       ├── MainViewController.kt           # iOS entry point
│   │       └── data/database/
│   │           └── DatabaseFactory.ios.kt      # iOS implementation
│   └── build.gradle.kts                        # Build configuration
├── gradle/
│   └── libs.versions.toml                      # Version catalog
└── gradle.properties                            # Gradle properties
```

---

## Layer-by-Layer Architecture

### 1. Presentation Layer

#### Components
- **UI (Compose)**: UserScreen.kt
- **ViewModel**: UserViewModel.kt
- **State Management**: StateFlow

```kotlin
┌──────────────────────────────────────────────────────┐
│              Presentation Layer                      │
├──────────────────────────────────────────────────────┤
│  UserScreen (Composable)                             │
│    ├─ Text Input Field                               │
│    ├─ Add Button                                     │
│    └─ User List Display                              │
│                  ▲                                    │
│                  │ observes StateFlow                │
│                  │                                    │
│  UserViewModel                                       │
│    ├─ users: StateFlow<List<String>>                │
│    ├─ loadUsers()                                    │
│    └─ addUser(name: String)                          │
└──────────────────────────────────────────────────────┘
```

**Responsibilities:**
- Display user interface
- Handle user interactions
- Observe and react to state changes
- No business logic or data access

**Key Features:**
- Reactive UI updates via StateFlow
- Lifecycle-aware via ViewModel
- Platform-independent Compose UI

### 2. Domain Layer (Repository)

```kotlin
┌──────────────────────────────────────────────────────┐
│                Domain Layer                          │
├──────────────────────────────────────────────────────┤
│  UserRepository                                      │
│    ├─ addUser(name: String): suspend                │
│    └─ getUsers(): Flow<List<UserEntity>>            │
│                  │                                    │
│                  │ delegates to DAO                  │
│                  ▼                                    │
└──────────────────────────────────────────────────────┘
```

**Responsibilities:**
- Business logic encapsulation
- Data transformation (Entity ↔ Domain models)
- Coordinate data sources
- Abstraction over data layer

**Key Features:**
- Single source of truth coordination
- Coroutine-based async operations
- Flow-based reactive streams

### 3. Data Layer

```kotlin
┌──────────────────────────────────────────────────────┐
│                  Data Layer                          │
├──────────────────────────────────────────────────────┤
│  UserDao (Interface)                                 │
│    ├─ insertUser(user: UserEntity): suspend         │
│    └─ getUsers(): Flow<List<UserEntity>>            │
│                  │                                    │
│  UserEntity (Data Class)                             │
│    ├─ @Entity(tableName = "users")                  │
│    ├─ id: Int @PrimaryKey(autoGenerate = true)     │
│    └─ name: String                                   │
│                  │                                    │
│  AppDatabase (RoomDatabase)                          │
│    ├─ @Database(entities = [...], version = 1)     │
│    ├─ @ConstructedBy(AppDatabaseConstructor)       │
│    └─ abstract fun userDao(): UserDao               │
└──────────────────────────────────────────────────────┘
```

**Responsibilities:**
- Data persistence (Room Database)
- SQL query execution
- Entity-to-table mapping
- Type-safe database operations

**Key Features:**
- Compile-time SQL validation
- Automatic code generation via KSP
- Coroutine & Flow support
- Migration support

### 4. Platform Layer (Factory Pattern)

```kotlin
┌──────────────────────────────────────────────────────┐
│              Platform Abstraction                    │
├──────────────────────────────────────────────────────┤
│  DatabaseFactory (expect class)                      │
│    └─ fun createDatabase(): AppDatabase             │
│                                                       │
├─────────────────────┬────────────────────────────────┤
│  Android (actual)   │  iOS (actual)                  │
├─────────────────────┼────────────────────────────────┤
│  - Requires Context │  - No Context needed           │
│  - Room.builder()   │  - Room.builder()              │
│  - Android SQLite   │  - BundledSQLiteDriver         │
│  - Internal storage │  - Documents directory         │
└─────────────────────┴────────────────────────────────┘
```

**Responsibilities:**
- Platform-specific database initialization
- Handle platform differences
- Provide unified interface

---

## Design Patterns

### 1. MVVM (Model-View-ViewModel)

```
View (Compose UI)  ←→  ViewModel  ←→  Repository  ←→  Data Source (Room)
```

**Benefits:**
- Clear separation of concerns
- Testability (each layer independently testable)
- Lifecycle awareness
- Reactive data flow

### 2. Repository Pattern

```kotlin
// Abstraction over data sources
class UserRepository(database: AppDatabase) {
    private val dao = database.userDao()

    suspend fun addUser(name: String) { ... }
    fun getUsers(): Flow<List<UserEntity>> { ... }
}
```

**Benefits:**
- Single source of truth
- Abstraction over data layer
- Easy to swap implementations
- Centralized data logic

### 3. Factory Pattern (expect/actual)

```kotlin
// Common code
expect class DatabaseFactory {
    fun createDatabase(): AppDatabase
}

// Platform-specific implementations
actual class DatabaseFactory(context: Context) { ... }  // Android
actual class DatabaseFactory() { ... }                   // iOS
```

**Benefits:**
- Platform abstraction
- Common interface
- Clean separation of platform code

### 4. DAO Pattern (Data Access Object)

```kotlin
@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM users")
    fun getUsers(): Flow<List<UserEntity>>
}
```

**Benefits:**
- Clean database abstraction
- Type-safe queries
- Compile-time validation
- Generated implementations

### 5. Observer Pattern (Flow & StateFlow)

```kotlin
// Publisher
repository.getUsers(): Flow<List<UserEntity>>

// Subscriber
viewModelScope.launch {
    repository.getUsers().collect { users ->
        _users.value = users
    }
}
```

**Benefits:**
- Reactive data updates
- Automatic UI updates
- Decoupled components
- Backpressure handling

### 6. Dependency Injection (Manual)

```kotlin
@Composable
fun App(databaseFactory: DatabaseFactory) {
    val database = remember { databaseFactory.createDatabase() }
    val repository = remember { UserRepository(database) }
    val viewModel: UserViewModel = viewModel { UserViewModel(repository) }
    UserScreen(viewModel)
}
```

**Benefits:**
- Testability
- Flexibility
- Loose coupling
- Easy mocking

---

## Data Flow

### Read Flow (Database → UI)

```
┌─────────────┐
│  Database   │
│   (Room)    │
└──────┬──────┘
       │ Flow<List<UserEntity>>
       ▼
┌─────────────┐
│     DAO     │
│  (UserDao)  │
└──────┬──────┘
       │ Flow<List<UserEntity>>
       ▼
┌─────────────┐
│ Repository  │
│(UserRepo)   │
└──────┬──────┘
       │ Flow<List<UserEntity>>
       ▼
┌─────────────┐
│  ViewModel  │
│ (UserVM)    │
│   collect   │
└──────┬──────┘
       │ StateFlow<List<String>>
       ▼
┌─────────────┐
│     UI      │
│ (Compose)   │
│collectAsState│
└─────────────┘
```

**Flow Characteristics:**
- **Hot Stream**: Database emits updates automatically
- **Reactive**: UI updates when data changes
- **Lifecycle-aware**: Collection stops when inactive
- **Backpressure**: Handled automatically by Flow

### Write Flow (UI → Database)

```
┌─────────────┐
│     UI      │
│  (Button)   │
└──────┬──────┘
       │ addUser("John")
       ▼
┌─────────────┐
│  ViewModel  │
│   (UserVM)  │
└──────┬──────┘
       │ viewModelScope.launch
       ▼
┌─────────────┐
│ Repository  │
│ (UserRepo)  │
└──────┬──────┘
       │ suspend addUser()
       ▼
┌─────────────┐
│     DAO     │
│  (UserDao)  │
└──────┬──────┘
       │ suspend insertUser()
       ▼
┌─────────────┐
│  Database   │
│   (Room)    │
└──────┬──────┘
       │ Triggers Flow update
       ▼
   (Back to Read Flow)
```

**Write Characteristics:**
- **Asynchronous**: Uses suspend functions
- **Coroutine-based**: Runs on background thread
- **Transaction-safe**: Room handles transactions
- **Reactive**: Triggers Flow updates automatically

---

## Platform-Specific Architecture

### Android Architecture

```
┌───────────────────────────────────────────────────┐
│              Android Platform                     │
├───────────────────────────────────────────────────┤
│  MainActivity (ComponentActivity)                 │
│    └─ onCreate()                                  │
│         └─ DatabaseFactory(applicationContext)   │
│                                                    │
│  DatabaseFactory                                  │
│    ├─ context: Context                           │
│    └─ Room.databaseBuilder()                     │
│         ├─ Context                               │
│         ├─ AppDatabase::class.java               │
│         └─ "app.db"                              │
│                                                    │
│  Storage Location:                                │
│    /data/data/com.brightly.kmpdatabasepoc/        │
│    databases/app.db                               │
│                                                    │
│  SQLite Driver:                                   │
│    Android Framework (built-in)                   │
└───────────────────────────────────────────────────┘
```

**Android-Specific Features:**
- Context-based initialization
- Internal storage by default
- Built-in SQLite support
- ProGuard/R8 compatibility

### iOS Architecture

```
┌───────────────────────────────────────────────────┐
│               iOS Platform                        │
├───────────────────────────────────────────────────┤
│  MainViewController()                             │
│    └─ ComposeUIViewController                    │
│         └─ DatabaseFactory()                     │
│                                                    │
│  DatabaseFactory                                  │
│    └─ Room.databaseBuilder<AppDatabase>()        │
│         ├─ name: "/path/to/Documents/app.db"    │
│         └─ setDriver(BundledSQLiteDriver())      │
│                                                    │
│  Storage Location:                                │
│    ~/Library/Developer/CoreSimulator/.../         │
│    Documents/app.db                               │
│                                                    │
│  SQLite Driver:                                   │
│    BundledSQLiteDriver (androidx.sqlite)          │
└───────────────────────────────────────────────────┘
```

**iOS-Specific Features:**
- No Context required
- Documents directory for storage
- BundledSQLiteDriver required
- Full file path specification

---

## Component Interactions

### Initialization Sequence

```
┌─────────────┐
│    App      │
│   Start     │
└──────┬──────┘
       │
       ▼
┌─────────────────────────────────────────┐
│  Platform Entry Point                   │
│  Android: MainActivity.onCreate()       │
│  iOS: MainViewController()              │
└──────┬──────────────────────────────────┘
       │
       ▼
┌─────────────────────────────────────────┐
│  DatabaseFactory.createDatabase()       │
│  Platform-specific implementation       │
└──────┬──────────────────────────────────┘
       │
       ▼
┌─────────────────────────────────────────┐
│  Room Database Builder                  │
│  - Loads schema from annotations        │
│  - KSP-generated implementations        │
│  - Creates database instance            │
└──────┬──────────────────────────────────┘
       │
       ▼
┌─────────────────────────────────────────┐
│  UserRepository(database)               │
│  - Gets DAO reference                   │
│  - Ready for operations                 │
└──────┬──────────────────────────────────┘
       │
       ▼
┌─────────────────────────────────────────┐
│  UserViewModel(repository)              │
│  - init { loadUsers() }                 │
│  - Starts Flow collection               │
└──────┬──────────────────────────────────┘
       │
       ▼
┌─────────────────────────────────────────┐
│  UserScreen(viewModel)                  │
│  - UI rendered                          │
│  - Ready for user interaction           │
└─────────────────────────────────────────┘
```

### Runtime Interaction Flow

```
User Action (Add User)
        │
        ▼
┌──────────────────────┐
│  TextField           │
│  "John"              │
└──────┬───────────────┘
       │
       ▼
┌──────────────────────┐
│  Button.onClick      │
└──────┬───────────────┘
       │
       ▼
┌──────────────────────────────┐
│  viewModel.addUser("John")   │
└──────┬───────────────────────┘
       │
       ▼
┌──────────────────────────────┐
│  viewModelScope.launch {     │
│    repository.addUser()      │
│  }                           │
└──────┬───────────────────────┘
       │
       ▼
┌──────────────────────────────┐
│  dao.insertUser(entity)      │
│  (suspend, IO thread)        │
└──────┬───────────────────────┘
       │
       ▼
┌──────────────────────────────┐
│  Room inserts into SQLite    │
│  (Transaction-safe)          │
└──────┬───────────────────────┘
       │
       ▼
┌──────────────────────────────┐
│  Flow emission triggered     │
│  (Invalidation tracker)      │
└──────┬───────────────────────┘
       │
       ▼
┌──────────────────────────────┐
│  ViewModel.collect {         │
│    _users.value = newList    │
│  }                           │
└──────┬───────────────────────┘
       │
       ▼
┌──────────────────────────────┐
│  UI recomposes               │
│  New user appears in list    │
└──────────────────────────────┘
```

---

## Threading & Concurrency

### Thread Model

```
┌─────────────────────────────────────────────────┐
│               Thread Architecture                │
├─────────────────────────────────────────────────┤
│  Main Thread (UI)                               │
│    ├─ Compose rendering                         │
│    ├─ StateFlow collection                      │
│    └─ User interaction handling                 │
│                                                  │
│  Background Thread (IO)                         │
│    ├─ Database writes (suspend functions)      │
│    ├─ Database queries (Flow emissions)        │
│    └─ Managed by Room & Coroutines            │
│                                                  │
│  Threading Coordination                         │
│    ├─ viewModelScope (Main context)            │
│    ├─ Dispatchers.IO (Room operations)         │
│    └─ Flow (automatic context switching)       │
└─────────────────────────────────────────────────┘
```

### Coroutine Scopes

1. **viewModelScope**
   - Tied to ViewModel lifecycle
   - Cancelled when ViewModel cleared
   - Main thread by default

2. **Room's IO Dispatcher**
   - Automatic for suspend functions
   - Efficient thread pooling
   - Automatic context switching

### Flow Threading

```kotlin
repository.getUsers()  // Executed on IO thread
    .map { ... }       // Transformation on IO thread
    .collect { ... }   // Collection on Main thread (UI)
```

**Key Points:**
- Flow emissions happen on IO thread
- Collection happens on caller's context (usually Main)
- No manual thread management needed
- Room handles optimization internally

---

## State Management

### State Architecture

```
┌─────────────────────────────────────────────────┐
│             State Management                     │
├─────────────────────────────────────────────────┤
│  Database (Single Source of Truth)              │
│         │                                        │
│         ▼                                        │
│    Flow<List<UserEntity>>                       │
│         │                                        │
│         ▼                                        │
│  ViewModel (State Holder)                       │
│    StateFlow<List<String>>                      │
│         │                                        │
│         ▼                                        │
│  Compose UI (State Consumer)                    │
│    collectAsState()                             │
└─────────────────────────────────────────────────┘
```

### State Types

1. **Persistent State (Database)**
   - Source of truth
   - Survives app restart
   - Automatically synchronized

2. **ViewModel State (StateFlow)**
   - Survives configuration changes
   - Lifecycle-aware
   - Reactive updates

3. **UI State (Compose)**
   - Transient state (e.g., text field)
   - Recomposition-driven
   - Temporary interactions

### State Synchronization

```kotlin
// Automatic synchronization
init {
    viewModelScope.launch {
        repository.getUsers()           // Database state
            .map { list -> list.map { it.name } }
            .collect { userNames ->
                _users.value = userNames  // ViewModel state
            }
    }
}

// UI automatically updates
@Composable
fun UserScreen(viewModel: UserViewModel) {
    val users by viewModel.users.collectAsState()  // UI state
    // ... render users
}
```

**Benefits:**
- Automatic UI updates
- No manual refresh needed
- Consistent state across layers
- Single source of truth

---

## Scalability Considerations

### Current Architecture Scalability

#### Strengths
1. **Modular Design**: Easy to add new entities/features
2. **Layer Separation**: Can replace components independently
3. **Platform Abstraction**: Easy to add new platforms
4. **Reactive**: Efficient updates, no polling needed

#### Extension Points

1. **Adding New Entities**
```kotlin
// 1. Create Entity
@Entity(tableName = "products")
data class ProductEntity(...)

// 2. Create DAO
@Dao
interface ProductDao { ... }

// 3. Update Database
@Database(
    entities = [UserEntity::class, ProductEntity::class],
    version = 2  // Increment version
)
```

2. **Database Migrations**
```kotlin
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("CREATE TABLE products (...)")
    }
}

Room.databaseBuilder(...)
    .addMigrations(MIGRATION_1_2)
    .build()
```

3. **Multiple Data Sources**
```kotlin
class UserRepository(
    private val localDatabase: AppDatabase,
    private val remoteApi: ApiService
) {
    // Implement cache strategy
    suspend fun getUsers(): Flow<List<User>> {
        // Fetch from remote, update local
        // Return local data
    }
}
```

4. **Dependency Injection**
```kotlin
// Can be migrated to Koin or similar
object Dependencies {
    val database: AppDatabase by lazy { ... }
    val repository: UserRepository by lazy { UserRepository(database) }
}
```

### Performance Considerations

1. **Database Indexing**
```kotlin
@Entity(
    tableName = "users",
    indices = [Index(value = ["name"])]  // Index for faster queries
)
```

2. **Pagination**
```kotlin
@Query("SELECT * FROM users LIMIT :limit OFFSET :offset")
suspend fun getUsersPaged(limit: Int, offset: Int): List<UserEntity>
```

3. **Lazy Loading**
```kotlin
// Load only when needed
val users: Flow<List<UserEntity>> = dao.getUsers()
    .flowOn(Dispatchers.IO)
```

4. **Transaction Batching**
```kotlin
@Transaction
suspend fun insertMultipleUsers(users: List<UserEntity>) {
    users.forEach { dao.insertUser(it) }
}
```

### Future Enhancements

1. **Offline-First with Sync**
   - Local database as cache
   - Background sync with remote
   - Conflict resolution

2. **Multi-Module Architecture**
   - Feature modules
   - Shared data module
   - Platform-specific modules

3. **Testing Infrastructure**
   - In-memory database for tests
   - Mock repositories
   - UI testing with fake data

4. **Monitoring & Analytics**
   - Database query performance
   - Error tracking
   - Usage analytics

---

## Security Considerations

### Data Security

1. **Database Encryption** (Future Enhancement)
```kotlin
// Android: SQLCipher
val passphrase = ...
Room.databaseBuilder(...)
    .openHelperFactory(SupportFactory(passphrase))
    .build()
```

2. **Secure Storage**
   - Android: Internal storage (app-private)
   - iOS: Documents directory (app sandbox)

3. **SQL Injection Prevention**
   - Room uses prepared statements
   - Type-safe queries
   - No raw SQL concatenation

### Access Control

1. **Context Isolation** (Android)
   - Database accessible only to app
   - No external access without root

2. **Sandbox Isolation** (iOS)
   - App container isolation
   - No cross-app access

---

## Monitoring & Debugging

### Debug Tools

1. **Database Inspector** (Android Studio)
   - View tables in real-time
   - Execute queries
   - Inspect data

2. **Logging**
```kotlin
Room.databaseBuilder(...)
    .setQueryCallback({ sqlQuery, bindArgs ->
        Log.d("RoomQuery", "Query: $sqlQuery")
    }, Dispatchers.IO)
    .build()
```

3. **Export Database** (Development)
```kotlin
// Android
adb pull /data/data/com.brightly.kmpdatabasepoc/databases/app.db

// iOS
// Access via Xcode device container
```

---

## Conclusion

This architecture provides:
- ✅ Clean separation of concerns
- ✅ Platform abstraction for iOS/Android
- ✅ Reactive data flow with Flow
- ✅ Type-safe database operations
- ✅ Scalable and maintainable structure
- ✅ Testable components
- ✅ Modern Kotlin idioms

The design allows for easy extension, platform additions, and feature growth while maintaining code quality and performance.

---

## Architecture Diagram Legend

```
┌─────┐
│     │  = Component/Layer
└─────┘

   │     = Data Flow
   ▼

   ◄──►  = Bidirectional Communication

   ├──   = Relationship/Dependency
```

---

**Document Version:** 1.0
**Last Updated:** March 12, 2026
**Project:** KMPDatabasePOC
**Architecture Pattern:** Clean Architecture + MVVM