package com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui

import androidx.compose.runtime.Composable
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings

sealed interface BlockActionType {
    data object User : BlockActionType

    data object Community : BlockActionType

    data object Instance : BlockActionType
}

fun Int.toBlockActionType(): BlockActionType =
    when (this) {
        2 -> BlockActionType.Instance
        1 -> BlockActionType.Community
        else -> BlockActionType.User
    }

@Composable
fun BlockActionType.toReadableName(): String =
    when (this) {
        BlockActionType.Community -> LocalStrings.current.blockActionCommunity
        BlockActionType.Instance -> LocalStrings.current.communityDetailBlockInstance
        BlockActionType.User -> LocalStrings.current.blockActionUser
    }
