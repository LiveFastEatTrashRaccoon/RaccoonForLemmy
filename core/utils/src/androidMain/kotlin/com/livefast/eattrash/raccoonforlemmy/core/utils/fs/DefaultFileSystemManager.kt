package com.livefast.eattrash.raccoonforlemmy.core.utils.fs

import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import okio.FileSystem
import okio.Path
import org.koin.core.annotation.Single
import org.koin.java.KoinJavaComponent
import java.io.InputStreamReader
import java.io.OutputStreamWriter

actual fun getFileSystemManager(): FileSystemManager {
    val res by KoinJavaComponent.inject<FileSystemManager>(FileSystemManager::class.java)
    return res
}

@Single
internal actual class DefaultFileSystemManager(
    private val context: Context,
) : FileSystemManager {
    actual override val isSupported = true

    @Composable
    actual override fun readFromFile(
        mimeTypes: Array<String>,
        callback: (String?) -> Unit,
    ) {
        val launcher =
            rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
                if (uri != null) {
                    val stream = context.contentResolver.openInputStream(uri)
                    InputStreamReader(stream).use { reader ->
                        val content = reader.readText()
                        callback(content)
                    }
                }
            }
        SideEffect {
            launcher.launch(mimeTypes)
        }
    }

    @Composable
    actual override fun writeToFile(
        mimeType: String,
        name: String,
        data: String,
        callback: (Boolean) -> Unit,
    ) {
        val launcher =
            rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument(mimeType)) { uri ->
                if (uri != null) {
                    val stream = context.contentResolver.openOutputStream(uri)
                    OutputStreamWriter(stream).use { writer ->
                        writer.write(data)
                        callback(true)
                    }
                }
            }
        SideEffect {
            launcher.launch(name)
        }
    }

    actual override fun getTempDir(): Path = FileSystem.SYSTEM_TEMPORARY_DIRECTORY
}
