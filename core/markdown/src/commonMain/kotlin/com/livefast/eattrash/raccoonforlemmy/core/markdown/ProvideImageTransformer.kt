package com.livefast.eattrash.raccoonforlemmy.core.markdown

import com.mikepenz.markdown.coil3.Coil3ImageTransformerImpl
import com.mikepenz.markdown.model.ImageTransformer

internal fun provideImageTransformer(): ImageTransformer = Coil3ImageTransformerImpl
