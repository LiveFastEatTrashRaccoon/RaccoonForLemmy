package com.livefast.eattrash.raccoonforlemmy.core.persistence.repository

import app.cash.sqldelight.Query
import com.livefast.eattrash.raccoonforlemmy.core.persistence.MultiCommunityEntity
import com.livefast.eattrash.raccoonforlemmy.core.persistence.dao.MultiCommunityDao
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.MultiCommunityModel
import com.livefast.eattrash.raccoonforlemmy.core.testutils.DispatcherTestRule
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class DefaultMultiCommunityRepositoryTest {
    @get:Rule
    val dispatcherTestRule = DispatcherTestRule()

    private val query = mockk<Query<MultiCommunityEntity>>()
    private val dao =
        mockk<MultiCommunityDao>(relaxUnitFun = true) {
            every { getAll(any()) } returns query
            every { getBy(name = any(), accountId = any()) } returns query
            every { getById(any()) } returns query
        }

    private val sut = DefaultMultiCommunityRepository(dao)

    @Test
    fun givenEmpty_whenGetAll_thenResultIsAsExpected() = runTest {
        every { query.executeAsList() } returns listOf()

        val res = sut.getAll(1)

        assertTrue(res.isEmpty())
        verify {
            dao.getAll(1)
        }
    }

    @Test
    fun givenNotEmpty_whenGetAll_thenResultIsAsExpected() = runTest {
        every { query.executeAsList() } returns listOf(createFakeMultiCommunityEntity(id = 2))

        val res = sut.getAll(1)

        assertTrue(res.isNotEmpty())
        assertEquals(2, res.first().id)
        verify {
            dao.getAll(1)
        }
    }

    @Test
    fun givenEmpty_whenGetById_thenResultIsAsExpected() = runTest {
        every { query.executeAsOneOrNull() } returns null

        val res = sut.getById(1)

        assertNull(res)
        verify {
            dao.getById(1)
        }
    }

    @Test
    fun givenNotEmpty_whenGetById_thenResultIsAsExpected() = runTest {
        every { query.executeAsOneOrNull() } returns createFakeMultiCommunityEntity(id = 2)

        val res = sut.getById(2)

        assertNotNull(res)
        assertEquals(2, res.id)
        verify {
            dao.getById(2)
        }
    }

    @Test
    fun whenCreate_thenInteractionsAreAsExpected() = runTest {
        val model = MultiCommunityModel(name = "test", communityIds = listOf(1, 2, 3))
        every { query.executeAsOneOrNull() } returns createFakeMultiCommunityEntity(id = 2)

        val res = sut.create(model = model, accountId = 1)

        assertEquals(2, res)
        verify {
            dao.create(
                name = "test",
                icon = null,
                communityIds = "1,2,3",
                accountId = 1,
            )
        }
    }

    @Test
    fun whenUpdate_thenInteractionsAreAsExpected() = runTest {
        val model =
            MultiCommunityModel(
                name = "test",
                id = 2,
                icon = "fake-icon",
                communityIds = listOf(1, 2, 3),
            )
        every { query.executeAsOneOrNull() } returns createFakeMultiCommunityEntity(id = 2)

        sut.update(model = model)

        verify {
            dao.update(
                id = 2,
                name = "test",
                icon = "fake-icon",
                communityIds = "1,2,3",
            )
        }
    }

    @Test
    fun whenDelete_thenInteractionsAreAsExpected() = runTest {
        val model = MultiCommunityModel(name = "test", id = 2)
        every { query.executeAsOneOrNull() } returns createFakeMultiCommunityEntity(id = 2)

        sut.delete(model)

        verify {
            dao.delete(id = 2)
        }
    }

    private fun createFakeMultiCommunityEntity(
        id: Long,
        name: String = "",
        icon: String? = null,
        communityIds: String = "",
        accountId: Long? = null,
    ) = MultiCommunityEntity(
        id = id,
        name = name,
        icon = icon,
        communityIds = communityIds,
        account_id = accountId,
    )
}
