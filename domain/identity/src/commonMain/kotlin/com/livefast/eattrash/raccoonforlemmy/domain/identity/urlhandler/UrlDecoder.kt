package com.livefast.eattrash.raccoonforlemmy.domain.identity.urlhandler

import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.CommunityModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.PostModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.UserModel

interface UrlDecoder {
    fun getCommunity(url: String?): CommunityModel?

    fun getUser(url: String?): UserModel?

    fun getPost(url: String?): Pair<PostModel?, String?>
}
