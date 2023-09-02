package com.github.diegoberaldin.raccoonforlemmy.core.commonui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeableCard(
    modifier: Modifier = Modifier,
    directions: Set<DismissDirection> = setOf(
        DismissDirection.StartToEnd,
        DismissDirection.EndToStart,
    ),
    content: @Composable () -> Unit,
    swipeContent: @Composable (DismissDirection) -> Unit,
    backgroundColor: @Composable (DismissValue) -> Color,
    onGestureBegin: (() -> Unit) = {},
    onDismissToEnd: (() -> Unit) = {},
    onDismissToStart: (() -> Unit) = {},
) {
    var width by remember { mutableStateOf(0f) }
    val dismissState = rememberDismissState(
        confirmStateChange = {
            when (it) {
                DismissValue.DismissedToEnd -> {
                    onDismissToEnd()
                }

                DismissValue.DismissedToStart -> {
                    onDismissToStart()
                }

                else -> Unit
            }
            false
        },
    )
    var willDismissDirection: DismissDirection? by remember {
        mutableStateOf(null)
    }
    val threshold = 0.15f
    LaunchedEffect(Unit) {
        snapshotFlow { dismissState.offset.value }.collect {
            willDismissDirection = when {
                it > width * threshold -> DismissDirection.StartToEnd
                it < -width * threshold -> DismissDirection.EndToStart
                else -> null
            }
        }
    }
    LaunchedEffect(willDismissDirection) {
        if (willDismissDirection != null) {
            onGestureBegin()
        }
    }
    SwipeToDismiss(
        modifier = modifier.onGloballyPositioned {
            width = it.size.toSize().width
        },
        state = dismissState,
        directions = directions,
        dismissThresholds = {
            FractionalThreshold(threshold)
        },
        background = {
            val direction =
                dismissState.dismissDirection ?: return@SwipeToDismiss
            val bgColor by animateColorAsState(
                backgroundColor(dismissState.targetValue),
            )
            val alignment = when (direction) {
                DismissDirection.StartToEnd -> Alignment.CenterStart
                DismissDirection.EndToStart -> Alignment.CenterEnd
            }
            Box(
                Modifier.fillMaxSize().background(bgColor).padding(horizontal = 20.dp),
                contentAlignment = alignment,
            ) {
                swipeContent(direction)
            }
        },
    ) {
        content()
    }
}
