package com.github.diegoberaldin.raccoonforlemmy.core_appearance.repository

import com.github.diegoberaldin.raccoonforlemmy.core_appearance.data.ThemeState
import kotlinx.coroutines.flow.MutableStateFlow

internal class DefaultThemeRepository : ThemeRepository {

    override val state = MutableStateFlow<ThemeState>(ThemeState.Light)

    override fun changeTheme(value: ThemeState) {
        state.value = value
    }
}