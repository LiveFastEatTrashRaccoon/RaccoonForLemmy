package com.github.diegoberaldin.raccoonforlemmy.feature_example

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform