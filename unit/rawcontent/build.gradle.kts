plugins {
    id("com.livefast.eattrash.kotlinMultiplatform")
    id("com.livefast.eattrash.koinWithKsp")
    id("com.livefast.eattrash.composeMultiplatform")
    id("com.livefast.eattrash.androidTest")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.voyager.navigator)
                implementation(libs.voyager.bottomsheet)

                implementation(projects.core.appearance)
                implementation(projects.core.l10n)
                implementation(projects.core.utils)
            }
        }
    }
}
