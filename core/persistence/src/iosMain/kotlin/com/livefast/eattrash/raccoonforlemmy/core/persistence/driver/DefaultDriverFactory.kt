package com.livefast.eattrash.raccoonforlemmy.core.persistence.driver

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.livefast.eattrash.raccoonforlemmy.core.persistence.entities.AppDatabase

internal class DefaultDriverFactory : DriverFactory {
    override fun createDriver(): SqlDriver = NativeSqliteDriver(AppDatabase.Schema, "racconforlemmy.db")
}
