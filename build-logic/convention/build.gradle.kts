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
    compileOnly(libs.compose.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
    compileOnly(libs.spotless.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("composeMultiplatform") {
            id = "com.livefast.eattrash.composeMultiplatform"
            implementationClass = "plugins.ComposeMultiplatformPlugin"
        }

        register("kotlinMultiplatform") {
            id = "com.livefast.eattrash.kotlinMultiplatform"
            implementationClass = "plugins.KotlinMultiplatformPlugin"
        }

        register("serializationPlugin") {
            id = "com.livefast.eattrash.serialization"
            implementationClass = "plugins.SerializationPlugin"
        }

        register("androidTestPlugin") {
            id = "com.livefast.eattrash.androidTest"
            implementationClass = "plugins.AndroidTestPlugin"
        }

        register("spotlessPlugin") {
            id = "com.livefast.eattrash.spotless"
            implementationClass = "plugins.SpotlessPlugin"
        }
    }
}
