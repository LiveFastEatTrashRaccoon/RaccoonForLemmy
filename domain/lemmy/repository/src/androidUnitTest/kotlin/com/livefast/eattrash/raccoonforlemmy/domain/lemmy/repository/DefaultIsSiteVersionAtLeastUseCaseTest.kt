package com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository

import com.livefast.eattrash.raccoonforlemmy.core.testutils.DispatcherTestRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DefaultIsSiteVersionAtLeastUseCaseTest {
    @get:Rule
    val dispatcherTestRule = DispatcherTestRule()

    private val siteRepository = mockk<SiteRepository>()

    private val sut = DefaultIsSiteVersionAtLeastUseCase(siteRepository = siteRepository)

    @Test
    fun givenSameVersion_whenExecute_thenResultIsAsExpected() =
        runTest {
            coEvery { siteRepository.getSiteVersion() } returns "0.19.2"

            val res = sut.execute(major = 0, minor = 19, patch = 2)

            assertTrue(res)
        }

    @Test
    fun givenPatchLessThanThreshold_whenExecute_thenResultIsAsExpected() =
        runTest {
            coEvery { siteRepository.getSiteVersion() } returns "0.19.2"

            val res = sut.execute(major = 0, minor = 19, patch = 3)

            assertFalse(res)
        }

    @Test
    fun givenPatchGreaterThanThreshold_whenExecute_thenResultIsAsExpected() =
        runTest {
            coEvery { siteRepository.getSiteVersion() } returns "0.19.2"

            val res = sut.execute(major = 0, minor = 19, patch = 1)

            assertTrue(res)
        }

    @Test
    fun givenMinorLessThanThreshold_whenExecute_thenResultIsAsExpected() =
        runTest {
            coEvery { siteRepository.getSiteVersion() } returns "0.18.2"

            val res = sut.execute(major = 0, minor = 19, patch = 2)

            assertFalse(res)
        }

    @Test
    fun givenMinorGreaterThanThreshold_whenExecute_thenResultIsAsExpected() =
        runTest {
            coEvery { siteRepository.getSiteVersion() } returns "0.20.2"

            val res = sut.execute(major = 0, minor = 19, patch = 2)

            assertTrue(res)
        }

    @Test
    fun givenMajorLessThanThreshold_whenExecute_thenResultIsAsExpected() =
        runTest {
            coEvery { siteRepository.getSiteVersion() } returns "0.19.2"

            val res = sut.execute(major = 1, minor = 0, patch = 0)

            assertFalse(res)
        }

    @Test
    fun givenMajorGreaterThanThreshold_whenExecute_thenResultIsAsExpected() =
        runTest {
            coEvery { siteRepository.getSiteVersion() } returns "1.0.0"

            val res = sut.execute(major = 0, minor = 19, patch = 2)

            assertTrue(res)
        }
}
