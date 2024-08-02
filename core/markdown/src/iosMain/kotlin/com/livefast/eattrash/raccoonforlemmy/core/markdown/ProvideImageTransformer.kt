package com.livefast.eattrash.raccoonforlemmy.core.markdown

import com.mikepenz.markdown.model.ImageTransformer
import com.mikepenz.markdown.model.NoOpImageTransformerImpl

actual fun provideImageTransformer(): ImageTransformer = NoOpImageTransformerImpl()
