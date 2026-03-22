# KMP Room Core - Publishing Guide

This guide covers how to publish and consume the `kmp-room-core` library using different approaches.

---

## Table of Contents
1. [GitHub Packages](#github-packages)
2. [Version Management](#version-management)
3. [Troubleshooting](#troubleshooting)

---

## GitHub Packages

### Prerequisites
- GitHub account with access to the repository
- GitHub Personal Access Token (PAT) with `read:packages` and `write:packages` permissions

### Step 1: Create GitHub Personal Access Token

1. Go to GitHub Settings → Developer settings → Personal access tokens → Tokens (classic)
2. Click "Generate new token (classic)"
3. Name: `GITHUB_PACKAGES_TOKEN`
4. Select scopes:
   - ✅ `write:packages` (Upload packages to GitHub Package Registry)
   - ✅ `read:packages` (Download packages from GitHub Package Registry)
   - ✅ `repo` (Full control of private repositories - only if repo is private)
5. Generate token and **save it securely**

### Step 2: Configure Publishing

#### Update Repository URL in `kmp-room-core/build.gradle.kts`

Replace the placeholder in line 76:
```kotlin
url = uri("https://maven.pkg.github.com/YOUR_USERNAME/KMPDatabasePOC")
```

Example:
```kotlin
url = uri("https://maven.pkg.github.com/neerajsoni/KMPDatabasePOC")
```

### Step 3: Publish the Library

#### Option A: Using Environment Variables (Recommended for CI/CD)

```bash
# Set environment variables
export GITHUB_ACTOR=your-github-username
export GITHUB_TOKEN=your-personal-access-token

# Publish to GitHub Packages
./gradlew :kmp-room-core:publishMavenPublicationToGitHubPackagesRepository
```

#### Option B: Using gradle.properties (Local Development)

Create/edit `~/.gradle/gradle.properties`:
```properties
gpr.user=your-github-username
gpr.token=your-personal-access-token
```

Then publish:
```bash
./gradlew :kmp-room-core:publishMavenPublicationToGitHubPackagesRepository
```

#### Option C: Publish All Targets

To publish all configurations:
```bash
./gradlew :kmp-room-core:publish
```

### Step 4: Verify Publication

After publishing, verify at:
```
https://github.com/YOUR_USERNAME/KMPDatabasePOC/packages
```

---

## Consuming the Library from GitHub Packages

### Step 1: Configure Repository Access

In your **consuming project**, add to `settings.gradle.kts`:

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()

        // GitHub Packages
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/YOUR_USERNAME/KMPDatabasePOC")
            credentials {
                username = System.getenv("GITHUB_ACTOR") ?:
                          project.findProperty("gpr.user") as String?
                password = System.getenv("GITHUB_TOKEN") ?:
                          project.findProperty("gpr.token") as String?
            }
        }
    }
}
```

### Step 2: Add Dependency

In your module's `build.gradle.kts`:

```kotlin
dependencies {
    implementation("com.brightly:kmp-room-core:1.0.0")
}
```

### Step 3: Set Credentials

#### For Local Development:
Add to `~/.gradle/gradle.properties`:
```properties
gpr.user=your-github-username
gpr.token=your-personal-access-token
```

#### For CI/CD:
Set environment variables:
```bash
export GITHUB_ACTOR=your-github-username
export GITHUB_TOKEN=your-personal-access-token
```

#### For GitHub Actions:
```yaml
- name: Build
  env:
    GITHUB_ACTOR: ${{ github.actor }}
    GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}
  run: ./gradlew build
```

---

## Version Management

### Update Version

Edit `kmp-room-core/build.gradle.kts`:
```kotlin
publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.brightly"
            artifactId = "kmp-room-core"
            version = "1.0.1" // Update version here
        }
    }
}
```

### Semantic Versioning
- **Major (1.x.x)**: Breaking changes
- **Minor (x.1.x)**: New features, backward compatible
- **Patch (x.x.1)**: Bug fixes

### Publishing New Version

1. Update version in `build.gradle.kts`
2. Publish:
   ```bash
   ./gradlew :kmp-room-core:publish
   ```
3. (Optional) Create Git tag:
   ```bash
   git tag -a v1.0.1 -m "Release v1.0.1"
   git push origin v1.0.1
   ```

---

## Complete Publishing Commands Reference

```bash
# Publish all platforms (Recommended)
./gradlew :kmp-room-core:publish

