package com.livefast.eattrash.raccoonforlemmy

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DrawerDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.toSize
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.TabNavigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.toColor
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.toCommentBarTheme
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.toPostLayout
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.toUiBarTheme
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.toUiFontFamily
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.toUiTheme
import com.livefast.eattrash.raccoonforlemmy.core.appearance.di.getAppColorRepository
import com.livefast.eattrash.raccoonforlemmy.core.appearance.di.getThemeRepository
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.AppTheme
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.DraggableSideMenu
import com.livefast.eattrash.raccoonforlemmy.core.commonui.detailopener.api.getDetailOpener
import com.livefast.eattrash.raccoonforlemmy.core.di.RootDI
import com.livefast.eattrash.raccoonforlemmy.core.l10n.ProvideStrings
import com.livefast.eattrash.raccoonforlemmy.core.l10n.di.getL10nManager
import com.livefast.eattrash.raccoonforlemmy.core.navigation.ComposeEvent
import com.livefast.eattrash.raccoonforlemmy.core.navigation.DrawerEvent
import com.livefast.eattrash.raccoonforlemmy.core.navigation.SideMenuEvents
import com.livefast.eattrash.raccoonforlemmy.core.navigation.di.getBottomNavItemsRepository
import com.livefast.eattrash.raccoonforlemmy.core.navigation.di.getDrawerCoordinator
import com.livefast.eattrash.raccoonforlemmy.core.navigation.di.getNavigationCoordinator
import com.livefast.eattrash.raccoonforlemmy.core.navigation.toInts
import com.livefast.eattrash.raccoonforlemmy.core.persistence.di.getAccountRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.di.getSettingsRepository
import com.livefast.eattrash.raccoonforlemmy.core.preferences.di.getAppConfigStore
import com.livefast.eattrash.raccoonforlemmy.core.utils.compose.onClick
import com.livefast.eattrash.raccoonforlemmy.core.utils.toLocalDp
import com.livefast.eattrash.raccoonforlemmy.domain.identity.di.getApiConfigurationRepository
import com.livefast.eattrash.raccoonforlemmy.domain.identity.di.getCustomUriHandler
import com.livefast.eattrash.raccoonforlemmy.domain.identity.urlhandler.ProvideCustomUriHandler
import com.livefast.eattrash.raccoonforlemmy.main.MainScreen
import com.livefast.eattrash.raccoonforlemmy.unit.drawer.content.ModalDrawerContent
import com.livefast.eattrash.raccoonforlemmy.unit.drawer.di.getSubscriptionsCache
import com.livefast.eattrash.raccoonforlemmy.unit.multicommunity.detail.MultiCommunityScreen
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.kodein.di.compose.withDI

