package com.livefast.eattrash.raccoonforlemmy.feature.profile.main

import com.livefast.eattrash.raccoonforlemmy.core.architecture.testutils.MviModelTestRule
import com.livefast.eattrash.raccoonforlemmy.core.navigation.TabNavigationSection
import com.livefast.eattrash.raccoonforlemmy.core.navigation.toInt
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.SettingsRepository
import com.livefast.eattrash.raccoonforlemmy.core.testutils.DispatcherTestRule
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.LemmyValueCache
import com.livefast.eattrash.raccoonforlemmy.feature.profile.menu.ProfileSideMenuViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ProfileSideMenuViewModelTest {
    @get:Rule(order = 0)
    val dispatcherTestRule = DispatcherTestRule()

    @get:Rule(order = 1)
    val rule =
        MviModelTestRule {
            ProfileSideMenuViewModel(
                settingsRepository = settingsRepository,
                lemmyValueCache = lemmyValueCache,
            )
        }

    private val settingsRepository =
        mockk<SettingsRepository> {
            every { currentBottomBarSections } returns
                MutableStateFlow(
                    listOf(
                        TabNavigationSection.Home.toInt(),
                        TabNavigationSection.Explore.toInt(),
                        TabNavigationSection.Inbox.toInt(),
                        TabNavigationSection.Profile.toInt(),
                    ),
                )
        }
    private val lemmyValueCache =
        mockk<LemmyValueCache> {
            every { isCurrentUserModerator } returns MutableStateFlow(false)
            every { isCurrentUserAdmin } returns MutableStateFlow(false)
            every { isCommunityCreationAdminOnly } returns MutableStateFlow(false)
        }

    @Test
    fun givenNotModCommunityNotRestrictedDefaultBottomBarItems_whenInitialized_thenStateIsAsExpected() =
        runTest {
            rule.onState {
                assertTrue(it.isBookmarksVisible)
                assertTrue(it.canCreateCommunity)
                assertFalse(it.isModerator)
            }
        }
}
