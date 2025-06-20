package com.livefast.eattrash.raccoonforlemmy.core.persistence.repository

import app.cash.sqldelight.Query
import com.livefast.eattrash.raccoonforlemmy.core.persistence.FavoriteCommunityEntity
import com.livefast.eattrash.raccoonforlemmy.core.persistence.dao.FavoriteCommunityDao
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.FavoriteCommunityModel
import com.livefast.eattrash.raccoonforlemmy.core.testutils.DispatcherTestRule
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class DefaultFavoriteCommunityRepositoryTest {
    @get:Rule
    val dispatcherTestRule = DispatcherTestRule()

    private val query = mockk<Query<FavoriteCommunityEntity>>()
    private val dao =
        mockk<FavoriteCommunityDao>(relaxUnitFun = true) {
            every { getAll(any()) } returns query
            every { getBy(communityId = any(), accountId = any()) } returns query
        }

    private val sut = DefaultFavoriteCommunityRepository(dao)

    @Test
    fun givenEmpty_whenGetAll_thenResultsAreAsExpected() = runTest {
        every { query.executeAsList() } returns emptyList()

        val res = sut.getAll(1)

        assertTrue(res.isEmpty())
        verify {
            dao.getAll(1)
        }
    }

    @Test
    fun givenNotEmpty_whenGetAll_thenResultsAreAsExpected() = runTest {
        every { query.executeAsList() } returns
            listOf(
                createFakeFavoriteCommunityEntity(
                    id = 1,
                    accountId = 1,
                ),
            )

        val res = sut.getAll(1)

        assertTrue(res.isNotEmpty())
        assertEquals(1, res.first().id)
        verify {
            dao.getAll(1)
        }
    }

    @Test
    fun givenEmpty_whenGetBy_thenResultsAreAsExpected() = runTest {
        every { query.executeAsOneOrNull() } returns null

        val res = sut.getBy(accountId = 1, communityId = 2)

        assertNull(res)
        verify {
            dao.getBy(communityId = 2, accountId = 1)
        }
    }

    @Test
    fun givenNotEmpty_whenGetBy_thenResultsAreAsExpected() = runTest {
        every { query.executeAsOneOrNull() } returns
            createFakeFavoriteCommunityEntity(
                id = 2,
                accountId = 1,
            )

        val res = sut.getBy(accountId = 1, communityId = 3)

        assertNotNull(res)
        assertEquals(2, res.id)
        verify {
            dao.getBy(communityId = 3, accountId = 1)
        }
    }

    @Test
    fun whenCreate_thenInteractionsAreAsExpected() = runTest {
        every { query.executeAsOneOrNull() } returns
            createFakeFavoriteCommunityEntity(
                id = 2,
                accountId = 1,
            )

        val model = FavoriteCommunityModel(communityId = 3)
        val res = sut.create(model = model, accountId = 1)

        assertEquals(2, res)
        verify {
            dao.create(communityId = 3, accountId = 1)
        }
    }

    @Test
    fun whenDelete_thenInteractionsAreAsExpected() = runTest {
        every { query.executeAsOneOrNull() } returns
            createFakeFavoriteCommunityEntity(
                id = 2,
                accountId = 1,
            )

        val model = FavoriteCommunityModel(communityId = 3)
        sut.delete(accountId = 1, model = model)

        verify {
            dao.delete(2)
        }
    }

    private fun createFakeFavoriteCommunityEntity(id: Long = 0, communityId: Long = 0, accountId: Long = 0) =
        FavoriteCommunityEntity(
            id = id,
            communityId = communityId,
            account_id = accountId,
        )
}
