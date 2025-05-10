package com.livefast.eattrash.raccoonforlemmy.domain.lemmy.usecase

import com.livefast.eattrash.raccoonforlemmy.core.testutils.DispatcherTestRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DefaultGetSiteSupportsHiddenPostsUseCaseTest {
    @get:Rule
    val dispatcherTestRule = DispatcherTestRule()

    private val isSiteVersionAtLeastUseCase: IsSiteVersionAtLeastUseCase = mockk()

    private val sut =
        DefaultGetSiteSupportsHiddenPostsUseCase(
            isSiteVersionAtLeastUseCase = isSiteVersionAtLeastUseCase,
        )

    @Test
    fun givenVersionAboveThreshold_whenInvoke_thenResultsIsAsExpected() =
        runTest {
            coEvery {
                isSiteVersionAtLeastUseCase.execute(
                    major = any(),
                    minor = any(),
                    patch = any(),
                    otherInstance = any(),
                )
            } returns true

            val res = sut.invoke()

            assertTrue(res)
        }

    @Test
    fun givenVersionBelowThreshold_whenInvoke_thenResultsIsAsExpected() =
        runTest {
            coEvery {
                isSiteVersionAtLeastUseCase.execute(
                    major = any(),
                    minor = any(),
                    patch = any(),
                    otherInstance = any(),
                )
            } returns false

            val res = sut.invoke()

            assertFalse(res)
        }
}
