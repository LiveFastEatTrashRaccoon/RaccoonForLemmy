package com.github.diegoberaldin.raccoonforlemmy.feature_home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.HeatPump
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material.icons.filled.Rocket
import androidx.compose.material.icons.filled.SpaceDashboard
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.github.diegoberaldin.raccoonforlemmy.core_appearance.theme.CornerSize
import com.github.diegoberaldin.raccoonforlemmy.core_appearance.theme.Spacing
import com.github.diegoberaldin.raccoonforlemmy.core_architecture.bindToLifecycle
import com.github.diegoberaldin.raccoonforlemmy.core_md.compose.Markdown
import com.github.diegoberaldin.raccoonforlemmy.data.ListingType
import com.github.diegoberaldin.raccoonforlemmy.data.SortType
import com.github.diegoberaldin.raccoonforlemmy.resources.MR
import dev.icerock.moko.resources.compose.stringResource

object HomeTab : Tab {

    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(MR.strings.navigation_home)
            val icon = rememberVectorPainter(Icons.Default.SpaceDashboard)

            return remember {
                TabOptions(
                    index = 0u,
                    title = title,
                    icon = icon
                )
            }
        }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val model = rememberScreenModel { getHomeScreenModel() }
        model.bindToLifecycle(key)

        val uiState by model.uiState.collectAsState()
        Scaffold(
            modifier = Modifier.padding(Spacing.xxs),
            topBar = {
                Row(
                    modifier = Modifier.padding(Spacing.s),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(Spacing.xxxs)
                    ) {
                        Text(
                            text = uiState.listingType.toReadableName(),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = stringResource(MR.strings.home_instance_via, uiState.instance),
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Image(
                        imageVector = when (uiState.sortType) {
                            SortType.Active -> Icons.Default.Rocket
                            SortType.Hot -> Icons.Default.HeatPump
                            SortType.MostComments -> Icons.Default.Message
                            SortType.New -> Icons.Default.Radar
                            SortType.NewComments -> Icons.Default.Message
                            else -> Icons.Default.AutoAwesome
                        },
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
                    )
                }
            }
        ) {
            LazyColumn(
                modifier = Modifier.padding(it),
                verticalArrangement = Arrangement.spacedBy(Spacing.xs)
            ) {
                items(uiState.posts) { post ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                shape = RoundedCornerShape(CornerSize.m)
                            ).padding(Spacing.s)
                    ) {
                        Column {
                            Text(
                                text = post.title,
                                style = MaterialTheme.typography.titleMedium,
                            )
                            val body = post.text
                            if (body.isNotEmpty()) {
                                Markdown(content = body)
                            }
                        }
                    }
                }
                item {
                    if (!uiState.loading && uiState.canFetchMore) {
                        model.reduce(HomeScreenMviModel.Intent.LoadNextPage)
                    }
                    if (uiState.loading) {
                        Box(
                            modifier = Modifier.fillMaxWidth().padding(Spacing.xs),
                            contentAlignment = Alignment.Center,
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(25.dp),
                                color = MaterialTheme.colorScheme.primary,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ListingType.toReadableName(): String = when (this) {
    ListingType.All -> stringResource(MR.strings.home_listing_type_all)
    ListingType.Local -> stringResource(MR.strings.home_listing_type_local)
    ListingType.Subscribed -> stringResource(MR.strings.home_listing_type_subscribed)
}
