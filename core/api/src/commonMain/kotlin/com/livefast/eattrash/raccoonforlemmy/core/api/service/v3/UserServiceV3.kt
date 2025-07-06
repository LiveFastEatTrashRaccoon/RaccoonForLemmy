package com.livefast.eattrash.raccoonforlemmy.core.api.service.v3

import com.livefast.eattrash.raccoonforlemmy.core.api.dto.BlockPersonForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.BlockPersonResponse
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.CommentSortType
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.CommunityId
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.DeleteAccountForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.GetPersonDetailsResponse
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.GetPersonMentionsResponse
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.GetRepliesResponse
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.ListMediaResponse
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.MarkAllAsReadForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.MarkPersonMentionAsReadForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.PersonId
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.PersonMentionResponse
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.PurgePersonForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.SaveUserSettingsForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.SaveUserSettingsResponse
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.SuccessResponse

interface UserServiceV3 {
    suspend fun getDetails(
        authHeader: String? = null,
        auth: String? = null,
        communityId: CommunityId? = null,
        personId: PersonId? = null,
        page: Int? = null,
        limit: Int? = null,
        sort: CommentSortType = CommentSortType.New,
        username: String? = null,
        savedOnly: Boolean? = null,
    ): GetPersonDetailsResponse

    suspend fun getMentions(
        authHeader: String? = null,
        auth: String? = null,
        page: Int? = null,
        limit: Int? = null,
        sort: CommentSortType = CommentSortType.New,
        unreadOnly: Boolean? = null,
    ): GetPersonMentionsResponse

    suspend fun getReplies(
        authHeader: String? = null,
        auth: String? = null,
        page: Int? = null,
        limit: Int? = null,
        sort: CommentSortType = CommentSortType.New,
        unreadOnly: Boolean? = null,
    ): GetRepliesResponse

    suspend fun markAllAsRead(authHeader: String? = null, form: MarkAllAsReadForm): GetRepliesResponse

    suspend fun markPersonMentionAsRead(
        authHeader: String? = null,
        form: MarkPersonMentionAsReadForm,
    ): PersonMentionResponse

    suspend fun block(authHeader: String? = null, form: BlockPersonForm): BlockPersonResponse

    suspend fun saveUserSettings(authHeader: String? = null, form: SaveUserSettingsForm): SaveUserSettingsResponse

    suspend fun purge(authHeader: String? = null, form: PurgePersonForm): SuccessResponse

    suspend fun listMedia(authHeader: String? = null, page: Int? = null, limit: Int? = null): ListMediaResponse

    suspend fun deleteAccount(authHeader: String? = null, form: DeleteAccountForm): Boolean
}
