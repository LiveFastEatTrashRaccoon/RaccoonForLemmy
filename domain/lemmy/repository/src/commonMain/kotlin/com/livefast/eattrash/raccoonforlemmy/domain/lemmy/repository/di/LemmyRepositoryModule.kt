package com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.di

import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.CommentModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.CommunityModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.PostModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.UserModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.CommentRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.CommunityRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.DefaultCommentRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.DefaultCommunityRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.DefaultLemmyItemCache
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.DefaultLemmyValueCache
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.DefaultLocalItemCache
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.DefaultMediaRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.DefaultModlogRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.DefaultPostRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.DefaultPrivateMessageRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.DefaultSiteRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.DefaultUserRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.DefaultUserTagHelper
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.LemmyItemCache
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.LemmyValueCache
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.LocalItemCache
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.MediaRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.ModlogRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.PostRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.PrivateMessageRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.SiteRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.UserRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.UserTagHelper
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

private val cacheModule =
    DI.Module("CacheModule") {
        bind<LocalItemCache<PostModel>>(tag = "postCache") {
            singleton { DefaultLocalItemCache() }
        }
        bind<LocalItemCache<CommentModel>>(tag = "commentCache") {
            singleton { DefaultLocalItemCache() }
        }
        bind<LocalItemCache<CommunityModel>>(tag = "communityCache") {
            singleton { DefaultLocalItemCache() }
        }
        bind<LocalItemCache<UserModel>>(tag = "userCache") {
            singleton { DefaultLocalItemCache() }
        }
    }

val lemmyRepositoryModule =
    DI.Module("LemmyRepositoryModule") {
        import(cacheModule)

        bind<CommentRepository> {
            singleton {
                DefaultCommentRepository(
                    services = instance(tag = "default"),
                    customServices = instance(tag = "custom"),
                )
            }
        }
        bind<CommunityRepository> {
            singleton {
                DefaultCommunityRepository(
                    services = instance(tag = "default"),
                    customServices = instance(tag = "custom"),
                )
            }
        }
        bind<LemmyItemCache> {
            singleton {
                DefaultLemmyItemCache(
                    postCache = instance(tag = "postCache"),
                    communityCache = instance(tag = "communityCache"),
                    commentCache = instance(tag = "commentCache"),
                    userCache = instance(tag = "userCache"),
                )
            }
        }
        bind<LemmyValueCache> {
            singleton {
                DefaultLemmyValueCache(
                    services = instance(tag = "default"),
                )
            }
        }
        bind<MediaRepository> {
            singleton {
                DefaultMediaRepository(
                    services = instance(tag = "default"),
                )
            }
        }
        bind<ModlogRepository> {
            singleton {
                DefaultModlogRepository(
                    services = instance(tag = "default"),
                )
            }
        }
        bind<PostRepository> {
            singleton {
                DefaultPostRepository(
                    services = instance(tag = "default"),
                    customServices = instance(tag = "custom"),
                )
            }
        }
        bind<PrivateMessageRepository> {
            singleton {
                DefaultPrivateMessageRepository(
                    services = instance(tag = "default"),
                )
            }
        }
        bind<SiteRepository> {
            singleton {
                DefaultSiteRepository(
                    services = instance(tag = "default"),
                    customServices = instance(tag = "custom"),
                )
            }
        }
        bind<UserRepository> {
            singleton {
                DefaultUserRepository(
                    services = instance(tag = "default"),
                    customServices = instance(tag = "custom"),
                )
            }
        }
        bind<UserTagHelper> {
            singleton {
                DefaultUserTagHelper(
                    accountRepository = instance(),
                    userTagRepository = instance(),
                )
            }
        }
    }
