import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    id("maven-publish")
}

kotlin {
    androidTarget {
        publishLibraryVariants("release") // Only publish release variant for library
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "KmpRoomCore"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            // Core Room dependencies - exposed as API
            api("androidx.room:room-runtime:2.7.0")
            api("androidx.sqlite:sqlite-bundled:2.4.0")

            // Coroutines for Flow and suspend functions
            api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
        }

        androidMain.dependencies {
            api("androidx.room:room-runtime:2.7.0")
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}

android {
    namespace = "com.brightly.kmp.room.core"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

// Publishing configuration - GitHub Packages
group = "com.brightly"
version = "1.0.2"

publishing {
    repositories {
        // GitHub Packages
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/neeraj-brightly12/KMPDatabasePOC")
            credentials {
                username = System.getenv("GITHUB_ACTOR") ?: findProperty("gpr.user") as String?
                password = System.getenv("GITHUB_TOKEN") ?: findProperty("gpr.token") as String?
            }
        }
    }
}