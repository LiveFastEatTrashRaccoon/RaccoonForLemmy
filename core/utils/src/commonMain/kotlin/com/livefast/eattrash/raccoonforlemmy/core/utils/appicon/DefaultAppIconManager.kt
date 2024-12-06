package com.livefast.eattrash.raccoonforlemmy.core.utils.appicon

import org.koin.core.annotation.Single

@Single
internal expect class DefaultAppIconManager : AppIconManager {
    override val supportsMultipleIcons: Boolean

    override fun changeIcon(variant: AppIconVariant)
}
