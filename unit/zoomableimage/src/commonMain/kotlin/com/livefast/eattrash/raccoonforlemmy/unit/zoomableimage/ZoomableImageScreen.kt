package com.livefast.eattrash.raccoonforlemmy.unit.zoomableimage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AspectRatio
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.unit.DpOffset
import cafe.adriel.voyager.core.screen.Screen
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.CustomDropDown
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.VideoPlayer
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.ZoomableImage
import com.livefast.eattrash.raccoonforlemmy.core.commonui.modals.CustomModalBottomSheet
import com.livefast.eattrash.raccoonforlemmy.core.commonui.modals.CustomModalBottomSheetItem
import com.livefast.eattrash.raccoonforlemmy.core.l10n.messages.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.navigation.di.getDrawerCoordinator
import com.livefast.eattrash.raccoonforlemmy.core.navigation.di.getNavigationCoordinator
import com.livefast.eattrash.raccoonforlemmy.core.navigation.getScreenModel
import com.livefast.eattrash.raccoonforlemmy.core.notifications.NotificationCenterEvent
import com.livefast.eattrash.raccoonforlemmy.core.notifications.di.getNotificationCenter
import com.livefast.eattrash.raccoonforlemmy.core.utils.compose.onClick
import com.livefast.eattrash.raccoonforlemmy.core.utils.share.getShareHelper
import com.livefast.eattrash.raccoonforlemmy.core.utils.toLocalDp
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.core.parameter.parametersOf

class ZoomableImageScreen(
    private val url: String,
    private val source: String = "",
    private val isVideo: Boolean = false,
) : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val model = getScreenModel<ZoomableImageMviModel>(tag = url, parameters = { parametersOf(url) })
        val uiState by model.uiState.collectAsState()
        val snackbarHostState = remember { SnackbarHostState() }
        val successMessage = LocalStrings.current.messageOperationSuccessful
        val errorMessage = LocalStrings.current.messageGenericError
        val navigationCoordinator = remember { getNavigationCoordinator() }
        val drawerCoordinator = remember { getDrawerCoordinator() }
        val shareHelper = remember { getShareHelper() }
        val notificationCenter = remember { getNotificationCenter() }
        var imageShareBottomSheetOpened by remember { mutableStateOf(false) }

        LaunchedEffect(model) {
            model.effects
                .onEach {
                    when (it) {
                        ZoomableImageMviModel.Effect.ShareSuccess -> snackbarHostState.showSnackbar(successMessage)
                        ZoomableImageMviModel.Effect.ShareFailure -> snackbarHostState.showSnackbar(errorMessage)
                    }
                }.launchIn(this)
        }
        LaunchedEffect(key) {
            drawerCoordinator.setGesturesEnabled(false)
        }
        DisposableEffect(key) {
            onDispose {
                drawerCoordinator.setGesturesEnabled(true)
            }
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {},
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                navigationCoordinator.popScreen()
                            },
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                contentDescription = null,
                            )
                        }
                    },
                    actions = {
                        Icon(
                            modifier =
                                Modifier
                                    .padding(horizontal = Spacing.xs)
                                    .onClick(
                                        onClick = {
                                            model.reduce(
                                                ZoomableImageMviModel.Intent.SaveToGallery(source),
                                            )
                                        },
                                    ),
                            imageVector = Icons.Default.Download,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onBackground,
                        )
                        Icon(
                            modifier =
                                Modifier
                                    .padding(horizontal = Spacing.xs)
                                    .onClick(
                                        onClick = {
                                            if (shareHelper.supportsShareImage) {
                                                imageShareBottomSheetOpened = true
                                            } else {
                                                notificationCenter.send(
                                                    NotificationCenterEvent.ShareImageModeSelected.ModeUrl(url),
                                                )
                                            }
                                        },
                                    ),
                            imageVector = Icons.Default.Share,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onBackground,
                        )

                        // content scale option menu
                        Box {
                            val options =
                                buildList {
                                    this += ContentScale.Fit
                                    this += ContentScale.FillWidth
                                    this += ContentScale.FillHeight
                                }
                            var optionsExpanded by remember { mutableStateOf(false) }
                            var optionsOffset by remember { mutableStateOf(Offset.Zero) }
                            if (!isVideo) {
                                IconButton(
                                    modifier =
                                        Modifier
                                            .padding(horizontal = Spacing.xs)
                                            .onGloballyPositioned {
                                                optionsOffset = it.positionInParent()
                                            },
                                    onClick = {
                                        optionsExpanded = true
                                    },
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AspectRatio,
                                        contentDescription = null,
                                    )
                                }

                                CustomDropDown(
                                    expanded = optionsExpanded,
                                    onDismiss = {
                                        optionsExpanded = false
                                    },
                                    offset =
                                        DpOffset(
                                            x = optionsOffset.x.toLocalDp(),
                                            y = optionsOffset.y.toLocalDp(),
                                        ),
                                ) {
                                    options.forEach { option ->
                                        DropdownMenuItem(
                                            text = {
                                                val text =
                                                    when (option) {
                                                        ContentScale.FillHeight -> LocalStrings.current.contentScaleFillHeight
                                                        ContentScale.FillWidth -> LocalStrings.current.contentScaleFillWidth
                                                        else -> LocalStrings.current.contentScaleFit
                                                    }
                                                Text(text)
                                            },
                                            onClick = {
                                                optionsExpanded = false
                                                model.reduce(
                                                    ZoomableImageMviModel.Intent.ChangeContentScale(
                                                        option,
                                                    ),
                                                )
                                            },
                                        )
                                    }
                                }
                            }
                        }
                    },
                )
            },
            snackbarHost = {
                SnackbarHost(snackbarHostState) { data ->
                    Snackbar(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        snackbarData = data,
                    )
                }
            },
            content =
                { padding ->
                    Box(
                        modifier =
                            Modifier
                                .padding(top = padding.calculateTopPadding())
                                .fillMaxSize()
                                .background(Color.Black),
                        contentAlignment = Alignment.Center,
                    ) {
                        if (isVideo) {
                            VideoPlayer(
                                url = url,
                                muted = false,
                                contentScale = ContentScale.Fit,
                            )
                        } else {
                            ZoomableImage(
                                url = url,
                                autoLoadImages = uiState.autoLoadImages,
                                contentScale = uiState.contentScale,
                            )
                        }
                    }
                },
        )

        if (imageShareBottomSheetOpened) {
            val items =
                listOf(
                    LocalStrings.current.shareModeUrl,
                    LocalStrings.current.shareModeFile,
                )
            CustomModalBottomSheet(
                title = LocalStrings.current.postActionShare,
                items = items.map { CustomModalBottomSheetItem(label = it) },
                onSelected = { index ->
                    imageShareBottomSheetOpened = false
                    if (index != null) {
                        if (index == 0) {
                            notificationCenter.send(
                                NotificationCenterEvent.ShareImageModeSelected.ModeUrl(url),
                            )
                        } else {
                            notificationCenter.send(
                                NotificationCenterEvent.ShareImageModeSelected.ModeFile(
                                    url = url,
                                    source = source,
                                ),
                            )
                        }
                    }
                },
            )
        }
    }
}
