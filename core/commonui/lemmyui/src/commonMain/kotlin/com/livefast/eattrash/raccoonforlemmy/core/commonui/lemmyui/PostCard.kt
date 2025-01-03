package com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalTextToolbar
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.PostLayout
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.VoteFormat
import com.livefast.eattrash.raccoonforlemmy.core.appearance.repository.ContentFontClass
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.CornerSize
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.ancillaryTextAlpha
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.CustomizedContent
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.persistence.di.getSettingsRepository
import com.livefast.eattrash.raccoonforlemmy.core.utils.compose.onClick
import com.livefast.eattrash.raccoonforlemmy.core.utils.di.getShareHelper
import com.livefast.eattrash.raccoonforlemmy.core.utils.looksLikeAVideo
import com.livefast.eattrash.raccoonforlemmy.core.utils.looksLikeAnImage
import com.livefast.eattrash.raccoonforlemmy.core.utils.texttoolbar.getCustomTextToolbar
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.CommunityModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.PostModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.UserModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.imageUrl
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.videoUrl

@Composable
fun PostCard(
    post: PostModel,
    modifier: Modifier = Modifier,
    isFromModerator: Boolean = false,
    autoLoadImages: Boolean = true,
    preferNicknames: Boolean = true,
    showScores: Boolean = true,
    hideAuthor: Boolean = false,
    postLayout: PostLayout = PostLayout.Card,
    voteFormat: VoteFormat = VoteFormat.Aggregated,
    includeFullBody: Boolean = false,
    fullHeightImage: Boolean = true,
    fullWidthImage: Boolean = false,
    limitBodyHeight: Boolean = false,
    blurNsfw: Boolean = true,
    fadeRead: Boolean = false,
    showUnreadComments: Boolean = false,
    actionButtonsActive: Boolean = true,
    downVoteEnabled: Boolean = true,
    highlightText: String? = null,
    options: List<Option> = emptyList(),
    onOpenCommunity: ((CommunityModel, String) -> Unit)? = null,
    onOpenCreator: ((UserModel, String) -> Unit)? = null,
    onUpVote: (() -> Unit)? = null,
    onDownVote: (() -> Unit)? = null,
    onSave: (() -> Unit)? = null,
    onReply: (() -> Unit)? = null,
    onOpenImage: ((String) -> Unit)? = null,
    onOpenVideo: ((String) -> Unit)? = null,
    onOptionSelected: ((OptionId) -> Unit)? = null,
    onClick: (() -> Unit)? = null,
    onDoubleClick: (() -> Unit)? = null,
) {
    val markRead = post.read && fadeRead
    Box(
        modifier =
            modifier
                .then(
                    if (postLayout == PostLayout.Card) {
                        Modifier
                            .padding(horizontal = Spacing.xs)
                            .shadow(
                                elevation = 5.dp,
                                shape = RoundedCornerShape(CornerSize.l),
                            ).clip(RoundedCornerShape(CornerSize.l))
                            .background(
                                color = MaterialTheme.colorScheme.surfaceColorAtElevation(5.dp),
                            ).padding(vertical = Spacing.s)
                    } else {
                        Modifier
                    },
                ).onClick(
                    onDoubleClick = onDoubleClick ?: {},
                ),
    ) {
        if (postLayout != PostLayout.Compact) {
            ExtendedPost(
                post = post,
                isFromModerator = isFromModerator,
                hideAuthor = hideAuthor,
                backgroundColor =
                    when (postLayout) {
                        PostLayout.Card -> MaterialTheme.colorScheme.surfaceColorAtElevation(5.dp)
                        else -> MaterialTheme.colorScheme.background
                    },
                showBody = includeFullBody || postLayout == PostLayout.Full,
                limitBodyHeight = limitBodyHeight,
                voteFormat = voteFormat,
                autoLoadImages = autoLoadImages,
                preferNicknames = preferNicknames,
                showScores = showScores,
                roundedCornerImage = postLayout == PostLayout.Card,
                fullHeightImage = fullHeightImage,
                fullWidthImage = fullWidthImage,
                blurNsfw = blurNsfw,
                markRead = markRead,
                highlightText = highlightText,
                actionButtonsActive = actionButtonsActive,
                showUnreadComments = showUnreadComments,
                downVoteEnabled = downVoteEnabled,
                options = options,
                onOpenCommunity = onOpenCommunity,
                onOpenCreator = onOpenCreator,
                onUpVote = onUpVote,
                onDownVote = onDownVote,
                onSave = onSave,
                onReply = onReply,
                onOpenImage = onOpenImage,
                onOpenVideo = onOpenVideo,
                onOptionSelected = onOptionSelected,
                onClick = onClick,
                onDoubleClick = onDoubleClick,
            )
        } else {
            CompactPost(
                post = post,
                isFromModerator = isFromModerator,
                hideAuthor = hideAuthor,
                blurNsfw = blurNsfw,
                markRead = markRead,
                highlightText = highlightText,
                voteFormat = voteFormat,
                autoLoadImages = autoLoadImages,
                preferNicknames = preferNicknames,
                showScores = showScores,
                actionButtonsActive = actionButtonsActive,
                showUnreadComments = showUnreadComments,
                downVoteEnabled = downVoteEnabled,
                options = options,
                onOpenCommunity = onOpenCommunity,
                onOpenCreator = onOpenCreator,
                onUpVote = onUpVote,
                onDownVote = onDownVote,
                onSave = onSave,
                onReply = onReply,
                onOpenImage = onOpenImage,
                onOpenVideo = onOpenVideo,
                onOptionSelected = onOptionSelected,
                onClick = onClick,
                onDoubleClick = onDoubleClick,
            )
        }
    }
}

