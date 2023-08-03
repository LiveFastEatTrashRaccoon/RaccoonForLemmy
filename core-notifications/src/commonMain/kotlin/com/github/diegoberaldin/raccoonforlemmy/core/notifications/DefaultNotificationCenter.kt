package com.github.diegoberaldin.raccoonforlemmy.core.notifications

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

object DefaultNotificationCenter : NotificationCenter {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    override val events = MutableSharedFlow<NotificationCenter.Event>()

    override fun send(event: NotificationCenter.Event) {
        scope.launch {
            events.emit(event)
        }
    }
}
