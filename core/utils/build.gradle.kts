plugins {
    id("com.livefast.eattrash.kotlinMultiplatform")
    id("com.livefast.eattrash.koinWithKsp")
    id("com.livefast.eattrash.composeMultiplatform")
    id("com.livefast.eattrash.androidTest")
    alias(libs.plugins.kotlinx.kover)
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.ktor.cio)
                implementation(libs.coil)
                implementation(libs.coil.network.ktor)
                implementation(project.dependencies.platform(libs.kotlincrypto.bom))
                implementation(libs.kotlincrypto.md5)

                implementation(projects.core.l10n)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(libs.androidx.activity.compose)
                implementation(libs.androidx.activity)
                implementation(libs.androidx.browser)
                implementation(libs.ktor.android)
                implementation(libs.coil.gif)
            }
        }
        val iosMain by getting {
            dependencies {
                implementation(libs.ktor.darwin)
            }
        }
    }
}
