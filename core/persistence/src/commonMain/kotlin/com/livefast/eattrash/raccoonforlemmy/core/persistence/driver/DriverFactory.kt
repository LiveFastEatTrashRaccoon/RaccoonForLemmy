package com.livefast.eattrash.raccoonforlemmy.core.persistence.driver

import app.cash.sqldelight.db.SqlDriver

interface DriverFactory {
    fun createDriver(): SqlDriver
}
