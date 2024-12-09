plugins {
    id("com.livefast.eattrash.kotlinMultiplatform")
}

kotlin {
    sourceSets {
        val commonMain by getting
        val androidMain by getting {
            dependencies {
                implementation(libs.kotlinx.coroutines)
                implementation(projects.core.architecture)
                implementation(kotlin("test-junit"))
            }
        }
    }
}
