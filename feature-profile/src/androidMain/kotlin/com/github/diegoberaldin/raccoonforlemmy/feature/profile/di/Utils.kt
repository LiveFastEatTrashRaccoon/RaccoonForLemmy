package com.github.diegoberaldin.raccoonforlemmy.feature.profile.di

import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.UserModel
import com.github.diegoberaldin.raccoonforlemmy.feature.profile.content.ProfileContentViewModel
import com.github.diegoberaldin.raccoonforlemmy.feature.profile.content.logged.ProfileLoggedViewModel
import com.github.diegoberaldin.raccoonforlemmy.feature.profile.content.logged.comments.ProfileCommentsViewModel
import com.github.diegoberaldin.raccoonforlemmy.feature.profile.content.logged.posts.ProfilePostsViewModel
import com.github.diegoberaldin.raccoonforlemmy.feature.profile.login.LoginBottomSheetViewModel
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.inject

actual fun getProfileScreenModel(): ProfileContentViewModel {
    val res: ProfileContentViewModel by inject(ProfileContentViewModel::class.java)
    return res
}

actual fun getLoginBottomSheetViewModel(): LoginBottomSheetViewModel {
    val res: LoginBottomSheetViewModel by inject(LoginBottomSheetViewModel::class.java)
    return res
}

actual fun getProfileLoggedViewModel(): ProfileLoggedViewModel {
    val res: ProfileLoggedViewModel by inject(ProfileLoggedViewModel::class.java)
    return res
}

actual fun getProfilePostsViewModel(
    user: UserModel,
    savedOnly: Boolean,
): ProfilePostsViewModel {
    val res: ProfilePostsViewModel by inject(
        clazz = ProfilePostsViewModel::class.java,
        parameters = { parametersOf(user, savedOnly) },
    )
    return res
}

actual fun getProfileCommentsViewModel(user: UserModel): ProfileCommentsViewModel {
    val res: ProfileCommentsViewModel by inject(
        clazz = ProfileCommentsViewModel::class.java,
        parameters = { parametersOf(user) },
    )
    return res
}
