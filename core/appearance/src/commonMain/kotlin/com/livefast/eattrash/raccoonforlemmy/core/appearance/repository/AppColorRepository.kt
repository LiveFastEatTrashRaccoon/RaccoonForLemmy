package com.livefast.eattrash.raccoonforlemmy.core.appearance.repository

import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.AppColor

interface AppColorRepository {
    fun getColors(): List<AppColor>

    fun getRandomColor(): AppColor
}
