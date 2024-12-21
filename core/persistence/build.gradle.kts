plugins {
    id("com.livefast.eattrash.kotlinMultiplatform")
    alias(libs.plugins.sqldelight)
    id("com.livefast.eattrash.composeMultiplatform")
    id("com.livefast.eattrash.serialization")
    id("com.livefast.eattrash.androidTest")
    alias(libs.plugins.kotlinx.kover)
}

kotlin {
    sourceSets {
        val androidMain by getting {
            dependencies {
                implementation(libs.sqlcipher)
                implementation(libs.sqldelight.android)
            }
        }
        val iosMain by getting {
            dependencies {
                implementation(libs.sqldelight.native)
            }
        }
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlinx.coroutines)
                implementation(libs.kotlinx.serialization.json)

                implementation(libs.kodein)

                implementation(projects.core.appearance)
                implementation(projects.core.di)
                implementation(projects.core.l10n)
                implementation(projects.core.preferences)
                implementation(projects.core.utils)
            }
        }
    }
}

sqldelight {
    databases {
        create("AppDatabase") {
            packageName.set("com.livefast.eattrash.raccoonforlemmy.core.persistence.entities")
            srcDirs.setFrom("src/commonMain/sqldelight")
        }
    }
    linkSqlite = true
}
