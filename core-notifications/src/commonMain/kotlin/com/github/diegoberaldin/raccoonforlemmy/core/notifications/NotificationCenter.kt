package com.github.diegoberaldin.raccoonforlemmy.core.notifications

import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.CommentModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.PostModel
import kotlinx.coroutines.flow.SharedFlow

/**
 * Utility to publish and subscribe for broadcast notifications.
 */
interface NotificationCenter {

    /**
     * Available event types.
     */
    sealed interface Event {
        data class PostUpdate(val post: PostModel) : Event
        data class CommentUpdate(val comment: CommentModel) : Event
    }

    /**
     * Observable event flow
     */
    val events: SharedFlow<Event>

    /**
     * Publish and event to subscribers.
     *
     * @param event Event to send
     */
    fun send(event: Event)
}
