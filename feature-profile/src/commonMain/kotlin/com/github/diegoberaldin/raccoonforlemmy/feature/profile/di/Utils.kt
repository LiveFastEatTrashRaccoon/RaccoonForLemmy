package com.github.diegoberaldin.raccoonforlemmy.feature.profile.di

import com.github.diegoberaldin.raccoonforlemmy.feature.profile.logged.ProfileLoggedViewModel
import com.github.diegoberaldin.raccoonforlemmy.feature.profile.login.LoginBottomSheetViewModel
import com.github.diegoberaldin.raccoonforlemmy.feature.profile.main.ProfileMainViewModel
import com.github.diegoberaldin.raccoonforlemmy.feature.profile.manageaccounts.ManageAccountsViewModel

expect fun getProfileScreenModel(): ProfileMainViewModel

expect fun getLoginBottomSheetViewModel(): LoginBottomSheetViewModel

expect fun getProfileLoggedViewModel(): ProfileLoggedViewModel

expect fun getManageAccountsViewModel(): ManageAccountsViewModel
