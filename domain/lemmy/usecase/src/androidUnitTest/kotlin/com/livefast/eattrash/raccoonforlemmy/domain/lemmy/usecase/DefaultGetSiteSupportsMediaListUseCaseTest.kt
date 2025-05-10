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

    private val isSiteVersionAtLeastUseCase =
        mockk<IsSiteVersionAtLeastUseCase> {
            coEvery { execute(any(), any(), any(), any()) } returns true
        }
    private val sut =
        DefaultGetSiteSupportsMediaListUseCase(
            isSiteVersionAtLeastUseCase = isSiteVersionAtLeastUseCase,
        )

    @Test
    fun whenInvoked_thenResultAndInteractionsAreAsExpected() =
        runTest {
            val res = sut()

            assertTrue(res)
            coVerify {
                isSiteVersionAtLeastUseCase.execute(major = 0, minor = 19, patch = 4)
            }
        }
}
