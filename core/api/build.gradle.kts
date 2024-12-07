plugins {
    id("com.livefast.eattrash.kotlinMultiplatform")
    id("com.livefast.eattrash.koinWithKsp")
    alias(libs.plugins.ktorfit)
    alias(libs.plugins.kotlinx.serialization)
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.ktorfit.lib)
                implementation(libs.ktor.serialization)
                implementation(libs.ktor.contentnegotiation)
                implementation(libs.ktor.json)
                implementation(libs.ktor.logging)

                implementation(projects.core.utils)
            }
        }
    }
}
