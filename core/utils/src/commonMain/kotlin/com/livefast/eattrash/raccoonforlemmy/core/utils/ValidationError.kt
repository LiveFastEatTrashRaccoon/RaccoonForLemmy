package com.livefast.eattrash.raccoonforlemmy.core.utils

import androidx.compose.runtime.Composable
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings

sealed interface ValidationError {
    data object InvalidField : ValidationError

    data object MissingField : ValidationError
}

@Composable
fun ValidationError.toReadableMessage(): String =
    when (this) {
        ValidationError.InvalidField -> LocalStrings.current.messageInvalidField
        ValidationError.MissingField -> LocalStrings.current.messageMissingField
    }
