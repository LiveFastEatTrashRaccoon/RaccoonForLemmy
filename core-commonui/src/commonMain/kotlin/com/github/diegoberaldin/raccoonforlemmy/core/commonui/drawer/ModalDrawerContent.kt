package com.github.diegoberaldin.raccoonforlemmy.core.commonui.drawer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.data.FontScale
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.data.scaleFactor
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.Spacing
import com.github.diegoberaldin.raccoonforlemmy.core.architecture.bindToLifecycle
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.components.CommunityItem
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.components.CustomImage
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.components.MultiCommunityItem
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.di.getDrawerCoordinator
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.di.getModalDrawerViewModel
import com.github.diegoberaldin.raccoonforlemmy.core.utils.onClick
import com.github.diegoberaldin.raccoonforlemmy.resources.MR
import dev.icerock.moko.resources.compose.localized
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

object ModalDrawerContent : Tab {

    override val options: TabOptions
        @Composable get() {
            return TabOptions(0u, "")
        }

    @OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val model = rememberScreenModel { getModalDrawerViewModel() }
        model.bindToLifecycle(key)
        val uiState by model.uiState.collectAsState()
        val coordinator = remember { getDrawerCoordinator() }
        val scope = rememberCoroutineScope()
        var changeInstanceDialogOpen by remember {
            mutableStateOf(false)
        }
        LaunchedEffect(model) {
            model.effects.onEach { evt ->
                when (evt) {
                    ModalDrawerMviModel.Effect.CloseChangeInstanceDialog -> {
                        changeInstanceDialogOpen = false
                    }
                }
            }.launchIn(this)
        }

