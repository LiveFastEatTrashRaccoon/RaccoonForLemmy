package com.github.diegoberaldin.raccoonforlemmy.feature.search.managesubscriptions

import cafe.adriel.voyager.core.model.ScreenModel
import com.github.diegoberaldin.raccoonforlemmy.core.architecture.DefaultMviModel
import com.github.diegoberaldin.raccoonforlemmy.core.architecture.MviModel
import com.github.diegoberaldin.raccoonforlemmy.core.utils.HapticFeedback
import com.github.diegoberaldin.raccoonforlemmy.domain.identity.repository.IdentityRepository
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.CommunityModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.repository.CommunityRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch

class ManageSubscriptionsViewModel(
    private val mvi: DefaultMviModel<ManageSubscriptionsMviModel.Intent, ManageSubscriptionsMviModel.UiState, ManageSubscriptionsMviModel.Effect>,
    private val identityRepository: IdentityRepository,
    private val communityRepository: CommunityRepository,
    private val hapticFeedback: HapticFeedback,
) : ScreenModel,
    MviModel<ManageSubscriptionsMviModel.Intent, ManageSubscriptionsMviModel.UiState, ManageSubscriptionsMviModel.Effect> by mvi {

    override fun onStarted() {
        mvi.onStarted()
        if (uiState.value.communities.isEmpty()) {
            refresh()
        }
    }

    override fun reduce(intent: ManageSubscriptionsMviModel.Intent) {
        when (intent) {
            ManageSubscriptionsMviModel.Intent.HapticIndication -> hapticFeedback.vibrate()
            ManageSubscriptionsMviModel.Intent.Refresh -> refresh()
            is ManageSubscriptionsMviModel.Intent.Unsubscribe -> handleUnsubscription(
                community = uiState.value.communities[intent.index]
            )
        }
    }

    private fun refresh() {
        if (uiState.value.refreshing) {
            return
        }
        mvi.updateState { it.copy(refreshing = true) }
        mvi.scope?.launch(Dispatchers.IO) {
            val auth = identityRepository.authToken.value
            val items = communityRepository.getSubscribed(auth).sortedBy { it.name }
            mvi.updateState {
                it.copy(
                    refreshing = false,
                    communities = items,
                )
            }
        }
    }

    private fun handleUnsubscription(community: CommunityModel) {
        mvi.scope?.launch {
            val auth = identityRepository.authToken.value
            communityRepository.unsubscribe(
                auth = auth, id = community.id
            )
            mvi.updateState {
                it.copy(communities = it.communities.filter { c -> c.id != community.id })
            }
        }
    }
}