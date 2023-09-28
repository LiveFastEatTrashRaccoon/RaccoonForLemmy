package com.github.diegoberaldin.raccoonforlemmy.core.commonui.components

import androidx.compose.ui.platform.UriHandler
import cafe.adriel.voyager.navigator.Navigator
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.communitydetail.CommunityDetailScreen
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.userdetail.UserDetailScreen
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.web.WebViewScreen
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.CommunityModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.UserModel

fun handleUrl(
    url: String,
    openExternal: Boolean,
    uriHandler: UriHandler,
    navigator: Navigator? = null,
) {
    val community = extractCommunity(url)
    val user = extractUser(url)
    when {
        community != null -> {
            navigator?.push(
                CommunityDetailScreen(
                    community = community,
                    otherInstance = community.host
                )
            )
        }

        user != null -> {
            navigator?.push(
                UserDetailScreen(
                    user = user,
                    otherInstance = user.host
                )
            )
        }

        openExternal -> {
            uriHandler.openUri(url)
        }

        else -> {
            navigator?.push(WebViewScreen(url))
        }
    }
}

private fun extractCommunity(url: String): CommunityModel? = when {
    url.startsWith("/c/") -> {
        val cleanString = url.substring(3)
        if (url.count { it == '@' } == 1) {
            val (name, host) = cleanString.split("@", limit = 2).let { l -> l[0] to l[1] }
            CommunityModel(
                name = name,
                host = host,
            )
        } else {
            CommunityModel(
                name = cleanString,
            )
        }
    }

    url.startsWith("!") -> {
        val cleanString = url.substring(1)
        if (url.count { it == '@' } == 1) {
            val (name, host) = cleanString.split("@", limit = 2).let { l -> l[0] to l[1] }
            CommunityModel(
                name = name,
                host = host,
            )
        } else {
            CommunityModel(
                name = cleanString,
            )
        }
    }

    else -> null
}

private fun extractUser(url: String): UserModel? = when {
    url.startsWith("/u/") -> {
        val cleanString = url.substring(3)
        if (url.count { it == '@' } == 1) {
            val (name, host) = cleanString.split("@", limit = 2).let { l -> l[0] to l[1] }
            UserModel(
                name = name,
                host = host,
            )
        } else {
            UserModel(
                name = cleanString,
            )
        }
    }

    else -> null
}