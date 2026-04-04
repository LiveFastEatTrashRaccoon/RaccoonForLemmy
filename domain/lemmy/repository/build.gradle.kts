plugins {
    id("com.livefast.eattrash.kotlinMultiplatform")
    id("com.livefast.eattrash.androidTest")
    id("com.livefast.eattrash.spotless")
//    id("com.android.kotlin.multiplatform.library")
    alias(libs.plugins.kotlinx.kover)
}

kotlin {

    android {
        withDeviceTest {
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            execution = "HOST"
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kodein)
                implementation(libs.kotlinx.coroutines)
                implementation(libs.ktor.client.core)

                implementation(projects.core.api)
                implementation(projects.core.persistence)
                implementation(projects.core.utils)

                implementation(projects.domain.lemmy.data)
            }

            androidMain {
                dependencies {
                    implementation(libs.llamaAndroid)
                    implementation(libs.ktor.cio)
                    implementation(libs.mediapipe.tasks.text)
                }
            }
        }

        getByName("androidDeviceTest") {

            dependencies {
                val androidXTestVersion = "1.5.0"
                implementation("androidx.test:runner:$androidXTestVersion")
                implementation("androidx.test:rules:$androidXTestVersion")

                implementation(libs.kotlinx.coroutines.test)
                implementation(kotlin("test-junit"))
                implementation(project(":core:testutils"))
                implementation("androidx.test.ext:junit:1.2.1")
            }
        }
    }
}
