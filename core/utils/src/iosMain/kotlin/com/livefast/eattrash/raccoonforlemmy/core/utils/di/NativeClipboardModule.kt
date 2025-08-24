package com.livefast.eattrash.raccoonforlemmy.core.utils.di

import androidx.compose.ui.platform.Clipboard
import com.livefast.eattrash.raccoonforlemmy.core.utils.clipboard.ClipboardHelper
import com.livefast.eattrash.raccoonforlemmy.core.utils.clipboard.DefaultClipboardHelper
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.factory

actual val nativeClipboardModule = DI.Module("NativeClipboardModule") {
    bind<ClipboardHelper> {
        factory<Any, Clipboard, ClipboardHelper> { clipboard: Clipboard ->
            DefaultClipboardHelper(clipboard = clipboard)
        }
    }
}
