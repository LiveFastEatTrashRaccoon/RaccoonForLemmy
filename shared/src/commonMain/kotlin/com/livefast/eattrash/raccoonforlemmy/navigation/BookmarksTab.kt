package com.livefast.eattrash.raccoonforlemmy.navigation

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.livefast.eattrash.raccoonforlemmy.unit.filteredcontents.FilteredContentsMviModel
import com.livefast.eattrash.raccoonforlemmy.unit.filteredcontents.FilteredContentsScreen
import com.livefast.eattrash.raccoonforlemmy.unit.filteredcontents.FilteredContentsType
import com.livefast.eattrash.raccoonforlemmy.unit.filteredcontents.toInt

@Composable
fun BookmarksTab(
    model: FilteredContentsMviModel,
    modifier: Modifier = Modifier,
    lazyListState: LazyListState,
) {
    FilteredContentsScreen(
        type = FilteredContentsType.Bookmarks.toInt(),
        modifier = modifier,
        model = model,
        lazyListState = lazyListState,
    )
}
