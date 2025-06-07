package com.livefast.eattrash.raccoonforlemmy.unit.accountsettings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.SettingsSwitchRow
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.utils.ValidationError
import com.livefast.eattrash.raccoonforlemmy.core.utils.toReadableMessage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DeleteAccountDialog(
    validationError: ValidationError? = null,
    onDismiss: (() -> Unit)? = null,
    onConfirm: ((String, Boolean) -> Unit)? = null,
) {
    var deleteContent by remember { mutableStateOf(false) }
    var textFieldValue by remember {
        mutableStateOf(TextFieldValue(text = ""))
    }
    var transformation: VisualTransformation by remember {
        mutableStateOf(PasswordVisualTransformation())
    }

    BasicAlertDialog(
        onDismissRequest = {
            onDismiss?.invoke()
        },
    ) {
        Column(
            modifier =
            Modifier
                .background(color = MaterialTheme.colorScheme.surface)
                .padding(Spacing.s),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Spacing.xxs),
        ) {
            Text(
                text = LocalStrings.current.actionDeleteAccount,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Spacer(modifier = Modifier.height(Spacing.s))
            Text(
                text = LocalStrings.current.deleteAccountBody,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
            )

            SettingsSwitchRow(
                title = LocalStrings.current.deleteAccountRemoveContent,
                value = deleteContent,
                onChangeValue = {
                    deleteContent = it
                },
            )
            Spacer(modifier = Modifier.height(Spacing.xs))

            TextField(
                modifier = Modifier.fillMaxWidth(),
                colors =
                TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                ),
                isError = validationError != null,
                singleLine = true,
                label = {
                    Text(
                        text = LocalStrings.current.loginFieldPassword,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                },
                supportingText = {
                    if (validationError != null) {
                        Text(
                            text = validationError.toReadableMessage(),
                            color = MaterialTheme.colorScheme.error,
                        )
                    }
                },
                textStyle = MaterialTheme.typography.bodyMedium,
                value = textFieldValue,
                keyboardOptions =
                KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    autoCorrectEnabled = true,
                ),
                onValueChange = { value ->
                    textFieldValue = value
                },
                visualTransformation = transformation,
                trailingIcon = {
                    IconButton(
                        onClick = {
                            transformation =
                                if (transformation == VisualTransformation.None) {
                                    PasswordVisualTransformation()
                                } else {
                                    VisualTransformation.None
                                }
                        },
                    ) {
                        Icon(
                            imageVector =
                            if (transformation == VisualTransformation.None) {
                                Icons.Default.VisibilityOff
                            } else {
                                Icons.Default.Visibility
                            },
                            contentDescription = LocalStrings.current.actionToggleVisibility,
                        )
                    }
                },
            )

            Spacer(modifier = Modifier.height(Spacing.xs))

            Row(
                horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = {
                        onDismiss?.invoke()
                    },
                ) {
                    Text(text = LocalStrings.current.buttonCancel)
                }
                Button(
                    onClick = {
                        onConfirm?.invoke(textFieldValue.text, deleteContent)
                    },
                ) {
                    Text(text = LocalStrings.current.buttonConfirm)
                }
            }
        }
    }
}
