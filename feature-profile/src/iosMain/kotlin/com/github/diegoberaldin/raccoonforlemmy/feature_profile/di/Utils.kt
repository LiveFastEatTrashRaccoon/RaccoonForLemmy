package com.github.diegoberaldin.raccoonforlemmy.feature_profile.di

import com.github.diegoberaldin.raccoonforlemmy.feature_profile.login.LoginBottomSheetViewModel
import com.github.diegoberaldin.raccoonforlemmy.feature_profile.viewmodel.ProfileScreenModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

actual fun getProfileScreenModel() = ProfileScreenModelHelper.profileModel

actual fun getLoginBottomSheetViewModel() = ProfileScreenModelHelper.loginModel

object ProfileScreenModelHelper : KoinComponent {
    val profileModel: ProfileScreenModel by inject()
    val loginModel: LoginBottomSheetViewModel by inject()
}