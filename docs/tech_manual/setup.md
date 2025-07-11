## Setting up the development environment

This is a Kotlin Multiplatform (KMP) project that uses the Gradle build tool. The recommended
development environment is Android Studio with the Kotlin Multiplatform plugin installed.

The project is using Gradle with the Android Gradle Plugin (AGP), so you should a compatible version
of Android Studio (have a look
[here](https://developer.android.com/build/releases/gradle-plugin?hl=en#android_gradle_plugin_and_android_studio_compatibility)
for a compatibility matrix between versions of Gradle, AGP and Android Studio) and
[here](https://kotlinlang.org/docs/multiplatform-compatibility-guide.html)
for the compatibility between the Kotlin Multiplatform plugin, Kotlin, Gradle and AGP.
Alternatively, you can try and use IntelliJ IDEA or Fleet but some extra steps may be needed to
ensure everything fits and runs together.

In order for Gradle to build, you will need to have a JDK installed on your local development
machine, if you are using stock Android Studio it ships with the default JetBrains runtime, you
could have a look in the Settings dialog under the section "Build, Execution, Deployment > Build
Tools > Gradle"in the "Gradle JDK" location drop-down menu.

If you want to use your custom JDK (e.g. under Linux you want to try OpenJDK instead), please make
sure that it has a suitable version, according
to [this page](https://docs.gradle.org/current/userguide/compatibility.html).

Finally, since building this project requires a quite lot of RAM due to its multi-module structure
and to the fact that it is quite a complex project, please make sure that the `gradle.properties`
file in the root folder contains proper memory settings for the JVM and the Kotlin compile daemon:

```properties
org.gradle.jvmargs=-Xmx4096M -Dfile.encoding=UTF-8 -Dkotlin.daemon.jvm.options\="-Xmx4096M"
```

The first thing that Android Studio does upon first opening the project is a Gradle sync, this may
take some time since at the beginning it has to download all the dependencies and build the cache.

A Gradle sync is required every time:

- the Gradle wrapper is updated or some Gradle plugins are updated to a newer version;
- a new external dependency is added or an existing library is updated to a newer version;
- a new Gradle module is added to the project or whenever you edit the `settings.gradle.kts` file or
  any `build.gradle.kts` file in any module of the project hierarchy.

In case it does not suggest it to you automatically, you will fine the "Sync Project with Gradle
Files" button in the top left corner of the toolbar, right before the "Search Everywhere" button.
The operation can be, depending on your hardware and connection speed, quite time consuming so be
patient.
