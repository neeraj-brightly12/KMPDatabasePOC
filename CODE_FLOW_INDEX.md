# Code Flow Documentation Index

Complete guide to understanding how consumer apps use the kmp-room-core library.

---

## 📚 Available Documents

### 1. CODE_FLOW_EXPLANATION.md ⭐ START HERE
**Complete text-based explanation**

**Contents:**
- Part 1: The Consumer's Code (What you write)
- Part 2: What KSP Generates (Auto-generated code)
- Part 3: The Library's Code (kmp-room-core internals)
- Part 4: Complete Flow Diagram (Overall picture)
- Part 5: Method-by-Method Explanation (Detailed walkthrough)
- Part 6: Platform-Specific Details (Android vs iOS)
- Part 7: Complete Example Walkthrough (Real scenario)

**Best for:** Understanding WHAT each part does and WHY

---

### 2. VISUAL_CODE_FLOW.md
**Visual ASCII diagrams**

**Contents:**
- Diagram 1: Component Relationships
- Diagram 2: Method Call Sequence
- Diagram 3: Data Flow - Insert Operation
- Diagram 4: Platform-Specific Initialization
- Diagram 5: Query Flow with Reactive Updates
- Diagram 6: Who Provides What
- Diagram 7: Configuration Flow
- Diagram 8: Thread Safety & Coroutines

**Best for:** Seeing HOW components connect visually

---

## 🎯 Quick Navigation

### I want to understand...

#### "What code do I need to write?"
→ **CODE_FLOW_EXPLANATION.md** → Part 1: The Consumer's Code

#### "What does KSP generate?"
→ **CODE_FLOW_EXPLANATION.md** → Part 2: What KSP Generates

#### "How does the library work internally?"
→ **CODE_FLOW_EXPLANATION.md** → Part 3: The Library's Code

#### "What's the complete flow from start to finish?"
→ **CODE_FLOW_EXPLANATION.md** → Part 4: Complete Flow Diagram
→ **VISUAL_CODE_FLOW.md** → Diagram 2: Method Call Sequence

#### "How does an insert operation work?"
→ **CODE_FLOW_EXPLANATION.md** → Part 7: Complete Example Walkthrough
→ **VISUAL_CODE_FLOW.md** → Diagram 3: Data Flow

#### "What's different between Android and iOS?"
→ **CODE_FLOW_EXPLANATION.md** → Part 6: Platform-Specific Details
→ **VISUAL_CODE_FLOW.md** → Diagram 4: Platform Initialization

#### "Who provides what?"
→ **VISUAL_CODE_FLOW.md** → Diagram 6: Who Provides What

---

## 📖 Reading Path

### Beginner (First Time)
```
1. CODE_FLOW_EXPLANATION.md → Overview
2. CODE_FLOW_EXPLANATION.md → Part 1 (Consumer's Code)
3. VISUAL_CODE_FLOW.md → Diagram 1 (Component Relationships)
4. CODE_FLOW_EXPLANATION.md → Part 7 (Example Walkthrough)
```
**Time:** 30 minutes

---

### Intermediate (Understanding Details)
```
1. CODE_FLOW_EXPLANATION.md → All parts (1-7)
2. VISUAL_CODE_FLOW.md → All diagrams
```
**Time:** 1 hour

---

### Advanced (Deep Dive)
```
1. Read all documentation
2. Trace code in IDE alongside docs
3. Experiment with modifications
```
**Time:** 2+ hours

---

## 🔍 Key Concepts Explained

### Consumer's Responsibilities

| What | Where | Example |
|------|-------|---------|
| Entity | commonMain | `@Entity data class UserEntity` |
| DAO | commonMain | `@Dao interface UserDao` |
| Database | commonMain | `@Database abstract class AppDatabase` |
| Factory | androidMain/iosMain | `actual class DatabaseFactory extends...` |

**Details:** CODE_FLOW_EXPLANATION.md → Part 1

---

### KSP's Responsibilities

