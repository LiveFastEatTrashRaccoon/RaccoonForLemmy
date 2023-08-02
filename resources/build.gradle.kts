plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.compose)
    alias(libs.plugins.native.cocoapods)
    alias(libs.plugins.moko.resources)
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    targetHierarchy.default()

    android {
        compilations.all {
            kotlinOptions {
                jvmTarget = "17"
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
            baseName = "resources"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.koin.core)
                api(libs.moko.resources)
                api(libs.moko.resources.compose)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}

multiplatformResources {
    multiplatformResourcesPackage = "com.github.diegoberaldin.raccoonforlemmy.resources" // required
    iosBaseLocalizationRegion = "en"
}

android {
    namespace = "com.github.diegoberaldin.raccoonforlemmy.resources"
    compileSdk = 33
    defaultConfig {
        minSdk = 26
    }
}
