package com.livefast.eattrash.raccoonforlemmy.domain.identity.urlhandler

import com.livefast.eattrash.raccoonforlemmy.core.commonui.detailopener.api.DetailOpener
import com.livefast.eattrash.raccoonforlemmy.domain.identity.repository.IdentityRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.PostModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.PostRepository
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

class DefaultPostProcessorTest {
    private val identityRepository: IdentityRepository =
        mockk {
            every { authToken } returns MutableStateFlow(FAKE_AUTH)
        }
    private val postRepository: PostRepository =
        mockk {
            coEvery { getResolved(any(), any()) } returns null
        }
    private val detailOpener: DetailOpener = mockk(relaxUnitFun = true)
    private val urlDecoder: UrlDecoder =
        mockk {
            every { getPost(any()) } returns (null to null)
        }
    private val sut =
        DefaultPostProcessor(
            identityRepository = identityRepository,
            postRepository = postRepository,
            detailOpener = detailOpener,
            urlDecoder = urlDecoder,
        )

    @Test
    fun givenResolve_whenProcess_thenResultIsAsExpected() = runTest {
        val item = PostModel(id = 1)
        coEvery { postRepository.getResolved(any(), any()) } returns item

        val res = sut.process(URL)

        assertTrue(res)
        coVerify {
            postRepository.getResolved(URL, FAKE_AUTH)
        }
        verify {
            identityRepository.authToken
            urlDecoder wasNot Called
            detailOpener.openPostDetail(item)
        }
    }

    @Test
    fun givenNotResolveAndValidUrl_whenProcess_thenResultIsAsExpected() = runTest {
        val item = PostModel(id = 1)
        coEvery { urlDecoder.getPost(any()) } returns (item to "")

        val res = sut.process(URL)

        assertTrue(res)
        coVerify {
            postRepository.getResolved(URL, FAKE_AUTH)
            urlDecoder.getPost(URL)
        }
        verify {
            identityRepository.authToken
            detailOpener.openPostDetail(item)
        }
    }

    @Test
    fun givenNotResolveAndInvalidUrl_whenProcess_thenResultIsAsExpected() = runTest {
        val res = sut.process(URL)

        assertFalse(res)
        coVerify {
            postRepository.getResolved(URL, FAKE_AUTH)
            urlDecoder.getPost(URL)
        }
        verify {
            identityRepository.authToken
            detailOpener wasNot Called
        }
    }

    companion object {
        private const val URL = "https://example.com"
        private const val FAKE_AUTH = "auth-token"
    }
}
