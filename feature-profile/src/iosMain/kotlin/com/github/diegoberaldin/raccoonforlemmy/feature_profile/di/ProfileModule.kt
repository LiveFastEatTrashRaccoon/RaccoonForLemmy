package com.github.diegoberaldin.raccoonforlemmy.feature_profile.di

import com.github.diegoberaldin.raccoonforlemmy.core_architecture.DefaultMviModel
import com.github.diegoberaldin.raccoonforlemmy.feature_profile.login.LoginBottomSheetMviModel
import com.github.diegoberaldin.raccoonforlemmy.feature_profile.login.LoginBottomSheetViewModel
import com.github.diegoberaldin.raccoonforlemmy.feature_profile.viewmodel.ProfileScreenModel
import com.github.diegoberaldin.raccoonforlemmy.feature_profile.viewmodel.ProfileScreenMviModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.dsl.module

actual val profileTabModule = module {
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
}

actual fun getProfileScreenModel() = ProfileScreenModelHelper.profileModel

actual fun getLoginBottomSheetViewModel() = ProfileScreenModelHelper.loginModel

object ProfileScreenModelHelper : KoinComponent {
    val profileModel: ProfileScreenModel by inject()
    val loginModel: LoginBottomSheetViewModel by inject()
}