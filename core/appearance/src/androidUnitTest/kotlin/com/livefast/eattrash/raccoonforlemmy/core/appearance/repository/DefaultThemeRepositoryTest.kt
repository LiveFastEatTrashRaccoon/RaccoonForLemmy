package com.livefast.eattrash.raccoonforlemmy.core.appearance.repository

import androidx.compose.ui.graphics.Color
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.CommentBarTheme
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.PostLayout
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.UiFontFamily
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.UiTheme
import com.livefast.eattrash.raccoonforlemmy.core.testutils.DispatcherTestRule
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNull

class DefaultThemeRepositoryTest {
    @get:Rule
    val dispatcherTestRule = DispatcherTestRule()

    private val sut = DefaultThemeRepository()

    @Test
    fun whenChangeUiTheme_thenValueIsAsExpected() {
        val resBefore = sut.uiTheme.value
        assertEquals(resBefore, UiTheme.Default)

        sut.changeUiTheme(UiTheme.Dark)

        val resAfter = sut.uiTheme.value
        assertEquals(UiTheme.Dark, resAfter)
    }

    @Test
    fun whenChangeUiFontFamily_thenValueIsAsExpected() {
        val resBefore = sut.uiFontFamily.value
        assertEquals(UiFontFamily.Poppins, resBefore)

        sut.changeUiFontFamily(UiFontFamily.NotoSans)

        val resAfter = sut.uiFontFamily.value
        assertEquals(UiFontFamily.NotoSans, resAfter)
    }

    @Test
    fun whenChangeUiFontScale_thenValueIsAsExpected() {
        val resBefore = sut.uiFontScale.value
        assertEquals(1f, resBefore)

        val value = 1.5f
        sut.changeUiFontScale(value)

        val resAfter = sut.uiFontScale.value
        assertEquals(value, resAfter)
    }

    @Test
    fun whenChangeContentFontScales_thenValueIsAsExpected() {
        val resBefore = sut.contentFontScale.value
        assertEquals(ContentFontScales(), resBefore)

        val value = ContentFontScales(title = 1.25f)
        sut.changeContentFontScale(value)

        val resAfter = sut.contentFontScale.value
        assertEquals(value, resAfter)
    }

    @Test
    fun whenChangeContentFontFamily_thenValueIsAsExpected() {
        val resBefore = sut.contentFontFamily.value
        assertEquals(UiFontFamily.Poppins, resBefore)

        sut.changeContentFontFamily(UiFontFamily.NotoSans)

        val resAfter = sut.contentFontFamily.value
        assertEquals(UiFontFamily.NotoSans, resAfter)
    }

    @Test
    fun whenChangeNavItemTitles_thenValueIsAsExpected() {
        val initial = sut.navItemTitles.value
        sut.changeNavItemTitles(!initial)

        val res = sut.navItemTitles.value
        assertNotEquals(initial, res)
    }

    @Test
    fun whenChangeDynamicColors_thenValueIsAsExpected() {
        val initial = sut.dynamicColors.value
        sut.changeDynamicColors(!initial)

        val res = sut.dynamicColors.value
        assertNotEquals(initial, res)
    }

    @Test
    fun whenChangePostLayout_thenValueIsAsExpected() {
        val resBefore = sut.postLayout.value
        assertEquals(PostLayout.Card, resBefore)

        sut.changePostLayout(PostLayout.Full)

        val resAfter = sut.postLayout.value
        assertEquals(PostLayout.Full, resAfter)
    }

    @Test
    fun whenChangeCustomSeedColor_thenValueIsAsExpected() {
        val resBefore = sut.customSeedColor.value
        assertNull(resBefore)

        sut.changeCustomSeedColor(Color.Red)

        val resAfter = sut.customSeedColor.value
        assertEquals(Color.Red, resAfter)
    }

    @Test
    fun whenChangeUpVoteColor_thenValueIsAsExpected() {
        sut.changeUpVoteColor(Color.Red)

        val resAfter = sut.upVoteColor.value
        assertEquals(Color.Red, resAfter)
    }

    @Test
    fun whenChangeDownVoteColor_thenValueIsAsExpected() {
        sut.changeDownVoteColor(Color.Red)

        val resAfter = sut.downVoteColor.value
        assertEquals(Color.Red, resAfter)
    }

    @Test
    fun whenChangeReplyColor_thenValueIsAsExpected() {
        sut.changeReplyColor(Color.Red)

        val resAfter = sut.replyColor.value
        assertEquals(Color.Red, resAfter)
    }

    @Test
    fun whenChangeSaveColor_thenValueIsAsExpected() {
        sut.changeSaveColor(Color.Red)

        val resAfter = sut.saveColor.value
        assertEquals(Color.Red, resAfter)
    }

