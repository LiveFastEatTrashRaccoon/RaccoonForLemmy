package com.livefast.eattrash.raccoonforlemmy.unit.modlog.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.buildAnnotatedString
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.PostLayout
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.ModlogItem
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.UserModel

@Composable
internal fun AdminPurgeCommunityItem(
    item: ModlogItem.AdminPurgeCommunity,
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
        moderator = item.admin,
        onOpenUser = onOpenUser,
        innerContent = {
            Text(
                text =
                    buildAnnotatedString {
                        append(LocalStrings.current.modlogItemCommunityPurged)
                    },
                style = MaterialTheme.typography.bodySmall,
            )
        },
    )
}
