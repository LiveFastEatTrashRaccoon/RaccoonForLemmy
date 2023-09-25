plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose)
}

android {
    namespace = "com.github.diegoberaldin.raccoonforlemmy.android"
    compileSdk = libs.versions.android.targetSdk.get().toInt()
    defaultConfig {
        applicationId = "com.github.diegoberaldin.raccoonforlemmy.android"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 16
        versionName = "1.0.0-beta06"

        buildConfigField(
            "String",
            "CRASH_REPORT_EMAIL",
            "\"diego.beraldin+raccon4lemmy@gmail.com\""
        )
        buildConfigField("String", "CRASH_REPORT_SUBJECT", "\"Crash report\"")
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(compose.runtime)
    implementation(compose.foundation)
    implementation(compose.material3)
    implementation(libs.androidx.activity.compose)
    implementation(libs.koin.core)
    implementation(libs.koin.android)

    implementation(libs.acra.mail)
    implementation(libs.acra.notification)

    implementation(projects.shared)
    implementation(projects.coreUtils)
}
