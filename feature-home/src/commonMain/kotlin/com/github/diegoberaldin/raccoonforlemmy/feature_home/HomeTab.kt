package com.github.diegoberaldin.raccoonforlemmy.feature_home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SpaceDashboard
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.github.diegoberaldin.raccoonforlemmy.core_appearance.theme.CornerSize
import com.github.diegoberaldin.raccoonforlemmy.core_appearance.theme.Spacing
import com.github.diegoberaldin.raccoonforlemmy.core_architecture.bindToLifecycle
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

    @Composable
    override fun Content() {
        val model = rememberScreenModel { getHomeScreenModel() }
        model.bindToLifecycle(key)

        val uiState by model.uiState.collectAsState()
        Column(modifier = Modifier.padding(Spacing.xs)) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(Spacing.xxs)
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
                                Text(
                                    text = body,
                                    style = MaterialTheme.typography.bodyMedium,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
