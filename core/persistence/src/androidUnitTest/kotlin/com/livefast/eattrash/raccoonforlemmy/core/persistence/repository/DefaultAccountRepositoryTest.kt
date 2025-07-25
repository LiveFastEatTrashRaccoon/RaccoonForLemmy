package com.livefast.eattrash.raccoonforlemmy.core.persistence.repository

import app.cash.sqldelight.Query
import app.cash.turbine.test
import com.livefast.eattrash.raccoonforlemmy.core.persistence.AccountEntity
import com.livefast.eattrash.raccoonforlemmy.core.persistence.dao.AccountDao
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.AccountModel
import com.livefast.eattrash.raccoonforlemmy.core.testutils.DispatcherTestRule
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class DefaultAccountRepositoryTest {
    @get:Rule
    val dispatcherRule = DispatcherTestRule()

    private val query = mockk<Query<AccountEntity>>()
    private val dao =
        mockk<AccountDao>(relaxUnitFun = true) {
            every { getActive() } returns query
            every { getAll() } returns query
            every { getBy(any(), any()) } returns query
        }

    private val sut = DefaultAccountRepository(dao)

    @Test
    fun givenNoAccounts_whenGetAll_thenResultIsAsExpected() = runTest {
        every { query.executeAsList() } returns listOf()

        val res = sut.getAll()

        assertTrue(res.isEmpty())
    }

    @Test
    fun givenExitingAccounts_whenGetAll_thenResultIsAsExpected() = runTest {
        val accounts = listOf(createFakeEntity())
        every { query.executeAsList() } returns accounts

        val res = sut.getAll()

        assertTrue(res.size == 1)
    }

    @Test
    fun givenExitingAccounts_whenObserveAll_thenResultIsAsExpected() = runTest {
        val accounts = listOf(createFakeEntity())
        every { dao.observeAll() } returns flowOf(accounts)

        sut.observeAll().test {
            val res = awaitItem()
            assertTrue(res.size == 1)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun givenNoActiveAccount_whenGetActive_thenResultIsAsExpected() = runTest {
        every { query.executeAsOneOrNull() } returns null

        val res = sut.getActive()

        assertNull(res)
    }

    @Test
    fun givenActiveAccount_whenGetActive_thenResultIsAsExpected() = runTest {
        val account = createFakeEntity(active = true)
        every { query.executeAsOneOrNull() } returns account

        val res = sut.getActive()

        assertNotNull(res)
    }

    @Test
    fun givenNoAccount_whenGetBy_thenResultIsAsExpected() = runTest {
        val username = "username"
        val instance = "instance"
        every { query.executeAsOneOrNull() } returns null

        val res = sut.getBy(username, instance)

        assertNull(res)
    }

    @Test
    fun givenAccount_whenGetBy_thenResultIsAsExpected() = runTest {
        val username = "username"
        val instance = "instance"
        val account = createFakeEntity(active = true, username = username, instance = instance)
        every { query.executeAsOneOrNull() } returns account

        val res = sut.getBy(username, instance)

        assertNotNull(res)
    }

    @Test
    fun whenCreate_thenIdIsReturned() = runTest {
        val username = "username"
        val instance = "instance"
        val jwt = "jwt"
        val account = AccountModel(username = username, instance = instance, jwt = jwt)
        every { query.executeAsList() } returns listOf(createFakeEntity(id = 1, jwt = jwt))

        val id = sut.createAccount(account)

        assertEquals(1, id)
        verify {
            dao.create(
                username = username,
                instance = instance,
                jwt = jwt,
                avatar = null,
            )
        }
    }

    @Test
    fun whenSetActive_thenResultIsAsExpected() = runTest {
        sut.setActive(id = 1, active = true)

        verify {
            dao.setActive(1)
        }
    }

    @Test
    fun whenSetInactive_thenInteractionsAreAsExpected() = runTest {
        sut.setActive(id = 1, active = false)

        verify {
            dao.setInactive(1)
        }
    }

    @Test
    fun whenUpdate_thenInteractionsAreAsExpected() = runTest {
        val jwt = "jwt"
        val avatar = "avatar"
        sut.update(1, avatar = avatar, jwt = jwt)

        verify {
            dao.update(
                jwt = jwt,
                avatar = avatar,
                id = 1,
            )
        }
    }

    @Test
    fun whenDelete_thenInteractionsAreAsExpected() = runTest {
        sut.delete(1)

        verify {
            dao.delete(1)
        }
    }

    private fun createFakeEntity(
        id: Long = 0,
        active: Boolean = false,
        username: String = "",
        instance: String = "",
        jwt: String? = null,
        avatar: String? = null,
    ) = AccountEntity(
        id = id,
        active = if (active) 1 else 0,
        username = username,
        instance = instance,
        jwt = jwt,
        avatar = avatar,
    )
}
