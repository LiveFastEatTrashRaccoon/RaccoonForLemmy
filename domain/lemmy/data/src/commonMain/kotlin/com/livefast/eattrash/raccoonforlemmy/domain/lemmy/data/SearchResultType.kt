package com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.resources.LocalResources

sealed interface SearchResultType {
    data object All : SearchResultType

    data object Posts : SearchResultType

    data object Comments : SearchResultType

    data object Users : SearchResultType

    data object Communities : SearchResultType

    data object Urls : SearchResultType
}

@Composable
fun SearchResultType.toIcon(): ImageVector = when (this) {
    SearchResultType.All -> LocalResources.current.allInclusive
    SearchResultType.Comments -> LocalResources.current.comment
    SearchResultType.Communities -> LocalResources.current.groups
    SearchResultType.Posts -> LocalResources.current.article
    SearchResultType.Users -> LocalResources.current.person
    SearchResultType.Urls -> LocalResources.current.alternateEmail
}

@Composable
fun SearchResultType.toReadableName(): String = when (this) {
    SearchResultType.All -> LocalStrings.current.exploreResultTypeAll
    SearchResultType.Comments -> LocalStrings.current.exploreResultTypeComments
    SearchResultType.Communities -> LocalStrings.current.exploreResultTypeCommunities
    SearchResultType.Posts -> LocalStrings.current.exploreResultTypePosts
    SearchResultType.Users -> LocalStrings.current.exploreResultTypeUsers
    SearchResultType.Urls -> LocalStrings.current.createPostUrl
}

fun SearchResultType.toInt(): Int = when (this) {
    SearchResultType.Posts -> 1
    SearchResultType.Communities -> 2
    SearchResultType.Users -> 3
    SearchResultType.Comments -> 4
    SearchResultType.Urls -> 5
    else -> 0
}

fun Int.toSearchResultType(): SearchResultType = when (this) {
    1 -> SearchResultType.Posts
    2 -> SearchResultType.Communities
    3 -> SearchResultType.Users
    4 -> SearchResultType.Comments
    5 -> SearchResultType.Urls
    else -> SearchResultType.All
}
