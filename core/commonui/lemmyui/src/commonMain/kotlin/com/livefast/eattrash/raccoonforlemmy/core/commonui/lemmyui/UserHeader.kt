package com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.automirrored.filled.Reply
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.IconSize
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.CustomImage
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.PlaceholderImage
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.utils.compose.onClick
import com.livefast.eattrash.raccoonforlemmy.core.utils.datetime.prettifyDate
import com.livefast.eattrash.raccoonforlemmy.core.utils.getPrettyNumber
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.UserModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.readableHandle
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.readableName

private const val ASPECT_RATIO = 3.5f

@Composable
fun UserHeader(
    user: UserModel,
    modifier: Modifier = Modifier,
    autoLoadImages: Boolean = true,
    preferNicknames: Boolean = true,
    onOpenImage: ((String) -> Unit)? = null,
    onInfo: (() -> Unit)? = null,
) {
    Box(
        modifier = modifier.fillMaxWidth(),
    ) {
        // banner
        val banner = user.banner.orEmpty()
        if (banner.isNotEmpty() && autoLoadImages) {
            Box(
                modifier = Modifier.fillMaxWidth().aspectRatio(ASPECT_RATIO),
            ) {
                CustomImage(
                    modifier = Modifier.fillMaxSize(),
                    url = banner,
                    autoload = autoLoadImages,
                    quality = FilterQuality.Low,
                    contentScale = ContentScale.Crop,
                )
                Box(
                    modifier =
                    Modifier.fillMaxSize().background(
                        brush =
                        Brush.horizontalGradient(
                            colors =
                            listOf(
                                MaterialTheme.colorScheme.background.copy(alpha = 0.95f),
                                Color.Transparent,
                                MaterialTheme.colorScheme.background.copy(alpha = 0.75f),
                            ),
                        ),
                    ),
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(Spacing.s).align(Alignment.Center),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.m),
        ) {
            // avatar
            val userAvatar = user.avatar.orEmpty()
            if (userAvatar.isNotEmpty() && autoLoadImages) {
                CustomImage(
                    modifier =
                    Modifier
                        .padding(Spacing.xxxs)
                        .size(IconSize.xxl)
                        .clip(RoundedCornerShape(IconSize.xxl / 2))
                        .onClick(
                            onClick = {
                                onOpenImage?.invoke(userAvatar)
                            },
                        ),
                    url = userAvatar,
                    autoload = autoLoadImages,
                    quality = FilterQuality.Low,
                    contentScale = ContentScale.FillBounds,
                )
            } else {
                PlaceholderImage(
                    size = IconSize.xxl,
                    title = user.name,
                )
            }

            // textual data
            Column(
                verticalArrangement = Arrangement.spacedBy(Spacing.s),
            ) {
                Text(
                    text = user.readableName(preferNicknames),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                if (user.readableHandle != user.readableName(preferNicknames)) {
                    Text(
                        text = user.readableHandle,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                }

                // stats and age
                val iconSize = 22.dp
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Spacing.s),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    val postScore = user.score?.postScore
                    val commentScore = user.score?.commentScore
                    if (postScore != null) {
                        Icon(
                            modifier = Modifier.size(iconSize),
                            imageVector = Icons.AutoMirrored.Default.Article,
                            contentDescription = LocalStrings.current.userPostScore,
                        )
                        Text(
                            text =
                            postScore.getPrettyNumber(
                                thousandLabel = LocalStrings.current.profileThousandShort,
                                millionLabel = LocalStrings.current.profileMillionShort,
                            ),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                    }
                    if (commentScore != null) {
                        if (postScore != null) {
                            Spacer(modifier = Modifier.width(Spacing.xxxs))
                        }
                        Icon(
                            modifier = Modifier.size(iconSize),
                            imageVector = Icons.AutoMirrored.Default.Reply,
                            contentDescription = LocalStrings.current.userCommentScore,
                        )
                        Text(
                            text =
                            commentScore.getPrettyNumber(
                                thousandLabel = LocalStrings.current.profileThousandShort,
                                millionLabel = LocalStrings.current.profileMillionShort,
                            ),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                    }

                    if (user.accountAge.isNotEmpty()) {
                        if (postScore != null || commentScore != null) {
                            Spacer(modifier = Modifier.width(Spacing.xxxs))
                        }
                        Icon(
                            modifier = Modifier.size(iconSize),
                            imageVector = Icons.Default.Cake,
                            contentDescription = LocalStrings.current.userAccountAge,
                        )
                        Text(
                            text = user.accountAge.prettifyDate(),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                    }

                    if (user.bot) {
                        if (postScore != null || commentScore != null || user.accountAge.isNotEmpty()) {
                            Spacer(modifier = Modifier.width(Spacing.xxxs))
                        }
                        IndicatorChip(text = "BOT")
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    if (onInfo != null) {
                        IconButton(
                            modifier = Modifier.padding(end = Spacing.s).size(iconSize),
                            onClick = {
                                onInfo.invoke()
                            },
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = LocalStrings.current.moreInfo,
                            )
                        }
                    }
                }
            }
        }
    }
}
