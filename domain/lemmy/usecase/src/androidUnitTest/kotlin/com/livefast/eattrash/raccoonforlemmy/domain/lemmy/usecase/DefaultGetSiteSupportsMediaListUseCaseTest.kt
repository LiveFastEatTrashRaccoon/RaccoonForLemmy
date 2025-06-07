package com.livefast.eattrash.raccoonforlemmy.domain.lemmy.usecase

import com.livefast.eattrash.raccoonforlemmy.core.testutils.DispatcherTestRule
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.utils.SiteVersionDataSource
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

    private val siteVersionDataSource: SiteVersionDataSource =
        mockk {
            coEvery {
                isAtLeast(
                    major = any(),
                    minor = any(),
                    patch = any(),
                    otherInstance = any(),
                )
            } returns true
        }
    private val sut =
        DefaultGetSiteSupportsMediaListUseCase(
            siteVersionDataSource = siteVersionDataSource,
        )

    @Test
    fun whenInvoked_thenResultAndInteractionsAreAsExpected() = runTest {
        val res = sut()

        assertTrue(res)
        coVerify {
            siteVersionDataSource.isAtLeast(major = 0, minor = 19, patch = 4)
        }
    }
}
