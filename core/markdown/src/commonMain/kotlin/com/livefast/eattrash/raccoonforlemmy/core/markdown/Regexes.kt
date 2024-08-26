package com.livefast.eattrash.raccoonforlemmy.core.markdown

internal object SpoilerRegex {
    val spoilerOpening = Regex("(:{3,}\\s?spoiler\\s+)(?<title>.*)")
    val spoilerClosing = Regex(":{3,}")
    val spoilerFull = Regex(":{3,}\\s?spoiler\\s.*(?=\\n)(.|\\n)+?(:{3,}\\v|\\Z)")
}

internal object LemmyLinkRegex {
    private const val DETAIL_FRAGMENT: String = "[a-zA-Z0-9_]{3,}"

    private const val INSTANCE_FRAGMENT: String =
        "([a-zA-Z0-9][a-zA-Z0-9-]{0,61}[a-zA-Z0-9]\\.)+[a-zA-Z]{2,}"

    val handle: Regex =
        Regex("!(?<detail>$DETAIL_FRAGMENT)(?:@(?<instance>$INSTANCE_FRAGMENT))?")

    val mention: Regex =
        Regex("@(?<detail>$DETAIL_FRAGMENT)@(?<instance>$INSTANCE_FRAGMENT)")
}

internal object ImageRegex {
    val image = Regex("!\\[[^]]*]\\((?<url>.*?)\\)")
    val imageNotAfter2Newlines = Regex("(?<before>[^\n])\n(?<image>!\\[[^]]*]\\(.*?\\))")
    val imageAddNewLineAfter = Regex("(?<image>!\\[[^]]*]\\(.*?\\))(?<newline>\n?)(?<after>.+)")
}
