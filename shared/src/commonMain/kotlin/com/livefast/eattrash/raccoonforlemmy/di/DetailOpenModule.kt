package com.livefast.eattrash.raccoonforlemmy.di

import com.livefast.eattrash.raccoonforlemmy.core.commonui.detailopener.api.DetailOpener
import com.livefast.eattrash.raccoonforlemmy.core.commonui.detailopener.impl.DefaultDetailOpener
import com.livefast.eattrash.raccoonforlemmy.core.navigation.NavigationCoordinator
import com.livefast.eattrash.raccoonforlemmy.domain.identity.repository.IdentityRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.CommunityRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.LemmyItemCache
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
internal class DetailOpenModule {
    @Single
    fun provideDetailOpener(
        navigationCoordinator: NavigationCoordinator,
        itemCache: LemmyItemCache,
        identityRepository: IdentityRepository,
        communityRepository: CommunityRepository,
    ): DetailOpener =
        DefaultDetailOpener(
            navigationCoordinator = navigationCoordinator,
            itemCache = itemCache,
            identityRepository = identityRepository,
            communityRepository = communityRepository,
        )
}
