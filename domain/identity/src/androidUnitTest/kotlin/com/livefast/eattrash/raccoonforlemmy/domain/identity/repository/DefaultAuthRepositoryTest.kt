package com.livefast.eattrash.raccoonforlemmy.domain.identity.repository

import com.livefast.eattrash.raccoonforlemmy.core.api.dto.LoginForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.LoginResponse
import com.livefast.eattrash.raccoonforlemmy.core.api.provider.ServiceProvider
import com.livefast.eattrash.raccoonforlemmy.core.api.service.AuthService
import com.livefast.eattrash.raccoonforlemmy.core.testutils.DispatcherTestRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class DefaultAuthRepositoryTest {
    @get:Rule
    val dispatcherTestRule = DispatcherTestRule()

    private val authService = mockk<AuthService>()
    private val serviceProvider =
        mockk<ServiceProvider>(relaxUnitFun = true) {
            every { auth } returns authService
        }

    private val sut =
        DefaultAuthRepository(
            services = serviceProvider,
        )

    @Test
    fun whenLogin_thenResultIsAsExpected() =
        runTest {
            val loginData = LoginResponse(token = "")
            coEvery { authService.login(any()) } returns loginData
            val res = sut.login("username", "password")

            assertTrue(res.isSuccess)

            val resultData = res.getOrThrow()
            assertEquals(loginData, resultData)
            coVerify {
                authService.login(LoginForm("username", "password"))
            }
        }

    @Test
    fun givenFailure_whenLogin_thenResultIsAsExpected() =
        runTest {
            val loginData = LoginResponse(error = "fake-error-message")
            coEvery { authService.login(any()) } returns loginData
            val res = sut.login("username", "password")

            val resultData = res.getOrThrow()
            assertEquals(loginData, resultData)
            coVerify {
                authService.login(LoginForm("username", "password"))
            }
        }
}
