package com.github.diegoberaldin.raccoonforlemmy.core.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetSiteResponse(
    @SerialName("site_view") val siteView: SiteView,
    @SerialName("admins") val admins: List<PersonView>,
    @SerialName("version") val version: String,
    @SerialName("my_user") val myUser: MyUserInfo? = null,
    @SerialName("all_languages") val allLanguages: List<Language>,
    @SerialName("discussion_languages") val discussionLanguages: List<LanguageId>,
    @SerialName("taglines") val taglines: List<Tagline>,
    @SerialName("custom_emojis") val customEmojis: List<CustomEmojiView>,
)
