package com.livefast.eattrash.raccoonforlemmy.core.persistence.provider

import com.livefast.eattrash.raccoonforlemmy.core.persistence.driver.DriverFactory
import com.livefast.eattrash.raccoonforlemmy.core.persistence.entities.AppDatabase

class DefaultDatabaseProvider(
    private val driverFactory: DriverFactory,
) : DatabaseProvider {
    private val db: AppDatabase by lazy {
        val driver = driverFactory.createDriver()
        AppDatabase(driver)
    }

    override fun getDatabase(): AppDatabase = db
}
