package com.github.diegoberaldin.raccoonforlemmy.di

import com.github.diegoberaldin.raccoonforlemmy.MainViewModel
import org.koin.dsl.module

internal val internalSharedModule = module {
    factory { MainViewModel() }
}