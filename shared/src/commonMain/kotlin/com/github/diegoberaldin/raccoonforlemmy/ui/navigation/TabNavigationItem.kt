package com.github.diegoberaldin.raccoonforlemmy.ui.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import com.github.diegoberaldin.racconforlemmy.core_utils.onClick
import com.github.diegoberaldin.raccoonforlemmy.core_appearance.theme.Spacing

@Composable
internal fun RowScope.TabNavigationItem(tab: Tab) {
    val tabNavigator = LocalTabNavigator.current
    Column(
        modifier = Modifier.weight(1f)
            .onClick {
                tabNavigator.current = tab
            }
            .padding(top = Spacing.xxs)
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val color = if (tabNavigator.current == tab) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.outline
        }
        Icon(
            modifier = Modifier.size(30.dp),
            painter = tab.options.icon ?: rememberVectorPainter(Icons.Default.Home),
            contentDescription = null,
            tint = color,
        )
        Text(
            text = tab.options.title,
            style = MaterialTheme.typography.labelSmall,
            color = color,
        )
    }
}