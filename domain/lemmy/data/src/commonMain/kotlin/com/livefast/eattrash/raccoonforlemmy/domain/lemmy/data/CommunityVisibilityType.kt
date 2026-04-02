package com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.resources.LocalResources

sealed interface CommunityVisibilityType {
    data object Public : CommunityVisibilityType

    data object LocalOnly : CommunityVisibilityType
}

@Composable
fun CommunityVisibilityType.toIcon(): ImageVector = when (this) {
    CommunityVisibilityType.LocalOnly -> LocalResources.current.cottage
    else -> LocalResources.current.public
}

@Composable
fun CommunityVisibilityType.toReadableName(): String = when (this) {
    CommunityVisibilityType.LocalOnly -> LocalStrings.current.communityVisibilityLocalOnly
    else -> LocalStrings.current.communityVisibilityPublic
}
