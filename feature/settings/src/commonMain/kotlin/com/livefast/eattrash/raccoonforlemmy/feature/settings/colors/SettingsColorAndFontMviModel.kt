package com.livefast.eattrash.raccoonforlemmy.feature.settings.colors

import androidx.compose.ui.graphics.Color
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.CommentBarTheme
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.UiFontFamily
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.UiTheme
import com.livefast.eattrash.raccoonforlemmy.core.architecture.MviModel
import com.livefast.eattrash.raccoonforlemmy.feature.settings.colors.SettingsColorAndFontMviModel.Effect
import com.livefast.eattrash.raccoonforlemmy.feature.settings.colors.SettingsColorAndFontMviModel.Intent
import com.livefast.eattrash.raccoonforlemmy.feature.settings.colors.SettingsColorAndFontMviModel.UiState

interface SettingsColorAndFontMviModel : MviModel<Intent, UiState, Effect> {
    sealed interface Intent {
        data class ChangeDynamicColors(val value: Boolean) : Intent

        data class ChangeRandomColor(val value: Boolean) : Intent

        data class ChangeTheme(val value: UiTheme) : Intent

        data class ChangeCommentBarTheme(val value: CommentBarTheme) : Intent

        data class ChangeFontFamily(val value: UiFontFamily) : Intent

        data class ChangeFontSize(val value: Float) : Intent

        data class ChangeThemeColor(val value: Color) : Intent

        data class ChangeActionColor(val value: Color, val type: CustomColorType) : Intent
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
