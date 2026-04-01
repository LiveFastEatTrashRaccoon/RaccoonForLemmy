package com.livefast.eattrash.raccoonforlemmy.domain.identity.repository

import com.livefast.eattrash.raccoonforlemmy.core.api.provider.ServiceProvider
import com.livefast.eattrash.raccoonforlemmy.core.preferences.store.TemporaryKeyStore
import com.livefast.eattrash.raccoonforlemmy.core.testutils.DispatcherTestRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertEquals

class DefaultApiConfigurationRepositoryTest {
    @get:Rule
    val dispatcherTestRule = DispatcherTestRule()

    private val initialInstance = "lemmy.world"
    private val newInstance = "feddit.it"
    private val serviceProvider =
        mockk<ServiceProvider>(relaxUnitFun = true) {
            every { defaultInstance } returns initialInstance
        }
    private val keyStore =
        mockk<TemporaryKeyStore>(relaxUnitFun = true) {
            coEvery { get(any(), any<String>()) } returns ""
        }
    private val sut =
        DefaultApiConfigurationRepository(
            serviceProvider = serviceProvider,
            keyStore = keyStore,
            dispatcher = dispatcherTestRule.dispatcher,
        )

    @Test
    fun whenChangeInstance_thenValueIsUpdated() {
        val resBefore = sut.instance.value
        assertEquals(initialInstance, resBefore)

        sut.changeInstance(newInstance)

        coVerify {
            serviceProvider.changeInstance(newInstance)
            keyStore.save(any(), newInstance)
        }
    }
}
