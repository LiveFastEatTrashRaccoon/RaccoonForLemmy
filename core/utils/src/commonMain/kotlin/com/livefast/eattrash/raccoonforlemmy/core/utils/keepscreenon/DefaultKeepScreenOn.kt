package com.livefast.eattrash.raccoonforlemmy.core.utils.keepscreenon

import org.koin.core.annotation.Single

@Single
internal expect class DefaultKeepScreenOn : KeepScreenOn {
    override fun activate()

    override fun deactivate()
}
