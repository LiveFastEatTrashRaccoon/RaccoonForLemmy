package com.github.diegoberaldin.raccoonforlemmy.feature_home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddComment
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Domain
import androidx.compose.material.icons.filled.Fireplace
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Reviews
import androidx.compose.material.icons.filled.Rocket
import androidx.compose.runtime.Composable
import com.github.diegoberaldin.raccoonforlemmy.domain_lemmy.data.ListingType
import com.github.diegoberaldin.raccoonforlemmy.domain_lemmy.data.SortType
import com.github.diegoberaldin.raccoonforlemmy.resources.MR
import dev.icerock.moko.resources.compose.stringResource


internal fun ListingType.toIcon() = when (this) {
    ListingType.Local -> Icons.Default.Domain
    ListingType.Subscribed -> Icons.Default.Newspaper
    else -> Icons.Default.Public
}

@Composable
internal fun ListingType.toReadableName(): String = when (this) {
    ListingType.All -> stringResource(MR.strings.home_listing_type_all)
    ListingType.Local -> stringResource(MR.strings.home_listing_type_local)
    ListingType.Subscribed -> stringResource(MR.strings.home_listing_type_subscribed)
}

internal fun SortType.toIcon() = when (this) {
    SortType.Active -> Icons.Default.Rocket
    SortType.Hot -> Icons.Default.Fireplace
    SortType.MostComments -> Icons.Default.Reviews
    SortType.New -> Icons.Default.Bolt
    SortType.NewComments -> Icons.Default.AddComment
    else -> Icons.Default.MilitaryTech
}

@Composable
internal fun SortType.toReadableName(): String = when (this) {
    SortType.Active -> stringResource(MR.strings.home_sort_type_active)
    SortType.Hot -> stringResource(MR.strings.home_sort_type_hot)
    SortType.MostComments -> stringResource(MR.strings.home_sort_type_most_comments)
    SortType.New -> stringResource(MR.strings.home_sort_type_new)
    SortType.NewComments -> stringResource(MR.strings.home_sort_type_new_comments)
    SortType.Top.Day -> stringResource(MR.strings.home_sort_type_top_day)
    SortType.Top.Month -> stringResource(MR.strings.home_sort_type_top_month)
    SortType.Top.Past12Hours -> stringResource(MR.strings.home_sort_type_top_12_hours)
    SortType.Top.Past6Hours -> stringResource(MR.strings.home_sort_type_top_6_hours)
    SortType.Top.PastHour -> stringResource(MR.strings.home_sort_type_top_hour)
    SortType.Top.Week -> stringResource(MR.strings.home_sort_type_top_week)
    SortType.Top.Year -> stringResource(MR.strings.home_sort_type_top_year)
    else -> stringResource(MR.strings.home_sort_type_top)
}