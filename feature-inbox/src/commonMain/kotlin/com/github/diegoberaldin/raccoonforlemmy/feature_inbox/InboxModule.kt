package com.github.diegoberaldin.raccoonforlemmy.feature_inbox

import org.koin.core.module.Module

expect val inboxTabModule: Module

expect fun getInboxScreenModel(): InboxScreenModel