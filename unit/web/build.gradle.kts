plugins {
    id("com.livefast.eattrash.kotlinMultiplatform")
    id("com.livefast.eattrash.composeMultiplatform")
    id("com.livefast.eattrash.androidTest")
    id("com.livefast.eattrash.spotless")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.calf)
                implementation(libs.kodein)
                implementation(libs.voyager.core)

                implementation(projects.core.utils)
                implementation(projects.core.appearance)
                implementation(projects.core.commonui.components)
                implementation(projects.core.l10n)
                implementation(projects.core.navigation)
            }
        }
    }
}
