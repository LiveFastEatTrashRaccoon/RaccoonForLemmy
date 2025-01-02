package com.livefast.eattrash.raccoonforlemmy.core.persistence.repository

import app.cash.sqldelight.Query
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.VoteFormat
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.toLong
import com.livefast.eattrash.raccoonforlemmy.core.persistence.SettingsQueries
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.ActionOnSwipe
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.SettingsModel
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.toInt
import com.livefast.eattrash.raccoonforlemmy.core.persistence.entities.AppDatabase
import com.livefast.eattrash.raccoonforlemmy.core.persistence.provider.DatabaseProvider
import com.livefast.eattrash.raccoonforlemmy.core.persistence.settings.GetBy
import com.livefast.eattrash.raccoonforlemmy.core.preferences.store.TemporaryKeyStore
import com.livefast.eattrash.raccoonforlemmy.core.testutils.DispatcherTestRule
import io.mockk.Called
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.time.Duration
import kotlin.time.DurationUnit

class DefaultSettingsRepositoryTest {
    @get:Rule
    val dispatcherTestRule = DispatcherTestRule()

    private val query = mockk<Query<GetBy>>()
    private val queries =
        mockk<SettingsQueries>(relaxUnitFun = true) {
            every { getBy(any()) } returns query
        }
    private val provider =
        mockk<DatabaseProvider> {
            every { getDatabase() } returns
                mockk<AppDatabase> {
                    every { settingsQueries } returns queries
                }
        }
    private val keyStore = mockk<TemporaryKeyStore>(relaxUnitFun = true)

    private val sut =
        DefaultSettingsRepository(
            provider = provider,
            keyStore = keyStore,
        )

    @Test
    fun givenAccount_whenGetSettings_thenResultIsAsExpected() =
        runTest {
            every { query.executeAsOneOrNull() } returns createFake(id = 2)
            every { keyStore[any(), any<Boolean>()] } returns false

            val res = sut.getSettings(1)

            assertEquals(2, res.id)

            verify {
                queries.getBy(account_id = 1)
            }
        }

    @Test
    fun givenNoAccount_whenGetSettings_thenResultIsAsExpected() =
        runTest {
            every { keyStore[any(), any<Long>()] } returns 0
            every { keyStore[any(), any<Int>()] } returns 0
            every { keyStore[any(), any<Float>()] } returns 1f
            every { keyStore[any(), any<String>()] } returns ""
            every { keyStore[any(), any<Boolean>()] } returns false
            every { keyStore.containsKey(any()) } returns true

            val res = sut.getSettings(null)

            assertNotNull(res)

            verify {
                queries wasNot Called
            }
        }

    @Test
    fun whenChangeCurrentSettings_thenValueIsUpdated() =
        runTest {
            val model = SettingsModel(defaultListingType = 1)
            sut.changeCurrentSettings(model)
            val value = sut.currentSettings.value
            assertEquals(model, value)
        }

    @Test
    fun whenChangeCurrentBottomBarSections_thenValueIsUpdated() =
        runTest {
            val sectionIds = listOf(0, 1, 2)
            sut.changeCurrentBottomBarSections(sectionIds)
            val value = sut.currentBottomBarSections.value
            assertEquals(sectionIds, value)
        }

