package com.livefast.eattrash.raccoonforlemmy.unit.modlog.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.PostLayout
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.utils.ellipsize
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.ModlogItem
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.UserModel

@Composable
internal fun ModRemoveCommentItem(
    item: ModlogItem.ModRemoveComment,
    modifier: Modifier = Modifier,
    autoLoadImages: Boolean = true,
    preferNicknames: Boolean = true,
    postLayout: PostLayout = PostLayout.Card,
    onOpenUser: ((UserModel) -> Unit)? = null,
) {
    InnerModlogItem(
        modifier = modifier,
        autoLoadImages = autoLoadImages,
        preferNicknames = preferNicknames,
        date = item.date,
        postLayout = postLayout,
        creator = item.moderator,
        onOpenUser = onOpenUser,
        innerContent = {
            Text(
                text =
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
                            append(item.comment?.text.ellipsize())
                        }
                        append(" ")
                        if (item.removed) {
                            append(LocalStrings.current.modlogItemCommentRemoved)
                        } else {
                            append(LocalStrings.current.modlogItemCommentRestored)
                        }
                        if (item.post != null) {
                            append(" ")
                            withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
                                append(item.post?.title.ellipsize())
                            }
                        }
                    },
                style = MaterialTheme.typography.bodySmall,
            )
        },
    )
}
