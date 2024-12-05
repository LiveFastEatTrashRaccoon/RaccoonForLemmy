package com.livefast.eattrash.raccoonforlemmy.core.persistence.provider

import com.livefast.eattrash.raccoonforlemmy.core.persistence.entities.AppDatabase

interface DatabaseProvider {
    fun getDatabase(): AppDatabase
}
