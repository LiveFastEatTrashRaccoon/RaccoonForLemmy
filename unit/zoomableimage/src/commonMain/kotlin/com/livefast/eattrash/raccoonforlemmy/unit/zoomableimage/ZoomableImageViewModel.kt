package com.livefast.eattrash.raccoonforlemmy.unit.zoomableimage

import androidx.compose.ui.layout.ContentScale
import cafe.adriel.voyager.core.model.screenModelScope
import com.livefast.eattrash.raccoonforlemmy.core.architecture.DefaultMviModel
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.SettingsRepository
import com.livefast.eattrash.raccoonforlemmy.core.utils.datetime.epochMillis
import com.livefast.eattrash.raccoonforlemmy.core.utils.gallery.GalleryHelper
import com.livefast.eattrash.raccoonforlemmy.core.utils.gallery.download
import com.livefast.eattrash.raccoonforlemmy.core.utils.imageload.ImagePreloadManager
import com.livefast.eattrash.raccoonforlemmy.core.utils.share.ShareHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ZoomableImageViewModel(
    private val url: String,
    private val settingsRepository: SettingsRepository,
    private val shareHelper: ShareHelper,
    private val galleryHelper: GalleryHelper,
    private val imagePreloadManager: ImagePreloadManager,
) : DefaultMviModel<ZoomableImageMviModel.Intent, ZoomableImageMviModel.UiState, ZoomableImageMviModel.Effect>(
        initialState = ZoomableImageMviModel.UiState(),
    ),
    ZoomableImageMviModel {
    init {
        screenModelScope.launch {
            settingsRepository.currentSettings
                .onEach { settings ->
                    updateState { it.copy(autoLoadImages = settings.autoLoadImages) }
                }.launchIn(this)
        }
    }

    override fun reduce(intent: ZoomableImageMviModel.Intent) {
        when (intent) {
            is ZoomableImageMviModel.Intent.SaveToGallery -> downloadAndSave(intent.source)
            is ZoomableImageMviModel.Intent.ChangeContentScale -> changeContentScale(intent.contentScale)
            is ZoomableImageMviModel.Intent.ShareImageModeSelected.ModeFile ->
                shareFile(
                    url = intent.url,
                    folder = intent.source,
                )

            is ZoomableImageMviModel.Intent.ShareImageModeSelected.ModeUrl -> shareUrl(intent.url)
        }
    }

    private fun downloadAndSave(folder: String) {
        if (uiState.value.loading) {
            return
        }
        screenModelScope.launch {
            updateState { it.copy(loading = true) }
            val imageSourcePath = settingsRepository.currentSettings.value.imageSourcePath
            try {
                val bytes = galleryHelper.download(url)
                val extension = url.extractExtension()
                withContext(Dispatchers.IO) {
                    galleryHelper.saveToGallery(
                        bytes = bytes,
                        name = "${epochMillis()}$extension",
                        additionalPathSegment = folder.takeIf { imageSourcePath },
                    )
                }
                updateState { it.copy(loading = false) }
                emitEffect(ZoomableImageMviModel.Effect.ShareSuccess)
            } catch (e: Throwable) {
                e.printStackTrace()
                updateState { it.copy(loading = false) }
                emitEffect(ZoomableImageMviModel.Effect.ShareFailure)
            }
        }
    }

    private fun shareUrl(url: String) {
        runCatching {
            shareHelper.share(url)
        }
    }

    private fun shareFile(
        url: String,
        folder: String,
    ) {
        if (uiState.value.loading) {
            return
        }
        screenModelScope.launch {
            updateState { it.copy(loading = true) }
            val imageSourcePath = settingsRepository.currentSettings.value.imageSourcePath
            try {
                val bytes = galleryHelper.download(url)
                val extension = url.extractExtension()
                val path =
                    withContext(Dispatchers.IO) {
                        galleryHelper.saveToGallery(
                            bytes = bytes,
                            name = "${epochMillis()}$extension",
                            additionalPathSegment = folder.takeIf { imageSourcePath },
                        )
                    }
                updateState { it.copy(loading = false) }
                if (path != null) {
                    shareHelper.shareImage(path)
                } else {
                    emitEffect(ZoomableImageMviModel.Effect.ShareFailure)
                }
            } catch (e: Throwable) {
                e.printStackTrace()
                updateState { it.copy(loading = false) }
                emitEffect(ZoomableImageMviModel.Effect.ShareFailure)
            }
        }
    }

    private fun changeContentScale(contentScale: ContentScale) {
        imagePreloadManager.remove(url)
        screenModelScope.launch {
            updateState {
                it.copy(contentScale = contentScale)
            }
        }
    }
}

private fun String.extractExtension(): String =
    let { s ->
        val idx = s.lastIndexOf(".").takeIf { it >= 0 } ?: s.length
        s.substring(idx).takeIf { it.isNotEmpty() } ?: ".jpeg"
    }
