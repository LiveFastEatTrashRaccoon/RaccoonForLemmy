plugins {
    id("com.livefast.eattrash.kotlinMultiplatform")
    id("com.livefast.eattrash.composeMultiplatform")
    id("com.livefast.eattrash.androidTest")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.calf)
                implementation(libs.koin.core)
                implementation(libs.voyager.navigator)
                implementation(libs.voyager.screenmodel)
                implementation(libs.voyager.koin)

                implementation(projects.core.utils)
                implementation(projects.core.appearance)
                implementation(projects.core.commonui.components)
                implementation(projects.core.l10n)
                implementation(projects.core.navigation)
            }
        }
    }
}
