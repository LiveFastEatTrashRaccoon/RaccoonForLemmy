package com.livefast.eattrash.raccoonforlemmy.core.commonui.modals

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.BottomSheetHeader
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.SettingsRow
import com.livefast.eattrash.raccoonforlemmy.core.l10n.messages.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.navigation.di.getNavigationCoordinator
import com.livefast.eattrash.raccoonforlemmy.core.notifications.NotificationCenterEvent
import com.livefast.eattrash.raccoonforlemmy.core.notifications.di.getNotificationCenter
import com.livefast.eattrash.raccoonforlemmy.core.resources.di.getCoreResources
import com.livefast.eattrash.raccoonforlemmy.core.utils.appicon.AppIconVariant
import com.livefast.eattrash.raccoonforlemmy.core.utils.appicon.toInt
import com.livefast.eattrash.raccoonforlemmy.core.utils.appicon.toReadableName

class AppIconBottomSheet : Screen {
    @Composable
    override fun Content() {
        val navigationCoordinator = remember { getNavigationCoordinator() }
        val notificationCenter = remember { getNotificationCenter() }
        val coreResources = remember { getCoreResources() }

        Surface {
            Column(
                modifier =
                    Modifier
                        .windowInsetsPadding(WindowInsets.navigationBars)
                        .padding(
                            top = Spacing.s,
                            start = Spacing.s,
                            end = Spacing.s,
                            bottom = Spacing.m,
                        ),
                verticalArrangement = Arrangement.spacedBy(Spacing.s),
            ) {
                BottomSheetHeader(LocalStrings.current.settingsAppIcon)
                val values =
                    listOf(
                        AppIconVariant.Default,
                        AppIconVariant.Alt1,
                        AppIconVariant.Alt2,
                    )
                Column(
                    modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(Spacing.xxs),
                ) {
                    for (value in values) {
                        SettingsRow(
                            modifier = Modifier.padding(vertical = Spacing.xxs),
                            title = value.toReadableName(),
                            painter =
                                when (value) {
                                    AppIconVariant.Alt2 -> coreResources.appIconAlt2
                                    AppIconVariant.Alt1 -> coreResources.appIconAlt1
                                    else -> coreResources.appIconDefault
                                },
                            onTap = {
                                navigationCoordinator.hideBottomSheet()
                                notificationCenter.send(
                                    NotificationCenterEvent.AppIconVariantSelected(value.toInt()),
                                )
                            },
                        )
                    }
                }
            }
        }
    }
}
