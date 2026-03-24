# Visual Diagrams & Workflows

This document contains visual representations of the KMP Room Core library architecture and workflows.

---

## System Architecture Diagram

```
┌──────────────────────────────────────────────────────────────────────┐
│                       KMP Application                                 │
│                                                                       │
│  ┌────────────────────────────────────────────────────────────────┐ │
│  │                    Presentation Layer                           │ │
│  │  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐           │ │
│  │  │  Android UI │  │   iOS UI    │  │  Desktop UI │           │ │
│  │  │  (Compose)  │  │  (Compose)  │  │  (Compose)  │           │ │
│  │  └──────┬──────┘  └──────┬──────┘  └──────┬──────┘           │ │
│  │         │                 │                 │                  │ │
│  │         └─────────────────┴─────────────────┘                  │ │
│  │                           │                                     │ │
│  │  ┌────────────────────────▼────────────────────────┐           │ │
│  │  │               ViewModels                         │           │ │
│  │  │  - UserViewModel                                │           │ │
│  │  │  - ProductViewModel                             │           │ │
│  │  │  - State Management (StateFlow)                │           │ │
│  │  │  - Coroutine Scopes (viewModelScope)          │           │ │
│  │  └────────────────────────┬────────────────────────┘           │ │
│  └───────────────────────────┼─────────────────────────────────────┘ │
│                              │                                        │
│  ┌───────────────────────────▼─────────────────────────────────────┐ │
│  │                    Domain Layer (Optional)                        │ │
│  │  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐         │ │
│  │  │  Use Cases   │  │   Business   │  │   Domain     │         │ │
│  │  │  (Optional)  │  │     Logic    │  │   Models     │         │ │
│  │  └──────────────┘  └──────────────┘  └──────────────┘         │ │
│  └───────────────────────────┬─────────────────────────────────────┘ │
│                              │                                        │
│  ┌───────────────────────────▼─────────────────────────────────────┐ │
│  │                        Data Layer                                 │ │
│  │  ┌──────────────────────────────────────────────────────────┐   │ │
│  │  │              Repositories                                 │   │ │
│  │  │  ┌────────────────┐    ┌────────────────┐               │   │ │
│  │  │  │ UserRepository │    │ProductRepository│              │   │ │
│  │  │  └────────┬───────┘    └────────┬───────┘               │   │ │
│  │  │           │                     │                        │   │ │
│  │  │           └──────────┬──────────┘                        │   │ │
│  │  │                      │                                   │   │ │
│  │  │           ┌──────────▼──────────┐                        │   │ │
│  │  │           │  BaseRepository<E,D>│                        │   │ │
│  │  │           │  - add(entity)      │                        │   │ │
│  │  │           │  - update(entity)   │                        │   │ │
│  │  │           │  - delete(entity)   │                        │   │ │
│  │  │           │  - getAll()         │                        │   │ │
│  │  │           └──────────┬──────────┘                        │   │ │
│  │  └──────────────────────┼────────────────────────────────────┘   │ │
│  │                         │                                        │ │
│  │  ┌──────────────────────▼────────────────────────────────────┐   │ │
│  │  │                  DAOs (Data Access Objects)               │   │ │
│  │  │  ┌────────────┐    ┌────────────┐    ┌────────────┐     │   │ │
│  │  │  │  UserDao   │    │ ProductDao │    │  OrderDao  │     │   │ │
│  │  │  └──────┬─────┘    └──────┬─────┘    └──────┬─────┘     │   │ │
│  │  │         │                  │                 │            │   │ │
│  │  │         └──────────────────┴─────────────────┘            │   │ │
│  │  │                            │                              │   │ │
│  │  │                 ┌──────────▼──────────┐                  │   │ │
│  │  │                 │      BaseDao<T>     │                  │   │ │
│  │  │                 │  @Insert            │                  │   │ │
│  │  │                 │  @Update            │                  │   │ │
│  │  │                 │  @Delete            │                  │   │ │
│  │  │                 │  @Query             │                  │   │ │
│  │  │                 └──────────┬──────────┘                  │   │ │
│  │  └────────────────────────────┼──────────────────────────────┘   │ │
│  │                               │                                  │ │
│  │  ┌────────────────────────────▼──────────────────────────────┐   │ │
│  │  │                Room Database                              │   │ │
│  │  │  ┌──────────────────────────────────────────────────┐    │   │ │
│  │  │  │           AppDatabase : RoomDatabase             │    │   │ │
│  │  │  │  - userDao(): UserDao                            │    │   │ │
│  │  │  │  - productDao(): ProductDao                      │    │   │ │
│  │  │  │  @Database(entities, version)                    │    │   │ │
│  │  │  └───────────────────┬──────────────────────────────┘    │   │ │
│  │  └──────────────────────┼──────────────────────────────────────┘ │
│  │                         │                                        │ │
│  │  ┌──────────────────────▼──────────────────────────────────────┐ │
│  │  │         kmp-room-core Library (This Library)                │ │
│  │  │  ┌─────────────────────────────────────────────────────┐   │ │
│  │  │  │        BaseDatabaseFactory<T : RoomDatabase>        │   │ │
│  │  │  │  - abstract createDatabase(): T                     │   │ │
│  │  │  │  - Platform-specific implementations               │   │ │
│  │  │  └──────────────────┬──────────────────────────────────┘   │ │
│  │  └────────────────────┼────────────────────────────────────────┘ │
│  └───────────────────────┼─────────────────────────────────────────┘ │
│                          │                                           │
│  ┌───────────────────────▼──────────────────────────────────────────┐│
│  │              Platform-Specific Layer (expect/actual)              ││
│  │  ┌─────────────────┐  ┌──────────────────┐  ┌─────────────────┐││
│  │  │    Android      │  │       iOS        │  │     Desktop     │││
│  │  │  Context-based  │  │  NSHomeDirectory │  │   File-based    │││
│  │  │  Room Builder   │  │   Room Builder   │  │   Room Builder  │││
│  │  └────────┬────────┘  └────────┬─────────┘  └────────┬────────┘││
│  └───────────┼──────────────────────┼──────────────────────┼─────────┘│
└──────────────┼──────────────────────┼──────────────────────┼──────────┘
               │                      │                      │
               ▼                      ▼                      ▼
          ┌─────────┐           ┌─────────┐           ┌─────────┐
          │ SQLite  │           │ SQLite  │           │ SQLite  │
          │(Android)│           │  (iOS)  │           │(Desktop)│
          └─────────┘           └─────────┘           └─────────┘
```

