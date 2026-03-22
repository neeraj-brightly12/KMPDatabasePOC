# Workflow Diagrams

## Library Publishing Workflow

```
┌─────────────────────────────────────────────────────────────┐
│                   LIBRARY DEVELOPMENT                        │
└─────────────────────────────────────────────────────────────┘
                            │
                            ▼
              ┌─────────────────────────┐
              │  Edit Library Code      │
              │  kmp-room-core/src/     │
              └─────────────────────────┘
                            │
                            ▼
              ┌─────────────────────────┐
              │  Update Version         │
              │  version = "1.0.2"      │
              └─────────────────────────┘
                            │
                            ▼
              ┌─────────────────────────┐
              │  Clean Build            │
              │  ./gradlew clean        │
              └─────────────────────────┘
                            │
                            ▼
              ┌─────────────────────────┐
              │  Publish                │
              │  ./gradlew publish      │
              └─────────────────────────┘
                            │
                            ▼
       ┌────────────────────┴────────────────────┐
       │                                         │
       ▼                                         ▼
┌──────────────┐                      ┌──────────────────┐
│  GitHub      │                      │  Git Tag         │
│  Packages    │                      │  git tag v1.0.2  │
│  📦          │                      │  git push        │
└──────────────┘                      └──────────────────┘
       │
       │  Available for consumption
       ▼
┌─────────────────────────────────────────────────────────────┐
│                  CONSUMER APPS                               │
└─────────────────────────────────────────────────────────────┘
```

---

## App Using Library Workflow

```
┌─────────────────────────────────────────────────────────────┐
│                    SETUP (ONE-TIME)                          │
└─────────────────────────────────────────────────────────────┘
                            │
                            ▼
       ┌────────────────────────────────────┐
       │  Create GitHub Token               │
       │  (read:packages)                   │
       └────────────────────────────────────┘
                            │
                            ▼
       ┌────────────────────────────────────┐
       │  Save to                           │
       │  ~/.gradle/gradle.properties       │
       └────────────────────────────────────┘
                            │
                            ▼
       ┌────────────────────────────────────┐
       │  Add GitHub Packages repo          │
       │  settings.gradle.kts               │
       └────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────┐
│                  DEVELOPMENT WORKFLOW                        │
└─────────────────────────────────────────────────────────────┘
                            │
                            ▼
       ┌────────────────────────────────────┐
       │  Add Library Dependency            │
       │  implementation(                   │
       │    "com.brightly:                  │
       │     kmp-room-core:1.0.1"           │
       │  )                                 │
       └────────────────────────────────────┘
                            │
                            ▼
       ┌────────────────────────────────────┐
       │  Enable KSP + Dependencies         │
       │  plugins { ksp }                   │
       │  add("kspAndroid", ...)            │
       └────────────────────────────────────┘
                            │
                            ▼
       ┌────────────────────────────────────┐
       │  Define Your Database              │
       │  - Entities (@Entity)              │
       │  - DAOs (@Dao)                     │
       │  - Database (@Database)            │
       └────────────────────────────────────┘
                            │
                            ▼
       ┌────────────────────────────────────┐
       │  Implement DatabaseFactory         │
       │  - Android (AndroidMain)           │
       │  - iOS (iosMain)                   │
       └────────────────────────────────────┘
                            │
                            ▼
       ┌────────────────────────────────────┐
       │  Gradle Sync                       │
       │  ./gradlew --refresh-dependencies  │
       └────────────────────────────────────┘
                            │
                ┌───────────┴───────────┐
                │                       │
                ▼                       ▼
     ┌──────────────────┐    ┌──────────────────┐
     │  Build Android   │    │  Build iOS       │
     │  ./gradlew       │    │  ./gradlew       │
     │  assembleDebug   │    │  compileKotlin   │
     │                  │    │  IosSimulator    │
     └──────────────────┘    └──────────────────┘
                │                       │
                └───────────┬───────────┘
                            │
                            ▼
                    ┌───────────────┐
                    │  Run App ▶️   │
                    └───────────────┘
```

---

## KSP Processing Flow

```
┌─────────────────────────────────────────────────────────────┐
│                    YOUR APP CODE                             │
└─────────────────────────────────────────────────────────────┘
                            │
       ┌────────────────────┼────────────────────┐
       │                    │                    │
       ▼                    ▼                    ▼
┌──────────────┐  ┌──────────────────┐  ┌──────────────┐
│   @Entity    │  │      @Dao        │  │  @Database   │
│  UserEntity  │  │    UserDao       │  │ AppDatabase  │
└──────────────┘  └──────────────────┘  └──────────────┘
       │                    │                    │
       └────────────────────┼────────────────────┘
                            │
                            ▼
                ┌───────────────────────┐
                │   KSP PROCESSOR       │
                │   (room-compiler)     │
                └───────────────────────┘
                            │
       ┌────────────────────┼────────────────────┐
       │                    │                    │
       ▼                    ▼                    ▼
┌──────────────┐  ┌──────────────────┐  ┌──────────────────┐
│ UserDao_Impl │  │ AppDatabase_Impl │  │AppDatabaseConstr │
│              │  │                  │  │     uctor        │
│ (Generated)  │  │   (Generated)    │  │   (Generated)    │
└──────────────┘  └──────────────────┘  └──────────────────┘
       │                    │                    │
       └────────────────────┼────────────────────┘
                            │
                            ▼
              ┌─────────────────────────┐
              │  Compiled App           │
              │  (Ready to Run)         │
              └─────────────────────────┘
```

