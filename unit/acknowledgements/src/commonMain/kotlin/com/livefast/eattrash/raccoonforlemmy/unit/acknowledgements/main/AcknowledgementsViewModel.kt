package com.livefast.eattrash.raccoonforlemmy.unit.acknowledgements.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.livefast.eattrash.raccoonforlemmy.core.architecture.DefaultMviModelDelegate
import com.livefast.eattrash.raccoonforlemmy.core.architecture.MviModelDelegate
import com.livefast.eattrash.raccoonforlemmy.unit.acknowledgements.repository.AcknowledgementsRepository
import kotlinx.coroutines.launch

internal class AcknowledgementsViewModel(private val acknowledgementsRepository: AcknowledgementsRepository) :
    ViewModel(),
    MviModelDelegate<AcknowledgementsMviModel.Intent, AcknowledgementsMviModel.State, AcknowledgementsMviModel.Effect>
    by DefaultMviModelDelegate(initialState = AcknowledgementsMviModel.State()),
    AcknowledgementsMviModel {
    init {
        viewModelScope.launch {
            refresh(initial = true)
        }
    }

    override fun reduce(intent: AcknowledgementsMviModel.Intent) {
        when (intent) {
            AcknowledgementsMviModel.Intent.Refresh ->
                viewModelScope.launch {
                    refresh()
                }
        }
    }

    private suspend fun refresh(initial: Boolean = false) {
        updateState {
            it.copy(
                initial = initial,
                refreshing = !initial,
            )
        }
        val items = acknowledgementsRepository.getAcknowledgements()
        updateState {
            it.copy(
                items = items.orEmpty(),
                initial = false,
                refreshing = false,
            )
        }
    }
}
