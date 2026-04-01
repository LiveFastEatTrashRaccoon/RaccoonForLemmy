package com.livefast.eattrash.raccoonforlemmy.core.preferences.appconfig

import com.livefast.eattrash.raccoonforlemmy.core.testutils.DispatcherTestRule
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertEquals
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
    private val sut = RemoteAppConfigDataSource(engine = mockEngine, json = Json)

    @Test
    fun whenGet_thenResultIsAsExpected() = runTest {
        val res = sut.get()

        assertTrue(res.alternateMarkdownRenderingSettingsItemEnabled)
        assertEquals(0, res.version)
    }
}