    @Test
    fun whenCreate_thenResultIsAsExpected() =
        runTest {
            val model = SettingsModel()
            sut.createSettings(model, 1)

            verify {
                queries.create(
                    theme = model.theme?.toLong(),
                    uiFontScale = model.uiFontScale.toDouble(),
                    uiFontFamily = model.uiFontFamily.toLong(),
                    titleFontScale = model.contentFontScale.title.toDouble(),
                    contentFontScale = model.contentFontScale.body.toDouble(),
                    commentFontScale = model.contentFontScale.comment.toDouble(),
                    ancillaryFontScale = model.contentFontScale.ancillary.toDouble(),
                    locale = model.locale,
                    defaultListingType = model.defaultListingType.toLong(),
                    defaultPostSortType = model.defaultPostSortType.toLong(),
                    defaultCommentSortType = model.defaultCommentSortType.toLong(),
                    defaultInboxType = model.defaultInboxType.toLong(),
                    includeNsfw = if (model.includeNsfw) 1 else 0,
                    blurNsfw = if (model.blurNsfw) 1 else 0,
                    navigationTitlesVisible = if (model.navigationTitlesVisible) 1 else 0,
                    dynamicColors = if (model.dynamicColors) 1 else 0,
                    openUrlsInExternalBrowser = model.urlOpeningMode.toLong(),
                    enableSwipeActions = if (model.enableSwipeActions) 1 else 0,
                    enableDoubleTapAction = if (model.enableDoubleTapAction) 1 else 0,
                    customSeedColor = model.customSeedColor?.toLong(),
                    postLayout = model.postLayout.toLong(),
                    separateUpAndDownVotes = if (model.voteFormat == VoteFormat.Separated) 1 else 0,
                    autoLoadImages = if (model.autoLoadImages) 1 else 0,
                    autoExpandComments = if (model.autoExpandComments) 1 else 0,
                    fullHeightImages = if (model.fullHeightImages) 1 else 0,
                    upvoteColor = model.upVoteColor?.toLong(),
                    downvoteColor = model.downVoteColor?.toLong(),
                    hideNavigationBarWhileScrolling = if (model.hideNavigationBarWhileScrolling) 1 else 0,
                    zombieModeInterval = model.zombieModeInterval.toLong(DurationUnit.MILLISECONDS),
                    zombieModeScrollAmount = model.zombieModeScrollAmount.toDouble(),
                    markAsReadWhileScrolling = if (model.markAsReadWhileScrolling) 1 else 0,
                    commentBarTheme = model.commentBarTheme.toLong(),
                    replyColor = model.replyColor?.toLong(),
                    saveColor = model.saveColor?.toLong(),
                    searchPostTitleOnly = if (model.searchPostTitleOnly) 1 else 0,
                    contentFontFamily = model.contentFontFamily.toLong(),
                    edgeToEdge = if (model.edgeToEdge) 1 else 0,
                    postBodyMaxLines = model.postBodyMaxLines?.toLong(),
                    infiniteScrollEnabled = if (model.infiniteScrollEnabled) 1 else 0,
                    actionsOnSwipeToStartPosts = model.actionsOnSwipeToStartPosts.serialized(),
                    actionsOnSwipeToEndPosts = model.actionsOnSwipeToEndPosts.serialized(),
                    actionsOnSwipeToStartComments = model.actionsOnSwipeToStartComments.serialized(),
                    actionsOnSwipeToEndComments = model.actionsOnSwipeToEndComments.serialized(),
                    actionsOnSwipeToStartInbox = model.actionsOnSwipeToStartInbox.serialized(),
                    actionsOnSwipeToEndInbox = model.actionsOnSwipeToEndInbox.serialized(),
                    opaqueSystemBars = model.systemBarTheme.toLong(),
                    showScores = if (model.showScores) 1 else 0,
                    preferUserNicknames = if (model.preferUserNicknames) 1 else 0,
                    commentBarThickness = model.commentBarThickness.toLong(),
                    imageSourcePath = if (model.imageSourcePath) 1 else 0,
                    defaultExploreType = model.defaultExploreType.toLong(),
                    defaultLanguageId = model.defaultLanguageId,
                    inboxBackgroundCheckPeriod = model.inboxBackgroundCheckPeriod?.inWholeMilliseconds,
                    enableButtonsToScrollBetweenComments = if (model.enableButtonsToScrollBetweenComments) 1 else 0,
                    fadeReadPosts = if (model.fadeReadPosts) 1 else 0,
                    fullWidthImages = if (model.fullWidthImages) 1 else 0,
                    showUnreadComments = if (model.showUnreadComments) 1 else 0,
                    commentIndentAmount = model.commentIndentAmount.toLong(),
                    enableToggleFavoriteInNavDrawer = if (model.enableToggleFavoriteInNavDrawer) 1 else 0,
                    account_id = 1,
                    inboxPreviewMaxLines = model.inboxPreviewMaxLines?.toLong(),
                    defaultExploreResultType = model.defaultExploreResultType.toLong(),
                    useAvatarAsProfileNavigationIcon = if (model.useAvatarAsProfileNavigationIcon) 1 else 0,
                    randomThemeColor = if (model.randomThemeColor) 1 else 0,
                    openPostWebPageOnImageClick = if (model.openPostWebPageOnImageClick) 1 else 0,
                    restrictLocalUserSearch = if (model.restrictLocalUserSearch) 1 else 0,
                )
            }
        }

