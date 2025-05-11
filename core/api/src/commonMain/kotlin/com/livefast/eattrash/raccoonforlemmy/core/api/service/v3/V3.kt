package com.livefast.eattrash.raccoonforlemmy.core.api.service.v3

interface V3 {
    val auth: AuthServiceV3
    val post: PostServiceV3
    val community: CommunityServiceV3
    val user: UserServiceV3
    val site: SiteServiceV3
    val comment: CommentServiceV3
    val search: SearchServiceV3
    val privateMessages: PrivateMessageServiceV3
    val modLog: ModlogServiceV3
}
