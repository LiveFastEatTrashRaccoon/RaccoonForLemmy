package com.livefast.eattrash.raccoonforlemmy.core.utils.compose

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Indication
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Modifier.onClick(
    indication: Indication? = LocalIndication.current,
    onClick: () -> Unit = {},
    onDoubleClick: () -> Unit = {},
    onLongClick: () -> Unit = {},
): Modifier =
    combinedClickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = indication,
        onClick = {
            onClick()
        },
        onDoubleClick = {
            onDoubleClick()
        },
        onLongClick = {
            onLongClick()
        },
    )
