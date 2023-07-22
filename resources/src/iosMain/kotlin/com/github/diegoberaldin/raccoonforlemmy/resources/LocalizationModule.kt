package com.github.diegoberaldin.raccoonforlemmy.resources

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual val localizationModule = module {
    singleOf<LanguageRepository>(::DefaultLanguageRepository)
}

actual fun getLanguageRepository(): LanguageRepository = LanguageRepositoryHelper.repository

object LanguageRepositoryHelper : KoinComponent {
    val repository: LanguageRepository by inject()
}