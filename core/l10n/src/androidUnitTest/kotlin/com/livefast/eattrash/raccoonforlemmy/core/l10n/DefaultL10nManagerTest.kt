package com.livefast.eattrash.raccoonforlemmy.core.l10n

import java.util.Locale
import kotlin.test.Test
import kotlin.test.assertEquals

class DefaultL10nManagerTest {
    private val sut = DefaultL10nManager()

    @Test
    fun whenInitial_thenResultIsAsExpected() {
        val res = sut.lang.value

        assertEquals(Locales.EN, res)
    }

    @Test
    fun whenChangeLanguage_thenResultIsAsExpected() {
        sut.changeLanguage(Locales.IT)

        val res = sut.lang.value
        val defaultLocale = Locale.getDefault()

        assertEquals(Locales.IT, res)
        assertEquals(Locales.IT, defaultLocale.language)
    }
}
