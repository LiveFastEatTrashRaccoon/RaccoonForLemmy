package com.github.diegoberaldin.raccoonforlemmy.feature.home.postlist

import androidx.compose.ui.input.nestedscroll.NestedScrollConnection

internal class DefaultBottomNavCoordinator : BottomNavBarCoordinator {
    private var connection: NestedScrollConnection? = null

    override fun setConnection(value: NestedScrollConnection?) {
        connection = value
    }

    override fun getConnection() = connection
}