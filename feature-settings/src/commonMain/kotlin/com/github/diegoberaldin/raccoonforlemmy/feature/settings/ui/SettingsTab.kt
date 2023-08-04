package com.github.diegoberaldin.raccoonforlemmy.feature.settings.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.Spacing
import com.github.diegoberaldin.raccoonforlemmy.core.architecture.bindToLifecycle
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.modals.LanguageBottomSheet
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.modals.ListingTypeBottomSheet
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.modals.SortBottomSheet
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.modals.ThemeBottomSheet
import com.github.diegoberaldin.raccoonforlemmy.feature.settings.di.getSettingsScreenModel
import com.github.diegoberaldin.raccoonforlemmy.feature.settings.viewmodel.SettingsScreenMviModel
import com.github.diegoberaldin.raccoonforlemmy.resources.MR
import com.github.diegoberaldin.raccoonforlemmy.resources.di.getLanguageRepository
import com.github.diegoberaldin.raccoonforlemmy.resources.di.staticString
import dev.icerock.moko.resources.desc.desc

object SettingsTab : Tab {

    override val options: TabOptions
        @Composable get() {
            val icon = rememberVectorPainter(Icons.Default.Settings)
            val languageRepository = remember { getLanguageRepository() }
            val lang by languageRepository.currentLanguage.collectAsState()
            return remember(lang) {
                val title = staticString(MR.strings.navigation_settings.desc())
                TabOptions(
                    index = 4u,
                    title = title,
                    icon = icon,
                )
            }
        }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val model = rememberScreenModel { getSettingsScreenModel() }
        model.bindToLifecycle(key)
        val uiState by model.uiState.collectAsState()
        val bottomSheetNavigator = LocalBottomSheetNavigator.current

        Scaffold(
            modifier = Modifier.padding(Spacing.xxs),
            topBar = {
                val languageRepository = remember { getLanguageRepository() }
                val lang by languageRepository.currentLanguage.collectAsState()
                val title by remember(lang) {
                    mutableStateOf(staticString(MR.strings.navigation_settings.desc()))
                }
                TopAppBar(title = {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                    )
                })
            },
        ) {
            Box(
                modifier = Modifier.padding(it),
            ) {
                SettingsContent(
                    uiState = uiState,
                    onSelectTheme = {
                        bottomSheetNavigator.show(
                            ThemeBottomSheet(
                                onSelected = { newValue ->
                                    model.reduce(SettingsScreenMviModel.Intent.ChangeTheme(newValue))
                                },
                                onHide = {
                                    bottomSheetNavigator.hide()
                                },
                            ),
                        )
                    },
                    onSelectLanguage = {
                        bottomSheetNavigator.show(
                            LanguageBottomSheet(
                                onSelected = { newValue ->
                                    model.reduce(
                                        SettingsScreenMviModel.Intent.ChangeLanguage(
                                            newValue,
                                        ),
                                    )
                                },
                                onHide = {
                                    bottomSheetNavigator.hide()
                                },
                            ),
                        )
                    },
                    onSelectListingType = {
                        bottomSheetNavigator.show(
                            ListingTypeBottomSheet(
                                isLogged = uiState.isLogged,
                                onSelected = { newValue ->
                                    model.reduce(
                                        SettingsScreenMviModel.Intent.ChangeDefaultListingType(
                                            newValue,
                                        ),
                                    )
                                },
                                onHide = {
                                    bottomSheetNavigator.hide()
                                },
                            ),
                        )
                    },
                    onSelectPostSortType = {
                        bottomSheetNavigator.show(
                            SortBottomSheet(
                                onSelected = { newValue ->
                                    model.reduce(
                                        SettingsScreenMviModel.Intent.ChangeDefaultPostSortType(
                                            newValue,
                                        ),
                                    )
                                },
                                onHide = {
                                    bottomSheetNavigator.hide()
                                },
                            ),
                        )
                    },
                    onSelectCommentSortType = {
                        bottomSheetNavigator.show(
                            SortBottomSheet(
                                onSelected = { newValue ->
                                    model.reduce(
                                        SettingsScreenMviModel.Intent.ChangeDefaultCommentSortType(
                                            newValue,
                                        ),
                                    )
                                },
                                onHide = {
                                    bottomSheetNavigator.hide()
                                },
                            ),
                        )
                    },
                )
            }
        }
    }
}
