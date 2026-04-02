package com.livefast.eattrash.raccoonforlemmy.core.resources

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import com.livefast.eattrash.raccoonforlemmy.core.resources.di.getCoreResources

val LocalResources: ProvidableCompositionLocal<CoreResources> =
    staticCompositionLocalOf { getCoreResources() }

@Composable
fun ProvideResources(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        value = LocalResources provides getCoreResources(),
        content = content,
    )
}
