package com.github.diegoberaldin.raccoonforlemmy.android.example.presentation

import com.github.diegoberaldin.raccoonforlemmy.example.Greeting

class GreetPresenter(
    private val greeting: Greeting,
) {

    fun print() = greeting.greet()
}