package com.livefast.eattrash.raccoonforlemmy.core.appearance.data

import androidx.compose.runtime.Composable
import com.livefast.eattrash.raccoonforlemmy.core.l10n.messages.LocalStrings

sealed interface FontScale {
    data object Largest : FontScale

    data object Larger : FontScale

    data object Large : FontScale

    data object Normal : FontScale

    data object Small : FontScale

    data object Smaller : FontScale

    data object Smallest : FontScale
}

val FontScale.scaleFactor: Float
    get() =
        when (this) {
            FontScale.Largest -> ReferenceValues.LARGEST
            FontScale.Larger -> ReferenceValues.LARGER
            FontScale.Large -> ReferenceValues.LARGE
            FontScale.Normal -> ReferenceValues.NORMAL
            FontScale.Small -> ReferenceValues.SMALL
            FontScale.Smaller -> ReferenceValues.SMALLER
            FontScale.Smallest -> ReferenceValues.SMALLEST
        }

@Composable
fun FontScale.toReadableName(): String =
    when (this) {
        FontScale.Largest -> LocalStrings.current.settingsContentFontLargest
        FontScale.Larger -> LocalStrings.current.settingsContentFontLarger
        FontScale.Large -> LocalStrings.current.settingsContentFontLarge
        FontScale.Normal -> LocalStrings.current.settingsContentFontNormal
        FontScale.Small -> LocalStrings.current.settingsContentFontSmall
        FontScale.Smaller -> LocalStrings.current.settingsContentFontSmaller
        FontScale.Smallest -> LocalStrings.current.settingsContentFontSmallest
    }

fun Float.toFontScale(): FontScale =
    when (this) {
        ReferenceValues.LARGEST -> FontScale.Largest
        ReferenceValues.LARGER -> FontScale.Larger
        ReferenceValues.LARGE -> FontScale.Large
        ReferenceValues.SMALL -> FontScale.Small
        ReferenceValues.SMALLER -> FontScale.Smaller
        ReferenceValues.SMALLEST -> FontScale.Smallest
        else -> FontScale.Normal
    }

private object ReferenceValues {
    const val LARGEST = 1.5f
    const val LARGER = 1.36f
    const val LARGE = 1.23f
    const val NORMAL = 1.1f
    const val SMALL = 0.96f
    const val SMALLER = 0.83f
    const val SMALLEST = 0.7f
}
