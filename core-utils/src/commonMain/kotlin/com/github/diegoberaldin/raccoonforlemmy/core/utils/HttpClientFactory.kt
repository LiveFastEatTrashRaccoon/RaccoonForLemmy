package com.github.diegoberaldin.raccoonforlemmy.core.utils

import io.ktor.client.engine.HttpClientEngineFactory

expect fun provideHttpClientEngineFactory(): HttpClientEngineFactory<*>