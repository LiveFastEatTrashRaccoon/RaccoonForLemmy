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
import com.livefast.eattrash.raccoonforlemmy.core.l10n.messages.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.ModlogItem
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.UserModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.readableName

@Composable
internal fun RemoveCommunityItem(
    item: ModlogItem.RemoveCommunity,
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
        moderator = item.moderator,
        onOpenUser = onOpenUser,
        onOpen = {
            item.moderator?.also {
                onOpenUser?.invoke(it)
            }
        },
        innerContent = {
            Text(
                text =
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
                            val name = item.community?.readableName(preferNicknames).orEmpty()
                            append(name)
                        }
                        append(" ")
                        append(LocalStrings.current.modlogItemPostRemoved)
                    },
                style = MaterialTheme.typography.bodySmall,
            )
        },
    )
}
