package com.github.diegoberaldin.raccoonforlemmy.core.appearance.repository

import androidx.compose.ui.graphics.Color
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.data.PostLayout
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.data.ThemeState
import kotlinx.coroutines.flow.StateFlow

interface ThemeRepository {

    val state: StateFlow<ThemeState>

    val contentFontScale: StateFlow<Float>

    val navItemTitles: StateFlow<Boolean>

    val dynamicColors: StateFlow<Boolean>

    val customSeedColor: StateFlow<Color?>

    val postLayout: StateFlow<PostLayout>

    fun changeTheme(value: ThemeState)

    fun changeContentFontScale(value: Float)

    fun changeNavItemTitles(value: Boolean)

    fun changeDynamicColors(value: Boolean)

    fun getCommentBarColor(
        depth: Int,
        maxDepth: Int,
        startColor: Color,
        endColor: Color,
    ): Color

    fun changeCustomSeedColor(color: Color?)

    fun changePostLayout(value: PostLayout)
}
