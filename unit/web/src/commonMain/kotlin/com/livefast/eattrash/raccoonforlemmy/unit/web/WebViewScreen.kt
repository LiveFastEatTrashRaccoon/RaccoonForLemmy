package com.livefast.eattrash.raccoonforlemmy.unit.web

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.CustomWebView
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.rememberWebViewNavigator
import com.livefast.eattrash.raccoonforlemmy.core.navigation.di.getDrawerCoordinator
import com.livefast.eattrash.raccoonforlemmy.core.navigation.di.getNavigationCoordinator
import com.livefast.eattrash.raccoonforlemmy.core.utils.compose.onClick
import com.livefast.eattrash.raccoonforlemmy.core.utils.md5
import com.livefast.eattrash.raccoonforlemmy.core.utils.share.getShareHelper

class WebViewScreen(
    private val url: String,
) : Screen {
    override val key: ScreenKey
        get() = super.key + url.md5()

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigationCoordinator = remember { getNavigationCoordinator() }
        val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
        val shareHelper = remember { getShareHelper() }
        val drawerCoordinator = remember { getDrawerCoordinator() }

        LaunchedEffect(key) {
            drawerCoordinator.setGesturesEnabled(false)
        }
        DisposableEffect(key) {
            onDispose {
                drawerCoordinator.setGesturesEnabled(true)
            }
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {},
                    scrollBehavior = scrollBehavior,
                    navigationIcon = {
                        Image(
                            modifier =
                                Modifier.onClick(
                                    onClick = {
                                        navigationCoordinator.popScreen()
                                    },
                                ),
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
                        )
                    },
                    actions = {
                        Icon(
                            modifier =
                                Modifier
                                    .padding(horizontal = Spacing.xs)
                                    .onClick(
                                        onClick = {
                                            shareHelper.share(url)
                                        },
                                    ),
                            imageVector = Icons.Default.Share,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onBackground,
                        )
                    },
                )
            },
        ) { padding ->
            Box(
                modifier = Modifier.padding(
                    top = padding.calculateTopPadding(),
                ),
            ) {
                val webNavigator = rememberWebViewNavigator()

                DisposableEffect(key) {
                    navigationCoordinator.setCanGoBackCallback {
                        val result = webNavigator.canGoBack
                        if (result) {
                            webNavigator.goBack()
                            return@setCanGoBackCallback false
                        }
                        true
                    }
                    onDispose {
                        navigationCoordinator.setCanGoBackCallback(null)
                    }
                }

                CustomWebView(
                    modifier = Modifier.fillMaxSize(),
                    navigator = webNavigator,
                    scrollConnection = scrollBehavior.nestedScrollConnection,
                    url = url,
                )
            }
        }
    }
}
