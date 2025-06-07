package com.livefast.eattrash.raccoonforlemmy.core.persistence.repository

internal class DefaultSortSerializer : SortSerializer {
    override fun deserializeMap(list: List<String>): MutableMap<String, Int> = list
        .mapNotNull {
            it.split(":").takeIf { e -> e.size == 2 }?.let { e -> e[0] to e[1] }
        }.let { pairs ->
            val res = mutableMapOf<String, Int>()
            for (pair in pairs) {
                res[pair.first] = pair.second.toInt()
            }
            res
        }

    override fun serializeMap(map: Map<String, Int>): List<String> = map.map { e ->
        buildString {
            append(e.key)
            append(":")
            append(e.value)
        }
    }
}
