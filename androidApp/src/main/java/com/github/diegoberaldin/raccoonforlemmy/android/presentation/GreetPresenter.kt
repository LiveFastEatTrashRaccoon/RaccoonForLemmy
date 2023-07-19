package com.github.diegoberaldin.raccoonforlemmy.android.presentation

import com.github.diegoberaldin.raccoonforlemmy.Greeting

class GreetPresenter(
    private val greeting: Greeting,
) {

    fun print() = greeting.greet()
}