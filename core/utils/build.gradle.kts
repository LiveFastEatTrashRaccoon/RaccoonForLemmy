plugins {
    id("com.livefast.eattrash.kotlinMultiplatform")
    id("com.livefast.eattrash.composeMultiplatform")
    id("com.livefast.eattrash.androidTest")
    id("com.livefast.eattrash.spotless")
    alias(libs.plugins.kotlinx.kover)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kodein)
                implementation(libs.ktor.cio)
                implementation(libs.coil)
                implementation(libs.coil.network.ktor)
                implementation(project.dependencies.platform(libs.kotlincrypto.bom))
                implementation(libs.kotlincrypto.md5)

                implementation(projects.core.di)
                implementation(projects.core.l10n)
                implementation(projects.core.resources)
            }
        }
        androidMain {
            dependencies {
                implementation(libs.androidx.activity.compose)
                implementation(libs.androidx.activity)
                implementation(libs.androidx.browser)
                implementation(libs.ktor.android)
                implementation(libs.coil.gif)
            }
        }
        iosMain {
            dependencies {
                implementation(libs.ktor.darwin)
            }
        }
    }
}

spotless {
    kotlin {
        target("**/FileSystemManager.kt", "**/GalleryHelper.kt")
        suppressLintsFor {
            step = "ktlint"
            shortCode = "compose:naming-check"
        }
    }
}