| What | Why |
|------|-----|
| DAO Implementation | Interface can't execute SQL |
| Database Constructor | RoomDatabase needs initialization |
| SQL Generation | @Query strings → SQLite commands |
| Type Mapping | SQL results → Kotlin objects |

**Details:** CODE_FLOW_EXPLANATION.md → Part 2

---

### Library's Responsibilities

| What | Why |
|------|-----|
| AndroidDatabaseFactory | Android needs Context handling |
| IosDatabaseFactory | iOS needs path resolution + driver |
| DatabaseConfig | Standardized configuration |
| Platform abstraction | Hide platform differences |

**Details:** CODE_FLOW_EXPLANATION.md → Part 3

---

## 🎓 Method Reference

### Consumer Methods

```kotlin
// What you write:
DatabaseFactory(context).createDatabase()

// What it does:
1. Calls your actual implementation
2. Calls your override
3. Calls library's buildDatabase()
4. Returns configured database
```

**Full trace:** CODE_FLOW_EXPLANATION.md → Part 5

---

### Library Methods

```kotlin
// AndroidDatabaseFactory.buildDatabase()
protected fun buildDatabase(
    klass: Class<T>,
    config: DatabaseConfig
): T

// What it does:
1. Creates Room.databaseBuilder
2. Adds migrations from config
3. Enables logging if requested
4. Builds and returns database
```

**Full implementation:** CODE_FLOW_EXPLANATION.md → Part 3

---

### DAO Methods

```kotlin
// What you write:
@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: UserEntity)
}

// What KSP generates:
class UserDao_Impl : UserDao {
    override suspend fun insert(user: UserEntity) {
        // 50+ lines of SQL execution code
    }
}
```

**Full generated code:** CODE_FLOW_EXPLANATION.md → Part 2

---

## 🔄 Complete Flow Summary

```
1. You define: Entity, DAO, Database
2. KSP generates: Implementations
3. You create: DatabaseFactory with library base class
4. Library configures: Room database with platform specifics
5. Room builds: Database instance
6. You use: DAO methods for CRUD
7. KSP implementation: Executes SQL
8. Room manages: Threading, transactions, connections
9. SQLite stores: Data on disk
10. Flow observes: Changes and updates UI
```

**Detailed:** CODE_FLOW_EXPLANATION.md → Part 4
**Visual:** VISUAL_CODE_FLOW.md → Diagram 2

---

## 🧩 Component Interaction

### At Build Time (Compilation)

```
Your Code → KSP Processor → Generated Code
```

**Example:**
```kotlin
// You write:
@Dao interface UserDao { @Query fun getAll() }

// KSP generates:
class UserDao_Impl : UserDao { override fun getAll() { /* SQL */ } }
```

---

### At Runtime (Execution)

```
UI → ViewModel → Repository → DAO → Room → SQLite
```

**Example:**
```kotlin
Button.onClick
  → viewModel.addUser()
  → repository.insert()
  → userDao.insert()
  → UserDao_Impl.insert() (KSP generated)
  → Room executes SQL
  → SQLite writes to disk
```

**Full trace:** CODE_FLOW_EXPLANATION.md → Part 7

---

## 📱 Platform Differences

### Android

```kotlin
// Needs Context
DatabaseFactory(applicationContext)

// Uses Android's database directory
/data/data/com.myapp/databases/app.db

// Built-in SQLite
No additional driver needed
```

---

### iOS

```kotlin
// No Context
DatabaseFactory()

// Uses iOS documents directory
NSHomeDirectory() + "/app.db"

// Bundled SQLite
.setDriver(BundledSQLiteDriver())
```

**Full comparison:** CODE_FLOW_EXPLANATION.md → Part 6

---

## 🎯 Common Questions Answered

### Q: Why do I need KSP?

**A:** Room annotations are just metadata. KSP reads them and generates actual executable code (DAO implementations, database constructors, SQL execution).

**Explained in:** CODE_FLOW_EXPLANATION.md → Part 2

---

### Q: What does buildDatabase() do?

