package com.livefast.eattrash.raccoonforlemmy.unit.web

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.navigation.di.rememberDrawerCoordinator
import com.livefast.eattrash.raccoonforlemmy.core.navigation.di.rememberNavigationCoordinator
import com.livefast.eattrash.raccoonforlemmy.core.resources.LocalResources
import com.livefast.eattrash.raccoonforlemmy.core.utils.compose.onClick
import com.livefast.eattrash.raccoonforlemmy.core.utils.di.rememberShareHelper
import com.mohamedrejeb.calf.ui.web.WebView
import com.mohamedrejeb.calf.ui.web.rememberWebViewState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WebViewScreen(url: String, modifier: Modifier = Modifier) {
    val navigationCoordinator = rememberNavigationCoordinator()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val shareHelper = rememberShareHelper()
    val drawerCoordinator = rememberDrawerCoordinator()
    val state =
        rememberWebViewState(
            url = url,
        )

    LaunchedEffect(Unit) {
        state.settings.javaScriptEnabled = true
        state.settings.androidSettings.supportZoom = true
    }
    DisposableEffect(Unit) {
        drawerCoordinator.setGesturesEnabled(false)

        onDispose {
            drawerCoordinator.setGesturesEnabled(true)
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {},
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navigationCoordinator.pop()
                        },
                    ) {
                        Icon(
                            imageVector = LocalResources.current.arrowBack,
                            contentDescription = LocalStrings.current.actionGoBack,
                        )
                    }
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
                        imageVector = LocalResources.current.share,
                        contentDescription = LocalStrings.current.postActionShare,
                        tint = MaterialTheme.colorScheme.onBackground,
                    )
                },
            )
        },
    ) { padding ->
        WebView(
            modifier = Modifier.padding(top = padding.calculateTopPadding()).fillMaxSize(),
            state = state,
        )
    }
}
