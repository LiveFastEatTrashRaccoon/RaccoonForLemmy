plugins {
    id("com.livefast.eattrash.kotlinMultiplatform")
    id("com.livefast.eattrash.serialization")
    alias(libs.plugins.ktorfit)
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kodein)
                implementation(libs.ktor.contentnegotiation)
                implementation(libs.ktor.json)
                implementation(libs.ktor.logging)
                implementation(libs.ktor.serialization)
                implementation(libs.ktorfit.converters.response)
                implementation(libs.ktorfit.lib)

                implementation(projects.core.utils)
            }
        }
    }
}

// workaround after KSP 2.0.0
tasks.configureEach {
    if (name.contains(Regex("ksp.*KotlinAndroid")) || name.contains(Regex("ksp.*KotlinIos*"))) {
        dependsOn(tasks.named("kspCommonMainKotlinMetadata"))
    }
}
