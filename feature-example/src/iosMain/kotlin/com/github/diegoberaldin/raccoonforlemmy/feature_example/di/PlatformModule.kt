package com.github.diegoberaldin.raccoonforlemmy.feature_example.di

import com.github.diegoberaldin.raccoonforlemmy.feature_example.IOSPlatform
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    single<Platform> { IOSPlatform() }
}
