package com.github.diegoberaldin.raccoonforlemmy.android

import android.app.Application
import com.github.diegoberaldin.raccoonforlemmy.sharedHelperModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MainApplication)
            androidLogger()
            modules(
                sharedHelperModule
            )
        }
    }
}