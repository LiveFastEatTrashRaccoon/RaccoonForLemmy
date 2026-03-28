plugins {
    id("com.livefast.eattrash.kotlinMultiplatform")
    id("com.livefast.eattrash.androidTest")
    id("com.livefast.eattrash.spotless")
    alias(libs.plugins.kotlinx.kover)
    // Apply KSP plugin for annotation processing
    alias(libs.plugins.ksp)
}

kotlin {
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
        }
        val androidMain by getting {
            dependencies {
                implementation(libs.llamaAndroid)
                implementation(libs.ktor.cio)
                implementation(libs.mediapipe.tasks.text)

                // AutoValue dependencies for annotation processing
//                implementation(libs.autovalue)
//                ksp(libs.autovalue.ksp)
            }
        }
        val androidInstrumentedTest by getting {
            dependencies {
                val androidXTestVersion = "1.5.0"
                implementation ("androidx.test:runner:$androidXTestVersion")
                implementation ("androidx.test:rules:$androidXTestVersion")

                implementation(libs.kotlinx.coroutines.test)
                implementation(kotlin("test-junit"))
                implementation(project(":core:testutils"))
                implementation("androidx.test.ext:junit:1.2.1")
            }
        }
    }
}

android {
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}
