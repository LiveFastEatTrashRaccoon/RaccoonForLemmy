package com.github.diegoberaldin.raccoonforlemmy.feature.profile.menu

import cafe.adriel.voyager.core.model.screenModelScope
import com.github.diegoberaldin.raccoonforlemmy.core.architecture.DefaultMviModel
import com.github.diegoberaldin.raccoonforlemmy.core.navigation.TabNavigationSection
import com.github.diegoberaldin.raccoonforlemmy.core.navigation.toTabNavigationSections
import com.github.diegoberaldin.raccoonforlemmy.core.persistence.repository.SettingsRepository
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.repository.LemmyValueCache
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ProfileSideMenuViewModel(
    private val settingsRepository: SettingsRepository,
    private val lemmyValueCache: LemmyValueCache,
) : DefaultMviModel<ProfileSideMenuMviModel.Intent, ProfileSideMenuMviModel.State, ProfileSideMenuMviModel.Effect>(
        ProfileSideMenuMviModel.State(),
    ),
    ProfileSideMenuMviModel {
    init {
        screenModelScope.launch {
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
