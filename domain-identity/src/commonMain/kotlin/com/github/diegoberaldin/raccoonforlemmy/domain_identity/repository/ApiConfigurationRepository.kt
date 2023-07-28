package com.github.diegoberaldin.raccoonforlemmy.domain_identity.repository

interface ApiConfigurationRepository {
    fun getInstance(): String

    fun changeInstance(value: String)
}

