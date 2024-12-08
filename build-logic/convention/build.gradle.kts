plugins {
    `kotlin-dsl`
}

group = "com.livefaast.eattrash.raccoonforlemmy.buildlogic"

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    compileOnly(libs.gradle)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("kotlinMultiplatform") {
            id = "com.livefast.eattrash.kotlinMultiplatform"
            implementationClass = "plugins.KotlinMultiplatformPlugin"
        }
        register("koinWithKsp") {
            id = "com.livefast.eattrash.koinWithKsp"
            implementationClass = "plugins.KoinWithKspPlugin"
        }
        register("composeMultiplatform") {
            id = "com.livefast.eattrash.composeMultiplatform"
            implementationClass = "plugins.ComposeMultiplatformPlugin"
        }
        register("androidTestPlugin") {
            id = "com.livefast.eattrash.androidTest"
            implementationClass = "plugins.AndroidTestPlugin"
        }
    }
}
