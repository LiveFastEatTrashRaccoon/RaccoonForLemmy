package com.github.diegoberaldin.raccoonforlemmy.inbox

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val inboxTabModule = module {
    factoryOf(::InboxScreenModel)
}