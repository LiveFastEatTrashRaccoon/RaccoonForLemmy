package com.livefast.eattrash.raccoonforlemmy.feature.profile.ui

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.livefast.eattrash.raccoonforlemmy.feature.profile.main.ProfileMainMviModel
import com.livefast.eattrash.raccoonforlemmy.feature.profile.main.ProfileMainScreen
import com.livefast.eattrash.raccoonforlemmy.unit.myaccount.ProfileLoggedMviModel

@Composable
fun ProfileTab(
    model: ProfileMainMviModel,
    loggedModel: ProfileLoggedMviModel,
    modifier: Modifier = Modifier,
    loggedLazyListState: LazyListState = rememberLazyListState(),
) {
    ProfileMainScreen(
        modifier = modifier,
        model = model,
        loggedModel = loggedModel,
        loggedLazyListState = loggedLazyListState,
    )
}
