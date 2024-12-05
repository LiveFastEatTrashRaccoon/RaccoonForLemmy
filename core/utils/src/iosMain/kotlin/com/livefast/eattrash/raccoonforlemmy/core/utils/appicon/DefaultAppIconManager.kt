package com.livefast.eattrash.raccoonforlemmy.core.utils.appicon

import org.koin.core.annotation.Single

@Single
internal actual class DefaultAppIconManager : AppIconManager {
    actual override val supportsMultipleIcons = false

    actual override fun changeIcon(variant: AppIconVariant) {
        // no-op
    }
}