    @Test
    fun givenLoggedUser_whenUpdate_thenResultIsAsExpected() =
        runTest {
            val model = SettingsModel(defaultListingType = 1)
            sut.updateSettings(model, 1)

            verify {
                queries.update(
                    theme = model.theme?.toLong(),
                    uiFontScale = model.uiFontScale.toDouble(),
                    uiFontFamily = model.uiFontFamily.toLong(),
                    titleFontScale = model.contentFontScale.title.toDouble(),
                    contentFontScale = model.contentFontScale.body.toDouble(),
                    commentFontScale = model.contentFontScale.comment.toDouble(),
                    ancillaryFontScale = model.contentFontScale.ancillary.toDouble(),
                    locale = model.locale,
                    defaultListingType = model.defaultListingType.toLong(),
                    defaultPostSortType = model.defaultPostSortType.toLong(),
                    defaultCommentSortType = model.defaultCommentSortType.toLong(),
                    defaultInboxType = model.defaultInboxType.toLong(),
                    includeNsfw = if (model.includeNsfw) 1 else 0,
                    blurNsfw = if (model.blurNsfw) 1 else 0,
                    navigationTitlesVisible = if (model.navigationTitlesVisible) 1 else 0,
                    dynamicColors = if (model.dynamicColors) 1 else 0,
                    openUrlsInExternalBrowser = model.urlOpeningMode.toLong(),
                    enableSwipeActions = if (model.enableSwipeActions) 1 else 0,
                    enableDoubleTapAction = if (model.enableDoubleTapAction) 1 else 0,
                    customSeedColor = model.customSeedColor?.toLong(),
                    postLayout = model.postLayout.toLong(),
                    separateUpAndDownVotes = if (model.voteFormat == VoteFormat.Separated) 1 else 0,
                    autoLoadImages = if (model.autoLoadImages) 1 else 0,
                    autoExpandComments = if (model.autoExpandComments) 1 else 0,
                    fullHeightImages = if (model.fullHeightImages) 1 else 0,
                    upvoteColor = model.upVoteColor?.toLong(),
                    downvoteColor = model.downVoteColor?.toLong(),
                    hideNavigationBarWhileScrolling = if (model.hideNavigationBarWhileScrolling) 1 else 0,
                    zombieModeInterval = model.zombieModeInterval.toLong(DurationUnit.MILLISECONDS),
                    zombieModeScrollAmount = model.zombieModeScrollAmount.toDouble(),
                    markAsReadWhileScrolling = if (model.markAsReadWhileScrolling) 1 else 0,
                    commentBarTheme = model.commentBarTheme.toLong(),
                    replyColor = model.replyColor?.toLong(),
                    saveColor = model.saveColor?.toLong(),
                    searchPostTitleOnly = if (model.searchPostTitleOnly) 1 else 0,
                    contentFontFamily = model.contentFontFamily.toLong(),
                    edgeToEdge = if (model.edgeToEdge) 1 else 0,
                    postBodyMaxLines = model.postBodyMaxLines?.toLong(),
                    infiniteScrollEnabled = if (model.infiniteScrollEnabled) 1 else 0,
                    actionsOnSwipeToStartPosts = model.actionsOnSwipeToStartPosts.serialized(),
                    actionsOnSwipeToEndPosts = model.actionsOnSwipeToEndPosts.serialized(),
                    actionsOnSwipeToStartComments = model.actionsOnSwipeToStartComments.serialized(),
                    actionsOnSwipeToEndComments = model.actionsOnSwipeToEndComments.serialized(),
                    actionsOnSwipeToStartInbox = model.actionsOnSwipeToStartInbox.serialized(),
                    actionsOnSwipeToEndInbox = model.actionsOnSwipeToEndInbox.serialized(),
                    opaqueSystemBars = model.systemBarTheme.toLong(),
                    showScores = if (model.showScores) 1 else 0,
                    preferUserNicknames = if (model.preferUserNicknames) 1 else 0,
                    commentBarThickness = model.commentBarThickness.toLong(),
                    imageSourcePath = if (model.imageSourcePath) 1 else 0,
                    defaultExploreType = model.defaultExploreType.toLong(),
                    defaultLanguageId = model.defaultLanguageId,
                    inboxBackgroundCheckPeriod = model.inboxBackgroundCheckPeriod?.inWholeMilliseconds,
                    enableButtonsToScrollBetweenComments = if (model.enableButtonsToScrollBetweenComments) 1 else 0,
                    fadeReadPosts = if (model.fadeReadPosts) 1 else 0,
                    fullWidthImages = if (model.fullWidthImages) 1 else 0,
                    showUnreadComments = if (model.showUnreadComments) 1 else 0,
                    commentIndentAmount = model.commentIndentAmount.toLong(),
                    enableToggleFavoriteInNavDrawer = if (model.enableToggleFavoriteInNavDrawer) 1 else 0,
                    account_id = 1,
                    inboxPreviewMaxLines = model.inboxPreviewMaxLines?.toLong(),
                    defaultExploreResultType = model.defaultExploreResultType.toLong(),
                    useAvatarAsProfileNavigationIcon = if (model.useAvatarAsProfileNavigationIcon) 1 else 0,
                    randomThemeColor = if (model.randomThemeColor) 1 else 0,
                    openPostWebPageOnImageClick = if (model.openPostWebPageOnImageClick) 1 else 0,
                    restrictLocalUserSearch = if (model.restrictLocalUserSearch) 1 else 0,
                )
            }
        }

