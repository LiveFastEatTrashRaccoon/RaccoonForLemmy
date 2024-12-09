package com.livefast.eattrash.raccoonforlemmy.domain.identity.urlhandler

import androidx.compose.ui.platform.UriHandler
import com.livefast.eattrash.raccoonforlemmy.core.commonui.detailopener.api.DetailOpener
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.SettingsModel
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.SettingsRepository
import com.livefast.eattrash.raccoonforlemmy.core.testutils.DispatcherTestRule
import com.livefast.eattrash.raccoonforlemmy.core.utils.url.CustomTabsHelper
import io.mockk.Called
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test

class DefaultCustomUriHandlerTest {
    @get:Rule
    val dispatcherTestRule = DispatcherTestRule()

    private val fallbackHandler: UriHandler = mockk(relaxUnitFun = true)
    private val settingsRepository: SettingsRepository =
        mockk {
            every { currentSettings } returns MutableStateFlow(SettingsModel())
        }
    private val communityProcessor: CommunityProcessor =
        mockk {
            coEvery { process(any()) } returns false
        }
    private val userProcessor: UserProcessor =
        mockk {
            coEvery { process(any()) } returns false
        }
    private val postProcessor: PostProcessor =
        mockk {
            coEvery { process(any()) } returns false
        }
    private val commentProcessor: CommentProcessor =
        mockk {
            coEvery { process(any()) } returns false
        }
    private val detailOpener: DetailOpener = mockk(relaxUnitFun = true)
    private val customTabsHelper: CustomTabsHelper = mockk(relaxUnitFun = true)
    private val sut =
        DefaultCustomUriHandler(
            fallbackHandler = fallbackHandler,
            settingsRepository = settingsRepository,
            communityProcessor = communityProcessor,
            userProcessor = userProcessor,
            postProcessor = postProcessor,
            commentProcessor = commentProcessor,
            detailOpener = detailOpener,
            customTabsHelper = customTabsHelper,
            dispatcher = dispatcherTestRule.dispatcher,
        )

    @Test
    fun givenCommunityRecognized_whenProcess_thenInteractionsAreExpected() {
        coEvery { communityProcessor.process(any()) } returns true

        sut.openUri(URL)

        coVerify {
            communityProcessor.process(URL)
        }
        verify {
            userProcessor wasNot Called
            postProcessor wasNot Called
            commentProcessor wasNot Called
            fallbackHandler wasNot Called
            detailOpener wasNot Called
            customTabsHelper wasNot Called
        }
    }

    @Test
    fun givenUserRecognized_whenProcess_thenInteractionsAreExpected() {
        coEvery { userProcessor.process(any()) } returns true

        sut.openUri(URL)

        coVerify {
            communityProcessor.process(URL)
            userProcessor.process(URL)
        }
        verify {
            postProcessor wasNot Called
            commentProcessor wasNot Called
            fallbackHandler wasNot Called
            detailOpener wasNot Called
            customTabsHelper wasNot Called
        }
    }

    @Test
    fun givenPostRecognized_whenProcess_thenInteractionsAreExpected() {
        coEvery { postProcessor.process(any()) } returns true

        sut.openUri(URL)

        coVerify {
            communityProcessor.process(URL)
            userProcessor.process(URL)
            postProcessor.process(URL)
        }
        verify {
            commentProcessor wasNot Called
            fallbackHandler wasNot Called
            detailOpener wasNot Called
            customTabsHelper wasNot Called
        }
    }

    @Test
    fun givenCommentRecognized_whenProcess_thenInteractionsAreExpected() {
        coEvery { commentProcessor.process(any()) } returns true

        sut.openUri(URL)

        coVerify {
            communityProcessor.process(URL)
            userProcessor.process(URL)
            postProcessor.process(URL)
            commentProcessor.process(URL)
        }
        verify {
            fallbackHandler wasNot Called
            detailOpener wasNot Called
            customTabsHelper wasNot Called
        }
    }

    @Test
    fun givenNotRecognizedAndOpenExternalBrowser_whenProcess_thenInteractionsAreExpected() {
        sut.openUri(URL)

        coVerify {
            communityProcessor.process(URL)
            userProcessor.process(URL)
            postProcessor.process(URL)
            commentProcessor.process(URL)
        }
        verify {
            fallbackHandler.openUri(URL)
            detailOpener wasNot Called
            customTabsHelper wasNot Called
        }
    }

    @Test
    fun givenNotRecognizedAndOpenInternalWebView_whenProcess_thenInteractionsAreExpected() {
        every { settingsRepository.currentSettings } returns
            MutableStateFlow(SettingsModel(urlOpeningMode = 0))

        sut.openUri(URL)

        coVerify {
            communityProcessor.process(URL)
            userProcessor.process(URL)
            postProcessor.process(URL)
            commentProcessor.process(URL)
        }
        verify {
            fallbackHandler wasNot Called
            detailOpener.openWebInternal(URL)
            customTabsHelper wasNot Called
        }
    }

    @Test
    fun givenNotRecognizedAndOpenCustomTabs_whenProcess_thenInteractionsAreExpected() {
        every { settingsRepository.currentSettings } returns
            MutableStateFlow(SettingsModel(urlOpeningMode = 2))

        sut.openUri(URL)

        coVerify {
            communityProcessor.process(URL)
            userProcessor.process(URL)
            postProcessor.process(URL)
            commentProcessor.process(URL)
        }
        verify {
            fallbackHandler wasNot Called
            detailOpener wasNot Called
            customTabsHelper.handle(URL)
        }
    }

    companion object {
        private const val URL = "https://example.com"
    }
}
