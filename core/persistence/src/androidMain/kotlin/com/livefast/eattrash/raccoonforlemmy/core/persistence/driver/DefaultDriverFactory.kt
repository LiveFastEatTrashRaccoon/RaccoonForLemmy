package com.livefast.eattrash.raccoonforlemmy.core.persistence.driver

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.livefast.eattrash.raccoonforlemmy.core.persistence.entities.AppDatabase
import com.livefast.eattrash.raccoonforlemmy.core.persistence.key.DatabaseKeyProvider
import net.zetetic.database.sqlcipher.SupportOpenHelperFactory
import org.koin.core.annotation.Single

@Single
internal actual class DefaultDriverFactory(
    private val context: Context,
    private val keyProvider: DatabaseKeyProvider,
) : DriverFactory {
    companion object {
        private const val DATABASE_NAME = "raccoonforlemmy.db"
    }

    actual override fun createDriver(): SqlDriver {
        System.loadLibrary("sqlcipher")
        val key = keyProvider.getKey()
        val supportFactory = SupportOpenHelperFactory(key)
        return AndroidSqliteDriver(
            factory = supportFactory,
            schema = AppDatabase.Schema,
            context = context,
            name = DATABASE_NAME,
        )
    }
}
