package com.livefast.eattrash.raccoonforlemmy.unit.selectinstance.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.IconSize
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.utils.ValidationError
import com.livefast.eattrash.raccoonforlemmy.core.utils.compose.onClick
import com.livefast.eattrash.raccoonforlemmy.core.utils.toReadableMessage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ChangeInstanceDialog(
    instanceName: String = "",
    loading: Boolean = false,
    instanceNameError: ValidationError? = null,
    onChangeInstanceName: ((String) -> Unit)? = null,
    onClose: (() -> Unit)? = null,
    onSubmit: (() -> Unit)? = null,
) {
    BasicAlertDialog(
        onDismissRequest = {
            onClose?.invoke()
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
                text = LocalStrings.current.dialogTitleAddInstance,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
            )
            TextField(
                colors =
                TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                ),
                label = {
                    Text(text = LocalStrings.current.loginFieldInstanceName)
                },
                singleLine = true,
                value = instanceName,
                isError = instanceNameError != null,
                keyboardOptions =
                KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next,
                ),
                onValueChange = { value ->
                    onChangeInstanceName?.invoke(value)
                },
                supportingText = {
                    if (instanceNameError != null) {
                        Text(
                            text = instanceNameError.toReadableMessage(),
                            color = MaterialTheme.colorScheme.error,
                        )
                    }
                },
                trailingIcon = {
                    if (instanceName.isNotEmpty()) {
                        Icon(
                            modifier =
                            Modifier.onClick(
                                onClick = {
                                    onChangeInstanceName?.invoke("")
                                },
                            ),
                            imageVector = Icons.Default.Clear,
                            contentDescription = LocalStrings.current.actionClear,
                        )
                    }
                },
            )

            Button(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = {
                    onSubmit?.invoke()
                },
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Spacing.s),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(IconSize.s),
                            color = MaterialTheme.colorScheme.onPrimary,
                        )
                    }
                    Text(LocalStrings.current.buttonConfirm)
                }
            }
        }
    }
}
