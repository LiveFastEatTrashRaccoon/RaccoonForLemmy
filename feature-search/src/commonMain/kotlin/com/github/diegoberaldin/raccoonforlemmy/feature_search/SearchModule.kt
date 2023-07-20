package com.github.diegoberaldin.raccoonforlemmy.feature_search

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val searchTabModule = module {
    factoryOf(::SearchScreenModel)
}