    @Test
    fun whenChangeCommentBarTheme_thenValueIsAsExpected() {
        val resBefore = sut.commentBarTheme.value
        assertEquals(CommentBarTheme.Blue, resBefore)

        sut.changeCommentBarTheme(CommentBarTheme.Green)

        val resAfter = sut.commentBarTheme.value
        assertEquals(CommentBarTheme.Green, resAfter)
    }

    @Test
    fun getCommentBarColorGreen_thenValueIsAsExpected() {
        val res0 = sut.getCommentBarColor(0, CommentBarTheme.Green)
        val res1 = sut.getCommentBarColor(1, CommentBarTheme.Green)
        val res2 = sut.getCommentBarColor(2, CommentBarTheme.Green)
        val res3 = sut.getCommentBarColor(3, CommentBarTheme.Green)
        val res4 = sut.getCommentBarColor(4, CommentBarTheme.Green)
        val res5 = sut.getCommentBarColor(5, CommentBarTheme.Green)
        val res6 = sut.getCommentBarColor(6, CommentBarTheme.Green)

        assertEquals(Color(0xFF1B4332), res0)
        assertEquals(Color(0xFF2D6A4F), res1)
        assertEquals(Color(0xFF40916C), res2)
        assertEquals(Color(0xFF52B788), res3)
        assertEquals(Color(0xFF74C69D), res4)
        assertEquals(Color(0xFF95D5B2), res5)
        assertEquals(Color(0xFF1B4332), res6)
    }

    @Test
    fun getCommentBarColorRed_thenValueIsAsExpected() {
        val res0 = sut.getCommentBarColor(0, CommentBarTheme.Red)
        val res1 = sut.getCommentBarColor(1, CommentBarTheme.Red)
        val res2 = sut.getCommentBarColor(2, CommentBarTheme.Red)
        val res3 = sut.getCommentBarColor(3, CommentBarTheme.Red)
        val res4 = sut.getCommentBarColor(4, CommentBarTheme.Red)
        val res5 = sut.getCommentBarColor(5, CommentBarTheme.Red)
        val res6 = sut.getCommentBarColor(6, CommentBarTheme.Red)

        assertEquals(Color(0xFF6A040F), res0)
        assertEquals(Color(0xFF9D0208), res1)
        assertEquals(Color(0xFFD00000), res2)
        assertEquals(Color(0xFFDC2F02), res3)
        assertEquals(Color(0xFFE85D04), res4)
        assertEquals(Color(0xFFF48C06), res5)
        assertEquals(Color(0xFF6A040F), res6)
    }

    @Test
    fun getCommentBarColorBlue_thenValueIsAsExpected() {
        val res0 = sut.getCommentBarColor(0, CommentBarTheme.Blue)
        val res1 = sut.getCommentBarColor(1, CommentBarTheme.Blue)
        val res2 = sut.getCommentBarColor(2, CommentBarTheme.Blue)
        val res3 = sut.getCommentBarColor(3, CommentBarTheme.Blue)
        val res4 = sut.getCommentBarColor(4, CommentBarTheme.Blue)
        val res5 = sut.getCommentBarColor(5, CommentBarTheme.Blue)
        val res6 = sut.getCommentBarColor(6, CommentBarTheme.Blue)

        assertEquals(Color(0xFF012A4A), res0)
        assertEquals(Color(0xFF013A63), res1)
        assertEquals(Color(0xFF014F86), res2)
        assertEquals(Color(0xFF2C7DA0), res3)
        assertEquals(Color(0xFF61A5C2), res4)
        assertEquals(Color(0xFFA9D6E5), res5)
        assertEquals(Color(0xFF012A4A), res6)
    }

    @Test
    fun getCommentBarColorRainbow_thenValueIsAsExpected() {
        val res0 = sut.getCommentBarColor(0, CommentBarTheme.Rainbow)
        val res1 = sut.getCommentBarColor(1, CommentBarTheme.Rainbow)
        val res2 = sut.getCommentBarColor(2, CommentBarTheme.Rainbow)
        val res3 = sut.getCommentBarColor(3, CommentBarTheme.Rainbow)
        val res4 = sut.getCommentBarColor(4, CommentBarTheme.Rainbow)
        val res5 = sut.getCommentBarColor(5, CommentBarTheme.Rainbow)
        val res6 = sut.getCommentBarColor(6, CommentBarTheme.Rainbow)

        assertEquals(Color(0xFF9400D3), res0)
        assertEquals(Color(0xFF0000FF), res1)
        assertEquals(Color(0xFF00FF00), res2)
        assertEquals(Color(0xFFFFFF00), res3)
        assertEquals(Color(0xFFFF7F00), res4)
        assertEquals(Color(0xFFFF0000), res5)
        assertEquals(Color(0xFF9400D3), res6)
    }
}
