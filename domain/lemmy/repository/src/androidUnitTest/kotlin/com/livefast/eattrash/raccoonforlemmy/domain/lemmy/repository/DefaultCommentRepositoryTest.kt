package com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository

import com.livefast.eattrash.raccoonforlemmy.core.api.dto.CommentResponse
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.CreateCommentLikeForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.GetCommentResponse
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.GetCommentsResponse
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.ResolveObjectResponse
import com.livefast.eattrash.raccoonforlemmy.core.api.provider.ServiceProvider
import com.livefast.eattrash.raccoonforlemmy.core.api.service.v3.CommentServiceV3
import com.livefast.eattrash.raccoonforlemmy.core.api.service.v3.SearchServiceV3
import com.livefast.eattrash.raccoonforlemmy.core.testutils.DispatcherTestRule
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.CommentModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.ListingType
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.PersonMentionModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.SortType
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.utils.toAuthHeader
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.utils.toCommentDto
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.utils.toDto
import io.mockk.Called
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class DefaultCommentRepositoryTest {
    @get:Rule
    val dispatcherTestRule = DispatcherTestRule()

    private val searchServiceV3 = mockk<SearchServiceV3>()
    private val commentServiceV3 = mockk<CommentServiceV3>()
    private val serviceProvider =
        mockk<ServiceProvider> {
            every { v3 } returns
                mockk {
                    every { comment } returns commentServiceV3
                    every { search } returns searchServiceV3
                }
        }
    private val customServiceProvider =
        mockk<ServiceProvider>(relaxUnitFun = true) {
            every { v3 } returns
                mockk {
                    every { comment } returns commentServiceV3
                }
        }
    private val sut =
        DefaultCommentRepository(
            services = serviceProvider,
            customServices = customServiceProvider,
        )

    @Test
    fun givenSuccess_whenGetAll_thenResultIsAsExpected() = runTest {
        coEvery {
            commentServiceV3.getAll(
                authHeader = any(),
                auth = any(),
                limit = any(),
                sort = any(),
                postId = any(),
                parentId = any(),
                page = any(),
                maxDepth = any(),
                type = any(),
                communityId = any(),
                communityName = any(),
                savedOnly = any(),
                likedOnly = any(),
                dislikedOnly = any(),
            )
        } returns GetCommentsResponse(comments = listOf(mockk(relaxed = true)))
        val token = FAKE_TOKEN
        val res =
            sut.getAll(
                postId = 1,
                auth = token,
                page = 1,
                type = ListingType.All,
                sort = SortType.New,
                limit = 20,
                maxDepth = 6,
            )

        assertNotNull(res)
        assertEquals(1, res.size)

        coVerify {
            customServiceProvider wasNot Called
            commentServiceV3.getAll(
                authHeader = token.toAuthHeader(),
                auth = token,
                postId = 1,
                page = 1,
                type = ListingType.All.toDto(),
                sort = SortType.New.toCommentDto(),
                limit = 20,
                maxDepth = 6,
            )
        }
    }

    @Test
    fun givenSuccess_whenGetBy_thenResultIsAsExpected() = runTest {
        val token = FAKE_TOKEN
        val commentId = 1L
        coEvery { commentServiceV3.getBy(any(), any(), any()) } returns
            GetCommentResponse(
                commentView =
                mockk(relaxed = true) {
                    every { comment } returns mockk(relaxed = true) { every { id } returns commentId }
                },
            )

        val res = sut.getBy(id = commentId, auth = token)

        assertNotNull(res)
        assertEquals(1, res.id)
        coVerify {
            customServiceProvider wasNot Called
            commentServiceV3.getBy(
                authHeader = token.toAuthHeader(),
                id = commentId,
                auth = token,
            )
        }
    }

    @Test
    fun whenGetChildren_thenResultIsAsExpected() = runTest {
        coEvery {
            commentServiceV3.getAll(
                authHeader = any(),
                auth = any(),
                limit = any(),
                sort = any(),
                postId = any(),
                parentId = any(),
                page = any(),
                maxDepth = any(),
                type = any(),
                communityId = any(),
                communityName = any(),
                savedOnly = any(),
                likedOnly = any(),
                dislikedOnly = any(),
            )
        } returns GetCommentsResponse(comments = listOf(mockk(relaxed = true)))
        val token = FAKE_TOKEN
        val res =
            sut.getChildren(
                parentId = 2,
                auth = token,
                type = ListingType.All,
                sort = SortType.New,
                limit = 20,
                maxDepth = 6,
            )

        assertNotNull(res)
        assertEquals(1, res.size)

        coVerify {
            customServiceProvider wasNot Called
            commentServiceV3.getAll(
                authHeader = token.toAuthHeader(),
                auth = token,
                parentId = 2,
                type = ListingType.All.toDto(),
                sort = SortType.New.toCommentDto(),
                limit = 20,
                maxDepth = 6,
            )
        }
    }

    @Test
    fun whenAsUpVotedMention_thenResultIsAsExpected() = runTest {
        val mention =
            PersonMentionModel(
                id = 1,
                post = mockk(),
                comment = mockk(),
                community = mockk(),
                creator = mockk(),
            )

        val res = sut.asUpVoted(mention, true)

        assertEquals(1, res.myVote)
        assertEquals(1, res.score)
        assertEquals(1, res.upvotes)

        val afterRes = sut.asUpVoted(res, false)
        assertEquals(0, afterRes.myVote)
        assertEquals(0, afterRes.score)
        assertEquals(0, afterRes.upvotes)
    }

    @Test
    fun whenAsUpVoted_thenResultIsAsExpected() = runTest {
        val comment = CommentModel(text = "text")

        val res = sut.asUpVoted(comment, true)

        assertEquals(1, res.myVote)
        assertEquals(1, res.score)
        assertEquals(1, res.upvotes)

        val afterRes = sut.asUpVoted(res, false)
        assertEquals(0, afterRes.myVote)
        assertEquals(0, afterRes.score)
        assertEquals(0, afterRes.upvotes)
    }

    @Test
    fun givenSuccess_whenUpVote_thenResultIsAsExpected() = runTest {
        val comment = CommentModel(id = 1, text = "text")
        val token = FAKE_TOKEN
        coEvery {
            commentServiceV3.like(
                any(),
                any(),
            )
        } returns
            CommentResponse(
                commentView = mockk(relaxed = true),
                recipientIds = listOf(),
            )

        val res = sut.upVote(comment, token, true)

        assertNotNull(res)
        coVerify {
            commentServiceV3.like(
                authHeader = token.toAuthHeader(),
                form =
                CreateCommentLikeForm(
                    commentId = 1,
                    score = 1,
                    auth = token,
                ),
            )
        }
    }

    @Test
    fun whenAsDownVotedMention_thenResultIsAsExpected() = runTest {
        val mention =
            PersonMentionModel(
                id = 1,
                post = mockk(),
                comment = mockk(),
                community = mockk(),
                creator = mockk(),
            )

        val res = sut.asDownVoted(mention, true)

        assertEquals(-1, res.myVote)
        assertEquals(-1, res.score)
        assertEquals(1, res.downvotes)

        val afterRes = sut.asDownVoted(res, false)
        assertEquals(0, afterRes.myVote)
        assertEquals(0, afterRes.score)
        assertEquals(0, afterRes.upvotes)
    }

    @Test
    fun whenAsDownVoted_thenResultIsAsExpected() = runTest {
        val comment = CommentModel(text = "text")

        val res = sut.asDownVoted(comment, true)

        assertEquals(-1, res.myVote)
        assertEquals(-1, res.score)
        assertEquals(1, res.downvotes)

        val afterRes = sut.asDownVoted(res, false)
        assertEquals(0, afterRes.myVote)
        assertEquals(0, afterRes.score)
        assertEquals(0, afterRes.downvotes)
    }

    @Test
    fun givenSuccess_whenUDownVote_thenResultIsAsExpected() = runTest {
        val comment = CommentModel(id = 1, text = "text")
        val token = FAKE_TOKEN
        coEvery {
            commentServiceV3.like(
                any(),
                any(),
            )
        } returns
            CommentResponse(
                commentView = mockk(relaxed = true),
                recipientIds = listOf(),
            )

        val res = sut.downVote(comment, token, true)

        assertNotNull(res)
        coVerify {
            commentServiceV3.like(
                authHeader = token.toAuthHeader(),
                form =
                CreateCommentLikeForm(
                    commentId = 1,
                    score = -1,
                    auth = token,
                ),
            )
        }
    }

    @Test
    fun whenAsSaved_thenResultIsAsExpected() = runTest {
        val comment = CommentModel(text = "text")

        val res = sut.asSaved(comment, true)
        assertTrue(res.saved)

        val afterRes = sut.asSaved(res, false)
        assertFalse(afterRes.saved)
    }

    @Test
    fun whenCreate_thenInteractionsAreAsExpected() = runTest {
        val postId = 1L
        val parentId = 0L
        val text = "test"
        val token = FAKE_TOKEN
        sut.create(
            postId = postId,
            parentId = parentId,
            text = text,
            auth = token,
        )

        coVerify {
            commentServiceV3.create(
                authHeader = token.toAuthHeader(),
                form =
                withArg { data ->
                    assertEquals(token, data.auth)
                    assertEquals(postId, data.postId)
                    assertEquals(parentId, data.parentId)
                    assertEquals(text, data.content)
                },
            )
        }
    }

    @Test
    fun whenEdit_thenInteractionsAreAsExpected() = runTest {
        val itemId = 1L
        val text = "test"
        val token = FAKE_TOKEN
        sut.edit(
            commentId = itemId,
            text = text,
            auth = token,
        )

        coVerify {
            commentServiceV3.edit(
                authHeader = token.toAuthHeader(),
                form =
                withArg { data ->
                    assertEquals(token, data.auth)
                    assertEquals(itemId, data.commentId)
                    assertEquals(text, data.content)
                },
            )
        }
    }

    @Test
    fun whenDelete_thenInteractionsAreAsExpected() = runTest {
        val itemId = 1L
        val token = FAKE_TOKEN
        sut.delete(
            commentId = itemId,
            auth = token,
        )

        coVerify {
            commentServiceV3.delete(
                authHeader = token.toAuthHeader(),
                form =
                withArg { data ->
                    assertEquals(itemId, data.commentId)
                    assertTrue(data.deleted)
                },
            )
        }
    }

    @Test
    fun whenRestore_thenInteractionsAreAsExpected() = runTest {
        val itemId = 1L
        val token = FAKE_TOKEN
        sut.restore(
            commentId = itemId,
            auth = token,
        )

        coVerify {
            commentServiceV3.delete(
                authHeader = token.toAuthHeader(),
                form =
                withArg { data ->
                    assertEquals(itemId, data.commentId)
                    assertFalse(data.deleted)
                },
            )
        }
    }

    @Test
    fun whenReport_thenInteractionsAreAsExpected() = runTest {
        val itemId = 1L
        val token = FAKE_TOKEN
        val reason = "reason"
        sut.report(
            commentId = itemId,
            auth = token,
            reason = reason,
        )

        coVerify {
            commentServiceV3.createReport(
                authHeader = token.toAuthHeader(),
                form =
                withArg { data ->
                    assertEquals(itemId, data.commentId)
                    assertEquals(token, data.auth)
                    assertEquals(reason, data.reason)
                },
            )
        }
    }

    @Test
    fun whenRemove_thenInteractionsAreAsExpected() = runTest {
        val itemId = 1L
        val token = FAKE_TOKEN
        val reason = "reason"
        sut.remove(
            commentId = itemId,
            auth = token,
            reason = reason,
            removed = true,
        )

        coVerify {
            commentServiceV3.remove(
                authHeader = token.toAuthHeader(),
                form =
                withArg { data ->
                    assertEquals(itemId, data.commentId)
                    assertEquals(reason, data.reason)
                    assertEquals(token, data.auth)
                    assertTrue(data.removed)
                },
            )
        }
    }

    @Test
    fun whenDistinguish_thenInteractionsAreAsExpected() = runTest {
        val itemId = 1L
        val token = FAKE_TOKEN
        sut.distinguish(
            commentId = itemId,
            auth = token,
            distinguished = true,
        )

        coVerify {
            commentServiceV3.distinguish(
                authHeader = token.toAuthHeader(),
                form =
                withArg { data ->
                    assertEquals(itemId, data.commentId)
                    assertEquals(token, data.auth)
                    assertTrue(data.distinguished)
                },
            )
        }
    }

    @Test
    fun whenGetReports_thenInteractionsAreAsExpected() = runTest {
        val itemId = 1L
        val token = FAKE_TOKEN
        sut.getReports(
            communityId = itemId,
            auth = token,
            page = 1,
            unresolvedOnly = true,
        )

        coVerify {
            commentServiceV3.listReports(
                authHeader = token.toAuthHeader(),
                communityId = itemId,
                auth = token,
                page = 1,
                limit = any(),
                unresolvedOnly = true,
            )
        }
    }

    @Test
    fun whenResolveReport_thenInteractionsAreAsExpected() = runTest {
        val itemId = 1L
        val token = FAKE_TOKEN
        sut.resolveReport(
            reportId = itemId,
            auth = token,
            resolved = true,
        )

        coVerify {
            commentServiceV3.resolveReport(
                authHeader = token.toAuthHeader(),
                form =
                withArg { data ->
                    assertEquals(itemId, data.reportId)
                    assertEquals(token, data.auth)
                    assertTrue(data.resolved)
                },
            )
        }
    }

    @Test
    fun whenGetResolved_thenInteractionsAreAsExpected() = runTest {
        val commentId = 1L
        coEvery {
            searchServiceV3.resolveObject(any(), any())
        } returns
            ResolveObjectResponse(
                comment =
                mockk(relaxed = true) {
                    every { comment } returns mockk(relaxed = true) { every { id } returns commentId }
                },
            )
        val token = FAKE_TOKEN
        val res =
            sut.getResolved(
                query = "text",
                auth = token,
            )

        assertEquals(commentId, res?.id)
        coVerify {
            searchServiceV3.resolveObject(
                authHeader = token.toAuthHeader(),
                q = "text",
            )
        }
    }

    companion object {
        private const val FAKE_TOKEN = "fake-token"
    }
}
