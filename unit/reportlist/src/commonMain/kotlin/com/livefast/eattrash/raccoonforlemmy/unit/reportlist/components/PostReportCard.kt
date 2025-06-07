package com.livefast.eattrash.raccoonforlemmy.unit.reportlist.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalUriHandler
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.PostLayout
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.CornerSize
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.Option
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.OptionId
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.PostCardBody
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.PostCardImage
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.PostCardTitle
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.PostLinkBanner
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.PostReportModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.imageUrl

@Composable
internal fun PostReportCard(
    report: PostReportModel,
    modifier: Modifier = Modifier,
    postLayout: PostLayout = PostLayout.Card,
    autoLoadImages: Boolean = true,
    preferNicknames: Boolean = true,
    onOpen: (() -> Unit)? = null,
    options: List<Option> = emptyList(),
    onSelectOption: ((OptionId) -> Unit)? = null,
) {
    val uriHandler = LocalUriHandler.current

    InnerReportCard(
        modifier = modifier,
        reason = report.reason.orEmpty(),
        postLayout = postLayout,
        creator = report.creator,
        date = report.publishDate,
        autoLoadImages = autoLoadImages,
        preferNicknames = preferNicknames,
        options = options,
        onSelectOption = onSelectOption,
        onOpen = onOpen,
        originalContent = {
            Column(
                verticalArrangement = Arrangement.spacedBy(Spacing.xxs),
            ) {
                report.originalTitle?.also { title ->
                    PostCardTitle(
                        modifier =
                        Modifier.padding(
                            vertical = Spacing.xs,
                            horizontal = Spacing.xs,
                        ),
                        text = title,
                        autoLoadImages = autoLoadImages,
                    )
                }
                report.imageUrl.takeIf { it.isNotEmpty() }?.also { imageUrl ->
                    PostCardImage(
                        modifier =
                        Modifier
                            .padding(vertical = Spacing.xs)
                            .clip(RoundedCornerShape(CornerSize.xl)),
                        imageUrl = imageUrl,
                        autoLoadImages = autoLoadImages,
                    )
                }
                report.originalText?.also { text ->
                    PostCardBody(
                        modifier =
                        Modifier.padding(
                            vertical = Spacing.xs,
                            horizontal = Spacing.xs,
                        ),
                        text = text,
                        autoLoadImages = autoLoadImages,
                    )
                }
                report.originalUrl?.also { url ->
                    PostLinkBanner(
                        modifier = Modifier.padding(top = Spacing.s, bottom = Spacing.xxs),
                        url = url,
                        onClick = {
                            uriHandler.openUri(url)
                        },
                    )
                }
            }
        },
    )
}
