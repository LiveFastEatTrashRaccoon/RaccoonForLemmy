plugins {
    id("com.livefast.eattrash.kotlinMultiplatform")
    id("com.livefast.eattrash.composeMultiplatform")
    id("com.livefast.eattrash.androidTest")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kodein)
                implementation(libs.voyager.navigator)
                implementation(libs.voyager.screenmodel)
                implementation(libs.voyager.kodein)

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
                implementation(projects.core.utils)

                implementation(projects.domain.identity)
                implementation(projects.domain.lemmy.data)
                implementation(projects.domain.lemmy.pagination)
                implementation(projects.domain.lemmy.repository)
                implementation(projects.domain.lemmy.usecase)

                implementation(projects.unit.ban)
                implementation(projects.unit.communityinfo)
                implementation(projects.unit.createcomment)
                implementation(projects.unit.createpost)
                implementation(projects.unit.editcommunity)
                implementation(projects.unit.explore)
                implementation(projects.unit.instanceinfo)
                implementation(projects.unit.moderatewithreason)
                implementation(projects.unit.modlog)
                implementation(projects.unit.rawcontent)
                implementation(projects.unit.reportlist)
                implementation(projects.unit.web)
                implementation(projects.unit.zoomableimage)
            }
        }
    }
}
