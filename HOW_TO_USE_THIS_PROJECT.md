# How to Use This Project for ANY KMP App

**Simple, practical guide to get you started in 5 minutes**

---

## 🎯 What Is This?

This project gives you **Room Database for Kotlin Multiplatform** in 3 ways:

1. **Ready-to-use library** - Just add dependency ⚡ (30 min)
2. **Customizable library** - Fork and modify 🛠 (2-4 hours)
3. **Complete prompts** - Build from scratch 📚 (4-8 hours)

---

## ⚡ Fastest Start (30 Minutes)

### What You Need
- GitHub account
- Existing KMP project
- 30 minutes

### 3 Steps to Success

#### Step 1: Get GitHub Token (5 min)
```
1. Go to: https://github.com/settings/tokens/new
2. Select scope: read:packages
3. Generate token
4. Copy it
```

#### Step 2: Add to Your Project (10 min)

**File: `~/.gradle/gradle.properties`**
```properties
gpr.user=YOUR_GITHUB_USERNAME
gpr.token=YOUR_GITHUB_TOKEN
```

**File: `settings.gradle.kts`**
```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://maven.pkg.github.com/neeraj-brightly12/KMPDatabasePOC")
            credentials {
                username = findProperty("gpr.user") as String?
                password = findProperty("gpr.token") as String?
            }
        }
    }
}
```

**File: `app/build.gradle.kts`**
```kotlin
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.ksp) // ⚠️ REQUIRED
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation("com.brightly:kmp-room-core:1.0.2")
        }
    }
}

dependencies {
    // ⚠️ REQUIRED: KSP for YOUR entities
    add("kspAndroid", "androidx.room:room-compiler:2.7.0")
    add("kspIosArm64", "androidx.room:room-compiler:2.7.0")
    add("kspIosSimulatorArm64", "androidx.room:room-compiler:2.7.0")
}
```

#### Step 3: Create Your Database (15 min)

**Entity (commonMain):**
```kotlin
@Entity(tableName = "items")
data class Item(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String
)
```

**DAO (commonMain):**
```kotlin
@Dao
interface ItemDao {
    @Insert suspend fun insert(item: Item)
    @Query("SELECT * FROM items") fun getAll(): Flow<List<Item>>
}
```

**Database (commonMain):**
```kotlin
@Database(entities = [Item::class], version = 1)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase>
```

**Factory Interface (commonMain):**
```kotlin
expect class DatabaseFactory {
    fun createDatabase(): AppDatabase
}
```

**Android Implementation (androidMain):**
```kotlin
import com.brightly.kmp.room.core.android.AndroidDatabaseFactory
import com.brightly.kmp.room.core.DatabaseConfig

actual class DatabaseFactory(
    context: Context
) : AndroidDatabaseFactory<AppDatabase>(context) {
    actual fun createDatabase(): AppDatabase {
        return buildDatabase(
            AppDatabase::class.java,
            DatabaseConfig(name = "app.db", version = 1)
        )
    }
    override fun createDatabase(name: String, migrations: List<Migration>) =
        buildDatabase(AppDatabase::class.java, DatabaseConfig(name, 1))
}
```

**iOS Implementation (iosMain):**
```kotlin
import com.brightly.kmp.room.core.ios.IosDatabaseFactory
import com.brightly.kmp.room.core.DatabaseConfig

actual class DatabaseFactory : IosDatabaseFactory<AppDatabase>() {
    actual fun createDatabase(): AppDatabase {
        return buildDatabase(DatabaseConfig(name = "app.db", version = 1))
    }
    override fun createDatabase(name: String, migrations: List<Migration>) =
        buildDatabase(DatabaseConfig(name, 1))
}
```

### Build and Run
```bash
# Android
./gradlew :app:assembleDebug

# iOS
./gradlew :app:compileKotlinIosSimulatorArm64
```

### ✅ Done! You now have Room Database working on Android & iOS

---

## 📚 Full Documentation

### Start Here (Choose ONE)

| Your Goal | Read This | Time |
|-----------|-----------|------|
| Use library in my app | **[APPLY_TO_ANY_APP_GUIDE.md](./APPLY_TO_ANY_APP_GUIDE.md)** → Path A | 30 min |
| Create custom library | **[APPLY_TO_ANY_APP_GUIDE.md](./APPLY_TO_ANY_APP_GUIDE.md)** → Path B | 2-4 hrs |
| Build from scratch | **[PROMPT_LIBRARY.md](./PROMPT_LIBRARY.md)** | 4-8 hrs |
| Understand everything | **[MASTER_GUIDE_INDEX.md](./MASTER_GUIDE_INDEX.md)** | 2 hrs |
| Visual guide | **[COMPLETE_WORKFLOW_VISUAL.md](./COMPLETE_WORKFLOW_VISUAL.md)** | 1 hr |
| Quick commands | **[QUICK_REFERENCE.md](./QUICK_REFERENCE.md)** | 5 min |

### All Documents

