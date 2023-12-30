package com.github.diegoberaldin.raccoonforlemmy.unit.userinfo.di

import com.github.diegoberaldin.raccoonforlemmy.core.architecture.DefaultMviModel
import com.github.diegoberaldin.raccoonforlemmy.unit.userinfo.UserInfoMviModel
import com.github.diegoberaldin.raccoonforlemmy.unit.userinfo.UserInfoViewModel
import org.koin.dsl.module

val userInfoModule = module {
    factory<UserInfoMviModel> { params ->
        UserInfoViewModel(
            mvi = DefaultMviModel(UserInfoMviModel.UiState()),
            userId = params[0],
            userRepository = get(),
            settingsRepository = get(),
            itemCache = get(),
        )
    }
}