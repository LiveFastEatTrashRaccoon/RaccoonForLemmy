package com.github.diegoberaldin.raccoonforlemmy.core.commonui.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpOffset

@Composable
expect fun Dropdown(
    expanded: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    offset: DpOffset,
    content: @Composable ColumnScope.() -> Unit,
)
