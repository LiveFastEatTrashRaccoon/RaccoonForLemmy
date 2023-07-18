package com.github.diegoberaldin.raccoonforlemmy

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform