package com.github.diegoberaldin.raccoonforlemmy.core_api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LocalUser(
    @SerialName("id") val id: LocalUserId,
    @SerialName("person_id") val personId: PersonId,
    @SerialName("email") val email: String? = null,
    @SerialName("show_nsfw") val showNsfw: Boolean,
    @SerialName("theme") val theme: String,
    @SerialName("default_sort_type") val defaultSortType: SortType,
    @SerialName("default_listing_type") val defaultListingType: ListingType,
    @SerialName("interface_language") val interfaceLanguage: String,
    @SerialName("show_avatars") val showAvatars: Boolean,
    @SerialName("send_notifications_to_email") val sendNotificationsToEmail: Boolean,
    @SerialName("validator_time") val validatorTime: String,
    @SerialName("show_scores") val showScores: Boolean,
    @SerialName("show_bot_accounts") val showBotAccounts: Boolean,
    @SerialName("show_read_posts") val showReadPosts: Boolean,
    @SerialName("show_new_post_notifs") val showNewPostNotifs: Boolean,
    @SerialName("email_verified") val emailVerified: Boolean,
    @SerialName("accepted_application") val acceptedApplication: Boolean,
    @SerialName("totp_2fa_url") val totp2faUrl: String? = null,
)