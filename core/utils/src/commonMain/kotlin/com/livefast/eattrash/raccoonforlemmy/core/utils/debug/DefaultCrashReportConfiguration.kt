package com.livefast.eattrash.raccoonforlemmy.core.utils.debug

import org.koin.core.annotation.Single

@Single
internal expect class DefaultCrashReportConfiguration : CrashReportConfiguration {
    override fun isEnabled(): Boolean

    override fun setEnabled(value: Boolean)
}
