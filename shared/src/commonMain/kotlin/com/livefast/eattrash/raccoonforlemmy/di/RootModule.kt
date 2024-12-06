package com.livefast.eattrash.raccoonforlemmy.di

import com.livefast.eattrash.raccoonforlemmy.core.api.di.ApiModule
import com.livefast.eattrash.raccoonforlemmy.core.appearance.di.AppearanceModule
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.di.LemmyUiModule
import com.livefast.eattrash.raccoonforlemmy.core.l10n.di.L10nModule
import com.livefast.eattrash.raccoonforlemmy.core.navigation.di.NavigationModule
import com.livefast.eattrash.raccoonforlemmy.core.notifications.di.NotificationsModule
import com.livefast.eattrash.raccoonforlemmy.core.persistence.di.PersistenceModule
import com.livefast.eattrash.raccoonforlemmy.core.preferences.di.PreferencesModule
import com.livefast.eattrash.raccoonforlemmy.core.utils.di.UtilsModule
import com.livefast.eattrash.raccoonforlemmy.domain.identity.di.IdentityModule
import com.livefast.eattrash.raccoonforlemmy.domain.inbox.di.InboxModule
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.pagination.di.LemmyPaginationModule
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.di.LemmyRepositoryModule
import com.livefast.eattrash.raccoonforlemmy.feature.inbox.di.InboxTabModule
import com.livefast.eattrash.raccoonforlemmy.feature.profile.di.ProfileTabModule
import com.livefast.eattrash.raccoonforlemmy.feature.settings.di.SettingsTabModule
import com.livefast.eattrash.raccoonforlemmy.unit.about.di.AboutModule
import com.livefast.eattrash.raccoonforlemmy.unit.accountsettings.di.AccountSettingsModule
import com.livefast.eattrash.raccoonforlemmy.unit.acknowledgements.di.AcknowledgementsModule
import com.livefast.eattrash.raccoonforlemmy.unit.ban.di.BanModule
import com.livefast.eattrash.raccoonforlemmy.unit.chat.di.ChatModule
import com.livefast.eattrash.raccoonforlemmy.unit.communitydetail.di.CommunityDetailModule
import com.livefast.eattrash.raccoonforlemmy.unit.communityinfo.di.CommunityInfoModule
import com.livefast.eattrash.raccoonforlemmy.unit.configurecontentview.di.ConfigureContentViewModule
import com.livefast.eattrash.raccoonforlemmy.unit.configurenavbar.di.ConfigureNavBarModule
import com.livefast.eattrash.raccoonforlemmy.unit.configureswipeactions.di.ConfigureSwipeActionsModule
import com.livefast.eattrash.raccoonforlemmy.unit.createcomment.di.CreateCommentModule
import com.livefast.eattrash.raccoonforlemmy.unit.createpost.di.CreatePostModule
import com.livefast.eattrash.raccoonforlemmy.unit.drafts.di.DraftsModule
import com.livefast.eattrash.raccoonforlemmy.unit.drawer.di.DrawerModule
import com.livefast.eattrash.raccoonforlemmy.unit.editcommunity.di.EditCommunityModule
import com.livefast.eattrash.raccoonforlemmy.unit.explore.di.ExploreModule
import com.livefast.eattrash.raccoonforlemmy.unit.filteredcontents.di.FilteredContentsModule
import com.livefast.eattrash.raccoonforlemmy.unit.instanceinfo.di.InstanceInfoModule
import com.livefast.eattrash.raccoonforlemmy.unit.licences.di.LicenceModule
import com.livefast.eattrash.raccoonforlemmy.unit.login.di.LoginModule
import com.livefast.eattrash.raccoonforlemmy.unit.manageaccounts.di.ManageAccountsModule
import com.livefast.eattrash.raccoonforlemmy.unit.manageban.di.ManageBanModule
import com.livefast.eattrash.raccoonforlemmy.unit.managesubscriptions.di.ManageSubscriptionsModule
import com.livefast.eattrash.raccoonforlemmy.unit.medialist.di.MediaListModule
import com.livefast.eattrash.raccoonforlemmy.unit.mentions.di.MentionsModule
import com.livefast.eattrash.raccoonforlemmy.unit.messages.di.MessagesModule
import com.livefast.eattrash.raccoonforlemmy.unit.moderatewithreason.di.ModerateWithReasonModule
import com.livefast.eattrash.raccoonforlemmy.unit.modlog.di.ModlogModule
import com.livefast.eattrash.raccoonforlemmy.unit.myaccount.di.MyAccountModule
import com.livefast.eattrash.raccoonforlemmy.unit.postdetail.di.PostDetailModule
import com.livefast.eattrash.raccoonforlemmy.unit.postlist.di.PostListModule
import com.livefast.eattrash.raccoonforlemmy.unit.replies.di.RepliesModule
import com.livefast.eattrash.raccoonforlemmy.unit.reportlist.di.ReportListModule
import com.livefast.eattrash.raccoonforlemmy.unit.selectcommunity.di.SelectCommunityModule
import com.livefast.eattrash.raccoonforlemmy.unit.selectinstance.di.SelectInstanceModule
import com.livefast.eattrash.raccoonforlemmy.unit.userdetail.di.UserDetailModule
import com.livefast.eattrash.raccoonforlemmy.unit.userinfo.di.UserInfoModule
import com.livefast.eattrash.raccoonforlemmy.unit.zoomableimage.di.ZoomableImageModule
import org.koin.core.annotation.Module
import org.koin.ksp.generated.module

@Module(
    includes = [
        ApiModule::class,
        AppearanceModule::class,
        L10nModule::class,
        LemmyUiModule::class,
        NavigationModule::class,
        NotificationsModule::class,
        PersistenceModule::class,
        PreferencesModule::class,
        UtilsModule::class,
    ],
)
internal class CoreModules

@Module(
    includes = [
        IdentityModule::class,
        InboxModule::class,
        LemmyPaginationModule::class,
        LemmyRepositoryModule::class,
    ],
)
internal class DomainModules

@Module(
    includes = [
        AboutModule::class,
        AccountSettingsModule::class,
        AcknowledgementsModule::class,
        BanModule::class,
        ChatModule::class,
        CommunityDetailModule::class,
        CommunityInfoModule::class,
        ConfigureContentViewModule::class,
        ConfigureNavBarModule::class,
        ConfigureSwipeActionsModule::class,
        CreateCommentModule::class,
        CreatePostModule::class,
        DraftsModule::class,
        DrawerModule::class,
        EditCommunityModule::class,
        ExploreModule::class,
        FilteredContentsModule::class,
        InboxTabModule::class,
        InstanceInfoModule::class,
        LicenceModule::class,
        LoginModule::class,
        ManageAccountsModule::class,
        ManageBanModule::class,
        ManageSubscriptionsModule::class,
        MediaListModule::class,
        MentionsModule::class,
        MessagesModule::class,
        ModerateWithReasonModule::class,
        ModlogModule::class,
        MyAccountModule::class,
        PostDetailModule::class,
        PostListModule::class,
        ProfileTabModule::class,
        RepliesModule::class,
        ReportListModule::class,
        SelectCommunityModule::class,
        SelectInstanceModule::class,
        SettingsTabModule::class,
        UserDetailModule::class,
        UserInfoModule::class,
        ZoomableImageModule::class,
    ],
)
internal class FeatureModules

@Module(
    includes = [
        SharedModule::class,
        CoreModules::class,
        DomainModules::class,
        FeatureModules::class,
    ],
)
internal class RootModule

val rootModule = RootModule().module