#### Core Guides (Must Read)
- **APPLY_TO_ANY_APP_GUIDE.md** - How to apply to ANY app (3 paths)
- **PROMPT_LIBRARY.md** - All 40 prompts used to create this
- **MASTER_GUIDE_INDEX.md** - Complete navigation and index
- **COMPLETE_WORKFLOW_VISUAL.md** - Visual step-by-step workflows

#### Quick Reference
- **QUICK_REFERENCE.md** - Commands and configs
- **README_START_HERE.md** - Original documentation index
- **HOW_TO_USE_THIS_PROJECT.md** - This file

#### Architecture & Understanding
- **ARCHITECTURE_DOCUMENT.md** - Architecture details
- **CODE_FLOW_EXPLANATION.md** - Code execution flow
- **COMPLETE_GUIDE.md** - Comprehensive guide

#### Library Specific
- **kmp-room-core/README.md** - Library documentation
- **kmp-room-core/USAGE_EXAMPLE.md** - Code examples
- **kmp-room-core/PUBLISHING.md** - Publishing guide

---

## 🎓 What You Get

### The Library: `kmp-room-core`
✅ Platform abstractions (Android & iOS)
✅ Database factories (ready to use)
✅ Migration utilities
✅ Flow extensions
✅ Database utilities
✅ Published to GitHub Packages

### The Prompts: `PROMPT_LIBRARY.md`
✅ 40 organized prompts for Claude Code
✅ Setup, library creation, publishing, integration
✅ Testing, documentation, troubleshooting
✅ Advanced features

### The Documentation
✅ Multiple learning paths
✅ Step-by-step guides
✅ Visual workflows
✅ Complete examples
✅ Troubleshooting solutions

### The Example App
✅ Working implementation
✅ Android & iOS support
✅ All patterns demonstrated
✅ Compose Multiplatform UI

---

## 🤔 Which Path Should I Choose?

### Path A: Use Published Library (RECOMMENDED)
**Best for:**
- Most projects (80%)
- Quick setup needed
- Standard requirements
- Want easy updates

**Time:** 30 minutes
**Skill:** Beginner-friendly
**Result:** Working Room DB

### Path B: Create Custom Library
**Best for:**
- Multiple internal apps
- Custom requirements
- Company needs private library
- Want to extend features

**Time:** 2-4 hours
**Skill:** Intermediate
**Result:** Custom published library

### Path C: Direct Integration
**Best for:**
- Single app
- Maximum control
- Experienced team
- Prototype/POC

**Time:** 1-2 hours
**Skill:** Intermediate/Advanced
**Result:** Direct implementation

**MOST PEOPLE SHOULD USE PATH A** ⭐

---

## ⚠️ Critical Information

### KSP is ALWAYS Required

**Question:** "Do I need KSP if I use the published library?"

**Answer:** **YES, ALWAYS!**

**Why:**
- Library provides infrastructure
- YOUR app defines entities, DAOs, database
- Room needs KSP to generate code for YOUR entities
- Without KSP = Build fails ❌

**Configuration:**
```kotlin
plugins {
    alias(libs.plugins.ksp) // Required!
}

dependencies {
    add("kspAndroid", "androidx.room:room-compiler:2.7.0")
    add("kspIosArm64", "androidx.room:room-compiler:2.7.0")
    add("kspIosSimulatorArm64", "androidx.room:room-compiler:2.7.0")
}
```

### Build Commands

**Don't use:**
```bash
./gradlew build # ❌ May fail with metadata errors
```

**Use instead:**
```bash
./gradlew :app:assembleDebug # ✅ Android
./gradlew :app:compileKotlinIosSimulatorArm64 # ✅ iOS
```

### Common Issues

**Issue:** "Could not find library"
**Fix:** Check credentials in `~/.gradle/gradle.properties`

**Issue:** "KSP not generating code"
**Fix:** Ensure KSP plugin and dependencies are added

**Issue:** "Metadata build fails"
**Fix:** This is normal! Use platform-specific builds

---

## 📖 Complete Documentation Map

```
START HERE
    ↓
┌─────────────────────────────────────────┐
│   HOW_TO_USE_THIS_PROJECT.md          │ ← You are here
│   (5-minute overview)                  │
└─────────────────┬───────────────────────┘
                  │
    ┌─────────────┼─────────────┐
    │             │             │
    ▼             ▼             ▼
┌─────────┐ ┌──────────┐ ┌──────────────┐
│Path A   │ │Path B    │ │From Scratch  │
│30 min   │ │2-4 hrs   │ │4-8 hrs       │
└─────────┘ └──────────┘ └──────────────┘
    │             │             │
    └─────────────┼─────────────┘
                  │
                  ▼
    ┌──────────────────────────────────┐
    │ APPLY_TO_ANY_APP_GUIDE.md       │
    │ (Complete guide for all paths)   │
    └──────────────┬───────────────────┘
                   │
    ┌──────────────┼──────────────┐
    │              │              │
    ▼              ▼              ▼
┌─────────┐ ┌──────────┐ ┌──────────────┐
│PROMPT   │ │MASTER    │ │WORKFLOW      │
│LIBRARY  │ │GUIDE     │ │VISUAL        │
│40 prompts│ │INDEX     │ │Diagrams      │
└─────────┘ └──────────┘ └──────────────┘
```

