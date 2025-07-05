package com.livefast.eattrash.raccoonforlemmy.unit.licences

import com.livefast.eattrash.raccoonforlemmy.core.architecture.MviModel
import com.livefast.eattrash.raccoonforlemmy.unit.licences.models.LicenceItem

interface LicencesMviModel : MviModel<LicencesMviModel.Intent, LicencesMviModel.State, LicencesMviModel.Effect> {
    sealed interface Intent

    data class State(val items: List<LicenceItem> = emptyList())

    sealed interface Effect
}
