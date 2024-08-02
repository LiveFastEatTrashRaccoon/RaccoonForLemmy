package com.livefast.eattrash.raccoonforlemmy.core.utils.network

import io.ktor.client.engine.HttpClientEngineFactory

expect fun provideHttpClientEngineFactory(): HttpClientEngineFactory<*>
