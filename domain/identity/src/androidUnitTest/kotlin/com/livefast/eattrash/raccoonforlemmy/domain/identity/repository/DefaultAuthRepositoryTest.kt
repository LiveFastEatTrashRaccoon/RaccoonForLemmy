package com.livefast.eattrash.raccoonforlemmy.domain.identity.repository

import com.livefast.eattrash.raccoonforlemmy.core.api.dto.LoginForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.LoginResponse
import com.livefast.eattrash.raccoonforlemmy.core.api.provider.ServiceProvider
import com.livefast.eattrash.raccoonforlemmy.core.api.service.v3.AuthServiceV3
import com.livefast.eattrash.raccoonforlemmy.core.testutils.DispatcherTestRule
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
    private val serviceProvider =
        mockk<ServiceProvider>(relaxUnitFun = true) {
            every { v3 } returns mockk { every { auth } returns authServiceV3 }
        }

    private val sut =
        DefaultAuthRepository(
            services = serviceProvider,
        )

    @Test
    fun whenLogin_thenResultIsAsExpected() =
        runTest {
            val loginData = LoginResponse(token = "")
            coEvery { authServiceV3.login(any()) } returns loginData
            val res = sut.login("username", "password")

            assertTrue(res.isSuccess)

            val resultData = res.getOrThrow()
            assertEquals(loginData, resultData)
            coVerify {
                authServiceV3.login(LoginForm("username", "password"))
            }
        }

    @Test
    fun givenFailure_whenLogin_thenResultIsAsExpected() =
        runTest {
            val loginData = LoginResponse(error = "fake-error-message")
            coEvery { authServiceV3.login(any()) } returns loginData
            val res = sut.login("username", "password")

            val resultData = res.getOrThrow()
            assertEquals(loginData, resultData)
            coVerify {
                authServiceV3.login(LoginForm("username", "password"))
            }
        }
}
