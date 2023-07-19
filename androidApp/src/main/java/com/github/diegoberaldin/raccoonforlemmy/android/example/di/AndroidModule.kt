package com.github.diegoberaldin.raccoonforlemmy.android.example.di

import com.github.diegoberaldin.raccoonforlemmy.android.example.presentation.GreetPresenter
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val androidModule = module {
    singleOf(::GreetPresenter)
}