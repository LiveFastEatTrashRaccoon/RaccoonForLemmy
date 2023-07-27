package com.github.diegoberaldin.raccoonforlemmy.core_appearance.data

sealed interface ThemeState {
    object Light : ThemeState
    object Dark : ThemeState
    object Black : ThemeState

    companion object {
        fun fromInt(value: Int) = when (value) {
            2 -> Black
            1 -> Dark
            else -> Light
        }
    }
}

fun ThemeState.toInt() = when (this) {
    ThemeState.Black -> 2
    ThemeState.Dark -> 1
    ThemeState.Light -> 0
}