---

## Component Interaction Flow

```
┌──────────────┐
│  User Action │
│ (Button Click)│
└──────┬───────┘
       │
       ▼
┌──────────────────────────────────────┐
│       Compose UI Component           │
│  - Collects StateFlow                │
│  - Displays data                     │
│  - Captures user input               │
└──────┬───────────────────────────────┘
       │ viewModel.addUser(name)
       ▼
┌──────────────────────────────────────┐
│          ViewModel                   │
│  - Manages UI State (StateFlow)      │
│  - Launches coroutines               │
│  - Handles business logic            │
└──────┬───────────────────────────────┘
       │ viewModelScope.launch {
       │   repository.add(user)
       │ }
       ▼
┌──────────────────────────────────────┐
│         Repository                   │
│  - Extends BaseRepository            │
│  - Provides data operations          │
│  - Transforms data if needed         │
└──────┬───────────────────────────────┘
       │ dao.insert(entity)
       ▼
┌──────────────────────────────────────┐
│            DAO                       │
│  - Extends BaseDao                   │
│  - Room annotations (@Insert, etc.)  │
│  - SQL queries                       │
└──────┬───────────────────────────────┘
       │ Generated SQL
       ▼
┌──────────────────────────────────────┐
│       Room Database                  │
│  - Manages SQLite connection         │
│  - Executes queries                  │
│  - Handles transactions              │
└──────┬───────────────────────────────┘
       │ SQL Commands
       ▼
┌──────────────────────────────────────┐
│         SQLite                       │
│  - Stores data on disk               │
│  - ACID transactions                 │
│  - Query execution                   │
└──────┬───────────────────────────────┘
       │ Data Changed
       ▼
┌──────────────────────────────────────┐
│    Flow Emission (Reactive)          │
│  - Room detects data change          │
│  - Emits new data through Flow       │
└──────┬───────────────────────────────┘
       │
       ▼ (Flows back up the chain)
┌──────────────────────────────────────┐
│         DAO → Repository             │
│    → ViewModel → UI (StateFlow)      │
│          UI Updates                  │
└──────────────────────────────────────┘
```

