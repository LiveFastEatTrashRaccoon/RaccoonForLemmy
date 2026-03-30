plugins {
    id("com.livefast.eattrash.kotlinMultiplatform")
    id("com.livefast.eattrash.composeMultiplatform")
    id("com.livefast.eattrash.spotless")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlinx.coroutines)
                implementation(libs.kodein)
                implementation(libs.kodein.compose)
            }
        }
    }
}
