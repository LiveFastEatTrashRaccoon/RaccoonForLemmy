package com.github.diegoberaldin.raccoonforlemmy

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.data.ThemeState
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.data.toInt
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.data.toPostLayout
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.data.toThemeState
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.di.getThemeRepository
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.AppTheme
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.CornerSize
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.Spacing
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.communitydetail.CommunityDetailScreen
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.components.getCommmunityFromUrl
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.components.getPostFromUrl
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.components.getUserFromUrl
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.di.getNavigationCoordinator
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.postdetail.PostDetailScreen
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.userdetail.UserDetailScreen
import com.github.diegoberaldin.raccoonforlemmy.core.persistence.di.getAccountRepository
import com.github.diegoberaldin.raccoonforlemmy.core.persistence.di.getSettingsRepository
import com.github.diegoberaldin.raccoonforlemmy.domain.identity.di.getApiConfigurationRepository
import com.github.diegoberaldin.raccoonforlemmy.resources.MR
import com.github.diegoberaldin.raccoonforlemmy.resources.di.getLanguageRepository
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource
import dev.icerock.moko.resources.desc.StringDesc
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun App() {
    val accountRepository = remember { getAccountRepository() }
    val settingsRepository = remember { getSettingsRepository() }
    val settings by settingsRepository.currentSettings.collectAsState()
    var hasBeenInitialized by remember { mutableStateOf(false) }
    LaunchedEffect(settingsRepository) {
        val accountId = accountRepository.getActive()?.id
        val currentSettings = settingsRepository.getSettings(accountId)
        settingsRepository.changeCurrentSettings(currentSettings)
        hasBeenInitialized = true
    }
    val defaultTheme = if (isSystemInDarkTheme()) {
        ThemeState.Dark.toInt()
    } else {
        ThemeState.Light.toInt()
    }

    val defaultLocale = stringResource(MR.strings.lang)
    val languageRepository = remember { getLanguageRepository() }
    val locale by derivedStateOf { settings.locale }
    LaunchedEffect(locale) {
        languageRepository.changeLanguage(locale ?: defaultLocale)
    }
    val scope = rememberCoroutineScope()
    languageRepository.currentLanguage.onEach { lang ->
        StringDesc.localeType = StringDesc.LocaleType.Custom(lang)
    }.launchIn(scope)

    val apiConfigurationRepository = remember { getApiConfigurationRepository() }
    LaunchedEffect(Unit) {
        val lastActiveAccount = accountRepository.getActive()
        val lastInstance = lastActiveAccount?.instance
        if (lastInstance != null) {
            apiConfigurationRepository.changeInstance(lastInstance)
        }
    }

    val themeRepository = remember { getThemeRepository() }
    LaunchedEffect(settings) {
        with(themeRepository) {
            changeTheme((settings.theme ?: defaultTheme).toThemeState())
            changeNavItemTitles(settings.navigationTitlesVisible)
            changeDynamicColors(settings.dynamicColors)
            changeCustomSeedColor(settings.customSeedColor?.let { Color(it) })
            changePostLayout(settings.postLayout.toPostLayout())
            changeContentFontScale(settings.contentFontScale)
            changeUiFontScale(settings.uiFontScale)
        }
    }
    val currentTheme by themeRepository.state.collectAsState()
    val useDynamicColors by themeRepository.dynamicColors.collectAsState()
    val fontScale by themeRepository.contentFontScale.collectAsState()
    val uiFontScale by themeRepository.uiFontScale.collectAsState()
    val navigationCoordinator = remember { getNavigationCoordinator() }

    AppTheme(
        theme = currentTheme,
        contentFontScale = fontScale,
        useDynamicColors = useDynamicColors,
    ) {
        val lang by languageRepository.currentLanguage.collectAsState()
        LaunchedEffect(lang) {}

        CompositionLocalProvider(
            LocalDensity provides Density(
                density = LocalDensity.current.density,
                fontScale = uiFontScale,
            ),
        ) {
            BottomSheetNavigator(
                sheetShape = RoundedCornerShape(topStart = CornerSize.xl, topEnd = CornerSize.xl),
                sheetBackgroundColor = MaterialTheme.colorScheme.background,
            ) {
                val screens: List<Screen> = remember(navigationCoordinator.deeplinkUrl) {
                    val url = navigationCoordinator.deeplinkUrl.value.orEmpty()
                    val community = getCommmunityFromUrl(url)
                    val user = getUserFromUrl(url)
                    val postAndInstance = getPostFromUrl(url)
                    buildList {
                        if (community != null) {
                            add(
                                CommunityDetailScreen(
                                    community = community,
                                    otherInstance = community.host,
                                )
                            )
                        } else if (user != null) {
                            add(
                                UserDetailScreen(
                                    user = user,
                                    otherInstance = user.host,
                                )
                            )
                        } else if (postAndInstance != null) {
                            val (post, otherInstance) = postAndInstance
                            add(
                                PostDetailScreen(
                                    post = post,
                                    otherInstance = otherInstance,
                                )
                            )
                        } else {
                            add(MainScreen())
                        }
                    }
                }
                Navigator(
                    screens = screens,
                    onBackPressed = {
                        val callback = navigationCoordinator.getCanGoBackCallback()
                        callback?.let { it() } ?: true
                    }
                ) {
                    val navigator = LocalNavigator.current
                    navigationCoordinator.setRootNavigator(navigator)
                    if (hasBeenInitialized) {
                        CurrentScreen()
                    } else {
                        // loading screen
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center,
                        ) {
                            Column(
                                modifier = Modifier.padding(top = 24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(Spacing.s),
                            ) {
                                Image(
                                    painter = painterResource(MR.images.icon),
                                    contentDescription = null,
                                )
                                CircularProgressIndicator(
                                    color = MaterialTheme.colorScheme.onPrimary,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
