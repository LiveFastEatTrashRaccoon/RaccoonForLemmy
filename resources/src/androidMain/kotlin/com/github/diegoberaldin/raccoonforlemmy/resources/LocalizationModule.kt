package com.github.diegoberaldin.raccoonforlemmy.resources

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent.inject

actual val localizationModule = module {
    singleOf<LanguageRepository>(::DefaultLanguageRepository)
}

actual fun getLanguageRepository(): LanguageRepository {
    val res: LanguageRepository by inject(LanguageRepository::class.java)
    return res
}
