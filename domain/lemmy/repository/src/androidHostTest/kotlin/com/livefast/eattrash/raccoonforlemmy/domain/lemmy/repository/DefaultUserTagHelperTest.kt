package com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository

import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.AccountModel
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.UserTagModel
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.AccountRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.UserTagRepository
import com.livefast.eattrash.raccoonforlemmy.core.testutils.DispatcherTestRule
import com.livefast.eattrash.raccoonforlemmy.core.utils.cache.LruCache
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.UserModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.readableHandle
import io.mockk.Called
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class DefaultUserTagHelperTest {
    @get:Rule
    val dispatcherTestRule = DispatcherTestRule()

    private val accountRepository = mockk<AccountRepository>()
    private val userTagRepository = mockk<UserTagRepository>()
    private val cache = mockk<LruCache<String, List<UserTagModel>>>(relaxUnitFun = true)
    private val sut =
        DefaultUserTagHelper(
            accountRepository = accountRepository,
            userTagRepository = userTagRepository,
            cache = cache,
        )

    @Test
    fun giveNoActiveAccountAndCacheMiss_whenGet_thenResultIsAsExpected() = runTest {
        coEvery { accountRepository.getActive() } returns null
        coEvery { cache.get(any()) } returns null
        val user = UserModel(id = 1L, name = "user", host = "host")

        val res =
            with(sut) {
                user.withTags()
            }

        assertEquals(user, res)
        coVerify {
            cache.get(user.readableHandle)
            accountRepository.getActive()
            userTagRepository wasNot Called
        }
    }

    @Test
    fun givenCacheMiss_whenGet_thenResultIsAsExpected() = runTest {
        val tagName = "test"
        val accountId = 2L
        coEvery { cache.get(any()) } returns null
        coEvery {
            userTagRepository.getTags(any(), any())
        } returns listOf(UserTagModel(id = 1, name = tagName))
        coEvery {
            accountRepository.getActive()
        } returns
            AccountModel(
                id = accountId,
                username = "",
                instance = "",
                jwt = "",
                active = true,
            )
        val user = UserModel(id = 1L, name = "user", host = "host")

        val res =
            with(sut) {
                user.withTags()
            }

        assertNotNull(res)
        assertEquals(1, res.tags.size)
        assertEquals(UserTagModel(name = tagName, color = null), res.tags.first())

        coVerify {
            cache.get(user.readableHandle)
            accountRepository.getActive()
            userTagRepository.getTags(username = user.readableHandle, accountId = accountId)
        }
    }

    @Test
    fun givenCacheHit_whenGet_thenResultIsAsExpected() = runTest {
        val tagName = "test"
        coEvery { cache.get(any()) } returns listOf(UserTagModel(id = 1, name = tagName))
        val user = UserModel(id = 1L, name = "user", host = "host")

        val res =
            with(sut) {
                user.withTags()
            }

        assertNotNull(res)
        assertEquals(1, res.tags.size)
        assertEquals(UserTagModel(name = tagName, color = null), res.tags.first())

        coVerify {
            cache.get(user.readableHandle)
            accountRepository wasNot Called
            userTagRepository wasNot Called
        }
    }

    @Test
    fun whenClear_thenInteractionsAreAsExpected() = runTest {
        sut.clear()

        coVerify {
            cache.clear()
        }
    }
}
