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
                implementation(libs.kodein)
                implementation(libs.voyager.core)

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
                implementation(projects.core.resources)
                implementation(projects.core.utils)

                implementation(projects.domain.lemmy.data)
                implementation(projects.domain.lemmy.repository)
                implementation(projects.domain.lemmy.usecase)
                implementation(projects.domain.identity)

                implementation(projects.unit.web)
                implementation(projects.unit.about)
                implementation(projects.unit.accountsettings)
                implementation(projects.unit.manageban)
                implementation(projects.unit.configureswipeactions)
                implementation(projects.unit.configurenavbar)
                implementation(projects.unit.configurecontentview)
                implementation(projects.unit.filteredcontents)
                implementation(projects.unit.medialist)
                implementation(projects.unit.usertags)
            }
        }
    }
}
