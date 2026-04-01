package com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.resources.LocalResources

sealed interface SortType {
    data object Active : SortType

    data object Hot : SortType

    data object New : SortType

    data object MostComments : SortType

    data object NewComments : SortType

    data object Old : SortType

    sealed interface Top : SortType {
        data object Generic : Top

        data object PastHour : Top

        data object Past6Hours : Top

        data object Past12Hours : Top

        data object Day : Top

        data object Week : Top

        data object Month : Top

        data object Year : Top

        data object All : Top
    }

    data object Controversial : SortType

    data object Scaled : SortType
}

fun SortType.toInt() = when (this) {
    SortType.Hot -> 1
    SortType.MostComments -> 2
    SortType.New -> 3
    SortType.NewComments -> 4
    SortType.Top.Day -> 5
    SortType.Top.Generic -> 6
    SortType.Top.Month -> 7
    SortType.Top.Past12Hours -> 8
    SortType.Top.Past6Hours -> 9
    SortType.Top.PastHour -> 10
    SortType.Top.Week -> 11
    SortType.Top.Year -> 12
    SortType.Old -> 13
    SortType.Controversial -> 14
    SortType.Scaled -> 15
    else -> 0
}

fun Int.toSortType() = when (this) {
    1 -> SortType.Hot
    2 -> SortType.MostComments
    3 -> SortType.New
    4 -> SortType.NewComments
    5 -> SortType.Top.Day
    6 -> SortType.Top.Generic
    7 -> SortType.Top.Month
    8 -> SortType.Top.Past12Hours
    9 -> SortType.Top.Past6Hours
    10 -> SortType.Top.PastHour
    11 -> SortType.Top.Week
    12 -> SortType.Top.Year
    13 -> SortType.Old
    14 -> SortType.Controversial
    15 -> SortType.Scaled
    else -> SortType.Active
}

@Composable
fun SortType.toIcon(): ImageVector = when (this) {
    SortType.Active -> LocalResources.current.rocketLaunch
    SortType.Hot -> LocalResources.current.localFireDepartment
    SortType.MostComments -> LocalResources.current.comment
    SortType.New -> LocalResources.current.trendingUp
    SortType.NewComments -> LocalResources.current.markChatUnread
    SortType.Old -> LocalResources.current.elderlyWoman
    SortType.Controversial -> LocalResources.current.thunderstorm
    SortType.Scaled -> LocalResources.current.scale
    else -> LocalResources.current.workspacePremium
}

@Composable
fun SortType.toReadableName(): String = when (this) {
    SortType.Active -> LocalStrings.current.homeSortTypeActive
    SortType.Hot -> LocalStrings.current.homeSortTypeHot
    SortType.MostComments -> LocalStrings.current.homeSortTypeMostComments
    SortType.New -> LocalStrings.current.homeSortTypeNew
    SortType.NewComments -> LocalStrings.current.homeSortTypeNewComments
    SortType.Top.All -> LocalStrings.current.homeListingTypeAll
    SortType.Top.Day -> LocalStrings.current.homeSortTypeTopDay
    SortType.Top.Month -> LocalStrings.current.homeSortTypeTopMonth
    SortType.Top.Past12Hours -> LocalStrings.current.homeSortTypeTop12Hours
    SortType.Top.Past6Hours -> LocalStrings.current.homeSortTypeTop6Hours
    SortType.Top.PastHour -> LocalStrings.current.homeSortTypeTopHour
    SortType.Top.Week -> LocalStrings.current.homeSortTypeTopWeek
    SortType.Top.Year -> LocalStrings.current.homeSortTypeTopYear
    SortType.Old -> LocalStrings.current.homeSortTypeOld
    SortType.Controversial -> LocalStrings.current.homeSortTypeControversial
    SortType.Scaled -> LocalStrings.current.homeSortTypeScaled
    else -> LocalStrings.current.homeSortTypeTop
}

@Composable
fun SortType?.getAdditionalLabel(): String = when (this) {
    SortType.Top.Day -> LocalStrings.current.homeSortTypeTopDayShort
    SortType.Top.Month -> LocalStrings.current.homeSortTypeTopMonthShort
    SortType.Top.Past12Hours -> LocalStrings.current.homeSortTypeTop12HoursShort
    SortType.Top.Past6Hours -> LocalStrings.current.homeSortTypeTop6HoursShort
    SortType.Top.PastHour -> LocalStrings.current.homeSortTypeTopHourShort
    SortType.Top.Week -> LocalStrings.current.homeSortTypeTopWeekShort
    SortType.Top.Year -> LocalStrings.current.homeSortTypeTopYearShort
    else -> ""
}
