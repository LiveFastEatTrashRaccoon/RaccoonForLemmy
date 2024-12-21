package com.livefast.eattrash.raccoonforlemmy.feature.settings.di

import com.livefast.eattrash.raccoonforlemmy.feature.settings.advanced.AdvancedSettingsMviModel
import com.livefast.eattrash.raccoonforlemmy.feature.settings.advanced.AdvancedSettingsViewModel
import com.livefast.eattrash.raccoonforlemmy.feature.settings.colors.SettingsColorAndFontMviModel
import com.livefast.eattrash.raccoonforlemmy.feature.settings.colors.SettingsColorAndFontViewModel
import com.livefast.eattrash.raccoonforlemmy.feature.settings.main.SettingsMviModel
import com.livefast.eattrash.raccoonforlemmy.feature.settings.main.SettingsViewModel
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider

val settingsTabModule =
    DI.Module("SettingsTabModule") {
        bind<AdvancedSettingsMviModel> {
            provider {
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
                )
            }
        }
        bind<SettingsColorAndFontMviModel> {
            provider {
                SettingsColorAndFontViewModel(
                    themeRepository = instance(),
                    colorSchemeProvider = instance(),
                    identityRepository = instance(),
                    settingsRepository = instance(),
                    accountRepository = instance(),
                    notificationCenter = instance(),
                )
            }
        }
        bind<SettingsMviModel> {
            provider {
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
    }
