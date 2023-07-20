package com.github.diegoberaldin.raccoonforlemmy.feature_example

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object GreetingHelper : KoinComponent {
    private val greeting: Greeting by inject()
    fun greet(): String = greeting.greet()
}