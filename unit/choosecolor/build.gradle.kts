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
                implementation(libs.compose.colorpicker)
                implementation(libs.voyager.navigator)

                implementation(projects.core.utils)
                implementation(projects.core.appearance)
                implementation(projects.core.commonui.components)
                implementation(projects.core.l10n)
                implementation(projects.core.navigation)
                implementation(projects.core.persistence)
                implementation(projects.core.notifications)
            }
        }
    }
}
