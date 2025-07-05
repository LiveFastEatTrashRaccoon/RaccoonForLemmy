package com.livefast.eattrash.raccoonforlemmy.feature.settings.di

import com.livefast.eattrash.raccoonforlemmy.core.architecture.di.bindViewModel
import com.livefast.eattrash.raccoonforlemmy.feature.settings.advanced.AdvancedSettingsViewModel
import com.livefast.eattrash.raccoonforlemmy.feature.settings.colors.SettingsColorAndFontViewModel
import com.livefast.eattrash.raccoonforlemmy.feature.settings.main.SettingsViewModel
import org.kodein.di.DI
import org.kodein.di.instance

val settingsTabModule =
    DI.Module("SettingsTabModule") {
        bindViewModel {
            AdvancedSettingsViewModel(
                themeRepository = instance(),
                identityRepository = instance(),
                settingsRepository = instance(),
                accountRepository = instance(),
                siteRepository = instance(),
                notificationCenter = instance(),
                galleryHelper = instance(),
                appIconManager = instance(),
                fileSystemManager = instance(),
                importSettings = instance(),
                exportSettings = instance(),
                appConfigStore = instance(),
                appInfoRepository = instance(),
                barColorProvider = instance(),
            )
        }
        bindViewModel {
            SettingsColorAndFontViewModel(
                themeRepository = instance(),
                colorSchemeProvider = instance(),
                identityRepository = instance(),
                settingsRepository = instance(),
                accountRepository = instance(),
                notificationCenter = instance(),
            )
        }
        bindViewModel {
            SettingsViewModel(
                themeRepository = instance(),
                identityRepository = instance(),
                settingsRepository = instance(),
                accountRepository = instance(),
                notificationCenter = instance(),
                crashReportConfiguration = instance(),
                l10nManager = instance(),
                getSortTypesUseCase = instance(),
                customTabsHelper = instance(),
                siteSupportsHiddenPosts = instance(),
                siteSupportsMediaListUseCase = instance(),
            )
        }
    }
