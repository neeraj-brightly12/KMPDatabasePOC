# Prompt Library for KMP Room Database Implementation

This document contains all the prompts used to create the `kmp-room-core` library, publish it to GitHub Packages, and integrate it into a KMP application. Use these prompts sequentially with Claude Code to recreate or adapt this setup for any KMP project.

---

## Table of Contents

1. [Initial Setup Prompts](#initial-setup-prompts)
2. [Library Creation Prompts](#library-creation-prompts)
3. [Publishing Configuration Prompts](#publishing-configuration-prompts)
4. [App Integration Prompts](#app-integration-prompts)
5. [Build and Test Prompts](#build-and-test-prompts)
6. [Documentation Prompts](#documentation-prompts)
7. [Troubleshooting Prompts](#troubleshooting-prompts)

---

## Initial Setup Prompts

### Prompt 1: Create KMP Project Structure

```
Create a new Kotlin Multiplatform project with the following structure:
- Support for Android and iOS platforms
- Use Compose Multiplatform for UI
- Include Room Database support
- Project name: KMPDatabasePOC
- Package name: com.brightly.kmpdatabasepoc
- Set up gradle.properties with appropriate Kotlin and compose versions
```

### Prompt 2: Add Required Dependencies

```
Add the following dependencies to the project:
1. Room Database 2.7.0 for Android and iOS
2. SQLite bundled driver 2.4.0
3. KSP plugin 2.1.20-1.0.31
4. Kotlinx coroutines
5. Configure KSP for all targets (Android, iosArm64, iosSimulatorArm64)
```

---

## Library Creation Prompts

### Prompt 3: Create Library Module Structure

```
Create a new library module named 'kmp-room-core' with:
- Kotlin Multiplatform structure (commonMain, androidMain, iosMain)
- Android library plugin configuration
- Maven publish plugin for GitHub Packages
- Group ID: com.brightly
- Artifact ID: kmp-room-core
- Version: 1.0.0
```

### Prompt 4: Create Platform Database Factory (Common)

```
In kmp-room-core/src/commonMain/kotlin, create:

1. DatabaseConfig data class with properties:
   - name: String
   - version: Int
   - enableLogging: Boolean
   - migrations: List<Migration>

2. KmpDatabaseFactory interface with:
   - createDatabase method that accepts name and migrations
   - Platform-specific implementations

Include comprehensive documentation for each component.
```

### Prompt 5: Create Android Database Factory

```
In kmp-room-core/src/androidMain/kotlin, create AndroidDatabaseFactory:
- Abstract class that requires Android Context
- Implements buildDatabase method using Room.databaseBuilder
- Configures AndroidSQLiteDriver
- Supports migrations and logging
- Handles database configuration from DatabaseConfig
- Include proper error handling and documentation
```

### Prompt 6: Create iOS Database Factory

```
In kmp-room-core/src/iosMain/kotlin, create IosDatabaseFactory:
- Abstract class for iOS platform
- Implements buildDatabase method using Room.databaseBuilder
- Configures BundledSQLiteDriver for iOS
- Uses NSHomeDirectory for database path
- Supports migrations and logging
- Include proper error handling and documentation
```

### Prompt 7: Create Database Utilities

```
Create DatabaseUtils with expect/actual pattern:

Common interface:
- databaseExists(name: String): Boolean
- getDatabasePath(name: String): String
- deleteDatabase(name: String): Boolean

Platform implementations:
- Android: Use context.getDatabasePath
- iOS: Use NSHomeDirectory/Documents path

Include unit tests for each utility function.
```

### Prompt 8: Create Migration Builder Utilities

```
Create a DSL for building Room migrations:

1. migration(from: Int, to: Int, block: (SupportSQLiteDatabase) -> Unit): Migration
2. Extension functions for common migration operations:
   - addColumn
   - dropColumn
   - renameTable
   - createIndex

Include usage examples in documentation.
```

### Prompt 9: Create Flow Extensions

```
In extensions package, create Flow extensions for Room queries:
- mapToSize(): Flow<Int> - Get size of list
- mapToFirstOrNull(): Flow<T?> - Get first element or null
- filterNotEmpty(): Flow<List<T>> - Filter out empty lists
- distinctUntilChangedBy: Distinct by property

Include comprehensive KDoc comments.
```

### Prompt 10: Create Base DAO and Repository

```
Create base classes for common patterns:

1. BaseDao interface with:
   - insert, update, delete operations
   - Batch operations
   - Transaction support

2. BaseRepository abstract class with:
   - Common repository patterns
   - Error handling
   - Flow transformation utilities

Include usage examples and best practices.
```

---

## Publishing Configuration Prompts

### Prompt 11: Configure GitHub Packages Publishing

```
Configure Maven publishing for GitHub Packages:

1. Update kmp-room-core/build.gradle.kts:
   - Add maven-publish plugin
   - Set group, version, artifactId
   - Configure GitHub Packages repository URL
   - Set up credentials from environment variables or gradle.properties

2. Repository URL pattern:
   https://maven.pkg.github.com/{USERNAME}/{REPOSITORY}

3. Credentials:
   - GITHUB_ACTOR or gpr.user
   - GITHUB_TOKEN or gpr.token
```

### Prompt 12: Create Publishing Documentation

```
Create PUBLISHING.md documentation with:
1. Prerequisites (GitHub account, repository, token)
2. Step-by-step token creation guide
3. Gradle properties configuration
4. Publishing commands
5. Version management guidelines
6. Troubleshooting common issues
7. CI/CD integration examples

Include screenshots or clear instructions for each step.
```

### Prompt 13: Publish Library to GitHub Packages

```
Execute the following steps to publish the library:

1. Clean the project:
   ./gradlew :kmp-room-core:clean

2. Publish to GitHub Packages:
   ./gradlew :kmp-room-core:publish

3. Verify publication:
   - Check GitHub repository packages
   - Verify all 4 artifacts are uploaded (Android, iOS ARM64, iOS Simulator, metadata)

4. Tag the release:
   git tag v1.0.0
   git push origin v1.0.0

Document any errors and solutions.
```

---

## App Integration Prompts

### Prompt 14: Configure App to Use Published Library

```
Update the main app (composeApp module) to use the published library:

1. Remove local project dependency if exists
2. Add GitHub Packages repository in settings.gradle.kts
3. Add library dependency: implementation("com.brightly:kmp-room-core:1.0.0")
4. Ensure KSP is configured for the app module
5. Add Room compiler dependencies for all targets
6. Sync and verify dependency resolution
```

### Prompt 15: Create Entity, DAO, and Database

```
In composeApp/src/commonMain/kotlin, create:

1. Entity:
   @Entity(tableName = "users")
   data class UserEntity with id, name, email fields

2. DAO:
   @Dao interface UserDao with:
   - insertUser, getUsers, deleteUser operations
   - Use Flow for queries

3. Database:
   @Database(entities = [UserEntity::class], version = 1)
   abstract class AppDatabase : RoomDatabase()
   - Include @ConstructedBy annotation
   - Define abstract DAO getters

4. Create expect/actual AppDatabaseConstructor
```

### Prompt 16: Implement Platform-Specific Database Factories

```
Create DatabaseFactory implementations:

Android (androidMain):
- Extend AndroidDatabaseFactory from kmp-room-core
- Require Context in constructor
- Implement createDatabase method
- Configure with DatabaseConfig

iOS (iosMain):
- Extend IosDatabaseFactory from kmp-room-core
- Implement createDatabase method
- Configure with DatabaseConfig

Both should return AppDatabase instance.
```

### Prompt 17: Create Repository Layer

```
Create UserRepository in commonMain:
- Accept AppDatabase in constructor
- Wrap DAO operations with proper error handling
- Expose Flow for reactive queries
- Implement suspend functions for write operations
- Add logging for debugging
- Include comprehensive documentation
```

### Prompt 18: Create ViewModel

```
Create UserViewModel in commonMain:
- Use ViewModel from androidx.lifecycle
- Accept UserRepository dependency
- Expose StateFlow for UI state
- Implement user operations (add, delete, load)
- Handle loading and error states
- Use viewModelScope for coroutines
```

### Prompt 19: Create UI with Compose

```
Create Compose UI in commonMain:
- Display list of users
- Add user form with TextField
- Delete user functionality
- Show loading state
- Display error messages
- Use Material3 components
- Implement proper state management
```

### Prompt 20: Set Up Dependency Injection

```
Create a simple DI setup or use manual dependency injection:

1. Create DatabaseModule:
   - Provide singleton DatabaseFactory
   - Provide singleton AppDatabase
   - Provide repositories

2. Create ViewModelFactory:
   - Handle ViewModel creation with dependencies

Or use Koin/Kodein for DI if preferred.
```

---

## Build and Test Prompts

### Prompt 21: Build Android Application

```
Build and verify the Android application:

Commands:
./gradlew :composeApp:assembleDebug
./gradlew :composeApp:assembleRelease

Verify:
- No compilation errors
- KSP generated Room code successfully
- App runs on Android device/emulator
- Database operations work correctly
- Check for memory leaks
```

### Prompt 22: Build iOS Application

```
Build and verify the iOS application:

Commands:
./gradlew :composeApp:compileKotlinIosSimulatorArm64
./gradlew :composeApp:compileKotlinIosArm64

Verify:
- No compilation errors
- Framework generated successfully
- KSP generated Room code for iOS
- App runs on iOS simulator
- Database operations work correctly
```

### Prompt 23: Run Tests

```
Create and run tests for:

1. Database operations:
   - Insert, query, update, delete
   - Flow emissions
   - Transactions

2. Repository layer:
   - Business logic
   - Error handling

3. ViewModel:
   - State management
   - User interactions

Commands:
./gradlew :composeApp:testDebugUnitTest (Android)
./gradlew :composeApp:iosSimulatorArm64Test (iOS)
```

### Prompt 24: Integration Testing

```
Perform integration testing:

1. Test data persistence across app restarts
2. Test migration scenarios (upgrade database version)
3. Test concurrent database access
4. Test large dataset performance
5. Verify memory usage
6. Check for crashes or ANRs

Document test results and any issues found.
```

---

## Documentation Prompts

### Prompt 25: Create Library README

```
Create comprehensive README.md for kmp-room-core with:
- Overview and features
- Installation instructions
- Usage examples
- API reference
- Platform-specific notes
- Migration guide
- Troubleshooting section
- Version compatibility table
- License and support information
```

### Prompt 26: Create Usage Examples

```
Create USAGE_EXAMPLE.md with complete examples:
- Basic setup
- Entity and DAO creation
- Database initialization
- Repository pattern
- ViewModel integration
- UI integration
- Migration examples
- Advanced use cases
- Common patterns
- Best practices

Include full code snippets for each example.
```

### Prompt 27: Create Architecture Documentation

```
Create ARCHITECTURE_DOCUMENT.md explaining:
- Overall architecture design
- Layer responsibilities (Entity, DAO, Repository, ViewModel, UI)
- Data flow diagrams
- Platform abstraction strategy
- Dependency graph
- Design patterns used
- Scalability considerations
- Performance optimizations
- Security considerations
```

### Prompt 28: Create Quick Reference Guide

```
Create QUICK_REFERENCE.md with:
- Common commands (build, publish, clean)
- Dependency configurations
- Common code snippets
- Troubleshooting quick fixes
- Version upgrade checklist
- Git workflow
- One-page cheat sheet format
```

### Prompt 29: Create Complete Guide

```
Create COMPLETE_GUIDE.md covering:
- Part 1: Using the published library
- Part 2: Publishing library updates
- Part 3: Understanding KSP requirements
- Part 4: Troubleshooting
- Part 5: Advanced topics
- Part 6: Team collaboration
- Include flowcharts and diagrams where helpful
```

### Prompt 30: Create README_START_HERE

```
Create README_START_HERE.md as documentation index:
- Overview of all documentation files
- Recommended reading order
- Quick start paths for different roles
- Essential links and resources
- Status and checklist
- Support information
- Keep it concise and navigable
```

---

## Troubleshooting Prompts

### Prompt 31: Debug KSP Code Generation

```
Debug KSP code generation issues:

1. Check generated code location:
   - build/generated/ksp/android/debug
   - build/generated/ksp/iosArm64/main

2. Verify KSP is running:
   ./gradlew :composeApp:kspDebugKotlinAndroid --info

3. Common fixes:
   - Clean build directory
   - Invalidate caches
   - Check annotation syntax
   - Verify Room version compatibility

Document findings and solutions.
```

### Prompt 32: Debug GitHub Packages Authentication

```
Debug authentication issues with GitHub Packages:

1. Verify token has correct scopes:
   - read:packages
   - write:packages (for publishing)

2. Check credentials location:
   - ~/.gradle/gradle.properties
   - Environment variables

3. Test authentication:
   curl -H "Authorization: token YOUR_TOKEN" \
     https://maven.pkg.github.com/USERNAME/REPO/

4. Common issues:
   - Token expired
   - Wrong username
   - Repository access denied
   - Package visibility settings

Provide step-by-step resolution.
```

### Prompt 33: Debug Platform-Specific Issues

```
Debug platform-specific issues:

Android:
- Context memory leaks
- SQLite version conflicts
- ProGuard/R8 rules
- Multidex issues

iOS:
- Framework linking
- BundledSQLiteDriver configuration
- Path resolution
- Bitcode settings

Document platform-specific solutions.
```

### Prompt 34: Debug Build Failures

```
Debug and fix build failures:

1. Analyze error messages
2. Check dependency conflicts
3. Verify plugin versions
4. Clean and rebuild
5. Check Gradle daemon
6. Verify environment setup

Commands:
./gradlew clean
./gradlew --stop
./gradlew build --stacktrace --info

Document each error and its solution.
```

### Prompt 35: Performance Optimization

```
Optimize performance:

1. Database query optimization:
   - Add indexes
   - Optimize queries
   - Use transactions

2. Build optimization:
   - Enable Gradle cache
   - Use configuration cache
   - Parallel execution

3. Runtime optimization:
   - Lazy initialization
   - Background threading
   - Memory management

Measure and document improvements.
```

---

## Advanced Prompts

### Prompt 36: Add Database Encryption

```
Add database encryption support:

1. Add SQLCipher dependency
2. Create EncryptedDatabaseFactory
3. Handle encryption key management
4. Update documentation
5. Add encryption examples
6. Test encrypted database operations

Note: Platform-specific implementation required.
```

### Prompt 37: Add Multi-Module Support

```
Extend library to support multiple database modules:

1. Create modular architecture
2. Support feature-based database modules
3. Implement database federation
4. Add module configuration
5. Update documentation
6. Create examples for multi-module setup
```

### Prompt 38: Add Database Backup/Restore

```
Implement backup and restore functionality:

1. Create BackupManager
2. Implement export to JSON/SQL
3. Implement import from backup
4. Handle schema versioning
5. Add platform-specific file operations
6. Create UI for backup/restore
7. Add tests for data integrity
```

### Prompt 39: Add Database Inspector

```
Create debugging tools:

1. Database content viewer
2. Query console
3. Schema inspector
4. Performance monitor
5. Debug-only features
6. Compose UI for inspector

Only for debug builds.
```

### Prompt 40: Set Up CI/CD

```
Configure CI/CD pipeline:

1. GitHub Actions workflow for:
   - Build verification
   - Test execution
   - Library publishing
   - Version tagging
   - Documentation generation

2. Configure secrets:
   - GitHub token
   - Signing keys

3. Add status badges to README

Create .github/workflows/publish.yml
```

---

## Usage Instructions

### How to Use These Prompts

1. **Sequential Execution**: Use prompts in order for initial setup
2. **Adaptation**: Modify prompts to match your project requirements
3. **Iteration**: Some prompts may need multiple iterations
4. **Verification**: Test after each major prompt
5. **Documentation**: Document changes and decisions

### Prompt Categories

- **Setup (1-2)**: Initial project creation
- **Library (3-10)**: Core library implementation
- **Publishing (11-13)**: Publishing configuration
- **Integration (14-20)**: App integration
- **Testing (21-24)**: Build and test
- **Docs (25-30)**: Documentation creation
- **Debug (31-35)**: Troubleshooting
- **Advanced (36-40)**: Additional features

### Tips for Success

1. **Understand Context**: Read existing documentation before using prompts
2. **Adapt Variables**: Replace placeholder values with your specifics
3. **Test Incrementally**: Don't run all prompts at once
4. **Document Changes**: Keep notes on customizations
5. **Version Control**: Commit after each successful prompt
6. **Review Output**: Carefully review generated code
7. **Ask Questions**: Request clarification if output is unclear

### Customization Guide

Replace these variables in prompts:

- `{USERNAME}`: Your GitHub username
- `{REPOSITORY}`: Your repository name
- `{PACKAGE}`: Your package name
- `{GROUP_ID}`: Your Maven group ID
- `{VERSION}`: Your version number
- `{PROJECT_NAME}`: Your project name

### Success Criteria

After completing all prompts, you should have:

- ✅ Working KMP library module
- ✅ Published to GitHub Packages
- ✅ Sample app using the library
- ✅ Comprehensive documentation
- ✅ Build and test configuration
- ✅ CI/CD pipeline (optional)
- ✅ Working on Android and iOS

---

## Example Conversation Flow

Here's an example of how to use these prompts with Claude Code:

```
User: [Use Prompt 1]

Claude: [Creates KMP project structure]

User: [Review output, then use Prompt 2]

Claude: [Adds dependencies]

User: [Build and verify, then use Prompt 3]

Claude: [Creates library module]

... continue sequentially ...
```

---

## Additional Resources

### Related Documentation
- README.md - Project overview
- COMPLETE_GUIDE.md - Comprehensive guide
- QUICK_REFERENCE.md - Command reference
- ARCHITECTURE_DOCUMENT.md - Architecture details

### External Links
- [Room Documentation](https://developer.android.com/training/data-storage/room)
- [KMP Documentation](https://kotlinlang.org/docs/multiplatform.html)
- [KSP Documentation](https://kotlinlang.org/docs/ksp-overview.html)
- [GitHub Packages](https://docs.github.com/en/packages)

---

## Version History

| Version | Date | Changes |
|---------|------|---------|
| 1.0.0 | 2026-03-20 | Initial prompt library |

---

## License

Copyright © 2026 Brightly

---

## Support

For questions or issues with these prompts:
1. Review the generated documentation
2. Check troubleshooting prompts
3. Consult external documentation
4. Contact the development team

---

**Note**: These prompts are designed to work with Claude Code CLI. Adjust as needed for your specific use case or other AI assistants.