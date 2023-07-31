package com.github.diegoberaldin.raccoonforlemmy.core_api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MyUserInfo(
    @SerialName("local_user_view") val localUserView: LocalUserView,
    @SerialName("follows") val follows: List<CommunityFollowerView>,
    @SerialName("moderates") val moderates: List<CommunityModeratorView>,
    @SerialName("community_blocks") val communityBlocks: List<CommunityBlockView>,
    @SerialName("person_blocks") val personBlocks: List<PersonBlockView>,
    @SerialName("discussion_languages") val discussionLanguages: List<LanguageId>,
)