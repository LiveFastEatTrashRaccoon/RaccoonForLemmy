package com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui

sealed interface CreatePostSection {
    data object Edit : CreatePostSection

    data object Preview : CreatePostSection
}
