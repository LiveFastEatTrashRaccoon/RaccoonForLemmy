package com.livefast.eattrash.raccoonforlemmy.core.persistence

import app.cash.sqldelight.db.SqlDriver

interface DriverFactory {
    fun createDriver(): SqlDriver
}
