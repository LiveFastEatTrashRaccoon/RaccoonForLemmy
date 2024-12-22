package com.livefast.eattrash.raccoonforlemmy.core.utils.fs

import androidx.compose.runtime.Composable
import okio.FileSystem
import okio.Path

internal class DefaultFileSystemManager : FileSystemManager {
    override val isSupported = false

    @Composable
    override fun readFromFile(
        mimeTypes: Array<String>,
        callback: (String?) -> Unit,
    ) {
        callback(null)
    }

    @Composable
    override fun writeToFile(
        mimeType: String,
        name: String,
        data: String,
        callback: (Boolean) -> Unit,
    ) {
        callback(false)
    }

    override fun getTempDir(): Path = FileSystem.SYSTEM_TEMPORARY_DIRECTORY
}
