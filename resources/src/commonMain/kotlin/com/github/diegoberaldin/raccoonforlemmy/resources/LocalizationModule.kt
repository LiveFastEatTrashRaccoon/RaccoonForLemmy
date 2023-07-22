package com.github.diegoberaldin.raccoonforlemmy.resources

import org.koin.core.module.Module

expect val localizationModule: Module

expect fun getLanguageRepository(): LanguageRepository