---

## Library Creation Workflow

```
START
  │
  ▼
┌─────────────────────────────────┐
│ Create KMP Library Module       │
│ - Module name: kmp-room-core    │
│ - Package: com.brightly.kmp...  │
└───────────┬─────────────────────┘
            │
            ▼
┌─────────────────────────────────┐
│ Configure build.gradle.kts      │
│ - Add KMP plugin                │
│ - Configure targets (Android/iOS)│
│ - Add dependencies (Room, etc.)  │
│ - Setup publishing              │
└───────────┬─────────────────────┘
            │
            ▼
┌─────────────────────────────────┐
│ Create Source Structure         │
│ /commonMain                     │
│   /kotlin                       │
│     /base                       │
│       - BaseDatabaseFactory.kt  │
│       - BaseRepository.kt       │
│       - BaseDao.kt              │
└───────────┬─────────────────────┘
            │
            ▼
┌─────────────────────────────────┐
│ Create Android Implementation   │
│ /androidMain                    │
│   /kotlin                       │
│     /android                    │
│       - AndroidDatabaseFactory  │
│         (uses Context)          │
└───────────┬─────────────────────┘
            │
            ▼
┌─────────────────────────────────┐
│ Create iOS Implementation       │
│ /iosMain                        │
│   /kotlin                       │
│     /ios                        │
│       - IosDatabaseFactory      │
│         (uses NSHomeDirectory)  │
└───────────┬─────────────────────┘
            │
            ▼
┌─────────────────────────────────┐
│ Build Library                   │
│ ./gradlew :kmp-room-core:build │
└───────────┬─────────────────────┘
            │
            ▼
        ┌───┴────┐
        │ Build  │
        │Success?│
        └───┬────┘
            │
    ┌───────┴────────┐
    │                │
   NO               YES
    │                │
    ▼                ▼
┌───────┐    ┌──────────────────┐
│  Fix  │    │ Test Locally     │
│ Errors│    │ - Integration    │
└───┬───┘    │ - Unit tests     │
    │        └────────┬─────────┘
    │                 │
    │                 ▼
    │        ┌──────────────────┐
    │        │ Setup Publishing │
    │        │ - GitHub token   │
    │        │ - Maven config   │
    │        └────────┬─────────┘
    │                 │
    │                 ▼
    │        ┌──────────────────┐
    │        │ Publish Library  │
    │        │ ./gradlew publish│
    │        └────────┬─────────┘
    │                 │
    │                 ▼
    │        ┌──────────────────┐
    │        │ Verify on GitHub │
    │        │ Packages         │
    │        └────────┬─────────┘
    │                 │
    │                 ▼
    │               END
    │
    └──────> (Loop back to build)
```

---

## Application Integration Workflow

