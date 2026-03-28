package com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository

import android.util.Log
import com.arm.aichat.InferenceEngine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

internal class LlamaCppCategorizer(
    private val modelDownloader: ModelDownloader,
    private val engine: InferenceEngine,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default),
) : BasePostCategorizer() {

    private val modelUrl =
        "https://huggingface.co/HuggingFaceTB/SmolLM2-360M-Instruct-GGUF/resolve/main/smollm2-360m-instruct-q8_0.gguf"
    private val modelFilename = "smollm2-360m-q8_0.gguf"

    private var isInitialized = false
    private var isInitializing = false
    private val categoriesMap = mutableMapOf<String, String>()

    init {
        initialize()
    }

    private fun initialize() {
        if (isInitialized || isInitializing) return
        isInitializing = true

        scope.launch {
            try {
                val modelFile = modelDownloader.ensureModelDownloaded(modelUrl, modelFilename) { progress ->
                    Log.d("PostCategorizer", "Download progress: $progress")
                }

                if (modelFile != null) {
                    engine.state.first {
                        it is InferenceEngine.State.Initialized || it is InferenceEngine.State.ModelReady
                    }
                    engine.loadModel(modelFile.path)
                    isInitialized = true
                }
            } catch (e: Exception) {
                Log.e("PostCategorizer", "Initialization failed", e)
            } finally {
                isInitializing = false
            }
        }
    }

    private val lock = Mutex(false)
    override suspend fun performCategorization(postHeadline: String): String? {

        return lock.withLock {
            categorizeInternal(postHeadline)
        }
    }

    private suspend fun categorizeInternal(postHeadline: String): String? {
        if (!isInitialized) {
            initialize()
            val prompt1 = """
                You are a helpful assistant that categorizes news headlines into one of the following categories: Politics, Technology, Sports, Entertainment, Health, Science, Business, World, and Other.
                If the headline does not fit into any of the first 8 categories, categorize it freely.
                You are only allowed to reply using a single word.
            """.trimIndent()

            val prompt2 = """
                You are a helpful assistant that decides if the user is talking about one of these topics: Politics, Technology, Sports, Entertainment, Food or Other. You're only allowed 1 word; do not repeat the headline.
            """.trimIndent()


            engine.setSystemPrompt(prompt2)
            return "init"
        }

        if (categoriesMap.containsKey(postHeadline)) {
            Log.d("PostCategorizer", "cache hit <$postHeadline> ")
            return categoriesMap[postHeadline]
        } else {
            Log.d(
                "PostCategorizer",
                "cache miss (size = ${categoriesMap.size}) <$postHeadline> / keys " + categoriesMap.keys.map { it },
            )
        }

        try {
            var response = ""

            Log.i("PostCategorizer", "input <$postHeadline> processing...")

            withContext(Dispatchers.Default) {
                engine.sendUserPrompt("Headline: < $postHeadline > ; Category: ")
                    .takeWhile { !response.contains(" ") }
                    .collect { token ->
                        response += token
                        Log.v("PostCategorizer", "input <$postHeadline> processing... $response")
                    }
            }

            Log.e("PostCategorizer", "input <$postHeadline> -> category <$response>")
            categoriesMap[postHeadline] = response.trim()
        } catch (e: Exception) {
            Log.e("PostCategorizer", "Categorization failed", e)
        }

        return categoriesMap[postHeadline] ?: "no value"
    }

    override suspend fun detectLanguage(postHeadline: String): String? {
        TODO("Not yet implemented")
    }
}
