plugins {
    id("com.livefast.eattrash.kotlinMultiplatform")
    id("com.livefast.eattrash.composeMultiplatform")
    id("com.livefast.eattrash.spotless")
}

kotlin {

    sourceSets {
        androidMain {
            dependencies {
                implementation(libs.multiplatform.markdown.renderer.coil3)
            }
        }
        commonMain {
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
