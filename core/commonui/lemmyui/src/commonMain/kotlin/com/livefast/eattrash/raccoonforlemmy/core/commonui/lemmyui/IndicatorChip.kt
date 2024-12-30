package com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.CornerSize
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing

@Composable
fun IndicatorChip(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onBackground,
    full: Boolean = false,
) {
    val shape = RoundedCornerShape(CornerSize.m)
    Box(
        modifier =
            modifier
                .border(
                    color = color,
                    width = Dp.Hairline,
                    shape = shape,
                ).then(
                    if (full) {
                        Modifier.background(color, shape)
                    } else {
                        Modifier
                    },
                ).padding(
                    vertical = Spacing.xxxs,
                    horizontal = Spacing.xs,
                ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            fontSize = 8.sp,
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.SemiBold,
            color =
                if (full) {
                    getReadableColorFor(color)
                } else {
                    color
                },
        )
    }
}

private fun getReadableColorFor(backgroundColor: Color): Color {
    val r = backgroundColor.red
    val g = backgroundColor.green
    val b = backgroundColor.blue
    // calculate the Y (luma) component of the YIQ color space
    val y = ((r * 0.299) + (g * 0.587) + (b * 0.114))
    return if (y >= 0.5) Color.Black else Color.White
}
