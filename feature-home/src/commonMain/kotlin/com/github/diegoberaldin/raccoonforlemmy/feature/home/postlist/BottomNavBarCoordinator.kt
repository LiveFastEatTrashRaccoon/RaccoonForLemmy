package com.github.diegoberaldin.raccoonforlemmy.feature.home.postlist

import androidx.compose.ui.input.nestedscroll.NestedScrollConnection

interface BottomNavBarCoordinator {
    fun setConnection(value: NestedScrollConnection?)

    fun getConnection(): NestedScrollConnection?
}
