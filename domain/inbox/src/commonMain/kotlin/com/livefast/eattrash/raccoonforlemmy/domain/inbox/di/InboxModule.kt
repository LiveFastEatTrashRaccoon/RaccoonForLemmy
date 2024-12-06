package com.livefast.eattrash.raccoonforlemmy.domain.inbox.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module
@ComponentScan("com.livefast.eattrash.raccoonforlemmy.domain.inbox.coordinator")
internal class CoordinatorModule

@Module
@ComponentScan("com.livefast.eattrash.raccoonforlemmy.domain.inbox.usecase")
internal class UseCaseModule

@Module(includes = [CoordinatorModule::class, UseCaseModule::class, NotificationModule::class])
class InboxModule
