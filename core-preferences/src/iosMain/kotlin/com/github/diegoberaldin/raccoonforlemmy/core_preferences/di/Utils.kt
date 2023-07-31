package com.github.diegoberaldin.raccoonforlemmy.core_preferences.di

import com.github.diegoberaldin.raccoonforlemmy.core_preferences.TemporaryKeyStore

actual fun getTemporaryKeyStore(): TemporaryKeyStore {
    return TemporaryKeyStoreHelper.temporaryKeyStore
}