package com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.resources.LocalResources

sealed interface ModeratorZoneAction {
    data object GlobalModLog : ModeratorZoneAction

    data object GlobalReports : ModeratorZoneAction

    data object ModeratedContents : ModeratorZoneAction
}

@Composable
fun ModeratorZoneAction.toReadableName(): String = when (this) {
    ModeratorZoneAction.GlobalModLog -> LocalStrings.current.modlogTitle
    ModeratorZoneAction.GlobalReports -> LocalStrings.current.reportListTitle
    ModeratorZoneAction.ModeratedContents -> LocalStrings.current.moderatorZoneActionContents
}

@Composable
fun ModeratorZoneAction.toIcon(): ImageVector = when (this) {
    ModeratorZoneAction.GlobalModLog -> LocalResources.current.listAlt
    ModeratorZoneAction.GlobalReports -> LocalResources.current.report
    ModeratorZoneAction.ModeratedContents -> LocalResources.current.shield
}
