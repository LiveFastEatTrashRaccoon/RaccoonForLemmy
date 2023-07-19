package com.github.diegoberaldin.raccoonforlemmy.main

import kotlinx.coroutines.CoroutineScope

class DefaultAppComponent(
) : AppComponent {

    private lateinit var viewModelScope: CoroutineScope
}