package com.livefast.eattrash.raccoonforlemmy.core.utils

import androidx.compose.foundation.layout.imePadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
actual fun Modifier.safeImePadding(): Modifier = then(Modifier.imePadding())
