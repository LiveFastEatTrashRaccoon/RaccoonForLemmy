plugins {
    id("com.livefast.eattrash.kotlinMultiplatform")
    id("com.livefast.eattrash.koinWithKsp")
    id("com.livefast.eattrash.composeMultiplatform")
    id("com.livefast.eattrash.androidTest")
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.kotlinx.kover)
}

kotlin {
    sourceSets {
        val androidMain by getting {
            dependencies {
                implementation(libs.multiplatform.settings)
                implementation(libs.androidx.security.crypto)
            }
        }
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlinx.coroutines)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.ktor.client.core)
                implementation(libs.multiplatform.settings)

                implementation(projects.core.utils)
            }
        }
        val androidUnitTest by getting {
            dependencies {
                implementation(libs.ktor.client.mock)
            }
        }
    }
}
