package com.github.diegoberaldin.raccoonforlemmy.search

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val searchTabModule = module {
    factoryOf(::SearchScreenModel)
}