package com.livefast.eattrash.raccoonforlemmy.feature.settings.colors

import androidx.compose.ui.graphics.Color
import cafe.adriel.voyager.core.model.ScreenModel
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.CommentBarTheme
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.UiFontFamily
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.UiTheme
import com.livefast.eattrash.raccoonforlemmy.core.architecture.MviModel

interface SettingsColorAndFontMviModel :
    MviModel<SettingsColorAndFontMviModel.Intent, SettingsColorAndFontMviModel.UiState, SettingsColorAndFontMviModel.Effect>,
    ScreenModel {
    sealed interface Intent {
        data class ChangeDynamicColors(
            val value: Boolean,
        ) : Intent

        data class ChangeRandomColor(
            val value: Boolean,
        ) : Intent
    }

    data class UiState(
        val isLogged: Boolean = false,
        val supportsDynamicColors: Boolean = false,
        val uiTheme: UiTheme? = null,
        val dynamicColors: Boolean = false,
        val customSeedColor: Color? = null,
        val upVoteColor: Color? = null,
        val downVoteColor: Color? = null,
        val replyColor: Color? = null,
        val saveColor: Color? = null,
        val commentBarTheme: CommentBarTheme = CommentBarTheme.Blue,
        val uiFontScale: Float = 1f,
        val uiFontFamily: UiFontFamily = UiFontFamily.Poppins,
        val randomColor: Boolean = false,
    )

    sealed interface Effect
}
