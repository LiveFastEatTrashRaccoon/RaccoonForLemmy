package com.livefast.eattrash.raccoonforlemmy.unit.zoomableimage

import androidx.compose.runtime.Stable
import androidx.compose.ui.layout.ContentScale
import com.livefast.eattrash.raccoonforlemmy.core.architecture.MviModel

@Stable
interface ZoomableImageMviModel :
    MviModel<ZoomableImageMviModel.Intent, ZoomableImageMviModel.UiState, ZoomableImageMviModel.Effect> {
    sealed interface Intent {
        data class SaveToGallery(val source: String) : Intent

        sealed interface ShareImageModeSelected : Intent {
            data class ModeUrl(val url: String) : ShareImageModeSelected

            data class ModeFile(val url: String, val source: String) : ShareImageModeSelected
        }

        data class ChangeContentScale(val contentScale: ContentScale) : Intent
    }

    data class UiState(
        val loading: Boolean = false,
        val autoLoadImages: Boolean = true,
        val contentScale: ContentScale = ContentScale.FillWidth,
    )

    sealed interface Effect {
        data object ShareSuccess : Effect

        data object ShareFailure : Effect
    }
}
