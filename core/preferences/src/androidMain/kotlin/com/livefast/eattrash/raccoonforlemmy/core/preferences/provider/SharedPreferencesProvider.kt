package com.livefast.eattrash.raccoonforlemmy.core.preferences.provider

import android.content.SharedPreferences

interface SharedPreferencesProvider {
    fun provide(name: String): SharedPreferences
}
