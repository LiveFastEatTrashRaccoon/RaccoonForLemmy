package com.livefast.eattrash.raccoonforlemmy.domain.identity.urlhandler

import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.CommunityModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.PostModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.UserModel
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DefaultUrlDecoderTest {
    private val sut = DefaultUrlDecoder()

    @Test
    fun givenValidUrl_whenGetPost_thenResultIsAsExpected() {
        val (post, instance) = sut.getPost("https://$HOST/post/1")

        assertEquals(PostModel(id = 1), post)
        assertEquals(HOST, instance)
    }

    @Test
    fun givenInvalidUrl_whenGetPost_thenResultIsAsExpected() {
        val (post, _) = sut.getPost("https://$HOST")

        assertNull(post)
    }

    @Test
    fun givenValidUrl_whenGetUser_thenResultIsAsExpected() {
        val res = sut.getUser("https://$HOST/u/test")

        assertEquals(UserModel(name = "test", host = HOST), res)
    }

    @Test
    fun givenInternalReference_whenGetUser_thenResultIsAsExpected() {
        val res = sut.getUser("@test@$HOST")

        assertEquals(UserModel(name = "test", host = HOST), res)
    }

    @Test
    fun givenInvalidUrl_whenGetUser_thenResultIsAsExpected() {
        val res = sut.getUser("https://$HOST")

        assertNull(res)
    }

    @Test
    fun givenValidUrl_whenGetCommunity_thenResultIsAsExpected() {
        val res = sut.getCommunity("https://$HOST/c/test")

        assertEquals(CommunityModel(name = "test", host = HOST), res)
    }

    @Test
    fun givenInternalReference_whenGetCommunity_thenResultIsAsExpected() {
        val res = sut.getCommunity("!test@$HOST")

        assertEquals(CommunityModel(name = "test", host = HOST), res)
    }

    @Test
    fun givenInvalidUrl_whenGetCommunity_thenResultIsAsExpected() {
        val res = sut.getCommunity("https://$HOST")

        assertNull(res)
    }

    companion object {
        private const val HOST = "example.com"
    }
}
