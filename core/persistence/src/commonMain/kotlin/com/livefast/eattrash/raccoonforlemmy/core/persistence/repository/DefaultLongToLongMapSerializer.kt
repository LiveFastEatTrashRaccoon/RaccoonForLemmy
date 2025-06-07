package com.livefast.eattrash.raccoonforlemmy.core.persistence.repository

internal class DefaultLongToLongMapSerializer : LongToLongMapSerializer {
    override fun deserializeMap(list: List<String>): MutableMap<Long, Long> = list
        .mapNotNull {
            it.split(":").takeIf { e -> e.size == 2 }?.let { e -> e[0] to e[1] }
        }.let { pairs ->
            val res = mutableMapOf<Long, Long>()
            for (pair in pairs) {
                res[pair.first.toLong()] = pair.second.toLong()
            }
            res
        }

    override fun serializeMap(map: Map<Long, Long>): List<String> = map.map { e ->
        buildString {
            append("")
            append(e.key)
            append(":")
            append(e.value)
        }
    }
}
