package com.livefast.eattrash.raccoonforlemmy.core.preferences.appconfig

import com.livefast.eattrash.raccoonforlemmy.core.testutils.DispatcherTestRule
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertTrue

class RemoteAppConfigDataSourceTest {
    @get:Rule
    val dispatcherTestRule = DispatcherTestRule()

    private val mockEngine =
        MockEngine {
            respond(
                content = "{\"alternateMarkdownRenderingSettingsItemEnabled\": true}",
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json"),
            )
        }
    private val clientEngineFactory =
        mockk<HttpClientEngineFactory<*>> {
            every { create(any<HttpClientEngineConfig.() -> Unit>()) } returns mockEngine
        }
    private val sut =
        RemoteAppConfigDataSource(
            factory = clientEngineFactory,
        )

    @Test
    fun whenGet_thenResultIsAsExpected() =
        runTest {
            val res = sut.get()

            assertTrue(res.alternateMarkdownRenderingSettingsItemEnabled)
        }
}
