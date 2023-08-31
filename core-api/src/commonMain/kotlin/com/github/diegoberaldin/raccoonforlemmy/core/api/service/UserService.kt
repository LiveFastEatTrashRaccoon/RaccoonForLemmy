package com.github.diegoberaldin.raccoonforlemmy.core.api.service

import com.github.diegoberaldin.raccoonforlemmy.core.api.dto.CommentSortType
import com.github.diegoberaldin.raccoonforlemmy.core.api.dto.GetPersonDetailsResponse
import com.github.diegoberaldin.raccoonforlemmy.core.api.dto.GetPersonMentionsResponse
import com.github.diegoberaldin.raccoonforlemmy.core.api.dto.GetRepliesResponse
import com.github.diegoberaldin.raccoonforlemmy.core.api.dto.MarkAllAsReadForm
import com.github.diegoberaldin.raccoonforlemmy.core.api.dto.MarkPersonMentionAsReadForm
import com.github.diegoberaldin.raccoonforlemmy.core.api.dto.PersonMentionResponse
import de.jensklingenberg.ktorfit.Response
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Headers
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Query

interface UserService {

    @GET("user")
    suspend fun getDetails(
        @Query("auth") auth: String? = null,
        @Query("community_id") communityId: Int? = null,
        @Query("person_id") personId: Int? = null,
        @Query("page") page: Int? = null,
        @Query("limit") limit: Int? = null,
        @Query("sort") sort: CommentSortType = CommentSortType.New,
        @Query("username") username: String? = null,
        @Query("saved_only") savedOnly: Boolean? = null,
    ): Response<GetPersonDetailsResponse>

    @GET("user/mention")
    suspend fun getMentions(
        @Query("auth") auth: String? = null,
        @Query("page") page: Int? = null,
        @Query("limit") limit: Int? = null,
        @Query("sort") sort: CommentSortType = CommentSortType.New,
        @Query("unread_only") unreadOnly: Boolean? = null,
    ): Response<GetPersonMentionsResponse>

    @GET("user/replies")
    suspend fun getReplies(
        @Query("auth") auth: String? = null,
        @Query("page") page: Int? = null,
        @Query("limit") limit: Int? = null,
        @Query("sort") sort: CommentSortType = CommentSortType.New,
        @Query("unread_only") unreadOnly: Boolean? = null,
    ): Response<GetRepliesResponse>

    @POST("user/mark_all_as_read")
    @Headers("Content-Type: application/json")
    suspend fun markAllAsRead(@Body form: MarkAllAsReadForm): Response<GetRepliesResponse>

    @POST("user/mention/mark_as_read")
    @Headers("Content-Type: application/json")
    suspend fun markPersonMentionAsRead(@Body form: MarkPersonMentionAsReadForm): Response<PersonMentionResponse>
}
