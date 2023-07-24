package com.github.diegoberaldin.raccoonforlemmy.core_md.model

/** An interface of providing use case specific un/ordered list handling.*/
fun interface BulletHandler {
    fun transform(bullet: CharSequence?): String
}
