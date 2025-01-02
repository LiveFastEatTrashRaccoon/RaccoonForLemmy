package com.livefast.eattrash.raccoonforlemmy.core.persistence.repository

import kotlin.test.Test
import kotlin.test.assertEquals

class DefaultSortSerializerTest {
    private val sut = DefaultSortSerializer()

    @Test
    fun givenEmpty_whenSerializeMap_thenResultIsAsExpected() {
        val map = mapOf<String, Int>()

        val res = sut.serializeMap(map)

        assertEquals(emptyList(), res)
    }

    @Test
    fun whenSerializeMap_thenResultIsAsExpected() {
        val map = mapOf("key" to 1)

        val res = sut.serializeMap(map)

        assertEquals(listOf("key:1"), res)
    }

    @Test
    fun givenEmpty_whenDeserializeMap_thenResultIsAsExpected() {
        val list = listOf<String>()

        val res = sut.deserializeMap(list)

        assertEquals(mapOf(), res)
    }

    @Test
    fun whenDeserializeMap_thenResultIsAsExpected() {
        val list = listOf("key:1")

        val res = sut.deserializeMap(list)

        assertEquals(mapOf("key" to 1), res)
    }
}
