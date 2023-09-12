package com.github.diegoberaldin.raccoonforlemmy.core.commonui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun CustomWebView(
    navigator: WebViewNavigator = rememberWebViewNavigator(),
    modifier: Modifier = Modifier,
    url: String,
)
