package com.livefast.eattrash.raccoonforlemmy.feature.profile.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Drafts
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.ThumbsUpDown
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.SettingsRow
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings

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
            icon = Icons.Default.Book,
            onTap = onManageSubscriptions,
        )
        if (isBookmarksVisible) {
            SettingsRow(
                title = LocalStrings.current.navigationDrawerTitleBookmarks,
                icon = Icons.Default.Bookmark,
                onTap = onOpenBookmarks,
            )
        }
        SettingsRow(
            title = LocalStrings.current.navigationDrawerTitleDrafts,
            icon = Icons.Default.Drafts,
            onTap = onOpenDrafts,
        )
        SettingsRow(
            title = LocalStrings.current.profileUpvotesDownvotes,
            icon = Icons.Default.ThumbsUpDown,
            onTap = onOpenVotes,
        )

        if (isModerator) {
            SettingsRow(
                title = LocalStrings.current.moderatorZoneTitle,
                icon = Icons.Default.Shield,
                onTap = onModeratorZone,
            )
        }
        if (canCreateCommunity) {
            SettingsRow(
                title = LocalStrings.current.actionCreateCommunity,
                icon = Icons.Default.GroupAdd,
                onTap = onCreateCommunity,
            )
        }

        SettingsRow(
            title = LocalStrings.current.manageAccountsTitle,
            icon = Icons.Default.ManageAccounts,
            onTap = onManageAccounts,
        )

        HorizontalDivider()

        SettingsRow(
            title = LocalStrings.current.actionLogout,
            icon = Icons.AutoMirrored.Default.Logout,
            onTap = onLogout,
        )
    }
}
