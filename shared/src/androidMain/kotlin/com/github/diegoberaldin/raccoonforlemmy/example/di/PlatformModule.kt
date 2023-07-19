package com.github.diegoberaldin.raccoonforlemmy.example.di

import com.github.diegoberaldin.raccoonforlemmy.example.AndroidPlatform
import com.github.diegoberaldin.raccoonforlemmy.example.Platform
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    single<Platform> { AndroidPlatform() }
}

