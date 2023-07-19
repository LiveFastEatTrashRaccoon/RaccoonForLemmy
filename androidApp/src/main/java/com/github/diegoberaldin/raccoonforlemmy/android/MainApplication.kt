package com.github.diegoberaldin.raccoonforlemmy.android

import android.app.Application
import com.github.diegoberaldin.raccoonforlemmy.android.example.di.androidModule
import com.github.diegoberaldin.raccoonforlemmy.example.di.greetingModule
import com.github.diegoberaldin.raccoonforlemmy.example.di.platformModule
import com.github.diegoberaldin.raccoonforlemmy.main.di.appModule
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
                greetingModule,
                platformModule,
                appModule,
                androidModule,
            )
        }
    }
}