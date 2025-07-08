package com.livefast.eattrash.raccoonforlemmy.unit.about

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.architecture.di.getViewModel
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.navigation.di.getMainRouter
import com.livefast.eattrash.raccoonforlemmy.core.notifications.NotificationCenterEvent
import com.livefast.eattrash.raccoonforlemmy.core.notifications.di.getNotificationCenter
import com.livefast.eattrash.raccoonforlemmy.core.resources.di.getCoreResources
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.CommunityModel
import com.livefast.eattrash.raccoonforlemmy.unit.about.components.AboutItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutDialog(modifier: Modifier = Modifier) {
    val viewModel: AboutDialogMviModel = getViewModel<AboutDialogViewModel>()
    val uriHandler = LocalUriHandler.current
    val uiState by viewModel.uiState.collectAsState()
    val notificationCenter = remember { getNotificationCenter() }
    val mainRouter = remember { getMainRouter() }
    val coreResources = remember { getCoreResources() }

    BasicAlertDialog(
        modifier = modifier,
        onDismissRequest = {
            notificationCenter.send(NotificationCenterEvent.CloseDialog)
        },
    ) {
        Surface {
            Column(
                modifier =
                Modifier
                    .background(color = MaterialTheme.colorScheme.surface)
                    .padding(vertical = Spacing.s),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = LocalStrings.current.settingsAbout,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Spacer(modifier = Modifier.height(Spacing.s))
                LazyColumn(
                    modifier =
                    Modifier
                        .padding(vertical = Spacing.s, horizontal = Spacing.m)
                        .heightIn(max = 500.dp),
                    verticalArrangement = Arrangement.spacedBy(Spacing.xs),
                ) {
                    item {
                        AboutItem(
                            text = LocalStrings.current.settingsAboutAppVersion,
                            value = uiState.version,
                        )
                    }
                    item {
                        AboutItem(
                            text = LocalStrings.current.settingsAboutChangelog,
                            vector = Icons.Default.OpenInBrowser,
                            textDecoration = TextDecoration.Underline,
                            onClick = {
                                uriHandler.openUri(AboutConstants.CHANGELOG_URL)
                            },
                        )
                    }
                    item {
                        Button(
                            onClick = {
                                uriHandler.openUri(AboutConstants.REPORT_URL)
                            },
                        ) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = LocalStrings.current.settingsAboutReportGithub,
                                style = MaterialTheme.typography.labelLarge,
                            )
                        }
                        Button(
                            onClick = {
                                runCatching {
                                    uriHandler.openUri(AboutConstants.REPORT_EMAIL_ADDRESS)
                                }
                            },
                        ) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = LocalStrings.current.settingsAboutReportEmail,
                                style = MaterialTheme.typography.labelLarge,
                            )
                        }
                    }
                    item {
                        AboutItem(
                            painter = coreResources.github,
                            text = LocalStrings.current.settingsAboutViewGithub,
                            textDecoration = TextDecoration.Underline,
                            onClick = {
                                uriHandler.openUri(AboutConstants.WEBSITE_URL)
                            },
                        )
                    }
                    item {
                        AboutItem(
                            painter = coreResources.lemmy,
                            text = LocalStrings.current.settingsAboutViewLemmy,
                            textDecoration = TextDecoration.Underline,
                            onClick = {
                                mainRouter.openCommunityDetail(
                                    community = CommunityModel(name = AboutConstants.LEMMY_COMMUNITY_NAME),
                                    otherInstance = AboutConstants.LEMMY_COMMUNITY_INSTANCE,
                                )
                            },
                        )
                    }
                    item {
                        AboutItem(
                            vector = Icons.AutoMirrored.Default.Chat,
                            text = LocalStrings.current.settingsAboutMatrix,
                            textDecoration = TextDecoration.Underline,
                            onClick = {
                                uriHandler.openUri(AboutConstants.MATRIX_URL)
                            },
                        )
                    }
                    item {
                        AboutItem(
                            text = LocalStrings.current.settingsAboutLicences,
                            vector = Icons.Default.Gavel,
                            textDecoration = TextDecoration.Underline,
                            onClick = {
                                mainRouter.openLicences()
                            },
                        )
                    }
                    item {
                        AboutItem(
                            text = LocalStrings.current.settingsAboutAcknowledgements,
                            vector = Icons.Default.VolunteerActivism,
                            textDecoration = TextDecoration.Underline,
                            onClick = {
                                mainRouter.openAcknowledgements()
                            },
                        )
                    }
                }
                Button(
                    onClick = {
                        notificationCenter.send(NotificationCenterEvent.CloseDialog)
                    },
                ) {
                    Text(text = LocalStrings.current.buttonClose)
                }
            }
        }
    }
}
