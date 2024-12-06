package com.livefast.eattrash.raccoonforlemmy.feature.profile.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module
@ComponentScan("com.livefast.eattrash.raccoonforlemmy.feature.profile.main")
internal class ProfileMainModule

@Module
@ComponentScan("com.livefast.eattrash.raccoonforlemmy.feature.profile.menu")
internal class ProfileMenuModule

@Module
@ComponentScan("com.livefast.eattrash.raccoonforlemmy.feature.profile.notlogged")
internal class ProfileNotLoggedModule

@Module(
    includes = [
        ProfileMainModule::class,
        ProfileMenuModule::class,
        ProfileNotLoggedModule::class,
    ],
)
class ProfileTabModule
