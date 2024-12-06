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
import com.livefast.eattrash.raccoonforlemmy.feature.home.di.homeTabModule
import com.livefast.eattrash.raccoonforlemmy.feature.inbox.di.inboxTabModule
import com.livefast.eattrash.raccoonforlemmy.feature.profile.di.profileTabModule
import com.livefast.eattrash.raccoonforlemmy.feature.search.di.searchTabModule
import com.livefast.eattrash.raccoonforlemmy.feature.settings.di.settingsTabModule
import com.livefast.eattrash.raccoonforlemmy.unit.accountsettings.di.accountSettingsModule
import com.livefast.eattrash.raccoonforlemmy.unit.acknowledgements.di.acknowledgementsModule
import com.livefast.eattrash.raccoonforlemmy.unit.ban.di.banModule
import com.livefast.eattrash.raccoonforlemmy.unit.chat.di.chatModule
import com.livefast.eattrash.raccoonforlemmy.unit.communitydetail.di.communityDetailModule
import com.livefast.eattrash.raccoonforlemmy.unit.communityinfo.di.communityInfoModule
import com.livefast.eattrash.raccoonforlemmy.unit.configurecontentview.di.configureContentViewModule
import com.livefast.eattrash.raccoonforlemmy.unit.configurenavbar.di.configureNavBarModule
import com.livefast.eattrash.raccoonforlemmy.unit.configureswipeactions.di.configureSwipeActionsModule
import com.livefast.eattrash.raccoonforlemmy.unit.createcomment.di.createCommentModule
import com.livefast.eattrash.raccoonforlemmy.unit.createpost.di.createPostModule
import com.livefast.eattrash.raccoonforlemmy.unit.drafts.di.draftsModule
import com.livefast.eattrash.raccoonforlemmy.unit.drawer.di.drawerModule
import com.livefast.eattrash.raccoonforlemmy.unit.editcommunity.di.editCommunityModule
import com.livefast.eattrash.raccoonforlemmy.unit.filteredcontents.di.filteredContentsModule
import com.livefast.eattrash.raccoonforlemmy.unit.instanceinfo.di.instanceInfoModule
import com.livefast.eattrash.raccoonforlemmy.unit.licences.di.licenceModule
import com.livefast.eattrash.raccoonforlemmy.unit.manageban.di.manageBanModule
import com.livefast.eattrash.raccoonforlemmy.unit.managesubscriptions.di.manageSubscriptionsModule
import com.livefast.eattrash.raccoonforlemmy.unit.medialist.di.mediaListModule
import com.livefast.eattrash.raccoonforlemmy.unit.moderatewithreason.di.moderateWithReasonModule
import com.livefast.eattrash.raccoonforlemmy.unit.modlog.di.modlogModule
import com.livefast.eattrash.raccoonforlemmy.unit.postdetail.di.postDetailModule
import com.livefast.eattrash.raccoonforlemmy.unit.reportlist.di.reportListModule
import com.livefast.eattrash.raccoonforlemmy.unit.selectcommunity.di.selectCommunityModule
import com.livefast.eattrash.raccoonforlemmy.unit.selectinstance.di.selectInstanceModule
import com.livefast.eattrash.raccoonforlemmy.unit.userdetail.di.userDetailModule
import com.livefast.eattrash.raccoonforlemmy.unit.userinfo.di.userInfoModule
import com.livefast.eattrash.raccoonforlemmy.unit.zoomableimage.di.zoomableImageModule
import org.koin.core.annotation.Module
import org.koin.dsl.module
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

val rootModule =
    module {
        includes(
            SharedModule().module,
            CoreModules().module,
            DomainModules().module,
            // feature modules
            homeTabModule,
            inboxTabModule,
            profileTabModule,
            searchTabModule,
            settingsTabModule,
            // unit modules
            banModule,
            zoomableImageModule,
            chatModule,
            selectCommunityModule,
            drawerModule,
            communityInfoModule,
            instanceInfoModule,
            reportListModule,
            createPostModule,
            createCommentModule,
            postDetailModule,
            communityDetailModule,
            userDetailModule,
            userInfoModule,
            manageSubscriptionsModule,
            modlogModule,
            accountSettingsModule,
            manageBanModule,
            selectInstanceModule,
            configureSwipeActionsModule,
            configureContentViewModule,
            draftsModule,
            filteredContentsModule,
            editCommunityModule,
            licenceModule,
            moderateWithReasonModule,
            acknowledgementsModule,
            mediaListModule,
            configureNavBarModule,
        )
    }
