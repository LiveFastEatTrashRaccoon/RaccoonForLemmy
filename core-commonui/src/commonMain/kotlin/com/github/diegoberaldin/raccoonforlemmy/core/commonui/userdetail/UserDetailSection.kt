package com.github.diegoberaldin.raccoonforlemmy.core.commonui.userdetail

sealed interface UserDetailSection {
    data object Posts : UserDetailSection
    data object Comments : UserDetailSection
}
