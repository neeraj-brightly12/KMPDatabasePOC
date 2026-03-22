# KMP Database POC

A Kotlin Multiplatform project demonstrating Room Database implementation across Android and iOS platforms using the `kmp-room-core` library.

## Project Overview

This project showcases:
- ✅ **Room Database** in Kotlin Multiplatform
- ✅ **Published Library Usage** - Uses `kmp-room-core` from GitHub Packages
- ✅ **Clean Architecture** with MVVM pattern
- ✅ **Platform Abstractions** for Android and iOS
- ✅ **Type-safe database operations** with Flow support

## 🚀 Quick Start - Choose Your Path

**New to this project?** Start here based on your goal:

### 🎯 I want to use this in my app (30 minutes)
**Read:** [HOW_TO_USE_THIS_PROJECT.md](./HOW_TO_USE_THIS_PROJECT.md) - 5-minute overview, then follow Path A

### 🛠 I want to recreate this with Claude Code
**Read:** [PROMPT_LIBRARY.md](./PROMPT_LIBRARY.md) - 40 organized prompts to recreate everything

### 📚 I want complete documentation
**Read:** [MASTER_GUIDE_INDEX.md](./MASTER_GUIDE_INDEX.md) - Navigation hub for all docs

### 📊 I want visual workflows
**Read:** [COMPLETE_WORKFLOW_VISUAL.md](./COMPLETE_WORKFLOW_VISUAL.md) - Step-by-step diagrams

### 📋 I want a quick summary
**Read:** [PROJECT_SUMMARY.md](./PROJECT_SUMMARY.md) - One-page project overview

### ⚡ I want to start NOW
**Read:** [APPLY_TO_ANY_APP_GUIDE.md](./APPLY_TO_ANY_APP_GUIDE.md) - Complete guide with 3 paths

## Project Structure

* **[/kmp-room-core](./kmp-room-core)** - Published library providing Room Database abstractions
  - Platform-specific factory implementations
  - Database utilities and migration helpers
  - Flow extensions for common operations

* **[/composeApp](./composeApp/src)** - Main KMP application using the library
  - [commonMain](./composeApp/src/commonMain/kotlin) - Shared code (entities, DAOs, repositories, ViewModels)
  - [androidMain](./composeApp/src/androidMain/kotlin) - Android-specific implementations
  - [iosMain](./composeApp/src/iosMain/kotlin) - iOS-specific implementations

* **[/iosApp](./iosApp/iosApp)** - iOS application entry point

## Key Dependencies

```kotlin
// Using published library
implementation("com.brightly:kmp-room-core:1.0.2")

// ⚠️ KSP Required for Room annotation processing
add("kspAndroid", "androidx.room:room-compiler:2.7.0")
add("kspIosArm64", "androidx.room:room-compiler:2.7.0")
add("kspIosSimulatorArm64", "androidx.room:room-compiler:2.7.0")
```

**Important:** Even when using the published library, KSP is required because the app defines its own Room entities and DAOs.

## 📖 Documentation

### 🌟 Essential Guides (Start Here!)
- **[HOW_TO_USE_THIS_PROJECT.md](./HOW_TO_USE_THIS_PROJECT.md)** - ⭐ **5-min quick start** for any use case
- **[APPLY_TO_ANY_APP_GUIDE.md](./APPLY_TO_ANY_APP_GUIDE.md)** - Complete guide to apply this to ANY KMP app
- **[PROMPT_LIBRARY.md](./PROMPT_LIBRARY.md)** - 40 prompts to recreate/customize with Claude Code
- **[MASTER_GUIDE_INDEX.md](./MASTER_GUIDE_INDEX.md)** - Complete navigation hub for all documentation

### Visual & Reference
- [COMPLETE_WORKFLOW_VISUAL.md](./COMPLETE_WORKFLOW_VISUAL.md) - Visual step-by-step workflows
- [PROJECT_SUMMARY.md](./PROJECT_SUMMARY.md) - One-page project summary
- [QUICK_REFERENCE.md](./QUICK_REFERENCE.md) - Daily command reference

### Technical Deep Dive
- [ARCHITECTURE_DOCUMENT.md](./ARCHITECTURE_DOCUMENT.md) - Detailed architecture overview
- [CODE_FLOW_EXPLANATION.md](./CODE_FLOW_EXPLANATION.md) - Code execution flow
- [COMPLETE_GUIDE.md](./COMPLETE_GUIDE.md) - Comprehensive everything guide

### Library Specific
- [kmp-room-core/README.md](./kmp-room-core/README.md) - Library usage and API reference
- [kmp-room-core/USAGE_EXAMPLE.md](./kmp-room-core/USAGE_EXAMPLE.md) - Complete code examples
- [kmp-room-core/PUBLISHING.md](./kmp-room-core/PUBLISHING.md) - Publishing guide

### Legacy Guides
- [CURRENT_IMPLEMENTATION_GUIDE.md](./CURRENT_IMPLEMENTATION_GUIDE.md) - Current setup using published library
- [DEV_TEAM_COMPLETE_GUIDE.md](./DEV_TEAM_COMPLETE_GUIDE.md) - Creating and publishing libraries

### ⚠️ Critical Setup Information

**Your app MUST have:**
1. ✅ KSP plugin enabled
2. ✅ KSP dependencies for Android, iOS targets
3. ✅ Published library dependency
4. ✅ GitHub credentials configured

**See [CURRENT_IMPLEMENTATION_GUIDE.md](./CURRENT_IMPLEMENTATION_GUIDE.md) for complete details.**

---

## Build and Run

### Build and Run Android Application

To build and run the development version of the Android app, use the run configuration from the run widget
in your IDE’s toolbar or build it directly from the terminal:
- on macOS/Linux
  ```shell
  ./gradlew :composeApp:assembleDebug
  ```
- on Windows
  ```shell
  .\gradlew.bat :composeApp:assembleDebug
  ```

### Build and Run iOS Application

To build and run the development version of the iOS app, use the run configuration from the run widget
in your IDE’s toolbar or open the [/iosApp](./iosApp) directory in Xcode and run it from there.

---

Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…