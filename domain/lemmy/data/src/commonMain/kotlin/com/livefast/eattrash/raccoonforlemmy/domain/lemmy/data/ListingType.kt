package com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Cottage
import androidx.compose.material.icons.filled.Public
import androidx.compose.runtime.Composable
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings

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

fun ListingType.toIcon() = when (this) {
    ListingType.Local -> Icons.Default.Cottage
    ListingType.Subscribed -> Icons.Default.Book
    else -> Icons.Default.Public
}

@Composable
fun ListingType.toReadableName(): String = when (this) {
    ListingType.All -> LocalStrings.current.homeListingTypeAll
    ListingType.Local -> LocalStrings.current.homeListingTypeLocal
    ListingType.Subscribed -> LocalStrings.current.homeListingTypeSubscribed
    else -> ""
}
