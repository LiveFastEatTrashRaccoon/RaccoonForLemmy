package com.livefast.eattrash.raccoonforlemmy.core.navigation

import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

class DefaultNavigationAdapter(
    private val navController: NavController,
    dispatcher: CoroutineDispatcher = Dispatchers.Main,
) : NavigationAdapter {
    override val canPop = MutableStateFlow(false)
    private val scope = CoroutineScope(SupervisorJob() + dispatcher)
    private var job: Job? = null

    init {
        navController.currentBackStack
            .onEach { backStack -> canPop.update { backStack.size > 2 } }
            .launchIn(scope)
    }

    override fun navigate(destination: Destination, replaceTop: Boolean) {
        if (job?.isActive == true) {
            return
        }
        perform {
            if (replaceTop && canPop.value) {
                navController.popBackStack()
            }
            navController.navigate(destination)
        }
    }

    override fun pop() {
        if (job?.isActive == true) {
            return
        }
        perform {
            if (canPop.value) {
                navController.popBackStack()
            }
        }
    }

    override fun popUntilRoot() {
        if (job?.isActive == true) {
            return
        }
        perform {
            navController.popBackStack(route = Destination.Main, inclusive = false)
        }
    }

    private fun perform(interval: Duration = 250.milliseconds, action: () -> Unit) {
        job = scope.launch {
            action()
            delay(interval)
            job = null
        }
    }
}
