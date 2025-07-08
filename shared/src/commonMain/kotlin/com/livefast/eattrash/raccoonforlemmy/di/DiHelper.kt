package com.livefast.eattrash.raccoonforlemmy.di

import com.livefast.eattrash.raccoonforlemmy.core.api.di.apiModule
import com.livefast.eattrash.raccoonforlemmy.core.appearance.di.appearanceModule
import com.livefast.eattrash.raccoonforlemmy.core.architecture.di.viewModelFactoryModule
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.di.lemmyUiModule
import com.livefast.eattrash.raccoonforlemmy.core.di.RootDI
import com.livefast.eattrash.raccoonforlemmy.core.l10n.di.l10nModule
import com.livefast.eattrash.raccoonforlemmy.core.navigation.di.navigationModule
import com.livefast.eattrash.raccoonforlemmy.core.notifications.di.notificationsModule
import com.livefast.eattrash.raccoonforlemmy.core.persistence.di.persistenceModule
import com.livefast.eattrash.raccoonforlemmy.core.preferences.di.preferencesModule
import com.livefast.eattrash.raccoonforlemmy.core.utils.di.utilsModule
import com.livefast.eattrash.raccoonforlemmy.domain.identity.di.identityModule
import com.livefast.eattrash.raccoonforlemmy.domain.inbox.di.inboxModule
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.pagination.di.lemmyPaginationModule
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.di.lemmyRepositoryModule
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.usecase.di.lemmyUseCaseModule
import com.livefast.eattrash.raccoonforlemmy.feature.inbox.di.inboxTabModule
import com.livefast.eattrash.raccoonforlemmy.feature.profile.di.profileTabModule
import com.livefast.eattrash.raccoonforlemmy.feature.settings.di.settingsTabModule
import com.livefast.eattrash.raccoonforlemmy.unit.about.di.aboutModule
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
import com.livefast.eattrash.raccoonforlemmy.unit.explore.di.exploreModule
import com.livefast.eattrash.raccoonforlemmy.unit.filteredcontents.di.filteredContentsModule
import com.livefast.eattrash.raccoonforlemmy.unit.instanceinfo.di.instanceInfoModule
import com.livefast.eattrash.raccoonforlemmy.unit.licences.di.licenceModule
import com.livefast.eattrash.raccoonforlemmy.unit.login.di.loginModule
import com.livefast.eattrash.raccoonforlemmy.unit.manageaccounts.di.manageAccountsModule
import com.livefast.eattrash.raccoonforlemmy.unit.manageban.di.manageBanModule
import com.livefast.eattrash.raccoonforlemmy.unit.managesubscriptions.di.manageSubscriptionsModule
import com.livefast.eattrash.raccoonforlemmy.unit.medialist.di.mediaListModule
import com.livefast.eattrash.raccoonforlemmy.unit.mentions.di.mentionsModule
import com.livefast.eattrash.raccoonforlemmy.unit.messages.di.messagesModule
import com.livefast.eattrash.raccoonforlemmy.unit.moderatewithreason.di.moderateWithReasonModule
import com.livefast.eattrash.raccoonforlemmy.unit.modlog.di.modlogModule
import com.livefast.eattrash.raccoonforlemmy.unit.multicommunity.di.multiCommunityModule
import com.livefast.eattrash.raccoonforlemmy.unit.myaccount.di.myAccountModule
import com.livefast.eattrash.raccoonforlemmy.unit.postdetail.di.postDetailModule
import com.livefast.eattrash.raccoonforlemmy.unit.postlist.di.postListModule
import com.livefast.eattrash.raccoonforlemmy.unit.replies.di.repliesModule
import com.livefast.eattrash.raccoonforlemmy.unit.reportlist.di.reportListModule
import com.livefast.eattrash.raccoonforlemmy.unit.selectcommunity.di.selectCommunityModule
import com.livefast.eattrash.raccoonforlemmy.unit.selectinstance.di.selectInstanceModule
import com.livefast.eattrash.raccoonforlemmy.unit.userdetail.di.userDetailModule
import com.livefast.eattrash.raccoonforlemmy.unit.userinfo.di.userInfoModule
import com.livefast.eattrash.raccoonforlemmy.unit.usertags.di.userTagsModule
import com.livefast.eattrash.raccoonforlemmy.unit.zoomableimage.di.zoomableImageModule
import org.kodein.di.DI

fun initDi(additionalBuilder: DI.Builder.() -> Unit = {}) {
    RootDI.di =
        DI {
            additionalBuilder()

            // core modules
            importAll(
                apiModule,
                appearanceModule,
                l10nModule,
                lemmyUiModule,
                navigationModule,
                notificationsModule,
                persistenceModule,
                preferencesModule,
                utilsModule,
            )

            // domain
            importAll(
                identityModule,
                inboxModule,
                lemmyPaginationModule,
                lemmyRepositoryModule,
                lemmyUseCaseModule,
            )

            // features
            importAll(
                inboxTabModule,
                profileTabModule,
                settingsTabModule,
            )

            // unit
            importAll(
                aboutModule,
                accountSettingsModule,
                acknowledgementsModule,
                banModule,
                chatModule,
                communityDetailModule,
                communityInfoModule,
                configureContentViewModule,
                configureNavBarModule,
                configureSwipeActionsModule,
                createCommentModule,
                createPostModule,
                draftsModule,
                drawerModule,
                editCommunityModule,
                exploreModule,
                filteredContentsModule,
                instanceInfoModule,
                licenceModule,
                loginModule,
                manageAccountsModule,
                manageBanModule,
                manageSubscriptionsModule,
                mediaListModule,
                mentionsModule,
                messagesModule,
                moderateWithReasonModule,
                modlogModule,
                multiCommunityModule,
                myAccountModule,
                postDetailModule,
                postListModule,
                repliesModule,
                reportListModule,
                selectCommunityModule,
                selectInstanceModule,
                userDetailModule,
                userInfoModule,
                userTagsModule,
                zoomableImageModule,
            )

            // shared
            importAll(
                mainRouterModule,
                mainModule,
                sharedResourcesModule,
                viewModelFactoryModule,
            )
        }
}
