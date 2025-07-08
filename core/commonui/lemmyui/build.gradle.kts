plugins {
    id("com.livefast.eattrash.kotlinMultiplatform")
    id("com.livefast.eattrash.composeMultiplatform")
    id("com.livefast.eattrash.androidTest")
    id("com.livefast.eattrash.spotless")
}

kotlin {
    sourceSets {
        val androidMain by getting {
            dependencies {
                implementation(libs.coil.compose)
            }
        }
        val commonMain by getting {
            dependencies {
                implementation(libs.calf)
                implementation(libs.kodein)

                implementation(projects.core.appearance)
                implementation(projects.core.di)
                implementation(projects.core.commonui.components)
                implementation(projects.core.markdown)
                implementation(projects.core.l10n)
                implementation(projects.core.navigation)
                implementation(projects.core.persistence)
                implementation(projects.core.utils)

                implementation(projects.domain.lemmy.data)
            }
        }
    }
}
