package com.livefast.eattrash.raccoonforlemmy.domain.lemmy.pagination.di

import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.pagination.CommentPaginationManager
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.pagination.CommunityPaginationManager
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.pagination.DefaultCommentPaginationManager
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.pagination.DefaultCommunityPaginationManager
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.pagination.DefaultExplorePaginationManager
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.pagination.DefaultMultiCommunityPaginator
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.pagination.DefaultPostNavigationManager
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.pagination.DefaultPostPaginationManager
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.pagination.ExplorePaginationManager
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.pagination.MultiCommunityPaginator
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.pagination.PostNavigationManager
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.pagination.PostPaginationManager
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider
import org.kodein.di.singleton

val lemmyPaginationModule =
    DI.Module("LemmyPaginationModule") {
        bind<CommentPaginationManager> {
            provider {
                DefaultCommentPaginationManager(
                    identityRepository = instance(),
                    commentRepository = instance(),
                    userRepository = instance(),
                    userTagHelper = instance(),
                    notificationCenter = instance(),
                )
            }
        }
        bind<CommunityPaginationManager> {
            provider {
                DefaultCommunityPaginationManager(
                    identityRepository = instance(),
                    communityRepository = instance(),
                )
            }
        }
        bind<ExplorePaginationManager> {
            provider {
                DefaultExplorePaginationManager(
                    identityRepository = instance(),
                    accountRepository = instance(),
                    commentRepository = instance(),
                    communityRepository = instance(),
                    postRepository = instance(),
                    userRepository = instance(),
                    domainBlocklistRepository = instance(),
                    stopWordRepository = instance(),
                    apiConfigurationRepository = instance(),
                    userTagHelper = instance(),
                )
            }
        }
        bind<MultiCommunityPaginator> {
            provider {
                DefaultMultiCommunityPaginator(
                    postRepository = instance(),
                )
            }
        }
        bind<PostNavigationManager> {
            singleton {
                DefaultPostNavigationManager(
                    postPaginationManager = instance(),
                )
            }
        }
        bind<PostPaginationManager> {
            provider {
                DefaultPostPaginationManager(
                    identityRepository = instance(),
                    accountRepository = instance(),
                    postRepository = instance(),
                    communityRepository = instance(),
                    userRepository = instance(),
                    multiCommunityPaginator = instance(),
                    domainBlocklistRepository = instance(),
                    stopWordRepository = instance(),
                    userTagHelper = instance(),
                    notificationCenter = instance(),
                )
            }
        }
    }
