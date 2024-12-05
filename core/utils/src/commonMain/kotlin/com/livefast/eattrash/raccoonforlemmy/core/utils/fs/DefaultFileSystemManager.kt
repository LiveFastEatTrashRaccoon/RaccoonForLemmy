package com.livefast.eattrash.raccoonforlemmy.core.utils.fs

import androidx.compose.runtime.Composable
import okio.Path
import org.koin.core.annotation.Single

@Single
internal expect class DefaultFileSystemManager : FileSystemManager {
    override val isSupported: Boolean

    @Composable
    override fun readFromFile(
        mimeTypes: Array<String>,
        callback: (String?) -> Unit,
    )

    @Composable
    override fun writeToFile(
        mimeType: String,
        name: String,
        data: String,
        callback: (Boolean) -> Unit,
    )

    override fun getTempDir(): Path
}
