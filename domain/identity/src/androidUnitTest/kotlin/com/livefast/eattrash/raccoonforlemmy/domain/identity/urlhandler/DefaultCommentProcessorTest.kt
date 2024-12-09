package com.livefast.eattrash.raccoonforlemmy.domain.identity.urlhandler

import com.livefast.eattrash.raccoonforlemmy.core.commonui.detailopener.api.DetailOpener
import com.livefast.eattrash.raccoonforlemmy.domain.identity.repository.IdentityRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.CommentModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.PostModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.CommentRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.PostRepository
import io.mockk.Called
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DefaultCommentProcessorTest {
    private val identityRepository: IdentityRepository =
        mockk {
            every { authToken } returns MutableStateFlow(FAKE_AUTH)
        }
    private val commentRepository: CommentRepository =
        mockk {
            coEvery { getResolved(any(), any()) } returns null
        }
    private val postRepository: PostRepository =
        mockk {
            coEvery { get(any(), any(), any()) } returns null
        }
    private val detailOpener: DetailOpener = mockk(relaxUnitFun = true)
    private val sut =
        DefaultCommentProcessor(
            identityRepository = identityRepository,
            commentRepository = commentRepository,
            postRepository = postRepository,
            detailOpener = detailOpener,
        )

    @Test
    fun givenResolve_whenProcess_thenResultIsAsExpected() =
        runTest {
            val item =
                CommentModel(
                    id = 1,
                    postId = 2,
                )
            coEvery { commentRepository.getResolved(any(), any()) } returns item
            val postItem = PostModel(id = 2)
            coEvery { postRepository.get(id = any(), auth = any()) } returns postItem

            val res = sut.process(URL)

            assertTrue(res)
            coVerify {
                commentRepository.getResolved(URL, FAKE_AUTH)
                postRepository.get(id = 2, auth = FAKE_AUTH)
            }
            verify {
                identityRepository.authToken
                detailOpener.openPostDetail(
                    post = postItem,
                    highlightCommentId = item.id,
                )
            }
        }

    @Test
    fun givenResolveAndInvalidParent_whenProcess_thenResultIsAsExpected() =
        runTest {
            val item =
                CommentModel(
                    id = 1,
                    postId = 2,
                )
            coEvery { commentRepository.getResolved(any(), any()) } returns item

            val res = sut.process(URL)

            assertFalse(res)
            coVerify {
                commentRepository.getResolved(URL, FAKE_AUTH)
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
