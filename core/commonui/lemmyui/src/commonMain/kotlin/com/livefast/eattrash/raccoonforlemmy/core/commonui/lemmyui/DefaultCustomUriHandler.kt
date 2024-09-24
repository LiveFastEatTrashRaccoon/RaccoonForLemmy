package com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui

import androidx.compose.ui.platform.UriHandler
import com.livefast.eattrash.raccoonforlemmy.core.commonui.detailopener.api.DetailOpener
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.SettingsRepository
import com.livefast.eattrash.raccoonforlemmy.core.utils.looksLikeAVideo
import com.livefast.eattrash.raccoonforlemmy.core.utils.looksLikeAnImage
import com.livefast.eattrash.raccoonforlemmy.core.utils.url.CustomTabsHelper
import com.livefast.eattrash.raccoonforlemmy.core.utils.url.UrlOpeningMode
import com.livefast.eattrash.raccoonforlemmy.core.utils.url.toUrlOpeningMode

internal class DefaultCustomUriHandler(
    private val fallbackHandler: UriHandler,
    private val settingsRepository: SettingsRepository,
    private val detailOpener: DetailOpener,
    private val customTabsHelper: CustomTabsHelper,
) : CustomUriHandler {
    override fun openUri(uri: String) {
        val community = getCommunityFromUrl(uri)
        val user = getUserFromUrl(uri)
        val (post, postInstance) = getPostFromUrl(uri) ?: (null to null)
        val isMedia = uri.looksLikeAVideo || uri.looksLikeAnImage
        val openingMode =
            settingsRepository.currentSettings.value.urlOpeningMode
                .toUrlOpeningMode()

        when {
            community != null && !isMedia -> {
                detailOpener.openCommunityDetail(community, community.host)
            }

            user != null && !isMedia -> {
                detailOpener.openUserDetail(user, user.host)
            }

            post != null && !isMedia -> {
                detailOpener.openPostDetail(post, postInstance.orEmpty())
            }

            openingMode == UrlOpeningMode.Internal -> {
                detailOpener.openWebInternal(uri)
            }

            openingMode == UrlOpeningMode.CustomTabs -> {
                runCatching {
                    customTabsHelper.handle(uri)
                }.also {
                    it.exceptionOrNull()?.also { e ->
                        e.printStackTrace()
                    }
                }
            }

            else -> {
                fallbackHandler.openUri(uri)
            }
        }
    }
}
