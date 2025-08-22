plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlinx.kover)
    id("com.livefast.eattrash.spotless")
}

android {
    namespace = "com.livefast.eattrash.raccoonforlemmy.android"
    compileSdk =
        libs.versions.android.compileSdk
            .get()
            .toInt()
    defaultConfig {
        applicationId = "com.livefast.eattrash.raccoonforlemmy.android"
        minSdk =
            libs.versions.android.minSdk
                .get()
                .toInt()
        targetSdk =
            libs.versions.android.targetSdk
                .get()
                .toInt()
        versionCode = 153
        versionName = "1.15.0-beta02"
    }
    base.archivesName = "RaccoonForLemmy"
    buildFeatures {
        compose = true
        buildConfig = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    signingConfigs {
        create("release") {
            storeFile = File(projectDir, "keystore.jks")
            storePassword = System.getenv("KEYSTORE_PASSWORD")
            keyAlias = System.getenv("KEYSTORE_ALIAS")
            keyPassword = System.getenv("KEY_PASSWORD")
        }
    }
    buildTypes {
        getByName("debug") {
            resValue("string", "app_name", "Raccoon (dev)")
            applicationIdSuffix = ".dev"
        }
        getByName("release") {
            resValue("string", "app_name", "Raccoon")
            isMinifyEnabled = true
            setProguardFiles(
                listOf(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro",
                ),
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    splits {
        abi {
            isEnable = true
            reset()
            include("arm64-v8a", "x86_64")
            isUniversalApk = true
        }
    }
    dependenciesInfo {
        includeInApk = false
    }
}

dependencies {
    implementation(compose.runtime)
    implementation(compose.foundation)
    implementation(compose.material3)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.splashscreen)
    implementation(libs.kodein)

    implementation(projects.shared)
    implementation(projects.core.appearance)
    implementation(projects.core.di)
    implementation(projects.core.utils)
    implementation(projects.core.navigation)
    implementation(projects.core.persistence)
    implementation(projects.core.resources)

    kover(projects.shared)
    kover(projects.core.appearance)
    kover(projects.core.navigation)
    kover(projects.core.notifications)
    kover(projects.core.persistence)
    kover(projects.core.preferences)
    kover(projects.core.utils)
    kover(projects.domain.identity)
    kover(projects.domain.inbox)
    kover(projects.domain.lemmy.repository)
    kover(projects.domain.lemmy.pagination)
    kover(projects.domain.lemmy.usecase)
    kover(projects.feature.inbox)
    kover(projects.feature.profile)
}

kover {
    reports {
        filters {
            excludes {
                androidGeneratedClasses()
                classes(
                    "*Entity",
                    "*Queries",
                )
                packages(
                    "*.resources",
                    "*.di",
                    "*.entities.*",
                    "*.core.persistence.entities",
                    "*.core.persistence.provider",
                    "*.core.utils.appicon",
                    "*.core.utils.compose",
                    "*.core.utils.datetime",
                    "*.core.utils.debug",
                    "*.core.utils.fs",
                    "*.core.utils.gallery",
                    "*.core.utils.imageload",
                    "*.core.utils.keepscreenon",
                    "*.core.utils.network",
                    "*.core.utils.share",
                    "*.core.utils.texttoolbar",
                    "*.core.utils.url",
                    "*.core.utils.vibrate",
                    "*.domain.inbox.notification",
                )
            }
        }
    }
}
