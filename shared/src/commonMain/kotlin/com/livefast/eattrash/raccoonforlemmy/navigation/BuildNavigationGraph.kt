package com.livefast.eattrash.raccoonforlemmy.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.livefast.eattrash.raccoonforlemmy.core.navigation.Destination
import com.livefast.eattrash.raccoonforlemmy.feature.settings.advanced.AdvancedSettingsScreen
import com.livefast.eattrash.raccoonforlemmy.feature.settings.colors.SettingsColorAndFontScreen
import com.livefast.eattrash.raccoonforlemmy.feature.settings.main.SettingsScreen
import com.livefast.eattrash.raccoonforlemmy.main.MainScreen
import com.livefast.eattrash.raccoonforlemmy.unit.accountsettings.AccountSettingsScreen
import com.livefast.eattrash.raccoonforlemmy.unit.acknowledgements.main.AcknowledgementsScreen
import com.livefast.eattrash.raccoonforlemmy.unit.ban.BanUserScreen
import com.livefast.eattrash.raccoonforlemmy.unit.chat.InboxChatScreen
import com.livefast.eattrash.raccoonforlemmy.unit.communitydetail.CommunityDetailScreen
import com.livefast.eattrash.raccoonforlemmy.unit.communityinfo.CommunityInfoScreen
import com.livefast.eattrash.raccoonforlemmy.unit.configurecontentview.ConfigureContentViewScreen
import com.livefast.eattrash.raccoonforlemmy.unit.configurenavbar.ConfigureNavBarScreen
import com.livefast.eattrash.raccoonforlemmy.unit.configureswipeactions.ConfigureSwipeActionsScreen
import com.livefast.eattrash.raccoonforlemmy.unit.createcomment.CreateCommentScreen
import com.livefast.eattrash.raccoonforlemmy.unit.createpost.CreatePostScreen
import com.livefast.eattrash.raccoonforlemmy.unit.drafts.DraftsScreen
import com.livefast.eattrash.raccoonforlemmy.unit.editcommunity.EditCommunityScreen
import com.livefast.eattrash.raccoonforlemmy.unit.explore.ExploreScreen
import com.livefast.eattrash.raccoonforlemmy.unit.filteredcontents.FilteredContentsScreen
import com.livefast.eattrash.raccoonforlemmy.unit.instanceinfo.InstanceInfoScreen
import com.livefast.eattrash.raccoonforlemmy.unit.licences.LicencesScreen
import com.livefast.eattrash.raccoonforlemmy.unit.login.LoginScreen
import com.livefast.eattrash.raccoonforlemmy.unit.manageban.ManageBanScreen
import com.livefast.eattrash.raccoonforlemmy.unit.managesubscriptions.ManageSubscriptionsScreen
import com.livefast.eattrash.raccoonforlemmy.unit.medialist.MediaListScreen
import com.livefast.eattrash.raccoonforlemmy.unit.moderatewithreason.ModerateWithReasonScreen
import com.livefast.eattrash.raccoonforlemmy.unit.modlog.ModlogScreen
import com.livefast.eattrash.raccoonforlemmy.unit.multicommunity.detail.MultiCommunityScreen
import com.livefast.eattrash.raccoonforlemmy.unit.multicommunity.editor.MultiCommunityEditorScreen
import com.livefast.eattrash.raccoonforlemmy.unit.postdetail.PostDetailScreen
import com.livefast.eattrash.raccoonforlemmy.unit.reportlist.ReportListScreen
import com.livefast.eattrash.raccoonforlemmy.unit.userdetail.UserDetailScreen
import com.livefast.eattrash.raccoonforlemmy.unit.usertags.detail.UserTagDetailScreen
import com.livefast.eattrash.raccoonforlemmy.unit.usertags.list.UserTagsScreen
import com.livefast.eattrash.raccoonforlemmy.unit.web.WebViewScreen
import com.livefast.eattrash.raccoonforlemmy.unit.zoomableimage.ZoomableImageScreen

