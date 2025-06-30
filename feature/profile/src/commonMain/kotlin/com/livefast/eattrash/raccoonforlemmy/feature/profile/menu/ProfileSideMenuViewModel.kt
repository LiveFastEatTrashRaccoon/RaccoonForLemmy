package com.livefast.eattrash.raccoonforlemmy.feature.profile.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.livefast.eattrash.raccoonforlemmy.core.architecture.DefaultMviModelDelegate
import com.livefast.eattrash.raccoonforlemmy.core.architecture.MviModelDelegate
import com.livefast.eattrash.raccoonforlemmy.core.navigation.TabNavigationSection
import com.livefast.eattrash.raccoonforlemmy.core.navigation.toTabNavigationSections
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.SettingsRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.LemmyValueCache
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ProfileSideMenuViewModel(
    private val settingsRepository: SettingsRepository,
    private val lemmyValueCache: LemmyValueCache,
) : ViewModel(),
    MviModelDelegate<ProfileSideMenuMviModel.Intent, ProfileSideMenuMviModel.State, ProfileSideMenuMviModel.Effect>
    by DefaultMviModelDelegate(ProfileSideMenuMviModel.State()),
    ProfileSideMenuMviModel {
    init {
        viewModelScope.launch {
            lemmyValueCache.isCurrentUserModerator
                .onEach { isModerator ->
                    updateState {
                        it.copy(
                            isModerator = isModerator,
                        )
                    }
                }.launchIn(this)
            combine(
                lemmyValueCache.isCurrentUserAdmin,
                lemmyValueCache.isCommunityCreationAdminOnly,
            ) { isAdmin, isCommunityCreationAdminOnly ->
                updateState {
                    it.copy(
                        canCreateCommunity =
                        if (isCommunityCreationAdminOnly) {
                            isAdmin
                        } else {
                            true
                        },
                    )
                }
            }.launchIn(this)
            settingsRepository.currentBottomBarSections
                .onEach { sectionIds ->
                    val isBookmarksInBottomBar =
                        sectionIds
                            .toTabNavigationSections()
                            .contains(TabNavigationSection.Bookmarks)
                    updateState {
                        it.copy(isBookmarksVisible = !isBookmarksInBottomBar)
                    }
                }.launchIn(this)
        }
    }
}
