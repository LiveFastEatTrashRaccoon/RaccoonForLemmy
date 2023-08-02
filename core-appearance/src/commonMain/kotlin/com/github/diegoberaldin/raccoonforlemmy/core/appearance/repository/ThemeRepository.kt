package com.github.diegoberaldin.raccoonforlemmy.core.appearance.repository

import com.github.diegoberaldin.raccoonforlemmy.core.appearance.data.ThemeState
import kotlinx.coroutines.flow.StateFlow

interface ThemeRepository {

    val state: StateFlow<ThemeState>

    fun changeTheme(value: ThemeState)
}
