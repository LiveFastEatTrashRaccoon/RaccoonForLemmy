plugins {
    id("com.livefast.eattrash.kotlinMultiplatform")
    id("com.livefast.eattrash.composeMultiplatform")
    id("com.livefast.eattrash.androidTest")
    id("com.livefast.eattrash.serialization")
    id("com.livefast.eattrash.spotless")
    alias(libs.plugins.kotlinx.kover)
}

kotlin {
    sourceSets {
        androidMain {
            dependencies {
                implementation(libs.multiplatform.settings)
                implementation(libs.androidx.security.crypto)
            }
        }
        commonMain {
            dependencies {
                implementation(libs.kodein)
                implementation(libs.kotlinx.coroutines)
                implementation(libs.ktor.client.core)
                implementation(libs.multiplatform.settings)

                implementation(projects.core.di)
                implementation(projects.core.utils)
            }
        }
        configureEach {
            if (name == "androidHostTest") {
                dependencies {
                    implementation(libs.ktor.client.mock)
                }
            }
        }
    }
}
