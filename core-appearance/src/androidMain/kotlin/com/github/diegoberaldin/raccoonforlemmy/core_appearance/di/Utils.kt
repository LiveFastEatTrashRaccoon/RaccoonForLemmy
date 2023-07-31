package com.github.diegoberaldin.raccoonforlemmy.core_appearance.di

import com.github.diegoberaldin.raccoonforlemmy.core_appearance.repository.ThemeRepository
import org.koin.java.KoinJavaComponent.inject

actual fun getThemeRepository(): ThemeRepository {
    val res: ThemeRepository by inject(ThemeRepository::class.java)
    return res
}
