package com.github.diegoberaldin.raccoonforlemmy.core.appearance.repository

import com.github.diegoberaldin.raccoonforlemmy.core.appearance.data.ThemeState
import kotlinx.coroutines.flow.StateFlow

interface ThemeRepository {

    val state: StateFlow<ThemeState>

    val contentFontScale: StateFlow<Float>

    val navItemTitles: StateFlow<Boolean>

    val dynamicColors: StateFlow<Boolean>

    fun changeTheme(value: ThemeState)

    fun changeContentFontScale(value: Float)

    fun changeNavItemTitles(value: Boolean)

    fun changeDynamicColors(value: Boolean)
}
