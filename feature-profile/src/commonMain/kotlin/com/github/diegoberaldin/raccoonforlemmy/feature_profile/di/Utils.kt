package com.github.diegoberaldin.raccoonforlemmy.feature_profile.di

import com.github.diegoberaldin.raccoonforlemmy.domain_lemmy.data.UserModel
import com.github.diegoberaldin.raccoonforlemmy.feature_profile.content.logged.ProfileLoggedViewModel
import com.github.diegoberaldin.raccoonforlemmy.feature_profile.content.logged.comments.ProfileCommentsViewModel
import com.github.diegoberaldin.raccoonforlemmy.feature_profile.content.logged.posts.ProfilePostsViewModel
import com.github.diegoberaldin.raccoonforlemmy.feature_profile.login.LoginBottomSheetViewModel
import com.github.diegoberaldin.raccoonforlemmy.feature_profile.viewmodel.ProfileScreenModel

expect fun getProfileScreenModel(): ProfileScreenModel

expect fun getLoginBottomSheetViewModel(): LoginBottomSheetViewModel

expect fun getProfileLoggedViewModel(): ProfileLoggedViewModel

expect fun getProfilePostsViewModel(
    user: UserModel,
    savedOnly: Boolean = false,
): ProfilePostsViewModel

expect fun getProfileCommentsViewModel(user: UserModel): ProfileCommentsViewModel
