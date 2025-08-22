package com.livefast.eattrash.raccoonforlemmy.core.architecture.di

import androidx.lifecycle.ViewModel
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.bindings.BindingDI
import org.kodein.di.bindings.NoArgBindingDI
import org.kodein.di.factory
import org.kodein.di.provider
import kotlin.reflect.cast

/**
 * Utility to be used in module definition to define a binding for a [ViewModel] without arguments.
 *
 * @param T [ViewModel] type
 * @param overrides whether this bind must or must not override an existing binding.
 * @param block a block which returns the instance to bind
 */
inline fun <reified T : ViewModel> DI.Builder.bindViewModel(
    overrides: Boolean? = null,
    noinline block: NoArgBindingDI<*>.() -> T,
) {
    bind<T>(
        tag = T::class.diKey,
        overrides = overrides,
    ) {
        provider<Any, T> {
            block()
        }
    }
}

/**
 * Utility to be used in module definition to define a binding for a [ViewModel] with arguments.
 *
 * @param T [ViewModel] type
 * @param A [ViewModelCreationArgs] type for the arguments
 * @param overrides whether this bind must or must not override an existing binding.
 * @param block a block which returns the instance to bind taking in some parameters [A]
 */
inline fun <reified A : ViewModelCreationArgs, reified T : ViewModel> DI.Builder.bindViewModelWithArgs(
    overrides: Boolean? = null,
    noinline block: BindingDI<*>.(A) -> T,
) {
    bind<T>(
        tag = T::class.diKey,
        overrides = overrides,
    ) {
        factory<Any, ViewModelCreationArgs, T> { args: ViewModelCreationArgs ->
            block(A::class.cast(args))
        }
    }
}
