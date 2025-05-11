package com.livefast.eattrash.raccoonforlemmy.domain.lemmy.usecase

import com.livefast.eattrash.raccoonforlemmy.core.testutils.DispatcherTestRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertTrue

class DefaultGetSiteSupportsMediaListUseCaseTest {
    @get:Rule
    val dispatcherTestRule = DispatcherTestRule()

    private val isSiteVersionAtLeast: IsSiteVersionAtLeastUseCase =
        mockk {
            coEvery {
                this@mockk.invoke(
                    major = any(),
                    minor = any(),
                    patch = any(),
                    otherInstance = any()
                )
            } returns true
        }
    private val sut =
        DefaultGetSiteSupportsMediaListUseCase(
            isSiteVersionAtLeast = isSiteVersionAtLeast,
        )

    @Test
    fun whenInvoked_thenResultAndInteractionsAreAsExpected() =
        runTest {
            val res = sut()

            assertTrue(res)
            coVerify {
                isSiteVersionAtLeast(major = 0, minor = 19, patch = 4)
            }
        }
}
