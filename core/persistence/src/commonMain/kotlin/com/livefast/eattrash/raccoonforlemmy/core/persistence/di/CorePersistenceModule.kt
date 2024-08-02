package com.livefast.eattrash.raccoonforlemmy.core.persistence.di

import com.livefast.eattrash.raccoonforlemmy.core.persistence.DatabaseProvider
import com.livefast.eattrash.raccoonforlemmy.core.persistence.DefaultDatabaseProvider
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.AccountRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.CommunityPreferredLanguageRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.CommunitySortRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.DefaultAccountRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.DefaultCommunityPreferredLanguageRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.DefaultCommunitySortRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.DefaultDomainBlocklistRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.DefaultDraftRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.DefaultFavoriteCommunityRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.DefaultInstanceSelectionRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.DefaultMultiCommunityRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.DefaultSettingsRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.DefaultStopWordRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.DomainBlocklistRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.DraftRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.FavoriteCommunityRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.InstanceSelectionRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.MultiCommunityRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.SettingsRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.StopWordRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.usecase.DefaultExportSettingsUseCase
import com.livefast.eattrash.raccoonforlemmy.core.persistence.usecase.DefaultImportSettingsUseCase
import com.livefast.eattrash.raccoonforlemmy.core.persistence.usecase.ExportSettingsUseCase
import com.livefast.eattrash.raccoonforlemmy.core.persistence.usecase.ImportSettingsUseCase
import org.koin.dsl.module

val corePersistenceModule =
    module {
        includes(persistenceInnerModule)
        single<DatabaseProvider> {
            DefaultDatabaseProvider(
                driverFactory = get(),
            )
        }
        single<AccountRepository> {
            DefaultAccountRepository(
                provider = get(),
            )
        }
        single<SettingsRepository> {
            DefaultSettingsRepository(
                provider = get(),
                keyStore = get(),
            )
        }
        single<MultiCommunityRepository> {
            DefaultMultiCommunityRepository(
                provider = get(),
            )
        }
        single<FavoriteCommunityRepository> {
            DefaultFavoriteCommunityRepository(
                provider = get(),
            )
        }
        single<InstanceSelectionRepository> {
            DefaultInstanceSelectionRepository(
                keyStore = get(),
            )
        }
        single<DraftRepository> {
            DefaultDraftRepository(
                provider = get(),
            )
        }
        single<CommunitySortRepository> {
            DefaultCommunitySortRepository(
                keyStore = get(),
            )
        }
        single<CommunityPreferredLanguageRepository> {
            DefaultCommunityPreferredLanguageRepository(
                keyStore = get(),
            )
        }
        single<ImportSettingsUseCase> {
            DefaultImportSettingsUseCase(
                settingsRepository = get(),
                accountRepository = get(),
            )
        }
        single<ExportSettingsUseCase> {
            DefaultExportSettingsUseCase(
                settingsRepository = get(),
            )
        }
        single<DomainBlocklistRepository> {
            DefaultDomainBlocklistRepository(
                keyStore = get(),
            )
        }
        single<StopWordRepository> {
            DefaultStopWordRepository(
                keyStore = get(),
            )
        }
    }
