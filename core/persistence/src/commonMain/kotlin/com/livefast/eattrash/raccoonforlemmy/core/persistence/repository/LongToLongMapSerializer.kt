package com.livefast.eattrash.raccoonforlemmy.core.persistence.repository

internal interface LongToLongMapSerializer {
    fun deserializeMap(list: List<String>): MutableMap<Long, Long>

    fun serializeMap(map: Map<Long, Long>): List<String>
}
