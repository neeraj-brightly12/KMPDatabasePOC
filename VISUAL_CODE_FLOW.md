# Visual Code Flow Diagrams

Complete visual representation of how consumer app uses kmp-room-core library.

---

## Diagram 1: Component Relationships

```
┌────────────────────────────────────────────────────────────────┐
│                        CONSUMER APP                             │
├────────────────────────────────────────────────────────────────┤
│                                                                 │
│  ┌─────────────┐   ┌─────────────┐   ┌──────────────┐        │
│  │ UserEntity  │   │   UserDao   │   │ AppDatabase  │        │
│  │             │   │  (interface)│   │  (abstract)  │        │
│  │ @Entity     │   │   @Dao      │   │  @Database   │        │
│  │ data class  │   │   @Query    │   │              │        │
│  └─────────────┘   └─────────────┘   └──────────────┘        │
│        │                  │                   │                │
│        └──────────────────┼───────────────────┘                │
│                           │                                    │
│                           │ Processed by                       │
│                           ▼                                    │
│               ┌───────────────────────┐                        │
│               │         KSP           │                        │
│               │  (room-compiler)      │                        │
│               └───────────────────────┘                        │
│                           │                                    │
│                           │ Generates                          │
│                           ▼                                    │
│  ┌──────────────────┐   ┌────────────────────┐               │
│  │ UserDao_Impl     │   │ AppDatabaseConstr  │               │
│  │ (implementation) │   │      uctor         │               │
│  └──────────────────┘   └────────────────────┘               │
│            │                       │                           │
│            └───────────┬───────────┘                           │
│                        │                                       │
│  ┌─────────────────────┴─────────────────┐                   │
│  │    DatabaseFactory (expect/actual)     │                   │
│  │                                        │                   │
│  │  Android:                iOS:          │                   │
│  │  extends Android     extends iOS       │                   │
│  │  DatabaseFactory     DatabaseFactory   │                   │
│  └────────────────────────────────────────┘                   │
│            │                       │                           │
│            │                       │                           │
└────────────┼───────────────────────┼───────────────────────────┘
             │                       │
             │ Uses library          │ Uses library
             ▼                       ▼
┌────────────────────────────────────────────────────────────────┐
│                    KMP-ROOM-CORE LIBRARY                        │
├────────────────────────────────────────────────────────────────┤
│                                                                 │
│  ┌────────────────────┐          ┌────────────────────┐       │
│  │ AndroidDatabase    │          │ IosDatabaseFactory │       │
│  │      Factory       │          │   <T : RoomDB>     │       │
│  │  <T : RoomDB>      │          │                    │       │
│  │                    │          │                    │       │
│  │ + buildDatabase()  │          │ + buildDatabase()  │       │
│  └────────────────────┘          └────────────────────┘       │
│            │                               │                   │
│            │                               │                   │
│  ┌─────────┴───────────────────────────────┴─────────┐        │
│  │            DatabaseConfig                          │        │
│  │  - name: String                                    │        │
│  │  - version: Int                                    │        │
│  │  - enableLogging: Boolean                          │        │
│  │  - migrations: List<Migration>                     │        │
│  └────────────────────────────────────────────────────┘        │
│                                                                 │
└────────────────────────────────────────────────────────────────┘
             │                       │
             │ Uses Room API         │ Uses Room API
             ▼                       ▼
┌────────────────────────────────────────────────────────────────┐
│                        ROOM LIBRARY                             │
│                  (androidx.room:room-runtime)                   │
│                                                                 │
│  Room.databaseBuilder() → Creates database instances           │
│  RoomDatabase → Base class for all databases                   │
│  SQLite operations → Read/write data                           │
└────────────────────────────────────────────────────────────────┘
```

---

## Diagram 2: Method Call Sequence

