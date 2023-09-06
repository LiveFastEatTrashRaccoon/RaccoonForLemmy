package com.github.diegoberaldin.raccoonforlemmy.core.appearance.repository

import com.github.diegoberaldin.raccoonforlemmy.core.appearance.data.ThemeState
import kotlinx.coroutines.flow.MutableStateFlow

internal class DefaultThemeRepository : ThemeRepository {

    override val state = MutableStateFlow<ThemeState>(ThemeState.Light)
    override val contentFontScale = MutableStateFlow(1f)
    override val navItemTitles = MutableStateFlow(false)
    override val dynamicColors = MutableStateFlow(false)

    override fun changeTheme(value: ThemeState) {
        state.value = value
    }

    override fun changeContentFontScale(value: Float) {
        contentFontScale.value = value
    }

    override fun changeNavItemTitles(value: Boolean) {
        navItemTitles.value = value
    }

    override fun changeDynamicColors(value: Boolean) {
        dynamicColors.value = value
    }
}
