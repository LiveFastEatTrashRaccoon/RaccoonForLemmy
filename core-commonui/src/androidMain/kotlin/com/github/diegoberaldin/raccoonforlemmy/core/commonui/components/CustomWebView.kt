package com.github.diegoberaldin.raccoonforlemmy.core.commonui.components

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@SuppressLint("SetJavaScriptEnabled")
@Composable
actual fun CustomWebView(
    navigator: WebViewNavigator,
    modifier: Modifier,
    url: String,
) {
    var webView: WebView? = null

    LaunchedEffect(true) {
        navigator.events.onEach {
            when (it) {
                WebViewNavigationEvent.GoBack -> webView?.goBack()
            }
        }.launchIn(this)
    }

    AndroidView(
        modifier = modifier,
        factory = { context ->
            WebView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                webViewClient = object : WebViewClient() {
                    override fun onPageStarted(view: WebView, url: String?, favicon: Bitmap?) {
                        navigator.canGoBack = view.canGoBack()
                    }
                }
                settings.javaScriptEnabled = true

                loadUrl(url)
                webView = this
            }
        },
        update = {
            webView = it
        },
    )
}
