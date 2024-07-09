package com.github.diegoberaldin.raccoonforlemmy.core.preferences.appconfig

import com.github.diegoberaldin.raccoonforlemmy.core.testutils.DispatcherTestRule
import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class DefaultAppConfigStoreTest {
    @get:Rule
    val dispatcherTestRule = DispatcherTestRule()

    private val localDataSource = mockk<AppConfigDataSource>(relaxUnitFun = true)
    private val remoteDataSource = mockk<AppConfigDataSource>(relaxUnitFun = true)
    private val sut =
        DefaultAppConfigStore(
            localDataSource = localDataSource,
            remoteDataSource = remoteDataSource,
            dispatcher = dispatcherTestRule.dispatcher,
        )

    @Test
    fun whenInitialize_thenResultAndInteractionsAreAsExpected() =
        runTest {
            val initialConfig = AppConfig()
            val remoteConfig = AppConfig(alternateMarkdownRenderingSettingsItemEnabled = true)
            coEvery {
                localDataSource.get()
            } returns initialConfig
            coEvery {
                remoteDataSource.get()
            } returns remoteConfig

            sut.initialize()

            val res = sut.appConfig.value
            assertEquals(remoteConfig, res)

            coVerifySequence {
                localDataSource.get()
                remoteDataSource.get()
                localDataSource.update(remoteConfig)
            }
        }
}