        Column(
            modifier = Modifier.fillMaxWidth(0.75f)
        ) {
            // header
            val user = uiState.user
            val avatarSize = 52.dp
            Row(
                modifier = Modifier.padding(
                    top = Spacing.m,
                    start = Spacing.s,
                    end = Spacing.s,
                    bottom = Spacing.s,
                ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Spacing.m)
            ) {
                if (user != null) {
                    // avatar
                    val userAvatar = user.avatar.orEmpty()
                    if (userAvatar.isNotEmpty()) {
                        CustomImage(
                            modifier = Modifier.padding(Spacing.xxxs).size(avatarSize)
                                .clip(RoundedCornerShape(avatarSize / 2)),
                            url = userAvatar,
                            quality = FilterQuality.Low,
                            contentDescription = null,
                            contentScale = ContentScale.FillBounds,
                        )
                    } else {
                        Box(
                            modifier = Modifier.padding(Spacing.xxxs).size(avatarSize).background(
                                color = MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(avatarSize / 2),
                            ),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = user.name.firstOrNull()?.toString().orEmpty().uppercase(),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onBackground,
                            )
                        }
                    }

                    Column(
                        verticalArrangement = Arrangement.spacedBy(Spacing.xxxs),
                    ) {
                        Text(
                            text = buildString {
                                if (user.displayName.isNotEmpty()) {
                                    append(user.displayName)
                                } else {
                                    append(user.name)
                                }
                            },
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                        Text(
                            text = buildString {
                                append(user.name)
                                append("@")
                                append(user.host)
                            },
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                    }
                } else {
                    val anonymousTitle = stringResource(MR.strings.navigation_drawer_anonymous)
                    Box(
                        modifier = Modifier.padding(Spacing.xxxs).size(avatarSize).background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(avatarSize / 2),
                        ),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = anonymousTitle.firstOrNull()?.toString().orEmpty().uppercase(),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onPrimary,
                        )
                    }
                    Column(
                        verticalArrangement = Arrangement.spacedBy(Spacing.xxxs),
                    ) {
                        Text(
                            text = anonymousTitle,
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                        Row {
                            Text(
                                text = uiState.instance.orEmpty(),
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.onBackground,
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Icon(
                                modifier = Modifier.onClick {
                                    changeInstanceDialogOpen = true
                                },
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = null,
                            )
                        }
                    }
                }
            }

            Divider(
                modifier = Modifier.padding(
                    top = Spacing.m,
                    bottom = Spacing.s,
                )
            )

            val pullRefreshState = rememberPullRefreshState(uiState.refreshing, {
                model.reduce(ModalDrawerMviModel.Intent.Refresh)
            })
            Box(
                modifier = Modifier.weight(1f).pullRefresh(pullRefreshState),
            ) {
                CompositionLocalProvider(
                    LocalDensity provides Density(
                        density = LocalDensity.current.density,
                        fontScale = FontScale.Small.scaleFactor,
                    ),
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(Spacing.xxs),
                    ) {
                        if (user != null) {
                            val additionalIconSize = 18.dp
                            item {
                                Row(
                                    modifier = Modifier.fillMaxWidth()
                                        .padding(
                                            horizontal = Spacing.s,
                                            vertical = Spacing.xs,
                                        )
                                        .onClick {
                                            scope.launch {
                                                coordinator.toggleDrawer()
                                                coordinator.sendEvent(DrawerEvent.ManageSubscriptions)
                                            }
                                        },
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Text(
                                        text = stringResource(MR.strings.navigation_drawer_title_subscriptions),
                                        style = MaterialTheme.typography.titleLarge,
                                        color = MaterialTheme.colorScheme.onBackground,
                                    )
                                    Spacer(modifier = Modifier.weight(1f))
                                    Icon(
                                        modifier = Modifier.size(additionalIconSize),
                                        imageVector = Icons.Default.ManageAccounts,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onBackground,
                                    )
                                }
                            }
                            item {
                                Row(
                                    modifier = Modifier.fillMaxWidth()
                                        .padding(
                                            horizontal = Spacing.s,
                                            vertical = Spacing.xs,
                                        )
                                        .onClick {
                                            scope.launch {
                                                coordinator.toggleDrawer()
                                                coordinator.sendEvent(DrawerEvent.OpenBookmarks)
                                            }
                                        },
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Text(
                                        text = stringResource(MR.strings.navigation_drawer_title_bookmarks),
                                        style = MaterialTheme.typography.titleLarge,
                                        color = MaterialTheme.colorScheme.onBackground,
                                    )
                                    Spacer(modifier = Modifier.weight(1f))
                                    Icon(
                                        modifier = Modifier.size(additionalIconSize),
                                        imageVector = Icons.Default.Bookmarks,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onBackground,
                                    )
                                }
                            }
                        }

                        itemsIndexed(uiState.multiCommunities) { _, community ->
                            MultiCommunityItem(
                                modifier = Modifier.fillMaxWidth().onClick {
                                    scope.launch {
                                        coordinator.toggleDrawer()
                                        coordinator.sendEvent(
                                            DrawerEvent.OpenMultiCommunity(community),
                                        )
                                    }
                                },
                                community = community,
                                small = true,
                            )
                        }
                        itemsIndexed(uiState.communities) { _, community ->
                            CommunityItem(
                                modifier = Modifier.fillMaxWidth().onClick {
                                    scope.launch {
                                        coordinator.toggleDrawer()
                                        coordinator.sendEvent(
                                            DrawerEvent.OpenCommunity(community),
                                        )
                                    }
                                },
                                community = community,
                                small = true,
                            )
                        }
                    }
                }
                PullRefreshIndicator(
                    refreshing = uiState.refreshing,
                    state = pullRefreshState,
                    modifier = Modifier.align(Alignment.TopCenter),
                    backgroundColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onBackground,
                )
            }
        }

        if (changeInstanceDialogOpen) {
            AlertDialog(
                onDismissRequest = {
                    changeInstanceDialogOpen = false
                },
            ) {
                Column(
                    modifier = Modifier
                        .background(color = MaterialTheme.colorScheme.surface)
                        .padding(Spacing.s),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(Spacing.xxs),
                ) {
                    Text(
                        text = stringResource(MR.strings.dialog_title_change_instance),
                        style = MaterialTheme.typography.titleLarge
                    )
                    TextField(
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                        ),
                        label = {
                            Text(text = stringResource(MR.strings.login_field_instance_name))
                        },
                        singleLine = true,
                        value = uiState.changeInstanceName,
                        isError = uiState.changeInstanceNameError != null,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            autoCorrect = false,
                            imeAction = ImeAction.Next,
                        ),
                        onValueChange = { value ->
                            model.reduce(ModalDrawerMviModel.Intent.ChangeInstanceName(value))
                        },
                        supportingText = {
                            if (uiState.changeInstanceNameError != null) {
                                Text(
                                    text = uiState.changeInstanceNameError?.localized().orEmpty(),
                                    color = MaterialTheme.colorScheme.error,
                                )
                            }
                        },
                    )

                    Button(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        onClick = {
                            model.reduce(ModalDrawerMviModel.Intent.SubmitChangeInstance)
                        },
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(Spacing.s),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            if (uiState.changeInstanceloading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = MaterialTheme.colorScheme.onPrimary,
                                )
                            }
                            Text(stringResource(MR.strings.button_confirm))
                        }
                    }
                }
            }
        }
    }
}