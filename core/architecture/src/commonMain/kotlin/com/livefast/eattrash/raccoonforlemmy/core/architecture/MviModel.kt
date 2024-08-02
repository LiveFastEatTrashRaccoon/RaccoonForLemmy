package com.livefast.eattrash.raccoonforlemmy.core.architecture

import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Model contract for Model-View-Intent architecture.
 */
interface MviModel<Intent, State, Effect> {
    /**
     * Representation of the state holder's state for the view to consume.
     */
    val uiState: StateFlow<State>

    /**
     * One-shot events generated by the state holder.
     */
    val effects: SharedFlow<Effect>

    /**
     * Reduce a view intent updating the [uiState] accordingly.
     *
     * @param intent View intent to process
     */
    fun reduce(intent: Intent)
}
