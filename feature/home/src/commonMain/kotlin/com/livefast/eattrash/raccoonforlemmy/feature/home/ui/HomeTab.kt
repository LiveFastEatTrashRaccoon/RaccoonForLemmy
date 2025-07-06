package com.livefast.eattrash.raccoonforlemmy.feature.home.ui

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.livefast.eattrash.raccoonforlemmy.unit.postlist.PostListMviModel
import com.livefast.eattrash.raccoonforlemmy.unit.postlist.PostListScreen

@Composable
fun HomeTab(model: PostListMviModel, lazyListState: LazyListState, modifier: Modifier = Modifier) {
    PostListScreen(modifier = modifier, model = model, lazyListState = lazyListState)
}
