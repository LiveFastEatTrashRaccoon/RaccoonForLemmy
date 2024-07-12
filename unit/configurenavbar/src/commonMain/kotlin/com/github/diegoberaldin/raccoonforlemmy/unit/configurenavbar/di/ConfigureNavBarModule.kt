package com.github.diegoberaldin.raccoonforlemmy.unit.configurenavbar.di

import com.github.diegoberaldin.raccoonforlemmy.unit.configurenavbar.ConfigureNavBarMviModel
import com.github.diegoberaldin.raccoonforlemmy.unit.configurenavbar.ConfigureNavBarViewModel
import org.koin.dsl.module

val configureNavBarModule =
    module {
        single<ConfigureNavBarMviModel> {
            ConfigureNavBarViewModel(
                accountRepository = get(),
                identityRepository = get(),
                bottomNavItemsRepository = get(),
                settingsRepository = get(),
                hapticFeedback = get(),
                notificationCenter = get(),
            )
        }
    }