---

## 🚀 Next Steps

### For First-Time Users

1. **Read:** This document (you just did! ✅)
2. **Choose:** Your path (A, B, or C)
3. **Follow:** APPLY_TO_ANY_APP_GUIDE.md
4. **Build:** Your app
5. **Bookmark:** QUICK_REFERENCE.md for daily use

### For Team Leads

1. **Read:** MASTER_GUIDE_INDEX.md
2. **Review:** ARCHITECTURE_DOCUMENT.md
3. **Decide:** Which path for your team
4. **Share:** APPLY_TO_ANY_APP_GUIDE.md with team
5. **Set up:** Team workflow

### For Library Maintainers

1. **Study:** PROMPT_LIBRARY.md
2. **Review:** All source code
3. **Read:** kmp-room-core/PUBLISHING.md
4. **Practice:** Publishing workflow
5. **Document:** Any customizations

---

## ✅ Success Checklist

**Before You Start:**
- [ ] Have GitHub account
- [ ] Have KMP project
- [ ] Know basic Kotlin
- [ ] Read this document

**During Setup (Path A):**
- [ ] Created GitHub token
- [ ] Saved credentials
- [ ] Added repository
- [ ] Added dependencies with KSP
- [ ] Created entities and DAOs
- [ ] Implemented factories
- [ ] Built successfully

**After Setup:**
- [ ] Android builds ✅
- [ ] iOS builds ✅
- [ ] Database operations work ✅
- [ ] Data persists ✅
- [ ] Team onboarded ✅

---

## 🆘 Need Help?

### Quick Fixes

**Problem:** Build error
**Solution:** See QUICK_REFERENCE.md → Troubleshooting

**Problem:** Don't understand architecture
**Solution:** Read ARCHITECTURE_DOCUMENT.md

**Problem:** Need examples
**Solution:** See kmp-room-core/USAGE_EXAMPLE.md

**Problem:** Publishing issues
**Solution:** See kmp-room-core/PUBLISHING.md

### Documentation

- **MASTER_GUIDE_INDEX.md** - Find any document
- **QUICK_REFERENCE.md** - Fast command lookup
- **COMPLETE_GUIDE.md** - Everything explained

### External Resources

- [Room Docs](https://developer.android.com/training/data-storage/room)
- [KMP Docs](https://kotlinlang.org/docs/multiplatform.html)
- [KSP Docs](https://kotlinlang.org/docs/ksp-overview.html)

---

## 📊 Project Stats

- **Library Version:** 1.0.2
- **Platforms:** Android, iOS
- **Status:** Production Ready ✅
- **Documentation:** 30+ files
- **Prompts:** 40 organized prompts
- **Example App:** Fully working
- **Build Time:** ~30 min setup
- **Lines of Code:** 2000+ (library)

---

## 🎯 Summary

**This project gives you everything to implement Room Database in KMP:**

1. ✅ **Ready-to-use library** published on GitHub Packages
2. ✅ **Complete prompts** to recreate or customize
3. ✅ **Comprehensive docs** for every use case
4. ✅ **Working example** app on Android & iOS
5. ✅ **Multiple paths** choose what fits your needs

**Choose your path and start building!** 🚀

---

## 🔗 Quick Links

| Link | Purpose |
|------|---------|
| [APPLY_TO_ANY_APP_GUIDE.md](./APPLY_TO_ANY_APP_GUIDE.md) | Main guide for any app |
| [PROMPT_LIBRARY.md](./PROMPT_LIBRARY.md) | 40 prompts to recreate |
| [MASTER_GUIDE_INDEX.md](./MASTER_GUIDE_INDEX.md) | Complete navigation |
| [QUICK_REFERENCE.md](./QUICK_REFERENCE.md) | Daily command reference |
| [kmp-room-core/README.md](./kmp-room-core/README.md) | Library documentation |
| [GitHub Packages](https://github.com/neeraj-brightly12/KMPDatabasePOC/packages) | Published library |

---

**Ready to add Room Database to your KMP app?**

**Choose your path:**
- ⚡ **Fast:** APPLY_TO_ANY_APP_GUIDE.md → Path A
- 🛠 **Custom:** APPLY_TO_ANY_APP_GUIDE.md → Path B
- 📚 **Learn:** PROMPT_LIBRARY.md → All 40 prompts

**Start now →** [APPLY_TO_ANY_APP_GUIDE.md](./APPLY_TO_ANY_APP_GUIDE.md)

---

**Document Version:** 1.0.0
**Last Updated:** 2026-03-20
**Author:** Brightly Development Team

**Happy coding! 🚀**