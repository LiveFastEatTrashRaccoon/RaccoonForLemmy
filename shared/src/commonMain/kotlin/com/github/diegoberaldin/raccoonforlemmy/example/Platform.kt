package com.github.diegoberaldin.raccoonforlemmy.example

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform