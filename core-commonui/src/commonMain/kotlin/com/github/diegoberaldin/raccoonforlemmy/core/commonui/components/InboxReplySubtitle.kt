package com.github.diegoberaldin.raccoonforlemmy.core.commonui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleDown
import androidx.compose.material.icons.filled.ArrowCircleUp
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.Spacing
import com.github.diegoberaldin.raccoonforlemmy.core.utils.DateTime
import com.github.diegoberaldin.raccoonforlemmy.core.utils.onClick
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.CommunityModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.UserModel
import com.github.diegoberaldin.raccoonforlemmy.resources.MR
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun InboxReplySubtitle(
    modifier: Modifier = Modifier,
    autoLoadImages: Boolean = true,
    creator: UserModel? = null,
    community: CommunityModel? = null,
    iconSize: Dp = 20.dp,
    date: String? = null,
    score: Int = 0,
    upvotes: Int = 0,
    downvotes: Int = 0,
    separateUpAndDownVotes: Boolean = false,
    upVoted: Boolean = false,
    downVoted: Boolean = false,
    onOpenCommunity: ((CommunityModel) -> Unit)? = null,
    onOpenCreator: ((UserModel) -> Unit)? = null,
    onUpVote: (() -> Unit)? = null,
    onDownVote: (() -> Unit)? = null,
) {
    val buttonModifier = Modifier.size(28.dp).padding(4.dp)
    Column {
        val communityName = community?.name.orEmpty()
        val communityIcon = community?.icon.orEmpty()
        val communityHost = community?.host.orEmpty()
        val creatorName = creator?.name.orEmpty()
        val creatorAvatar = creator?.avatar.orEmpty()
        val creatorHost = creator?.host.orEmpty()
        if (communityName.isNotEmpty() || creatorName.isNotEmpty()) {
            Row(
                modifier = modifier.padding(vertical = Spacing.xs),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
            ) {
                if (creatorName.isNotEmpty()) {
                    Row(
                        modifier = Modifier
                            .onClick {
                                if (creator != null) {
                                    onOpenCreator?.invoke(creator)
                                }
                            },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
                    ) {
                        if (creatorAvatar.isNotEmpty() && autoLoadImages) {
                            CustomImage(
                                modifier = Modifier
                                    .padding(Spacing.xxxs)
                                    .size(iconSize)
                                    .clip(RoundedCornerShape(iconSize / 2)),
                                url = creatorAvatar,
                                quality = FilterQuality.Low,
                                contentDescription = null,
                                contentScale = ContentScale.FillBounds,
                            )
                        }
                        Text(
                            modifier = Modifier.padding(vertical = Spacing.xs),
                            text = buildString {
                                append(creatorName)
                                if (creatorHost.isNotEmpty() && communityHost != creatorHost) {
                                    append("@$creatorHost")
                                }
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                    }
                }
                if (communityName.isNotEmpty()) {
                    if (creatorName.isNotEmpty()) {
                        Text(
                            text = "•",
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                    Row(
                        modifier = Modifier
                            .onClick {
                                if (community != null) {
                                    onOpenCommunity?.invoke(community)
                                }
                            },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
                    ) {
                        if (communityIcon.isNotEmpty() && autoLoadImages) {
                            CustomImage(
                                modifier = Modifier
                                    .padding(Spacing.xxxs)
                                    .size(iconSize)
                                    .clip(RoundedCornerShape(iconSize / 2)),
                                url = communityIcon,
                                quality = FilterQuality.Low,
                                contentDescription = null,
                                contentScale = ContentScale.FillBounds,
                            )
                        }
                        Text(
                            modifier = Modifier.padding(vertical = Spacing.xs),
                            text = buildString {
                                append(communityName)
                                if (communityHost.isNotEmpty()) {
                                    append("@$communityHost")
                                }
                            },
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                }
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                modifier = buttonModifier,
                imageVector = Icons.Default.Schedule,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground,
            )
            Text(
                text = date?.let {
                    when {
                        it.isEmpty() -> it
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
                } ?: "",
            )
            Spacer(modifier = Modifier.weight(1f))
            Image(
                modifier = buttonModifier
                    .onClick {
                        onUpVote?.invoke()
                    },
                imageVector = Icons.Default.ArrowCircleUp,
                contentDescription = null,
                colorFilter = ColorFilter.tint(
                    color = if (upVoted) {
                        MaterialTheme.colorScheme.surfaceTint
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    },
                ),
            )
            Text(
                text = buildAnnotatedString {
                    if (separateUpAndDownVotes) {
                        val upvoteText = upvotes.toString()
                        append(upvoteText)
                        if (upVoted) {
                            addStyle(
                                style = SpanStyle(color = MaterialTheme.colorScheme.surfaceTint),
                                start = 0,
                                end = upvoteText.length
                            )
                        }
                        append(" / ")
                        val downvoteText = downvotes.toString()
                        append(downvoteText)
                        if (downVoted) {
                            addStyle(
                                style = SpanStyle(color = MaterialTheme.colorScheme.tertiary),
                                start = upvoteText.length + 3,
                                end = upvoteText.length + 3 + downvoteText.length
                            )
                        }
                    } else {
                        val text = score.toString()
                        append(text)
                        if (upVoted) {
                            addStyle(
                                style = SpanStyle(color = MaterialTheme.colorScheme.surfaceTint),
                                start = 0,
                                end = text.length
                            )
                        } else if (downVoted) {
                            addStyle(
                                style = SpanStyle(color = MaterialTheme.colorScheme.tertiary),
                                start = 0,
                                end = length
                            )
                        }
                    }
                },
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Image(
                modifier = buttonModifier
                    .onClick {
                        onDownVote?.invoke()
                    },
                imageVector = Icons.Default.ArrowCircleDown,
                contentDescription = null,
                colorFilter = ColorFilter.tint(
                    color = if (downVoted) {
                        MaterialTheme.colorScheme.tertiary
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    },
                ),
            )
        }
    }
}
