package com.livefast.eattrash.raccoonforlemmy.core.appearance.theme

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarState.toWindowInsets(): WindowInsets {
    val scope = rememberCoroutineScope()
    val maxTopInsetPx = with(LocalDensity.current) { Dimensions.maxTopBarInset.toPx() }
    var topInsetPx by remember { mutableStateOf(maxTopInsetPx) }
    val topInset = with(LocalDensity.current) { topInsetPx.toDp() }
    snapshotFlow { collapsedFraction }
        .onEach {
            topInsetPx = maxTopInsetPx * (1 - it)
        }.launchIn(scope)
    return WindowInsets(0.dp, topInset, 0.dp, 0.dp)
}
