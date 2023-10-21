package com.github.diegoberaldin.raccoonforlemmy.domain.identity.repository

import kotlinx.coroutines.flow.StateFlow

interface ApiConfigurationRepository {

    val instance: StateFlow<String>

    fun changeInstance(value: String)
}
