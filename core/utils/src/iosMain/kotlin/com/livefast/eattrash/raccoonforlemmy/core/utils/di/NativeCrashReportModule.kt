package com.livefast.eattrash.raccoonforlemmy.core.utils.di

import com.livefast.eattrash.raccoonforlemmy.core.utils.debug.AppInfoRepository
import com.livefast.eattrash.raccoonforlemmy.core.utils.debug.CrashReportConfiguration
import com.livefast.eattrash.raccoonforlemmy.core.utils.debug.CrashReportWriter
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton

internal actual val nativeCrashReportModule =
    DI.Module("NativeCrashReportModule") {
        bind<CrashReportConfiguration> {
            singleton {
                DefaultCrashReportConfiguration()
            }
        }
        bind<CrashReportWriter> {
            singleton {
                DefaultCrashReportWriter()
            }
        }
        bind<AppInfoRepository> {
            singleton {
                DefaultAppInfoRepository()
            }
        }
    }
