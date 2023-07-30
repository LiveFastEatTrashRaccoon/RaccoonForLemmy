package com.github.diegoberaldin.raccoonforlemmy.feature_profile.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.github.diegoberaldin.raccoonforlemmy.core_appearance.theme.Spacing
import com.github.diegoberaldin.raccoonforlemmy.resources.MR
import dev.icerock.moko.resources.compose.stringResource

@Composable
internal fun ProfileLoggedContent(
    onLogout: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = Spacing.m),
        verticalArrangement = Arrangement.spacedBy(Spacing.xs)
    ) {
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