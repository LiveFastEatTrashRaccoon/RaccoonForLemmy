package com.github.diegoberaldin.raccoonforlemmy.feature_profile.di

import com.github.diegoberaldin.raccoonforlemmy.core_architecture.DefaultMviModel
import com.github.diegoberaldin.raccoonforlemmy.feature_profile.login.LoginBottomSheetMviModel
import com.github.diegoberaldin.raccoonforlemmy.feature_profile.login.LoginBottomSheetViewModel
import com.github.diegoberaldin.raccoonforlemmy.feature_profile.viewmodel.ProfileScreenModel
import com.github.diegoberaldin.raccoonforlemmy.feature_profile.viewmodel.ProfileScreenMviModel
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent.inject

actual val profileTabModule = module {
    factory {
        ProfileScreenModel(
            mvi = DefaultMviModel(ProfileScreenMviModel.UiState()),
            identityRepository = get(),
        )
    }
    factory {
        LoginBottomSheetViewModel(
            mvi = DefaultMviModel(LoginBottomSheetMviModel.UiState()),
            loginUseCase = get(),
        )
    }
}

actual fun getProfileScreenModel(): ProfileScreenModel {
    val res: ProfileScreenModel by inject(ProfileScreenModel::class.java)
    return res
}

actual fun getLoginBottomSheetViewModel(): LoginBottomSheetViewModel {
    val res: LoginBottomSheetViewModel by inject(LoginBottomSheetViewModel::class.java)
    return res
}