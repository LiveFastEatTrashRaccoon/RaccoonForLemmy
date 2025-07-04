package com.livefast.eattrash.raccoonforlemmy.domain.identity.usecase

import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.VoteFormat
import com.livefast.eattrash.raccoonforlemmy.core.navigation.BottomNavItemsRepository
import com.livefast.eattrash.raccoonforlemmy.core.navigation.toInts
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.AccountModel
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.AccountRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.CommunityPreferredLanguageRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.CommunitySortRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.PostLastSeenDateRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.SettingsRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.UserSortRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.usecase.CreateSpecialTagsUseCase
import com.livefast.eattrash.raccoonforlemmy.domain.identity.repository.ApiConfigurationRepository
import com.livefast.eattrash.raccoonforlemmy.domain.identity.repository.AuthRepository
import com.livefast.eattrash.raccoonforlemmy.domain.identity.repository.IdentityRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.LemmyValueCache
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.SiteRepository

internal class DefaultLoginUseCase(
    private val authRepository: AuthRepository,
    private val apiConfigurationRepository: ApiConfigurationRepository,
    private val identityRepository: IdentityRepository,
    private val accountRepository: AccountRepository,
    private val settingsRepository: SettingsRepository,
    private val siteRepository: SiteRepository,
    private val communitySortRepository: CommunitySortRepository,
    private val communityPreferredLanguageRepository: CommunityPreferredLanguageRepository,
    private val bottomNavItemsRepository: BottomNavItemsRepository,
    private val lemmyValueCache: LemmyValueCache,
    private val createSpecialTagsUseCase: CreateSpecialTagsUseCase,
    private val userSortRepository: UserSortRepository,
    private val postLastSeenDateRepository: PostLastSeenDateRepository,
) : LoginUseCase {
    override suspend operator fun invoke(
        instance: String,
        username: String,
        password: String,
        totp2faToken: String?,
    ): Result<Unit> {
        val oldInstance = apiConfigurationRepository.instance.value
        apiConfigurationRepository.changeInstance(instance)

        val response =
            authRepository.login(
                username = username,
                password = password,
                totp2faToken = totp2faToken,
            )
        return response.mapCatching {
            if (it.error != null) {
                throw Exception("$instance says: ${it.error}")
            }

            val auth = it.token
            if (auth == null) {
                apiConfigurationRepository.changeInstance(oldInstance)
                throw Exception("Unable to log in")
            }

            val accountSettings = siteRepository.getAccountSettings(auth)
            identityRepository.storeToken(auth)
            identityRepository.refreshLoggedState()
            lemmyValueCache.refresh(auth)

            val account =
                AccountModel(
                    username = username,
                    instance = instance,
                    jwt = auth,
                )
            val existing = accountRepository.getBy(username = username, instance = instance)
            val accountId =
                if (existing == null) {
                    // new account with a copy of the anonymous settings
                    // (except a couple of fields from the Lemmy account)
                    val newAccountId = accountRepository.createAccount(account)
                    val anonymousSettings =
                        settingsRepository
                            .getSettings(null)
                            .copy(
                                showScores = accountSettings?.showScores ?: true,
                                includeNsfw = accountSettings?.showNsfw ?: false,
                                voteFormat =
                                accountSettings?.let { settings ->
                                    val showPercentage = settings.showUpVotePercentage == true
                                    val separate =
                                        settings.showUpVotes == true &&
                                            settings.showDownVotes == true &&
                                            settings.showScores == false
                                    val hidden =
                                        settings.showUpVotes == false &&
                                            settings.showDownVotes == false &&
                                            settings.showScores == false
                                    when {
                                        showPercentage -> VoteFormat.Percentage
                                        separate -> VoteFormat.Separated
                                        hidden -> VoteFormat.Hidden
                                        else -> VoteFormat.Aggregated
                                    }
                                } ?: VoteFormat.Aggregated,
                            )
                    settingsRepository.createSettings(
                        settings = anonymousSettings,
                        accountId = newAccountId,
                    )
                    newAccountId
                } else {
                    existing.id ?: 0
                }
            val oldActiveAccountId = accountRepository.getActive()?.id
            if (oldActiveAccountId != null) {
                accountRepository.setActive(oldActiveAccountId, false)
            }
            accountRepository.setActive(accountId, true)

            communitySortRepository.clear()
            communityPreferredLanguageRepository.clear()
            userSortRepository.clear()
            postLastSeenDateRepository.clear()

            val newSettings = settingsRepository.getSettings(accountId)
            settingsRepository.changeCurrentSettings(newSettings)

            val bottomBarSections = bottomNavItemsRepository.get(accountId)
            settingsRepository.changeCurrentBottomBarSections(bottomBarSections.toInts())

            createSpecialTagsUseCase()
        }
    }
}
