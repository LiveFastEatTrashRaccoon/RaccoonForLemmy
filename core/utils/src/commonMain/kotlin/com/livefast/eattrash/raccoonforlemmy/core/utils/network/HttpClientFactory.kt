package com.livefast.eattrash.raccoonforlemmy.core.utils.network

import io.ktor.client.engine.HttpClientEngine

expect fun provideHttpClientEngine(): HttpClientEngine
