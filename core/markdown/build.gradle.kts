plugins {
    id("com.livefast.eattrash.kotlinMultiplatform")
    id("com.livefast.eattrash.composeMultiplatform")
}

kotlin {

    sourceSets {
        val androidMain by getting {
            dependencies {
                implementation(libs.multiplatform.markdown.renderer.coil3)
            }
        }
        val commonMain by getting {
            dependencies {
                api(libs.multiplatform.markdown.renderer)
                api(libs.multiplatform.markdown.renderer.m3)
                api(libs.multiplatform.markdown.renderer.coil3)

                implementation(projects.core.l10n)
                implementation(projects.core.commonui.components)
                implementation(projects.core.utils)
            }
        }
    }
}
