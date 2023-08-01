package com.github.diegoberaldin.raccoonforlemmy.feature_profile.di

import com.github.diegoberaldin.raccoonforlemmy.feature_profile.content.logged.ProfileLoggedViewModel
import com.github.diegoberaldin.raccoonforlemmy.feature_profile.login.LoginBottomSheetViewModel
import com.github.diegoberaldin.raccoonforlemmy.feature_profile.viewmodel.ProfileScreenModel

expect fun getProfileScreenModel(): ProfileScreenModel

expect fun getLoginBottomSheetViewModel(): LoginBottomSheetViewModel

expect fun getProfileLoggedViewModel(): ProfileLoggedViewModel
