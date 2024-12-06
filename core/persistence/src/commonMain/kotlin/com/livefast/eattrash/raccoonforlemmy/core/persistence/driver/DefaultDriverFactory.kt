package com.livefast.eattrash.raccoonforlemmy.core.persistence.driver

import app.cash.sqldelight.db.SqlDriver
import org.koin.core.annotation.Single

@Single
internal expect class DefaultDriverFactory : DriverFactory {
    override fun createDriver(): SqlDriver
}
