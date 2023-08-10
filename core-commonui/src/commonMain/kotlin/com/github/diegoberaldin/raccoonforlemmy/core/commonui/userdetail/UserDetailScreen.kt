package com.github.diegoberaldin.raccoonforlemmy.core.commonui.userdetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import com.github.diegoberaldin.racconforlemmy.core.utils.onClick
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.Spacing
import com.github.diegoberaldin.raccoonforlemmy.core.architecture.bindToLifecycle
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.di.getUserDetailScreenViewModel
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.userdetail.comments.UserDetailCommentsScreen
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.userdetail.posts.UserDetailPostsScreen
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.UserModel

class UserDetailScreen(
    private val user: UserModel,
    private val onBack: () -> Unit,
) : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        Scaffold(
            modifier = Modifier.background(MaterialTheme.colorScheme.surface).padding(Spacing.xs),
            topBar =
            {
                val communityName = user.name
                val communityHost = user.host
                TopAppBar(
                    title = {
                        Text(
                            modifier = Modifier.padding(horizontal = Spacing.s),
                            text = buildString {
                                append(communityName)
                                if (communityHost.isNotEmpty()) {
                                    append("@$communityHost")
                                }
                            },
                        )
                    },
                    navigationIcon = {
                        Image(
                            modifier = Modifier.onClick {
                                onBack()
                            },
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                        )
                    },
                )
            },
        ) { padding ->
            Column(
                modifier = Modifier.padding(padding).fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(Spacing.s),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                val model =
                    rememberScreenModel(user.id.toString()) { getUserDetailScreenViewModel(user) }
                model.bindToLifecycle(key)
                val uiState by model.uiState.collectAsState()

                when (uiState.currentTab) {
                    UserDetailSection.POSTS -> {
                        UserDetailPostsScreen(
                            modifier = Modifier.weight(1f).fillMaxWidth(),
                            user = user,
                            onSectionSelected = {
                                model.reduce(UserDetailMviModel.Intent.SelectTab(it))
                            },
                        ).Content()
                    }

                    UserDetailSection.COMMENTS -> {
                        UserDetailCommentsScreen(
                            modifier = Modifier.weight(1f).fillMaxWidth(),
                            user = user,
                            onSectionSelected = {
                                model.reduce(UserDetailMviModel.Intent.SelectTab(it))
                            },
                        ).Content()
                    }
                }
            }
        }
    }
}
