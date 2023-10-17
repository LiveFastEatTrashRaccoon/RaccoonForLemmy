package com.github.diegoberaldin.raccoonforlemmy.core.commonui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissState
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FixedThreshold
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.ResistanceConfig
import androidx.compose.material.SwipeableDefaults
import androidx.compose.material.ThresholdConfig
import androidx.compose.material.rememberDismissState
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeableCard(
    modifier: Modifier = Modifier,
    directions: Set<DismissDirection> = setOf(
        DismissDirection.StartToEnd,
        DismissDirection.EndToStart,
    ),
    enabled: Boolean = true,
    content: @Composable () -> Unit,
    swipeContent: @Composable (DismissDirection) -> Unit,
    backgroundColor: @Composable (DismissValue) -> Color,
    onGestureBegin: (() -> Unit)? = null,
    onDismissToEnd: (() -> Unit)? = null,
    onDismissToStart: (() -> Unit)? = null,
) {
    if (enabled) {
        var width by remember { mutableStateOf(0f) }
        val dismissToEndCallback by rememberUpdatedState(onDismissToEnd)
        val dismissToStartCallback by rememberUpdatedState(onDismissToStart)
        val gestureBeginCallback by rememberUpdatedState(onGestureBegin)
        val dismissState = rememberDismissState(
            confirmStateChange = {
                when (it) {
                    DismissValue.DismissedToEnd -> {
                        dismissToEndCallback?.invoke()
                    }

                    DismissValue.DismissedToStart -> {
                        dismissToStartCallback?.invoke()
                    }

                    else -> Unit
                }
                false
            },
        )

        val threshold = 0.15f
        LaunchedEffect(dismissState) {
            snapshotFlow { dismissState.offset.value }.map { offset ->
                when {
                    offset > width * threshold -> DismissDirection.StartToEnd
                    offset < -width * threshold -> DismissDirection.EndToStart
                    else -> null
                }
            }.stateIn(this).onEach { willDismissDirection ->
                if (willDismissDirection != null) {
                    gestureBeginCallback?.invoke()
                }
            }.launchIn(this)
        }

        SwipeToDismiss2(
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
                    dismissState.dismissDirection ?: DismissDirection.StartToEnd
                val bgColor by animateColorAsState(
                    backgroundColor(dismissState.targetValue),
                )
                val alignment = when (direction) {
                    DismissDirection.StartToEnd -> Alignment.CenterStart
                    DismissDirection.EndToStart -> Alignment.CenterEnd
                }
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(bgColor)
                        .padding(horizontal = 20.dp),
                    contentAlignment = alignment,
                ) {
                    swipeContent(direction)
                }
            },
        ) {
            content()
        }
    } else {
        content()
    }
}

/*
 * Copied from androidx.material.SwipeToDismiss with different ResistanceConfig and velocity threshold.
 */
@Composable
@ExperimentalMaterialApi
private fun SwipeToDismiss2(
    state: DismissState,
    modifier: Modifier = Modifier,
    directions: Set<DismissDirection> = setOf(
        DismissDirection.EndToStart,
        DismissDirection.StartToEnd
    ),
    dismissThresholds: (DismissDirection) -> ThresholdConfig = {
        FixedThreshold(DISMISS_THRESHOLD)
    },
    background: @Composable RowScope.() -> Unit,
    dismissContent: @Composable RowScope.() -> Unit,
) = BoxWithConstraints(modifier) {
    val width = constraints.maxWidth.toFloat()
    val isRtl = LocalLayoutDirection.current == LayoutDirection.Rtl

    val anchors = mutableMapOf(0f to DismissValue.Default)
    if (DismissDirection.StartToEnd in directions) anchors += width to DismissValue.DismissedToEnd
    if (DismissDirection.EndToStart in directions) anchors += -width to DismissValue.DismissedToStart

    val thresholds = { from: DismissValue, to: DismissValue ->
        dismissThresholds(getDismissDirection(from, to)!!)
    }
    Box(
        Modifier.swipeable(
            state = state,
            anchors = anchors,
            thresholds = thresholds,
            orientation = Orientation.Horizontal,
            enabled = state.currentValue == DismissValue.Default,
            reverseDirection = isRtl,
            resistance = ResistanceConfig(
                basis = width,
                factorAtMin = SwipeableDefaults.StiffResistanceFactor,
                factorAtMax = SwipeableDefaults.StiffResistanceFactor
            ),
            velocityThreshold = Dp.Infinity
        )
    ) {
        Row(
            content = background,
            modifier = Modifier.matchParentSize()
        )
        Row(
            content = dismissContent,
            modifier = Modifier.offset { IntOffset(state.offset.value.roundToInt(), 0) }
        )
    }
}

private fun getDismissDirection(from: DismissValue, to: DismissValue): DismissDirection? {
    return when {
        // settled at the default state
        from == to && from == DismissValue.Default -> null
        // has been dismissed to the end
        from == to && from == DismissValue.DismissedToEnd -> DismissDirection.StartToEnd
        // has been dismissed to the start
        from == to && from == DismissValue.DismissedToStart -> DismissDirection.EndToStart
        // is currently being dismissed to the end
        from == DismissValue.Default && to == DismissValue.DismissedToEnd -> DismissDirection.StartToEnd
        // is currently being dismissed to the start
        from == DismissValue.Default && to == DismissValue.DismissedToStart -> DismissDirection.EndToStart
        // has been dismissed to the end but is now animated back to default
        from == DismissValue.DismissedToEnd && to == DismissValue.Default -> DismissDirection.StartToEnd
        // has been dismissed to the start but is now animated back to default
        from == DismissValue.DismissedToStart && to == DismissValue.Default -> DismissDirection.EndToStart
        else -> null
    }
}

private val DISMISS_THRESHOLD = 56.dp