```
MainActivity.onCreate()
     │
     ├─────────────────────────────────────────────────────────┐
     │                                                          │
     ▼                                                          │
DatabaseFactory(context)                                        │
     │                                                          │
     ▼                                                          │
createDatabase()  ← Call actual function                        │
     │                                                          │
     ▼                                                          │
createDatabase("app.db", [])  ← Call override                   │
     │                                                          │
     ▼                                                          │
buildDatabase(                                                  │
    AppDatabase::class.java,                                    │
    DatabaseConfig(...)                                         │
)  ← Call library method                                        │
     │                                                          │
     │  Inside AndroidDatabaseFactory                           │
     ▼                                                          │
Room.databaseBuilder(                                           │
    context,           ← From constructor                       │
    AppDatabase,       ← Parameter                              │
    "app.db"          ← From config                            │
)                                                               │
     │                                                          │
     ▼                                                          │
.addMigrations(...)  ← From config                             │
     │                                                          │
     ▼                                                          │
.setQueryCallback(...)  ← If logging enabled                   │
     │                                                          │
     ▼                                                          │
.build()  ← Build database                                     │
     │                                                          │
     │  Room calls                                              │
     ▼                                                          │
AppDatabaseConstructor.initialize()  ← KSP generated            │
     │                                                          │
     ▼                                                          │
Create tables (SQL CREATE)                                      │
     │                                                          │
     ▼                                                          │
Create database instance                                        │
     │                                                          │
     ▼                                                          │
Return AppDatabase  ──────────────────────────────────────────┘
     │
     ▼
database.userDao()  ← Get DAO
     │
     ▼
Return UserDao_Impl  ← KSP generated
     │
     ▼
userDao.insert(user)  ← Use DAO
     │
     ▼
SQL: INSERT INTO users...  ← Execute query
     │
     ▼
Data written to database
```

---

## Diagram 3: Data Flow - Insert Operation

```
┌──────────────┐
│   UI Layer   │
│              │
│  Button      │
│  "Add User"  │
└──────┬───────┘
       │ onClick
       ▼
┌──────────────────────────────────┐
│      ViewModel Layer             │
│                                  │
│  fun addUser(name, email) {      │
│      viewModelScope.launch {     │
│          repository.addUser(...) │
│      }                            │
│  }                                │
└──────────────┬───────────────────┘
               │
               ▼
┌──────────────────────────────────┐
│      Repository Layer            │
│                                  │
│  suspend fun addUser(...) {      │
│      val entity = UserEntity(...)│
│      userDao.insert(entity)      │
│  }                                │
└──────────────┬───────────────────┘
               │
               ▼
┌──────────────────────────────────┐
│      DAO Interface               │
│  (Written by Consumer)           │
│                                  │
│  @Dao                            │
│  interface UserDao {             │
│      @Insert                     │
│      suspend fun insert(user)    │
│  }                                │
└──────────────┬───────────────────┘
               │
               ▼
┌──────────────────────────────────┐
│    DAO Implementation            │
│    (Generated by KSP)            │
│                                  │
│  class UserDao_Impl : UserDao {  │
│      override suspend fun insert │
│      (user: UserEntity) {        │
│          database.execute {      │
│              val stmt =          │
│              "INSERT INTO..."    │
│              stmt.bind(user...)  │
│              stmt.step()         │
│          }                        │
│      }                            │
│  }                                │
└──────────────┬───────────────────┘
               │
               ▼
┌──────────────────────────────────┐
│      Room Database               │
│                                  │
│  - Query Dispatcher              │
│  - Connection Pool               │
│  - Transaction Manager           │
└──────────────┬───────────────────┘
               │
               ▼
┌──────────────────────────────────┐
│      SQLite Engine               │
│                                  │
│  Prepare: "INSERT INTO users..." │
│  Bind: name="Alice"              │
│  Bind: email="alice@..."         │
│  Execute                         │
└──────────────┬───────────────────┘
               │
               ▼
┌──────────────────────────────────┐
│      Database File               │
│                                  │
│  /data/.../databases/app.db      │
│                                  │
│  Table: users                    │
│  | id | name  | email |          │
│  |----|-------|-------|          │
│  | 1  | John  | ...   |          │
│  | 2  | Alice | ...   | ← NEW!   │
└──────────────┬───────────────────┘
               │
               │ Notifies observers
               ▼
┌──────────────────────────────────┐
│      Flow Observer               │
│                                  │
│  userDao.getAll().collect {     │
│      users -> // Updated list    │
│  }                                │
└──────────────┬───────────────────┘
               │
               ▼
┌──────────────────────────────────┐
│      UI Updates                  │
│                                  │
│  LazyColumn {                    │
│      items(users) { user ->      │
│          Text(user.name)         │
│      }                            │
│  }                                │
└──────────────────────────────────┘
```

---

## Diagram 4: Platform-Specific Initialization

### Android Path

```
MainActivity.onCreate()
     │
     ├── applicationContext (passed)
     │
     ▼
DatabaseFactory(context)  ← Android actual class
     │
     │ Extends
     ▼
AndroidDatabaseFactory<AppDatabase>(context)  ← Library class
     │
     │ Has method
     ▼
buildDatabase(
    klass = AppDatabase::class.java,
    config = DatabaseConfig(...)
)
     │
     │ Uses
     ▼
Room.databaseBuilder(
    context,           ← Android Context
    klass,            ← Database class
    config.name       ← "app.db"
)
     │
     │ Resolves to
     ▼
Path: /data/data/com.myapp/databases/app.db
     │
     ▼
SQLite database file created at this path
```

