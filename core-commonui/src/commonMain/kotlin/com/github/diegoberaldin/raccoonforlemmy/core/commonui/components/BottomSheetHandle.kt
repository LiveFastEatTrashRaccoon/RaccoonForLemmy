package com.github.diegoberaldin.raccoonforlemmy.core.commonui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BottomSheetHandle(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.width(60.dp)
            .height(1.dp)
            .background(
                color = MaterialTheme.colorScheme.onSurface,
                shape = RoundedCornerShape(1.dp),
            ),
    )
}