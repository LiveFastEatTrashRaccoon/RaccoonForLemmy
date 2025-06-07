package com.livefast.eattrash.raccoonforlemmy.main

import cafe.adriel.voyager.core.model.ScreenModel
import com.livefast.eattrash.raccoonforlemmy.core.architecture.MviModel
import com.livefast.eattrash.raccoonforlemmy.core.navigation.BottomNavItemsRepository
import com.livefast.eattrash.raccoonforlemmy.core.navigation.TabNavigationSection

interface MainMviModel :
    MviModel<MainMviModel.Intent, MainMviModel.UiState, MainMviModel.Effect>,
    ScreenModel {
    sealed interface Intent {
        data class SetBottomBarOffsetHeightPx(val value: Float) : Intent

        data object ReadAllInbox : Intent
    }

    data class UiState(
        val bottomBarOffsetHeightPx: Float = 0f,
        val customProfileUrl: String? = null,
        val isLogged: Boolean = false,
        val bottomBarSections: List<TabNavigationSection> = BottomNavItemsRepository.DEFAULT_ITEMS,
    )

    sealed interface Effect {
        data class UnreadItemsDetected(val value: Int) : Effect

        data object ReadAllInboxSuccess : Effect
    }
}
