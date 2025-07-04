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
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.ModlogItem
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.UserModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.readableName

@Composable
internal fun ModTransferCommunityItem(
    item: ModlogItem.ModTransferCommunity,
    modifier: Modifier = Modifier,
    autoLoadImages: Boolean = true,
    preferNicknames: Boolean = true,
    postLayout: PostLayout = PostLayout.Card,
    onOpenUser: ((UserModel) -> Unit)? = null,
) {
    InnerModlogItem(
        modifier = modifier,
        autoLoadImages = autoLoadImages,
        date = item.date,
        postLayout = postLayout,
        creator = item.moderator,
        onOpenUser = onOpenUser,
        onOpen = {
            item.user?.also {
                onOpenUser?.invoke(it)
            }
        },
        innerContent = {
            Text(
                text =
                buildAnnotatedString {
                    append(LocalStrings.current.modlogItemCommunityTransfer)
                    append(" ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
                        val name = item.user?.readableName(preferNicknames).orEmpty()
                        append(name)
                    }
                },
                style = MaterialTheme.typography.bodySmall,
            )
        },
    )
}
