package com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data

data class MediaModel(val alias: String, val deleteToken: String, val date: String? = null)

fun MediaModel.getUrl(instance: String): String = buildString {
    append("https://")
    append(instance)
    append("/pictrs/image/")
    append(alias)
}
