package com.livefast.eattrash.raccoonforlemmy.core.utils.appicon

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager

internal class DefaultAppIconManager(
    private val context: Context,
) : AppIconManager {
    private val allComponentNames =
        listOf(
            "com.livefast.eattrash.raccoonforlemmy.android.MainActivity",
            "com.livefast.eattrash.raccoonforlemmy.android.MainActivityAlias2",
        )

    override val supportsMultipleIcons = allComponentNames.isNotEmpty()

    override fun changeIcon(variant: AppIconVariant) {
        val indexToEnable = variant.toInt()
        with(context.packageManager) {
            allComponentNames.forEachIndexed { i, name ->
                val enabledState =
                    if (i == indexToEnable) {
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED
                    } else {
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED
                    }
                setComponentEnabledSetting(
                    ComponentName(context, name),
                    enabledState,
                    PackageManager.DONT_KILL_APP,
                )
            }
        }
    }
}
