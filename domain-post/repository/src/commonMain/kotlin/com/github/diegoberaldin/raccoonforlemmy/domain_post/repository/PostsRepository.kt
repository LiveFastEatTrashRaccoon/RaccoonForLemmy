package com.github.diegoberaldin.raccoonforlemmy.domain_post.repository

import com.github.diegoberaldin.raccoonforlemmy.core_api.dto.PostView
import com.github.diegoberaldin.raccoonforlemmy.core_api.service.PostService
import com.github.diegoberaldin.raccoonforlemmy.data.CommunityModel
import com.github.diegoberaldin.raccoonforlemmy.data.ListingType
import com.github.diegoberaldin.raccoonforlemmy.data.PostModel
import com.github.diegoberaldin.raccoonforlemmy.data.SortType
import com.github.diegoberaldin.raccoonforlemmy.core_api.dto.ListingType as DtoListingType
import com.github.diegoberaldin.raccoonforlemmy.core_api.dto.SortType as DtoSortType

class PostsRepository(
    private val postService: PostService,
) {

    companion object {
        const val DEFAULT_PAGE_SIZE = 15
    }

    suspend fun getPosts(
        page: Int,
        limit: Int = DEFAULT_PAGE_SIZE,
        type: ListingType = ListingType.Local,
        sort: SortType = SortType.Active,
    ): List<PostModel> {
        val response = postService.getPosts(
            page = page,
            limit = limit,
            type = type.toDto(),
            sort = sort.toDto(),
        )
        val dto = response.body()?.posts ?: emptyList()
        return dto.map { it.toModel() }
    }
}

private fun PostView.toModel() = PostModel(
    id = post.id,
    title = post.name,
    text = post.body.orEmpty(),
    score = counts.score,
    comments = counts.comments,
    thumbnailUrl = post.thumbnailUrl.orEmpty(),
    community = CommunityModel(id = post.communityId)
)

private fun ListingType.toDto() = when (this) {
    ListingType.All -> DtoListingType.All
    ListingType.Subscribed -> DtoListingType.Subscribed
    ListingType.Local -> DtoListingType.Local
}

private fun SortType.toDto() = when (this) {
    SortType.Hot -> DtoSortType.Hot
    SortType.MostComments -> DtoSortType.MostComments
    SortType.New -> DtoSortType.New
    SortType.NewComments -> DtoSortType.NewComments
    SortType.Top.Day -> DtoSortType.TopDay
    SortType.Top.Month -> DtoSortType.TopMonth
    SortType.Top.Past12Hours -> DtoSortType.TopTwelveHour
    SortType.Top.Past6Hours -> DtoSortType.TopSixHour
    SortType.Top.PastHour -> DtoSortType.TopHour
    SortType.Top.Week -> DtoSortType.TopWeek
    SortType.Top.Year -> DtoSortType.TopYear
    else -> DtoSortType.Active
}