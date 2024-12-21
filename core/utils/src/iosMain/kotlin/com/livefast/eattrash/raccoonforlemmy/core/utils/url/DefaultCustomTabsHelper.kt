package com.livefast.eattrash.raccoonforlemmy.core.utils.url

internal class DefaultCustomTabsHelper : CustomTabsHelper {
    override val isSupported = false

    override fun handle(url: String) {
        // no-op
    }
}
