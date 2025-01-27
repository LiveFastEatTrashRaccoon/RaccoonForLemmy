package com.livefast.eattrash.raccoonforlemmy.core.appearance.theme

import android.app.Activity
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.UiBarTheme
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.UiTheme

interface SolidBarColorWorkaround {
    fun apply(
        activity: Activity,
        theme: UiTheme,
        barTheme: UiBarTheme,
    )
}
