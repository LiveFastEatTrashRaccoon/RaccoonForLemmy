package com.livefast.eattrash.raccoonforlemmy.domain.inbox.di

import com.livefast.eattrash.raccoonforlemmy.domain.inbox.DefaultInboxCoordinator
import com.livefast.eattrash.raccoonforlemmy.domain.inbox.InboxCoordinator
import com.livefast.eattrash.raccoonforlemmy.domain.inbox.usecase.DefaultGetUnreadItemsUseCase
import com.livefast.eattrash.raccoonforlemmy.domain.inbox.usecase.GetUnreadItemsUseCase
import org.koin.dsl.module

val domainInboxModule =
    module {
        includes(inboxNativeModule)
        single<InboxCoordinator> {
            DefaultInboxCoordinator(
                identityRepository = get(),
                getUnreadItemsUseCase = get(),
            )
        }
        single<GetUnreadItemsUseCase> {
            DefaultGetUnreadItemsUseCase(
                identityRepository = get(),
                userRepository = get(),
                messageRepository = get(),
            )
        }
    }
