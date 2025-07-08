package com.livefast.eattrash.raccoonforlemmy.core.navigation

import androidx.compose.runtime.Composable
import app.cash.turbine.test
import com.livefast.eattrash.raccoonforlemmy.core.testutils.DispatcherTestRule
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class DefaultNavigationCoordinatorTest {
    @get:Rule
    val dispatcherRule = DispatcherTestRule()

    private val sut =
        DefaultNavigationCoordinator(
            dispatcher = dispatcherRule.dispatcher,
        )

    @Test
    fun whenSetBottomNavigationSection_thenValueIsUpdated() = runTest {
        val initial = sut.currentSection.value
        assertNull(initial)

        sut.setBottomNavigationSection(TabNavigationSection.Profile)

        val value = sut.currentSection.value
        assertEquals(TabNavigationSection.Profile, value)
    }

    @Test
    fun whenSetBottomNavigationSectionTwice_thenOnDoubleTabSelectionTriggered() = runTest {
        sut.setBottomNavigationSection(TabNavigationSection.Profile)
        launch {
            delay(DELAY)
            sut.setBottomNavigationSection(TabNavigationSection.Profile)
        }
        sut.onDoubleTabSelection.test {
            val section = awaitItem()
            assertEquals(TabNavigationSection.Profile, section)
        }
    }

    @Test
    fun givenNavigatorCanPop_whenRootNavigatorSet_thenCanPopIsUpdated() = runTest {
        val initial = sut.canPop.value
        assertFalse(initial)
        val navigator =
            mockk<NavigationAdapter> {
                every { canPop } returns true
            }

        sut.setRootNavigator(navigator)

        val value = sut.canPop.value
        assertTrue(value)
    }

    @Test
    fun whenSubmitDeeplink_thenValueIsEmitted() = runTest {
        val url = "deeplink-url"
        sut.submitDeeplink(url)

        sut.deepLinkUrl.test {
            val value = awaitItem()
            assertEquals(url, value)
        }
    }

    @Test
    fun whenSetInboxUnread_thenValueIsUpdated() = runTest {
        val count = 5
        sut.setInboxUnread(count)
        val value = sut.inboxUnread.value
        assertEquals(count, value)
    }

    @Test
    fun whenSetExitMessageVisible_thenValueIsUpdated() = runTest {
        val initial = sut.exitMessageVisible.value
        assertFalse(initial)

        sut.setExitMessageVisible(true)
        val value = sut.exitMessageVisible.value
        assertTrue(value)
    }

    @Test
    fun whenSetBottomNavigationSection_thenAdapterNavigatesToSection() = runTest {
        val adapter = mockk<BottomNavigationAdapter>(relaxUnitFun = true)
        sut.setBottomNavigator(adapter)

        sut.setBottomNavigationSection(TabNavigationSection.Home)

        verify {
            adapter.navigate(TabNavigationSection.Home)
        }
    }

    @Test
    fun whenPushScreen_thenInteractionsAreAsExpected() = runTest {
        val destination = Destination.WebInternal(url = "www.example.com")
        val navigator =
            mockk<NavigationAdapter>(relaxUnitFun = true) {
                every { canPop } returns true
            }
        sut.setRootNavigator(navigator)

        launch {
            sut.push(destination)
        }
        delay(DELAY)

        val canPop = sut.canPop.value
        assertTrue(canPop)
        verify {
            navigator.navigate(destination)
        }
    }

    @Test
    fun whenPopScreen_thenInteractionsAreAsExpected() = runTest {
        val navigator =
            mockk<NavigationAdapter>(relaxUnitFun = true) {
                every { canPop } returns false
            }
        sut.setRootNavigator(navigator)

        launch {
            sut.pop()
        }
        advanceTimeBy(DELAY)

        val canPop = sut.canPop.value
        assertFalse(canPop)
        verify {
            navigator.pop()
        }
    }

    @Test
    fun whenShowSideMenu_thenInteractionsAreAsExpected() = runTest {
        val content = @Composable {}
        launch {
            delay(DELAY)
            sut.openSideMenu(content)
        }

        sut.sideMenuEvents.test {
            val item = awaitItem()
            assertEquals(SideMenuEvents.Open(content), item)
        }
    }

    @Test
    fun givenAlreadyOpen_whenShowSideMenu_thenInteractionsAreAsExpected() = runTest {
        val content = @Composable {}
        launch {
            delay(DELAY)
            sut.openSideMenu(content)
            sut.openSideMenu(content)
        }

        sut.sideMenuEvents.test {
            val item = awaitItem()
            assertEquals(SideMenuEvents.Open(content), item)
            expectNoEvents()
        }
    }

    @Test
    fun whenCloseSideMenu_thenInteractionsAreAsExpected() = runTest {
        val content = @Composable {}
        sut.openSideMenu(content)
        launch {
            delay(DELAY)
            sut.closeSideMenu()
        }

        sut.sideMenuEvents.test {
            val item = awaitItem()
            assertEquals(SideMenuEvents.Close, item)
        }
    }

    @Test
    fun whenSubmitComposeEvent_thenInteractionsAreAsExpected() = runTest {
        launch {
            delay(DELAY)
            sut.submitComposeEvent(ComposeEvent.WithText("text"))
        }

        sut.composeEvents.test {
            val item = awaitItem()
            assertEquals(ComposeEvent.WithText("text"), item)
        }
    }

    @Test
    fun whenShowGlobalMessage_thenInteractionsAreAsExpected() = runTest {
        val message = "test message"
        launch {
            delay(DELAY)
            sut.showGlobalMessage(message)
        }

        sut.globalMessage.test {
            val item = awaitItem()
            assertEquals(message, item)
        }
    }

    @Test
    fun whenPopUntilRoot_thenInteractionsAreAsExpected() = runTest {
        val navigator =
            mockk<NavigationAdapter>(relaxUnitFun = true) {
                every { canPop } returns false
            }
        sut.setRootNavigator(navigator)

        sut.popUntilRoot()

        verify {
            navigator.popUntilRoot()
        }
    }

    companion object {
        private const val DELAY = 250L
    }
}
