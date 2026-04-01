package com.livefast.eattrash.raccoonforlemmy.unit.licences.models

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.livefast.eattrash.raccoonforlemmy.core.resources.LocalResources

sealed interface LicenceItemType {
    data object Library : LicenceItemType

    data object Resource : LicenceItemType
}

@Composable
internal fun LicenceItemType.toIcon(): ImageVector = when (this) {
    LicenceItemType.Library -> LocalResources.current.api
    LicenceItemType.Resource -> LocalResources.current.palette
}
