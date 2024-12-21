package com.livefast.eattrash.raccoonforlemmy.core.utils.di

import com.livefast.eattrash.raccoonforlemmy.core.utils.fs.DefaultFileSystemManager
import com.livefast.eattrash.raccoonforlemmy.core.utils.fs.FileSystemManager
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

internal actual val nativeFileSystemModule =
    DI.Module("NativeFileSystemModule") {
        bind<FileSystemManager> {
            singleton {
                DefaultFileSystemManager(context = instance())
            }
        }
    }
