package com.livefast.eattrash.raccoonforlemmy.core.utils.fs

import androidx.compose.runtime.Composable
import okio.Path

interface FileSystemManager {
    val isSupported: Boolean

    @Composable
    fun readFromFile(
        mimeTypes: Array<String>,
        callback: (String?) -> Unit,
    )

    @Composable
    fun writeToFile(
        mimeType: String,
        name: String,
        data: String,
        callback: (Boolean) -> Unit,
    )

    fun getTempDir(): Path
}