**A:** Creates Room.databaseBuilder, applies your configuration (migrations, logging), and builds the database.

**Explained in:** CODE_FLOW_EXPLANATION.md → Part 3 → AndroidDatabaseFactory

---

### Q: How does insert() actually work?

**A:**
1. You call `userDao.insert(entity)`
2. Calls KSP-generated `UserDao_Impl.insert()`
3. Prepares SQL: `INSERT INTO users...`
4. Binds parameters from entity
5. Executes statement
6. Data written to database

**Full trace:** CODE_FLOW_EXPLANATION.md → Part 7

---

### Q: How does Flow make UI reactive?

**A:** Room creates Flow that observes table changes. When data changes, Flow re-executes query and emits new data. UI collects and updates automatically.

**Explained with diagram:** VISUAL_CODE_FLOW.md → Diagram 5

---

### Q: What's the difference between actual and expect?

**A:**
- `expect`: Declaration in commonMain (signature only)
- `actual`: Implementation in androidMain/iosMain (platform-specific code)

**Example in:** CODE_FLOW_EXPLANATION.md → Part 1 → Step 4

---

## 📊 Quick Reference Tables

### Annotation Reference

| Annotation | Where | Purpose |
|------------|-------|---------|
| `@Entity` | Data class | Defines table |
| `@PrimaryKey` | Property | Primary key |
| `@Dao` | Interface | Data access object |
| `@Insert` | Function | Insert operation |
| `@Query` | Function | Custom query |
| `@Database` | Abstract class | Database definition |
| `@ConstructedBy` | Database | Specifies constructor |

---

### Class Responsibility

| Class | Type | Who Writes | Purpose |
|-------|------|-----------|---------|
| UserEntity | Data | You | Table structure |
| UserDao | Interface | You | Operations |
| UserDao_Impl | Class | KSP | SQL execution |
| AppDatabase | Abstract | You | DB definition |
| AppDatabaseConstructor | Object | KSP | DB initialization |
| DatabaseFactory | Actual | You | Platform factory |
| AndroidDatabaseFactory | Abstract | Library | Android base |
| IosDatabaseFactory | Abstract | Library | iOS base |

---

### Method Flow

| Method | Called By | Calls | Returns |
|--------|-----------|-------|---------|
| createDatabase() | Your code | createDatabase(name, migrations) | AppDatabase |
| createDatabase(name, migrations) | Above | buildDatabase() | AppDatabase |
| buildDatabase() | Above | Room.databaseBuilder() | AppDatabase |
| Room.databaseBuilder() | Above | AppDatabaseConstructor.initialize() | AppDatabase |
| initialize() | Above | Creates instance | AppDatabase |
| userDao() | Your code | Returns impl | UserDao_Impl |
| insert() | Your code | Executes SQL | Unit |

---

## 🚀 Next Steps

After understanding the code flow:

1. **Try it yourself**
   - Create a simple entity
   - Define a DAO
   - Create database
   - Build and run

2. **Experiment**
   - Add more entities
   - Try different query types
   - Use Flow for reactive UI
   - Add migrations

3. **Debug**
   - Enable logging in DatabaseConfig
   - Watch SQL in console
   - Step through with debugger

4. **Optimize**
   - Add indexes
   - Batch operations
   - Use transactions

---

## 📝 Summary

### You Write:
- Entities (data models)
- DAOs (interfaces with @Query)
- Database (abstract class)
- Factory (actual implementation)

### KSP Generates:
- DAO implementations
- Database constructor
- SQL execution code
- Type mappings

### Library Provides:
- Base factory classes
- Platform abstractions
- Configuration helpers
- Room dependencies

### Room Handles:
- Database building
- Query execution
- Threading
- Transactions
- Connection pooling

### Result:
- Type-safe database
- Reactive queries (Flow)
- Cross-platform (Android + iOS)
- Compile-time validation

---

**For complete details, see:**
- **CODE_FLOW_EXPLANATION.md** - Text explanations
- **VISUAL_CODE_FLOW.md** - Visual diagrams

---

Last updated: March 2026