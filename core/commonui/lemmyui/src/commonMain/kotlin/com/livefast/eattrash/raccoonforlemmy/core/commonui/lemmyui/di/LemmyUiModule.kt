package com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.di

import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.CustomUriHandler
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.DefaultCustomUriHandler
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.DefaultFabNestedScrollConnection
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.FabNestedScrollConnection
import org.koin.dsl.module

val lemmyUiModule =
    module {
        factory<FabNestedScrollConnection> {
            DefaultFabNestedScrollConnection()
        }
        single<CustomUriHandler> { params ->
            DefaultCustomUriHandler(
                fallbackHandler = params[0],
                settingsRepository = get(),
                detailOpener = get(),
                customTabsHelper = get(),
            )
        }
    }
