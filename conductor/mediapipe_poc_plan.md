# Plan: MediaPipe Post Categorizer PoC

Implement a MediaPipe-based `PostCategorizer` and refactor the existing implementation to support shared performance measurement.

## Proposed Changes

### 1. Refactor Architecture (Common)
- **Create `BasePostCategorizer.kt`** in `domain:lemmy:repository` (`commonMain`):
    - Abstract class implementing `PostCategorizer`.
    - `categorize` method:
        - Starts a timer using platform-agnostic timing (or `System.currentTimeMillis()` in `androidMain`).
        - Calls `performCategorization(postHeadline)`.
        - Calculates duration.
        - Logs: "Categorization took [X]ms for: [Headline]".
        - Returns the result.
    - `protected abstract suspend fun performCategorization(postHeadline: String): String?`

### 2. Rename Existing Implementation (Android)
- **Rename `DefaultPostCategorizer.kt`** to `LlamaCppCategorizer.kt`.
- Update class name to `LlamaCppCategorizer`.
- Inherit from `BasePostCategorizer`.
- Move classification logic from `categorize` to `performCategorization`.

### 3. Dependency Management
- **`gradle/libs.versions.toml`**:
    - Add `mediapipe-tasks-text = "com.google.mediapipe:tasks-text:0.10.21"`.
- **`domain/lemmy/repository/build.gradle.kts`**:
    - Add `implementation(libs.mediapipe.tasks.text)` to `androidMain`.

### 4. Implement `MediaPipePostCategorizer` (Android)
- **Create `MediaPipePostCategorizer.kt`** in `androidMain`:
    - Inherits from `BasePostCategorizer`.
    - Uses MediaPipe `TextClassifier`.
    - Loads model from `assets/classifier.tflite`.
    - Implements `performCategorization` by calling `textClassifier.classify()`.
    - **Note:** For the PoC, the model should be manually placed in `src/androidMain/assets/`.

### 5. Unit Testing
- **Create `MediaPipePostCategorizerTest.kt`**:
    - Location: `domain/lemmy/repository/src/androidMain/test/kotlin/.../`.
    - Objective: Verify classification of 1 input headline against 10 random topics.
    - **Milestone:** Pass this test before proceeding to any app-level changes.

### 6. Dependency Injection Update
- **`LemmyRepositoryPlatformModule.kt`**:
    - Update binding for `PostCategorizer` to use `MediaPipePostCategorizer` instead of `LlamaCppCategorizer`.

## Verification Plan
1. **Unit Test:** Run `./gradlew :domain:lemmy:repository:testDebugUnitTest` (or equivalent) and ensure `MediaPipePostCategorizerTest` passes.
2. **Refactor Check:** Verify `LlamaCppCategorizer` compiles.
3. **Full Build:** After the unit test passes, build the entire application using `./gradlew assembleDebug` to ensure integration integrity.
