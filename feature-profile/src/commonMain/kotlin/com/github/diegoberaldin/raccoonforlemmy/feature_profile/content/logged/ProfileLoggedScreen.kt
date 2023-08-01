package com.github.diegoberaldin.raccoonforlemmy.feature_profile.content.logged

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import com.github.diegoberaldin.raccoonforlemmy.core_appearance.theme.Spacing
import com.github.diegoberaldin.raccoonforlemmy.core_architecture.bindToLifecycle
import com.github.diegoberaldin.raccoonforlemmy.domain_lemmy.data.UserModel
import com.github.diegoberaldin.raccoonforlemmy.feature_profile.content.logged.posts.ProfilePostsScreen
import com.github.diegoberaldin.raccoonforlemmy.feature_profile.di.getProfileLoggedViewModel

internal class ProfileLoggedScreen(
    private val user: UserModel,
) : Screen {

    @Composable
    override fun Content() {
        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = Spacing.m),
            verticalArrangement = Arrangement.spacedBy(Spacing.s),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val model = rememberScreenModel { getProfileLoggedViewModel() }
            model.bindToLifecycle(key)
            val uiState by model.uiState.collectAsState()

            ProfileLoggedHeader(user = user)
            ProfileLoggedCounters(user = user)

            Spacer(modifier = Modifier.height(Spacing.s))

            SectionSelector(
                currentSection = uiState.currentTab,
                onSectionSelected = {
                    model.reduce(ProfileLoggedMviModel.Intent.SelectTab(it))
                },
            )
            when (uiState.currentTab) {
                ProfileLoggedSection.POSTS -> {
                    ProfilePostsScreen(
                        modifier = Modifier.weight(1f).fillMaxWidth(),
                        user = user,
                    ).Content()
                }

                ProfileLoggedSection.COMMENTS -> {
                }

                ProfileLoggedSection.SAVED -> {
                }
            }
        }
    }
}
