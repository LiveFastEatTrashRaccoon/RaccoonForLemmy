package com.livefast.eattrash.raccoonforlemmy.core.architecture.di

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import org.kodein.di.DI
import org.kodein.di.compose.localDI
import org.kodein.di.instance
import kotlin.reflect.KClass
import kotlin.reflect.cast

/**
 * Tagging interface for all ViewModel creation parameters.
 *
 * These are going to be passed as [CreationExtras] to the [ViewModelProvider.Factory].
 */
interface ViewModelCreationArgs

/**
 * Obtain a reference to the [ViewModel] for a given screen.
 *
 * @param T [ViewModel] which is going to be returned
 * @return an instance of the [ViewModel]
 */
@Composable
inline fun <reified T : ViewModel> getViewModel(arg: ViewModelCreationArgs? = null): T {
    val factory by localDI().instance<ViewModelProvider.Factory>()
    return viewModel(
        factory = factory,
        extras = MutableCreationExtras().apply {
            if (arg != null) {
                set(VM_ARG_KEY, arg)
            }
        },
    )
}

/**
 * [ViewModelProvider.Factory] instance which is used to retrieve [ViewModel] instances though dependency injection.
 *
 * @param injector [DI] reference to the DI instance to use
 */
internal class CustomViewModelFactory(private val injector: DI) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
        val argument = extras[VM_ARG_KEY]
        if (argument != null) {
            val model by injector.instance<ViewModelCreationArgs, ViewModel>(
                tag = modelClass.simpleName,
                arg = argument,
            )
            return modelClass.cast(model)
        }

        val model by injector.instance<ViewModel>(tag = modelClass.simpleName)
        return modelClass.cast(model)
    }
}

/**
 * Key used to store the [ViewModelCreationArgs] in the [CreationExtras] to instantiate the [ViewModel].
 */
val VM_ARG_KEY = object : CreationExtras.Key<ViewModelCreationArgs> {}
