package com.github.diegoberaldin.raccoonforlemmy.profile

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val profileTabModule = module {
    factoryOf(::ProfileScreenModel)
}