package com.livefast.eattrash.raccoonforlemmy.domain.identity.urlhandler

import com.livefast.eattrash.raccoonforlemmy.core.navigation.MainRouter
import com.livefast.eattrash.raccoonforlemmy.domain.identity.repository.IdentityRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.UserModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.UserRepository
import io.mockk.Called
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DefaultUserProcessorTest {
    private val identityRepository: IdentityRepository =
        mockk {
            every { authToken } returns MutableStateFlow(FAKE_AUTH)
        }
    private val userRepository: UserRepository =
        mockk {
            coEvery { getResolved(any(), any()) } returns null
        }
    private val mainRouter: MainRouter = mockk(relaxUnitFun = true)
    private val urlDecoder: UrlDecoder =
        mockk {
            every { getUser(any()) } returns null
        }
    private val sut =
        DefaultUserProcessor(
            identityRepository = identityRepository,
            userRepository = userRepository,
            mainRouter = mainRouter,
            urlDecoder = urlDecoder,
        )

    @Test
    fun givenResolve_whenProcess_thenResultIsAsExpected() = runTest {
        val item = UserModel(id = 1)
        coEvery { userRepository.getResolved(any(), any()) } returns item

        val res = sut.process(URL)

        assertTrue(res)
        coVerify {
            userRepository.getResolved(URL, FAKE_AUTH)
        }
        verify {
            identityRepository.authToken
            urlDecoder wasNot Called
            mainRouter.openUserDetail(item)
        }
    }

    @Test
    fun givenNotResolveAndValidUrl_whenProcess_thenResultIsAsExpected() = runTest {
        val item = UserModel(id = 1)
        coEvery { urlDecoder.getUser(any()) } returns item

        val res = sut.process(URL)

        assertTrue(res)
        coVerify {
            userRepository.getResolved(URL, FAKE_AUTH)
            urlDecoder.getUser(URL)
        }
        verify {
            identityRepository.authToken
            mainRouter.openUserDetail(item)
        }
    }

    @Test
    fun givenNotResolveAndInvalidUrl_whenProcess_thenResultIsAsExpected() = runTest {
        val res = sut.process(URL)

        assertFalse(res)
        coVerify {
            userRepository.getResolved(URL, FAKE_AUTH)
            urlDecoder.getUser(URL)
        }
        verify {
            identityRepository.authToken
            mainRouter wasNot Called
        }
    }

    companion object {
        private const val URL = "https://example.com"
        private const val FAKE_AUTH = "auth-token"
    }
}
