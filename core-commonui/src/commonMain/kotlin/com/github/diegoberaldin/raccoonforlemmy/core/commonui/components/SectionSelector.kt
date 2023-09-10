package com.github.diegoberaldin.raccoonforlemmy.core.commonui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SectionSelector(
    modifier: Modifier = Modifier,
    titles: List<String> = emptyList(),
    currentSection: Int,
    onSectionSelected: (Int) -> Unit,
) {
    TabRow(
        modifier = modifier,
        selectedTabIndex = currentSection,
        tabs = {
            titles.forEachIndexed { i, title ->
                Tab(
                    selected = i == currentSection,
                    text = {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    },
                    onClick = {
                        onSectionSelected(i)
                    },
                )
            }
        }
    )
}
