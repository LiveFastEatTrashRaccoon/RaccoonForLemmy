package com.livefast.eattrash.raccoonforlemmy.android

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.livefast.eattrash.raccoonforlemmy.MainView
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.toUiBarTheme
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.toUiTheme
import com.livefast.eattrash.raccoonforlemmy.core.appearance.di.getBarColorProvider
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.SolidBarColorWorkaround
import com.livefast.eattrash.raccoonforlemmy.core.navigation.ComposeEvent
import com.livefast.eattrash.raccoonforlemmy.core.navigation.TabNavigationSection
import com.livefast.eattrash.raccoonforlemmy.core.navigation.di.getNavigationCoordinator
import com.livefast.eattrash.raccoonforlemmy.core.persistence.di.getAccountRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.di.getSettingsRepository
import com.livefast.eattrash.raccoonforlemmy.feature.home.ui.HomeTab
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        var loadingFinished = false
        installSplashScreen().setKeepOnScreenCondition {
            !loadingFinished
        }
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        // manage exit confirmation
        val navigationCoordinator = getNavigationCoordinator()
        val backPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    // if in home, ask for confirmation
                    if (navigationCoordinator.currentSection.value == TabNavigationSection.Home) {
                        // asks for confirmation
                        if (!navigationCoordinator.exitMessageVisible.value) {
                            navigationCoordinator.setExitMessageVisible(true)
                        }
                        return
                    }

                    // goes back to home
                    with(navigationCoordinator) {
                        changeTab(HomeTab)
                        setCurrentSection(TabNavigationSection.Home)
                    }
                }
            }
        // when back is detected and the confirmation callback is not active, terminate the activity
        val finishBackPressedCallback =
            object : OnBackPressedCallback(false) {
                override fun handleOnBackPressed() {
                    navigationCoordinator.setExitMessageVisible(false)
                    finish()
                }
            }
        navigationCoordinator.exitMessageVisible
            .onEach { exitMessageVisible ->
                backPressedCallback.isEnabled = !exitMessageVisible
                finishBackPressedCallback.isEnabled = exitMessageVisible
            }.launchIn(lifecycleScope)
        onBackPressedDispatcher.addCallback(backPressedCallback)
        onBackPressedDispatcher.addCallback(finishBackPressedCallback)

        setContent {
            MainView(
                onLoadingFinished = {
                    loadingFinished = true
                },
            )
        }

        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    override fun onResume() {
        super.onResume()
        applyWorkaroundForSolidStatusBar()
    }

    private fun handleIntent(intent: Intent?) = intent?.apply {
        when (action) {
            Intent.ACTION_SEND ->
                intent.getStringExtra(Intent.EXTRA_TEXT)?.let { content ->
                    handleCreatePost(content)
                }

            else ->
                data.toString().takeUnless { it.isEmpty() }?.also { url ->
                    handleDeeplink(url)
                }
        }
    }

    private fun handleDeeplink(url: String) {
        val navigationCoordinator = getNavigationCoordinator()
        navigationCoordinator.submitDeeplink(url)
    }

    private fun handleCreatePost(content: String) {
        val looksLikeAnUrl = Patterns.WEB_URL.matcher(content).matches()
        val event =
            if (looksLikeAnUrl) {
                ComposeEvent.WithUrl(content)
            } else {
                ComposeEvent.WithText(content)
            }
        val navigationCoordinator = getNavigationCoordinator()
        navigationCoordinator.submitComposeEvent(event)
    }

    private fun applyWorkaroundForSolidStatusBar() {
        val barColorProvider = getBarColorProvider()
        val settingsRepository = getSettingsRepository()
        val accountRepository = getAccountRepository()
        val settings =
            runBlocking {
                val accountId = accountRepository.getActive()?.id
                settingsRepository.getSettings(accountId)
            }
        (barColorProvider as? SolidBarColorWorkaround)?.apply(
            activity = this@MainActivity,
            theme = settings.theme.toUiTheme(),
            barTheme = settings.systemBarTheme.toUiBarTheme(),
        )
    }
}
