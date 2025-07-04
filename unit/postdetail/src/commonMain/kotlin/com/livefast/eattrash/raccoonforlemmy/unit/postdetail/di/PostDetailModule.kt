package com.livefast.eattrash.raccoonforlemmy.unit.postdetail.di

import com.livefast.eattrash.raccoonforlemmy.core.architecture.di.ViewModelCreationArgs
import com.livefast.eattrash.raccoonforlemmy.core.architecture.di.bindViewModelWithArgs
import com.livefast.eattrash.raccoonforlemmy.unit.postdetail.PostDetailViewModel
import org.kodein.di.DI
import org.kodein.di.instance

internal class PostDetailMviModelParams(
    val postId: Long,
    val otherInstance: String,
    val highlightCommentId: Long,
    val isModerator: Boolean,
) : ViewModelCreationArgs

val postDetailModule =
    DI.Module("PostDetailModule") {
        bindViewModelWithArgs { params: PostDetailMviModelParams ->
            PostDetailViewModel(
                postId = params.postId,
                otherInstance = params.otherInstance,
                highlightCommentId = params.highlightCommentId,
                isModerator = params.isModerator,
                identityRepository = instance(),
                apiConfigurationRepository = instance(),
                postRepository = instance(),
                commentPaginationManager = instance(),
                commentRepository = instance(),
                communityRepository = instance(),
                siteRepository = instance(),
                themeRepository = instance(),
                settingsRepository = instance(),
                accountRepository = instance(),
                userTagRepository = instance(),
                userTagHelper = instance(),
                postLastSeenDateRepository = instance(),
                shareHelper = instance(),
                notificationCenter = instance(),
                hapticFeedback = instance(),
                getSortTypesUseCase = instance(),
                itemCache = instance(),
                postNavigationManager = instance(),
                lemmyValueCache = instance(),
            )
        }
    }
