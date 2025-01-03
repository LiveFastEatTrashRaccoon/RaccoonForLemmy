package com.livefast.eattrash.raccoonforlemmy.unit.drafts.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.automirrored.filled.Reply
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.PostLayout
import com.livefast.eattrash.raccoonforlemmy.core.appearance.repository.ContentFontClass
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.CornerSize
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.IconSize
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.ancillaryTextAlpha
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.CustomDropDown
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.CustomizedContent
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.Option
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.OptionId
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.PostCardBody
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.PostCardTitle
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.DraftModel
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.DraftType
import com.livefast.eattrash.raccoonforlemmy.core.utils.compose.onClick
import com.livefast.eattrash.raccoonforlemmy.core.utils.datetime.prettifyDate
import com.livefast.eattrash.raccoonforlemmy.core.utils.datetime.toIso8601Timestamp
import com.livefast.eattrash.raccoonforlemmy.core.utils.toLocalDp

@Composable
fun DraftCard(
    draft: DraftModel,
    postLayout: PostLayout,
    modifier: Modifier = Modifier,
    options: List<Option> = emptyList(),
    onOpen: () -> Unit,
    onOptionSelected: ((OptionId) -> Unit)? = null,
) {
    Box(
        modifier =
            modifier
                .then(
                    if (postLayout == PostLayout.Card) {
                        Modifier
                            .padding(horizontal = Spacing.xs)
                            .shadow(elevation = 5.dp, shape = RoundedCornerShape(CornerSize.l))
                            .clip(RoundedCornerShape(CornerSize.l))
                            .background(
                                color = MaterialTheme.colorScheme.surfaceColorAtElevation(5.dp),
                                shape = RoundedCornerShape(CornerSize.l),
                            ).padding(vertical = Spacing.s)
                    } else {
                        Modifier.background(MaterialTheme.colorScheme.background)
                    },
                ).onClick(onClick = onOpen),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(Spacing.xs),
        ) {
            draft.reference?.also { reference ->
                CustomizedContent(ContentFontClass.AncillaryText) {
                    Row(
                        modifier = Modifier.padding(horizontal = Spacing.s),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
                    ) {
                        val imageVector =
                            when (draft.type) {
                                DraftType.Comment -> Icons.AutoMirrored.Default.Reply
                                DraftType.Post -> Icons.AutoMirrored.Default.Article
                            }
                        Icon(
                            imageVector = imageVector,
                            contentDescription =
                                when (draft.type) {
                                    DraftType.Comment -> LocalStrings.current.exploreResultTypePosts
                                    DraftType.Post -> LocalStrings.current.exploreResultTypeComments
                                },
                            modifier = Modifier.size(IconSize.s),
                        )
                        Text(
                            text = reference,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onBackground,
                            maxLines = 1,
                        )
                    }
                }
            }

            draft.title?.also { title ->
                CustomizedContent(ContentFontClass.Title) {
                    PostCardTitle(
                        modifier =
                            Modifier.padding(horizontal = Spacing.s),
                        text = title,
                        onClick = onOpen,
                    )
                }
            }

            CustomizedContent(ContentFontClass.Body) {
                PostCardBody(
                    modifier = Modifier.padding(horizontal = Spacing.s),
                    text = draft.body,
                    maxLines = 40,
                    onClick = onOpen,
                )
            }
            DraftFooter(
                modifier = Modifier.padding(horizontal = Spacing.s),
                date = draft.date?.toIso8601Timestamp(),
                options = options,
                onOptionSelected = onOptionSelected,
            )
        }
    }
}

@Composable
private fun DraftFooter(
    modifier: Modifier = Modifier,
    date: String? = null,
    options: List<Option> = emptyList(),
    onOptionSelected: ((OptionId) -> Unit)? = null,
) {
    var optionsExpanded by remember { mutableStateOf(false) }
    var optionsOffset by remember { mutableStateOf(Offset.Zero) }
    val ancillaryColor = MaterialTheme.colorScheme.onBackground.copy(alpha = ancillaryTextAlpha)

    Box(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.xxs),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    modifier = Modifier.size(IconSize.m).padding(1.5.dp),
                    imageVector = Icons.Default.Schedule,
                    contentDescription = LocalStrings.current.creationDate,
                    tint = ancillaryColor,
                )
                Text(
                    modifier = Modifier.padding(start = Spacing.xxs),
                    text = date?.prettifyDate() ?: "",
                    style = MaterialTheme.typography.labelLarge,
                    color = ancillaryColor,
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            if (options.isNotEmpty()) {
                IconButton(
                    modifier =
                        Modifier
                            .size(IconSize.m)
                            .padding(Spacing.xs)
                            .padding(top = Spacing.xxs)
                            .onGloballyPositioned {
                                optionsOffset = it.positionInParent()
                            },
                    onClick = {
                        optionsExpanded = true
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreHoriz,
                        contentDescription = LocalStrings.current.actionOpenOptionMenu,
                        tint = ancillaryColor,
                    )
                }
            }
        }
        CustomDropDown(
            expanded = optionsExpanded,
            onDismiss = {
                optionsExpanded = false
            },
            offset =
                DpOffset(
                    x = optionsOffset.x.toLocalDp(),
                    y = optionsOffset.y.toLocalDp(),
                ),
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(option.text)
                    },
                    onClick = {
                        optionsExpanded = false
                        onOptionSelected?.invoke(option.id)
                    },
                )
            }
        }
    }
}
