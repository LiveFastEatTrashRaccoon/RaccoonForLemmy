package com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository

import android.content.Context
import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.prepareGet
import io.ktor.http.contentLength
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.core.remaining
import io.ktor.utils.io.exhausted
import io.ktor.utils.io.readRemaining
import kotlinx.io.asSink
import java.io.File

class ModelDownloader(private val context: Context, private val httpClient: HttpClient) {

    private val modelDir = File(context.cacheDir, "model")

    companion object {
        private const val BUFFER_SIZE: Long = 1024 * 1024
    }

    suspend fun ensureModelDownloaded(
        url: String,
        modelName: String,
        onProgress: (progress: Float) -> Unit
    ): File? {

        if (!modelDir.exists()) modelDir.mkdirs()
        val outputFile = File(modelDir, modelName)

        if (outputFile.exists() && outputFile.length() > 0) {
            Log.i(
                "ModelDownloader",
                "Model $modelName already exists at ${outputFile.absolutePath}. Skipping download."
            )
            onProgress(1f)
            return outputFile
        }

        Log.i(
            "ModelDownloader",
            "Model $modelName not found or empty. Starting download from $url."
        )

        return try {
            onProgress(0f)
            download(url, modelName, onProgress)
            Log.i("ModelDownloader", "Download finished for $modelName. Waiting for completion...")

            outputFile
        } catch (e: Exception) {
            Log.e("ModelDownloader", "Error downloading model $modelName: ${e.message}")
            if (outputFile.exists()) {
                outputFile.delete()
            }
            null
        }
    }

    private suspend fun download(url: String, modelName: String, onProgress: (progress: Float) -> Unit) {
        val outputFile = File(modelDir, modelName)
        httpClient.prepareGet(urlString = url).execute { httpResponse ->
            val channel: ByteReadChannel = httpResponse.body()
            var count = 0L
            outputFile.outputStream().asSink().use { stream ->
                while (!channel.exhausted()) {
                    val chunk = channel.readRemaining(BUFFER_SIZE)
                    count += chunk.remaining
                    chunk.transferTo(sink = stream)
                    val progress = count.toFloat() / (httpResponse.contentLength()?.toFloat() ?: 0f)
                    onProgress(progress)
                }
            }
        }
    }
}
