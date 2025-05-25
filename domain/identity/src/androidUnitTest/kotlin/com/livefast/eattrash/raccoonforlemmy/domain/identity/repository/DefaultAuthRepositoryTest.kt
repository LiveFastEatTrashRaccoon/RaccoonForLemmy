package com.livefast.eattrash.raccoonforlemmy.domain.identity.repository

import com.livefast.eattrash.raccoonforlemmy.core.api.dto.LoginForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.LoginResponse
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.SuccessResponse
import com.livefast.eattrash.raccoonforlemmy.core.api.provider.ServiceProvider
import com.livefast.eattrash.raccoonforlemmy.core.api.service.v3.AuthServiceV3
import com.livefast.eattrash.raccoonforlemmy.core.api.service.v4.AccountServiceV4
import com.livefast.eattrash.raccoonforlemmy.core.testutils.DispatcherTestRule
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.utils.SiteVersionDataSource
import io.mockk.Called
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DefaultAuthRepositoryTest {
    @get:Rule
    val dispatcherTestRule = DispatcherTestRule()

    private val authServiceV3 = mockk<AuthServiceV3>()
    private val accountServiceV4 = mockk<AccountServiceV4>()
    private val serviceProvider =
        mockk<ServiceProvider>(relaxUnitFun = true) {
            every { v3 } returns mockk { every { auth } returns authServiceV3 }
            every { v4 } returns mockk { every { account } returns accountServiceV4 }
        }
    private val siteVersionDataSource =
        mockk<SiteVersionDataSource> {
            coEvery {
                isAtLeast(
                    major = any(),
                    minor = any(),
                    patch = any(),
                    otherInstance = any(),
                )
            } returns false
        }

    private val sut =
        DefaultAuthRepository(
            services = serviceProvider,
            siteVersionDataSource = siteVersionDataSource,
        )

    // region V3
    @Test
    fun givenV3AndSuccess_whenLogin_thenResultIsAsExpected() =
        runTest {
            val loginData = LoginResponse(token = "")
            coEvery { authServiceV3.login(any()) } returns loginData

            val res = sut.login("username", "password")

            assertTrue(res.isSuccess)
            val resultData = res.getOrThrow()
            assertEquals(loginData, resultData)
            coVerify {
                siteVersionDataSource.isAtLeast(major = 1, minor = 0, patch = 0)
                authServiceV3.login(LoginForm("username", "password"))
                accountServiceV4 wasNot Called
            }
        }

    @Test
    fun givenV3AndFailure_whenLogin_thenResultIsAsExpected() =
        runTest {
            val loginData = LoginResponse(error = "fake-error-message")
            coEvery { authServiceV3.login(any()) } returns loginData

            val res = sut.login("username", "password")

            val resultData = res.getOrThrow()
            assertEquals(loginData, resultData)
            coVerify {
                siteVersionDataSource.isAtLeast(major = 1, minor = 0, patch = 0)
                authServiceV3.login(LoginForm("username", "password"))
                accountServiceV4 wasNot Called
            }
        }

    @Test
    fun givenV3AndSuccess_whenLogout_thenResultIsAsExpected() =
        runTest {
            val response = SuccessResponse(success = true)
            coEvery { authServiceV3.logout() } returns response

            val res = sut.logout()

            assertTrue(res.isSuccess)
            coVerify {
                siteVersionDataSource.isAtLeast(major = 1, minor = 0, patch = 0)
                authServiceV3.logout()
                accountServiceV4 wasNot Called
            }
        }

    @Test
    fun givenV3AndFailure_whenLogout_thenResultIsAsExpected() =
        runTest {
            val response = SuccessResponse(success = false)
            coEvery { authServiceV3.logout() } returns response

            val res = sut.logout()

            assertTrue(res.isFailure)
            coVerify {
                siteVersionDataSource.isAtLeast(major = 1, minor = 0, patch = 0)
                authServiceV3.logout()
                accountServiceV4 wasNot Called
            }
        }
    // endregion

    // region V4
    @Test
    fun givenV4AndSuccess_whenLogin_thenResultIsAsExpected() =
        runTest {
            coEvery {
                siteVersionDataSource.isAtLeast(
                    major = any(),
                    minor = any(),
                    patch = any(),
                    otherInstance = any()
                )
            } returns true
            val loginData = LoginResponse(token = "")
            coEvery { accountServiceV4.login(any()) } returns loginData

            val res = sut.login("username", "password")

            assertTrue(res.isSuccess)
            val resultData = res.getOrThrow()
            assertEquals(loginData, resultData)
            coVerify {
                siteVersionDataSource.isAtLeast(major = 1, minor = 0, patch = 0)
                accountServiceV4.login(LoginForm("username", "password"))
                authServiceV3 wasNot Called
            }
        }

    @Test
    fun givenV4AndFailure_whenLogin_thenResultIsAsExpected() =
        runTest {
            coEvery {
                siteVersionDataSource.isAtLeast(
                    major = any(),
                    minor = any(),
                    patch = any(),
                    otherInstance = any()
                )
            } returns true
            val loginData = LoginResponse(error = "fake-error-message")
            coEvery { accountServiceV4.login(any()) } returns loginData

            val res = sut.login("username", "password")

            val resultData = res.getOrThrow()
            assertEquals(loginData, resultData)
            coVerify {
                siteVersionDataSource.isAtLeast(major = 1, minor = 0, patch = 0)
                accountServiceV4.login(LoginForm("username", "password"))
                authServiceV3 wasNot Called
            }
        }

    @Test
    fun givenV4AndSuccess_whenLogout_thenResultIsAsExpected() =
        runTest {
            coEvery {
                siteVersionDataSource.isAtLeast(
                    major = any(),
                    minor = any(),
                    patch = any(),
                    otherInstance = any()
                )
            } returns true
            val response = SuccessResponse(success = true)
            coEvery { accountServiceV4.logout() } returns response

            val res = sut.logout()

            assertTrue(res.isSuccess)
            coVerify {
                siteVersionDataSource.isAtLeast(major = 1, minor = 0, patch = 0)
                accountServiceV4.logout()
                authServiceV3 wasNot Called
            }
        }

    @Test
    fun givenV4AndFailure_whenLogout_thenResultIsAsExpected() =
        runTest {
            coEvery {
                siteVersionDataSource.isAtLeast(
                    major = any(),
                    minor = any(),
                    patch = any(),
                    otherInstance = any()
                )
            } returns true
            val response = SuccessResponse(success = false)
            coEvery { accountServiceV4.logout() } returns response

            val res = sut.logout()

            assertTrue(res.isFailure)
            coVerify {
                siteVersionDataSource.isAtLeast(major = 1, minor = 0, patch = 0)
                accountServiceV4.logout()
                authServiceV3 wasNot Called
            }
        }
    // endregion
}
