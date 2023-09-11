package com.github.diegoberaldin.raccoonforlemmy.di

import com.github.diegoberaldin.raccoonforlemmy.MainViewModel
import org.koin.java.KoinJavaComponent.inject

actual fun getMainViewModel(): MainViewModel {
    val res: MainViewModel by inject(MainViewModel::class.java)
    return res
}