plugins {
    id("com.livefast.eattrash.kotlinMultiplatform")
    id("com.livefast.eattrash.androidTest")
    id("com.livefast.eattrash.spotless")
    alias(libs.plugins.kotlinx.kover)
}

kotlin {
    sourceSets {
        val commonMain by getting {
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
    }
}
