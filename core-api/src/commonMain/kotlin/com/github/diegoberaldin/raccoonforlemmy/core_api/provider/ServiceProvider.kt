package com.github.diegoberaldin.raccoonforlemmy.core_api.provider

import com.github.diegoberaldin.raccoonforlemmy.core_api.service.AuthService
import com.github.diegoberaldin.raccoonforlemmy.core_api.service.CommunityService
import com.github.diegoberaldin.raccoonforlemmy.core_api.service.PostService
import com.github.diegoberaldin.raccoonforlemmy.core_api.service.SiteService
import com.github.diegoberaldin.raccoonforlemmy.core_api.service.UserService

interface ServiceProvider {

    val currentInstance: String
    val auth: AuthService
    val post: PostService
    val community: CommunityService
    val user: UserService
    val site: SiteService

    fun changeInstance(value: String)
}

