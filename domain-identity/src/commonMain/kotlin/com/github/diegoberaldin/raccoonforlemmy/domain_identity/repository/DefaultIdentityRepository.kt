package com.github.diegoberaldin.raccoonforlemmy.domain_identity.repository

import com.github.diegoberaldin.raccoonforlemmy.core_preferences.KeyStoreKeys
import com.github.diegoberaldin.raccoonforlemmy.core_preferences.TemporaryKeyStore
import kotlinx.coroutines.flow.MutableStateFlow

internal class DefaultIdentityRepository(
    private val keyStore: TemporaryKeyStore,
) : IdentityRepository {

    override val authToken = MutableStateFlow<String?>(null)

    init {
        val previousToken = keyStore[KeyStoreKeys.AuthToken, ""]
            .takeIf { it.isNotEmpty() }
        authToken.value = previousToken
    }

    override fun storeToken(value: String) {
        authToken.value = value
        keyStore.save(KeyStoreKeys.AuthToken, value)
    }

    override fun clearToken() {
        authToken.value = null
        keyStore.save(KeyStoreKeys.AuthToken, "")
    }
}