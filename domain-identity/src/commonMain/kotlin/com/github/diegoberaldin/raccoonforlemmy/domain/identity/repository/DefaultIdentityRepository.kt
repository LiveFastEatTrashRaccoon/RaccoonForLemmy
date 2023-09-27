package com.github.diegoberaldin.raccoonforlemmy.domain.identity.repository

import com.github.diegoberaldin.raccoonforlemmy.core.preferences.KeyStoreKeys
import com.github.diegoberaldin.raccoonforlemmy.core.preferences.TemporaryKeyStore
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map

internal class DefaultIdentityRepository(
    private val keyStore: TemporaryKeyStore,
) : IdentityRepository {

    override val authToken = MutableStateFlow<String?>(null)

    @OptIn(FlowPreview::class)
    override val isLogged: Flow<Boolean?> = authToken.debounce(100).map {
        it?.isNotEmpty()
    }

    init {
        val previousToken = keyStore[KeyStoreKeys.AuthToken, ""].takeIf { it.isNotEmpty() }
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
