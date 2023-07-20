package com.github.diegoberaldin.raccoonforlemmy.feature_example

import org.koin.core.module.Module
import org.koin.dsl.module
import platform.UIKit.UIDevice

class IOSPlatform : Platform {
    override val name: String =
        UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

