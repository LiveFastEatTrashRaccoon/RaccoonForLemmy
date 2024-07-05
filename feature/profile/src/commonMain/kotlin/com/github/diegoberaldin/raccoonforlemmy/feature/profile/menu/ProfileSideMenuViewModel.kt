package com.github.diegoberaldin.raccoonforlemmy.feature.profile.menu

import cafe.adriel.voyager.core.model.screenModelScope
import com.github.diegoberaldin.raccoonforlemmy.core.architecture.DefaultMviModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.repository.LemmyValueCache
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ProfileSideMenuViewModel(
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
        }
    }
}
