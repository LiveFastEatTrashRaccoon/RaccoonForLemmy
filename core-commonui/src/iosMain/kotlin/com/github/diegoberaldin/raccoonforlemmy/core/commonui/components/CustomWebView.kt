package com.github.diegoberaldin.raccoonforlemmy.core.commonui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import kotlinx.cinterop.readValue
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import platform.CoreGraphics.CGRectZero
import platform.WebKit.WKNavigation
import platform.WebKit.WKNavigationDelegateProtocol
import platform.WebKit.WKWebView
import platform.WebKit.WKWebViewConfiguration
import platform.darwin.NSObject

@Composable
actual fun CustomWebView(
    navigator: WebViewNavigator,
    modifier: Modifier,
    url: String,
) {
    var webView: WKWebView? = null

    LaunchedEffect(true) {
        navigator.events.onEach {
            when (it) {
                WebViewNavigationEvent.GoBack -> webView?.goBack()
            }
        }.launchIn(this)
    }

    UIKitView(
        factory = {
            val config = WKWebViewConfiguration().apply {
                allowsInlineMediaPlayback = true
            }
            WKWebView(
                frame = CGRectZero.readValue(),
                configuration = config
            ).apply {
                userInteractionEnabled = true
                allowsBackForwardNavigationGestures = true
                val navigationDelegate = object : NSObject(), WKNavigationDelegateProtocol {

                    override fun webView(
                        webView: WKWebView,
                        didFinishNavigation: WKNavigation?,
                    ) {
                        navigator.canGoBack = webView.canGoBack
                    }
                }
                this.navigationDelegate = navigationDelegate
            }.also {
                webView = it
            }
        },
        modifier = modifier,
        onRelease = {
            webView = null
        }
    )
}
