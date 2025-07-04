plugins {
    id("com.livefast.eattrash.kotlinMultiplatform")
    id("com.livefast.eattrash.composeMultiplatform")
    id("com.livefast.eattrash.androidTest")
    id("com.livefast.eattrash.spotless")
    alias(libs.plugins.kotlinx.kover)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kodein)
            implementation(libs.stately.common)
            implementation(libs.voyager.navigator)
            implementation(libs.voyager.tab)

            implementation(projects.core.di)
            implementation(projects.core.l10n)
            implementation(projects.core.persistence)
            implementation(projects.core.preferences)
            implementation(projects.domain.lemmy.data)
        }
    }
}
