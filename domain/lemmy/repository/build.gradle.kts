plugins {
    id("com.livefast.eattrash.kotlinMultiplatform")
    id("com.livefast.eattrash.koinWithKsp")
    id("com.livefast.eattrash.androidTest")
    alias(libs.plugins.kotlinx.kover)
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.ktorfit.lib)

                implementation(projects.core.api)
                implementation(projects.core.utils)

                implementation(projects.domain.lemmy.data)
            }
        }
    }
}
