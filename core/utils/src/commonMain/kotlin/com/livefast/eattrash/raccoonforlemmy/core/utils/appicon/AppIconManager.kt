package com.livefast.eattrash.raccoonforlemmy.core.utils.appicon

import androidx.compose.runtime.Composable
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings

sealed interface AppIconVariant {
    data object Default : AppIconVariant

    data object Classical : AppIconVariant
}

interface AppIconManager {
    val supportsMultipleIcons: Boolean

    fun changeIcon(variant: AppIconVariant)
}

@Composable
fun AppIconVariant.toReadableName(): String =
    when (this) {
        AppIconVariant.Classical -> LocalStrings.current.appIconClassical
        AppIconVariant.Default -> LocalStrings.current.appIconDefault
    }

fun AppIconVariant.toInt(): Int =
    when (this) {
        AppIconVariant.Classical -> 1
        AppIconVariant.Default -> 0
    }
