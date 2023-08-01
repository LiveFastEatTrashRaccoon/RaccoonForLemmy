package com.github.diegoberaldin.raccoonforlemmy.feature_profile.di

import com.github.diegoberaldin.raccoonforlemmy.domain_lemmy.data.UserModel
import com.github.diegoberaldin.raccoonforlemmy.feature_profile.content.logged.ProfileLoggedViewModel
import com.github.diegoberaldin.raccoonforlemmy.feature_profile.content.logged.comments.ProfileCommentsViewModel
import com.github.diegoberaldin.raccoonforlemmy.feature_profile.content.logged.posts.ProfilePostsViewModel
import com.github.diegoberaldin.raccoonforlemmy.feature_profile.login.LoginBottomSheetViewModel
import com.github.diegoberaldin.raccoonforlemmy.feature_profile.viewmodel.ProfileScreenModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf

actual fun getProfileScreenModel() = ProfileScreenModelHelper.profileModel

actual fun getLoginBottomSheetViewModel() = ProfileScreenModelHelper.loginModel

actual fun getProfileLoggedViewModel(): ProfileLoggedViewModel =
    ProfileScreenModelHelper.loggedModel

actual fun getProfilePostsViewModel(
    user: UserModel,
    savedOnly: Boolean,
): ProfilePostsViewModel =
    ProfileScreenModelHelper.getPostsModel(user = user, savedOnly = savedOnly)

actual fun getProfileCommentsViewModel(user: UserModel): ProfileCommentsViewModel =
    ProfileScreenModelHelper.getCommentsModel(user)

object ProfileScreenModelHelper : KoinComponent {
    val profileModel: ProfileScreenModel by inject()
    val loginModel: LoginBottomSheetViewModel by inject()
    val loggedModel: ProfileLoggedViewModel by inject()

    fun getPostsModel(user: UserModel, savedOnly: Boolean): ProfilePostsViewModel {
        val res: ProfilePostsViewModel by inject(
            parameters = { parametersOf(user, savedOnly) },
        )
        return res
    }

    fun getCommentsModel(user: UserModel): ProfileCommentsViewModel {
        val res: ProfileCommentsViewModel by inject(
            parameters = { parametersOf(user) },
        )
        return res
    }
}
