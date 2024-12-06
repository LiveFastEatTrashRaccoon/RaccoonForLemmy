package com.livefast.eattrash.raccoonforlemmy.core.utils.url

import org.koin.core.annotation.Single

@Single
internal actual class DefaultCustomTabsHelper : CustomTabsHelper {
    actual override val isSupported = false

    actual override fun handle(url: String) {
        // no-op
    }
}
