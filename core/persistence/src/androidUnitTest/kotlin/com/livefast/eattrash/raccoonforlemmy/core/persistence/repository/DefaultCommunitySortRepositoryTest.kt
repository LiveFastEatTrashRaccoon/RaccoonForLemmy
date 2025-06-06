package com.livefast.eattrash.raccoonforlemmy.core.persistence.repository

import com.livefast.eattrash.raccoonforlemmy.core.preferences.store.TemporaryKeyStore
import com.livefast.eattrash.raccoonforlemmy.core.testutils.DispatcherTestRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertEquals

class DefaultCommunitySortRepositoryTest {
    @get:Rule
    val dispatcherTestRule = DispatcherTestRule()

    private val keyStore = mockk<TemporaryKeyStore>(relaxUnitFun = true)
    private val serializer =
        mockk<SortSerializer> {
            every { deserializeMap(any()) } answers {
                firstArg<List<String>>()
                    .associate { s ->
                        val tokens = s.split(":").map { it.trim() }
                        tokens[0] to tokens[1].toInt()
                    }.toMutableMap()
            }
            every { serializeMap(any()) } answers {
                firstArg<Map<String, Int>>()
                    .map { entry ->
                        "${entry.key}:${entry.value}"
                    }.toList()
            }
        }

    private val sut =
        DefaultCommunitySortRepository(
            keyStore = keyStore,
            serializer = serializer,
        )

    @Test
    fun givenEmptyInitialState_whenSave_thenValueIsStored() =
        runTest {
            coEvery { keyStore.get(KEY, listOf()) } returns listOf()
            sut.save(HANDLE, 1)

            coVerify {
                keyStore.save(KEY, listOf("$HANDLE:1"))
            }
        }

    @Test
    fun givenCommunityAlreadyExisting_whenSave_thenValueIsStored() =
        runTest {
            coEvery { keyStore.get(KEY, listOf()) } returns listOf("$HANDLE:0")

            sut.save(HANDLE, 1)

            coVerify {
                keyStore.save(KEY, listOf("$HANDLE:1"))
            }
        }

    @Test
    fun givenOtherCommunityAlreadyExisting_whenSave_thenBothValuesAreStored() =
        runTest {
            coEvery { keyStore.get(KEY, listOf()) } returns listOf("!test@lemmy.world:1")

            sut.save(HANDLE, 1)

            coEvery {
                keyStore.save(KEY, listOf("!test@lemmy.world:1", "$HANDLE:1"))
            }
        }

    @Test
    fun givenEmptyInitialState_whenGet_thenResultIsAsExpected() =
        runTest {
            coEvery { keyStore.get(KEY, listOf()) } returns listOf()

            val res = sut.get(HANDLE)

            assertNull(res)
        }

    @Test
    fun givenCommunityExists_whenGet_thenResultIsAsExpected() =
        runTest {
            coEvery { keyStore.get(KEY, listOf()) } returns listOf("$HANDLE:2")

            val res = sut.get(HANDLE)

            assertEquals(2, res)
        }

    @Test
    fun givenCommunityDoesNotExist_whenGet_thenResultIsAsExpected() =
        runTest {
            coEvery { keyStore.get(KEY, listOf()) } returns listOf("!test@lemmy.world:2")

            val res = sut.get(HANDLE)

            assertNull(res)
        }

    companion object {
        private const val KEY = "communitySort"
        private const val HANDLE = "!raccoonforlemmy@lemmy.world"
    }
}
