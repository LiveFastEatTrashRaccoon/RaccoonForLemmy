package com.livefast.eattrash.raccoonforlemmy.core.utils

import androidx.compose.foundation.layout.imePadding
import androidx.compose.ui.Modifier

actual fun Modifier.safeImePadding(): Modifier = then(Modifier.imePadding())
