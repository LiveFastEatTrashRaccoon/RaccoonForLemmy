package com.github.diegoberaldin.raccoonforlemmy.unit.manageban

import cafe.adriel.voyager.core.model.screenModelScope
import com.github.diegoberaldin.raccoonforlemmy.core.architecture.DefaultMviModel
import com.github.diegoberaldin.raccoonforlemmy.core.persistence.repository.AccountRepository
import com.github.diegoberaldin.raccoonforlemmy.core.persistence.repository.DomainBlocklistRepository
import com.github.diegoberaldin.raccoonforlemmy.core.persistence.repository.SettingsRepository
import com.github.diegoberaldin.raccoonforlemmy.core.persistence.repository.StopWordRepository
import com.github.diegoberaldin.raccoonforlemmy.domain.identity.repository.IdentityRepository
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.AccountBansModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.CommunityModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.InstanceModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.UserModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.readableHandle
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.repository.CommunityRepository
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.repository.SiteRepository
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.repository.UserRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class ManageBanViewModel(
    private val identityRepository: IdentityRepository,
    private val accountRepository: AccountRepository,
    private val siteRepository: SiteRepository,
    private val settingsRepository: SettingsRepository,
    private val userRepository: UserRepository,
    private val communityRepository: CommunityRepository,
    private val blocklistRepository: DomainBlocklistRepository,
    private val stopWordRepository: StopWordRepository,
) : DefaultMviModel<ManageBanMviModel.Intent, ManageBanMviModel.UiState, ManageBanMviModel.Effect>(
        initialState = ManageBanMviModel.UiState(),
    ),
    ManageBanMviModel {
    private var originalBans: AccountBansModel? = null
    private var originalBlockedDomains: List<String> = emptyList()
    private var originalStopWords: List<String> = emptyList()

    init {
        screenModelScope.launch {
            settingsRepository.currentSettings
                .onEach { settings ->
                    updateState {
                        it.copy(
                            autoLoadImages = settings.autoLoadImages,
                            preferNicknames = settings.preferUserNicknames,
                        )
                    }
                }.launchIn(this)

            uiState
                .map { it.searchText }
                .distinctUntilChanged()
                .drop(1)
                .debounce(1_000)
                .onEach { query ->
                    if (!uiState.value.initial) {
                        emitEffect(ManageBanMviModel.Effect.BackToTop)
                        delay(50)
                        filterResults(query)
                    }
                }.launchIn(this)

            if (uiState.value.initial) {
                refresh()
            }
        }
    }

    override fun reduce(intent: ManageBanMviModel.Intent) {
        when (intent) {
            is ManageBanMviModel.Intent.ChangeSection -> {
                screenModelScope.launch {
                    updateState { it.copy(section = intent.section) }
                }
            }

            ManageBanMviModel.Intent.Refresh -> {
                screenModelScope.launch {
                    refresh()
                }
            }

            is ManageBanMviModel.Intent.UnblockCommunity -> unbanCommunity(intent.id)
            is ManageBanMviModel.Intent.UnblockInstance -> unbanInstance(intent.id)
            is ManageBanMviModel.Intent.UnblockUser -> unbanUser(intent.id)
            is ManageBanMviModel.Intent.BlockDomain -> blockDomain(intent.value)
            is ManageBanMviModel.Intent.UnblockDomain -> unblockDomain(intent.value)
            is ManageBanMviModel.Intent.SetSearch -> updateSearchText(intent.value)
            is ManageBanMviModel.Intent.AddStopWord -> addStopWord(intent.value)
            is ManageBanMviModel.Intent.RemoveStopWord -> removeStopWord(intent.value)
        }
    }

    private suspend fun refresh() {
        val auth = identityRepository.authToken.value.orEmpty()
        originalBans = siteRepository.getBans(auth)
        val accountId = accountRepository.getActive()?.id
        originalBlockedDomains = blocklistRepository.get(accountId)
        originalStopWords = stopWordRepository.get(accountId)
        val query = uiState.value.searchText
        filterResults(query)
    }

    private fun unbanUser(id: Long) {
        screenModelScope.launch {
            val auth = identityRepository.authToken.value.orEmpty()
            try {
                userRepository.block(
                    id = id,
                    blocked = false,
                    auth = auth,
                )
                updateState {
                    it.copy(bannedUsers = it.bannedUsers.filter { e -> e.id != id })
                }
                emitEffect(ManageBanMviModel.Effect.Success)
            } catch (e: Throwable) {
                emitEffect(ManageBanMviModel.Effect.Failure(e.message))
            }
        }
    }

    private fun unbanCommunity(id: Long) {
        screenModelScope.launch {
            val auth = identityRepository.authToken.value.orEmpty()
            try {
                communityRepository.block(
                    id = id,
                    blocked = false,
                    auth = auth,
                )
                updateState {
                    it.copy(bannedCommunities = it.bannedCommunities.filter { e -> e.id != id })
                }
                emitEffect(ManageBanMviModel.Effect.Success)
            } catch (e: Throwable) {
                emitEffect(ManageBanMviModel.Effect.Failure(e.message))
            }
        }
    }

    private fun unbanInstance(id: Long) {
        screenModelScope.launch {
            val auth = identityRepository.authToken.value.orEmpty()
            try {
                siteRepository.block(
                    id = id,
                    blocked = false,
                    auth = auth,
                )
                updateState {
                    it.copy(bannedInstances = it.bannedInstances.filter { e -> e.id != id })
                }
                emitEffect(ManageBanMviModel.Effect.Success)
            } catch (e: Throwable) {
                emitEffect(ManageBanMviModel.Effect.Failure(e.message))
            }
        }
    }

    private fun updateSearchText(value: String) {
        screenModelScope.launch {
            updateState { it.copy(searchText = value) }
        }
    }

    private suspend fun filterResults(query: String) {
        val bans = originalBans ?: AccountBansModel()
        updateState {
            it.copy(
                bannedUsers = bans.users.filterUsersBy(query),
                bannedCommunities = bans.communities.filterCommunitiesBy(query),
                bannedInstances = bans.instances.filterInstancesBy(query),
                blockedDomains = originalBlockedDomains.filterBy(query),
                stopWords = originalStopWords.filterBy(query),
                initial = false,
            )
        }
    }

    private fun blockDomain(domain: String) {
        if (domain.isBlank()) {
            return
        }
        val newValues =
            if (originalBlockedDomains.contains(domain)) {
                originalBlockedDomains
            } else {
                originalBlockedDomains + domain
            }
        screenModelScope.launch {
            val accountId = accountRepository.getActive()?.id
            originalBlockedDomains = newValues
            blocklistRepository.update(accountId, newValues)
            val query = uiState.value.searchText
            filterResults(query)
        }
    }

    private fun unblockDomain(domain: String) {
        val newValues = originalBlockedDomains - domain
        screenModelScope.launch {
            val accountId = accountRepository.getActive()?.id
            originalBlockedDomains = newValues
            blocklistRepository.update(accountId, newValues)
            val query = uiState.value.searchText
            filterResults(query)
        }
    }

    private fun addStopWord(word: String) {
        if (word.isBlank()) {
            return
        }
        val newValues =
            if (originalStopWords.contains(word)) {
                originalStopWords
            } else {
                originalStopWords + word
            }
        screenModelScope.launch {
            val accountId = accountRepository.getActive()?.id
            originalStopWords = newValues
            stopWordRepository.update(accountId, newValues)
            val query = uiState.value.searchText
            filterResults(query)
        }
    }

    private fun removeStopWord(word: String) {
        val newValues = originalStopWords - word
        screenModelScope.launch {
            val accountId = accountRepository.getActive()?.id
            originalStopWords = newValues
            stopWordRepository.update(accountId, newValues)
            val query = uiState.value.searchText
            filterResults(query)
        }
    }
}

private fun List<UserModel>.filterUsersBy(query: String): List<UserModel> =
    if (query.isEmpty()) {
        this
    } else {
        filter { it.readableHandle.contains(query, ignoreCase = true) }
    }

private fun List<CommunityModel>.filterCommunitiesBy(query: String): List<CommunityModel> =
    if (query.isEmpty()) {
        this
    } else {
        filter { it.readableHandle.contains(query, ignoreCase = true) }
    }

private fun List<InstanceModel>.filterInstancesBy(query: String): List<InstanceModel> =
    if (query.isEmpty()) {
        this
    } else {
        filter { it.domain.contains(query, ignoreCase = true) }
    }

private fun List<String>.filterBy(query: String): List<String> =
    if (query.isEmpty()) {
        this
    } else {
        filter { it.contains(query, ignoreCase = true) }
    }
