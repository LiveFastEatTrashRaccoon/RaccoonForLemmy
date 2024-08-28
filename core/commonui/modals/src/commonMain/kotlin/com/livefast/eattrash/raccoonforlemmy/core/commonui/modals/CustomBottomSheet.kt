package com.livefast.eattrash.raccoonforlemmy.core.commonui.modals

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomBottomSheet(
    isOpen: Boolean,
    sheetState: SheetState,
    sheetScope: CoroutineScope,
    onDismiss: (() -> Unit)? = null,
    onSelection: ((Int) -> Unit),
    headerText: String,
    contentPreTextPainter: List<Painter?>? = null,
    contentPreTextIcon: List<ImageVector?>? = null,
    contentText: List<String?>,
    contentPostTextPainter: List<Painter?>? = null,
    contentPostTextIcon: List<ImageVector?>? = null,
) {
    if (isOpen) {
        ModalBottomSheet(
            onDismissRequest = { onDismiss?.invoke() },
            sheetState = sheetState,
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = headerText,
                style = MaterialTheme.typography.titleMedium,
            )
            Column(
                modifier = Modifier
                    .padding(
                        top = Spacing.s,
                        start = Spacing.s,
                        end = Spacing.s,
                        bottom = Spacing.xxxl,
                    ),
            ) {
                contentText.forEachIndexed { index, text ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = Color.Transparent,
                                shape = MaterialTheme.shapes.large,
                            )
                            .clickable {
                                onSelection(index)
                                sheetScope.launch {
                                    sheetState.hide()
                                }.invokeOnCompletion {
                                    onDismiss?.invoke()
                                }
                            }
                            .padding(
                                horizontal = Spacing.interItem,
                                vertical = Spacing.interItem,
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val prePaint = contentPreTextPainter?.getOrNull(index)
                        val preIcon = contentPreTextIcon?.getOrNull(index)
                        if (prePaint != null || preIcon != null) {
                            BottomSheetRowIcon(
                                icon = preIcon,
                                painter = prePaint,
                                description = text,
                            )
                        }

                        Text(
                            modifier = Modifier.weight(1f),
                            text = text.toString(),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onBackground,
                        )

                        val postPaint = contentPostTextPainter?.getOrNull(index)
                        val postIcon = contentPostTextIcon?.getOrNull(index)

                        if (postPaint != null || postIcon != null) {
                            BottomSheetRowIcon(
                                icon = postIcon,
                                painter = postPaint,
                                description = text,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BottomSheetRowIcon(
    icon: ImageVector? = null,
    painter: Painter? = null,
    description: String? = "",
) {
    if (icon != null) {
        Icon(
            modifier = Modifier.padding(
                top = Spacing.xxs,
                end = Spacing.s,
                bottom = Spacing.xxs,
            ),
            imageVector = icon,
            contentDescription = description
        )
    }
    else if (painter != null) {
        Icon(
            modifier = Modifier.padding(
                top = Spacing.xxs,
                end = Spacing.s,
                bottom = Spacing.xxs,
            ),
            painter = painter,
            contentDescription = description
        )
    }
}
