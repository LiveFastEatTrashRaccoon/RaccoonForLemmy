package com.github.diegoberaldin.raccoonforlemmy.profile

import org.koin.java.KoinJavaComponent.inject

actual fun getProfileScreenModel(): ProfileScreenModel {
    val res: ProfileScreenModel by inject(ProfileScreenModel::class.java)
    return res
}