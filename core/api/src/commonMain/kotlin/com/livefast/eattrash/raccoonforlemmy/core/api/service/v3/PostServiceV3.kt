package com.livefast.eattrash.raccoonforlemmy.core.api.service.v3

import com.livefast.eattrash.raccoonforlemmy.core.api.dto.CommentId
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.CommunityId
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.CreatePostForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.CreatePostLikeForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.CreatePostReportForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.DeletePostForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.EditPostForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.FeaturePostForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.GetPostResponse
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.GetPostsResponse
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.GetSiteMetadataResponse
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.HidePostForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.ListPostReportsResponse
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.ListingType
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.LockPostForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.MarkPostAsReadForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.PictrsImages
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.PostId
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.PostReportResponse
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.PostResponse
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.PurgePostForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.RemovePostForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.ResolvePostReportForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.SavePostForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.SortType
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.SuccessResponse
import io.ktor.client.request.forms.MultiPartFormDataContent

interface PostServiceV3 {
    suspend fun getAll(
        authHeader: String? = null,
        auth: String? = null,
        limit: Int? = null,
        sort: SortType? = null,
        page: Int? = null,
        pageCursor: String? = null,
        type: ListingType? = null,
        communityId: CommunityId? = null,
        communityName: String? = null,
        savedOnly: Boolean? = null,
        likedOnly: Boolean? = null,
        dislikedOnly: Boolean? = null,
        showHidden: Boolean? = null,
    ): GetPostsResponse

    suspend fun get(
        authHeader: String? = null,
        auth: String? = null,
        id: PostId? = null,
        commentId: CommentId? = null,
    ): GetPostResponse

    suspend fun getSiteMetadata(authHeader: String? = null, url: String): GetSiteMetadataResponse

    suspend fun save(authHeader: String? = null, form: SavePostForm): PostResponse

    suspend fun like(authHeader: String? = null, form: CreatePostLikeForm): PostResponse

    suspend fun create(authHeader: String? = null, form: CreatePostForm): PostResponse

    suspend fun edit(authHeader: String? = null, form: EditPostForm): PostResponse

    suspend fun markAsRead(authHeader: String? = null, form: MarkPostAsReadForm): PostResponse

    suspend fun hide(authHeader: String? = null, form: HidePostForm): PostResponse

    suspend fun delete(authHeader: String? = null, form: DeletePostForm): PostResponse

    suspend fun uploadImage(
        url: String,
        token: String,
        authHeader: String? = null,
        content: MultiPartFormDataContent,
    ): PictrsImages

    suspend fun deleteImage(url: String, token: String, authHeader: String? = null): Boolean

    suspend fun createReport(authHeader: String? = null, form: CreatePostReportForm): PostReportResponse

    suspend fun feature(authHeader: String? = null, form: FeaturePostForm): PostResponse

    suspend fun remove(authHeader: String? = null, form: RemovePostForm): PostResponse

    suspend fun lock(authHeader: String? = null, form: LockPostForm): PostResponse

    suspend fun listReports(
        authHeader: String? = null,
        auth: String? = null,
        limit: Int? = null,
        page: Int? = null,
        unresolvedOnly: Boolean? = null,
        communityId: CommunityId? = null,
    ): ListPostReportsResponse

    suspend fun resolveReport(authHeader: String? = null, form: ResolvePostReportForm): PostReportResponse

    suspend fun purge(authHeader: String? = null, form: PurgePostForm): SuccessResponse
}
