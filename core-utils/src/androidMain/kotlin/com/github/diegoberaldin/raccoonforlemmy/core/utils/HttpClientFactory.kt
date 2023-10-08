package com.github.diegoberaldin.raccoonforlemmy.core.utils

import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.android.Android

actual fun provideHttpClientEngineFactory(): HttpClientEngineFactory<*> {
    return Android
}