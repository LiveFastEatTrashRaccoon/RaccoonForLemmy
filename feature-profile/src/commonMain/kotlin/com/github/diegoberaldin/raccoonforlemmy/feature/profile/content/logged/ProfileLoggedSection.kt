package com.github.diegoberaldin.raccoonforlemmy.feature.profile.content.logged

sealed interface ProfileLoggedSection {
    data object Posts : ProfileLoggedSection
    data object Comments : ProfileLoggedSection
}
