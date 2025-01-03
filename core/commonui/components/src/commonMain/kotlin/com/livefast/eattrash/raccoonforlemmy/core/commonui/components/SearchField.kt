package com.livefast.eattrash.raccoonforlemmy.core.commonui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.IconSize
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.ancillaryTextAlpha
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.utils.toLocalDp

@Composable
fun SearchField(
    hint: String? = null,
    hintTextStyle: TextStyle = MaterialTheme.typography.bodySmall,
    value: String,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    onValueChange: (String) -> Unit,
    onClear: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions =
        KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Search,
        ),
) {
    var textFieldValue by remember {
        mutableStateOf(
            TextFieldValue(text = value),
        )
    }
    LaunchedEffect(textFieldValue) {
        onValueChange(textFieldValue.text)
    }
    var height by remember { mutableStateOf(0f) }
    BasicTextField(
        modifier =
            modifier.onGloballyPositioned {
                height = it.size.toSize().height
            },
        value = textFieldValue,
        onValueChange = { newValue ->
            textFieldValue = newValue
        },
        keyboardOptions = keyboardOptions,
        maxLines = 1,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.onBackground),
        textStyle = textStyle.copy(color = MaterialTheme.colorScheme.onBackground),
        decorationBox =
            { innerTextField ->
                Row(
                    modifier =
                        Modifier
                            .background(
                                color = MaterialTheme.colorScheme.surfaceColorAtElevation(5.dp),
                                shape = RoundedCornerShape((height / 2).toLocalDp()),
                            ).padding(
                                horizontal = 12.dp,
                                vertical = Spacing.s,
                            ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
                ) {
                    val iconModifier = Modifier.size(IconSize.m).padding(2.5.dp)
                    Icon(
                        modifier = iconModifier,
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground,
                    )
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.CenterStart,
                    ) {
                        innerTextField()

                        if (value.isEmpty() && hint != null) {
                            Text(
                                text = hint,
                                color =
                                    MaterialTheme.colorScheme.onBackground.copy(
                                        ancillaryTextAlpha,
                                    ),
                                style = hintTextStyle,
                            )
                        }
                    }

                    if (value.isNotEmpty() && onClear != null) {
                        IconButton(
                            modifier = iconModifier,
                            onClick = {
                                textFieldValue = TextFieldValue()
                            },
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = LocalStrings.current.actionClear,
                                tint = MaterialTheme.colorScheme.onBackground,
                            )
                        }
                    }
                }
            },
    )
}
