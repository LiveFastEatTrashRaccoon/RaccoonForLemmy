package com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.LocalPolice
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.livefast.eattrash.raccoonforlemmy.core.appearance.repository.ContentFontClass
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.IconSize
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.ancillaryTextAlpha
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.readContentAlpha
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.CustomImage
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.CustomizedContent
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.PlaceholderImage
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.utils.compose.onClick
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.CommunityModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.UserModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.populateSpecialTags
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.readableName

@Composable
fun CommunityAndCreatorInfo(
    modifier: Modifier = Modifier,
    iconSize: Dp = IconSize.l,
    indicatorExpanded: Boolean? = null,
    autoLoadImages: Boolean = true,
    preferNicknames: Boolean = true,
    community: CommunityModel? = null,
    creator: UserModel? = null,
    distinguished: Boolean = false,
    featuredCommunity: Boolean = false,
    featuredLocal: Boolean = false,
    locked: Boolean = false,
    isFromModerator: Boolean = false,
    isOp: Boolean = false,
    isBot: Boolean = false,
    isMod: Boolean = false,
    isAdmin: Boolean = false,
    isCurrentUser: Boolean = false,
    markRead: Boolean = false,
    compact: Boolean = false,
    adminTagColor: Int? = null,
    botTagColor: Int? = null,
    meTagColor: Int? = null,
    opTagColor: Int? = null,
    modTagColor: Int? = null,
    onOpenCommunity: ((CommunityModel) -> Unit)? = null,
    onOpenCreator: ((UserModel) -> Unit)? = null,
    onToggleExpand: (() -> Unit)? = null,
    onDoubleClick: (() -> Unit)? = null,
    onLongClick: (() -> Unit)? = null,
) {
    val additionalAlphaFactor = if (markRead) readContentAlpha else 1f
    val communityName = community?.readableName(preferNicknames).orEmpty()
    val communityIcon = community?.icon.orEmpty()
    val creatorName = creator?.readableName(preferNicknames).orEmpty()
    val creatorAvatar = creator?.avatar.orEmpty()
    val fullColor = MaterialTheme.colorScheme.onBackground.copy(alpha = additionalAlphaFactor)
    val ancillaryColor = MaterialTheme.colorScheme.onBackground.copy(alpha = ancillaryTextAlpha * additionalAlphaFactor)

    CustomizedContent(ContentFontClass.AncillaryText) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.s),
        ) {
            if (communityIcon.isNotEmpty()) {
                if (autoLoadImages) {
                    CustomImage(
                        modifier =
                        Modifier
                            .size(iconSize)
                            .padding(Spacing.xxxs)
                            .clip(RoundedCornerShape(iconSize / 2))
                            .onClick(
                                onClick = {
                                    if (community != null) {
                                        onOpenCommunity?.invoke(community)
                                    }
                                },
                                onDoubleClick = onDoubleClick ?: {},
                            ).clearAndSetSemantics { },
                        url = communityIcon,
                        autoload = autoLoadImages,
                        quality = FilterQuality.Low,
                        contentScale = ContentScale.FillBounds,
                    )
                } else {
                    PlaceholderImage(
                        modifier = Modifier.clearAndSetSemantics { },
                        onClick = {
                            if (community != null) {
                                onOpenCommunity?.invoke(community)
                            }
                        },
                        size = iconSize,
                        title = communityName,
                    )
                }
            } else if (creatorAvatar.isNotEmpty()) {
                if (autoLoadImages) {
                    CustomImage(
                        modifier =
                        Modifier
                            .size(iconSize)
                            .padding(Spacing.xxxs)
                            .clip(RoundedCornerShape(iconSize / 2))
                            .onClick(
                                onClick = {
                                    if (creator != null) {
                                        onOpenCreator?.invoke(creator)
                                    }
                                },
                                onDoubleClick = onDoubleClick ?: {},
                            ).clearAndSetSemantics { },
                        url = creatorAvatar,
                        quality = FilterQuality.Low,
                        autoload = autoLoadImages,
                        contentScale = ContentScale.FillBounds,
                    )
                } else {
                    PlaceholderImage(
                        modifier = Modifier.clearAndSetSemantics { },
                        onClick = {
                            if (creator != null) {
                                onOpenCreator?.invoke(creator)
                            }
                        },
                        size = iconSize,
                        title = creatorName,
                    )
                }
            }
            Column(
                modifier = Modifier.padding(vertical = Spacing.xxxs),
            ) {
                if (community != null) {
                    Text(
                        modifier =
                        Modifier
                            .onClick(
                                indication = null,
                                onDoubleClick = onDoubleClick ?: {},
                                onLongClick = onLongClick ?: {},
                            ).clearAndSetSemantics { },
                        text =
                        buildAnnotatedString {
                            pushLink(
                                LinkAnnotation.Clickable("click-community") {
                                    onOpenCommunity?.invoke(community)
                                },
                            )
                            append(communityName)
                            pop()
                            if (compact && creator != null) {
                                append(" • ")
                                pushLink(
                                    LinkAnnotation.Clickable("click-user") {
                                        onOpenCreator?.invoke(creator)
                                    },
                                )
                                pushStyle(SpanStyle(color = ancillaryColor))
                                append(creatorName)
                                pop()
                                pop()
                            }
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = if (creator == null) ancillaryColor else fullColor,
                        maxLines = 1,
                    )
                }
                if (creator != null && !compact) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            modifier =
                            Modifier
                                .onClick(
                                    indication = null,
                                    onClick = {
                                        onOpenCreator?.invoke(creator)
                                    },
                                    onDoubleClick = onDoubleClick ?: {},
                                    onLongClick = onLongClick ?: {},
                                ).clearAndSetSemantics { },
                            text = creatorName,
                            style = MaterialTheme.typography.bodySmall,
                            color =
                            when {
                                isCurrentUser && meTagColor != null -> Color(meTagColor)
                                isOp && opTagColor != null -> Color(opTagColor)
                                else -> ancillaryColor
                            },
                            maxLines = 1,
                        )

                        // user tags
                        val userWithTags =
                            creator.populateSpecialTags(
                                isAdmin = isAdmin,
                                isOp = isOp,
                                isMe = isCurrentUser,
                                isBot = isBot,
                                isMod = isMod,
                                adminColor = adminTagColor,
                                opColor = opTagColor,
                                meColor = meTagColor,
                                botColor = botTagColor,
                                modColor = modTagColor,
                            )
                        if (userWithTags.tags.isNotEmpty()) {
                            LazyRow(
                                modifier = Modifier.widthIn(max = 180.dp),
                                horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                items(userWithTags.tags) { tag ->
                                    val isColorful =
                                        tag.color != null && tag.color != Color.Transparent.toArgb()
                                    val tagColor =
                                        tag.color?.let {
                                            Color(it)
                                        } ?: MaterialTheme.colorScheme.onBackground
                                    IndicatorChip(
                                        text = tag.name,
                                        color = tagColor,
                                        full = isColorful,
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            val buttonModifier = Modifier.size(IconSize.l).padding(3.5.dp)
            if (isFromModerator) {
                Icon(
                    modifier = buttonModifier,
                    imageVector = Icons.Default.LocalPolice,
                    contentDescription = LocalStrings.current.itemFromModerator,
                )
            }
            if (distinguished) {
                Icon(
                    modifier = buttonModifier,
                    imageVector = Icons.Default.WorkspacePremium,
                    contentDescription = LocalStrings.current.itemDistinguished,
                )
            } else if (featuredLocal) {
                Icon(
                    modifier = buttonModifier,
                    imageVector = Icons.Default.Verified,
                    contentDescription = LocalStrings.current.itemFeaturedInstance,
                )
            } else if (featuredCommunity) {
                Icon(
                    modifier = buttonModifier,
                    imageVector = Icons.Default.Stars,
                    contentDescription = LocalStrings.current.itemFeaturedCommunity,
                )
            }

            if (locked) {
                Icon(
                    modifier = buttonModifier,
                    imageVector = Icons.Default.Lock,
                    contentDescription = LocalStrings.current.itemFeaturedInstance,
                )
            }
            if (indicatorExpanded != null) {
                IconButton(
                    modifier = Modifier.clearAndSetSemantics { },
                    onClick = {
                        onToggleExpand?.invoke()
                    },
                ) {
                    if (indicatorExpanded) {
                        Icon(
                            imageVector = Icons.Default.ExpandLess,
                            contentDescription = LocalStrings.current.actionCollapse,
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.ExpandMore,
                            contentDescription = LocalStrings.current.actionExpand,
                        )
                    }
                }
            }
        }
    }
}
