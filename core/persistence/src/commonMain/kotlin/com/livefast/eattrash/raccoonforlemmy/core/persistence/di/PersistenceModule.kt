package com.livefast.eattrash.raccoonforlemmy.core.persistence.di

import com.livefast.eattrash.raccoonforlemmy.core.persistence.provider.DatabaseProvider
import com.livefast.eattrash.raccoonforlemmy.core.persistence.provider.DefaultDatabaseProvider
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
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

val persistenceModule =
    DI.Module("PersistenceModule") {
        import(nativePersistenceModule)
        bind<DatabaseProvider> {
            singleton {
                DefaultDatabaseProvider(
                    driverFactory = instance(),
                )
            }
        }
        bind<AccountRepository> {
            singleton {
                DefaultAccountRepository(
                    provider = instance(),
                )
            }
        }
        bind<CommunityPreferredLanguageRepository> {
            singleton {
                DefaultCommunityPreferredLanguageRepository(
                    keyStore = instance(),
                )
            }
        }
        bind<CommunitySortRepository> {
            singleton {
                DefaultCommunitySortRepository(
                    keyStore = instance(),
                )
            }
        }
        bind<DomainBlocklistRepository> {
            singleton {
                DefaultDomainBlocklistRepository(
                    keyStore = instance(),
                )
            }
        }
        bind<DraftRepository> {
            singleton {
                DefaultDraftRepository(
                    provider = instance(),
                )
            }
        }
        bind<FavoriteCommunityRepository> {
            singleton {
                DefaultFavoriteCommunityRepository(
                    provider = instance(),
                )
            }
        }
        bind<InstanceSelectionRepository> {
            singleton {
                DefaultInstanceSelectionRepository(
                    keyStore = instance(),
                )
            }
        }
        bind<MultiCommunityRepository> {
            singleton {
                DefaultMultiCommunityRepository(
                    provider = instance(),
                )
            }
        }
        bind<SettingsRepository> {
            singleton {
                DefaultSettingsRepository(
                    provider = instance(),
                    keyStore = instance(),
                )
            }
        }
        bind<StopWordRepository> {
            singleton {
                DefaultStopWordRepository(
                    keyStore = instance(),
                )
            }
        }
    }
