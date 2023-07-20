package com.github.diegoberaldin.raccoonforlemmy.core_preferences.di

import com.github.diegoberaldin.raccoonforlemmy.core_preferences.TemporaryKeyStore
import org.koin.core.module.Module

expect val corePreferencesModule: Module

expect fun getTemporaryKeyStore(): TemporaryKeyStore