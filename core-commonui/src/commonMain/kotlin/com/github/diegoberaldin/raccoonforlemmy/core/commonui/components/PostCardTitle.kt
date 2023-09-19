package com.github.diegoberaldin.raccoonforlemmy.core.commonui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.di.getNavigationCoordinator
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.web.WebViewScreen
import com.github.diegoberaldin.raccoonforlemmy.core.markdown.compose.Markdown
import com.github.diegoberaldin.raccoonforlemmy.core.markdown.model.markdownColor
import com.github.diegoberaldin.raccoonforlemmy.core.markdown.model.markdownTypography
import com.github.diegoberaldin.raccoonforlemmy.core.preferences.KeyStoreKeys
import com.github.diegoberaldin.raccoonforlemmy.core.preferences.di.getTemporaryKeyStore

@Composable
fun PostCardTitle(
    text: String,
    modifier: Modifier = Modifier,
) {
    val uriHandler = LocalUriHandler.current
    val navigator = remember { getNavigationCoordinator().getRootNavigator() }
    val keyStore = remember { getTemporaryKeyStore() }

    Markdown(
        modifier = modifier,
        content = text,
        typography = markdownTypography(
            h1 = MaterialTheme.typography.headlineLarge,
            h2 = MaterialTheme.typography.headlineMedium,
            h3 = MaterialTheme.typography.headlineSmall,
            h4 = MaterialTheme.typography.titleLarge,
            h5 = MaterialTheme.typography.titleMedium,
            h6 = MaterialTheme.typography.titleSmall,
            text = MaterialTheme.typography.bodyMedium,
            paragraph = MaterialTheme.typography.bodyMedium,
        ),
        colors = markdownColor(
            text = MaterialTheme.colorScheme.onSurfaceVariant,
            backgroundCode = MaterialTheme.colorScheme.surfaceVariant,
        ),
        onOpenUrl = { url ->
            val openExternal = keyStore[KeyStoreKeys.OpenUrlsInExternalBrowser, false]
            if (openExternal) {
                uriHandler.openUri(url)
            } else {
                navigator?.push(WebViewScreen(url))
            }
        }
    )
}
