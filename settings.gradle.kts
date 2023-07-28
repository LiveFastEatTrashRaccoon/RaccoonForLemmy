pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://plugins.gradle.org/m2/")
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

enableFeaturePreview("VERSION_CATALOGS")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "Raccoon_for_Lemmy"
include(":androidApp")
include(":shared")
include(":feature-home")
include(":feature-inbox")
include(":feature-search")
include(":feature-profile")
include(":feature-settings")
include(":feature-example")
include(":core-utils")
include(":core-appearance")
include(":core-preferences")
include(":resources")
include(":core-architecture")
include(":core-api")
include(":domain-post")
include(":domain-post:repository")
include(":domain-post:data")
include(":domain-identity")
include(":core-md")
