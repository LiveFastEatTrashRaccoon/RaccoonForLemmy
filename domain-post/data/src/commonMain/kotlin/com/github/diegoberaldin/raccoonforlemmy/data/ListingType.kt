package com.github.diegoberaldin.raccoonforlemmy.data

sealed interface ListingType {
    object All : ListingType
    object Subscribed : ListingType
    object Local : ListingType
}