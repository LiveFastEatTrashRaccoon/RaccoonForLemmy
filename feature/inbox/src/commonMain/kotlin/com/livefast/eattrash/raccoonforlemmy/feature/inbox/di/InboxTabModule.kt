package com.livefast.eattrash.raccoonforlemmy.feature.inbox.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module
@ComponentScan("com.livefast.eattrash.raccoonforlemmy.feature.inbox.main")
internal class InboxMainbodule

@Module(includes = [InboxMainbodule::class])
class InboxTabModule
