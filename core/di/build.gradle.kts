plugins {
    id("com.livefast.eattrash.kotlinMultiplatform")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kodein)
            }
        }
    }
}
