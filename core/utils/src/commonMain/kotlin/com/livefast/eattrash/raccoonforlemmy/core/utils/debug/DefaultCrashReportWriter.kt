package com.livefast.eattrash.raccoonforlemmy.core.utils.debug

import org.koin.core.annotation.Single

@Single
internal expect class DefaultCrashReportWriter : CrashReportWriter {
    override fun write(reportText: String)
}
