# Library Publishing Setup - Summary

## What Has Been Configured

### 1. Build Configuration Updated ✅
- **File**: `kmp-room-core/build.gradle.kts`
- Added GitHub Packages publishing repository
- Added Local Maven publishing repository
- Configured to publish all Kotlin Multiplatform artifacts

### 2. Documentation Created ✅
- **PUBLISHING.md** - Complete guide with troubleshooting and CI/CD examples
- **PUBLISHING_QUICK_START.md** - 30-second quick reference guide
- **EXAMPLE_WORKFLOW.md** - Real-world step-by-step example
- **README.md** - Updated with installation instructions
- **.github/workflows/publish-library.yml** - Automated GitHub Actions workflow

---

## What You Need to Do Next

### Step 1: Update Repository URL (REQUIRED)

Edit `kmp-room-core/build.gradle.kts` at line **~76**:

**Change this:**
```kotlin
url = uri("https://maven.pkg.github.com/OWNER/REPOSITORY")
```

**To this:**
```kotlin
url = uri("https://maven.pkg.github.com/YOUR_GITHUB_USERNAME/KMPDatabasePOC")
```

Example:
```kotlin
url = uri("https://maven.pkg.github.com/neerajsoni/KMPDatabasePOC")
```

### Step 2: Create GitHub Personal Access Token

1. Go to: https://github.com/settings/tokens/new
2. Name: `GITHUB_PACKAGES_TOKEN`
3. Select scopes:
   - ✅ `write:packages`
   - ✅ `read:packages`
   - ✅ `repo` (if repository is private)
4. Generate and save the token securely

### Step 3: Publish to GitHub Packages

```bash
# Set credentials
export GITHUB_ACTOR=your-github-username
export GITHUB_TOKEN=your-personal-access-token

# Publish
./gradlew :kmp-room-core:publish
```

### Step 4: Verify Publication

Go to:
```
https://github.com/YOUR_USERNAME/KMPDatabasePOC/packages
```

You should see `kmp-room-core` package listed.

---

## Using the Library in Other Projects

### Setup Once (Per Machine)

Add to `~/.gradle/gradle.properties`:
```properties
gpr.user=your-github-username
gpr.token=your-personal-access-token
```

### In Each Project

**settings.gradle.kts:**
```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://maven.pkg.github.com/YOUR_USERNAME/KMPDatabasePOC")
            credentials {
                username = System.getenv("GITHUB_ACTOR") ?:
                          findProperty("gpr.user") as String?
                password = System.getenv("GITHUB_TOKEN") ?:
                          findProperty("gpr.token") as String?
            }
        }
    }
}
```

**build.gradle.kts:**
```kotlin
dependencies {
    implementation("com.brightly:kmp-room-core:1.0.0")
}
```

---

## Automated Publishing with GitHub Actions

The workflow file is already created at:
`.github/workflows/publish-library.yml`

**To trigger automatic publishing:**

```bash
# Create and push a version tag
git tag v1.0.0
git push origin v1.0.0
```

The GitHub Action will:
- Build the library
- Publish to GitHub Packages
- Create a GitHub Release with installation instructions

---

## Quick Commands Reference

### Publish Commands
```bash
# Publish all platforms (Recommended)
./gradlew :kmp-room-core:publish

# Publish to GitHub Packages (specific target)
./gradlew :kmp-room-core:publishMavenPublicationToGitHubPackagesRepository
```

### Version Update
Edit `kmp-room-core/build.gradle.kts`:
```kotlin
version = "1.0.1"  // Update version
```
Then republish.

---

## Important Notes

### Security
- ⚠️ **NEVER commit tokens to Git**
- ✅ Use environment variables or `~/.gradle/gradle.properties`
- ✅ Add `gradle.properties` to `.gitignore` if storing tokens there

### KSP Requirement
- 🔴 **Consumer apps MUST add KSP plugin and room-compiler**
- 🟢 The library itself does NOT need KSP (it has no Room entities)
- 🟢 Apps using the library need KSP to process their own @Entity/@Dao/@Database

### Version Management
- Use semantic versioning: `MAJOR.MINOR.PATCH`
- Example: `1.0.0` → `1.0.1` (bug fix) → `1.1.0` (new feature) → `2.0.0` (breaking change)

---

## Documentation Files

| File | Purpose |
|------|---------|
| `PUBLISHING_QUICK_START.md` | 30-second quick reference |
| `PUBLISHING.md` | Complete guide with troubleshooting |
| `README.md` | Library usage and API documentation |
| `.github/workflows/publish-library.yml` | Automated publishing |

---

## Next Steps Checklist

- [ ] Update repository URL in `kmp-room-core/build.gradle.kts`
- [ ] Create GitHub Personal Access Token
- [ ] Publish library (`./gradlew :kmp-room-core:publish`)
- [ ] Verify package on GitHub
- [ ] Test consuming library in another project
- [ ] (Optional) Set up GitHub Actions for automated publishing

---

## Support

For detailed help, see:
- Quick setup: [PUBLISHING_QUICK_START.md](kmp-room-core/PUBLISHING_QUICK_START.md)
- Full documentation: [PUBLISHING.md](kmp-room-core/PUBLISHING.md)
- Library usage: [README.md](kmp-room-core/README.md)