### iOS Path

```
MainViewController()
     │
     │ No context needed
     ▼
DatabaseFactory()  ← iOS actual class
     │
     │ Extends
     ▼
IosDatabaseFactory<AppDatabase>()  ← Library class
     │
     │ Has method
     ▼
buildDatabase(
    config = DatabaseConfig(...)
)
     │
     │ Calculates path
     ▼
NSHomeDirectory() + "/app.db"
     │
     │ Resolves to
     ▼
Path: /Users/.../Library/.../app.db
     │
     │ Uses
     ▼
Room.databaseBuilder(
    name = fullPath,
    factory = { getRoomDatabase() }
)
     │
     │ Sets driver
     ▼
.setDriver(BundledSQLiteDriver())
     │
     ▼
SQLite database file created at this path
```

---

## Diagram 5: Query Flow with Reactive Updates

```
Initial State:
┌──────────────────────────┐
│  Database: users table   │
│  [John]                  │
└──────────────────────────┘
           │
           │ Observing
           ▼
┌──────────────────────────┐
│  Flow<List<UserEntity>>  │
│                          │
│  userDao.getAll()        │
│      .collect { users -> │
│          updateUI(users) │
│      }                    │
└──────────────────────────┘
           │
           │ Initial emission
           ▼
┌──────────────────────────┐
│  UI Shows:               │
│  - John                  │
└──────────────────────────┘


User Action: Insert Alice
┌──────────────────────────┐
│  userDao.insert(Alice)   │
└──────────┬───────────────┘
           │
           ▼
┌──────────────────────────┐
│  SQL: INSERT INTO users  │
│  VALUES ('Alice', ...)   │
└──────────┬───────────────┘
           │
           ▼
┌──────────────────────────┐
│  Database: users table   │
│  [John, Alice]           │
└──────────┬───────────────┘
           │
           │ Table changed!
           │ Trigger re-query
           ▼
┌──────────────────────────┐
│  SQL: SELECT * FROM...   │
└──────────┬───────────────┘
           │
           │ Returns new data
           ▼
┌──────────────────────────┐
│  Flow emits new list     │
│  [John, Alice]           │
└──────────┬───────────────┘
           │
           │ collect receives
           ▼
┌──────────────────────────┐
│  UI Updates:             │
│  - John                  │
│  - Alice  ← NEW!         │
└──────────────────────────┘
```

---

## Diagram 6: Who Provides What

```
┌─────────────────────────────────────────────────────────────┐
│                      YOU PROVIDE                            │
│                   (Consumer App)                            │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  ✅ Entities                                                │
│     @Entity data class UserEntity                          │
│                                                             │
│  ✅ DAOs (interfaces)                                       │
│     @Dao interface UserDao                                 │
│                                                             │
│  ✅ Database (abstract)                                     │
│     @Database abstract class AppDatabase                   │
│                                                             │
│  ✅ DatabaseFactory implementation                         │
│     actual class DatabaseFactory extends ...               │
│                                                             │
│  ✅ Business logic (Repository, ViewModel, UI)             │
│                                                             │
└─────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────┐
│                    KSP GENERATES                            │
│              (Kotlin Symbol Processor)                      │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  ✅ DAO implementations                                     │
│     class UserDao_Impl : UserDao                           │
│                                                             │
│  ✅ Database constructor                                    │
│     actual object AppDatabaseConstructor                   │
│                                                             │
│  ✅ SQL generation and validation                          │
│     Converts @Query to actual SQL                          │
│                                                             │
│  ✅ Type mapping                                            │
│     Maps SQL results to Kotlin objects                     │
│                                                             │
└─────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────┐
│                  LIBRARY PROVIDES                           │
│                 (kmp-room-core)                             │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  ✅ AndroidDatabaseFactory<T>                              │
│     Abstract base class for Android                        │
│                                                             │
│  ✅ IosDatabaseFactory<T>                                  │
│     Abstract base class for iOS                            │
│                                                             │
│  ✅ DatabaseConfig                                          │
│     Configuration data class                               │
│                                                             │
│  ✅ Platform-specific setup                                │
│     Context handling, path resolution                      │
│                                                             │
│  ✅ Room dependencies                                       │
│     Brings in Room runtime, SQLite                         │
│                                                             │
└─────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────┐
│                   ROOM PROVIDES                             │
│              (androidx.room:room-runtime)                   │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  ✅ Room.databaseBuilder()                                 │
│     Creates database instances                             │
│                                                             │
│  ✅ RoomDatabase base class                                │
│     Core database functionality                            │
│                                                             │
│  ✅ Query dispatcher                                        │
│     Manages threading                                      │
│                                                             │
│  ✅ Connection pool                                         │
│     Manages SQLite connections                             │
│                                                             │
│  ✅ Migration framework                                     │
│     Handles schema changes                                 │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

---

## Diagram 7: Configuration Flow

```
Consumer Creates Config:
┌────────────────────────────────┐
│  DatabaseConfig(               │
│      name = "app.db",          │
│      version = 1,              │
│      enableLogging = true,     │
│      migrations = [...]        │
│  )                              │
└────────────┬───────────────────┘
             │
             │ Passed to library
             ▼
