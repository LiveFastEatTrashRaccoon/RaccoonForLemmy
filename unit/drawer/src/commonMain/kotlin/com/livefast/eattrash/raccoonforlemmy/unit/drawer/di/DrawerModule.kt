package com.livefast.eattrash.raccoonforlemmy.unit.drawer.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module
@ComponentScan("com.livefast.eattrash.raccoonforlemmy.unit.drawer.content")
internal class DrawerContentModule

@Module
@ComponentScan("com.livefast.eattrash.raccoonforlemmy.unit.drawer.cache")
internal class SubscriptionsCacheModule

@Module(includes = [DrawerContentModule::class, SubscriptionsCacheModule::class])
class DrawerModule
