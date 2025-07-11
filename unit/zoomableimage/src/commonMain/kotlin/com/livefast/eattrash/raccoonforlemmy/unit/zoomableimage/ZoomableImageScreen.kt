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
import com.livefast.eattrash.raccoonforlemmy.core.architecture.di.getViewModel
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.CustomDropDown
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.ProgressHud
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.VideoPlayer
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.ZoomableImage
import com.livefast.eattrash.raccoonforlemmy.core.commonui.modals.CustomModalBottomSheet
import com.livefast.eattrash.raccoonforlemmy.core.commonui.modals.CustomModalBottomSheetItem
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.navigation.di.getDrawerCoordinator
import com.livefast.eattrash.raccoonforlemmy.core.navigation.di.getNavigationCoordinator
import com.livefast.eattrash.raccoonforlemmy.core.utils.di.getShareHelper
import com.livefast.eattrash.raccoonforlemmy.core.utils.toLocalDp
import com.livefast.eattrash.raccoonforlemmy.unit.zoomableimage.di.ZoomableImageMviModelParams
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ZoomableImageScreen(url: String, modifier: Modifier = Modifier, source: String = "", isVideo: Boolean = false) {
    val model: ZoomableImageMviModel = getViewModel<ZoomableImageViewModel>(ZoomableImageMviModelParams(url))
    val uiState by model.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val successMessage = LocalStrings.current.messageOperationSuccessful
    val errorMessage = LocalStrings.current.messageGenericError
    val navigationCoordinator = remember { getNavigationCoordinator() }
    val drawerCoordinator = remember { getDrawerCoordinator() }
    val shareHelper = remember { getShareHelper() }
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
    DisposableEffect(Unit) {
        drawerCoordinator.setGesturesEnabled(false)

        onDispose {
            drawerCoordinator.setGesturesEnabled(true)
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navigationCoordinator.pop()
                        },
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = LocalStrings.current.actionGoBack,
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            model.reduce(ZoomableImageMviModel.Intent.SaveToGallery(source))
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Default.Download,
                            contentDescription = LocalStrings.current.actionDownload,
                        )
                    }
                    IconButton(
                        onClick = {
                            if (shareHelper.supportsShareImage) {
                                imageShareBottomSheetOpened = true
                            } else {
                                model.reduce(
                                    ZoomableImageMviModel.Intent.ShareImageModeSelected.ModeUrl(
                                        url,
                                    ),
                                )
                            }
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = LocalStrings.current.postActionShare,
                        )
                    }

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
                                    .onGloballyPositioned {
                                        optionsOffset = it.positionInParent()
                                    },
                                onClick = {
                                    optionsExpanded = true
                                },
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AspectRatio,
                                    contentDescription = LocalStrings.current.actionChangeImageScaleMode,
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
                                                    ContentScale.FillHeight ->
                                                        LocalStrings.current.contentScaleFillHeight

                                                    ContentScale.FillWidth ->
                                                        LocalStrings.current.contentScaleFillWidth

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

    if (uiState.loading) {
        ProgressHud()
    }

    if (imageShareBottomSheetOpened) {
        val items =
            listOf(
                LocalStrings.current.shareModeUrl,
                LocalStrings.current.shareModeFile,
            )
        CustomModalBottomSheet(
            title = LocalStrings.current.postActionShare,
            items = items.map { CustomModalBottomSheetItem(label = it) },
            onSelect = { index ->
                imageShareBottomSheetOpened = false
                if (index != null) {
                    if (index == 0) {
                        model.reduce(
                            ZoomableImageMviModel.Intent.ShareImageModeSelected.ModeUrl(
                                url,
                            ),
                        )
                    } else {
                        model.reduce(
                            ZoomableImageMviModel.Intent.ShareImageModeSelected.ModeFile(
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