@Composable
private fun CompactPost(
    modifier: Modifier = Modifier,
    post: PostModel,
    isFromModerator: Boolean,
    autoLoadImages: Boolean,
    preferNicknames: Boolean,
    showScores: Boolean,
    hideAuthor: Boolean,
    blurNsfw: Boolean,
    markRead: Boolean,
    highlightText: String?,
    actionButtonsActive: Boolean,
    voteFormat: VoteFormat,
    showUnreadComments: Boolean,
    downVoteEnabled: Boolean,
    options: List<Option>,
    onOpenCommunity: ((CommunityModel, String) -> Unit)?,
    onOpenCreator: ((UserModel, String) -> Unit)?,
    onUpVote: (() -> Unit)?,
    onDownVote: (() -> Unit)?,
    onSave: (() -> Unit)?,
    onReply: (() -> Unit)?,
    onOpenImage: ((String) -> Unit)?,
    onOpenVideo: ((String) -> Unit)?,
    onOptionSelected: ((OptionId) -> Unit)?,
    onClick: (() -> Unit)?,
    onDoubleClick: (() -> Unit)?,
) {
    val settingsRepository = remember { getSettingsRepository() }
    val settings by settingsRepository.currentSettings.collectAsState()
    val uriHandler = LocalUriHandler.current
    var textSelection by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val postLinkUrl =
        post.url
            .orEmpty()
            .takeIf { !it.looksLikeAnImage && !it.looksLikeAVideo }
            .orEmpty()
    val shareHelper = remember { getShareHelper() }
    val clipboardManager = LocalClipboardManager.current
    val onShareLambda = {
        val query = clipboardManager.getText()?.text.orEmpty()
        shareHelper.share(query)
    }
    val shareActionLabel = LocalStrings.current.postActionShare
    val cancelActionLabel = LocalStrings.current.buttonCancel

    CompositionLocalProvider(
        LocalTextToolbar provides
            getCustomTextToolbar(
                shareActionLabel = shareActionLabel,
                cancelActionLabel = cancelActionLabel,
                onShare = onShareLambda,
                onCancel = {
                    focusManager.clearFocus(true)
                },
            ),
    ) {
        Column(
            modifier =
                modifier
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = Spacing.xs)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = {
                                if (textSelection) {
                                    focusManager.clearFocus()
                                    textSelection = false
                                } else {
                                    onClick?.invoke()
                                }
                            },
                        )
                    },
            verticalArrangement = Arrangement.spacedBy(Spacing.xxs),
        ) {
            CommunityAndCreatorInfo(
                modifier = Modifier.padding(horizontal = Spacing.xs),
                community = post.community,
                creator = post.creator.takeIf { !hideAuthor },
                featuredCommunity = post.featuredCommunity,
                featuredLocal = post.featuredLocal,
                locked = post.locked,
                markRead = markRead,
                compact = true,
                isFromModerator = isFromModerator,
                onOpenCommunity = { community ->
                    onOpenCommunity?.invoke(community, "")
                },
                onOpenCreator = { user ->
                    onOpenCreator?.invoke(user, "")
                },
                autoLoadImages = autoLoadImages,
                preferNicknames = preferNicknames,
                onDoubleClick = onDoubleClick,
            )
            Row(
                modifier = Modifier.padding(horizontal = Spacing.xs),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
            ) {
                if (post.deleted) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = LocalStrings.current.messageContentDeleted,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = ancillaryTextAlpha),
                    )
                } else {
                    Box(
                        modifier = Modifier.weight(COMPACT_POST_TITLE_WEIGHT),
                    ) {
                        CustomizedContent(ContentFontClass.Title) {
                            PostCardTitle(
                                modifier = Modifier.fillMaxWidth(),
                                text = post.title,
                                autoLoadImages = autoLoadImages,
                                markRead = markRead,
                                highlightText = highlightText,
                                onClick = {
                                    if (textSelection) {
                                        focusManager.clearFocus(true)
                                        textSelection = false
                                    } else {
                                        onClick?.invoke()
                                    }
                                },
                                onOpenImage = onOpenImage,
                                onDoubleClick = onDoubleClick,
                                onLongClick = {
                                    textSelection = true
                                },
                            )
                        }
                    }

                    if (post.videoUrl.isNotEmpty()) {
                        PostCardVideo(
                            modifier =
                                Modifier
                                    .weight(1 - COMPACT_POST_TITLE_WEIGHT)
                                    .padding(vertical = Spacing.xxs)
                                    .aspectRatio(1f),
                            url = post.videoUrl,
                            blurred = blurNsfw && post.nsfw,
                            autoLoadImages = autoLoadImages,
                            onOpen = {
                                if (postLinkUrl.isNotEmpty() && settings.openPostWebPageOnImageClick) {
                                    uriHandler.openUri(postLinkUrl)
                                } else {
                                    onClick?.invoke()
                                }
                            },
                            onOpenFullScreen = {
                                onOpenVideo?.invoke(post.videoUrl)
                            },
                        )
                    } else {
                        PostCardImage(
                            modifier =
                                Modifier
                                    .weight(1 - COMPACT_POST_TITLE_WEIGHT)
                                    .padding(vertical = Spacing.xs)
                                    .clip(RoundedCornerShape(CornerSize.l))
                                    .aspectRatio(1f),
                            imageUrl = post.imageUrl,
                            contentScale = ContentScale.Crop,
                            autoLoadImages = autoLoadImages,
                            minHeight = Dp.Unspecified,
                            loadButtonContent = @Composable {
                                Icon(
                                    imageVector = Icons.Default.Download,
                                    contentDescription = LocalStrings.current.actionDownload,
                                )
                            },
                            blurred = blurNsfw && post.nsfw,
                            onImageClick = { url ->
                                if (postLinkUrl.isNotEmpty() && settings.openPostWebPageOnImageClick) {
                                    uriHandler.openUri(postLinkUrl)
                                } else {
                                    onOpenImage?.invoke(url)
                                }
                            },
                            onDoubleClick = onDoubleClick,
                        )
                    }
                }
            }
            PostCardFooter(
                modifier =
                    Modifier.padding(
                        top = Spacing.xxs,
                        start = Spacing.xs,
                        end = Spacing.xs,
                    ),
                markRead = markRead,
                comments = post.comments,
                voteFormat = voteFormat,
                score = post.score,
                showScores = showScores,
                unreadComments =
                    post.unreadComments.takeIf {
                        it != null && it > 0 && showUnreadComments && it != post.comments
                    },
                upVotes = post.upvotes,
                downVotes = post.downvotes,
                upVoted = post.myVote > 0,
                downVoted = post.myVote < 0,
                saved = post.saved,
                downVoteEnabled = downVoteEnabled,
                onClick = onClick,
                onUpVote = onUpVote,
                onDownVote = onDownVote,
                onSave = onSave,
                onReply = onReply,
                publishDate = post.publishDate,
                updateDate = post.updateDate,
                options = options,
                onOptionSelected = onOptionSelected,
                actionButtonsActive = actionButtonsActive,
            )
        }
    }
}

