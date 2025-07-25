package com.livefast.eattrash.raccoonforlemmy.unit.licences

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.livefast.eattrash.raccoonforlemmy.core.architecture.DefaultMviModelDelegate
import com.livefast.eattrash.raccoonforlemmy.core.architecture.MviModelDelegate
import com.livefast.eattrash.raccoonforlemmy.unit.licences.models.LicenceItem
import com.livefast.eattrash.raccoonforlemmy.unit.licences.models.LicenceItemType
import kotlinx.coroutines.launch

class LicencesViewModel :
    ViewModel(),
    MviModelDelegate<LicencesMviModel.Intent, LicencesMviModel.State, LicencesMviModel.Effect>
    by DefaultMviModelDelegate(initialState = LicencesMviModel.State()),
    LicencesMviModel {
    init {
        viewModelScope.launch {
            populate()
        }
    }

    private suspend fun populate() {
        updateState {
            it.copy(
                items =
                buildList {
                    LicenceItem(
                        type = LicenceItemType.Library,
                        title = "Android Jetpack",
                        subtitle = """
                                A suite of libraries, tools, and guidance to help developers write high-quality
                                apps easier
                        """.trimIndent(),
                        url = LicenceUrls.ANDROIDX,
                    )
                    this +=
                        LicenceItem(
                            type = LicenceItemType.Resource,
                            title = "Atkinson Hyperlegible, Noto Sans, Poppins",
                            subtitle = "Fonts used in the app are released under the Open Font Library (OFL)",
                            url = LicenceUrls.OFL,
                        )
                    this +=
                        LicenceItem(
                            type = LicenceItemType.Library,
                            title = "Coil",
                            subtitle = "An image loading library for Android",
                            url = LicenceUrls.COIL,
                        )
                    this +=
                        LicenceItem(
                            type = LicenceItemType.Library,
                            title = "Calf webview",
                            subtitle = """
                                    Calf is a library that allows you to easily create adaptive UIs and access platform
                                    specific APIs from your Compose Multiplatform apps
                            """.trimIndent(),
                            url = LicenceUrls.CALF,
                        )
                    this +=
                        LicenceItem(
                            type = LicenceItemType.Library,
                            title = "Compose ColorPicker",
                            subtitle = "Kotlin Multiplatform color picker library",
                            url = LicenceUrls.COMPOSE_COLORPICKER,
                        )
                    this +=
                        LicenceItem(
                            type = LicenceItemType.Library,
                            title = "Compose Multiplatform Media Player",
                            subtitle = """
                                    Compose Multiplatform Media Player is a powerful media player library designed for
                                    Compose Multiplatform projects
                            """.trimIndent(),
                            url = LicenceUrls.COMPOSE_MULTIPLATFORM_MEDIA_PLAYER,
                        )
                    this +=
                        LicenceItem(
                            type = LicenceItemType.Resource,
                            title = "GitHub logo",
                            url = LicenceUrls.GITHUB_LOGO,
                        )
                    this +=
                        LicenceItem(
                            type = LicenceItemType.Library,
                            title = "Kodein",
                            subtitle = "A straightforward and yet very useful dependency retrieval container",
                            url = LicenceUrls.KODEIN,
                        )
                    this +=
                        LicenceItem(
                            type = LicenceItemType.Library,
                            title = "KotlinCrypto",
                            subtitle = "Cryptographic components for Kotlin Multiplatform",
                            url = LicenceUrls.KOTLIN_CRYPTO,
                        )
                    this +=
                        LicenceItem(
                            type = LicenceItemType.Library,
                            title = "Ktor",
                            subtitle = """
                                    An asynchronous framework for creating microservices, web applications and more
                            """.trimIndent(),
                            url = LicenceUrls.KTOR,
                        )
                    this +=
                        LicenceItem(
                            type = LicenceItemType.Resource,
                            title = "Lemmy logo",
                            url = LicenceUrls.LEMMY_LOGO,
                        )
                    this +=
                        LicenceItem(
                            type = LicenceItemType.Resource,
                            title = "Material Design Icons",
                            subtitle = "A set of icons by Google",
                            url = LicenceUrls.MATERIAL_ICONS,
                        )
                    this +=
                        LicenceItem(
                            type = LicenceItemType.Library,
                            title = "MaterialKolor",
                            subtitle = """
                                    A Compose Multiplatform library for creating dynamic Material Design 3
                                    color palettes
                            """.trimIndent(),
                            url = LicenceUrls.MATERIAL_KOLOR,
                        )
                    this +=
                        LicenceItem(
                            type = LicenceItemType.Resource,
                            title = "Monochrome icon",
                            url = LicenceUrls.MONOCHROME_ICON,
                        )
                    this +=
                        LicenceItem(
                            type = LicenceItemType.Library,
                            title = "Multiplatform Markdown Renderer",
                            subtitle = "A Kotlin Multiplatform Markdown Renderer powered by Compose Multiplatform",
                            url = LicenceUrls.MULTIPLATFORM_MARKDOWN_RENDERER,
                        )
                    this +=
                        LicenceItem(
                            type = LicenceItemType.Library,
                            title = "Multiplatform Settings",
                            subtitle = """
                                    A Kotlin library for Multiplatform apps, so that common code can persist
                                    key-value data
                            """.trimIndent(),
                            url = LicenceUrls.SETTINGS,
                        )
                    this +=
                        LicenceItem(
                            type = LicenceItemType.Library,
                            title = "Reorderable",
                            subtitle = """
                                    A simple library that allows you to reorder items in Jetpack Compose with drag
                                    and drop
                            """.trimIndent(),
                            url = LicenceUrls.REORDERABLE,
                        )
                    this +=
                        LicenceItem(
                            type = LicenceItemType.Library,
                            title = "SQLCipher",
                            subtitle = """
                                    A library that provides transparent, secure 256-bit AES encryption of SQLite
                                     database files
                            """.trimIndent(),
                            url = LicenceUrls.SQLCIPHER,
                        )
                    this +=
                        LicenceItem(
                            type = LicenceItemType.Library,
                            title = "SQLDelight",
                            subtitle = "SQLDelight generates typesafe Kotlin APIs from your SQL statements",
                            url = LicenceUrls.SQLDELIGHT,
                        )
                },
            )
        }
    }
}
