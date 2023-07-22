package com.github.diegoberaldin.raccoonforlemmy.feature_home

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val homeTabModule = module {
    factoryOf(::HomeScreenModel)
}