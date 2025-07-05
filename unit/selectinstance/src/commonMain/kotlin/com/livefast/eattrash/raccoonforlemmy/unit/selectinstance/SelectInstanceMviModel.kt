package com.livefast.eattrash.raccoonforlemmy.unit.selectinstance

import com.livefast.eattrash.raccoonforlemmy.core.architecture.MviModel
import com.livefast.eattrash.raccoonforlemmy.core.utils.ValidationError

interface SelectInstanceMviModel :
    MviModel<SelectInstanceMviModel.Intent, SelectInstanceMviModel.State, SelectInstanceMviModel.Effect> {
    sealed interface Intent {
        data class SelectInstance(val value: String) : Intent

        data class DeleteInstance(val value: String) : Intent

        data class ChangeInstanceName(val value: String) : Intent

        data object HapticIndication : Intent

        data class SwapIntances(val from: Int, val to: Int) : Intent

        data object SubmitChangeInstanceDialog : Intent
    }

    data class State(
        val instances: List<String> = emptyList(),
        val currentInstance: String = "",
        val changeInstanceName: String = "",
        val changeInstanceNameError: ValidationError? = null,
        val changeInstanceLoading: Boolean = false,
    )

    sealed interface Effect {
        data object CloseDialog : Effect

        data class Confirm(val instance: String) : Effect
    }
}
