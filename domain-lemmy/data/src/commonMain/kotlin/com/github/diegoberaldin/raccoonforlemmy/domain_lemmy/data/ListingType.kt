package com.github.diegoberaldin.raccoonforlemmy.domain_lemmy.data

sealed interface ListingType {
    object All : ListingType
    object Subscribed : ListingType
    object Local : ListingType
}