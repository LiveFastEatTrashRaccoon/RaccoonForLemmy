package com.livefast.eattrash.raccoonforlemmy.unit.userinfo

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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.livefast.eattrash.raccoonforlemmy.core.appearance.di.getThemeRepository
import com.livefast.eattrash.raccoonforlemmy.core.appearance.repository.ContentFontClass
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.ancillaryTextAlpha
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.toTypography
import com.livefast.eattrash.raccoonforlemmy.core.architecture.di.getViewModel
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.CustomizedContent
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.DetailInfoItem
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.PostCardBody
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.navigation.di.getMainRouter
import com.livefast.eattrash.raccoonforlemmy.core.navigation.di.getNavigationCoordinator
import com.livefast.eattrash.raccoonforlemmy.core.utils.compose.onClick
import com.livefast.eattrash.raccoonforlemmy.core.utils.datetime.prettifyDate
import com.livefast.eattrash.raccoonforlemmy.core.utils.getPrettyNumber
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.readableHandle
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.readableName
import com.livefast.eattrash.raccoonforlemmy.unit.userinfo.components.ModeratedCommunityCell
import com.livefast.eattrash.raccoonforlemmy.unit.userinfo.di.UserInfoMMviModelParams
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserInfoScreen(userId: Long, username: String, otherInstance: String, modifier: Modifier = Modifier) {
    val model: UserInfoMviModel =
        getViewModel<UserInfoViewModel>(
            UserInfoMMviModelParams(
                userId = userId,
                username = username,
                otherInstance = otherInstance,
            ),
        )
    val uiState by model.uiState.collectAsState()
    val navigationCoordinator = remember { getNavigationCoordinator() }
    val scope = rememberCoroutineScope()
    val mainRouter = remember { getMainRouter() }
    val themeRepository = remember { getThemeRepository() }
    val family by themeRepository.contentFontFamily.collectAsState()
    val typography = family.toTypography()

    Scaffold(
        modifier = modifier,
        contentColor = MaterialTheme.colorScheme.onBackground,
        containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp),
        topBar = {
            val title = uiState.user.readableName(uiState.preferNicknames)
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
                    Icon(
                        modifier =
                        Modifier.padding(end = Spacing.s).onClick(
                            onClick = {
                                navigationCoordinator.closeSideMenu()
                            },
                        ),
                        imageVector = Icons.Default.Close,
                        contentDescription = LocalStrings.current.actionCloseSideMenu,
                        tint = MaterialTheme.colorScheme.onBackground,
                    )
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
        ) {
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(Spacing.xs),
                ) {
                    SelectionContainer {
                        DetailInfoItem(
                            modifier = Modifier.fillMaxWidth(),
                            icon = Icons.Default.AlternateEmail,
                            title = uiState.user.readableHandle,
                        )
                    }
                    DetailInfoItem(
                        modifier = Modifier.fillMaxWidth(),
                        icon = Icons.Default.Cake,
                        title = uiState.user.accountAge.prettifyDate(),
                    )
                    uiState.user.score?.also { score ->
                        DetailInfoItem(
                            modifier = Modifier.fillMaxWidth(),
                            icon = Icons.AutoMirrored.Default.Article,
                            title = LocalStrings.current.communityInfoPosts,
                            value =
                            score.postScore.getPrettyNumber(
                                thousandLabel = LocalStrings.current.profileThousandShort,
                                millionLabel = LocalStrings.current.profileMillionShort,
                            ),
                        )
                        DetailInfoItem(
                            modifier = Modifier.fillMaxWidth(),
                            icon = Icons.AutoMirrored.Default.Reply,
                            title = LocalStrings.current.communityInfoComments,
                            value =
                            score.commentScore.getPrettyNumber(
                                thousandLabel = LocalStrings.current.profileThousandShort,
                                millionLabel = LocalStrings.current.profileMillionShort,
                            ),
                        )
                    }
                    if (uiState.isAdmin) {
                        DetailInfoItem(
                            modifier = Modifier.fillMaxWidth(),
                            icon = Icons.Default.Shield,
                            title = LocalStrings.current.userInfoAdmin,
                        )
                    }
                }
            }
            uiState.user.displayName.takeIf { it.isNotEmpty() }?.also { name ->
                item {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = name,
                        textAlign = TextAlign.Center,
                        style = typography.titleMedium,
                    )
                }
            }
            uiState.user.bio?.takeIf { it.isNotEmpty() }?.also { biography ->
                item {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(Spacing.xxs),
                    ) {
                        Text(
                            text = LocalStrings.current.settingsWebBio,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = ancillaryTextAlpha),
                        )
                        CustomizedContent(ContentFontClass.Body) {
                            PostCardBody(
                                modifier = Modifier.fillMaxWidth(),
                                text = biography,
                                onOpenImage = { url ->
                                    scope.launch {
                                        mainRouter.openImage(
                                            url = url,
                                            source = uiState.user.readableHandle,
                                        )
                                    }
                                },
                            )
                        }
                    }
                }
            }

            uiState.user.matrixUserId?.also { matrixUserId ->
                item {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(Spacing.xxs),
                    ) {
                        Text(
                            text = LocalStrings.current.settingsWebMatrix,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = ancillaryTextAlpha),
                        )
                        CustomizedContent(ContentFontClass.AncillaryText) {
                            SelectionContainer {
                                Text(
                                    text = matrixUserId,
                                    style =
                                    MaterialTheme.typography.bodyMedium.copy(
                                        fontFamily = FontFamily.Monospace,
                                    ),
                                    color = MaterialTheme.colorScheme.onBackground,
                                )
                            }
                        }
                    }
                }
            }

            if (uiState.moderatedCommunities.isNotEmpty()) {
                item {
                    Text(
                        modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(
                                top = Spacing.s,
                                bottom = Spacing.xs,
                            ),
                        text = LocalStrings.current.userInfoModerates,
                    )
                    LazyRow(
                        modifier = Modifier.padding(top = Spacing.xxs),
                        horizontalArrangement = Arrangement.spacedBy(Spacing.s),
                    ) {
                        items(
                            count = uiState.moderatedCommunities.size,
                        ) { idx ->
                            val community = uiState.moderatedCommunities[idx]
                            ModeratedCommunityCell(
                                autoLoadImages = uiState.autoLoadImages,
                                community = community,
                                onOpenCommunity = { _ ->
                                    mainRouter.openCommunityDetail(community)
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}
