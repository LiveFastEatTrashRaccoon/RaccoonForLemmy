package com.livefast.eattrash.raccoonforlemmy.core.persistence

import com.livefast.eattrash.raccoonforlemmy.core.persistence.entities.AppDatabase

interface DatabaseProvider {
    fun getDatabase(): AppDatabase
}
