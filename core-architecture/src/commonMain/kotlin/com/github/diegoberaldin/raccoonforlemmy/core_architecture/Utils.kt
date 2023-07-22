package com.github.diegoberaldin.raccoonforlemmy.core_architecture

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect

@Composable
fun MviModel<*, *, *>.bindToLifecycle(key: Any = Unit) {
    DisposableEffect(key) {
        onStarted()
        onDispose(::onDisposed)
    }
}