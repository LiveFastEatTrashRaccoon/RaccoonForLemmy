package com.livefast.eattrash.raccoonforlemmy.domain.identity.urlhandler

import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.CommunityModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.PostModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.UserModel

internal class DefaultUrlDecoder : UrlDecoder {
    override fun getCommunity(url: String?): CommunityModel? {
        val (normalizedUrl, instance) = normalizeUrl(url)
        val res = extractCommunity(normalizedUrl)
        return if (res != null && res.host.isEmpty()) {
            res.copy(host = instance)
        } else {
            res
        }
    }

    override fun getUser(url: String?): UserModel? {
        val (normalizedUrl, instance) = normalizeUrl(url)
        val res = extractUser(normalizedUrl)
        return if (res != null && res.host.isEmpty()) {
            res.copy(host = instance)
        } else {
            res
        }
    }

    override fun getPost(url: String?): Pair<PostModel?, String?> {
        val (normalizedUrl, instance) = normalizeUrl(url)
        val post = extractPost(normalizedUrl)
        return if (post != null && instance.isNotEmpty()) {
            post to instance
        } else {
            null to null
        }
    }

    private fun normalizeUrl(url: String?): Pair<String, String> {
        val matches = Regex("https?://(?<instance>.*?)(?<pathAndQuery>/.*)").findAll(url.orEmpty())
        var instance = ""
        val res =
            buildString {
                if (matches.count() > 0) {
                    val match = matches.iterator().next()
                    val value = match.groups["pathAndQuery"]?.value.orEmpty()
                    instance = match.groups["instance"]?.value.orEmpty()
                    if (value.isNotEmpty()) {
                        append(value)
                    } else {
                        append(url)
                    }
                } else {
                    append(url)
                }
            }
        return res to instance
    }

    private fun extractCommunity(url: String): CommunityModel? =
        when {
            url.startsWith("/c/") -> {
                val cleanString = url.substring(3)
                if (cleanString.count { it == '@' } == 1) {
                    val (name, host) = cleanString.split("@", limit = 2).let { l -> l[0] to l[1] }
                    CommunityModel(
                        name = name,
                        host = host,
                    )
                } else {
                    CommunityModel(name = cleanString)
                }
            }

            url.startsWith("!") -> {
                val cleanString = url.substring(1)
                if (cleanString.count { it == '@' } == 1) {
                    val (name, host) = cleanString.split("@", limit = 2).let { l -> l[0] to l[1] }
                    CommunityModel(
                        name = name,
                        host = host,
                    )
                } else {
                    CommunityModel(name = cleanString)
                }
            }

            else -> null
        }

    private fun extractUser(url: String): UserModel? =
        when {
            url.startsWith("@") -> {
                val cleanString = url.substring(1)
                if (cleanString.count { it == '@' } == 1) {
                    val (name, host) = cleanString.split("@", limit = 2).let { l -> l[0] to l[1] }
                    UserModel(
                        name = name,
                        host = host,
                    )
                } else {
                    UserModel(name = cleanString)
                }
            }

            url.startsWith("/u/") -> {
                val cleanString = url.substring(3)
                if (cleanString.count { it == '@' } == 1) {
                    val (name, host) = cleanString.split("@", limit = 2).let { l -> l[0] to l[1] }
                    UserModel(
                        name = name,
                        host = host,
                    )
                } else {
                    UserModel(name = cleanString)
                }
            }

            else -> null
        }

    private fun extractPost(url: String): PostModel? {
        val regex = Regex("/post/(?<postId>.*$)")
        val match = regex.find(url)
        val id =
            match
                ?.let { it.groups["postId"]?.value.orEmpty() }
                ?.toLongOrNull()
        return if (id != null) {
            PostModel(id = id)
        } else {
            null
        }
    }
}
