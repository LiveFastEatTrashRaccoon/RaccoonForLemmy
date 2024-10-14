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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.SettingsRow
import com.livefast.eattrash.raccoonforlemmy.core.l10n.messages.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.notifications.NotificationCenterEvent
import com.livefast.eattrash.raccoonforlemmy.core.notifications.di.getNotificationCenter

@Composable
internal fun ProfileMenuContent(
    modifier: Modifier = Modifier,
    isModerator: Boolean = false,
    canCreateCommunity: Boolean = false,
    isBookmarksVisible: Boolean = true,
) {
    val notificationCenter = remember { getNotificationCenter() }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Spacing.m),
    ) {
        SettingsRow(
            title = LocalStrings.current.navigationDrawerTitleSubscriptions,
            icon = Icons.Default.Book,
            onTap = {
                notificationCenter.send(NotificationCenterEvent.ProfileSideMenuAction.ManageSubscriptions)
            },
        )
        if (isBookmarksVisible) {
            SettingsRow(
                title = LocalStrings.current.navigationDrawerTitleBookmarks,
                icon = Icons.Default.Bookmark,
                onTap = {
                    notificationCenter.send(NotificationCenterEvent.ProfileSideMenuAction.Bookmarks)
                },
            )
        }
        SettingsRow(
            title = LocalStrings.current.navigationDrawerTitleDrafts,
            icon = Icons.Default.Drafts,
            onTap = {
                notificationCenter.send(NotificationCenterEvent.ProfileSideMenuAction.Drafts)
            },
        )
        SettingsRow(
            title = LocalStrings.current.profileUpvotesDownvotes,
            icon = Icons.Default.ThumbsUpDown,
            onTap = {
                notificationCenter.send(NotificationCenterEvent.ProfileSideMenuAction.Votes)
            },
        )

        if (isModerator) {
            SettingsRow(
                title = LocalStrings.current.moderatorZoneTitle,
                icon = Icons.Default.Shield,
                onTap = {
                    notificationCenter.send(NotificationCenterEvent.ProfileSideMenuAction.ModeratorZone)
                },
            )
        }
        if (canCreateCommunity) {
            SettingsRow(
                title = LocalStrings.current.actionCreateCommunity,
                icon = Icons.Default.GroupAdd,
                onTap = {
                    notificationCenter.send(NotificationCenterEvent.ProfileSideMenuAction.CreateCommunity)
                },
            )
        }

        SettingsRow(
            title = LocalStrings.current.manageAccountsTitle,
            icon = Icons.Default.ManageAccounts,
            onTap = {
                notificationCenter.send(NotificationCenterEvent.ProfileSideMenuAction.ManageAccounts)
            },
        )

        HorizontalDivider()

        SettingsRow(
            title = LocalStrings.current.actionLogout,
            icon = Icons.AutoMirrored.Default.Logout,
            onTap = {
                notificationCenter.send(NotificationCenterEvent.ProfileSideMenuAction.Logout)
            },
        )
    }
}
