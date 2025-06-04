package com.livefast.eattrash.raccoonforlemmy.core.persistence.di

import com.livefast.eattrash.raccoonforlemmy.core.persistence.provider.DatabaseProvider
import com.livefast.eattrash.raccoonforlemmy.core.persistence.provider.DefaultDatabaseProvider
import com.livefast.eattrash.raccoonforlemmy.core.persistence.dao.AccountDao
import com.livefast.eattrash.raccoonforlemmy.core.persistence.dao.DefaultAccountDao
import com.livefast.eattrash.raccoonforlemmy.core.persistence.dao.DefaultDraftDao
import com.livefast.eattrash.raccoonforlemmy.core.persistence.dao.DefaultFavoriteCommunityDao
import com.livefast.eattrash.raccoonforlemmy.core.persistence.dao.DefaultMultiCommunityDao
import com.livefast.eattrash.raccoonforlemmy.core.persistence.dao.DefaultSettingsDao
import com.livefast.eattrash.raccoonforlemmy.core.persistence.dao.DefaultUserTagDao
import com.livefast.eattrash.raccoonforlemmy.core.persistence.dao.DefaultUserTagMemberDao
import com.livefast.eattrash.raccoonforlemmy.core.persistence.dao.DraftDao
import com.livefast.eattrash.raccoonforlemmy.core.persistence.dao.FavoriteCommunityDao
import com.livefast.eattrash.raccoonforlemmy.core.persistence.dao.MultiCommunityDao
import com.livefast.eattrash.raccoonforlemmy.core.persistence.dao.SettingsDao
import com.livefast.eattrash.raccoonforlemmy.core.persistence.dao.UserTagDao
import com.livefast.eattrash.raccoonforlemmy.core.persistence.dao.UserTagMemberDao
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
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.DefaultLongToLongMapSerializer
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.DefaultMultiCommunityRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.DefaultPostLastSeenDateRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.DefaultSettingsRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.DefaultSortSerializer
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.DefaultStopWordRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.DefaultUserSortRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.DefaultUserTagRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.DomainBlocklistRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.DraftRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.FavoriteCommunityRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.InstanceSelectionRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.LongToLongMapSerializer
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.MultiCommunityRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.PostLastSeenDateRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.SettingsRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.SortSerializer
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.StopWordRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.UserSortRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.UserTagRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.usecase.CreateSpecialTagsUseCase
import com.livefast.eattrash.raccoonforlemmy.core.persistence.usecase.DefaultCreateSpecialTagsUseCase
import com.livefast.eattrash.raccoonforlemmy.core.persistence.usecase.DefaultExportSettingsUseCase
import com.livefast.eattrash.raccoonforlemmy.core.persistence.usecase.DefaultImportSettingsUseCase
import com.livefast.eattrash.raccoonforlemmy.core.persistence.usecase.ExportSettingsUseCase
import com.livefast.eattrash.raccoonforlemmy.core.persistence.usecase.ImportSettingsUseCase
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider
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
        bind<AccountDao> {
            provider {
                val dbProvider: DatabaseProvider = instance()
                val queries = dbProvider.getDatabase().accountsQueries
                DefaultAccountDao(queries)
            }
        }
        bind<AccountRepository> {
            singleton {
                DefaultAccountRepository(
                    dao = instance()
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
        bind<SortSerializer> {
            singleton {
                DefaultSortSerializer()
            }
        }
        bind<CommunitySortRepository> {
            singleton {
                DefaultCommunitySortRepository(
                    keyStore = instance(),
                    serializer = instance(),
                )
            }
        }
        bind<UserSortRepository> {
            singleton {
                DefaultUserSortRepository(
                    keyStore = instance(),
                    serializer = instance(),
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
        bind<DraftDao> {
            provider {
                val dbProvider: DatabaseProvider = instance()
                val queries = dbProvider.getDatabase().draftsQueries
                DefaultDraftDao(queries)
            }
        }
        bind<DraftRepository> {
            singleton {
                DefaultDraftRepository(
                    dao = instance(),
                )
            }
        }
        bind<FavoriteCommunityDao> {
            provider {
                val dbProvider: DatabaseProvider = instance()
                val queries = dbProvider.getDatabase().favoritecommunitiesQueries
                DefaultFavoriteCommunityDao(queries)
            }
        }
        bind<FavoriteCommunityRepository> {
            singleton {
                DefaultFavoriteCommunityRepository(
                    dao = instance(),
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
        bind<MultiCommunityDao> {
            provider {
                val dbProvider: DatabaseProvider = instance()
                val queries = dbProvider.getDatabase().multicommunitiesQueries
                DefaultMultiCommunityDao(queries)
            }
        }
        bind<MultiCommunityRepository> {
            singleton {
                DefaultMultiCommunityRepository(
                    dao = instance(),
                )
            }
        }
        bind<SettingsDao> {
            provider {
                val dbProvider: DatabaseProvider = instance()
                val queries = dbProvider.getDatabase().settingsQueries
                DefaultSettingsDao(queries)
            }
        }
        bind<SettingsRepository> {
            singleton {
                DefaultSettingsRepository(
                    dao = instance(),
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
        bind<UserTagDao> {
            provider {
                val dbProvider: DatabaseProvider = instance()
                val queries = dbProvider.getDatabase().usertagsQueries
                DefaultUserTagDao(queries)
            }
        }
        bind<UserTagMemberDao> {
            provider {
                val dbProvider: DatabaseProvider = instance()
                val queries = dbProvider.getDatabase().usertagmembersQueries
                DefaultUserTagMemberDao(queries)
            }
        }
        bind<UserTagRepository> {
            singleton {
                DefaultUserTagRepository(
                    dao = instance(),
                    membersDao = instance(),
                )
            }
        }
        bind<ImportSettingsUseCase> {
            singleton {
                DefaultImportSettingsUseCase(
                    settingsRepository = instance(),
                    accountRepository = instance(),
                )
            }
        }
        bind<ExportSettingsUseCase> {
            singleton {
                DefaultExportSettingsUseCase(
                    settingsRepository = instance(),
                )
            }
        }
        bind<CreateSpecialTagsUseCase> {
            singleton {
                DefaultCreateSpecialTagsUseCase(
                    accountRepository = instance(),
                    userTagRepository = instance(),
                )
            }
        }
        bind<LongToLongMapSerializer> {
            singleton {
                DefaultLongToLongMapSerializer()
            }
        }
        bind<PostLastSeenDateRepository> {
            singleton {
                DefaultPostLastSeenDateRepository(
                    keyStore = instance(),
                    serializer = instance(),
                )
            }
        }
    }
