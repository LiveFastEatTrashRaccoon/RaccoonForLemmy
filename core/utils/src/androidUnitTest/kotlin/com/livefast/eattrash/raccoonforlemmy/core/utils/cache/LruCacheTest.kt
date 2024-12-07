package com.livefast.eattrash.raccoonforlemmy.core.utils.cache

import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class LruCacheTest {
    private val sut = LruCache<String, Any>(CAPACITY)

    @Test
    fun `given ley not in cache when get then result is as expected`() =
        runTest {
            val key = "key"

            val res = sut.containsKey(key)

            assertFalse(res)
        }

    @Test
    fun `given key in cache when get then result is as expected`() =
        runTest {
            val key = "key"
            sut.put(key, "value")

            val res = sut.containsKey(key)

            assertTrue(res)
        }

    @Test
    fun `given value not in cache when get then result is as expected`() =
        runTest {
            val key = "key"

            val res = sut.get(key)

            assertNull(res)
        }

    @Test
    fun `given value in cache when get then result is as expected`() =
        runTest {
            val key = "key"
            val value = "value"
            sut.put(key, value)

            val res = sut.get(key)

            assertNotNull(res)
            assertEquals(value, res)
        }

    @Test
    fun `when clear then result is as expected`() =
        runTest {
            val key = "key"
            sut.put(key, "value")

            sut.clear()
            val res = sut.get(key)

            assertNull(res)
        }

    @Test
    fun `when remove then result is as expected`() =
        runTest {
            val key = "key"
            sut.put(key, "value")

            sut.remove(key)
            val res = sut.get(key)

            assertNull(res)
        }

    @Test
    fun `given empty when remove then result is as expected`() =
        runTest {
            val key = "key"

            sut.remove(key)
            val res = sut.get(key)

            assertNull(res)
        }

    @Test
    fun `given capacity exceeded when put then oldest items are discarded`() =
        runTest {
            repeat(CAPACITY + 1) { idx ->
                val key = "key$idx"
                sut.put(key, "value$idx")
            }

            val res0 = sut.get("key0")
            assertNull(res0)
            val res1 = sut.get("key1")
            assertNotNull(res1)
        }

    @Test
    fun `given capacity exceeded when put then last referenced items are discarded`() =
        runTest {
            repeat(CAPACITY) { idx ->
                val key = "key$idx"
                sut.put(key, "value$idx")
            }
            // reference first item
            sut.get("key0")
            // put additional item afterwards
            sut.put("key11", "value11")

            val res0 = sut.get("key0")
            assertNotNull(res0)
            val res1 = sut.get("key1")
            assertNull(res1)
        }

    companion object {
        private const val CAPACITY = 10
    }
}
