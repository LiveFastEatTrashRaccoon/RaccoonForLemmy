package com.livefast.eattrash.raccoonforlemmy.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration

internal class DefaultNavigationCoordinator(dispatcher: CoroutineDispatcher = Dispatchers.Main) :
    NavigationCoordinator {
    override val currentSection = MutableStateFlow<TabNavigationSection?>(null)
    override val onDoubleTabSelection = MutableSharedFlow<TabNavigationSection>()
    override val deepLinkUrl = MutableSharedFlow<String>()
    override val composeEvents = MutableSharedFlow<ComposeEvent>()
    override val inboxUnread = MutableStateFlow(0)
    override val canPop = MutableStateFlow(false)
    override val exitMessageVisible = MutableStateFlow(false)
    override val sideMenuEvents = MutableSharedFlow<SideMenuEvents>()
    override val sideMenuOpened = MutableStateFlow(false)
    override val globalMessage = MutableSharedFlow<String>()

    private var connection: NestedScrollConnection? = null
    private var rootNavController: NavigationAdapter? = null
    private var bottomNavController: BottomNavigationAdapter? = null
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + dispatcher)

    companion object {
        private const val DEEP_LINK_DELAY = 500L
    }

    override fun setRootNavigator(adapter: NavigationAdapter) {
        rootNavController = adapter
        refreshCanPop()
    }

    override fun setBottomBarScrollConnection(value: NestedScrollConnection?) {
        connection = value
    }

    override fun getBottomBarScrollConnection() = connection

    override fun setBottomNavigator(adapter: BottomNavigationAdapter) {
        bottomNavController = adapter
    }

    override fun setBottomNavigationSection(section: TabNavigationSection) {
        bottomNavController?.navigate(section)
        currentSection.getAndUpdate { oldValue ->
            if (section == oldValue) {
                scope.launch {
                    onDoubleTabSelection.emit(section)
                }
            }
            section
        }
    }

    override fun submitDeeplink(url: String) {
        scope.launch {
            delay(DEEP_LINK_DELAY)
            runCatching {
                ensureActive()
                deepLinkUrl.emit(url)
            }
        }
    }

    override fun submitComposeEvent(event: ComposeEvent) {
        scope.launch {
            delay(DEEP_LINK_DELAY)
            runCatching {
                ensureActive()
                composeEvents.emit(event)
            }
        }
    }

    override fun setInboxUnread(count: Int) {
        inboxUnread.value = count
    }

    override fun push(destination: Destination) {
        closeSideMenu()
        rootNavController?.navigate(destination)
        refreshCanPop()
    }

    override fun pop() {
        rootNavController?.pop()
        refreshCanPop()
    }

    override fun setExitMessageVisible(value: Boolean) {
        exitMessageVisible.value = value
    }

    override fun openSideMenu(content: @Composable () -> Unit) {
        if (!sideMenuOpened.value) {
            scope.launch {
                sideMenuEvents.emit(SideMenuEvents.Open(content))
                sideMenuOpened.value = true
            }
        }
    }

    override fun closeSideMenu() {
        if (sideMenuOpened.value) {
            scope.launch {
                sideMenuEvents.emit(SideMenuEvents.Close)
                sideMenuOpened.value = false
            }
        }
    }

    override fun showGlobalMessage(message: String, delay: Duration) {
        scope.launch {
            delay(delay)
            globalMessage.emit(message)
        }
    }

    override fun popUntilRoot() {
        rootNavController?.popUntilRoot()
    }

    private fun refreshCanPop() {
        canPop.update {
            rootNavController?.canPop ?: false
        }
    }
}
