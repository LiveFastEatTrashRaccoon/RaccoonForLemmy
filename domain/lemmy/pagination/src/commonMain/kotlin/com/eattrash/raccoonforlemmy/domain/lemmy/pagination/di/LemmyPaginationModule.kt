package com.eattrash.raccoonforlemmy.domain.lemmy.pagination.di

import com.eattrash.raccoonforlemmy.domain.lemmy.pagination.CommentPaginationManager
import com.eattrash.raccoonforlemmy.domain.lemmy.pagination.CommunityPaginationManager
import com.eattrash.raccoonforlemmy.domain.lemmy.pagination.DefaultCommentPaginationManager
import com.eattrash.raccoonforlemmy.domain.lemmy.pagination.DefaultCommunityPaginationManager
import com.eattrash.raccoonforlemmy.domain.lemmy.pagination.DefaultExplorePaginationManager
import com.eattrash.raccoonforlemmy.domain.lemmy.pagination.DefaultMultiCommunityPaginator
import com.eattrash.raccoonforlemmy.domain.lemmy.pagination.DefaultPostNavigationManager
import com.eattrash.raccoonforlemmy.domain.lemmy.pagination.DefaultPostPaginationManager
import com.eattrash.raccoonforlemmy.domain.lemmy.pagination.ExplorePaginationManager
import com.eattrash.raccoonforlemmy.domain.lemmy.pagination.MultiCommunityPaginator
import com.eattrash.raccoonforlemmy.domain.lemmy.pagination.PostNavigationManager
import com.eattrash.raccoonforlemmy.domain.lemmy.pagination.PostPaginationManager
import org.koin.dsl.module

val paginationModule =
    module {
        factory<MultiCommunityPaginator> {
            DefaultMultiCommunityPaginator(
                postRepository = get(),
            )
        }
        factory<PostPaginationManager> {
            DefaultPostPaginationManager(
                identityRepository = get(),
                accountRepository = get(),
                postRepository = get(),
                communityRepository = get(),
                userRepository = get(),
                multiCommunityPaginator = get(),
                notificationCenter = get(),
                domainBlocklistRepository = get(),
                stopWordRepository = get(),
            )
        }
        factory<CommentPaginationManager> {
            DefaultCommentPaginationManager(
                identityRepository = get(),
                userRepository = get(),
                commentRepository = get(),
                notificationCenter = get(),
            )
        }
        single<PostNavigationManager> {
            DefaultPostNavigationManager(
                postPaginationManager = get(),
            )
        }
        factory<CommunityPaginationManager> {
            DefaultCommunityPaginationManager(
                identityRepository = get(),
                communityRepository = get(),
            )
        }
        factory<ExplorePaginationManager> {
            DefaultExplorePaginationManager(
                identityRepository = get(),
                accountRepository = get(),
                communityRepository = get(),
                userRepository = get(),
                domainBlocklistRepository = get(),
                stopWordRepository = get(),
            )
        }
    }
