package com.github.diegoberaldin.raccoonforlemmy.core.commonui.drawer

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow

class DefaultDrawerCoordinator : DrawerCoordinator {

    override val gesturesEnabled = MutableStateFlow(true)
    override val toggleEvents = MutableSharedFlow<DrawerEvent>()

    override suspend fun toggleDrawer() {
        toggleEvents.emit(DrawerEvent.Toggled)
    }

    override suspend fun sendEvent(event: DrawerEvent) {
        toggleEvents.emit(event)
    }

    override fun setGesturesEnabled(value: Boolean) {
        gesturesEnabled.value = value
    }
}
