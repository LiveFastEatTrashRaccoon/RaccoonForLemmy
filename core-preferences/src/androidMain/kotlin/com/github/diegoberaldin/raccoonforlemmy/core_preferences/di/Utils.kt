package com.github.diegoberaldin.raccoonforlemmy.core_preferences.di

import com.github.diegoberaldin.raccoonforlemmy.core_preferences.TemporaryKeyStore
import org.koin.java.KoinJavaComponent

actual fun getTemporaryKeyStore(): TemporaryKeyStore {
    val res by KoinJavaComponent.inject<TemporaryKeyStore>(TemporaryKeyStore::class.java)
    return res
}