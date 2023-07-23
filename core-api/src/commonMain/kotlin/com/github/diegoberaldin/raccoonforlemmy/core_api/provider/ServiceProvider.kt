package com.github.diegoberaldin.raccoonforlemmy.core_api.provider

import com.github.diegoberaldin.raccoonforlemmy.core_api.service.PostService

interface ServiceProvider {

    val postService: PostService

    fun changeInstance(value: String)
}

