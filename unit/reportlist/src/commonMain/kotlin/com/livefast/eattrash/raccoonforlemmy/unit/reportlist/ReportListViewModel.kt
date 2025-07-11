package com.livefast.eattrash.raccoonforlemmy.unit.reportlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.livefast.eattrash.raccoonforlemmy.core.appearance.repository.ThemeRepository
import com.livefast.eattrash.raccoonforlemmy.core.architecture.DefaultMviModelDelegate
import com.livefast.eattrash.raccoonforlemmy.core.architecture.MviModelDelegate
import com.livefast.eattrash.raccoonforlemmy.core.notifications.NotificationCenter
import com.livefast.eattrash.raccoonforlemmy.core.notifications.NotificationCenterEvent
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.SettingsRepository
import com.livefast.eattrash.raccoonforlemmy.core.utils.vibrate.HapticFeedback
import com.livefast.eattrash.raccoonforlemmy.domain.identity.repository.IdentityRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.CommentReportModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.PostReportModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.CommentRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.PostRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ReportListViewModel(
    private val communityId: Long,
    private val identityRepository: IdentityRepository,
    private val postRepository: PostRepository,
    private val commentRepository: CommentRepository,
    private val themeRepository: ThemeRepository,
    private val settingsRepository: SettingsRepository,
    private val hapticFeedback: HapticFeedback,
    private val notificationCenter: NotificationCenter,
) : ViewModel(),
    MviModelDelegate<ReportListMviModel.Intent, ReportListMviModel.UiState, ReportListMviModel.Effect>
    by DefaultMviModelDelegate(initialState = ReportListMviModel.UiState()),
    ReportListMviModel {
    private val currentPage = mutableMapOf<ReportListSection, Int>()

    init {
        viewModelScope.launch {
            themeRepository.postLayout
                .onEach { layout ->
                    updateState { it.copy(postLayout = layout) }
                }.launchIn(this)
            settingsRepository.currentSettings
                .onEach { settings ->
                    updateState {
                        it.copy(
                            autoLoadImages = settings.autoLoadImages,
                            preferNicknames = settings.preferUserNicknames,
                            swipeActionsEnabled = settings.enableSwipeActions,
                        )
                    }
                }.launchIn(this)
            notificationCenter
                .subscribe(NotificationCenterEvent.ChangeReportListType::class)
                .onEach { evt ->
                    changeUnresolvedOnly(evt.unresolvedOnly)
                }.launchIn(this)

            if (uiState.value.initial) {
                refresh(initial = true)
            }
        }
    }

    override fun reduce(intent: ReportListMviModel.Intent) {
        when (intent) {
            is ReportListMviModel.Intent.ChangeSection -> changeSection(intent.value)
            is ReportListMviModel.Intent.ChangeUnresolvedOnly -> changeUnresolvedOnly(intent.value)
            ReportListMviModel.Intent.Refresh ->
                viewModelScope.launch {
                    refresh()
                }

            ReportListMviModel.Intent.LoadNextPage ->
                viewModelScope.launch {
                    loadNextPage()
                }

            is ReportListMviModel.Intent.ResolveComment ->
                uiState.value.commentReports
                    .firstOrNull { it.id == intent.id }
                    ?.also {
                        resolve(it)
                    }

            is ReportListMviModel.Intent.ResolvePost ->
                uiState.value.postReports
                    .firstOrNull { it.id == intent.id }
                    ?.also {
                        resolve(it)
                    }

            ReportListMviModel.Intent.HapticIndication -> hapticFeedback.vibrate()
        }
    }

    private fun changeSection(section: ReportListSection) {
        viewModelScope.launch {
            updateState {
                it.copy(
                    section = section,
                )
            }
        }
    }

    private fun changeUnresolvedOnly(value: Boolean) {
        viewModelScope.launch {
            updateState {
                it.copy(unresolvedOnly = value)
            }
            emitEffect(ReportListMviModel.Effect.BackToTop)
            delay(50)
            refresh(initial = true)
        }
    }

    private suspend fun refresh(initial: Boolean = false) {
        currentPage[ReportListSection.Posts] = 1
        currentPage[ReportListSection.Comments] = 1
        updateState {
            it.copy(
                canFetchMore = true,
                refreshing = !initial,
                initial = initial,
                loading = false,
            )
        }
        loadNextPage()
    }

    private suspend fun loadNextPage() {
        val currentState = uiState.value
        if (!currentState.canFetchMore || currentState.loading) {
            updateState { it.copy(refreshing = false) }
            return
        }

        updateState { it.copy(loading = true) }
        val auth = identityRepository.authToken.value.orEmpty()
        val refreshing = currentState.refreshing
        val section = currentState.section
        val unresolvedOnly = currentState.unresolvedOnly
        if (section == ReportListSection.Posts) {
            val page = currentPage[ReportListSection.Posts] ?: 1
            coroutineScope {
                val itemList =
                    async {
                        postRepository.getReports(
                            auth = auth,
                            communityId = communityId.takeIf { it != 0L },
                            page = page,
                            unresolvedOnly = unresolvedOnly,
                        )
                    }.await()
                val commentReports =
                    async {
                        if (page == 1 && (currentState.commentReports.isEmpty() || refreshing)) {
                            // this is needed because otherwise on first selector change
                            // the lazy column scrolls back to top (it must have an empty data set)
                            commentRepository
                                .getReports(
                                    auth = auth,
                                    communityId = communityId.takeIf { it != 0L },
                                    page = 1,
                                    unresolvedOnly = unresolvedOnly,
                                ).orEmpty()
                        } else {
                            currentState.commentReports
                        }
                    }.await()
                updateState {
                    val postReports =
                        if (refreshing) {
                            itemList.orEmpty()
                        } else {
                            it.postReports + itemList.orEmpty()
                        }
                    it.copy(
                        postReports = postReports,
                        commentReports = commentReports,
                        loading = false,
                        canFetchMore = itemList?.isEmpty() != true,
                        refreshing = false,
                        initial = false,
                    )
                }
                if (!itemList.isNullOrEmpty()) {
                    currentPage[ReportListSection.Posts] = page + 1
                }
            }
        } else {
            val page = currentPage[ReportListSection.Comments] ?: 1
            val itemList =
                commentRepository.getReports(
                    auth = auth,
                    communityId = communityId.takeIf { it != 0L },
                    page = page,
                    unresolvedOnly = unresolvedOnly,
                )

            updateState {
                val commentReports =
                    if (refreshing) {
                        itemList.orEmpty()
                    } else {
                        it.commentReports + itemList.orEmpty()
                    }
                it.copy(
                    commentReports = commentReports,
                    loading = false,
                    canFetchMore = itemList?.isEmpty() != true,
                    refreshing = false,
                    initial = false,
                )
            }
            if (!itemList.isNullOrEmpty()) {
                currentPage[ReportListSection.Comments] = page + 1
            }
        }
    }

    private fun resolve(report: PostReportModel) {
        viewModelScope.launch {
            updateState { it.copy(asyncInProgress = true) }
            val auth = identityRepository.authToken.value.orEmpty()
            val newReport =
                postRepository.resolveReport(
                    reportId = report.id,
                    auth = auth,
                    resolved = !report.resolved,
                )
            updateState { it.copy(asyncInProgress = false) }
            if (newReport != null) {
                if (uiState.value.unresolvedOnly && newReport.resolved) {
                    handleReporDelete(newReport)
                } else {
                    handleReportUpdate(newReport)
                }
            }
        }
    }

    private fun resolve(report: CommentReportModel) {
        viewModelScope.launch {
            updateState { it.copy(asyncInProgress = true) }
            val auth = identityRepository.authToken.value.orEmpty()
            val newReport =
                commentRepository.resolveReport(
                    reportId = report.id,
                    auth = auth,
                    resolved = !report.resolved,
                )
            updateState { it.copy(asyncInProgress = false) }
            if (newReport != null) {
                if (uiState.value.unresolvedOnly && newReport.resolved) {
                    handleReporDelete(newReport)
                } else {
                    handleReportUpdate(newReport)
                }
            }
        }
    }

    private fun handleReportUpdate(report: PostReportModel) {
        viewModelScope.launch {
            updateState {
                it.copy(
                    postReports =
                    it.postReports.map { r ->
                        if (r.id == report.id) {
                            report
                        } else {
                            r
                        }
                    },
                )
            }
        }
    }

    private fun handleReportUpdate(report: CommentReportModel) {
        viewModelScope.launch {
            updateState {
                it.copy(
                    commentReports =
                    it.commentReports.map { r ->
                        if (r.id == report.id) {
                            report
                        } else {
                            r
                        }
                    },
                )
            }
        }
    }

    private fun handleReporDelete(report: PostReportModel) {
        viewModelScope.launch {
            updateState {
                it.copy(
                    postReports = it.postReports.filter { r -> r.id != report.id },
                )
            }
        }
    }

    private fun handleReporDelete(report: CommentReportModel) {
        viewModelScope.launch {
            updateState {
                it.copy(
                    commentReports = it.commentReports.filter { r -> r.id != report.id },
                )
            }
        }
    }
}