```
START: New KMP App Project
         │
         ▼
┌──────────────────────────────────┐
│ Add Maven Repository             │
│ (GitHub Packages)                │
└─────────┬────────────────────────┘
          │
          ▼
┌──────────────────────────────────┐
│ Add Library Dependency           │
│ implementation(                  │
│   "com.brightly:kmp-room-core:X" │
│ )                                │
└─────────┬────────────────────────┘
          │
          ▼
┌──────────────────────────────────┐
│ Setup Credentials                │
│ ~/.gradle/gradle.properties      │
│ - gpr.user                       │
│ - gpr.token                      │
└─────────┬────────────────────────┘
          │
          ▼
┌──────────────────────────────────┐
│ Sync Gradle                      │
└─────────┬────────────────────────┘
          │
      ┌───┴────┐
      │ Sync   │
      │Success?│
      └───┬────┘
          │
    ┌─────┴─────┐
    │           │
   NO          YES
    │           │
    ▼           ▼
┌───────┐  ┌────────────────────────┐
│ Check │  │ Define Entities        │
│ Token │  │ @Entity UserEntity     │
└───┬───┘  └──────────┬─────────────┘
    │                 │
    │                 ▼
    │        ┌────────────────────────┐
    │        │ Create DAOs            │
    │        │ interface UserDao      │
    │        │   : BaseDao<User>      │
    │        └──────────┬─────────────┘
    │                   │
    │                   ▼
    │        ┌────────────────────────┐
    │        │ Define Database        │
    │        │ @Database              │
    │        │ AppDatabase            │
    │        └──────────┬─────────────┘
    │                   │
    │                   ▼
    │        ┌────────────────────────┐
    │        │ Create DatabaseFactory │
    │        │ expect/actual pattern  │
    │        └──────────┬─────────────┘
    │                   │
    │                   ▼
    │        ┌────────────────────────┐
    │        │ Create Repositories    │
    │        │ extend BaseRepository  │
    │        └──────────┬─────────────┘
    │                   │
    │                   ▼
    │        ┌────────────────────────┐
    │        │ Create ViewModels      │
    │        │ StateFlow management   │
    │        └──────────┬─────────────┘
    │                   │
    │                   ▼
    │        ┌────────────────────────┐
    │        │ Initialize in App      │
    │        │ DatabaseFactory(...)   │
    │        └──────────┬─────────────┘
    │                   │
    │                   ▼
    │        ┌────────────────────────┐
    │        │ Create UI (Compose)    │
    │        │ Collect StateFlows     │
    │        └──────────┬─────────────┘
    │                   │
    │                   ▼
    │        ┌────────────────────────┐
    │        │ Build & Test           │
    │        │ - Android              │
    │        │ - iOS                  │
    │        └──────────┬─────────────┘
    │                   │
    │                   ▼
    │                  END
    │
    └────> (Loop back to sync)
```

---

## Data Flow Diagram (CRUD Operations)

### CREATE (Add)

```
User Input (Form)
      │
      ▼
┌──────────────┐
│ Compose UI   │ Button Click
└──────┬───────┘
       │ onClick { viewModel.addUser(name) }
       ▼
┌──────────────┐
│  ViewModel   │ viewModelScope.launch
└──────┬───────┘
       │ repository.add(UserEntity(...))
       ▼
┌──────────────┐
│  Repository  │ suspend fun add(entity)
└──────┬───────┘
       │ dao.insert(entity)
       ▼
┌──────────────┐
│     DAO      │ @Insert suspend fun
└──────┬───────┘
       │ SQL: INSERT INTO users VALUES(...)
       ▼
┌──────────────┐
│ Room/SQLite  │ Execute INSERT
└──────┬───────┘
       │ Row Added
       ▼
┌──────────────┐
│ Flow Trigger │ Room detects change
└──────┬───────┘
       │
       ▼ (Reactive update)
   UI Updates
```

### READ (Query)

```
App Initialization / Screen Load
      │
      ▼
┌──────────────┐
│  ViewModel   │ init { loadData() }
└──────┬───────┘
       │ repository.getAll()
       ▼
┌──────────────┐
│  Repository  │ fun getAll(): Flow<List<T>>
└──────┬───────┘
       │ dao.getAll()
       ▼
┌──────────────┐
│     DAO      │ @Query("SELECT * FROM...")
└──────┬───────┘
       │ Flow<List<Entity>>
       ▼
┌──────────────┐
│ Room/SQLite  │ Execute SELECT
└──────┬───────┘
       │ Return rows as Flow
       ▼
┌──────────────┐
│  ViewModel   │ .collect { data ->
│              │   _state.value = data
│              │ }
└──────┬───────┘
       │ StateFlow emission
       ▼
┌──────────────┐
│ Compose UI   │ collectAsState()
│              │ LazyColumn(items)
└──────────────┘
```