┌────────────────────────────────┐
│  AndroidDatabaseFactory        │
│      .buildDatabase(           │
│          klass,                │
│          config  ← HERE        │
│      )                          │
└────────────┬───────────────────┘
             │
             │ Extracts values
             ▼
┌────────────────────────────────┐
│  Room.databaseBuilder(         │
│      context,                  │
│      klass,                    │
│      config.name  ← "app.db"   │
│  )                              │
│  .addMigrations(               │
│      config.migrations ← [...]│
│  )                              │
│  .setQueryCallback(            │
│      if config.enableLogging   │
│  )                              │
│  .build()                      │
└────────────┬───────────────────┘
             │
             │ Result
             ▼
┌────────────────────────────────┐
│  Configured Database           │
│  - File: app.db                │
│  - Version: 1                  │
│  - Logging: ON                 │
│  - Migrations: Applied         │
└────────────────────────────────┘
```

---

## Diagram 8: Thread Safety & Coroutines

```
Main Thread (UI)
     │
     │ User clicks button
     ▼
viewModelScope.launch {  ← Starts coroutine
     │
     │ Switched to IO thread
     ▼
     │
repository.insertUser()
     │
     ▼
userDao.insert()  ← suspend function
     │
     │ Room's query dispatcher
     ▼
database.getQueryDispatcher().execute {
     │
     │ Background thread
     ▼
     │
     SQL: INSERT INTO...
     │
     ▼
     │
} ← Execution complete
     │
     │ Switched back to Main thread
     ▼
     │
UI updates (if needed)


Flow Collection (Reactive):
┌───────────────────────────┐
│  Main Thread (UI)         │
│                           │
│  userDao.getAll()         │
│      .collect { users ->  │
│          updateUI(users)  │
│      }                     │
└───────────┬───────────────┘
            │
            │ Observes database
            │
            │ When data changes:
            │
            ▼
┌───────────────────────────┐
│  Background Thread        │
│                           │
│  SQL: SELECT * FROM...    │
│                           │
│  Map results to objects   │
└───────────┬───────────────┘
            │
            │ Result ready
            ▼
┌───────────────────────────┐
│  Main Thread (UI)         │
│                           │
│  collect { users ->       │
│      updateUI(users) ← !! │
│  }                         │
└───────────────────────────┘
```

---

## Summary: The Complete Picture

```
┌─────────────────────────────────────────────────────────────┐
│                         LAYERS                              │
└─────────────────────────────────────────────────────────────┘

    ┌─────────────────────────────────────────┐
    │           UI LAYER                      │
    │  Composables, ViewModels               │
    └────────────────┬────────────────────────┘
                     │
                     │ Uses
                     ▼
    ┌─────────────────────────────────────────┐
    │        REPOSITORY LAYER                 │
    │  Business logic, data operations       │
    └────────────────┬────────────────────────┘
                     │
                     │ Uses
                     ▼
    ┌─────────────────────────────────────────┐
    │           DAO LAYER                     │
    │  Interface: Written by you             │
    │  Implementation: Generated by KSP      │
    └────────────────┬────────────────────────┘
                     │
                     │ Uses
                     ▼
    ┌─────────────────────────────────────────┐
    │        DATABASE LAYER                   │
    │  Abstract: Written by you              │
    │  Constructor: Generated by KSP         │
    │  Factory: Written by you + Library     │
    └────────────────┬────────────────────────┘
                     │
                     │ Uses
                     ▼
    ┌─────────────────────────────────────────┐
    │          ROOM LAYER                     │
    │  Room runtime, query execution         │
    └────────────────┬────────────────────────┘
                     │
                     │ Uses
                     ▼
    ┌─────────────────────────────────────────┐
    │         SQLITE LAYER                    │
    │  Database file, SQL operations         │
    └─────────────────────────────────────────┘
```

---

**End of Visual Diagrams**

These diagrams show the complete flow from consumer app through the library to the database.