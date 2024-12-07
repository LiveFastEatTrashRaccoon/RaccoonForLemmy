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
                implementation(projects.core.appearance)
                implementation(projects.core.persistence)
                implementation(projects.domain.lemmy.data)
            }
        }
    }
}
