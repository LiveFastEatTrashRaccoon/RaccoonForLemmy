package com.github.diegoberaldin.raccoonforlemmy.core.commonui.navigation

import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface NavigationCoordinator {

    val deeplinkUrl: StateFlow<String?>
    val onDoubleTabSelection: SharedFlow<Tab>
    val inboxUnread: StateFlow<Int>

    fun setCurrentSection(tab: Tab)

    fun submitDeeplink(url: String?)

    fun setRootNavigator(value: Navigator?)

    fun setCanGoBackCallback(value: (() -> Boolean)?)

    fun getCanGoBackCallback(): (() -> Boolean)?

    fun getRootNavigator(): Navigator?

    fun setBottomBarScrollConnection(value: NestedScrollConnection?)

    fun getBottomBarScrollConnection(): NestedScrollConnection?

    fun setInboxUnread(count: Int)
}
