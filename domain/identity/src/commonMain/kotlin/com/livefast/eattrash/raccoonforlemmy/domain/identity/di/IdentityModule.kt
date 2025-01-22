package com.livefast.eattrash.raccoonforlemmy.domain.identity.di

import androidx.compose.ui.platform.UriHandler
import com.livefast.eattrash.raccoonforlemmy.domain.identity.repository.ApiConfigurationRepository
import com.livefast.eattrash.raccoonforlemmy.domain.identity.repository.AuthRepository
import com.livefast.eattrash.raccoonforlemmy.domain.identity.repository.DefaultApiConfigurationRepository
import com.livefast.eattrash.raccoonforlemmy.domain.identity.repository.DefaultAuthRepository
import com.livefast.eattrash.raccoonforlemmy.domain.identity.repository.DefaultIdentityRepository
import com.livefast.eattrash.raccoonforlemmy.domain.identity.repository.IdentityRepository
import com.livefast.eattrash.raccoonforlemmy.domain.identity.urlhandler.CommentProcessor
import com.livefast.eattrash.raccoonforlemmy.domain.identity.urlhandler.CommunityProcessor
import com.livefast.eattrash.raccoonforlemmy.domain.identity.urlhandler.CustomUriHandler
import com.livefast.eattrash.raccoonforlemmy.domain.identity.urlhandler.DefaultCommentProcessor
import com.livefast.eattrash.raccoonforlemmy.domain.identity.urlhandler.DefaultCommunityProcessor
import com.livefast.eattrash.raccoonforlemmy.domain.identity.urlhandler.DefaultCustomUriHandler
import com.livefast.eattrash.raccoonforlemmy.domain.identity.urlhandler.DefaultPostProcessor
import com.livefast.eattrash.raccoonforlemmy.domain.identity.urlhandler.DefaultUrlDecoder
import com.livefast.eattrash.raccoonforlemmy.domain.identity.urlhandler.DefaultUserProcessor
import com.livefast.eattrash.raccoonforlemmy.domain.identity.urlhandler.PostProcessor
import com.livefast.eattrash.raccoonforlemmy.domain.identity.urlhandler.UrlDecoder
import com.livefast.eattrash.raccoonforlemmy.domain.identity.urlhandler.UserProcessor
import com.livefast.eattrash.raccoonforlemmy.domain.identity.usecase.DefaultDeleteAccountUseCase
import com.livefast.eattrash.raccoonforlemmy.domain.identity.usecase.DefaultLoginUseCase
import com.livefast.eattrash.raccoonforlemmy.domain.identity.usecase.DefaultLogoutUseCase
import com.livefast.eattrash.raccoonforlemmy.domain.identity.usecase.DefaultSwitchAccountUseCase
import com.livefast.eattrash.raccoonforlemmy.domain.identity.usecase.DeleteAccountUseCase
import com.livefast.eattrash.raccoonforlemmy.domain.identity.usecase.LoginUseCase
import com.livefast.eattrash.raccoonforlemmy.domain.identity.usecase.LogoutUseCase
import com.livefast.eattrash.raccoonforlemmy.domain.identity.usecase.SwitchAccountUseCase
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.factory
import org.kodein.di.instance
import org.kodein.di.singleton

val identityModule =
    DI.Module("IdentityModule") {
        bind<ApiConfigurationRepository> {
            singleton {
                DefaultApiConfigurationRepository(
                    serviceProvider = instance(tag = "default"),
                    keyStore = instance(),
                )
            }
        }
        bind<AuthRepository> {
            singleton {
                DefaultAuthRepository(
                    services = instance(tag = "default"),
                )
            }
        }
        bind<IdentityRepository> {
            singleton {
                DefaultIdentityRepository(
                    accountRepository = instance(),
                    siteRepository = instance(),
                    networkManager = instance(),
                )
            }
        }
        bind<CommentProcessor> {
            singleton {
                DefaultCommentProcessor(
                    identityRepository = instance(),
                    postRepository = instance(),
                    commentRepository = instance(),
                    detailOpener = instance(),
                )
            }
        }
        bind<CommunityProcessor> {
            singleton {
                DefaultCommunityProcessor(
                    identityRepository = instance(),
                    communityRepository = instance(),
                    detailOpener = instance(),
                    urlDecoder = instance(),
                )
            }
        }
        bind<CustomUriHandler> {
            factory { fallback: UriHandler ->
                DefaultCustomUriHandler(
                    fallbackHandler = fallback,
                    settingsRepository = instance(),
                    communityProcessor = instance(),
                    userProcessor = instance(),
                    postProcessor = instance(),
                    commentProcessor = instance(),
                    detailOpener = instance(),
                    customTabsHelper = instance(),
                )
            }
        }
        bind<PostProcessor> {
            singleton {
                DefaultPostProcessor(
                    identityRepository = instance(),
                    postRepository = instance(),
                    detailOpener = instance(),
                    urlDecoder = instance(),
                )
            }
        }
        bind<UrlDecoder> {
            singleton {
                DefaultUrlDecoder()
            }
        }
        bind<UserProcessor> {
            singleton {
                DefaultUserProcessor(
                    identityRepository = instance(),
                    userRepository = instance(),
                    detailOpener = instance(),
                    urlDecoder = instance(),
                )
            }
        }
        bind<DeleteAccountUseCase> {
            singleton {
                DefaultDeleteAccountUseCase(
                    accountRepository = instance(),
                )
            }
        }
        bind<LoginUseCase> {
            singleton {
                DefaultLoginUseCase(
                    authRepository = instance(),
                    apiConfigurationRepository = instance(),
                    identityRepository = instance(),
                    accountRepository = instance(),
                    settingsRepository = instance(),
                    siteRepository = instance(),
                    communitySortRepository = instance(),
                    communityPreferredLanguageRepository = instance(),
                    bottomNavItemsRepository = instance(),
                    lemmyValueCache = instance(),
                    createSpecialTagsUseCase = instance(),
                    userSortRepository = instance(),
                    postLastSeenDateRepository = instance(),
                )
            }
        }
        bind<LogoutUseCase> {
            singleton {
                DefaultLogoutUseCase(
                    identityRepository = instance(),
                    accountRepository = instance(),
                    notificationCenter = instance(),
                    settingsRepository = instance(),
                    communitySortRepository = instance(),
                    bottomNavItemsRepository = instance(),
                    userTagHelper = instance(),
                    lemmyValueCache = instance(),
                    userSortRepository = instance(),
                    postLastSeenDateRepository = instance(),
                )
            }
        }
        bind<SwitchAccountUseCase> {
            singleton {
                DefaultSwitchAccountUseCase(
                    identityRepository = instance(),
                    accountRepository = instance(),
                    serviceProvider = instance("default"),
                    notificationCenter = instance(),
                    settingsRepository = instance(),
                    communitySortRepository = instance(),
                    communityPreferredLanguageRepository = instance(),
                    bottomNavItemsRepository = instance(),
                    userTagHelper = instance(),
                    lemmyValueCache = instance(),
                    userSortRepository = instance(),
                    postLastSeenDateRepository = instance(),
                )
            }
        }
    }
