package com.github.diegoberaldin.raccoonforlemmy.core.markdown

internal object SpoilerRegex {
    val spoilerOpening = Regex("(:::\\s?spoiler\\s+)(?<title>.*)")
    val spoilerClosing = Regex(":::")
}

internal object LemmyLinkRegex {
    private const val DETAIL_FRAGMENT: String = "[a-zA-Z0-9_]{3,}"

    private const val INSTANCE_FRAGMENT: String =
        "([a-zA-Z0-9][a-zA-Z0-9-]{0,61}[a-zA-Z0-9]\\.)+[a-zA-Z]{2,}"

    val handle: Regex =
        Regex("(?<=\\s)!(?<detail>$DETAIL_FRAGMENT)(?:@(?<instance>$INSTANCE_FRAGMENT))?\\s")
}

internal object LemmyMentionRegex {
    private const val DETAIL_FRAGMENT: String = "[a-zA-Z0-9_]{3,}"

    private const val INSTANCE_FRAGMENT: String =
        "([a-zA-Z0-9][a-zA-Z0-9-]{0,61}[a-zA-Z0-9]\\.)+[a-zA-Z]{2,}"

    val mention: Regex =
        Regex("(?<=\\s)@(?<detail>$DETAIL_FRAGMENT)@(?<instance>$INSTANCE_FRAGMENT)\\b")
}

internal object ImageRegex {
    val image = Regex("!\\[[^]]*]\\((?<url>.*?)\\)")
    val imageNotAfter2Newlines = Regex("(?<before>[^\n])\n(?<image>!\\[[^]]*]\\(.*?\\))")
    val imageAddNewLineAfter = Regex("(?<image>!\\[[^]]*]\\(.*?\\))(?<newline>\n?)(?<after>.+)")
}
