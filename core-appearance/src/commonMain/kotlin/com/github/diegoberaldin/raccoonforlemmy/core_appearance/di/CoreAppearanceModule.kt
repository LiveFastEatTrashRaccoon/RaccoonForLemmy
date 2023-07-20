package com.github.diegoberaldin.raccoonforlemmy.core_appearance.di

import com.github.diegoberaldin.raccoonforlemmy.core_appearance.repository.ThemeRepository
import org.koin.core.module.Module

expect val coreAppearanceModule: Module

expect fun getThemeRepository(): ThemeRepository

