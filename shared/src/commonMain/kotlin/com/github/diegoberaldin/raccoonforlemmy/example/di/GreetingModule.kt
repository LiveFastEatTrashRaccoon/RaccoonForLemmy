package com.github.diegoberaldin.raccoonforlemmy.example.di

import com.github.diegoberaldin.raccoonforlemmy.example.Greeting
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val greetingModule = module {
    singleOf(::Greeting)
}