@OptIn(FlowPreview::class)
@Composable
fun App(onLoadingFinished: () -> Unit = {}) = withDI(RootDI.di) {
    val accountRepository = remember { getAccountRepository() }
    val settingsRepository = remember { getSettingsRepository() }
    val appColorRepository = remember { getAppColorRepository() }
    val settings by settingsRepository.currentSettings.collectAsState()
    var hasBeenInitialized by remember { mutableStateOf(false) }
    val apiConfigurationRepository = remember { getApiConfigurationRepository() }
    val themeRepository = remember { getThemeRepository() }
    val useDynamicColors by themeRepository.dynamicColors.collectAsState()
    val uiFontScale by themeRepository.uiFontScale.collectAsState()
    val navigationCoordinator = remember { getNavigationCoordinator() }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val drawerCoordinator = remember { getDrawerCoordinator() }
    val drawerGesturesEnabled by drawerCoordinator.gesturesEnabled.collectAsState()
    val detailOpener = remember { getDetailOpener() }
    val l10nManager = remember { getL10nManager() }
    val langState by l10nManager.lang.collectAsState()
    var screenWidth by remember { mutableStateOf(0f) }
    var sideMenuContent by remember { mutableStateOf<@Composable (() -> Unit)?>(null) }
    val sideMenuOpened by navigationCoordinator.sideMenuOpened.collectAsState()
    val scope = rememberCoroutineScope()
    val subscriptionsCache = remember { getSubscriptionsCache() }
    val appConfigStore = remember { getAppConfigStore() }
    val bottomNavItemsRepository = remember { getBottomNavItemsRepository() }
    val fallbackUriHandler = LocalUriHandler.current
    val customUriHandler = remember { getCustomUriHandler(fallbackUriHandler) }

    LaunchedEffect(Unit) {
        val lastActiveAccount = accountRepository.getActive()
        val lastInstance = lastActiveAccount?.instance?.takeIf { it.isNotEmpty() }
        val accountId = lastActiveAccount?.id
        val currentSettings = settingsRepository.getSettings(accountId)
        val seedColor =
            if (currentSettings.randomThemeColor) {
                appColorRepository
                    .getRandomColor()
                    .toColor()
                    .toArgb()
            } else {
                currentSettings.customSeedColor
            }
        settingsRepository.changeCurrentSettings(currentSettings.copy(customSeedColor = seedColor))
        val bottomBarSections = bottomNavItemsRepository.get(accountId)
        settingsRepository.changeCurrentBottomBarSections(bottomBarSections.toInts())
        if (lastInstance != null) {
            apiConfigurationRepository.changeInstance(lastInstance)
        }

        subscriptionsCache.initialize()
        appConfigStore.initialize()

        hasBeenInitialized = true

        launch {
            delay(50)
            onLoadingFinished()
        }
    }

    LaunchedEffect(settings) {
        l10nManager.changeLanguage(settings.locale ?: "en")
        with(themeRepository) {
            changeUiTheme(settings.theme.toUiTheme())
            changeNavItemTitles(settings.navigationTitlesVisible)
            changeDynamicColors(settings.dynamicColors)
            changeCustomSeedColor(settings.customSeedColor?.let { Color(it) })
            changePostLayout(settings.postLayout.toPostLayout())
            changeContentFontScale(settings.contentFontScale)
            changeUiFontScale(settings.uiFontScale)
            changeUiFontFamily(settings.uiFontFamily.toUiFontFamily())
            changeContentFontFamily(settings.contentFontFamily.toUiFontFamily())
            changeUpVoteColor(settings.upVoteColor?.let { Color(it) })
            changeDownVoteColor(settings.downVoteColor?.let { Color(it) })
            changeReplyColor(settings.replyColor?.let { Color(it) })
            changeSaveColor(settings.saveColor?.let { Color(it) })
            changeCommentBarTheme(settings.commentBarTheme.toCommentBarTheme())
        }
    }

    LaunchedEffect(navigationCoordinator) {
        navigationCoordinator.deepLinkUrl
            .debounce(750)
            .filterNotNull()
            .onEach { uri ->
                customUriHandler.openUri(uri = uri, allowOpenExternal = false)
            }.launchIn(this)
        navigationCoordinator.composeEvents
            .debounce(750)
            .onEach { event ->
                when (event) {
                    is ComposeEvent.WithText ->
                        detailOpener.openCreatePost(
                            initialText = event.text,
                            forceCommunitySelection = true,
                        )

                    is ComposeEvent.WithUrl ->
                        detailOpener.openCreatePost(
                            initialUrl = event.url,
                            forceCommunitySelection = true,
                        )

                    else -> Unit
                }
            }.launchIn(this)
        navigationCoordinator.sideMenuEvents
            .onEach { evt ->
                when (evt) {
                    is SideMenuEvents.Open -> {
                        sideMenuContent = @Composable {
                            evt.screen.Content()
                        }
                    }

                    SideMenuEvents.Close -> {
                        delay(250)
                        sideMenuContent = null
                    }
                }
            }.launchIn(this)
    }

    LaunchedEffect(drawerCoordinator) {
        // centralizes the information about drawer opening
        snapshotFlow {
            drawerState.isClosed
        }.onEach { closed ->
            drawerCoordinator.changeDrawerOpened(!closed)
        }.launchIn(this)

        drawerCoordinator.events
            .onEach { evt ->
                when (evt) {
                    DrawerEvent.Toggle -> {
                        drawerState.apply {
                            launch {
                                if (isClosed) {
                                    open()
                                } else {
                                    close()
                                }
                            }
                        }
                    }

                    DrawerEvent.Close -> {
                        drawerState.apply {
                            launch {
                                if (!isClosed) {
                                    close()
                                }
                            }
                        }
                    }

                    is DrawerEvent.OpenCommunity -> {
                        detailOpener.openCommunityDetail(community = evt.community)
                    }

                    is DrawerEvent.OpenMultiCommunity -> {
                        evt.community.id?.also {
                            navigationCoordinator.pushScreen(MultiCommunityScreen(it))
                        }
                    }

                    else -> Unit
                }
            }.launchIn(this)
    }

    AppTheme(
        useDynamicColors = useDynamicColors,
        barTheme = settings.systemBarTheme.toUiBarTheme(),
    ) {
        ProvideStrings(
            lang = langState,
        ) {
            ProvideCustomUriHandler {
                CompositionLocalProvider(
                    LocalDensity provides
                        Density(
                            density = LocalDensity.current.density,
                            fontScale = uiFontScale,
                        ),
                ) {
                    Navigator(
                        screen = MainScreen,
                        onBackPressed = {
                            // if the drawer is open, closes it
                            if (drawerCoordinator.drawerOpened.value) {
                                scope.launch {
                                    drawerCoordinator.toggleDrawer()
                                }
                                return@Navigator false
                            }
                            // if the side menu is open, closes it
                            if (navigationCoordinator.sideMenuOpened.value) {
                                navigationCoordinator.closeSideMenu()
                                return@Navigator false
                            }

                            // otherwise use the screen-provided callback
                            val callback = navigationCoordinator.getCanGoBackCallback()
                            callback?.let { it() } ?: true
                        },
                    ) { navigator ->
                        LaunchedEffect(Unit) {
                            navigationCoordinator.setRootNavigator(navigator)
                        }

                        ModalNavigationDrawer(
                            modifier =
                                Modifier
                                    .fillMaxSize()
                                    .onGloballyPositioned {
                                        screenWidth = it.size.toSize().width
                                    },
                            drawerState = drawerState,
                            gesturesEnabled = drawerGesturesEnabled,
                            drawerContent = {
                                ModalDrawerSheet {
                                    TabNavigator(ModalDrawerContent)
                                }
                            },
                        ) {
                            if (hasBeenInitialized) {
                                SlideTransition(
                                    animationSpec =
                                        tween(
                                            durationMillis = 250,
                                            easing = FastOutSlowInEasing,
                                        ),
                                    navigator = navigator,
                                )
                            }
                        }

                        // scrim for draggable side menu
                        AnimatedVisibility(
                            modifier = Modifier.fillMaxSize(),
                            visible = sideMenuOpened,
                        ) {
                            Surface(
                                modifier =
                                    Modifier
                                        .onClick(
                                            onClick = {
                                                navigationCoordinator.closeSideMenu()
                                            },
                                        ),
                                color = DrawerDefaults.scrimColor,
                            ) {
                                Box(modifier = Modifier.fillMaxSize())
                            }
                        }

                        // draggable side menu
                        DraggableSideMenu(
                            availableWidth = screenWidth.toLocalDp(),
                            opened = sideMenuOpened,
                            onDismiss = {
                                navigationCoordinator.closeSideMenu()
                            },
                            content = {
                                sideMenuContent?.invoke()
                            },
                        )
                    }
                }
            }
        }
    }
}
