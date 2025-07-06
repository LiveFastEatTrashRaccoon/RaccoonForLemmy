plugins {
    id("com.livefast.eattrash.kotlinMultiplatform")
    id("com.livefast.eattrash.composeMultiplatform")
    id("com.livefast.eattrash.spotless")
    alias(libs.plugins.kotlinx.kover)
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)

                implementation(libs.compose.multiplatform.media.player)
                implementation(libs.kodein.compose)
                implementation(libs.voyager.navigator)
                implementation(libs.voyager.core)
                implementation(libs.androidx.navigation.compose)

                implementation(projects.core.api)
                implementation(projects.core.appearance)
                implementation(projects.core.architecture)
                implementation(projects.core.di)
                implementation(projects.core.commonui.components)
                implementation(projects.core.commonui.detailopener.api)
                implementation(projects.core.commonui.detailopener.impl)
                implementation(projects.core.commonui.lemmyui)
                implementation(projects.core.l10n)
                implementation(projects.core.navigation)
                implementation(projects.core.notifications)
                implementation(projects.core.persistence)
                implementation(projects.core.preferences)
                implementation(projects.core.resources)
                implementation(projects.core.utils)

                implementation(projects.domain.identity)
                implementation(projects.domain.inbox)
                implementation(projects.domain.lemmy.data)
                implementation(projects.domain.lemmy.pagination)
                implementation(projects.domain.lemmy.repository)
                implementation(projects.domain.lemmy.usecase)

                implementation(projects.unit.about)
                implementation(projects.unit.accountsettings)
                implementation(projects.unit.acknowledgements)
                implementation(projects.unit.ban)
                implementation(projects.unit.chat)
                implementation(projects.unit.communitydetail)
                implementation(projects.unit.communityinfo)
                implementation(projects.unit.configurecontentview)
                implementation(projects.unit.configurenavbar)
                implementation(projects.unit.configureswipeactions)
                implementation(projects.unit.createcomment)
                implementation(projects.unit.createpost)
                implementation(projects.unit.drafts)
                implementation(projects.unit.drawer)
                implementation(projects.unit.editcommunity)
                implementation(projects.unit.explore)
                implementation(projects.unit.filteredcontents)
                implementation(projects.unit.instanceinfo)
                implementation(projects.unit.licences)
                implementation(projects.unit.login)
                implementation(projects.unit.manageaccounts)
                implementation(projects.unit.manageban)
                implementation(projects.unit.managesubscriptions)
                implementation(projects.unit.medialist)
                implementation(projects.unit.mentions)
                implementation(projects.unit.messages)
                implementation(projects.unit.moderatewithreason)
                implementation(projects.unit.modlog)
                implementation(projects.unit.multicommunity)
                implementation(projects.unit.myaccount)
                implementation(projects.unit.postdetail)
                implementation(projects.unit.postlist)
                implementation(projects.unit.rawcontent)
                implementation(projects.unit.replies)
                implementation(projects.unit.reportlist)
                implementation(projects.unit.selectcommunity)
                implementation(projects.unit.selectinstance)
                implementation(projects.unit.userdetail)
                implementation(projects.unit.userinfo)
                implementation(projects.unit.usertags)
                implementation(projects.unit.web)
                implementation(projects.unit.zoomableimage)

                api(projects.feature.home)
                api(projects.feature.inbox)
                api(projects.feature.search)
                api(projects.feature.profile)
                api(projects.feature.settings)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidUnitTest by getting
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by getting
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by getting
    }
}

customKotlinMultiplatformExtension {
    baseName = "shared"
}

spotless {
    kotlin {
        target("**/App.kt")
        suppressLintsFor {
            step = "ktlint"
            shortCode = "compose:modifier-missing-check"
        }
        target("**/main.kt", "**/main.ios.kt")
        suppressLintsFor {
            step = "ktlint"
            shortCode = "standard:filename"
        }
    }
}
