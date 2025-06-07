plugins {
    id("com.livefast.eattrash.kotlinMultiplatform")
    id("com.livefast.eattrash.composeMultiplatform")
    id("com.livefast.eattrash.androidTest")
    id("com.livefast.eattrash.spotless")
    alias(libs.plugins.kotlinx.kover)
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kodein)
                implementation(libs.voyager.navigator)

                implementation(projects.core.navigation)
                implementation(projects.core.commonui.detailopener.api)

                implementation(projects.domain.identity)
                implementation(projects.domain.lemmy.data)
                implementation(projects.domain.lemmy.pagination)
                implementation(projects.domain.lemmy.repository)

                implementation(projects.unit.postdetail)
                implementation(projects.unit.communitydetail)
                implementation(projects.unit.userdetail)
                implementation(projects.unit.createpost)
                implementation(projects.unit.createcomment)
                implementation(projects.unit.web)
            }
        }
    }
}
