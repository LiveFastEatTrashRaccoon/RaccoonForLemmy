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

class DefaultUserSortRepositoryTest {
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
        DefaultUserSortRepository(
            keyStore = keyStore,
            serializer = serializer,
        )

    // region Posts
    @Test
    fun givenEmptyInitialState_whenSaveForPosts_thenValueIsStored() =
        runTest {
            every { keyStore.get(KEY_POSTS, listOf()) } returns listOf()
            sut.saveForPosts(HANDLE, 1)

            verify {
                keyStore.save(KEY_POSTS, listOf("$HANDLE:1"))
            }
        }

    @Test
    fun givenUserAlreadyExisting_whenSaveForPosts_thenValueIsStored() =
        runTest {
            every {
                keyStore.get(
                    KEY_POSTS,
                    listOf(),
                )
            } returns listOf("$HANDLE:0")

            sut.saveForPosts(HANDLE, 1)

            verify {
                keyStore.save(KEY_POSTS, listOf("$HANDLE:1"))
            }
        }

    @Test
    fun givenOtherUserAlreadyExisting_whenSaveForPosts_thenBothValuesAreStored() =
        runTest {
            every { keyStore.get(KEY_POSTS, listOf()) } returns listOf("test@lemmy.world:1")

            sut.saveForPosts(HANDLE, 1)

            verify {
                keyStore.save(
                    KEY_POSTS,
                    listOf("test@lemmy.world:1", "$HANDLE:1"),
                )
            }
        }

    @Test
    fun givenEmptyInitialState_whenGetForPosts_thenResultIsAsExpected() =
        runTest {
            every { keyStore.get(KEY_POSTS, listOf()) } returns listOf()

            val res = sut.getForPosts(HANDLE)

            assertNull(res)
        }

    @Test
    fun givenUserExists_whenGetForPosts_thenResultIsAsExpected() =
        runTest {
            every {
                keyStore.get(
                    KEY_POSTS,
                    listOf(),
                )
            } returns listOf("$HANDLE:2")

            val res = sut.getForPosts(HANDLE)

            assertEquals(2, res)
        }

    @Test
    fun givenUserDoesNotExist_whenGetForPosts_thenResultIsAsExpected() =
        runTest {
            every { keyStore.get(KEY_POSTS, listOf()) } returns listOf("test@lemmy.world:2")

            val res = sut.getForPosts(HANDLE)

            assertNull(res)
        }
    // endregion

    // region Comments
    @Test
    fun givenEmptyInitialState_whenSaveForComments_thenValueIsStored() =
        runTest {
            every { keyStore.get(KEY_COMMENTS, listOf()) } returns listOf()
            sut.saveForComments(HANDLE, 1)

            verify {
                keyStore.save(KEY_COMMENTS, listOf("$HANDLE:1"))
            }
        }

    @Test
    fun givenUserAlreadyExisting_whenSaveForComments_thenValueIsStored() =
        runTest {
            every {
                keyStore.get(
                    KEY_COMMENTS,
                    listOf(),
                )
            } returns listOf("$HANDLE:0")

            sut.saveForComments(HANDLE, 1)

            verify {
                keyStore.save(KEY_COMMENTS, listOf("$HANDLE:1"))
            }
        }

    @Test
    fun givenOtherUserAlreadyExisting_whenSaveForComments_thenBothValuesAreStored() =
        runTest {
            every { keyStore.get(KEY_COMMENTS, listOf()) } returns listOf("test@lemmy.world:1")

            sut.saveForComments(HANDLE, 1)

            verify {
                keyStore.save(
                    KEY_COMMENTS,
                    listOf("test@lemmy.world:1", "$HANDLE:1"),
                )
            }
        }

    @Test
    fun givenEmptyInitialState_whenGetForComments_thenResultIsAsExpected() =
        runTest {
            every { keyStore.get(KEY_COMMENTS, listOf()) } returns listOf()

            val res = sut.getForComments(HANDLE)

            assertNull(res)
        }

    @Test
    fun givenUserExists_whenGetForComments_thenResultIsAsExpected() =
        runTest {
            every {
                keyStore.get(
                    KEY_COMMENTS,
                    listOf(),
                )
            } returns listOf("$HANDLE:2")

            val res = sut.getForComments(HANDLE)

            assertEquals(2, res)
        }

    @Test
    fun givenUserDoesNotExist_whenGetForComments_thenResultIsAsExpected() =
        runTest {
            every { keyStore.get(KEY_COMMENTS, listOf()) } returns listOf("test@lemmy.world:2")

            val res = sut.getForComments(HANDLE)

            assertNull(res)
        }
    // endregion

    companion object {
        private const val KEY_POSTS = "userPostSort"
        private const val KEY_COMMENTS = "userCommentSort"
        private const val HANDLE = "testuser@lemmy.world"
    }
}
