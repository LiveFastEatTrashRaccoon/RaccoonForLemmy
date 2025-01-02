package com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository

import com.livefast.eattrash.raccoonforlemmy.core.api.dto.GetModlogResponse
import com.livefast.eattrash.raccoonforlemmy.core.api.provider.ServiceProvider
import com.livefast.eattrash.raccoonforlemmy.core.api.service.ModlogService
import com.livefast.eattrash.raccoonforlemmy.core.testutils.DispatcherTestRule
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.ModlogItemType
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.utils.toAuthHeader
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.utils.toDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class DefaultModlogRepositoryTest {
    @get:Rule
    val dispatcherTestRule = DispatcherTestRule()

    private val modLogService: ModlogService = mockk()
    private val serviceProvider =
        mockk<ServiceProvider> {
            every { modLog } returns modLogService
        }
    private val sut =
        DefaultModlogRepository(
            services = serviceProvider,
        )

    @Test
    fun givenEmptyResponse_whenGetItems_thenResultAndInteractionsAreAsExpected() =
        runTest {
            coEvery {
                modLogService.getItems(
                    authHeader = any(),
                    auth = any(),
                    page = any(),
                    communityId = any(),
                    limit = any(),
                    modId = any(),
                    otherId = any(),
                    type = any(),
                )
            } returns GetModlogResponse()

            val communityId = 1L
            val res =
                sut.getItems(
                    auth = AUTH_TOKEN,
                    communityId = communityId,
                    page = 1,
                    type = ModlogItemType.All,
                )

            assertNotNull(res)
            assertEquals(0, res.size)
            coVerify {
                modLogService.getItems(
                    authHeader = AUTH_TOKEN.toAuthHeader(),
                    auth = AUTH_TOKEN,
                    communityId = communityId,
                    limit = 20,
                    page = 1,
                    type = ModlogItemType.All.toDto(),
                )
            }
        }

    @Test
    fun givenNonEmptyResponse_whenGetItems_thenResultAndInteractionsAreAsExpected() =
        runTest {
            coEvery {
                modLogService.getItems(
                    authHeader = any(),
                    auth = any(),
                    page = any(),
                    communityId = any(),
                    limit = any(),
                    modId = any(),
                    otherId = any(),
                    type = any(),
                )
            } returns
                GetModlogResponse(
                    removedComments = listOf(mockk(relaxed = true)),
                    removedPosts = listOf(mockk(relaxed = true)),
                )

            val communityId = 1L
            val res =
                sut.getItems(
                    auth = AUTH_TOKEN,
                    communityId = communityId,
                    page = 1,
                )

            assertNotNull(res)
            assertEquals(2, res.size)
            coVerify {
                modLogService.getItems(
                    authHeader = AUTH_TOKEN.toAuthHeader(),
                    auth = AUTH_TOKEN,
                    communityId = communityId,
                    limit = 20,
                    page = 1,
                    type = ModlogItemType.All.toDto(),
                )
            }
        }

    companion object {
        private const val AUTH_TOKEN = "fake-token"
    }
}
