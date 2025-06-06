package com.livefast.eattrash.raccoonforlemmy.core.preferences.appconfig

import com.livefast.eattrash.raccoonforlemmy.core.preferences.store.TemporaryKeyStore
import com.livefast.eattrash.raccoonforlemmy.core.testutils.DispatcherTestRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertTrue

class LocalAppConfigDataSourceTest {
    @get:Rule
    val dispatcherTestRule = DispatcherTestRule()

    private val keyStore = mockk<TemporaryKeyStore>(relaxUnitFun = true)
    private val sut =
        LocalAppConfigDataSource(
            keyStore = keyStore,
        )

    @Test
    fun whenGet_thenResultAndInteractionsAreAsExpected() =
        runTest {
            coEvery {
                keyStore.get(any<String>(), any<Boolean>())
            } returns true

            val res = sut.get()

            assertTrue(res.alternateMarkdownRenderingSettingsItemEnabled)

            coVerify {
                keyStore.get("AppConfig.alternateMarkdownRenderingSettingsItemEnabled", false)
            }
        }

    @Test
    fun whenUpdate_thenInteractionsAreAsExpected() =
        runTest {
            sut.update(AppConfig(alternateMarkdownRenderingSettingsItemEnabled = true))

            coVerify {
                keyStore.save("AppConfig.alternateMarkdownRenderingSettingsItemEnabled", true)
            }
        }
}
