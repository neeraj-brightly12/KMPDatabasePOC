# KMP Room Core - Database POC

[![Kotlin](https://img.shields.io/badge/Kotlin-2.1.21-blue.svg)](https://kotlinlang.org)
[![Room](https://img.shields.io/badge/Room-2.7.0-green.svg)](https://developer.android.com/training/data-storage/room)
[![License](https://img.shields.io/badge/License-Apache%202.0-orange.svg)](https://www.apache.org/licenses/LICENSE-2.0)

A **Kotlin Multiplatform** library providing a generalized, reusable foundation for implementing **Room Database** in KMP projects targeting **Android** and **iOS**.

---

## 🚀 Quick Start

### Add Dependency

```kotlin
// Root build.gradle.kts
repositories {
    maven {
        url = uri("https://maven.pkg.github.com/neeraj-brightly12/KMPDatabasePOC")
        credentials {
            username = project.findProperty("gpr.user") as String?
            password = project.findProperty("gpr.token") as String?
        }
    }
}

// composeApp/build.gradle.kts
commonMain.dependencies {
    implementation("com.brightly:kmp-room-core:1.0.2")
}
```

### Basic Usage

```kotlin
// 1. Define Entity
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String
)

// 2. Create DAO
@Dao
interface UserDao : BaseDao<UserEntity> {
    @Query("SELECT * FROM users")
    override fun getAll(): Flow<List<UserEntity>>
}

// 3. Create Repository
class UserRepository(database: AppDatabase)
    : BaseRepository<UserEntity, UserDao>(database.userDao())

// 4. Use in ViewModel
class UserViewModel(private val repository: UserRepository) : ViewModel() {
    val users = repository.getAll().stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        emptyList()
    )

    fun addUser(name: String) {
        viewModelScope.launch {
            repository.add(UserEntity(name = name))
        }
    }
}
```

---

## 📚 Documentation

### 📖 Complete Guide
**[COMPLETE_LIBRARY_DOCUMENTATION.md](./COMPLETE_LIBRARY_DOCUMENTATION.md)** - Comprehensive documentation including:
- ✅ Architecture & Design Patterns
- ✅ Complete Code Flow
- ✅ How to Create the Library
- ✅ Publishing Guide
- ✅ Usage Examples
- ✅ Visual Workflows & Diagrams
- ✅ Troubleshooting
- ✅ Best Practices

### 🎯 Quick References
- **[APP_USING_PUBLISHED_LIBRARY.md](./APP_USING_PUBLISHED_LIBRARY.md)** - Using the published library
- **[BUILD_AND_RUN.md](./BUILD_AND_RUN.md)** - Build and run instructions
- **[HOW_TO_USE_THIS_PROJECT.md](./HOW_TO_USE_THIS_PROJECT.md)** - Project overview

### 📁 Library Documentation
- **[kmp-room-core/README.md](./kmp-room-core/README.md)** - Library-specific documentation
- **[kmp-room-core/PUBLISHING.md](./kmp-room-core/PUBLISHING.md)** - Publishing guide
- **[kmp-room-core/EXAMPLE_WORKFLOW.md](./kmp-room-core/EXAMPLE_WORKFLOW.md)** - Example workflows

---

## 🏗️ Architecture

```
┌─────────────────────────────────────────┐
│         Your KMP Application             │
│  ┌────────────┐    ┌──────────────┐    │
│  │ ViewModels │◄───│ Repositories │    │
│  └────────────┘    └──────────────┘    │
│                           │              │
│                           ▼              │
│         ┌───────────────────────────┐   │
│         │   kmp-room-core Library   │   │
│         │  - BaseDatabaseFactory    │   │
│         │  - BaseRepository         │   │
│         │  - BaseDao                │   │
│         └───────────────────────────┘   │
│                     │                    │
│         ┌───────────┴───────────┐       │
│         ▼                       ▼        │
│  ┌────────────┐         ┌────────────┐ │
│  │  Android   │         │    iOS     │ │
│  │  Context   │         │ NSHomeDir  │ │
│  └────────────┘         └────────────┘ │
└─────────────────────────────────────────┘
```

---

## ✨ Features

### 🎯 Core Features
- ✅ **Platform-Agnostic Database Factory**
  - Abstract database creation across Android and iOS
  - Consistent API for both platforms

- ✅ **Generic Base Repository**
  - Eliminates CRUD boilerplate
  - Type-safe implementation
  - Easy to extend

- ✅ **Multiplatform Support**
  - Android (API 24+)
  - iOS (arm64, simulator)

- ✅ **Production Ready**
  - Published to GitHub Packages
  - Versioned releases
  - Comprehensive testing

### 📦 What's Included
- `BaseDatabaseFactory<T>` - Abstract factory for database creation
- `BaseRepository<E, D>` - Generic repository with CRUD operations
- `BaseDao<T>` - Base DAO interface
- Platform-specific implementations (Android & iOS)

---

## 🛠️ Tech Stack

| Component | Version |
|-----------|---------|
| Kotlin | 2.1.21 |
| Compose Multiplatform | 1.9.1 |
| Room | 2.7.0 |
| Gradle | 8.14.3 |
| Coroutines | 1.10.2 |

---

## 📱 Sample App

This repository includes a complete sample app demonstrating:
- ✅ User management (CRUD operations)
- ✅ Product management
- ✅ Navigation between screens
- ✅ StateFlow integration
- ✅ Compose UI implementation

### Run Sample App

**Android:**
```bash
./gradlew :composeApp:installDebug
```

**iOS:**
1. Open `iosApp/iosApp.xcodeproj` in Xcode
2. Select a simulator
3. Click Run

---

## 🎨 UI Components

The project includes reusable UI components:
- Form builder (`FormAgent`)
- Smart list rendering (`ListAgent`)
- Dialog management (`DialogAgent`)
- Card builders (`CardAgent`)
- Screen templates (`ScreenAgent`)

See [UI Components Documentation](.github/SETUP_COMPLETE.md) for details.

---

## 🤖 AI Agents

Included AI agent prompts for code analysis:
- **code-reviewer** - Comprehensive code reviews
- **bug-hunter** - Find bugs and edge cases
- **test-generator** - Generate test suites
- **performance-analyzer** - Performance optimization
- **security-auditor** - Security audits
- **documentation-writer** - Add documentation
- **code-optimizer** - Code optimization

See [Agent Usage Guide](.github/AGENT_USAGE_GUIDE.md) for usage.

---

## 🔧 GitHub Actions

Automated workflows included:
- ✅ Build check on push/PR
- ✅ Code quality analysis
- ✅ Automated PR reviews
- ✅ Auto-labeling
- ✅ Release automation
- ✅ Dependency updates

See [GitHub Actions Documentation](.github/README.md).

---

## 📝 License

```
Copyright 2026 Brightly Software

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

---

## 👥 Authors

**Neeraj Soni**
- Email: neeraj.soni@brightlysoftware.com
- GitHub: [@neeraj-brightly12](https://github.com/neeraj-brightly12)

---

## 🤝 Contributing

Contributions are welcome! Please:
1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Submit a pull request

See [PULL_REQUEST_TEMPLATE.md](.github/PULL_REQUEST_TEMPLATE.md) for PR guidelines.

---

## 📊 Project Stats

- **Lines of Code**: ~10,000+
- **Modules**: 2 (library + app)
- **Platforms**: Android, iOS
- **Test Coverage**: Comprehensive
- **Documentation**: Complete

---

## 🎯 Roadmap

- [ ] Add desktop (JVM) support
- [ ] Implement database migrations helper
- [ ] Add pagination support
- [ ] Create more sample apps
- [ ] Performance benchmarks
- [ ] Video tutorials

---

## 📞 Support

- **Documentation**: [COMPLETE_LIBRARY_DOCUMENTATION.md](./COMPLETE_LIBRARY_DOCUMENTATION.md)
- **Issues**: [GitHub Issues](https://github.com/neeraj-brightly12/KMPDatabasePOC/issues)
- **Discussions**: [GitHub Discussions](https://github.com/neeraj-brightly12/KMPDatabasePOC/discussions)

---

## 🌟 Acknowledgments

Built with:
- [Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform.html)
- [Room Database](https://developer.android.com/training/data-storage/room)
- [Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/)
- [Jetpack Libraries](https://developer.android.com/jetpack)

---

**⭐ Star this repo if you find it useful!**

**🔗 Share with the community!**

---

**Last Updated:** March 24, 2026