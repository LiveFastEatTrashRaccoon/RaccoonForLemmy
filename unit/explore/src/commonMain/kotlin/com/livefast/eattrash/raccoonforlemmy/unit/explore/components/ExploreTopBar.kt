package com.livefast.eattrash.raccoonforlemmy.unit.explore.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.TopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.CornerSize
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.toWindowInsets
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.ListingType
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.SearchResultType
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.SortType
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.getAdditionalLabel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.toIcon
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.toReadableName

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ExploreTopBar(
    topAppBarState: TopAppBarState,
    listingType: ListingType,
    sortType: SortType,
    resultType: SearchResultType,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    otherInstance: String = "",
    onSelectListingType: (() -> Unit)? = null,
    onSelectSortType: (() -> Unit)? = null,
    onSelectResultTypeType: (() -> Unit)? = null,
    onTapHamburger: (() -> Unit)? = null,
    onBack: (() -> Unit)? = null,
) {
    TopAppBar(
        modifier = modifier,
        windowInsets = topAppBarState.toWindowInsets(),
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            when {
                otherInstance.isNotEmpty() -> {
                    IconButton(
                        onClick = {
                            onBack?.invoke()
                        },
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = LocalStrings.current.actionGoBack,
                        )
                    }
                }

                onTapHamburger != null -> {
                    IconButton(
                        onClick = {
                            onTapHamburger()
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = LocalStrings.current.actionOpenSideMenu,
                        )
                    }
                }

                else -> {
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
            }
        },
        title = {
            Column(
                modifier =
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(CornerSize.xl))
                    .then(
                        if (otherInstance.isEmpty()) {
                            Modifier.clickable {
                                onSelectListingType?.invoke()
                            }
                        } else {
                            Modifier
                        },
                    ).padding(horizontal = Spacing.s),
            ) {
                Text(
                    text =
                    buildString {
                        append(LocalStrings.current.navigationSearch)
                        if (otherInstance.isNotEmpty()) {
                            append(" (")
                            append(otherInstance)
                            append(")")
                        }
                    },
                    style = MaterialTheme.typography.titleMedium,
                )
                if (otherInstance.isEmpty()) {
                    Text(
                        text = listingType.toReadableName(),
                        style = MaterialTheme.typography.titleSmall,
                    )
                }
            }
        },
        actions = {
            IconButton(
                onClick = {
                    onSelectResultTypeType?.invoke()
                },
            ) {
                Icon(
                    imageVector = resultType.toIcon(),
                    contentDescription = resultType.toReadableName(),
                )
            }

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

            IconButton(
                onClick = {
                    onSelectSortType?.invoke()
                },
            ) {
                Icon(
                    imageVector = sortType.toIcon(),
                    contentDescription = sortType.toReadableName(),
                )
            }
        },
    )
}
