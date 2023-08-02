package com.github.diegoberaldin.raccoonforlemmy.core.appearance.di

import com.github.diegoberaldin.raccoonforlemmy.core.appearance.repository.ThemeRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

actual fun getThemeRepository(): ThemeRepository = CoreAppearanceHelper.repository

object CoreAppearanceHelper : KoinComponent {
    internal val repository: ThemeRepository by inject()
}
