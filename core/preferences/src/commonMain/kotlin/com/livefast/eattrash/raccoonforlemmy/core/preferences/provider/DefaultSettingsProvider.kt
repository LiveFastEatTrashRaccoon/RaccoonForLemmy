package com.livefast.eattrash.raccoonforlemmy.core.preferences.provider

import com.russhwolf.settings.Settings
import org.koin.core.annotation.Single

@Single
internal expect class DefaultSettingsProvider : SettingsProvider {
    override fun provide(): Settings
}
