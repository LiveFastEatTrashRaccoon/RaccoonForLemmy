package com.github.diegoberaldin.raccoonforlemmy.example

class Greeting(
    private val platform: Platform,
) {

    fun greet(): String {
        return "Hello, ${platform.name}!"
    }
}