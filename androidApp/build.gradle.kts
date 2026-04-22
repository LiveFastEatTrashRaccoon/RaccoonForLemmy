plugins {
    alias(libs.plugins.android.application)
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
        versionCode = (project.findProperty("buildNumber") as? String)?.toInt() ?: 1
        versionName = project.findProperty("versionName") as? String ?: "1.0.0"
    }
    base.archivesName = "RaccoonForLemmy"
    buildFeatures {
        compose = true
        buildConfig = true
        resValues = true
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

    debugImplementation(libs.compose.ui.tooling.preview)
    debugImplementation(libs.compose.ui.test.manifest)
}
