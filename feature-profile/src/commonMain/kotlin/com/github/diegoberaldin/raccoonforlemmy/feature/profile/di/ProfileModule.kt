package com.github.diegoberaldin.raccoonforlemmy.feature.profile.di

import com.github.diegoberaldin.raccoonforlemmy.core.architecture.DefaultMviModel
import com.github.diegoberaldin.raccoonforlemmy.feature.profile.content.logged.ProfileLoggedMviModel
import com.github.diegoberaldin.raccoonforlemmy.feature.profile.content.logged.ProfileLoggedViewModel
import com.github.diegoberaldin.raccoonforlemmy.feature.profile.content.logged.comments.ProfileCommentsMviModel
import com.github.diegoberaldin.raccoonforlemmy.feature.profile.content.logged.comments.ProfileCommentsViewModel
import com.github.diegoberaldin.raccoonforlemmy.feature.profile.content.logged.posts.ProfilePostsMviModel
import com.github.diegoberaldin.raccoonforlemmy.feature.profile.content.logged.posts.ProfilePostsViewModel
import com.github.diegoberaldin.raccoonforlemmy.feature.profile.login.LoginBottomSheetMviModel
import com.github.diegoberaldin.raccoonforlemmy.feature.profile.login.LoginBottomSheetViewModel
import com.github.diegoberaldin.raccoonforlemmy.feature.profile.viewmodel.ProfileScreenModel
import com.github.diegoberaldin.raccoonforlemmy.feature.profile.viewmodel.ProfileScreenMviModel
import org.koin.dsl.module

val profileTabModule = module {
    factory {
        ProfileScreenModel(
            mvi = DefaultMviModel(ProfileScreenMviModel.UiState()),
            identityRepository = get(),
            siteRepository = get(),
        )
    }
    factory {
        LoginBottomSheetViewModel(
            mvi = DefaultMviModel(LoginBottomSheetMviModel.UiState()),
            loginUseCase = get(),
        )
    }
    factory {
        ProfileLoggedViewModel(
            mvi = DefaultMviModel(ProfileLoggedMviModel.UiState()),
        )
    }
    factory { params ->
        ProfilePostsViewModel(
            mvi = DefaultMviModel(ProfilePostsMviModel.UiState()),
            user = params[0],
            savedOnly = params[1],
            identityRepository = get(),
            userRepository = get(),
        )
    }
    factory { params ->
        ProfileCommentsViewModel(
            mvi = DefaultMviModel(ProfileCommentsMviModel.UiState()),
            user = params[0],
            identityRepository = get(),
            userRepository = get(),
        )
    }
}
