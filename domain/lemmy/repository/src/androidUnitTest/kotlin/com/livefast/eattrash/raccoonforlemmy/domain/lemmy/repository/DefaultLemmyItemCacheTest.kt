package com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository

import com.livefast.eattrash.raccoonforlemmy.core.testutils.DispatcherTestRule
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.CommentModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.CommunityModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.PostModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.UserModel
import io.mockk.Called
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DefaultLemmyItemCacheTest {
    @get:Rule
    val dispatcherTestRule = DispatcherTestRule()

    private val postCache = mockk<LocalItemCache<PostModel>>(relaxUnitFun = true)
    private val communityCache = mockk<LocalItemCache<CommunityModel>>(relaxUnitFun = true)
    private val commentCache = mockk<LocalItemCache<CommentModel>>(relaxUnitFun = true)
    private val userCache = mockk<LocalItemCache<UserModel>>(relaxUnitFun = true)

    private val sut =
        DefaultLemmyItemCache(
            postCache = postCache,
            commentCache = commentCache,
            communityCache = communityCache,
            userCache = userCache,
        )

    @Test
    fun whenPutPost_thenGetPostReturnsExpectedResult() = runTest {
        val id = 1L
        val model = PostModel(id = id)
        coEvery {
            postCache.get(any())
        } returns model

        sut.putPost(model)
        val res = sut.getPost(id)

        assertEquals(res, model)
        coVerify {
            postCache.put(value = model, key = id)
            postCache.get(id)
            commentCache wasNot Called
            communityCache wasNot Called
            userCache wasNot Called
        }
    }

    @Test
    fun givenEmptyCache_whenGetPost_thenResultIsAsExpected() = runTest {
        val id = 1L
        coEvery {
            postCache.get(any())
        } returns null

        val res = sut.getPost(id)

        assertNull(res)
        coVerify {
            postCache.get(id)
            commentCache wasNot Called
            communityCache wasNot Called
            userCache wasNot Called
        }
    }

    @Test
    fun whenPutComment_thenGetCommentReturnsExpectedResult() = runTest {
        val id = 1L
        val model = CommentModel(id = id, text = "")
        coEvery {
            commentCache.get(any())
        } returns model

        sut.putComment(model)
        val res = sut.getComment(id)

        assertEquals(res, model)
        coVerify {
            commentCache.put(value = model, key = id)
            commentCache.get(id)
            postCache wasNot Called
            communityCache wasNot Called
            userCache wasNot Called
        }
    }

    @Test
    fun givenEmptyCache_whenGetComment_thenResultIsAsExpected() = runTest {
        val id = 1L
        coEvery {
            commentCache.get(any())
        } returns null

        val res = sut.getComment(id)

        assertNull(res)
        coVerify {
            commentCache.get(id)
            postCache wasNot Called
            communityCache wasNot Called
            userCache wasNot Called
        }
    }

    @Test
    fun whenPutCommunity_thenGetCommunityReturnsExpectedResult() = runTest {
        val id = 1L
        val model = CommunityModel(id = id)
        coEvery {
            communityCache.get(any())
        } returns model

        sut.putCommunity(model)
        val res = sut.getCommunity(id)

        assertEquals(res, model)
        coVerify {
            communityCache.put(value = model, key = id)
            communityCache.get(id)
            postCache wasNot Called
            commentCache wasNot Called
            userCache wasNot Called
        }
    }

    @Test
    fun givenEmptyCache_whenGetCommunity_thenResultIsAsExpected() = runTest {
        val id = 1L
        coEvery {
            communityCache.get(any())
        } returns null

        val res = sut.getCommunity(id)

        assertNull(res)
        coVerify {
            communityCache.get(id)
            postCache wasNot Called
            commentCache wasNot Called
            userCache wasNot Called
        }
    }

    @Test
    fun whenPutUser_thenGetUserReturnsExpectedResult() = runTest {
        val id = 1L
        val model = UserModel(id = id)
        coEvery {
            userCache.get(any())
        } returns model

        sut.putUser(model)
        val res = sut.getUser(id)

        assertEquals(res, model)
        coVerify {
            userCache.put(value = model, key = id)
            userCache.get(id)
            postCache wasNot Called
            commentCache wasNot Called
            communityCache wasNot Called
        }
    }

    @Test
    fun givenEmptyCache_whenGetUser_thenResultIsAsExpected() = runTest {
        val id = 1L
        coEvery {
            userCache.get(any())
        } returns null

        val res = sut.getUser(id)

        assertNull(res)
        coVerify {
            userCache.get(id)
            postCache wasNot Called
            commentCache wasNot Called
            communityCache wasNot Called
        }
    }
}
