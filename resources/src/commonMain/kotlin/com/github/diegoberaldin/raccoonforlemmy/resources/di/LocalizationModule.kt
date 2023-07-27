package com.github.diegoberaldin.raccoonforlemmy.resources.di

import com.github.diegoberaldin.raccoonforlemmy.resources.LanguageRepository
import dev.icerock.moko.resources.desc.StringDesc
import org.koin.core.module.Module

expect val localizationModule: Module

expect fun getLanguageRepository(): LanguageRepository

expect fun staticString(stringDesc: StringDesc): String
