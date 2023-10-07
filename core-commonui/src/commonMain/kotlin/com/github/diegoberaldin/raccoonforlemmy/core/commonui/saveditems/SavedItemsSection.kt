package com.github.diegoberaldin.raccoonforlemmy.core.commonui.saveditems

sealed interface SavedItemsSection {
    data object Posts : SavedItemsSection

    data object Comments : SavedItemsSection
}
