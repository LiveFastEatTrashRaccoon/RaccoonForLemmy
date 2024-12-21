package com.livefast.eattrash.raccoonforlemmy.domain.inbox.di

import com.livefast.eattrash.raccoonforlemmy.domain.inbox.coordinator.DefaultInboxCoordinator
import com.livefast.eattrash.raccoonforlemmy.domain.inbox.coordinator.InboxCoordinator
import com.livefast.eattrash.raccoonforlemmy.domain.inbox.usecase.DefaultGetUnreadItemsUseCase
import com.livefast.eattrash.raccoonforlemmy.domain.inbox.usecase.GetUnreadItemsUseCase
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

val inboxModule =
    DI.Module("DomainInboxModule") {
        import(nativeInboxModule)

        bind<InboxCoordinator> {
            singleton {
                DefaultInboxCoordinator(
                    identityRepository = instance(),
                    getUnreadItemsUseCase = instance(),
                )
            }
        }
        bind<GetUnreadItemsUseCase> {
            singleton {
                DefaultGetUnreadItemsUseCase(
                    identityRepository = instance(),
                    userRepository = instance(),
                    messageRepository = instance(),
                )
            }
        }
    }
