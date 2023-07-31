package com.github.diegoberaldin.raccoonforlemmy.feature_profile.di

import com.github.diegoberaldin.raccoonforlemmy.feature_profile.login.LoginBottomSheetViewModel
import com.github.diegoberaldin.raccoonforlemmy.feature_profile.viewmodel.ProfileScreenModel
import org.koin.java.KoinJavaComponent

actual fun getProfileScreenModel(): ProfileScreenModel {
    val res: ProfileScreenModel by KoinJavaComponent.inject(ProfileScreenModel::class.java)
    return res
}

actual fun getLoginBottomSheetViewModel(): LoginBottomSheetViewModel {
    val res: LoginBottomSheetViewModel by KoinJavaComponent.inject(LoginBottomSheetViewModel::class.java)
    return res
}
