package com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui

sealed interface UserDetailSection {
    data object Posts : UserDetailSection

    data object Comments : UserDetailSection
}