@Composable
private fun ExtendedPost(
    modifier: Modifier = Modifier,
    post: PostModel,
    isFromModerator: Boolean,
    autoLoadImages: Boolean,
    preferNicknames: Boolean,
    showScores: Boolean,
    hideAuthor: Boolean,
    blurNsfw: Boolean,
    markRead: Boolean,
    highlightText: String?,
    voteFormat: VoteFormat = VoteFormat.Aggregated,
    showBody: Boolean,
    limitBodyHeight: Boolean,
    fullHeightImage: Boolean,
    fullWidthImage: Boolean,
    roundedCornerImage: Boolean,
    actionButtonsActive: Boolean,
    backgroundColor: Color,
    showUnreadComments: Boolean,
    downVoteEnabled: Boolean,
    options: List<Option>,
    onOpenCommunity: ((CommunityModel, String) -> Unit)?,
    onOpenCreator: ((UserModel, String) -> Unit)?,
    onUpVote: (() -> Unit)?,
    onDownVote: (() -> Unit)?,
    onSave: (() -> Unit)?,
    onReply: (() -> Unit)?,
    onOpenImage: ((String) -> Unit)?,
    onOpenVideo: ((String) -> Unit)?,
    onOptionSelected: ((OptionId) -> Unit)?,
    onClick: (() -> Unit)?,
    onDoubleClick: (() -> Unit)?,
) {
    val settingsRepository = remember { getSettingsRepository() }
    val settings by settingsRepository.currentSettings.collectAsState()
    val uriHandler = LocalUriHandler.current
    var textSelection by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val postLinkUrl =
        post.url
            .orEmpty()
            .takeIf {
                it != post.imageUrl &&
                    it != post.videoUrl &&
                    !it.looksLikeAnImage &&
                    !it.looksLikeAVideo
            }.orEmpty()
    val shareHelper = remember { getShareHelper() }
    val clipboardManager = LocalClipboardManager.current
    val onShareLambda = {
        val query = clipboardManager.getText()?.text.orEmpty()
        shareHelper.share(query)
    }
    val shareActionLabel = LocalStrings.current.postActionShare
    val cancelActionLabel = LocalStrings.current.buttonCancel

    CompositionLocalProvider(
        LocalTextToolbar provides
            getCustomTextToolbar(
                shareActionLabel = shareActionLabel,
                cancelActionLabel = cancelActionLabel,
                onShare = onShareLambda,
                onCancel = {
                    focusManager.clearFocus(true)
                },
            ),
    ) {
        Column(
            modifier =
                modifier
                    .background(backgroundColor)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = {
                                if (textSelection) {
                                    textSelection = false
                                } else {
                                    onClick?.invoke()
                                }
                            },
                        )
                    },
            verticalArrangement = Arrangement.spacedBy(Spacing.xxs),
        ) {
            CommunityAndCreatorInfo(
                modifier = Modifier.padding(horizontal = Spacing.s),
                community = post.community,
                creator = post.creator.takeIf { !hideAuthor },
                featuredCommunity = post.featuredCommunity,
                featuredLocal = post.featuredLocal,
                locked = post.locked,
                markRead = markRead,
                isFromModerator = isFromModerator,
                onOpenCommunity = { community ->
                    onOpenCommunity?.invoke(community, "")
                },
                onOpenCreator = { user ->
                    onOpenCreator?.invoke(user, "")
                },
                autoLoadImages = autoLoadImages,
                preferNicknames = preferNicknames,
                onDoubleClick = onDoubleClick,
            )
            if (post.deleted) {
                Text(
                    modifier =
                        Modifier.fillMaxWidth().padding(
                            all = Spacing.s,
                        ),
                    text = LocalStrings.current.messageContentDeleted,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = ancillaryTextAlpha),
                )
            } else {
                CustomizedContent(ContentFontClass.Title) {
                    PostCardTitle(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(
                                    vertical = Spacing.xs,
                                    horizontal = Spacing.s,
                                ),
                        text = post.title,
                        markRead = markRead,
                        highlightText = highlightText,
                        bolder = showBody,
                        autoLoadImages = autoLoadImages,
                        onClick = {
                            if (textSelection) {
                                focusManager.clearFocus(true)
                                textSelection = false
                            } else {
                                onClick?.invoke()
                            }
                        },
                        onOpenImage = onOpenImage,
                        onDoubleClick = onDoubleClick,
                        onLongClick = {
                            textSelection = true
                        },
                    )
                }

                if (post.videoUrl.isNotEmpty()) {
                    PostCardVideo(
                        modifier =
                            Modifier
                                .padding(
                                    vertical = Spacing.xxs,
                                    horizontal = if (fullWidthImage) 0.dp else Spacing.s,
                                ),
                        url = post.videoUrl,
                        blurred = blurNsfw && post.nsfw,
                        autoLoadImages = autoLoadImages,
                        onOpen = {
                            if (postLinkUrl.isNotEmpty() && settings.openPostWebPageOnImageClick) {
                                uriHandler.openUri(postLinkUrl)
                            } else {
                                onClick?.invoke()
                            }
                        },
                        onOpenFullScreen = {
                            onOpenVideo?.invoke(post.videoUrl)
                        },
                    )
                } else {
                    PostCardImage(
                        modifier =
                            Modifier
                                .padding(
                                    vertical = Spacing.xs,
                                    horizontal = if (fullWidthImage) 0.dp else Spacing.s,
                                ).then(
                                    if (roundedCornerImage && !fullWidthImage) {
                                        Modifier.clip(RoundedCornerShape(CornerSize.xl))
                                    } else {
                                        Modifier
                                    },
                                ),
                        imageUrl = post.imageUrl,
                        autoLoadImages = autoLoadImages,
                        blurred = blurNsfw && post.nsfw,
                        contentScale = if (fullHeightImage) ContentScale.FillWidth else ContentScale.Crop,
                        maxHeight = if (fullHeightImage) Dp.Unspecified else EXTENDED_POST_MAX_HEIGHT,
                        onImageClick = {
                            if (postLinkUrl.isNotEmpty() && settings.openPostWebPageOnImageClick) {
                                uriHandler.openUri(postLinkUrl)
                            } else {
                                val urlToOpen =
                                    post.url?.takeIf { it.looksLikeAnImage } ?: post.imageUrl
                                onOpenImage?.invoke(urlToOpen)
                            }
                        },
                        onDoubleClick = onDoubleClick,
                    )
                }

                if (showBody) {
                    if (post.removed) {
                        Text(
                            modifier = Modifier.padding(horizontal = Spacing.s),
                            text = LocalStrings.current.messageContentRemoved,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = ancillaryTextAlpha),
                        )
                    } else {
                        CustomizedContent(ContentFontClass.Body) {
                            PostCardBody(
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            top = Spacing.xxs,
                                            start = Spacing.s,
                                            end = Spacing.s,
                                        ),
                                text = post.text,
                                maxLines = settings.postBodyMaxLines.takeIf { limitBodyHeight },
                                autoLoadImages = autoLoadImages,
                                markRead = markRead,
                                highlightText = highlightText,
                                blurImages = blurNsfw && post.nsfw,
                                onClick = {
                                    if (textSelection) {
                                        focusManager.clearFocus(true)
                                        textSelection = false
                                    } else {
                                        onClick?.invoke()
                                    }
                                },
                                onOpenImage = onOpenImage,
                                onDoubleClick = onDoubleClick,
                                onLongClick = {
                                    textSelection = true
                                },
                            )
                        }
                    }
                }
                if (postLinkUrl.isNotEmpty()) {
                    PostLinkBanner(
                        modifier =
                            Modifier
                                .padding(
                                    top = Spacing.s,
                                    bottom = Spacing.xxs,
                                    start = Spacing.s,
                                    end = Spacing.s,
                                ),
                        url = postLinkUrl,
                        onClick = {
                            uriHandler.openUri(postLinkUrl)
                        },
                    )
                }
            }
            PostCardFooter(
                modifier =
                    Modifier.padding(
                        top = Spacing.xs,
                        start = Spacing.s,
                        end = Spacing.s,
                    ),
                markRead = markRead,
                comments = post.comments,
                voteFormat = voteFormat,
                score = post.score,
                showScores = showScores,
                unreadComments =
                    post.unreadComments.takeIf {
                        it != null && it > 0 && showUnreadComments && it != post.comments
                    },
                upVotes = post.upvotes,
                downVotes = post.downvotes,
                upVoted = post.myVote > 0,
                downVoted = post.myVote < 0,
                saved = post.saved,
                downVoteEnabled = downVoteEnabled,
                onClick = onClick,
                onUpVote = onUpVote,
                onDownVote = onDownVote,
                onSave = onSave,
                onReply = onReply,
                publishDate = post.publishDate,
                updateDate = post.updateDate,
                options = options,
                onOptionSelected = onOptionSelected,
                actionButtonsActive = actionButtonsActive,
            )
        }
    }
}

private const val COMPACT_POST_TITLE_WEIGHT = 0.85f
private val EXTENDED_POST_MAX_HEIGHT = 200.dp
