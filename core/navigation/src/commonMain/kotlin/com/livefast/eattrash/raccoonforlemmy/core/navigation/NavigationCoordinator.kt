package com.livefast.eattrash.raccoonforlemmy.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlin.time.Duration

sealed interface ComposeEvent {
    data class WithUrl(val url: String) : ComposeEvent

    data class WithText(val text: String) : ComposeEvent
}

sealed interface SideMenuEvents {
    data class Open(val content: @Composable (() -> Unit)) : SideMenuEvents

    data object Close : SideMenuEvents
}

@Stable
interface NavigationCoordinator {
    val currentSection: StateFlow<TabNavigationSection?>
    val onDoubleTabSelection: Flow<TabNavigationSection>
    val inboxUnread: StateFlow<Int>
    val deepLinkUrl: Flow<String?>
    val composeEvents: Flow<ComposeEvent?>
    val canPop: StateFlow<Boolean>
    val exitMessageVisible: StateFlow<Boolean>
    val sideMenuEvents: Flow<SideMenuEvents>
    val sideMenuOpened: StateFlow<Boolean>
    val globalMessage: Flow<String>

    fun setBottomNavigator(adapter: BottomNavigationAdapter)

    fun setBottomNavigationSection(section: TabNavigationSection)

    fun submitDeeplink(url: String)

    fun submitComposeEvent(event: ComposeEvent)

    fun setRootNavigator(adapter: NavigationAdapter)

    fun setBottomBarScrollConnection(value: NestedScrollConnection?)

    fun getBottomBarScrollConnection(): NestedScrollConnection?

    fun setInboxUnread(count: Int)

    fun push(destination: Destination)

    fun pop()

    fun setExitMessageVisible(value: Boolean)

    fun openSideMenu(content: @Composable () -> Unit)

    fun closeSideMenu()

    fun showGlobalMessage(message: String, delay: Duration = Duration.ZERO)

    fun popUntilRoot()
}
