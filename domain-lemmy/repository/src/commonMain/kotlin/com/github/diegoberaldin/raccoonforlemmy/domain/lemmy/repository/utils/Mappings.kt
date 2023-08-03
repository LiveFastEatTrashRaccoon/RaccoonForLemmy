package com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.repository.utils

import com.github.diegoberaldin.raccoonforlemmy.core.api.dto.CommentView
import com.github.diegoberaldin.raccoonforlemmy.core.api.dto.Community
import com.github.diegoberaldin.raccoonforlemmy.core.api.dto.ListingType.All
import com.github.diegoberaldin.raccoonforlemmy.core.api.dto.ListingType.Local
import com.github.diegoberaldin.raccoonforlemmy.core.api.dto.ListingType.Subscribed
import com.github.diegoberaldin.raccoonforlemmy.core.api.dto.Person
import com.github.diegoberaldin.raccoonforlemmy.core.api.dto.PersonAggregates
import com.github.diegoberaldin.raccoonforlemmy.core.api.dto.PostView
import com.github.diegoberaldin.raccoonforlemmy.core.api.dto.SortType.Active
import com.github.diegoberaldin.raccoonforlemmy.core.api.dto.SortType.Hot
import com.github.diegoberaldin.raccoonforlemmy.core.api.dto.SortType.MostComments
import com.github.diegoberaldin.raccoonforlemmy.core.api.dto.SortType.New
import com.github.diegoberaldin.raccoonforlemmy.core.api.dto.SortType.NewComments
import com.github.diegoberaldin.raccoonforlemmy.core.api.dto.SortType.TopDay
import com.github.diegoberaldin.raccoonforlemmy.core.api.dto.SortType.TopHour
import com.github.diegoberaldin.raccoonforlemmy.core.api.dto.SortType.TopMonth
import com.github.diegoberaldin.raccoonforlemmy.core.api.dto.SortType.TopSixHour
import com.github.diegoberaldin.raccoonforlemmy.core.api.dto.SortType.TopTwelveHour
import com.github.diegoberaldin.raccoonforlemmy.core.api.dto.SortType.TopWeek
import com.github.diegoberaldin.raccoonforlemmy.core.api.dto.SortType.TopYear
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.CommentModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.CommunityModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.ListingType
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.PostModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.SortType
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.UserModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.UserScoreModel

internal fun ListingType.toDto() = when (this) {
    ListingType.All -> All
    ListingType.Subscribed -> Subscribed
    ListingType.Local -> Local
}

internal fun SortType.toDto() = when (this) {
    SortType.Hot -> Hot
    SortType.MostComments -> MostComments
    SortType.New -> New
    SortType.NewComments -> NewComments
    SortType.Top.Day -> TopDay
    SortType.Top.Month -> TopMonth
    SortType.Top.Past12Hours -> TopTwelveHour
    SortType.Top.Past6Hours -> TopSixHour
    SortType.Top.PastHour -> TopHour
    SortType.Top.Week -> TopWeek
    SortType.Top.Year -> TopYear
    else -> Active
}

internal fun Person.toModel() = UserModel(
    id = id,
    name = name,
    avatar = avatar,
    host = actorId.toHost(),
    accountAge = published,
)

internal fun PersonAggregates.toModel() = UserScoreModel(
    postScore = postScore,
    commentScore = commentScore,
)

internal fun PostView.toModel() = PostModel(
    id = post.id,
    title = post.name,
    text = post.body.orEmpty(),
    score = counts.score,
    comments = counts.comments,
    thumbnailUrl = post.thumbnailUrl.orEmpty(),
    community = community.toModel(),
    creator = creator.toModel(),
    saved = saved,
    myVote = myVote ?: 0,
)

internal fun CommentView.toModel() = CommentModel(
    id = comment.id,
    text = comment.content,
    community = community.toModel(),
    creator = creator.toModel(),
    score = counts.score,
    saved = saved,
    myVote = myVote ?: 0,
)

internal fun Community.toModel() = CommunityModel(
    id = id,
    name = name,
    icon = icon,
    host = actorId.toHost(),
)

internal fun String.toHost(): String = this.replace("https://", "").let {
    val i = it.indexOf("/")
    it.substring(0, i)
}
