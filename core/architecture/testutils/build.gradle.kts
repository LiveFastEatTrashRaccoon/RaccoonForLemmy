plugins {
    id("com.livefast.eattrash.kotlinMultiplatform")
}

kotlin {
    sourceSets {
        androidMain {
            dependencies {
                implementation(libs.kotlinx.coroutines)
                implementation(projects.core.architecture)
                implementation(kotlin("test-junit"))
            }
        }
    }
}
