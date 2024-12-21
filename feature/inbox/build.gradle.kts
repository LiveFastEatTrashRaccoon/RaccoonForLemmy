plugins {
    id("com.livefast.eattrash.kotlinMultiplatform")
    id("com.livefast.eattrash.composeMultiplatform")
    id("com.livefast.eattrash.androidTest")
    alias(libs.plugins.kotlinx.kover)
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kodein)
                implementation(libs.voyager.navigator)
                implementation(libs.voyager.screenmodel)
                implementation(libs.voyager.kodein)
                implementation(libs.voyager.tab)
                implementation(libs.voyager.bottomsheet)

                implementation(projects.core.appearance)
                implementation(projects.core.architecture)
                implementation(projects.core.commonui.components)
                implementation(projects.core.commonui.detailopener.api)
                implementation(projects.core.commonui.lemmyui)
                implementation(projects.core.commonui.modals)
                implementation(projects.core.l10n)
                implementation(projects.core.navigation)
                implementation(projects.core.notifications)
                implementation(projects.core.persistence)
                implementation(projects.core.preferences)
                implementation(projects.core.utils)

                implementation(projects.unit.zoomableimage)
                implementation(projects.unit.replies)
                implementation(projects.unit.mentions)
                implementation(projects.unit.messages)

                implementation(projects.domain.lemmy.data)
                implementation(projects.domain.lemmy.repository)
                implementation(projects.domain.identity)
                implementation(projects.domain.inbox)
            }
        }
        val androidUnitTest by getting {
            dependencies {
                implementation(projects.core.architecture.testutils)
            }
        }
    }
}
