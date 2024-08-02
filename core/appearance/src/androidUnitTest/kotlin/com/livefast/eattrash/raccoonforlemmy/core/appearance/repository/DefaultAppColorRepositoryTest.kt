package com.livefast.eattrash.raccoonforlemmy.core.appearance.repository

import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.AppColor
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.DefaultAppColorRepository
import com.livefast.eattrash.raccoonforlemmy.core.testutils.DispatcherTestRule
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DefaultAppColorRepositoryTest {
    @get:Rule
    val dispatcherTestRule = DispatcherTestRule()

    private val sut = DefaultAppColorRepository()

    @Test
    fun whenGetColors_theResultIsAsExpected() {
        val res = sut.getColors()

        assertEquals(
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
            ),
            res,
        )
    }

    @Test
    fun whenGetRandomColor_theResultIsAsExpected() {
        val res = sut.getRandomColor()

        assertTrue {
            res in
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
        }
    }
}
