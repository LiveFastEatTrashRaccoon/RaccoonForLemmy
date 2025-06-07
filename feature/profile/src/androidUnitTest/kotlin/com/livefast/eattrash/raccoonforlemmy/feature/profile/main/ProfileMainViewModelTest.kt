package com.livefast.eattrash.raccoonforlemmy.feature.profile.main

import com.livefast.eattrash.raccoonforlemmy.core.architecture.testutils.MviModelTestRule
import com.livefast.eattrash.raccoonforlemmy.core.testutils.DispatcherTestRule
import com.livefast.eattrash.raccoonforlemmy.domain.identity.repository.IdentityRepository
import com.livefast.eattrash.raccoonforlemmy.domain.identity.usecase.LogoutUseCase
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.UserModel
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertEquals

class ProfileMainViewModelTest {
    @get:Rule(order = 0)
    val dispatcherTestRule = DispatcherTestRule()

    @get:Rule(order = 1)
    val unloggedRule =
        MviModelTestRule {
            every { identityRepository.isLogged } returns MutableStateFlow(false)
            ProfileMainViewModel(
                identityRepository = identityRepository,
                logout = logoutUseCase,
            )
        }

    @get:Rule(order = 1)
    val loggedRule =
        MviModelTestRule {
            every { identityRepository.isLogged } returns MutableStateFlow(true)
            every { identityRepository.cachedUser } returns user
            ProfileMainViewModel(
                identityRepository = identityRepository,
                logout = logoutUseCase,
            )
        }

    private val identityRepository = mockk<IdentityRepository>(relaxUnitFun = true)
    private val logoutUseCase = mockk<LogoutUseCase>(relaxUnitFun = true)
    private val user = UserModel(id = 1L)

    @Test
    fun givenNotLogged_whenInitialized_thenStateIsAsExpected() = runTest {
        with(unloggedRule) {
            onState { state ->
                assertEquals(false, state.logged)
            }
        }
    }

    @Test
    fun givenLogged_whenInitialized_thenStateIsAsExpected() = runTest {
        with(loggedRule) {
            onState { state ->
                assertEquals(true, state.logged)
                assertEquals(user, state.user)
            }
        }
    }

    @Test
    fun whenLogoutIntentReceived_thenInteractionsAreAsExpected() = runTest {
        with(loggedRule) {
            send(ProfileMainMviModel.Intent.Logout)

            coVerify {
                logoutUseCase()
            }
        }
    }
}
