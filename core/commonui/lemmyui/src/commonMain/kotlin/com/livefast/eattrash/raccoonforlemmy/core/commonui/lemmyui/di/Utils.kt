package com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.di

import androidx.compose.ui.platform.UriHandler
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.CustomUriHandler
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.FabNestedScrollConnection

expect fun getFabNestedScrollConnection(): FabNestedScrollConnection

expect fun getCustomUriHandler(fallbackUriHandler: UriHandler): CustomUriHandler
