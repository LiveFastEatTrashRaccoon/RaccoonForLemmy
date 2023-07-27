package com.github.diegoberaldin.raccoonforlemmy.feature_home.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import com.github.diegoberaldin.raccoonforlemmy.core_appearance.theme.Spacing
import com.github.diegoberaldin.raccoonforlemmy.data.PostModel

@Composable
internal fun PostCardFooter(post: PostModel) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.xxs),
    ) {
        val buttonModifier = Modifier.size(42.dp)
        Image(
            modifier = buttonModifier,
            imageVector = Icons.Default.ArrowDropUp,
            contentDescription = null,
            colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onSurface)
        )
        Text(
            text = "${post.score}"
        )
        Image(
            modifier = buttonModifier,
            imageVector = Icons.Default.ArrowDropDown,
            contentDescription = null,
            colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onSurface)
        )
        Spacer(modifier = Modifier.weight(1f))
        Image(
            modifier = buttonModifier.padding(10.dp),
            imageVector = Icons.Default.Chat,
            contentDescription = null,
            colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onSurface)
        )
        Text(
            modifier = Modifier.padding(end = Spacing.s),
            text = "${post.comments}"
        )
    }
}