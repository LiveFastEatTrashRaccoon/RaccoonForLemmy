package com.github.diegoberaldin.raccoonforlemmy.core_appearance.data

sealed interface ThemeState {
    object Light : ThemeState
    object Dark : ThemeState
}