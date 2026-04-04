package com.livefast.eattrash.raccoonforlemmy.unit.postlist

import android.content.Context
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.livefast.eattrash.raccoonforlemmy.core.appearance.di.appearanceModule
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.di.lemmyUiModule
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.l10n.testutils.MockStrings
import com.livefast.eattrash.raccoonforlemmy.core.navigation.MainRouter
import com.livefast.eattrash.raccoonforlemmy.core.navigation.di.navigationModule
import com.livefast.eattrash.raccoonforlemmy.core.notifications.di.notificationsModule
import com.livefast.eattrash.raccoonforlemmy.core.persistence.di.persistenceModule
import com.livefast.eattrash.raccoonforlemmy.core.preferences.di.preferencesModule
import com.livefast.eattrash.raccoonforlemmy.core.resources.LocalResources
import com.livefast.eattrash.raccoonforlemmy.core.resources.testutils.MockResources
import com.livefast.eattrash.raccoonforlemmy.core.testutils.KodeinTestRule
import com.livefast.eattrash.raccoonforlemmy.core.utils.di.utilsModule
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.PostModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton

@RunWith(AndroidJUnit4::class)
class PostListScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val uriHandler = mockk<UriHandler>(relaxUnitFun = true)

    @get:Rule
    val diRule =
        KodeinTestRule(
            listOf(
                appearanceModule,
                lemmyUiModule,
                navigationModule,
                notificationsModule,
                persistenceModule,
                preferencesModule,
                utilsModule,
                DI.Module("PostListScreenTestModule") {
                    bind<Context> {
                        singleton { InstrumentationRegistry.getInstrumentation().targetContext }
                    }
                    bind<MainRouter> {
                        singleton { mockk<MainRouter>(relaxUnitFun = true) }
                    }
                },
            ),
        )

    @Test
    fun givenPosts_whenDisplayed_thenContentsAreShowing() {
        val mockModel = mockk<PostListMviModel>(relaxed = true)
        val posts = listOf(
            PostModel(id = 1, title = "Test post 1"),
            PostModel(id = 2, title = "Test post 2"),
        )
        every { mockModel.uiState } returns MutableStateFlow(
            PostListMviModel.UiState(
                initial = false,
                loading = false,
                posts = posts,
                isLogged = false,
                currentUserId = 123L,
            ),
        )
        every { mockModel.effects } returns MutableSharedFlow()

        composeTestRule.setContent {
            CompositionLocalProvider(
                LocalStrings provides MockStrings(),
                LocalUriHandler provides uriHandler,
                LocalResources provides MockResources,
            ) {
                PostListScreen(model = mockModel)
            }
        }

        composeTestRule.onNodeWithText("Test post 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Test post 2").assertIsDisplayed()
    }
}
