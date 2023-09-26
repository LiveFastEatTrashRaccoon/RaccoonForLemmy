package com.github.diegoberaldin.raccoonforlemmy.core.utils

import org.koin.core.module.Module

interface ShareHelper {
    fun share(url: String, mimeType: String)
}

expect val shareHelperModule: Module