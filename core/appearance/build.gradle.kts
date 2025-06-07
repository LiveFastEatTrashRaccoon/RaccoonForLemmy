plugins {
    id("com.livefast.eattrash.kotlinMultiplatform")
    id("com.livefast.eattrash.composeMultiplatform")
    id("com.livefast.eattrash.androidTest")
    id("com.livefast.eattrash.spotless")
    alias(libs.plugins.kotlinx.kover)
}

kotlin {
    sourceSets {
        val androidMain by getting {
            dependencies {
                implementation(libs.androidx.activity)
            }
        }
        val commonMain by getting {
            dependencies {
                implementation(libs.kodein)
                implementation(libs.materialKolor)

                implementation(projects.core.di)
                implementation(projects.core.l10n)
                implementation(projects.core.resources)
            }
        }
    }
}

spotless {
    kotlin {
        target("**/BarColorProvider.kt")
        suppressLintsFor {
            step = "ktlint"
            shortCode = "compose:naming-check"
        }
    }
    kotlin {
        target("**/Colors.kt")
        suppressLintsFor {
            step = "ktlint"
            shortCode = "standard:property-naming"
        }
    }
}
