package com.livefast.eattrash.raccoonforlemmy.feature.search.ui

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.livefast.eattrash.raccoonforlemmy.unit.explore.ExploreMviModel
import com.livefast.eattrash.raccoonforlemmy.unit.explore.ExploreScreen

@Composable
fun ExploreTab(model: ExploreMviModel, lazyListState: LazyListState, modifier: Modifier = Modifier) {
    ExploreScreen(modifier = modifier, model = model, lazyListState = lazyListState)
}