# Publish specific target
./gradlew :kmp-room-core:publishMavenPublicationToGitHubPackagesRepository

# Publish Android release
./gradlew :kmp-room-core:publishAndroidReleasePublicationToGitHubPackagesRepository

# Publish iOS ARM64 (Physical devices)
./gradlew :kmp-room-core:publishIosArm64PublicationToGitHubPackagesRepository

# Publish iOS Simulator ARM64
./gradlew :kmp-room-core:publishIosSimulatorArm64PublicationToGitHubPackagesRepository
```

---

## Troubleshooting

### Issue: Authentication Failed (401)

**Cause:** Invalid or expired GitHub token, or missing permissions.

**Solution:**
1. Verify token has `write:packages` and `read:packages` scopes
2. Check token hasn't expired
3. For private repos, ensure token has `repo` scope
4. Verify credentials in environment variables or gradle.properties

### Issue: Package Not Found (404)

**Cause:** Package doesn't exist or authentication missing.

**Solution:**
1. Verify package was published: https://github.com/OWNER/REPO/packages
2. Check repository URL matches exactly
3. Ensure credentials are set for consuming project

### Issue: Version Conflict

**Cause:** Same version published multiple times.

**Solution:**
- Delete package version from GitHub UI (if allowed)
- Or increment version number

### Issue: SSL/TLS Error

**Cause:** Network or certificate issues.

**Solution:**
```bash
# Temporarily disable Gradle daemon
./gradlew --no-daemon :kmp-room-core:publish
```

### Issue: Module Not Found After Adding Dependency

**Cause:** Gradle sync failed or cache issue.

**Solution:**
```bash
# Clear Gradle cache
./gradlew clean
./gradlew --refresh-dependencies

# Invalidate Android Studio caches:
# File → Invalidate Caches → Invalidate and Restart
```

---

## Security Best Practices

1. **Never commit tokens to Git**
   - Use environment variables
   - Use gradle.properties (add to .gitignore)
   - Use secrets management in CI/CD

2. **Use minimal permissions**
   - Only `read:packages` for consuming
   - Only `write:packages` for publishing

3. **Rotate tokens regularly**
   - Set expiration dates on tokens
   - Update tokens before expiry

4. **Use different tokens**
   - Different tokens for CI/CD vs local development
   - Different tokens per team member

---

## CI/CD Integration Examples

### GitHub Actions

Create `.github/workflows/publish.yml`:

```yaml
name: Publish Library

on:
  push:
    tags:
      - 'v*'

jobs:
  publish:
    runs-on: ubuntu-latest

    steps:
    - uses: actions/checkout@v4

    - name: Set up JDK 17
      uses: actions/setup-java@v4
      with:
        java-version: '17'
        distribution: 'temurin'

    - name: Grant execute permission for gradlew
      run: chmod +x gradlew

    - name: Publish to GitHub Packages
      env:
        GITHUB_ACTOR: ${{ github.actor }}
        GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}
      run: ./gradlew :kmp-room-core:publish

    - name: Create Release
      uses: actions/create-release@v1
      env:
        GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}
      with:
        tag_name: ${{ github.ref }}
        release_name: Release ${{ github.ref }}
        draft: false
        prerelease: false
```

Usage:
```bash
git tag v1.0.0
git push origin v1.0.0
```

---

## Quick Start Checklist

### For Publishers:
- [ ] Create GitHub Personal Access Token with `write:packages`
- [ ] Update repository URL in `kmp-room-core/build.gradle.kts`
- [ ] Set GITHUB_ACTOR and GITHUB_TOKEN environment variables
- [ ] Run `./gradlew :kmp-room-core:publish`
- [ ] Verify package on GitHub

### For Consumers:
- [ ] Create GitHub Personal Access Token with `read:packages`
- [ ] Add maven repository to `settings.gradle.kts`
- [ ] Set credentials (environment variables or gradle.properties)
- [ ] Add dependency to `build.gradle.kts`
- [ ] Sync Gradle

---

## Support

For issues related to:
- **Library functionality**: Open issue in this repository
- **GitHub Packages**: Check [GitHub Packages Documentation](https://docs.github.com/en/packages)
- **Gradle Publishing**: Check [Gradle Publishing Documentation](https://docs.gradle.org/current/userguide/publishing_maven.html)