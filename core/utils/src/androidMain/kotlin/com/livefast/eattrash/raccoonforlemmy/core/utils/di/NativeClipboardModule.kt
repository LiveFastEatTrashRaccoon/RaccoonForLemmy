package com.livefast.eattrash.raccoonforlemmy.core.utils.di

import androidx.compose.ui.platform.Clipboard
import com.livefast.eattrash.raccoonforlemmy.core.utils.clipboard.ClipboardHelper
import com.livefast.eattrash.raccoonforlemmy.core.utils.clipboard.DefaultClipboardHelper
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.factory
import org.kodein.di.instance
import org.kodein.di.provider
import org.kodein.di.singleton

actual val nativeClipboardModule = DI.Module("NativeClipboardModule") {
    bind<ClipboardHelper> {
        factory<Any, Clipboard, ClipboardHelper> { clipboard: Clipboard ->
            DefaultClipboardHelper(
                clipboard = clipboard,
                context = instance(),
            )
        }
    }
}
