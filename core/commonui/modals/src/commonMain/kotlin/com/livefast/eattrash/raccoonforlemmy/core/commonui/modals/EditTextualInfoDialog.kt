package com.livefast.eattrash.raccoonforlemmy.core.commonui.modals

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import com.livefast.eattrash.raccoonforlemmy.core.appearance.di.getThemeRepository
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.toTypography
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTextualInfoDialog(
    title: String,
    modifier: Modifier = Modifier,
    label: String = "",
    isError: Boolean = false,
    singleLine: Boolean = false,
    value: String = "",
    onClose: ((String?) -> Unit)? = null,
) {
    var textFieldValue by remember {
        mutableStateOf(TextFieldValue(text = value))
    }
    val themeRepository = remember { getThemeRepository() }
    val contentFontFamily by themeRepository.contentFontFamily.collectAsState()
    val typography = contentFontFamily.toTypography()

    BasicAlertDialog(
        modifier = modifier,
        onDismissRequest = {
            onClose?.invoke(null)
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
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Spacer(modifier = Modifier.height(Spacing.s))

            TextField(
                modifier = Modifier.fillMaxWidth(),
                colors =
                TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                ),
                isError = isError,
                singleLine = singleLine,
                label = {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                },
                textStyle = typography.bodyMedium,
                value = textFieldValue,
                keyboardOptions =
                KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    autoCorrectEnabled = true,
                ),
                onValueChange = { value ->
                    textFieldValue = value
                },
            )

            Spacer(modifier = Modifier.height(Spacing.xs))
            Button(
                onClick = {
                    onClose?.invoke(textFieldValue.text)
                },
            ) {
                Text(text = LocalStrings.current.buttonConfirm)
            }
        }
    }
}
