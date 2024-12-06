package com.livefast.eattrash.raccoonforlemmy.core.appearance.repository

import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.AppColor
import org.koin.core.annotation.Single
import kotlin.random.Random

@Single
internal class DefaultAppColorRepository : AppColorRepository {
    override fun getColors(): List<AppColor> =
        listOf(
            AppColor.Blue,
            AppColor.LightBlue,
            AppColor.Purple,
            AppColor.Green,
            AppColor.Red,
            AppColor.Orange,
            AppColor.Yellow,
            AppColor.Pink,
            AppColor.Gray,
            AppColor.White,
        )

    override fun getRandomColor(): AppColor {
        val index = Random.nextInt(10)
        return getColors()[index]
    }
}
