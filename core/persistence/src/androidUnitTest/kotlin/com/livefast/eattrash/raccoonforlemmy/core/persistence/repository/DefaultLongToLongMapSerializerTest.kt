package com.livefast.eattrash.raccoonforlemmy.core.persistence.repository

import kotlin.test.Test
import kotlin.test.assertEquals

class DefaultLongToLongMapSerializerTest {
    private val sut = DefaultLongToLongMapSerializer()

    @Test
    fun givenEmpty_whenSerializeMap_thenResultIsAsExpected() {
        val map = mapOf<Long, Long>()

        val res = sut.serializeMap(map)

        assertEquals(emptyList(), res)
    }

    @Test
    fun whenSerializeMap_thenResultIsAsExpected() {
        val map = mapOf(1L to 2L)

        val res = sut.serializeMap(map)

        assertEquals(listOf("1:2"), res)
    }

    @Test
    fun givenEmpty_whenDeserializeMap_thenResultIsAsExpected() {
        val list = listOf<String>()

        val res = sut.deserializeMap(list)

        assertEquals(mapOf(), res)
    }

    @Test
    fun whenDeserializeMap_thenResultIsAsExpected() {
        val list = listOf("1:2")

        val res = sut.deserializeMap(list)

        assertEquals(mapOf(1L to 2L), res)
    }
}
