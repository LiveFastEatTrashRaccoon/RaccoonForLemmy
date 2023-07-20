package com.github.diegoberaldin.raccoonforlemmy.feature_example

class Greeting(
    private val platform: Platform,
) {

    fun greet(): String {
        return "Hello, ${platform.name}!"
    }
}