    @Test
    fun givenAnonymousUser_whenUpdate_thenResultIsAsExpected() =
        runTest {
            val model =
                SettingsModel(
                    theme = 1,
                    defaultListingType = 1,
                )
            sut.updateSettings(model, null)

            coVerify {
                keyStore.save("uiTheme", model.theme ?: 0)
                keyStore.save("uiFontFamily", model.uiFontFamily)
                keyStore.save("uiFontSize", model.uiFontScale)
                keyStore.save("titleFontSize", model.contentFontScale.title)
                keyStore.save("contentFontSize", model.contentFontScale.body)
                keyStore.save("commentFontSize", model.contentFontScale.comment)
                keyStore.save("ancillaryFontSize", model.contentFontScale.ancillary)
                keyStore.save("defaultListingType", model.defaultListingType)
                keyStore.save("defaultPostSortType", model.defaultPostSortType)
                keyStore.save("defaultInboxType", model.defaultInboxType)
                keyStore.save("defaultCommentSortType", model.defaultCommentSortType)
                keyStore.save("defaultExploreType", model.defaultExploreType)
                keyStore.save("includeNsfw", model.includeNsfw)
                keyStore.save("blurNsfw", model.blurNsfw)
                keyStore.save("navItemTitlesVisible", model.navigationTitlesVisible)
                keyStore.save("dynamicColors", model.dynamicColors)
                keyStore.save("openUrlsInExternalBrowser", model.urlOpeningMode)
                keyStore.save("enableSwipeActions", model.enableSwipeActions)
                keyStore.save("enableDoubleTapAction", model.enableDoubleTapAction)
                keyStore.save("postLayout", model.postLayout)
                keyStore.save("separateUpAndDownVotes", model.voteFormat.toLong())
                keyStore.save("autoLoadImages", model.autoLoadImages)
                keyStore.save("autoExpandComments", model.autoExpandComments)
                keyStore.save("fullHeightImages", model.fullHeightImages)
                keyStore.save(
                    "hideNavigationBarWhileScrolling",
                    model.hideNavigationBarWhileScrolling,
                )
                keyStore.save("zombieModeInterval", model.zombieModeInterval.inWholeMilliseconds)
                keyStore.save("zombieModeScrollAmount", model.zombieModeScrollAmount)
                keyStore.save("markAsReadWhileScrolling", model.markAsReadWhileScrolling)
                keyStore.save("commentBarTheme", model.commentBarTheme)
                keyStore.save("searchPostTitleOnly", model.searchPostTitleOnly)
                keyStore.save("contentFontFamily", model.contentFontFamily)
                keyStore.save("edgeToEdge", model.edgeToEdge)
                keyStore.save("infiniteScrollEnabled", model.infiniteScrollEnabled)
                keyStore.save("opaqueSystemBars", model.systemBarTheme)
                keyStore.save("showScores", model.showScores)
                keyStore.save("preferUserNicknames", model.preferUserNicknames)
                keyStore.save("commentBarThickness", model.commentBarThickness)
                keyStore.save("imageSourcePath", model.imageSourcePath)
                keyStore.save("fadeReadPosts", model.fadeReadPosts)
                keyStore.save(
                    "enableButtonsToScrollBetweenComments",
                    model.enableButtonsToScrollBetweenComments,
                )
                keyStore.save("fullWidthImages", model.fullWidthImages)
                keyStore.save("commentIndentAmount", model.commentIndentAmount)
                keyStore.save("defaultExploreResultType", model.defaultExploreResultType)
                keyStore.save("randomThemeColor", model.randomThemeColor)
                keyStore.save("openPostWebPageOnImageClick", model.openPostWebPageOnImageClick)
                keyStore.save(
                    "enableAlternateMarkdownRendering",
                    model.enableAlternateMarkdownRendering,
                )
            }
        }

