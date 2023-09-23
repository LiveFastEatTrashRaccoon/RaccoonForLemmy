package com.github.diegoberaldin.raccoonforlemmy.core.commonui.components

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
import androidx.compose.material.icons.filled.Padding
import androidx.compose.material.icons.filled.Reply
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.github.diegoberaldin.racconforlemmy.core.utils.DateTime
import com.github.diegoberaldin.racconforlemmy.core.utils.onClick
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.Spacing
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.UserModel
import com.github.diegoberaldin.raccoonforlemmy.resources.MR
import dev.icerock.moko.resources.compose.stringResource
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

@Composable
fun UserHeader(
    user: UserModel,
    onOpenBookmarks: (() -> Unit)? = null,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(4.5f)
            .padding(Spacing.xs),
    ) {
        // banner
        val banner = user.banner.orEmpty()
        if (banner.isNotEmpty()) {
            val painterResource = asyncPainterResource(
                data = banner,
                filterQuality = FilterQuality.Low,
            )
            KamelImage(
                modifier = Modifier.fillMaxSize(),
                resource = painterResource,
                contentScale = ContentScale.FillBounds,
                contentDescription = null,
            )
        }

        Row(
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            // open bookmarks button
            if (onOpenBookmarks != null) {
                Icon(
                    modifier = Modifier.onClick {
                        onOpenBookmarks.invoke()
                    },
                    imageVector = Icons.Outlined.Bookmarks,
                    contentDescription = null,
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.m)
        ) {
            // avatar
            val userAvatar = user.avatar.orEmpty()
            val avatarSize = 60.dp
            if (userAvatar.isNotEmpty()) {
                val painterResource = asyncPainterResource(
                    data = userAvatar,
                    filterQuality = FilterQuality.Low,
                )
                KamelImage(
                    modifier = Modifier
                        .padding(Spacing.xxxs)
                        .size(avatarSize)
                        .clip(RoundedCornerShape(avatarSize / 2)),
                    resource = painterResource,
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                )
            } else {
                Box(
                    modifier = Modifier
                        .padding(Spacing.xxxs)
                        .size(avatarSize)
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(avatarSize / 2),
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = user.name.firstOrNull()?.toString()
                            .orEmpty()
                            .uppercase(),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }

            // textual data
            Column(
                verticalArrangement = Arrangement.spacedBy(Spacing.s),
            ) {
                Text(
                    text = buildString {
                        if (user.displayName.isNotEmpty()) {
                            append(user.displayName)
                        } else {
                            append(user.name)
                        }
                    },
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = buildString {
                        append(user.name)
                        append("@")
                        append(user.host)
                    },
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )

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
                            imageVector = Icons.Default.Padding,
                            contentDescription = null
                        )
                        Text(
                            text = postScore.toString(),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                    if (commentScore != null) {
                        if (postScore != null) {
                            Spacer(modifier = Modifier.width(Spacing.xxxs))
                        }
                        Icon(
                            modifier = Modifier.size(iconSize),
                            imageVector = Icons.Default.Reply,
                            contentDescription = null
                        )
                        Text(
                            text = commentScore.toString(),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }

                    if (user.accountAge.isNotEmpty()) {
                        if (postScore != null || commentScore != null) {
                            Spacer(modifier = Modifier.width(Spacing.xxxs))
                        }
                        Icon(
                            modifier = Modifier.size(iconSize),
                            imageVector = Icons.Default.Schedule,
                            contentDescription = null
                        )
                        Text(
                            text = user.accountAge.let {
                                when {
                                    !it.endsWith("Z") -> {
                                        DateTime.getPrettyDate(
                                            iso8601Timestamp = it + "Z",
                                            yearLabel = stringResource(MR.strings.profile_year_short),
                                            monthLabel = stringResource(MR.strings.profile_month_short),
                                            dayLabel = stringResource(MR.strings.profile_day_short),
                                            hourLabel = stringResource(MR.strings.post_hour_short),
                                            minuteLabel = stringResource(MR.strings.post_minute_short),
                                            secondLabel = stringResource(MR.strings.post_second_short),
                                        )
                                    }

                                    else -> {
                                        DateTime.getPrettyDate(
                                            iso8601Timestamp = it,
                                            yearLabel = stringResource(MR.strings.profile_year_short),
                                            monthLabel = stringResource(MR.strings.profile_month_short),
                                            dayLabel = stringResource(MR.strings.profile_day_short),
                                            hourLabel = stringResource(MR.strings.post_hour_short),
                                            minuteLabel = stringResource(MR.strings.post_minute_short),
                                            secondLabel = stringResource(MR.strings.post_second_short),
                                        )
                                    }
                                }
                            },
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                }
            }
        }
    }
}
