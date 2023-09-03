package com.github.diegoberaldin.raccoonforlemmy.core.appearance.repository

import com.github.diegoberaldin.raccoonforlemmy.core.appearance.data.ThemeState
import kotlinx.coroutines.flow.MutableStateFlow

internal class DefaultThemeRepository : ThemeRepository {

    override val state = MutableStateFlow<ThemeState>(ThemeState.Light)
    override val contentFontScale = MutableStateFlow(1f)

    override fun changeTheme(value: ThemeState) {
        state.value = value
    }

    override fun changeContentFontScale(value: Float) {
        contentFontScale.value = value
    }
}