### UPDATE

```
User Edit Action
      │
      ▼
┌──────────────┐
│ Compose UI   │ Save Button
└──────┬───────┘
       │ viewModel.updateUser(updatedEntity)
       ▼
┌──────────────┐
│  ViewModel   │ viewModelScope.launch
└──────┬───────┘
       │ repository.update(entity)
       ▼
┌──────────────┐
│  Repository  │ suspend fun update(entity)
└──────┬───────┘
       │ dao.update(entity)
       ▼
┌──────────────┐
│     DAO      │ @Update suspend fun
└──────┬───────┘
       │ SQL: UPDATE users SET ... WHERE id=...
       ▼
┌──────────────┐
│ Room/SQLite  │ Execute UPDATE
└──────┬───────┘
       │ Row Updated
       ▼
   Flow triggers -> UI updates automatically
```

### DELETE

```
User Delete Action
      │
      ▼
┌──────────────┐
│ Compose UI   │ Delete Button (with confirmation)
└──────┬───────┘
       │ viewModel.deleteUser(entity)
       ▼
┌──────────────┐
│  ViewModel   │ viewModelScope.launch
└──────┬───────┘
       │ repository.delete(entity)
       ▼
┌──────────────┐
│  Repository  │ suspend fun delete(entity)
└──────┬───────┘
       │ dao.delete(entity)
       ▼
┌──────────────┐
│     DAO      │ @Delete suspend fun
└──────┬───────┘
       │ SQL: DELETE FROM users WHERE id=...
       ▼
┌──────────────┐
│ Room/SQLite  │ Execute DELETE
└──────┬───────┘
       │ Row Deleted
       ▼
   Flow triggers -> UI updates automatically
```

---

## Publishing Workflow

```
Code Changes Made
       │
       ▼
┌─────────────────────┐
│ Update Version      │
│ build.gradle.kts:   │
│ version = "1.0.X"   │
└─────────┬───────────┘
          │
          ▼
┌─────────────────────┐
│ Update CHANGELOG.md │
│ Document changes    │
└─────────┬───────────┘
          │
          ▼
┌─────────────────────┐
│ Commit Changes      │
│ git commit -m "..." │
└─────────┬───────────┘
          │
          ▼
┌─────────────────────┐
│ Create Git Tag      │
│ git tag vX.Y.Z      │
└─────────┬───────────┘
          │
          ▼
┌─────────────────────┐
│ Run Tests           │
│ ./gradlew test      │
└─────────┬───────────┘
          │
      ┌───┴────┐
      │ Tests  │
      │ Pass?  │
      └───┬────┘
          │
    ┌─────┴─────┐
    │           │
   NO          YES
    │           │
    ▼           ▼
┌───────┐  ┌────────────────────┐
│  Fix  │  │ Build Library      │
│ Tests │  │ ./gradlew build    │
└───┬───┘  └──────────┬─────────┘
    │                 │
    │             ┌───┴────┐
    │             │ Build  │
    │             │Success?│
    │             └───┬────┘
    │                 │
    │           ┌─────┴─────┐
    │           │           │
    │          NO          YES
    │           │           │
    │           ▼           ▼
    │      ┌────────┐  ┌──────────────────┐
    │      │  Fix   │  │ Publish Library  │
    │      │ Errors │  │ ./gradlew publish│
    │      └───┬────┘  └────────┬─────────┘
    │          │                │
    │          │            ┌───┴────┐
    │          │            │Publish │
    │          │            │Success?│
    │          │            └───┬────┘
    │          │                │
    │          │          ┌─────┴─────┐
    │          │          │           │
    │          │         NO          YES
    │          │          │           │
    │          │          ▼           ▼
    │          │     ┌────────┐  ┌──────────────┐
    │          │     │ Check  │  │ Push Git Tag │
    │          │     │ Token/ │  │ git push     │
    │          │     │ Config │  │ --tags       │
    │          │     └───┬────┘  └──────┬───────┘
    │          │         │              │
    │          │         │              ▼
    │          │         │     ┌──────────────────┐
    │          │         │     │ Verify on GitHub │
    │          │         │     │ Packages page    │
    │          │         │     └────────┬─────────┘
    │          │         │              │
    │          │         │              ▼
    │          │         │     ┌──────────────────┐
    │          │         │     │ Update Docs      │
    │          │         │     │ README.md etc.   │
    │          │         │     └────────┬─────────┘
    │          │         │              │
    │          │         │              ▼
    │          │         │            END
    │          │         │
    └──────────┴─────────┴───> Loop back to respective step
```

