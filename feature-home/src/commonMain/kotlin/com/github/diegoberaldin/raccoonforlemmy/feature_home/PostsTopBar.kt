package com.github.diegoberaldin.raccoonforlemmy.feature_home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import com.github.diegoberaldin.racconforlemmy.core_utils.onClick
import com.github.diegoberaldin.raccoonforlemmy.core_appearance.theme.Spacing
import com.github.diegoberaldin.raccoonforlemmy.data.ListingType
import com.github.diegoberaldin.raccoonforlemmy.data.SortType
import com.github.diegoberaldin.raccoonforlemmy.resources.MR
import dev.icerock.moko.resources.compose.stringResource

@Composable
internal fun PostsTopBar(
    currentInstance: String,
    listingType: ListingType,
    sortType: SortType,
    onSelectListingType: () -> Unit,
    onSelectSortType: () -> Unit,
) {
    Row(
        modifier = Modifier.height(64.dp).padding(Spacing.s),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier = Modifier.onClick {
                onSelectListingType()
            },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.m),
        ) {
            Image(
                imageVector = listingType.toIcon(),
                contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(Spacing.xxxs)
            ) {
                Text(
                    text = listingType.toReadableName(),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = stringResource(
                        MR.strings.home_instance_via,
                        currentInstance,
                    ),
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Image(
            modifier = Modifier.onClick {
                onSelectSortType()
            },
            imageVector = sortType.toIcon(),
            contentDescription = null,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
        )
    }
}