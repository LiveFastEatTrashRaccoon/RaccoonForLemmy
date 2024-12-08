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
                implementation(projects.core.preferences)
                implementation(projects.core.api)
                implementation(projects.core.utils)
                implementation(projects.core.appearance)
                implementation(projects.core.persistence)
                implementation(projects.core.navigation)
                implementation(projects.core.notifications)
                implementation(projects.domain.lemmy.repository)
                implementation(projects.domain.lemmy.data)
            }
        }
    }
}
