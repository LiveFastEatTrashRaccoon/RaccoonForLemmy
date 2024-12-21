package extensions

import com.android.build.gradle.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import utils.PACKAGE_PREFIX
import utils.libs
import utils.version

internal fun Project.configureKotlinAndroid(extension: LibraryExtension) =
    extension.apply {
        val moduleName = path.split(":").drop(1).joinToString(".")
        namespace = if (moduleName.isNotEmpty()) "$PACKAGE_PREFIX.$moduleName" else PACKAGE_PREFIX

        compileSdk = libs.findVersion("android-compileSdk").version
        defaultConfig {
            minSdk = libs.findVersion("android-minSdk").version
        }
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11
        }
        packaging {
            resources {
                excludes += "/META-INF/{AL2.0,LGPL2.1}"
            }
        }
    }
