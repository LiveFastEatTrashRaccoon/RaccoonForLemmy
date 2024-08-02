package com.livefast.eattrash.raccoonforlemmy.feature.settings.di

import com.livefast.eattrash.raccoonforlemmy.feature.settings.advanced.AdvancedSettingsMviModel
import com.livefast.eattrash.raccoonforlemmy.feature.settings.advanced.AdvancedSettingsViewModel
import com.livefast.eattrash.raccoonforlemmy.feature.settings.colors.SettingsColorAndFontMviModel
import com.livefast.eattrash.raccoonforlemmy.feature.settings.colors.SettingsColorAndFontViewModel
import com.livefast.eattrash.raccoonforlemmy.feature.settings.main.SettingsMviModel
import com.livefast.eattrash.raccoonforlemmy.feature.settings.main.SettingsViewModel
import com.livefast.eattrash.raccoonforlemmy.unit.about.di.aboutModule
import org.koin.dsl.module

val settingsTabModule =
    module {
        includes(aboutModule)
        factory<SettingsMviModel> {
            SettingsViewModel(
                settingsRepository = get(),
                accountRepository = get(),
                themeRepository = get(),
                l10nManager = get(),
                identityRepository = get(),
                notificationCenter = get(),
                crashReportConfiguration = get(),
                getSortTypesUseCase = get(),
                customTabsHelper = get(),
                siteSupportsHiddenPosts = get(),
                siteSupportsMediaListUseCase = get(),
            )
        }
        factory<SettingsColorAndFontMviModel> {
            SettingsColorAndFontViewModel(
                settingsRepository = get(),
                accountRepository = get(),
                themeRepository = get(),
                identityRepository = get(),
                colorSchemeProvider = get(),
                notificationCenter = get(),
            )
        }
        factory<AdvancedSettingsMviModel> {
            AdvancedSettingsViewModel(
                settingsRepository = get(),
                accountRepository = get(),
                themeRepository = get(),
                identityRepository = get(),
                notificationCenter = get(),
                galleryHelper = get(),
                siteRepository = get(),
                appIconManager = get(),
                fileSystemManager = get(),
                importSettings = get(),
                exportSettings = get(),
                appConfigStore = get(),
                appInfoRepository = get(),
            )
        }
    }