internal fun NavGraphBuilder.buildNavigationGraph() {
    composable<Destination.AccountSettings> {
        AccountSettingsScreen()
    }
    composable<Destination.Acknowledgements> {
        AcknowledgementsScreen()
    }
    composable<Destination.AdvancedSettings> {
        AdvancedSettingsScreen()
    }
    composable<Destination.BanUser> {
        val route: Destination.BanUser = it.toRoute()
        BanUserScreen(
            userId = route.userId,
            communityId = route.communityId,
            newValue = route.newValue,
            postId = route.postId,
            commentId = route.commentId,
        )
    }
    composable<Destination.Chat> {
        val route: Destination.Chat = it.toRoute()
        InboxChatScreen(otherUserId = route.otherUserId)
    }
    composable<Destination.ColorAndFont> {
        SettingsColorAndFontScreen()
    }
    composable<Destination.CommunityDetail> {
        val route: Destination.CommunityDetail = it.toRoute()
        CommunityDetailScreen(
            communityId = route.id,
            otherInstance = route.otherInstance,
        )
    }
    composable<Destination.CommunityInfo> {
        val route: Destination.CommunityInfo = it.toRoute()
        CommunityInfoScreen(
            communityId = route.id,
            communityName = route.name,
            otherInstance = route.otherInstance,
        )
    }
    composable<Destination.ConfigureContentView> {
        ConfigureContentViewScreen()
    }
    composable<Destination.ConfigureNavBar> {
        ConfigureNavBarScreen()
    }
    composable<Destination.ConfigureSwipeActions> {
        ConfigureSwipeActionsScreen()
    }
    composable<Destination.CreateComment> {
        val route: Destination.CreateComment = it.toRoute()
        CreateCommentScreen(
            draftId = route.draftId,
            originalPostId = route.originalPostId,
            originalCommentId = route.originalCommentId,
            editedCommentId = route.editedCommentId,
            initialText = route.initialText,
        )
    }
    composable<Destination.CreatePost> {
        val route: Destination.CreatePost = it.toRoute()
        CreatePostScreen(
            draftId = route.draftId,
            communityId = route.communityId,
            editedPostId = route.editedPostId,
            crossPostId = route.crossPostId,
            initialText = route.initialText,
            initialTitle = route.initialTitle,
            initialUrl = route.initialUrl,
            initialNsfw = route.initialNsfw,
            forceCommunitySelection = route.forceCommunitySelection,
        )
    }
    composable<Destination.Drafts> {
        DraftsScreen()
    }
    composable<Destination.EditCommunity> {
        val route: Destination.EditCommunity = it.toRoute()
        EditCommunityScreen(communityId = route.id)
    }
    composable<Destination.Explore> {
        val route: Destination.Explore = it.toRoute()
        ExploreScreen(
            otherInstance = route.otherInstance,
        )
    }
    composable<Destination.FilteredContents> {
        val route: Destination.FilteredContents = it.toRoute()
        FilteredContentsScreen(type = route.type)
    }
    composable<Destination.InstanceInfo> {
        val route: Destination.InstanceInfo = it.toRoute()
        InstanceInfoScreen(url = route.url)
    }
    composable<Destination.Licences> {
        LicencesScreen()
    }
    composable<Destination.Login> {
        LoginScreen()
    }
    composable<Destination.Main> {
        MainScreen()
    }
    composable<Destination.ManageBans> {
        ManageBanScreen()
    }
    composable<Destination.ManageSubscriptions> {
        ManageSubscriptionsScreen()
    }
    composable<Destination.MediaList> {
        MediaListScreen()
    }
    composable<Destination.ModerateWithReason> {
        val route: Destination.ModerateWithReason = it.toRoute()
        ModerateWithReasonScreen(
            actionId = route.actionId,
            contentId = route.contentId,
        )
    }
    composable<Destination.Modlog> {
        val route: Destination.Modlog = it.toRoute()
        ModlogScreen(communityId = route.communityId)
    }
    composable<Destination.MultiCommunity> {
        val route: Destination.MultiCommunity = it.toRoute()
        MultiCommunityScreen(communityId = route.id)
    }
    composable<Destination.MultiCommunityEditor> {
        val route: Destination.MultiCommunityEditor = it.toRoute()
        MultiCommunityEditorScreen(communityId = route.id)
    }
    composable<Destination.PostDetail> {
        val route: Destination.PostDetail = it.toRoute()
        PostDetailScreen(
            postId = route.id,
            otherInstance = route.otherInstance,
            highlightCommentId = route.highlightCommentId,
            isMod = route.isMod,
        )
    }
    composable<Destination.ReportList> {
        val route: Destination.ReportList = it.toRoute()
        ReportListScreen(communityId = route.communityId)
    }
    composable<Destination.Settings> {
        SettingsScreen()
    }
    composable<Destination.UserDetail> {
        val route: Destination.UserDetail = it.toRoute()
        UserDetailScreen(
            userId = route.id,
            otherInstance = route.otherInstance,
        )
    }
    composable<Destination.UserTagDetail> {
        val route: Destination.UserTagDetail = it.toRoute()
        UserTagDetailScreen(id = route.id)
    }
    composable<Destination.UserTags> {
        UserTagsScreen()
    }
    composable<Destination.WebInternal> {
        val route: Destination.WebInternal = it.toRoute()
        WebViewScreen(url = route.url)
    }
    composable<Destination.ZoomableImage> {
        val route: Destination.ZoomableImage = it.toRoute()
        ZoomableImageScreen(
            url = route.url,
            source = route.source,
            isVideo = route.isVideo,
        )
    }
}
