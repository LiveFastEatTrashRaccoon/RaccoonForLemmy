package com.github.diegoberaldin.raccoonforlemmy.feature_profile.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.github.diegoberaldin.raccoonforlemmy.core_appearance.theme.CornerSize
import com.github.diegoberaldin.raccoonforlemmy.core_appearance.theme.Spacing
import com.github.diegoberaldin.raccoonforlemmy.data.UserCounterModel
import com.github.diegoberaldin.raccoonforlemmy.data.UserModel
import com.github.diegoberaldin.raccoonforlemmy.resources.MR
import com.seiko.imageloader.rememberImagePainter
import dev.icerock.moko.resources.compose.stringResource

@Composable
internal fun ProfileLoggedContent(
    user: UserModel?,
    counters: UserCounterModel?,
    onLogout: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = Spacing.m),
        verticalArrangement = Arrangement.spacedBy(Spacing.xs)
    ) {
        val avatar = user?.avatar.orEmpty()
        if (avatar.isNotEmpty()) {
            val painter = rememberImagePainter(avatar)
            Image(
                modifier = Modifier.size(50.dp)
                    .clip(RoundedCornerShape(CornerSize.m)),
                painter = painter,
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
            )
        }
        val name = user?.name.orEmpty()
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = name,
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(Spacing.l))
        Button(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = {
                onLogout()
            },
        ) {
            Text(stringResource(MR.strings.profile_button_logout))
        }
    }
}