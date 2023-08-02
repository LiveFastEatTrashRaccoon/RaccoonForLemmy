package com.github.diegoberaldin.raccoonforlemmy.domain.identity.repository

import kotlinx.coroutines.flow.StateFlow

interface IdentityRepository {

    val authToken: StateFlow<String?>

    fun storeToken(value: String)

    fun clearToken()
}
