package com.livefast.eattrash.raccoonforlemmy.domain.identity.urlhandler

import com.livefast.eattrash.raccoonforlemmy.core.navigation.MainRouter
import com.livefast.eattrash.raccoonforlemmy.domain.identity.repository.IdentityRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.CommunityModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.CommunityRepository
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

class DefaultCommunityProcessorTest {
    private val identityRepository: IdentityRepository =
        mockk {
            every { authToken } returns MutableStateFlow(FAKE_AUTH)
        }
    private val communityRepository: CommunityRepository =
        mockk {
            coEvery { getResolved(any(), any()) } returns null
        }
    private val mainRouter: MainRouter = mockk(relaxUnitFun = true)
    private val urlDecoder: UrlDecoder =
        mockk {
            every { getCommunity(any()) } returns null
        }
    private val sut =
        DefaultCommunityProcessor(
            identityRepository = identityRepository,
            communityRepository = communityRepository,
            mainRouter = mainRouter,
            urlDecoder = urlDecoder,
        )

    @Test
    fun givenResolve_whenProcess_thenResultIsAsExpected() = runTest {
        val item = CommunityModel(id = 1)
        coEvery { communityRepository.getResolved(any(), any()) } returns item

        val res = sut.process(URL)

        assertTrue(res)
        coVerify {
            communityRepository.getResolved(URL, FAKE_AUTH)
        }
        verify {
            identityRepository.authToken
            urlDecoder wasNot Called
            mainRouter.openCommunityDetail(item)
        }
    }

    @Test
    fun givenNotResolveAndValidUrl_whenProcess_thenResultIsAsExpected() = runTest {
        val item = CommunityModel(id = 1)
        coEvery { urlDecoder.getCommunity(any()) } returns item

        val res = sut.process(URL)

        assertTrue(res)
        coVerify {
            communityRepository.getResolved(URL, FAKE_AUTH)
            urlDecoder.getCommunity(URL)
        }
        verify {
            identityRepository.authToken
            mainRouter.openCommunityDetail(item)
        }
    }

    @Test
    fun givenNotResolveAndInvalidUrl_whenProcess_thenResultIsAsExpected() = runTest {
        val res = sut.process(URL)

        assertFalse(res)
        coVerify {
            communityRepository.getResolved(URL, FAKE_AUTH)
            urlDecoder.getCommunity(URL)
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
