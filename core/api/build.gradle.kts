plugins {
    id("com.livefast.eattrash.kotlinMultiplatform")
    id("com.livefast.eattrash.serialization")
    id("com.livefast.eattrash.spotless")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kodein)
                implementation(libs.ktor.contentnegotiation)
                implementation(libs.ktor.json)
                implementation(libs.ktor.logging)
                implementation(libs.ktor.serialization)

                implementation(projects.core.di)
                implementation(projects.core.utils)
            }
        }
    }
}
