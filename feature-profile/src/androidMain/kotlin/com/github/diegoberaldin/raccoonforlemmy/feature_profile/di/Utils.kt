package com.github.diegoberaldin.raccoonforlemmy.feature_profile.di

import com.github.diegoberaldin.raccoonforlemmy.feature_profile.content.logged.ProfileLoggedViewModel
import com.github.diegoberaldin.raccoonforlemmy.feature_profile.login.LoginBottomSheetViewModel
import com.github.diegoberaldin.raccoonforlemmy.feature_profile.viewmodel.ProfileScreenModel
import org.koin.java.KoinJavaComponent.inject

actual fun getProfileScreenModel(): ProfileScreenModel {
    val res: ProfileScreenModel by inject(ProfileScreenModel::class.java)
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
