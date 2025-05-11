package com.livefast.eattrash.raccoonforlemmy.domain.lemmy.usecase

import com.livefast.eattrash.raccoonforlemmy.core.testutils.DispatcherTestRule
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.utils.SiteVersionDataSource
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

    private val siteVersionDataSource: SiteVersionDataSource = mockk()

    private val sut =
        DefaultGetSiteSupportsHiddenPostsUseCase(
            siteVersionDataSource = siteVersionDataSource,
        )

    @Test
    fun givenVersionAboveThreshold_whenInvoke_thenResultsIsAsExpected() =
        runTest {
            coEvery {
                siteVersionDataSource.isAtLeast(
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
                siteVersionDataSource.isAtLeast(
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
