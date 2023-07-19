package com.github.diegoberaldin.raccoonforlemmy.android.di

import com.github.diegoberaldin.raccoonforlemmy.android.presentation.GreetPresenter
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val androidModule = module {
    singleOf(::GreetPresenter)
}