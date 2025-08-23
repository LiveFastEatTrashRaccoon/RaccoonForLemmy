@file:Suppress("ktlint:standard:max-line-length")

package com.livefast.eattrash.raccoonforlemmy.unit.configurecontentview

import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.CommentBarTheme
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.PostLayout
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.UiFontFamily
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.VoteFormat
import com.livefast.eattrash.raccoonforlemmy.core.appearance.repository.ContentFontClass
import com.livefast.eattrash.raccoonforlemmy.core.appearance.repository.ContentFontScales
import com.livefast.eattrash.raccoonforlemmy.core.architecture.MviModel
interface ConfigureContentViewMviModel :
    MviModel<ConfigureContentViewMviModel.Intent, ConfigureContentViewMviModel.UiState, ConfigureContentViewMviModel.Effect> {
    sealed interface Intent {
        data class ChangePreferUserNicknames(val value: Boolean) : Intent

        data class ChangeFullHeightImages(val value: Boolean) : Intent

        data class ChangeFullWidthImages(val value: Boolean) : Intent

        data object IncrementCommentBarThickness : Intent

        data object DecrementCommentBarThickness : Intent

        data object IncrementCommentIndentAmount : Intent

        data object DecrementCommentIndentAmount : Intent

        data class ChangeVoteFormat(val value: VoteFormat) : Intent

        data class ChangePostLayout(val value: PostLayout) : Intent

        data class ChangeContentFontFamily(val value: UiFontFamily) : Intent

        data class ChangeContentFontSize(val value: Float, val contentClass: ContentFontClass) : Intent

        data class SelectPostBodyMaxLines(val value: Int) : Intent
    }

    data class UiState(
        val postLayout: PostLayout = PostLayout.Card,
        val commentBarTheme: CommentBarTheme = CommentBarTheme.Blue,
        val commentBarThickness: Int = 1,
        val commentIndentAmount: Int = 2,
        val contentFontScale: ContentFontScales = ContentFontScales(),
        val contentFontFamily: UiFontFamily = UiFontFamily.Poppins,
        val voteFormat: VoteFormat = VoteFormat.Aggregated,
        val postBodyMaxLines: Int? = null,
        val fullHeightImages: Boolean = false,
        val fullWidthImages: Boolean = false,
        val preferUserNicknames: Boolean = true,
        val downVoteEnabled: Boolean = true,
    )

    sealed interface Effect
}
