package com.github.diegoberaldin.raccoonforlemmy.core_appearance.repository

import com.github.diegoberaldin.raccoonforlemmy.core_appearance.data.ThemeState
import kotlinx.coroutines.flow.StateFlow

interface ThemeRepository {

    val state: StateFlow<ThemeState>

    fun changeTheme(value: ThemeState)
}

