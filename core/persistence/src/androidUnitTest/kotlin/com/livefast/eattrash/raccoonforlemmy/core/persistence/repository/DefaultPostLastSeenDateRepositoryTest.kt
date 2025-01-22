package com.livefast.eattrash.raccoonforlemmy.core.persistence.repository

import com.livefast.eattrash.raccoonforlemmy.core.preferences.store.TemporaryKeyStore
import com.livefast.eattrash.raccoonforlemmy.core.testutils.DispatcherTestRule
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertEquals

class DefaultPostLastSeenDateRepositoryTest {
    @get:Rule
    val dispatcherTestRule = DispatcherTestRule()

    private val keyStore = mockk<TemporaryKeyStore>(relaxUnitFun = true)
    private val serializer =
        mockk<LongToLongMapSerializer> {
            every { deserializeMap(any()) } answers {
                firstArg<List<String>>()
                    .associate { s ->
                        val tokens = s.split(":").map { it.trim() }
                        tokens[0].toLong() to tokens[1].toLong()
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
        DefaultPostLastSeenDateRepository(
            keyStore = keyStore,
            serializer = serializer,
        )

    @Test
    fun givenEmptyInitialState_whenSave_thenValueIsStored() =
        runTest {
            every { keyStore.get(KEY, listOf()) } returns listOf()
            sut.save(1L, 2L)

            verify {
                keyStore.save(KEY, listOf("1:2"))
            }
        }

    @Test
    fun givenEntryAlreadyExisting_whenSave_thenValueIsStored() =
        runTest {
            every { keyStore.get(KEY, listOf()) } returns listOf("1:2")

            sut.save(1, 3)

            verify {
                keyStore.save(KEY, listOf("1:3"))
            }
        }

    @Test
    fun givenOtherEntryAlreadyExisting_whenSave_thenBothValuesAreStored() =
        runTest {
            every { keyStore.get(KEY, listOf()) } returns listOf("1:2")

            sut.save(3, 4)

            verify {
                keyStore.save(KEY, listOf("1:2", "3:4"))
            }
        }

    @Test
    fun givenEmptyInitialState_whenGet_thenResultIsAsExpected() =
        runTest {
            every { keyStore.get(KEY, listOf()) } returns listOf()

            val res = sut.get(1L)

            assertNull(res)
        }

    @Test
    fun givenCommunityExists_whenGet_thenResultIsAsExpected() =
        runTest {
            every { keyStore.get(KEY, listOf()) } returns listOf("1:2")

            val res = sut.get(1L)

            assertEquals(2, res)
        }

    @Test
    fun givenCommunityDoesNotExist_whenGet_thenResultIsAsExpected() =
        runTest {
            every { keyStore.get(KEY, listOf()) } returns listOf("1:2")

            val res = sut.get(3L)

            assertNull(res)
        }

    companion object {
        private const val KEY = "postLastSeenDate"
    }
}
