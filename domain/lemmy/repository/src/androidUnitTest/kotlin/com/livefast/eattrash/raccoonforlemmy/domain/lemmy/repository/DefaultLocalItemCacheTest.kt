package com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository

import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.PostModel
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DefaultLocalItemCacheTest {
    private val sut = DefaultLocalItemCache<PostModel>()

    @Test
    fun `given item not present when get then result is as expected`() = runTest {
        val res = sut.get(1)
        assertNull(res)
    }

    @Test
    fun `given item present when get then result is as expected`() = runTest {
        val item = PostModel(id = 1)

        sut.put(item.id, item)
        val res = sut.get(1)

        assertEquals(item, res)
    }

    @Test
    fun `when remove then result is as expected`() = runTest {
        val item = PostModel(id = 1)

        sut.put(item.id, item)
        val res1 = sut.get(1)
        sut.remove(1)
        val res2 = sut.get(1)

        assertEquals(item, res1)
        assertNull(res2)
    }

    @Test
    fun `when clear then result is as expected`() = runTest {
        val item = PostModel(id = 1)

        sut.put(item.id, item)
        val res1 = sut.get(1)
        sut.clear()
        val res2 = sut.get(1)

        assertEquals(item, res1)
        assertNull(res2)
    }
}
