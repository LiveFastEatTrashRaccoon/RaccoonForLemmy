package com.github.diegoberaldin.raccoonforlemmy.core.commonui.createpost

sealed interface CreatePostSection {
    data object Edit : CreatePostSection
    data object Preview : CreatePostSection
}