plugins {
    id("com.livefast.eattrash.kotlinMultiplatform")
    id("com.livefast.eattrash.composeMultiplatform")
    id("com.livefast.eattrash.androidTest")
    id("com.livefast.eattrash.spotless")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kodein)

                implementation(projects.core.di)
            }
        }
    }
}

spotless {
    kotlin {
        target("**/ProvideStrings.kt")
        suppressLintsFor {
            step = "ktlint"
            shortCode = "compose:compositionlocal-allowlist"
        }
    }
}
