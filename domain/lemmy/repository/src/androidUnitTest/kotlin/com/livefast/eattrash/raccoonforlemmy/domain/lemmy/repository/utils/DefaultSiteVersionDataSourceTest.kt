package com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.utils

import com.livefast.eattrash.raccoonforlemmy.core.api.provider.ServiceProvider
import com.livefast.eattrash.raccoonforlemmy.core.testutils.DispatcherTestRule
import io.mockk.Called
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DefaultSiteVersionDataSourceTest {
    @get:Rule
    val dispatcherTestRule = DispatcherTestRule()

    private val services =
        mockk<ServiceProvider> {
            every { currentInstance } returns "feddit.it"
        }
    private val customServices = mockk<ServiceProvider>()

    private val sut =
        DefaultSiteVersionDataSource(
            services = services,
            customServices = customServices,
        )

    @Test
    fun givenSameVersion_whenInvoke_thenResultIsAsExpected() =
        runTest {
            coEvery { services.getApiVersion() } returns "0.19.2"

            val res = sut.isAtLeast(major = 0, minor = 19, patch = 2)

            assertTrue(res)
            verify {
                customServices wasNot Called
            }
        }

    @Test
    fun givenPatchLessThanThreshold_whenInvoke_thenResultIsAsExpected() =
        runTest {
            coEvery { services.getApiVersion() } returns "0.19.2"

            val res = sut.isAtLeast(major = 0, minor = 19, patch = 3)

            assertFalse(res)
            verify {
                customServices wasNot Called
            }
        }

    @Test
    fun givenPatchGreaterThanThreshold_whenInvoke_thenResultIsAsExpected() =
        runTest {
            coEvery { services.getApiVersion() } returns "0.19.2"

            val res = sut.isAtLeast(major = 0, minor = 19, patch = 1)

            assertTrue(res)
            verify {
                customServices wasNot Called
            }
        }

    @Test
    fun givenMinorLessThanThreshold_whenInvoke_thenResultIsAsExpected() =
        runTest {
            coEvery { services.getApiVersion() } returns "0.18.2"

            val res = sut.isAtLeast(major = 0, minor = 19, patch = 2)

            assertFalse(res)
            verify {
                customServices wasNot Called
            }
        }

    @Test
    fun givenMinorGreaterThanThreshold_whenInvoke_thenResultIsAsExpected() =
        runTest {
            coEvery { services.getApiVersion() } returns "0.20.2"

            val res = sut.isAtLeast(major = 0, minor = 19, patch = 2)

            assertTrue(res)
            verify {
                customServices wasNot Called
            }
        }

    @Test
    fun givenMajorLessThanThreshold_whenInvoke_thenResultIsAsExpected() =
        runTest {
            coEvery { services.getApiVersion() } returns "0.19.2"

            val res = sut.isAtLeast(major = 1, minor = 0, patch = 0)

            assertFalse(res)
            verify {
                customServices wasNot Called
            }
        }

    @Test
    fun givenMajorGreaterThanThreshold_whenInvoke_thenResultIsAsExpected() =
        runTest {
            coEvery { services.getApiVersion() } returns "1.0.0"

            val res = sut.isAtLeast(major = 0, minor = 19, patch = 2)

            assertTrue(res)
            verify {
                customServices wasNot Called
            }
        }
}