---

## Library Consumption Flow

```
┌─────────────────────────────────────────────────────────────┐
│                   GRADLE SYNC                                │
└─────────────────────────────────────────────────────────────┘
                            │
                            ▼
              ┌─────────────────────────┐
              │  Read settings.gradle   │
              │  Find GitHub Packages   │
              └─────────────────────────┘
                            │
                            ▼
              ┌─────────────────────────┐
              │  Authenticate           │
              │  (gpr.user/gpr.token)   │
              └─────────────────────────┘
                            │
                            ▼
              ┌─────────────────────────┐
              │  Download Library       │
              │  kmp-room-core:1.0.1    │
              └─────────────────────────┘
                            │
       ┌────────────────────┴────────────────────┐
       │                                         │
       ▼                                         ▼
┌──────────────────┐                  ┌──────────────────┐
│   Android        │                  │      iOS         │
│  .aar file       │                  │   .klib file     │
│                  │                  │                  │
│  kmp-room-core-  │                  │ kmp-room-core-   │
│  android-1.0.1   │                  │ iosarm64-1.0.1   │
└──────────────────┘                  └──────────────────┘
       │                                         │
       └────────────────────┬────────────────────┘
                            │
                            ▼
              ┌─────────────────────────┐
              │  Available in App       │
              │  (AndroidDatabaseFactory│
              │   IosDatabaseFactory)   │
              └─────────────────────────┘
```

---

## Component Responsibility

```
┌──────────────────────────────────────────────────────────────┐
│                    KMP-ROOM-CORE LIBRARY                      │
│                      (Published)                              │
├──────────────────────────────────────────────────────────────┤
│  ✅ AndroidDatabaseFactory                                   │
│  ✅ IosDatabaseFactory                                       │
│  ✅ DatabaseConfig                                           │
│  ✅ DatabaseUtils                                            │
│  ✅ MigrationBuilder                                         │
│  ✅ Flow Extensions                                          │
│  ✅ Room Runtime (dependency)                                │
│  ✅ SQLite Driver (dependency)                               │
└──────────────────────────────────────────────────────────────┘
                            ▲
                            │ uses
                            │
┌──────────────────────────────────────────────────────────────┐
│                      YOUR APP                                 │
│                  (Consumer Project)                           │
├──────────────────────────────────────────────────────────────┤
│  ✅ Entities (@Entity)                                       │
│  ✅ DAOs (@Dao)                                              │
│  ✅ Database (@Database)                                     │
│  ✅ DatabaseFactory implementations                          │
│  ✅ Business Logic                                           │
│  ✅ UI Layer                                                 │
│                                                              │
│  ⚠️  KSP Required (room-compiler)                           │
│      - Generates DAO implementations                         │
│      - Generates Database constructor                        │
│      - Validates queries                                     │
└──────────────────────────────────────────────────────────────┘
```

---

## Version Update Flow

```
┌─────────────────────────────────────────────────────────────┐
│                   LIBRARY v1.0.1                             │
│                   (Currently Published)                      │
└─────────────────────────────────────────────────────────────┘
                            │
                            │ New features/fixes
                            ▼
              ┌─────────────────────────┐
              │  Update to v1.0.2       │
              │  Publish                │
              └─────────────────────────┘
                            │
                            ▼
┌─────────────────────────────────────────────────────────────┐
│                   LIBRARY v1.0.2                             │
│                   (Newly Published)                          │
└─────────────────────────────────────────────────────────────┘
                            │
                            │ Both versions available
                            ▼
              ┌─────────────────────────┐
              │  Apps Choose Version    │
              └─────────────────────────┘
                            │
       ┌────────────────────┴────────────────────┐
       │                                         │
       ▼                                         ▼
┌──────────────────┐                  ┌──────────────────┐
│   App A          │                  │   App B          │
│   Still using    │                  │   Updated to     │
│   v1.0.1         │                  │   v1.0.2         │
└──────────────────┘                  └──────────────────┘

   Both work fine!                    Gets new features!
```

---

## Full Build Process

```
Developer runs: ./gradlew :composeApp:assembleDebug

                            │
                            ▼
              ┌─────────────────────────┐
              │  Resolve Dependencies   │
              │  - Download library     │
              │  - Resolve transitives  │
              └─────────────────────────┘
                            │
                            ▼
              ┌─────────────────────────┐
              │  Run KSP                │
              │  - Process @Entity      │
              │  - Process @Dao         │
              │  - Process @Database    │
              │  - Generate code        │
              └─────────────────────────┘
                            │
                            ▼
              ┌─────────────────────────┐
              │  Compile Kotlin         │
              │  - App code             │
              │  - Generated code       │
              │  - Library code         │
              └─────────────────────────┘
                            │
                            ▼
              ┌─────────────────────────┐
              │  Package APK            │
              │  - Combine all classes  │
              │  - Add resources        │
              │  - Sign (debug)         │
              └─────────────────────────┘
                            │
                            ▼
              ┌─────────────────────────┐
              │  APK Ready! ✅          │
              └─────────────────────────┘
```

---

## Summary

**Key Takeaways:**

1. **Library** provides infrastructure, not generated code
2. **KSP** is always required in consumer apps
3. **Gradle** automatically resolves correct platform variant
4. **Publishing** deploys all 4 artifacts (root + 3 platforms)
5. **Versioning** allows multiple versions to coexist

**Publishing:** Library → GitHub Packages → Available for all apps
**Consuming:** App → Downloads library → KSP generates code → Build succeeds
