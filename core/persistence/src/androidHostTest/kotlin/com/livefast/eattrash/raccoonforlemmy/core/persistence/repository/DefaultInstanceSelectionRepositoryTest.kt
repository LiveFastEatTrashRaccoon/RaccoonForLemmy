package com.livefast.eattrash.raccoonforlemmy.core.persistence.repository

import com.livefast.eattrash.raccoonforlemmy.core.preferences.store.TemporaryKeyStore
import com.livefast.eattrash.raccoonforlemmy.core.testutils.DispatcherTestRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DefaultInstanceSelectionRepositoryTest {
    @get:Rule
    val dispatcherTestRule = DispatcherTestRule()

    private val keyStore =
        mockk<TemporaryKeyStore>(relaxUnitFun = true) {
            coEvery { containsKey("customInstances") } returns true
        }

    private val sut = DefaultInstanceSelectionRepository(keyStore)

    @Test
    fun givenEmpty_whenGetAll_thenResultIsAsExpected() = runTest {
        coEvery { keyStore.get("customInstances", any<List<String>>()) } returns listOf()

        val res = sut.getAll()

        assertTrue(res.isEmpty())
    }

    @Test
    fun givenNotEmpty_whenGetAll_thenResultIsAsExpected() = runTest {
        coEvery { keyStore.get("customInstances", any<List<String>>()) } returns listOf("lemmy.world")

        val res = sut.getAll()

        assertTrue(res.isNotEmpty())
        assertEquals("lemmy.world", res.first())
    }

    @Test
    fun whenUpdateAll_thenResultIsAsExpected() = runTest {
        val values = listOf("lemmy.world", "lemmy.ml")
        sut.updateAll(values)

        coVerify {
            keyStore.save("customInstances", values)
        }
    }

    @Test
    fun givenNotAlreadyPresent_whenAdd_thenResultIsAsExpected() = runTest {
        coEvery { keyStore.get("customInstances", any<List<String>>()) } returns listOf("lemmy.world")

        sut.add("lemmy.ml")

        coVerify {
            keyStore.save("customInstances", listOf("lemmy.ml", "lemmy.world"))
        }
    }

    @Test
    fun givenAlreadyPresent_whenAdd_thenResultIsAsExpected() = runTest {
        coEvery { keyStore.get("customInstances", any<List<String>>()) } returns listOf("lemmy.world")

        sut.add("lemmy.world")

        coVerify {
            keyStore.save("customInstances", listOf("lemmy.world"))
        }
    }

    @Test
    fun whenRemove_thenResultIsAsExpected() = runTest {
        coEvery { keyStore.get("customInstances", any<List<String>>()) } returns listOf("lemmy.world", "lemmy.ml")

        sut.remove("lemmy.ml")

        coVerify {
            keyStore.save("customInstances", listOf("lemmy.world"))
        }
    }
}
