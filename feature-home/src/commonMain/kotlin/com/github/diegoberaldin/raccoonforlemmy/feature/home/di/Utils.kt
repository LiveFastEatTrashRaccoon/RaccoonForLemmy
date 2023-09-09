package com.github.diegoberaldin.raccoonforlemmy.feature.home.di

import com.github.diegoberaldin.raccoonforlemmy.feature.home.postlist.BottomNavBarCoordinator
import com.github.diegoberaldin.raccoonforlemmy.feature.home.postlist.PostListViewModel

expect fun getHomeScreenModel(): PostListViewModel

expect fun getBottomNavCoordinator(): BottomNavBarCoordinator