---

## Platform Abstraction (expect/actual)

```
┌─────────────────────────────────────────────────────────┐
│                    commonMain                            │
│  ┌───────────────────────────────────────────────────┐  │
│  │   expect class DatabaseFactory<T: RoomDatabase>   │  │
│  │      abstract fun createDatabase(): T             │  │
│  └───────────────────────────────────────────────────┘  │
└────────────────────┬────────────────────────────────────┘
                     │
          ┌──────────┴──────────┐
          │                     │
          ▼                     ▼
┌─────────────────────┐  ┌────────────────────┐
│    androidMain      │  │      iosMain       │
│ ┌─────────────────┐ │  │ ┌────────────────┐ │
│ │ actual class    │ │  │ │ actual class   │ │
│ │ DatabaseFactory │ │  │ │ DatabaseFactory│ │
│ │ (context:       │ │  │ │ () {           │ │
│ │  Context)       │ │  │ │                │ │
│ │                 │ │  │ │ Uses:          │ │
│ │ Uses:           │ │  │ │ NSHomeDir()    │ │
│ │ context         │ │  │ │                │ │
│ │ .appContext     │ │  │ │ Room.builder(  │ │
│ │ .getDatabasePath│ │  │ │   name = path  │ │
│ │                 │ │  │ │ )              │ │
│ │ Room.builder(   │ │  │ └────────────────┘ │
│ │   context=...   │ │  └────────────────────┘
│ │   klass=...     │ │
│ │   name=path     │ │
│ │ )               │ │
│ └─────────────────┘ │
└─────────────────────┘

          │                     │
          └─────────┬───────────┘
                    ▼
         ┌──────────────────────┐
         │    Application       │
         │  Creates factory:    │
         │                      │
         │  Android:            │
         │  DatabaseFactory(    │
         │    context           │
         │  )                   │
         │                      │
         │  iOS:                │
         │  DatabaseFactory()   │
         │  (no args)           │
         └──────────────────────┘
```

---

## State Management Flow

```
                    Application Lifecycle
                            │
                ┌───────────┴───────────┐
                │                       │
                ▼                       ▼
         ┌─────────────┐        ┌─────────────┐
         │   Android   │        │     iOS     │
         │  Activity   │        │ Controller  │
         └──────┬──────┘        └──────┬──────┘
                │                      │
                └──────────┬───────────┘
                           ▼
                  ┌─────────────────┐
                  │  App(@Composable)│
                  └────────┬─────────┘
                           │
                           ▼
         ┌─────────────────────────────────────┐
         │  ViewModel (ViewModelScope)         │
         │  ┌───────────────────────────────┐  │
         │  │  Private MutableStateFlow     │  │
         │  │  _state = MutableStateFlow()  │  │
         │  └──────────┬────────────────────┘  │
         │             │                        │
         │             │ .asStateFlow()         │
         │             │                        │
         │  ┌──────────▼────────────────────┐  │
         │  │  Public StateFlow (Read-only) │  │
         │  │  val state: StateFlow<T>      │  │
         │  └──────────┬────────────────────┘  │
         └─────────────┼───────────────────────┘
                       │
                       │ Data Changes
                       ▼
         ┌─────────────────────────────────┐
         │      Repository                  │
         │  ┌───────────────────────────┐  │
         │  │  DAO.getAll()             │  │
         │  │  Returns Flow<List<T>>    │  │
         │  └───────────┬───────────────┘  │
         └──────────────┼──────────────────┘
                        │
                        ▼
         ┌──────────────────────────────────┐
         │       Room Database              │
         │  - Observes table changes        │
         │  - Emits new data via Flow       │
         │  - Reactive updates              │
         └──────────────┬───────────────────┘
                        │
                        │ SQL change triggers
                        │ new emission
                        ▼
                 (Flows back up)
                        │
                        ▼
         ┌──────────────────────────────────┐
         │    Compose UI                    │
         │  val state by viewModel          │
         │              .state              │
         │              .collectAsState()   │
         │                                  │
         │  LazyColumn(items = state) {    │
         │    // Render items              │
         │  }                               │
         │                                  │
         │  (UI recomposes on state change) │
         └──────────────────────────────────┘
```

