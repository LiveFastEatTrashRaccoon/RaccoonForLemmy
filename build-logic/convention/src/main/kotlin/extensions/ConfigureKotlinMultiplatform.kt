package extensions

import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import org.gradle.api.Project
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.create
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import utils.PACKAGE_PREFIX
import utils.libs
import utils.version

interface CustomKotlinMultiplatformExtension {
    val baseName: Property<String>
    val iOSCustomLinkerOptions: ListProperty<String>
}

internal fun Project.configureKotlinMultiplatform(extension: KotlinMultiplatformExtension) =
    extension.apply {
        applyDefaultHierarchyTemplate()

        targets.findByName("android")
        val moduleName = path.split(":").drop(1).joinToString(".")
        val customExtension =
            project.extensions
                .create<CustomKotlinMultiplatformExtension>("customKotlinMultiplatformExtension")
        listOf(
            iosX64(),
            iosArm64(),
            iosSimulatorArm64(),
        ).forEach {
            it.binaries.framework {
                baseName = customExtension.baseName.orNull ?: moduleName
                isStatic = true
                val customOptions = customExtension.iOSCustomLinkerOptions.orNull ?: emptyList()
                for (option in customOptions) {
                    linkerOpts.add(option)
                }
            }
        }

        targets.withType(KotlinMultiplatformAndroidLibraryTarget::class.java).configureEach {
            val moduleName = path.split(":").drop(1).joinToString(".")
            namespace = if (moduleName.isNotEmpty()) "$PACKAGE_PREFIX.$moduleName" else PACKAGE_PREFIX

            compileSdk = libs.findVersion("android-compileSdk").version
            minSdk = libs.findVersion("android-minSdk").version

            packaging {
                resources {
                    excludes += "/META-INF/{AL2.0,LGPL2.1}"
                }
            }

//            withDeviceTest {
//                this.instrumentationRunner = "invalid"
//            }

            experimentalProperties["android.experimental.kmp.enableAndroidResources"] = true
        }
    }
