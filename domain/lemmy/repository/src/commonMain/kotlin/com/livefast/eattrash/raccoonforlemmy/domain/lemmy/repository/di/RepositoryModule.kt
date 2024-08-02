package com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.di

import com.livefast.eattrash.raccoonforlemmy.core.utils.cache.LruCache
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.CommentRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.CommunityRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.DefaultCommentRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.DefaultCommunityRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.DefaultGetSiteSupportsHiddenPostsUseCase
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.DefaultGetSiteSupportsMediaListUseCase
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.DefaultGetSortTypesUseCase
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.DefaultIsSiteVersionAtLeastUseCase
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.DefaultLemmyItemCache
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.DefaultLemmyValueCache
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.DefaultMediaRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.DefaultModlogRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.DefaultPostRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.DefaultPrivateMessageRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.DefaultSiteRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.DefaultUserRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.GetSiteSupportsHiddenPostsUseCase
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.GetSiteSupportsMediaListUseCase
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.GetSortTypesUseCase
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.IsSiteVersionAtLeastUseCase
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.LemmyItemCache
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.LemmyValueCache
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.MediaRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.ModlogRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.PostRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.PrivateMessageRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.SiteRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.UserRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module

private const val CACHE_SIZE = 5

val repositoryModule =
    module {
        single<IsSiteVersionAtLeastUseCase> {
            DefaultIsSiteVersionAtLeastUseCase(
                siteRepository = get(),
            )
        }
        single<GetSortTypesUseCase> {
            DefaultGetSortTypesUseCase(
                isSiteVersionAtLeastUseCase = get(),
            )
        }
        single<PostRepository> {
            DefaultPostRepository(
                services = get(named("default")),
                customServices = get(named("custom")),
            )
        }
        single<CommunityRepository> {
            DefaultCommunityRepository(
                services = get(named("default")),
                customServices = get(named("custom")),
            )
        }
        single<UserRepository> {
            DefaultUserRepository(
                services = get(named("default")),
                customServices = get(named("custom")),
            )
        }
        single<SiteRepository> {
            DefaultSiteRepository(
                services = get(named("default")),
                customServices = get(named("custom")),
            )
        }
        single<CommentRepository> {
            DefaultCommentRepository(
                services = get(named("default")),
                customServices = get(named("custom")),
            )
        }
        single<PrivateMessageRepository> {
            DefaultPrivateMessageRepository(
                services = get(named("default")),
            )
        }
        single<ModlogRepository> {
            DefaultModlogRepository(
                services = get(named("default")),
            )
        }
        single<LemmyItemCache> {
            DefaultLemmyItemCache(
                postCache = LruCache.factory(CACHE_SIZE),
                commentCache = LruCache.factory(CACHE_SIZE),
                communityCache = LruCache.factory(CACHE_SIZE),
                userCache = LruCache.factory(CACHE_SIZE),
            )
        }
        single<GetSiteSupportsHiddenPostsUseCase> {
            DefaultGetSiteSupportsHiddenPostsUseCase(
                isSiteVersionAtLeastUseCase = get(),
            )
        }
        single<MediaRepository> {
            DefaultMediaRepository(
                services = get(named("default")),
            )
        }
        single<GetSiteSupportsMediaListUseCase> {
            DefaultGetSiteSupportsMediaListUseCase(
                isSiteVersionAtLeastUseCase = get(),
            )
        }
        single<LemmyValueCache> {
            DefaultLemmyValueCache(
                services = get(named("default")),
            )
        }
    }
