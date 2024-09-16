package com.livefast.eattrash.raccoonforlemmy.unit.communityinfo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.automirrored.filled.Reply
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.CalendarViewDay
import androidx.compose.material.icons.filled.CalendarViewMonth
import androidx.compose.material.icons.filled.CalendarViewWeek
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Group
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.livefast.eattrash.raccoonforlemmy.core.appearance.repository.ContentFontClass
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.CustomizedContent
import com.livefast.eattrash.raccoonforlemmy.core.commonui.detailopener.api.getDetailOpener
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.DetailInfoItem
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.PostCardBody
import com.livefast.eattrash.raccoonforlemmy.core.l10n.messages.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.navigation.di.getNavigationCoordinator
import com.livefast.eattrash.raccoonforlemmy.core.navigation.getScreenModel
import com.livefast.eattrash.raccoonforlemmy.core.utils.datetime.prettifyDate
import com.livefast.eattrash.raccoonforlemmy.core.utils.getPrettyNumber
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.readableHandle
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.readableName
import com.livefast.eattrash.raccoonforlemmy.unit.communityinfo.components.ModeratorCell
import com.livefast.eattrash.raccoonforlemmy.unit.zoomableimage.ZoomableImageScreen
import org.koin.core.parameter.parametersOf

class CommunityInfoScreen(
    private val communityId: Long,
    private val communityName: String = "",
    private val otherInstance: String = "",
) : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val model =
            getScreenModel<CommunityInfoMviModel>(
                tag = communityId.toString(),
                parameters = { parametersOf(communityId, communityName, otherInstance) },
            )
        val uiState by model.uiState.collectAsState()
        val navigationCoordinator = remember { getNavigationCoordinator() }
        val detailOpener = remember { getDetailOpener() }

        Scaffold(
            contentColor = MaterialTheme.colorScheme.onBackground,
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp),
            topBar = {
                val title = uiState.community.readableName(uiState.preferNicknames)
                TopAppBar(
                    colors =
                        TopAppBarDefaults.topAppBarColors().copy(
                            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp),
                        ),
                    title = {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                navigationCoordinator.closeSideMenu()
                            },
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = null,
                            )
                        }
                    },
                )
            },
        ) { padding ->
            LazyColumn(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(
                            top = padding.calculateTopPadding(),
                            start = Spacing.m,
                            end = Spacing.m,
                        ),
                verticalArrangement = Arrangement.spacedBy(Spacing.s),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                item {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(Spacing.xs),
                    ) {
                        SelectionContainer {
                            DetailInfoItem(
                                modifier = Modifier.fillMaxWidth(),
                                icon = Icons.Default.AlternateEmail,
                                title = uiState.community.readableHandle,
                            )
                        }
                        DetailInfoItem(
                            modifier = Modifier.fillMaxWidth(),
                            icon = Icons.Default.Cake,
                            title =
                                uiState.community.creationDate
                                    ?.prettifyDate()
                                    .orEmpty(),
                        )
                        DetailInfoItem(
                            modifier = Modifier.fillMaxWidth(),
                            icon = Icons.AutoMirrored.Default.Article,
                            title = LocalStrings.current.communityInfoPosts,
                            value =
                                uiState.community.posts.getPrettyNumber(
                                    thousandLabel = LocalStrings.current.profileThousandShort,
                                    millionLabel = LocalStrings.current.profileMillionShort,
                                ),
                        )
                        DetailInfoItem(
                            modifier = Modifier.fillMaxWidth(),
                            icon = Icons.AutoMirrored.Default.Reply,
                            title = LocalStrings.current.communityInfoComments,
                            value =
                                uiState.community.comments.getPrettyNumber(
                                    thousandLabel = LocalStrings.current.profileThousandShort,
                                    millionLabel = LocalStrings.current.profileMillionShort,
                                ),
                        )
                        DetailInfoItem(
                            modifier = Modifier.fillMaxWidth(),
                            icon = Icons.Default.Group,
                            title = LocalStrings.current.communityInfoSubscribers,
                            value =
                                uiState.community.subscribers.getPrettyNumber(
                                    thousandLabel = LocalStrings.current.profileThousandShort,
                                    millionLabel = LocalStrings.current.profileMillionShort,
                                ),
                        )
                        DetailInfoItem(
                            modifier = Modifier.fillMaxWidth(),
                            icon = Icons.Default.CalendarViewMonth,
                            title = LocalStrings.current.communityInfoMonthlyActiveUsers,
                            value =
                                uiState.community.monthlyActiveUsers.getPrettyNumber(
                                    thousandLabel = LocalStrings.current.profileThousandShort,
                                    millionLabel = LocalStrings.current.profileMillionShort,
                                ),
                        )
                        DetailInfoItem(
                            modifier = Modifier.fillMaxWidth(),
                            icon = Icons.Default.CalendarViewWeek,
                            title = LocalStrings.current.communityInfoWeeklyActiveUsers,
                            value =
                                uiState.community.weeklyActiveUsers.getPrettyNumber(
                                    thousandLabel = LocalStrings.current.profileThousandShort,
                                    millionLabel = LocalStrings.current.profileMillionShort,
                                ),
                        )
                        DetailInfoItem(
                            modifier = Modifier.fillMaxWidth(),
                            icon = Icons.Default.CalendarViewDay,
                            title = LocalStrings.current.communityInfoDailyActiveUsers,
                            value =
                                uiState.community.dailyActiveUsers.getPrettyNumber(
                                    thousandLabel = LocalStrings.current.profileThousandShort,
                                    millionLabel = LocalStrings.current.profileMillionShort,
                                ),
                        )
                    }
                }
                if (uiState.moderators.isNotEmpty()) {
                    item {
                        Text(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        top = Spacing.s,
                                        bottom = Spacing.xs,
                                    ),
                            text = LocalStrings.current.communityInfoModerators,
                        )
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(Spacing.s),
                        ) {
                            items(
                                count = uiState.moderators.size,
                            ) { idx ->
                                val user = uiState.moderators[idx]
                                ModeratorCell(
                                    autoLoadImages = uiState.autoLoadImages,
                                    user = user,
                                    onOpenUser = { _ ->
                                        detailOpener.openUserDetail(user, "")
                                    },
                                )
                            }
                        }
                    }
                }
                item {
                    CustomizedContent(ContentFontClass.Body) {
                        PostCardBody(
                            modifier = Modifier.fillMaxWidth().padding(top = Spacing.m),
                            text = uiState.community.description,
                            onOpenImage = { url ->
                                navigationCoordinator.pushScreen(
                                    ZoomableImageScreen(
                                        url = url,
                                        source = uiState.community.readableHandle,
                                    ),
                                )
                            },
                        )
                    }
                }
            }
        }
    }
}
