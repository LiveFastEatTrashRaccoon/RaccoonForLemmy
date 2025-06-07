plugins {
    id("com.livefast.eattrash.kotlinMultiplatform")
    id("com.livefast.eattrash.spotless")
}

kotlin {
    sourceSets {
        val androidMain by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation(libs.kotlinx.coroutines.test)
            }
        }
    }
}
