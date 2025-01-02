package com.livefast.eattrash.raccoonforlemmy.core.persistence.repository

internal interface SortSerializer {
    fun deserializeMap(list: List<String>): MutableMap<String, Int>

    fun serializeMap(map: Map<String, Int>): List<String>
}
