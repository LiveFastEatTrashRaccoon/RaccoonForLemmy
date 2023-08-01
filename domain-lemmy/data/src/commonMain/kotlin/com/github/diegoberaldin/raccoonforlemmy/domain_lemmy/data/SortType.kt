package com.github.diegoberaldin.raccoonforlemmy.domain_lemmy.data

sealed interface SortType {
    object Active : SortType
    object Hot : SortType
    object New : SortType
    object MostComments : SortType
    object NewComments : SortType
    sealed interface Top : SortType {
        object Generic : Top
        object PastHour : Top
        object Past6Hours : Top
        object Past12Hours : Top
        object Day : Top
        object Week : Top
        object Month : Top
        object Year : Top
    }
}

