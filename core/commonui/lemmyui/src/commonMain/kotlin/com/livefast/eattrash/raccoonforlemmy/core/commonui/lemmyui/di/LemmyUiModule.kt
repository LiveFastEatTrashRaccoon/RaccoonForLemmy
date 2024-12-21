package com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.di

import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.DefaultFabNestedScrollConnection
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.FabNestedScrollConnection
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton

val lemmyUiModule =
    DI.Module("LemmyUiModule") {
        bind<FabNestedScrollConnection> {
            singleton {
                DefaultFabNestedScrollConnection()
            }
    }
}
