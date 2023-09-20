package com.github.diegoberaldin.raccoonforlemmy

import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel

class MainViewModel : ScreenModel {
    var bottomBarOffsetHeightPx = mutableStateOf(0f)
}