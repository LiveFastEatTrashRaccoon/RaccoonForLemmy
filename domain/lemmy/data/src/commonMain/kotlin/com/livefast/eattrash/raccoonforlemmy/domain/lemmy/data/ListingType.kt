package com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data

import androidx.compose.runtime.Composable
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.resources.LocalResources

sealed interface ListingType {
    data object All : ListingType

    data object Subscribed : ListingType

    data object Local : ListingType

    data object ModeratorView : ListingType
}

fun ListingType.toInt() = when (this) {
    ListingType.All -> 2
    ListingType.Subscribed -> 1
    else -> 0
}

fun Int.toListingType() = when (this) {
    1 -> ListingType.Subscribed
    2 -> ListingType.All
    else -> ListingType.Local
}

@Composable
fun ListingType.toIcon() = when (this) {
    ListingType.Local -> LocalResources.current.cottage
    ListingType.Subscribed -> LocalResources.current.book
    else -> LocalResources.current.public
}

@Composable
fun ListingType.toReadableName(): String = when (this) {
    ListingType.All -> LocalStrings.current.homeListingTypeAll
    ListingType.Local -> LocalStrings.current.homeListingTypeLocal
    ListingType.Subscribed -> LocalStrings.current.homeListingTypeSubscribed
    else -> ""
}