    private fun List<ActionOnSwipe>.serialized(): String = map { it.toInt() }.joinToString(",")

    private fun createFake(
        id: Long = 0,
        theme: Long? = null,
        uiFontFamily: Long = 0,
        uiFontScale: Double = 1.0,
        contentFontScale: Double = 1.0,
        ancillaryFontScale: Double = 1.0,
        commentFontScale: Double = 1.0,
        titleFontScale: Double = 1.0,
        contentFontFamily: Long = 0,
        locale: String? = null,
        defaultListingType: Long = 2,
        defaultPostSortType: Long = 1,
        defaultInboxType: Long = 0,
        defaultCommentSortType: Long = 3,
        defaultExploreType: Long = 2,
        includeNsfw: Boolean = false,
        blurNsfw: Boolean = true,
        navigationTitlesVisible: Boolean = true,
        dynamicColors: Boolean = false,
        openUrlsInExternalBrowser: Boolean = true,
        enableSwipeActions: Boolean = true,
        enableDoubleTapAction: Boolean = false,
        customSeedColor: Long? = null,
        upVoteColor: Long? = null,
        downVoteColor: Long? = null,
        postLayout: Long = 0,
        fullHeightImages: Boolean = true,
        autoLoadImages: Boolean = true,
        autoExpandComments: Boolean = true,
        hideNavigationBarWhileScrolling: Boolean = true,
        zombieModeInterval: Long = 1,
        zombieModeScrollAmount: Double = 55.0,
        markAsReadWhileScrolling: Boolean = false,
        commentBarTheme: Long = 0,
        replyColor: Long? = null,
        saveColor: Long? = null,
        searchPostTitleOnly: Boolean = false,
        edgeToEdge: Boolean = true,
        postBodyMaxLines: Long? = null,
        infiniteScrollEnabled: Boolean = true,
        actionsOnSwipeToStartPosts: String = "",
        actionsOnSwipeToEndPosts: String = "",
        actionsOnSwipeToStartComments: String = "",
        actionsOnSwipeToEndComments: String = "",
        actionsOnSwipeToStartInbox: String = "",
        actionsOnSwipeToEndInbox: String = "",
        opaqueSystemBars: Boolean = false,
        showScores: Boolean = true,
        preferUserNicknames: Boolean = true,
        commentBarThickness: Long = 1,
        imageSourcePath: Boolean = false,
        separateUpAndDownVotes: Boolean = false,
        defaultLanguageId: Long? = null,
        inboxBackgroundCheckPeriod: Duration? = null,
        enableButtonsToScrollBetweenComments: Boolean = false,
        fadeReadPosts: Boolean = false,
        fullWidthImages: Boolean = false,
        showUnreadComments: Boolean = true,
        commentIndentAmount: Int = 2,
        enableToggleFavoriteInNavDrawer: Boolean = false,
        inboxPreviewMaxLines: Int? = null,
        defaultExploreResultType: Int = 2,
        useAvatarAsProfileNavigationIcon: Boolean = false,
        randomThemeColor: Boolean = true,
        openPostWebPageOnImageClick: Boolean = true,
        restrictLocalUserSearch: Boolean = false,
    ) = GetBy(
        id = id,
        theme = theme,
        uiFontFamily = uiFontFamily,
        uiFontScale = uiFontScale,
        contentFontScale = contentFontScale,
        contentFontFamily = contentFontFamily,
        locale = locale,
        defaultListingType = defaultListingType,
        defaultPostSortType = defaultPostSortType,
        defaultInboxType = defaultInboxType,
        defaultCommentSortType = defaultCommentSortType,
        defaultExploreType = defaultExploreType,
        includeNsfw = if (includeNsfw) 1 else 0,
        blurNsfw = if (blurNsfw) 1 else 0,
        navigationTitlesVisible = if (navigationTitlesVisible) 1 else 0,
        dynamicColors = if (dynamicColors) 1 else 0,
        openUrlsInExternalBrowser = if (openUrlsInExternalBrowser) 1 else 0,
        enableSwipeActions = if (enableSwipeActions) 1 else 0,
        enableDoubleTapAction = if (enableDoubleTapAction) 1 else 0,
        customSeedColor = customSeedColor,
        upvoteColor = upVoteColor,
        downvoteColor = downVoteColor,
        postLayout = postLayout,
        fullHeightImages = if (fullHeightImages) 1 else 0,
        autoLoadImages = if (autoLoadImages) 1 else 0,
        autoExpandComments = if (autoExpandComments) 1 else 0,
        hideNavigationBarWhileScrolling = if (hideNavigationBarWhileScrolling) 1 else 0,
        zombieModeInterval = zombieModeInterval,
        zombieModeScrollAmount = zombieModeScrollAmount,
        markAsReadWhileScrolling = if (markAsReadWhileScrolling) 1 else 0,
        commentBarTheme = commentBarTheme,
        replyColor = replyColor,
        saveColor = saveColor,
        searchPostTitleOnly = if (searchPostTitleOnly) 1 else 0,
        edgeToEdge = if (edgeToEdge) 1 else 0,
        postBodyMaxLines = postBodyMaxLines,
        infiniteScrollEnabled = if (infiniteScrollEnabled) 1 else 0,
        actionsOnSwipeToStartPosts = actionsOnSwipeToStartPosts,
        actionsOnSwipeToEndPosts = actionsOnSwipeToEndPosts,
        actionsOnSwipeToStartComments = actionsOnSwipeToStartComments,
        actionsOnSwipeToEndComments = actionsOnSwipeToEndComments,
        actionsOnSwipeToStartInbox = actionsOnSwipeToStartInbox,
        actionsOnSwipeToEndInbox = actionsOnSwipeToEndInbox,
        opaqueSystemBars = if (opaqueSystemBars) 1 else 0,
        showScores = if (showScores) 1 else 0,
        preferUserNicknames = if (preferUserNicknames) 1 else 0,
        commentBarThickness = commentBarThickness,
        imageSourcePath = if (imageSourcePath) 1 else 0,
        ancillaryFontScale = ancillaryFontScale,
        commentFontScale = commentFontScale,
        titleFontScale = titleFontScale,
        separateUpAndDownVotes = if (separateUpAndDownVotes) 1 else 0,
        defaultLanguageId = defaultLanguageId,
        inboxBackgroundCheckPeriod = inboxBackgroundCheckPeriod?.inWholeMilliseconds,
        enableButtonsToScrollBetweenComments = if (enableButtonsToScrollBetweenComments) 1 else 0,
        fadeReadPosts = if (fadeReadPosts) 1 else 0,
        fullWidthImages = if (fullWidthImages) 1 else 0,
        showUnreadComments = if (showUnreadComments) 1 else 0,
        commentIndentAmount = commentIndentAmount.toLong(),
        enableToggleFavoriteInNavDrawer = if (enableToggleFavoriteInNavDrawer) 1 else 0,
        inboxPreviewMaxLines = inboxPreviewMaxLines?.toLong(),
        defaultExploreResultType = defaultExploreResultType.toLong(),
        useAvatarAsProfileNavigationIcon = if (useAvatarAsProfileNavigationIcon) 1 else 0,
        randomThemeColor = if (randomThemeColor) 1 else 0,
        openPostWebPageOnImageClick = if (openPostWebPageOnImageClick) 1 else 0,
        restrictLocalUserSearch = if (restrictLocalUserSearch) 1 else 0,
    )
}
