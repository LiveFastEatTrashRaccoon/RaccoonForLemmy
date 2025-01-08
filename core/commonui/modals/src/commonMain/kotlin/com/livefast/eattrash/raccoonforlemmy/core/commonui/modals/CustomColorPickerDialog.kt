package com.livefast.eattrash.raccoonforlemmy.core.commonui.modals

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.CornerSize
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomColorPickerDialog(
    initialValue: Color,
    allowManualSelection: Boolean = false,
    onClose: ((Color?) -> Unit)? = null,
) {
    val controller = rememberColorPickerController()
    var selectedColor by remember { mutableStateOf(initialValue) }
    var selectedColorHex by remember { mutableStateOf("") }
    var manualInputDialogOpen by remember { mutableStateOf(false) }

    BasicAlertDialog(
        modifier = Modifier.clip(RoundedCornerShape(CornerSize.xxl)),
        onDismissRequest = {
            onClose?.invoke(null)
        },
    ) {
        Column(
            modifier =
                Modifier
                    .background(color = MaterialTheme.colorScheme.surfaceColorAtElevation(5.dp))
                    .padding(Spacing.m),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Spacing.xxs),
        ) {
            Text(
                text = LocalStrings.current.settingsColorDialogTitle,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Spacer(modifier = Modifier.height(Spacing.s))

            HsvColorPicker(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .padding(10.dp),
                controller = controller,
                initialColor = initialValue,
                onColorChanged = { colorEnvelope: ColorEnvelope ->
                    // do something
                    selectedColor = colorEnvelope.color
                    selectedColorHex = colorEnvelope.hexCode
                },
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier =
                        Modifier
                            .border(
                                color = selectedColor,
                                width = 1.dp,
                                shape = RoundedCornerShape(CornerSize.xxl),
                            ).padding(
                                horizontal = Spacing.m,
                                vertical = Spacing.s,
                            ),
                ) {
                    SelectionContainer {
                        Text(
                            text = "#$selectedColorHex",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = selectedColor,
                        )
                    }
                }
                if (allowManualSelection) {
                    FilledIconButton(
                        onClick = {
                            manualInputDialogOpen = true
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = LocalStrings.current.postActionEdit,
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(Spacing.s))

            Row(
                horizontalArrangement = Arrangement.spacedBy(Spacing.s),
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = {
                        onClose?.invoke(null)
                    },
                ) {
                    Text(text = LocalStrings.current.buttonCancel)
                }
                Button(
                    onClick = {
                        onClose?.invoke(selectedColor)
                    },
                ) {
                    Text(text = LocalStrings.current.buttonConfirm)
                }
            }
        }
    }

    if (manualInputDialogOpen) {
        var isError by remember { mutableStateOf(false) }
        var label by remember { mutableStateOf("") }
        val invalidFieldMessage = LocalStrings.current.messageInvalidField

        EditTextualInfoDialog(
            title = LocalStrings.current.settingsColorDialogInsertHex,
            value = selectedColorHex,
            label = label,
            isError = isError,
            singleLine = true,
            onClose = { value ->
                val newColor = value?.toColor()
                when {
                    value != null && newColor == null -> {
                        isError = true
                        label = invalidFieldMessage
                    }

                    newColor != null -> {
                        isError = false
                        label = ""
                        selectedColorHex = value
                        selectedColor = newColor
                        manualInputDialogOpen = false
                    }

                    else -> manualInputDialogOpen = false
                }
            },
        )
    }
}

private fun String.toColor(): Color? {
    val sanitized =
        if (startsWith("#")) {
            substring(1)
        } else {
            this
        }
    return when {
        sanitized.length == 6 || sanitized.length == 8 ->
            sanitized.toLongOrNull(16)?.let { Color(it) }

        else -> null
    }
}
