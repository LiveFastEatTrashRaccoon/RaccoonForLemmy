package com.livefast.eattrash.raccoonforlemmy.core.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.cash.turbine.test
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.livefast.eattrash.raccoonforlemmy.core.testutils.DispatcherTestRule
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
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
    fun whenSetCurrentSection_thenValueIsUpdated() = runTest {
        val initial = sut.currentSection.value
        assertNull(initial)

        sut.setCurrentSection(TabNavigationSection.Profile)

        val value = sut.currentSection.value
        assertEquals(TabNavigationSection.Profile, value)
    }

    @Test
    fun whenSetCurrentSectionTwice_thenOnDoubleTabSelectionTriggered() = runTest {
        sut.setCurrentSection(TabNavigationSection.Profile)
        launch {
            delay(DELAY)
            sut.setCurrentSection(TabNavigationSection.Profile)
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
            mockk<Navigator> {
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
    fun whenChangeTab_thenCurrentTabIsUpdated() = runTest {
        val tabSlot = slot<Tab>()
        val navigator =
            mockk<TabNavigator>(relaxUnitFun = true) {
                every { current = capture(tabSlot) } answers {}
            }
        val tab =
            object : Tab {
                override val options @Composable get() = TabOptions(index = 0u, "title")

                @Composable
                override fun Content() {
                    Box(modifier = Modifier.fillMaxSize())
                }
            }
        sut.setTabNavigator(navigator)

        sut.changeTab(tab)

        val value = tabSlot.captured
        assertEquals(tab, value)
    }

    @Test
    fun whenPushScreen_thenInteractionsAreAsExpected() = runTest {
        val previous =
            object : Screen {
                override val key: ScreenKey = "old"

                @Composable
                override fun Content() {
                    Box(modifier = Modifier.fillMaxSize())
                }
            }
        val screen =
            object : Screen {
                override val key: ScreenKey = "new"

                @Composable
                override fun Content() {
                    Box(modifier = Modifier.fillMaxSize())
                }
            }
        val navigator =
            mockk<Navigator>(relaxUnitFun = true) {
                every { canPop } returns true
                every { lastItem } returns previous
            }
        sut.setRootNavigator(navigator)

        launch {
            sut.pushScreen(screen)
        }
        delay(DELAY)

        val canPop = sut.canPop.value
        assertTrue(canPop)
        verify {
            navigator.push(screen)
        }
    }

    @Test
    fun whenPushScreenTwice_thenInteractionsAreAsExpected() = runTest {
        val screen =
            object : Screen {
                override val key: ScreenKey = ""

                @Composable
                override fun Content() {
                    Box(modifier = Modifier.fillMaxSize())
                }
            }
        val navigator =
            mockk<Navigator>(relaxUnitFun = true) {
                every { canPop } returns true
                every { lastItem } returns screen
            }
        sut.setRootNavigator(navigator)

        launch {
            sut.pushScreen(screen)
        }
        advanceTimeBy(DELAY)

        val canPop = sut.canPop.value
        assertTrue(canPop)
        verify(inverse = true) {
            navigator.push(screen)
        }
    }

    @Test
    fun whenPopScreen_thenInteractionsAreAsExpected() = runTest {
        val navigator =
            mockk<Navigator>(relaxUnitFun = true) {
                every { pop() } returns true
                every { canPop } returns false
            }
        sut.setRootNavigator(navigator)

        launch {
            sut.popScreen()
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
        val screen =
            object : Screen {
                @Composable
                override fun Content() {
                    Box(modifier = Modifier.fillMaxSize())
                }
            }
        launch {
            delay(DELAY)
            sut.openSideMenu(screen)
        }

        sut.sideMenuEvents.test {
            val item = awaitItem()
            assertEquals(SideMenuEvents.Open(screen), item)
        }
    }

    @Test
    fun given_already_open_whenShowSideMenu_thenInteractionsAreAsExpected() = runTest {
        val screen =
            object : Screen {
                @Composable
                override fun Content() {
                    Box(modifier = Modifier.fillMaxSize())
                }
            }
        launch {
            delay(DELAY)
            sut.openSideMenu(screen)
            sut.openSideMenu(screen)
        }

        sut.sideMenuEvents.test {
            val item = awaitItem()
            assertEquals(SideMenuEvents.Open(screen), item)
            expectNoEvents()
        }
    }

    @Test
    fun whenCloseSideMenu_thenInteractionsAreAsExpected() = runTest {
        val screen =
            object : Screen {
                @Composable
                override fun Content() {
                    Box(modifier = Modifier.fillMaxSize())
                }
            }
        sut.openSideMenu(screen)
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
            mockk<Navigator>(relaxUnitFun = true) {
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
