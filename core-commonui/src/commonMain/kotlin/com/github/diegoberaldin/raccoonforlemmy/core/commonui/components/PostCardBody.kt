package com.github.diegoberaldin.raccoonforlemmy.core.commonui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.di.getNavigationCoordinator
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.image.ZoomableImageScreen
import com.github.diegoberaldin.raccoonforlemmy.core.markdown.compose.CustomMarkdown
import com.github.diegoberaldin.raccoonforlemmy.core.persistence.di.getSettingsRepository

@Composable
fun PostCardBody(
    modifier: Modifier = Modifier,
    text: String,
    autoLoadImages: Boolean = true,
) {
    val uriHandler = LocalUriHandler.current
    val navigator = remember { getNavigationCoordinator().getRootNavigator() }
    val settingsRepository = remember { getSettingsRepository() }
    val settings by settingsRepository.currentSettings.collectAsState()
    val openExternal = settings.openUrlsInExternalBrowser

    if (text.isNotEmpty()) {
        CustomMarkdown(
            modifier = modifier,
            content = text,
            inlineImages = false,
            autoLoadImages = autoLoadImages,
            onOpenUrl = { url ->
                handleUrl(
                    url = url,
                    openExternal = openExternal,
                    uriHandler = uriHandler,
                    navigator = navigator
                )
            },
            onOpenImage = { url ->
                navigator?.push(ZoomableImageScreen(url))
            },
        )
    }
}
