package com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.di

import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.DefaultFabNestedScrollConnection
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.FabNestedScrollConnection
import org.koin.dsl.module

val lemmyUiModule =
    module {
        factory<FabNestedScrollConnection> {
            DefaultFabNestedScrollConnection()
        }
    }
