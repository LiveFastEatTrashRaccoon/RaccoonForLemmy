package com.github.diegoberaldin.raccoonforlemmy.core.commonui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.CornerSize
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.Spacing
import com.github.diegoberaldin.raccoonforlemmy.core.utils.onClick

data class FloatingActionButtonMenuItem(
    val icon: ImageVector,
    val text: String,
    val onSelected: (() -> Unit)? = null,
)

@Composable
fun FloatingActionButtonMenu(
    items: List<FloatingActionButtonMenuItem> = emptyList(),
) {
    var fabExpanded by remember { mutableStateOf(false) }
    val fabRotation by animateFloatAsState(if (fabExpanded) 45f else 0f)
    val enterTransition = remember {
        expandVertically(
            expandFrom = Alignment.Bottom,
            animationSpec = tween(150, easing = FastOutSlowInEasing)
        ) + fadeIn(
            initialAlpha = 0.3f,
            animationSpec = tween(150, easing = FastOutSlowInEasing)
        )
    }
    val exitTransition = remember {
        shrinkVertically(
            shrinkTowards = Alignment.Bottom,
            animationSpec = tween(150, easing = FastOutSlowInEasing)
        ) + fadeOut(
            animationSpec = tween(150, easing = FastOutSlowInEasing)
        )
    }
    Column(
        horizontalAlignment = Alignment.End,
    ) {
        AnimatedVisibility(
            visible = fabExpanded,
            enter = enterTransition,
            exit = exitTransition
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(Spacing.xs)
            ) {
                Spacer(modifier = Modifier.height(Spacing.m))
                for (item in items) {
                    Row(
                        modifier = Modifier.onClick {
                            fabExpanded = false
                            item.onSelected?.invoke()
                        },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(Spacing.xxs)
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(26.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.secondary,
                                    shape = CircleShape
                                ).padding(6.dp),
                            imageVector = item.icon,
                            contentDescription = null,
                        )
                        Text(
                            modifier = Modifier.background(
                                color = MaterialTheme.colorScheme.background,
                                shape = RoundedCornerShape(CornerSize.s),
                            ).padding(vertical = Spacing.xs, horizontal = Spacing.s),
                            text = item.text,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
                Spacer(modifier = Modifier.height(Spacing.xxs))
            }
        }
        FloatingActionButton(
            shape = CircleShape,
            backgroundColor = MaterialTheme.colorScheme.secondary,
            onClick = {
                fabExpanded = !fabExpanded
            },
            content = {
                Icon(
                    modifier = Modifier.rotate(fabRotation),
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                )
            },
        )
    }
}
