package com.livefast.eattrash.raccoonforlemmy.core.preferences

import com.livefast.eattrash.raccoonforlemmy.core.preferences.store.DefaultTemporaryKeyStore
import com.livefast.eattrash.raccoonforlemmy.core.testutils.DispatcherTestRule
import com.russhwolf.settings.Settings
import com.russhwolf.settings.contains
import com.russhwolf.settings.get
import com.russhwolf.settings.set
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DefaultTemporaryKeyStoreTest {
    @get:Rule
    val dispatcherTestRule = DispatcherTestRule()

    private val settings = mockk<Settings>(relaxUnitFun = true)

    private val sut = DefaultTemporaryKeyStore(settings)

    @Test
    fun givenNonExistingKey_whenContainsKey_thenInteractionsAreAsExpected() = runTest {
        every { settings.contains(any()) } returns false
        every { settings.keys } returns setOf()

        val res = sut.containsKey("key")

        assertFalse(res)
    }

    @Test
    fun givenExistingKey_whenContainsKey_thenInteractionsAreAsExpected() = runTest {
        every { settings.keys } returns setOf("key")

        val res = sut.containsKey("key")

        assertTrue(res)
        verify {
            settings.keys
        }
    }

    @Test
    fun whenGetInt_thenResultIsAsExpected() = runTest {
        every { settings.hasKey(any()) } returns true
        every { settings.get("key", defaultValue = any<Int>()) } returns 2

        val res = sut.get("key", 1)
        assertEquals(2, res)
        verify {
            settings.get(key = "key", defaultValue = 1)
        }
    }

    @Test
    fun whenSaveInt_thenInteractionsAreAsExpected() = runTest {
        sut.save("key", 0)

        verify {
            settings["key"] = 0
        }
    }

    @Test
    fun whenGetLong_thenResultIsAsExpected() = runTest {
        every { settings.hasKey(any()) } returns true
        every { settings.get("key", defaultValue = any<Long>()) } returns 2L

        val res = sut.get("key", 1L)
        assertEquals(2L, res)
        verify {
            settings.get(key = "key", defaultValue = 1L)
        }
    }

    @Test
    fun whenSaveLong_thenInteractionsAreAsExpected() = runTest {
        sut.save("key", 1L)
        verify {
            settings["key"] = 1L
        }
    }

    @Test
    fun whenGetBoolean_thenResultIsAsExpected() = runTest {
        every { settings.hasKey(any()) } returns true
        every { settings.get("key", defaultValue = any<Boolean>()) } returns true

        val res = sut.get("key", false)
        assertTrue(res)
        verify {
            settings.get(key = "key", defaultValue = false)
        }
    }

    @Test
    fun whenSaveBoolean_thenInteractionsAreAsExpected() = runTest {
        sut.save("key", true)

        verify {
            settings["key"] = true
        }
    }

    @Test
    fun whenGetString_thenResultIsAsExpected() = runTest {
        every { settings.hasKey(any()) } returns true
        every { settings.get("key", defaultValue = any<String>()) } returns "b"

        val res = sut.get("key", "a")
        assertEquals("b", res)
        verify {
            settings.get(key = "key", defaultValue = "a")
        }
    }

    @Test
    fun whenSaveString_thenInteractionsAreAsExpected() = runTest {
        sut.save("key", "value")

        verify {
            settings["key"] = "value"
        }
    }

    @Test
    fun whenGetFloat_thenResultIsAsExpected() = runTest {
        every { settings.hasKey(any()) } returns true
        every { settings.get("key", defaultValue = any<Float>()) } returns 2.0f

        val res = sut.get("key", 1.0f)
        assertEquals(2.0f, res)
        verify {
            settings.get(key = "key", defaultValue = 1.0f)
        }
    }

    @Test
    fun whenSaveFloat_thenInteractionsAreAsExpected() = runTest {
        sut.save("key", 1.0f)
        verify {
            settings["key"] = 1.0f
        }
    }

    @Test
    fun whenGetDouble_thenResultIsAsExpected() = runTest {
        every { settings.hasKey(any()) } returns true
        every { settings.get("key", defaultValue = any<Double>()) } returns 2.0

        val res = sut.get("key", 1.0)
        assertEquals(2.0, res)
        verify {
            settings.get(key = "key", defaultValue = 1.0)
        }
    }

    @Test
    fun whenSaveDouble_thenInteractionsAreAsExpected() = runTest {
        sut.save("key", 1.0)

        verify {
            settings["key"] = 1.0
        }
    }

    @Test
    fun givenNonExistingKey_whenGetStringList_thenResultIsAsExpected() = runTest {
        every { settings.hasKey(any()) } returns false

        val res = sut.get("key", listOf(""))
        assertEquals(listOf(""), res)
    }

    @Test
    fun givenExistingKey_whenGetStringList_thenResultIsAsExpected() = runTest {
        every { settings.hasKey(any()) } returns true
        every { settings.get("key", defaultValue = any<String>()) } returns "a, b"

        val res = sut.get("key", listOf("c", "d"))
        assertEquals(2, res.size)
        assertEquals("a", res.first())
        assertEquals("b", res[1])
        verify {
            settings.get(key = "key", defaultValue = "")
        }
    }

    @Test
    fun whenSaveStringList_thenInteractionsAreAsExpected() = runTest {
        val values = listOf("a", "b", "c")
        sut.save("key", values)

        sut.save(key = "key", value = values, delimiter = ", ")

        verify {
            settings["key"] = values.joinToString(", ")
        }
    }

    @Test
    fun whenRemove_thenInteractionsAreAsExpected() = runTest {
        sut.remove("key")

        verify {
            settings.remove("key")
        }
    }

    @Test
    fun whenRemoveAll_thenInteractionsAreAsExpected() = runTest {
        sut.removeAll()

        verify {
            settings.clear()
        }
    }
}
