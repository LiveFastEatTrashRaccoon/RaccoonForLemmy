package com.livefast.eattrash.raccoonforlemmy.unit.postlist.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.TopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.CornerSize
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Dimensions
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.IconSize
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.l10n.messages.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.utils.toLocalPixel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.ListingType
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.SortType
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.getAdditionalLabel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.toIcon
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.toReadableName
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PostsTopBar(
    topAppBarState: TopAppBarState,
    currentInstance: String,
    listingType: ListingType?,
    sortType: SortType?,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    edgeToEdge: Boolean = true,
    onSelectListingType: (() -> Unit)? = null,
    onSelectInstance: (() -> Unit)? = null,
    onSelectSortType: (() -> Unit)? = null,
    onHamburgerTapped: (() -> Unit)? = null,
) {
    val scope = rememberCoroutineScope()
    val maxTopInset = Dimensions.maxTopBarInset.toLocalPixel()
    var topInset by remember { mutableStateOf(maxTopInset) }
    snapshotFlow { topAppBarState.collapsedFraction }
        .onEach {
            topInset = maxTopInset * (1 - it)
        }.launchIn(scope)

    TopAppBar(
        windowInsets =
            if (edgeToEdge) {
                WindowInsets(0, topInset.roundToInt(), 0, 0)
            } else {
                TopAppBarDefaults.windowInsets
            },
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            when {
                onHamburgerTapped != null -> {
                    IconButton(
                        onClick = {
                            onHamburgerTapped()
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = null,
                        )
                    }
                }

                listingType != null -> {
                    IconButton(
                        onClick = {
                            onSelectListingType?.invoke()
                        },
                    ) {
                        Icon(
                            imageVector = listingType.toIcon(),
                            contentDescription = null,
                        )
                    }
                }

                else -> {
                    Box(modifier = Modifier.size(IconSize.m))
                }
            }
        },
        title = {
            Column {
                Text(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(CornerSize.xl))
                            .clickable {
                                onSelectListingType?.invoke()
                            }.padding(horizontal = Spacing.s),
                    text = listingType?.toReadableName().orEmpty(),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Text(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(CornerSize.xl))
                            .clickable {
                                if (onSelectInstance != null) {
                                    onSelectInstance.invoke()
                                } else {
                                    onSelectListingType?.invoke()
                                }
                            }.padding(horizontal = Spacing.s),
                    text =
                        buildString {
                            append(LocalStrings.current.homeInstanceVia)
                            append(" ")
                            append(currentInstance)
                        },
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }
        },
        actions = {
            val additionalLabel = sortType.getAdditionalLabel()
            if (additionalLabel.isNotEmpty()) {
                Text(
                    text =
                        buildString {
                            append("(")
                            append(additionalLabel)
                            append(")")
                        },
                )
                Spacer(modifier = Modifier.width(Spacing.xs))
            }
            if (sortType != null) {
                IconButton(
                    onClick = {
                        onSelectSortType?.invoke()
                    },
                ) {
                    Icon(
                        imageVector = sortType.toIcon(),
                        contentDescription = null,
                    )
                }
            }
        },
    )
}
