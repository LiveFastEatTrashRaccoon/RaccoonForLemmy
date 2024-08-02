package com.livefast.eattrash.raccoonforlemmy.core.preferences.appconfig

import com.livefast.eattrash.raccoonforlemmy.core.preferences.TemporaryKeyStore
import com.livefast.eattrash.raccoonforlemmy.core.testutils.DispatcherTestRule
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
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
            every {
                keyStore[any<String>(), any<Boolean>()]
            } returns true

            val res = sut.get()

            assertTrue(res.alternateMarkdownRenderingSettingsItemEnabled)

            verify {
                keyStore["AppConfig.alternateMarkdownRenderingSettingsItemEnabled", false]
            }
        }

    @Test
    fun whenUpdate_thenInteractionsAreAsExpected() =
        runTest {
            sut.update(AppConfig(alternateMarkdownRenderingSettingsItemEnabled = true))

            verify {
                keyStore.save("AppConfig.alternateMarkdownRenderingSettingsItemEnabled", true)
            }
        }
}
