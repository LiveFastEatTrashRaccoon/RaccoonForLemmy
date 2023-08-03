plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.native.cocoapods)
    alias(libs.plugins.ktorfit)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlinx.serialization)
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    targetHierarchy.default()

    android {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        version = "1.0"
        ios.deploymentTarget = "14.1"
        framework {
            baseName = "core-api"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.koin.core)
                api(libs.ktorfit.lib)
                implementation(libs.ktor.serialization)
                implementation(libs.ktor.contentnegotiation)
                implementation(libs.ktor.json)
                implementation(libs.ktor.logging)
                implementation(projects.coreUtils)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}

android {
    namespace = "com.github.diegoberaldin.raccoonforlemmy.core.api"
    compileSdk = 33
    defaultConfig {
        minSdk = 26
    }
}

dependencies {
    val ktorfitVersion = libs.versions.ktorfit.lib.get()
    add(
        "kspCommonMainMetadata",
        "de.jensklingenberg.ktorfit:ktorfit-ksp:$ktorfitVersion",
    )
    add(
        "kspAndroid",
        "de.jensklingenberg.ktorfit:ktorfit-ksp:$ktorfitVersion",
    )
    add(
        "kspIosX64",
        "de.jensklingenberg.ktorfit:ktorfit-ksp:$ktorfitVersion",
    )
    add(
        "kspIosSimulatorArm64",
        "de.jensklingenberg.ktorfit:ktorfit-ksp:$ktorfitVersion",
    )
}
