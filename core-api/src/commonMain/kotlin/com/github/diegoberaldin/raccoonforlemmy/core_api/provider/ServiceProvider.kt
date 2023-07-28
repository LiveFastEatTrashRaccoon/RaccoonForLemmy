package com.github.diegoberaldin.raccoonforlemmy.core_api.provider

import com.github.diegoberaldin.raccoonforlemmy.core_api.service.AuthService
import com.github.diegoberaldin.raccoonforlemmy.core_api.service.CommunityService
import com.github.diegoberaldin.raccoonforlemmy.core_api.service.PostService

interface ServiceProvider {

    val currentInstance: String
    val postService: PostService
    val communityService: CommunityService
    val authService: AuthService

    fun changeInstance(value: String)
}

