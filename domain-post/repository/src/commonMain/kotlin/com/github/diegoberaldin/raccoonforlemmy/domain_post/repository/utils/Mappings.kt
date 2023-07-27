package com.github.diegoberaldin.raccoonforlemmy.domain_post.repository.utils

import com.github.diegoberaldin.raccoonforlemmy.core_api.dto.Community
import com.github.diegoberaldin.raccoonforlemmy.core_api.dto.ListingType.All
import com.github.diegoberaldin.raccoonforlemmy.core_api.dto.ListingType.Local
import com.github.diegoberaldin.raccoonforlemmy.core_api.dto.ListingType.Subscribed
import com.github.diegoberaldin.raccoonforlemmy.core_api.dto.Person
import com.github.diegoberaldin.raccoonforlemmy.core_api.dto.SortType.Active
import com.github.diegoberaldin.raccoonforlemmy.core_api.dto.SortType.Hot
import com.github.diegoberaldin.raccoonforlemmy.core_api.dto.SortType.MostComments
import com.github.diegoberaldin.raccoonforlemmy.core_api.dto.SortType.New
import com.github.diegoberaldin.raccoonforlemmy.core_api.dto.SortType.NewComments
import com.github.diegoberaldin.raccoonforlemmy.core_api.dto.SortType.TopDay
import com.github.diegoberaldin.raccoonforlemmy.core_api.dto.SortType.TopHour
import com.github.diegoberaldin.raccoonforlemmy.core_api.dto.SortType.TopMonth
import com.github.diegoberaldin.raccoonforlemmy.core_api.dto.SortType.TopSixHour
import com.github.diegoberaldin.raccoonforlemmy.core_api.dto.SortType.TopTwelveHour
import com.github.diegoberaldin.raccoonforlemmy.core_api.dto.SortType.TopWeek
import com.github.diegoberaldin.raccoonforlemmy.core_api.dto.SortType.TopYear
import com.github.diegoberaldin.raccoonforlemmy.data.CommunityModel
import com.github.diegoberaldin.raccoonforlemmy.data.ListingType
import com.github.diegoberaldin.raccoonforlemmy.data.SortType
import com.github.diegoberaldin.raccoonforlemmy.data.UserModel

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

internal fun Community.toModel() = CommunityModel(
    name = name,
    icon = icon,
    host = extractHost(actorId),
)


internal fun Person.toModel() = UserModel(
    name = name,
    avatar = avatar,
    host = extractHost(actorId)
)

internal fun extractHost(value: String) = value.replace("https://", "").let {
    val i = it.indexOf("/")
    it.substring(0, i)
}