package com.livefast.eattrash.raccoonforlemmy.domain.identity.urlhandler

import androidx.compose.ui.platform.UriHandler
import com.livefast.eattrash.raccoonforlemmy.core.commonui.detailopener.api.DetailOpener
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.SettingsRepository
import com.livefast.eattrash.raccoonforlemmy.core.utils.url.CustomTabsHelper
import com.livefast.eattrash.raccoonforlemmy.core.utils.url.UrlOpeningMode
import com.livefast.eattrash.raccoonforlemmy.core.utils.url.toUrlOpeningMode
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

internal class DefaultCustomUriHandler(
    private val fallbackHandler: UriHandler,
    private val settingsRepository: SettingsRepository,
    private val communityProcessor: CommunityProcessor,
    private val userProcessor: UserProcessor,
    private val postProcessor: PostProcessor,
    private val commentProcessor: CommentProcessor,
    private val detailOpener: DetailOpener,
    private val customTabsHelper: CustomTabsHelper,
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : CustomUriHandler {
    private val scope = CoroutineScope(SupervisorJob() + dispatcher)

    override fun openUri(
        uri: String,
        allowOpenExternal: Boolean,
    ) {
        scope.launch {
            val processors =
                listOf(
                    communityProcessor,
                    userProcessor,
                    postProcessor,
                    commentProcessor,
                )
            val openedInternally =
                processors.fold(false) { acc, processor ->
                    acc || processor.process(uri)
                }

            if (!openedInternally && allowOpenExternal) {
                val settings = settingsRepository.currentSettings.value
                val openingMode = settings.urlOpeningMode.toUrlOpeningMode()
                when {
                    openingMode == UrlOpeningMode.Internal && allowOpenExternal ->
                        detailOpener.openWebInternal(uri)

                    openingMode == UrlOpeningMode.CustomTabs && allowOpenExternal ->
                        runCatching { customTabsHelper.handle(uri) }

                    allowOpenExternal -> fallbackHandler.openUri(uri)

                    else -> Unit
                }
            }
        }
    }

    override fun openUri(uri: String) {
        openUri(uri = uri, allowOpenExternal = true)
    }
}
