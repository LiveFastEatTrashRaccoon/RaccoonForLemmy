package com.github.diegoberaldin.raccoonforlemmy.core.commonui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.github.diegoberaldin.racconforlemmy.core.utils.onClick
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.CornerSize
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.Spacing

@Composable
fun SectionSelector(
    modifier: Modifier = Modifier,
    titles: List<String> = emptyList(),
    currentSection: Int,
    onSectionSelected: (Int) -> Unit,
) {
    val highlightColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
    Row(
        modifier = modifier
            .height(34.dp)
            .padding(horizontal = Spacing.m)
            .fillMaxWidth()
            .border(
                color = highlightColor,
                width = 1.dp,
                shape = RoundedCornerShape(CornerSize.m),
            ),
    ) {
        titles.forEachIndexed { i, title ->
            Surface(
                color = Color.Transparent,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(bottom = Spacing.xxs)
                    .onClick {
                        onSectionSelected(i)
                    },
            ) {
                Box(
                    modifier = Modifier.fillMaxSize().let {
                        if (currentSection == i) {
                            it.background(
                                color = highlightColor,
                                shape = when (i) {
                                    0 -> RoundedCornerShape(
                                        topStart = CornerSize.m,
                                        bottomStart = CornerSize.m,
                                    )

                                    titles.lastIndex -> {
                                        RoundedCornerShape(
                                            topEnd = CornerSize.m,
                                            bottomEnd = CornerSize.m,
                                        )
                                    }

                                    else -> RectangleShape
                                },
                            )
                        } else {
                            it
                        }
                    },
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
            if (i < titles.lastIndex) {
                Box(
                    modifier = Modifier.width(1.dp).fillMaxHeight()
                        .background(color = highlightColor),
                )
            }
        }
    }
}
