package com.github.diegoberaldin.raccoonforlemmy.domain_post.repository

internal fun extractHost(value: String) = value.replace("https://", "").let {
    val i = it.indexOf("/")
    it.substring(0, i)
}