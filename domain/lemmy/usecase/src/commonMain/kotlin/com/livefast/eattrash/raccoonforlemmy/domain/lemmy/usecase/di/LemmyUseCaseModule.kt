package com.livefast.eattrash.raccoonforlemmy.domain.lemmy.usecase.di

import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.usecase.DefaultGetSiteSupportsHiddenPostsUseCase
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.usecase.DefaultGetSiteSupportsMediaListUseCase
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.usecase.DefaultGetSortTypesUseCase
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.usecase.DefaultIsSiteVersionAtLeastUseCase
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.usecase.GetSiteSupportsHiddenPostsUseCase
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.usecase.GetSiteSupportsMediaListUseCase
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.usecase.GetSortTypesUseCase
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.usecase.IsSiteVersionAtLeastUseCase
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

val lemmyUseCaseModule = DI.Module("LemmyUseCaseModule") {
    bind<GetSiteSupportsHiddenPostsUseCase> {
        singleton {
            DefaultGetSiteSupportsHiddenPostsUseCase(
                isSiteVersionAtLeastUseCase = instance(),
            )
        }
    }
    bind<GetSiteSupportsMediaListUseCase> {
        singleton {
            DefaultGetSiteSupportsMediaListUseCase(
                isSiteVersionAtLeastUseCase = instance(),
            )
        }
    }
    bind<GetSortTypesUseCase> {
        singleton {
            DefaultGetSortTypesUseCase(
                isSiteVersionAtLeastUseCase = instance(),
            )
        }
    }
    bind<IsSiteVersionAtLeastUseCase> {
        singleton {
            DefaultIsSiteVersionAtLeastUseCase(
                siteRepository = instance(),
            )
        }
    }
}
