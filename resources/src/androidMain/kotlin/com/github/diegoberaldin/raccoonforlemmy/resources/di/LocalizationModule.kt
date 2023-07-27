package com.github.diegoberaldin.raccoonforlemmy.resources.di

import android.content.Context
import com.github.diegoberaldin.raccoonforlemmy.resources.DefaultLanguageRepository
import com.github.diegoberaldin.raccoonforlemmy.resources.LanguageRepository
import dev.icerock.moko.resources.desc.StringDesc
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent.inject

actual val localizationModule = module {
    singleOf<LanguageRepository>(::DefaultLanguageRepository)
    singleOf(::ResourceStringProvider)
}

actual fun getLanguageRepository(): LanguageRepository {
    val res: LanguageRepository by inject(LanguageRepository::class.java)
    return res
}

private class ResourceStringProvider(
    private val context: Context,
) {
    fun getString(stringDesc: StringDesc): String = stringDesc.toString(context)
}

actual fun staticString(stringDesc: StringDesc): String {
    val provider by inject<ResourceStringProvider>(ResourceStringProvider::class.java)
    return provider.getString(stringDesc)
}