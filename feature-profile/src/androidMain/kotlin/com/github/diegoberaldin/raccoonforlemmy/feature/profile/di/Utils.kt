package com.github.diegoberaldin.raccoonforlemmy.feature.profile.di

import com.github.diegoberaldin.raccoonforlemmy.feature.profile.logged.ProfileLoggedMviModel
import com.github.diegoberaldin.raccoonforlemmy.feature.profile.login.LoginBottomSheetMviModel
import com.github.diegoberaldin.raccoonforlemmy.feature.profile.main.ProfileMainMviModel
import com.github.diegoberaldin.raccoonforlemmy.feature.profile.manageaccounts.ManageAccountsMviModel
import org.koin.java.KoinJavaComponent.inject

actual fun getProfileScreenModel(): ProfileMainMviModel {
    val res: ProfileMainMviModel by inject(ProfileMainMviModel::class.java)
    return res
}

actual fun getLoginBottomSheetViewModel(): LoginBottomSheetMviModel {
    val res: LoginBottomSheetMviModel by inject(LoginBottomSheetMviModel::class.java)
    return res
}

actual fun getProfileLoggedViewModel(): ProfileLoggedMviModel {
    val res: ProfileLoggedMviModel by inject(ProfileLoggedMviModel::class.java)
    return res
}

actual fun getManageAccountsViewModel(): ManageAccountsMviModel {
    val res: ManageAccountsMviModel by inject(ManageAccountsMviModel::class.java)
    return res
}