---

## Thread Safety & Coroutines

```
┌─────────────────────────────────────────────────────────┐
│                     Main Thread                         │
│  ┌───────────────────────────────────────────────────┐  │
│  │              Compose UI                           │  │
│  │  - Rendering                                      │  │
│  │  - collectAsState() - Runs on Main                │  │
│  │  - Recomposition                                  │  │
│  └───────────────┬───────────────────────────────────┘  │
└──────────────────┼──────────────────────────────────────┘
                   │
                   ▼
┌──────────────────────────────────────────────────────────┐
│                     ViewModel                            │
│  viewModelScope.launch {                                 │
│      │                                                    │
│      ├─> Dispatchers.Main (default)                     │
│      │   - State updates (_state.value = ...)          │
│      │   - UI-related operations                        │
│      │                                                   │
│      └─> Switches to IO for database ops               │
│          withContext(Dispatchers.IO) {                  │
│              repository.add(entity)                     │
│          }                                               │
│  }                                                       │
└──────────────────┬───────────────────────────────────────┘
                   │
                   ▼
┌──────────────────────────────────────────────────────────┐
│                    Repository                            │
│  suspend fun add(entity: E): Long {                     │
│      return dao.insert(entity)  // Runs on IO          │
│  }                                                       │
│                                                          │
│  fun getAll(): Flow<List<E>> {                          │
│      return dao.getAll()  // Flow collection on IO     │
│  }                                                       │
└──────────────────┬───────────────────────────────────────┘
                   │
                   ▼
┌──────────────────────────────────────────────────────────┐
│                        DAO                               │
│  @Insert                                                 │
│  suspend fun insert(entity: E): Long                    │
│  // Room automatically uses Dispatchers.IO              │
│                                                          │
│  @Query("SELECT * FROM table")                          │
│  fun getAll(): Flow<List<E>>                            │
│  // Room emits on background thread                     │
└──────────────────┬───────────────────────────────────────┘
                   │
                   ▼
┌──────────────────────────────────────────────────────────┐
│                  Room Database                           │
│  ┌────────────────────────────────────────────────────┐ │
│  │         Background Thread Pool                     │ │
│  │  - Query execution                                 │ │
│  │  - Transaction management                          │ │
│  │  - SQLite operations                               │ │
│  └────────────────────────────────────────────────────┘ │
└──────────────────┬───────────────────────────────────────┘
                   │
                   ▼
┌──────────────────────────────────────────────────────────┐
│                      SQLite                              │
│  - Disk I/O operations                                   │
│  - Database file access                                  │
│  - Transaction commits                                   │
└──────────────────────────────────────────────────────────┘

Flow emissions:
  Room DB (Background) → DAO → Repository → ViewModel
                                              ↓
                              StateFlow (collects on Main)
                                              ↓
                                         Compose UI
```

---

**Note:** These are conceptual diagrams. For interactive diagrams, consider using tools like:
- Mermaid (for flowcharts)
- PlantUML (for UML diagrams)
- Draw.io (for custom diagrams)

---

**Last Updated:** March 24, 2026