package com.livefast.eattrash.raccoonforlemmy.feature.profile.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.SettingsRow
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.resources.LocalResources

@Composable
internal fun ProfileMenuContent(
    onManageAccounts: () -> Unit,
    onManageSubscriptions: () -> Unit,
    onOpenBookmarks: () -> Unit,
    onOpenDrafts: () -> Unit,
    onOpenVotes: () -> Unit,
    onModeratorZone: () -> Unit,
    onCreateCommunity: () -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
    isModerator: Boolean = false,
    canCreateCommunity: Boolean = false,
    isBookmarksVisible: Boolean = true,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Spacing.s),
    ) {
        SettingsRow(
            title = LocalStrings.current.navigationDrawerTitleSubscriptions,
            icon = LocalResources.current.book,
            onTap = onManageSubscriptions,
        )
        if (isBookmarksVisible) {
            SettingsRow(
                title = LocalStrings.current.navigationDrawerTitleBookmarks,
                icon = LocalResources.current.bookmarkFill,
                onTap = onOpenBookmarks,
            )
        }
        SettingsRow(
            title = LocalStrings.current.navigationDrawerTitleDrafts,
            icon = LocalResources.current.stylusFountainPenFill,
            onTap = onOpenDrafts,
        )
        SettingsRow(
            title = LocalStrings.current.profileUpvotesDownvotes,
            icon = LocalResources.current.thumbsUpDownFill,
            onTap = onOpenVotes,
        )

        if (isModerator) {
            SettingsRow(
                title = LocalStrings.current.moderatorZoneTitle,
                icon = LocalResources.current.shield,
                onTap = onModeratorZone,
            )
        }
        if (canCreateCommunity) {
            SettingsRow(
                title = LocalStrings.current.actionCreateCommunity,
                icon = LocalResources.current.addCircle,
                onTap = onCreateCommunity,
            )
        }

        SettingsRow(
            title = LocalStrings.current.manageAccountsTitle,
            icon = LocalResources.current.changeCircle,
            onTap = onManageAccounts,
        )

        HorizontalDivider()

        SettingsRow(
            title = LocalStrings.current.actionLogout,
            icon = LocalResources.current.logout,
            onTap = onLogout,
        )
    }
}
