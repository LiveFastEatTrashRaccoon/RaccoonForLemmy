package com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository

import android.content.Context
import android.util.Log
import com.google.mediapipe.tasks.components.containers.Category
import com.google.mediapipe.tasks.text.languagedetector.LanguageDetector
import com.google.mediapipe.tasks.text.textclassifier.TextClassifier
import com.google.mediapipe.tasks.text.textclassifier.TextClassifier.TextClassifierOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * MediaPipe-based implementation of PostCategorizer.
 *
 * To use this, you need to place a MediaPipe text classification model in the assets folder.
 * For example, download the BERT classifier from:
 * https://storage.googleapis.com/mediapipe-models/text_classifier/bert_classifier/float32/1/bert_classifier.tflite
 * and save it as `src/androidMain/assets/classifier.tflite`.
 */
class MediaPipePostCategorizer(
    private val context: Context,
    private val modelPath: String = "classifier.tflite",
    private var textClassifier: TextClassifier? = null,
) : BasePostCategorizer() {

    private fun initializeClassifier() {
        if (textClassifier != null) return

        val options = TextClassifierOptions.builder()
            .setBaseOptions(
                com.google.mediapipe.tasks.core.BaseOptions.builder()
                    .setModelAssetPath(modelPath)
                    .build(),
            )
            .setCategoryAllowlist(
                listOf(
                    "Politics", "Technology", "Sports", "Entertainment", "Food",
                    "Health", "Science", "Business", "World", "Other",
                ),
            )
            .build()

        textClassifier = TextClassifier.createFromOptions(context, options)
    }

    override suspend fun performCategorization(postHeadline: String): String? = withContext(Dispatchers.Default) {
        try {
            initializeClassifier()
            val result = textClassifier?.classify(postHeadline)

            // Return the label of the category with the highest score
            result?.classificationResult()?.classifications()
                ?.also {
                    Log.i(
                        "CategorizationResult",
                        it.toList()
                            .joinToString(),
                    )
                }
                ?.firstOrNull()?.categories()
                ?.maxByOrNull { it.score() }
                ?.categoryName()
        } catch (e: Exception) {
            android.util.Log.e("MediaPipePostCategorizer", "Classification failed", e)
            null
        }
    }

    override suspend fun detectLanguage(postHeadline: String): String? {
        val languageDetector = LanguageDetector.createFromOptions(
            context,
            com.google.mediapipe.tasks.text.languagedetector.LanguageDetector.LanguageDetectorOptions.builder()
                .setBaseOptions(
                    com.google.mediapipe.tasks.core.BaseOptions.builder()
                        .setModelAssetPath("language_detector.tflite")
                        .build(),
                )
                .build(),
        )

        languageDetector.detect(postHeadline)
            .languagesAndScores()
        return super.categorize(postHeadline)
    }
}
