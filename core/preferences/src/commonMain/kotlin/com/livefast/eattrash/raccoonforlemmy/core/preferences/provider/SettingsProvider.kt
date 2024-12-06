package com.livefast.eattrash.raccoonforlemmy.core.preferences.provider

import com.russhwolf.settings.Settings

interface SettingsProvider {
    fun provide(): Settings
}
