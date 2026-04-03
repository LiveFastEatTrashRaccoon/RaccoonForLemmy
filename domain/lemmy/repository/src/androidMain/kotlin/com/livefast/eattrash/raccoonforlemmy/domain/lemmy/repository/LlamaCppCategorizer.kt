package com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import net.amazingapps.llama.android.core.InferenceEngine

internal class LlamaCppCategorizer(
    private val modelDownloader: ModelDownloader,
    private val engine: InferenceEngine,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default),
) : BasePostCategorizer() {

//    private val modelUrl =
//        "https://huggingface.co/HuggingFaceTB/SmolLM2-360M-Instruct-GGUF/resolve/main/smollm2-360m-instruct-q8_0.gguf"
//    private val modelFilename = "smollm2-360m-q8_0.gguf"

//    private val modelUrl = "https://huggingface.co/lmstudio-community/Qwen2-VL-2B-Instruct-GGUF/resolve/main/Qwen2-VL-2B-Instruct-Q3_K_L.gguf"
//    private val modelFilename = "Qwen2-VL-2B-Instruct-Q3_K_L.gguf"

//    private val modelUrl = "https://huggingface.co/polyverse/Llama-3.2-1B-Q4_0-GGUF/resolve/main/llama-3.2-1b-q4_0.gguf"
//
//    private val modelFilename = "llama-3.2-1b-q4_0.gguf"

    // https://huggingface.co/bartowski/Llama-3.2-1B-Instruct-GGUF
    // Q4_K_M	0.81GB	false	Good quality, default size for must use cases, recommended.
    // Q4_0_8_8	0.77GB	false	Optimized for ARM inference. Requires 'sve' support (see link below).
    // Q6_K: 1.02GB: Very high quality, near perfect, recommended.
    private val modelUrl =    "https://huggingface.co/bartowski/Llama-3.2-1B-Instruct-GGUF/resolve/main/Llama-3.2-1B-Instruct-Q6_K.gguf"

    private val modelFilename = "Llama-3.2-1B-Instruct-Q6_K.gguf"

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
            val startTime = System.currentTimeMillis()
            val result = categorizeInternal(postHeadline)
            val duration = System.currentTimeMillis() - startTime
            Log.i(
                "PostCategorizer",
                """
                        Categorization took %4d ms : [%17s] %s
                       """.trimIndent()
                    .format(
                        duration, result, postHeadline
                    ),
            )
//                "Categorization took ${duration}ms for: $postHeadline [$result]")

            result
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
                You are a helpful assistant that assigns topics to memes and social media posts. These topics assignment task is important for sorting

                 These are some of the valid topics:
                   -Politics
                   - Technology
                   - Sports
                   - Entertainment
                   - Food
                   - Neurodivergent
                   - Introspection
                   - Other.

                   You're only allowed 1 word; do not repeat the headline.

                Example:
                  Headline: < I bet this is how it actually feels > ; Topic: [ Introspection ]

                  Headline: < Normies > ; Topic:  [ Neurodivergent ]

                  Headline: < Transgender Woman Defies Kansa's Extreme Bathroom Ban in Acto of Civil Disobedience At State Capital > ; Topic:  [ Politics ]

                    Do not revise or repeat the headline, only assign a single topic to it.
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
//            CoroutineScope( Dispatchers.Default).launch {
               return hello(postHeadline)
//            }
//            return null
        }

    }

    suspend fun hello (postHeadline: String) :String {
        try {
            var response = ""

            Log.i("PostCategorizer", "input <$postHeadline> processing...")

            withContext(Dispatchers.Default) {
                engine.sendUserPrompt("Headline: < $postHeadline > ; ")
//                engine.sendUserPrompt("Headline: < $postHeadline > ; Topic: [ ")
                    .takeWhile { response.split(" ").size <= 10 }
//                    .takeWhile { !response.contains(" ") }
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
