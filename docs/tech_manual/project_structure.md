## Project structure

This is a Gradle project, it is setup to download a Gradle distribution and resolve dependencies
according to the definitions contained in the `gradle/libs.versions.toml` file.

Also, please note that in the `settings.gradle.kts` file we are using the option:

```
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
```

to reference Gradle subprojects in each `build.gradle.kts` with a type safe notation
e.g. `implementation(projects.core.utils)`.
