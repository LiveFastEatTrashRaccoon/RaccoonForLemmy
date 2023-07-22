package com.github.diegoberaldin.raccoonforlemmy.feature_home

import org.koin.core.module.Module

expect val homeTabModule: Module

expect fun getHomeScreenModel(): HomeScreenModel