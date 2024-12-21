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
                implementation(libs.ktor.cio)

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

                implementation(projects.domain.identity)
                implementation(projects.domain.lemmy.data)
                implementation(projects.domain.lemmy.repository)

                implementation(projects.unit.createcomment)
                implementation(projects.unit.createpost)
                implementation(projects.unit.drafts)
                implementation(projects.unit.editcommunity)
                implementation(projects.unit.filteredcontents)
                implementation(projects.unit.login)
                implementation(projects.unit.manageaccounts)
                implementation(projects.unit.managesubscriptions)
                implementation(projects.unit.modlog)
                implementation(projects.unit.myaccount)
                implementation(projects.unit.reportlist)
                implementation(projects.unit.web)
                implementation(projects.unit.zoomableimage)
            }
        }
        val androidUnitTest by getting {
            dependencies {
                implementation(projects.core.architecture.testutils)
            }
        }
    }
}
