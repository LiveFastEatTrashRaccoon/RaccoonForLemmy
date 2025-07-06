package com.livefast.eattrash.raccoonforlemmy.feature.profile.notlogged

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.architecture.di.getViewModel
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.navigation.di.getNavigationCoordinator
import com.livefast.eattrash.raccoonforlemmy.unit.login.LoginScreen

@Composable
fun ProfileNotLoggedScreen(modifier: Modifier = Modifier) {
    val model: ProfileNotLoggedMviModel = getViewModel<ProfileNotLoggedViewModel>()
    val navigationCoordinator = remember { getNavigationCoordinator() }
    val uiState by model.uiState.collectAsState()

    Column(
        modifier = modifier.fillMaxSize().padding(horizontal = Spacing.m),
        verticalArrangement = Arrangement.spacedBy(Spacing.xs),
    ) {
        val message =
            if (uiState.authError) {
                LocalStrings.current.messageAuthIssue
            } else {
                LocalStrings.current.profileNotLoggedMessage
            }
        Text(text = message)

        Spacer(modifier = Modifier.height(Spacing.l))

        if (uiState.authError) {
            Button(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = {
                    model.reduce(ProfileNotLoggedMviModel.Intent.Retry)
                },
            ) {
                Text(LocalStrings.current.buttonRetry)
            }
        } else {
            Button(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = {
                    navigationCoordinator.pushScreen(LoginScreen())
                },
            ) {
                Text(LocalStrings.current.profileButtonLogin)
            }
        }
    }
}
