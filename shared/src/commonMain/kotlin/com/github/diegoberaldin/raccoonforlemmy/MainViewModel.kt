package com.github.diegoberaldin.raccoonforlemmy

import cafe.adriel.voyager.core.model.ScreenModel
import com.github.diegoberaldin.raccoonforlemmy.core.architecture.DefaultMviModel
import com.github.diegoberaldin.raccoonforlemmy.core.architecture.MviModel
import com.github.diegoberaldin.raccoonforlemmy.domain.identity.repository.IdentityRepository
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MainViewModel(
    private val mvi: DefaultMviModel<MainScreenMviModel.Intent, MainScreenMviModel.UiState, MainScreenMviModel.Effect>,
    private val identityRepository: IdentityRepository,
    private val userRepository: UserRepository,
) : ScreenModel,
    MviModel<MainScreenMviModel.Intent, MainScreenMviModel.UiState, MainScreenMviModel.Effect> by mvi {

    @OptIn(FlowPreview::class)
    override fun onStarted() {
        mvi.onStarted()

        mvi.scope?.launch(Dispatchers.IO) {
            launch {
                identityRepository.authToken.debounce(250).onEach { auth ->
                    val unreadCount = if (!auth.isNullOrEmpty()) {
                        val mentionCount =
                            userRepository.getMentions(auth, page = 1, limit = 50).count()
                        val replyCount =
                            userRepository.getReplies(auth, page = 1, limit = 50).count()
                        mentionCount + replyCount
                    } else {
                        0
                    }
                    mvi.emitEffect(MainScreenMviModel.Effect.UnreadItemsDetected(unreadCount))
                }.launchIn(this)
            }

        }
    }

    override fun reduce(intent: MainScreenMviModel.Intent) {
        when (intent) {
            is MainScreenMviModel.Intent.SetBottomBarOffsetHeightPx -> {
                mvi.updateState { it.copy(bottomBarOffsetHeightPx = intent.value) }
            }
        }
    }
}