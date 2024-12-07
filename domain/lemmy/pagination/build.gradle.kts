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
                implementation(projects.core.notifications)
                implementation(projects.core.utils)
                implementation(projects.core.persistence)

                implementation(projects.domain.identity)
                implementation(projects.domain.lemmy.data)
                implementation(projects.domain.lemmy.repository)
            }
        }
    }
}
