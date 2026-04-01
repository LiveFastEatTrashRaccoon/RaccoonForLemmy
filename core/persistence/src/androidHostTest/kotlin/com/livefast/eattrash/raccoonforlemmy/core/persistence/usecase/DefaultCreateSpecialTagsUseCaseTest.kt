package com.livefast.eattrash.raccoonforlemmy.core.persistence.usecase

import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.AccountModel
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.UserTagModel
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.UserTagType
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.AccountRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.UserTagRepository
import io.mockk.Called
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class DefaultCreateSpecialTagsUseCaseTest {
    private val accountRepository = mockk<AccountRepository>()
    private val userTagRepository = mockk<UserTagRepository>(relaxUnitFun = true)
    private val sut =
        DefaultCreateSpecialTagsUseCase(
            accountRepository = accountRepository,
            userTagRepository = userTagRepository,
        )

    @Test
    fun givenNotLogged_whenInvoke_thenInteractionsAreAsExpected() = runTest {
        coEvery {
            accountRepository.getActive()
        } returns null

        sut()

        coVerify {
            accountRepository.getActive()
            userTagRepository wasNot Called
        }
    }

    @Test
    fun givenTagsAlreadyExisting_whenInvoke_thenInteractionsAreAsExpected() = runTest {
        val accountId = 1L
        coEvery {
            accountRepository.getActive()
        } returns
            AccountModel(
                id = accountId,
                username = "user-name",
                jwt = "",
                instance = "",
                active = true,
            )
        coEvery {
            userTagRepository.getAll(any())
        } returns
            listOf(
                UserTagModel(id = 2L, name = "admin", type = UserTagType.Admin),
                UserTagModel(id = 3L, name = "bot", type = UserTagType.Bot),
                UserTagModel(id = 4L, name = "me", type = UserTagType.Me),
                UserTagModel(id = 5L, name = "mod", type = UserTagType.Moderator),
                UserTagModel(id = 6L, name = "op", type = UserTagType.OriginalPoster),
            )

        sut()

        coVerify {
            accountRepository.getActive()
            userTagRepository.getAll(accountId)
        }
        coVerify(inverse = true) {
            userTagRepository.create(any(), any())
        }
    }

    @Test
    fun givenAdminTagMissing_whenInvoke_thenInteractionsAreAsExpected() = runTest {
        val accountId = 1L
        coEvery {
            accountRepository.getActive()
        } returns
            AccountModel(
                id = accountId,
                username = "user-name",
                jwt = "",
                instance = "",
                active = true,
            )
        coEvery {
            userTagRepository.getAll(any())
        } returns
            listOf(
                UserTagModel(id = 2L, name = "bot", type = UserTagType.Bot),
                UserTagModel(id = 3L, name = "me", type = UserTagType.Me),
                UserTagModel(id = 4L, name = "mod", type = UserTagType.Moderator),
                UserTagModel(id = 5L, name = "op", type = UserTagType.OriginalPoster),
            )

        sut()

        coVerify {
            accountRepository.getActive()
            userTagRepository.getAll(accountId)
            userTagRepository.create(
                withArg {
                    assertEquals(UserTagType.Admin, it.type)
                },
                accountId,
            )
        }
    }

    @Test
    fun givenBotTagMissing_whenInvoke_thenInteractionsAreAsExpected() = runTest {
        val accountId = 1L
        coEvery {
            accountRepository.getActive()
        } returns
            AccountModel(
                id = accountId,
                username = "user-name",
                jwt = "",
                instance = "",
                active = true,
            )
        coEvery {
            userTagRepository.getAll(any())
        } returns
            listOf(
                UserTagModel(id = 2L, name = "admin", type = UserTagType.Admin),
                UserTagModel(id = 3L, name = "me", type = UserTagType.Me),
                UserTagModel(id = 4L, name = "mod", type = UserTagType.Moderator),
                UserTagModel(id = 5L, name = "op", type = UserTagType.OriginalPoster),
            )

        sut()

        coVerify {
            accountRepository.getActive()
            userTagRepository.getAll(accountId)
            userTagRepository.create(
                withArg {
                    assertEquals(UserTagType.Bot, it.type)
                },
                accountId,
            )
        }
    }

    @Test
    fun givenMeTagMissing_whenInvoke_thenInteractionsAreAsExpected() = runTest {
        val accountId = 1L
        coEvery {
            accountRepository.getActive()
        } returns
            AccountModel(
                id = accountId,
                username = "user-name",
                jwt = "",
                instance = "",
                active = true,
            )
        coEvery {
            userTagRepository.getAll(any())
        } returns
            listOf(
                UserTagModel(id = 2L, name = "admin", type = UserTagType.Admin),
                UserTagModel(id = 3L, name = "bot", type = UserTagType.Bot),
                UserTagModel(id = 5L, name = "mod", type = UserTagType.Moderator),
                UserTagModel(id = 6L, name = "op", type = UserTagType.OriginalPoster),
            )

        sut()

        coVerify {
            accountRepository.getActive()
            userTagRepository.getAll(accountId)
            userTagRepository.create(
                withArg {
                    assertEquals(UserTagType.Me, it.type)
                },
                accountId,
            )
        }
    }

    @Test
    fun givenModTagMissing_whenInvoke_thenInteractionsAreAsExpected() = runTest {
        val accountId = 1L
        coEvery {
            accountRepository.getActive()
        } returns
            AccountModel(
                id = accountId,
                username = "user-name",
                jwt = "",
                instance = "",
                active = true,
            )
        coEvery {
            userTagRepository.getAll(any())
        } returns
            listOf(
                UserTagModel(id = 2L, name = "admin", type = UserTagType.Admin),
                UserTagModel(id = 3L, name = "bot", type = UserTagType.Bot),
                UserTagModel(id = 5L, name = "me", type = UserTagType.Me),
                UserTagModel(id = 6L, name = "op", type = UserTagType.OriginalPoster),
            )

        sut()

        coVerify {
            accountRepository.getActive()
            userTagRepository.getAll(accountId)
            userTagRepository.create(
                withArg {
                    assertEquals(UserTagType.Moderator, it.type)
                },
                accountId,
            )
        }
    }

    @Test
    fun givenOpTagMissing_whenInvoke_thenInteractionsAreAsExpected() = runTest {
        val accountId = 1L
        coEvery {
            accountRepository.getActive()
        } returns
            AccountModel(
                id = accountId,
                username = "user-name",
                jwt = "",
                instance = "",
                active = true,
            )
        coEvery {
            userTagRepository.getAll(any())
        } returns
            listOf(
                UserTagModel(id = 2L, name = "admin", type = UserTagType.Admin),
                UserTagModel(id = 3L, name = "bot", type = UserTagType.Bot),
                UserTagModel(id = 5L, name = "me", type = UserTagType.Me),
                UserTagModel(id = 6L, name = "mod", type = UserTagType.Moderator),
            )

        sut()

        coVerify {
            accountRepository.getActive()
            userTagRepository.getAll(accountId)
            userTagRepository.create(
                withArg {
                    assertEquals(UserTagType.OriginalPoster, it.type)
                },
                accountId,
            )
        }
    }
}
