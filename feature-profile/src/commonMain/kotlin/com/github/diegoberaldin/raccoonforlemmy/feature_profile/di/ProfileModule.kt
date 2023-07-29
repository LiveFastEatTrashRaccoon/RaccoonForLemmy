package com.github.diegoberaldin.raccoonforlemmy.feature_profile.di

import com.github.diegoberaldin.raccoonforlemmy.feature_profile.login.LoginBottomSheetViewModel
import com.github.diegoberaldin.raccoonforlemmy.feature_profile.viewmodel.ProfileScreenModel
import org.koin.core.module.Module

expect val profileTabModule: Module

expect fun getProfileScreenModel(): ProfileScreenModel

expect fun getLoginBottomSheetViewModel(): LoginBottomSheetViewModel