package com.github.diegoberaldin.raccoonforlemmy.feature_home.di

import com.github.diegoberaldin.raccoonforlemmy.feature_home.viewmodel.HomeScreenModel
import org.koin.core.module.Module

expect val homeTabModule: Module

expect fun getHomeScreenModel(): HomeScreenModel