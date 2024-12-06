package com.livefast.eattrash.raccoonforlemmy.unit.acknowledgements.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module
@ComponentScan("com.livefast.eattrash.raccoonforlemmy.unit.acknowledgements.main")
internal class MainModule

@Module
@ComponentScan("com.livefast.eattrash.raccoonforlemmy.unit.acknowledgements.datasource")
internal class DataSourceModule

@Module
@ComponentScan("com.livefast.eattrash.raccoonforlemmy.unit.acknowledgements.repository")
internal class RepositoryModule

@Module(includes = [MainModule::class, DataSourceModule::class, RepositoryModule::class])
class AcknowledgementsModule
