package com.livefast.eattrash.raccoonforlemmy.di

import com.livefast.eattrash.raccoonforlemmy.core.api.di.coreApiModule
import com.livefast.eattrash.raccoonforlemmy.core.appearance.di.coreAppearanceModule
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.di.lemmyUiModule
import com.livefast.eattrash.raccoonforlemmy.core.l10n.di.coreL10nModule
import com.livefast.eattrash.raccoonforlemmy.core.navigation.di.coreNavigationModule
import com.livefast.eattrash.raccoonforlemmy.core.notifications.di.coreNotificationModule
import com.livefast.eattrash.raccoonforlemmy.core.persistence.di.corePersistenceModule
import com.livefast.eattrash.raccoonforlemmy.core.preferences.di.coreAppConfigModule
import com.livefast.eattrash.raccoonforlemmy.core.preferences.di.corePreferencesModule
import com.livefast.eattrash.raccoonforlemmy.core.utils.di.appIconModule
import com.livefast.eattrash.raccoonforlemmy.core.utils.di.appInfoModule
import com.livefast.eattrash.raccoonforlemmy.core.utils.di.crashReportModule
import com.livefast.eattrash.raccoonforlemmy.core.utils.di.customTabsModule
import com.livefast.eattrash.raccoonforlemmy.core.utils.di.fileSystemModule
import com.livefast.eattrash.raccoonforlemmy.core.utils.di.galleryHelperModule
import com.livefast.eattrash.raccoonforlemmy.core.utils.di.hapticFeedbackModule
import com.livefast.eattrash.raccoonforlemmy.core.utils.di.networkModule
import com.livefast.eattrash.raccoonforlemmy.core.utils.di.shareHelperModule
import com.livefast.eattrash.raccoonforlemmy.core.utils.di.utilsModule
import com.livefast.eattrash.raccoonforlemmy.domain.identity.di.coreIdentityModule
import com.livefast.eattrash.raccoonforlemmy.domain.inbox.di.domainInboxModule
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.pagination.di.paginationModule
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.di.repositoryModule
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
import org.koin.dsl.module

val sharedHelperModule =
    module {
        includes(
            internalSharedModule,
            coreAppearanceModule,
            corePreferencesModule,
            coreAppConfigModule,
            coreApiModule,
            coreIdentityModule,
            coreL10nModule,
            coreNotificationModule,
            corePersistenceModule,
            hapticFeedbackModule,
            shareHelperModule,
            galleryHelperModule,
            crashReportModule,
            repositoryModule,
            utilsModule,
            domainInboxModule,
            networkModule,
            coreNavigationModule,
            lemmyUiModule,
            homeTabModule,
            inboxTabModule,
            profileTabModule,
            searchTabModule,
            settingsTabModule,
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
            appIconModule,
            fileSystemModule,
            coreResourceModule,
            paginationModule,
            customTabsModule,
            moderateWithReasonModule,
            acknowledgementsModule,
            mediaListModule,
            configureNavBarModule,
            appInfoModule,
        )
    }
