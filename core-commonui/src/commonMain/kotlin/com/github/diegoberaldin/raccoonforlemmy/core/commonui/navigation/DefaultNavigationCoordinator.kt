package com.github.diegoberaldin.raccoonforlemmy.core.commonui.navigation

import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import cafe.adriel.voyager.navigator.Navigator

internal class DefaultNavigationCoordinator : NavigationCoordinator {

    private var connection: NestedScrollConnection? = null
    private var navigator: Navigator? = null

    override fun setRootNavigator(value: Navigator?) {
        navigator = value
    }

    override fun getRootNavigator(): Navigator? = navigator

    override fun setBottomBarScrollConnection(value: NestedScrollConnection?) {
        connection = value
    }

    override fun getBottomBarScrollConnection() = connection
}